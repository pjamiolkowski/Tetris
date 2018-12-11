/** klasa zawierająca metody poruszania klocka
 *@author Pawel Jamiolkowski
 */
public class RuchKlocka extends Klocek
{
    /** zmienne określające położenie startowe klocka na planszy */
    private int klocekX = 3, klocekY = 0;
    /** tablica określająca zakres planszy, w której zapisywane są zajęte pola */
    private int[][] tablica= new int [12][22];
    /** dodatkowa tablica wykorzystywana przy obrocie klocka */
    private boolean[][] tabDod = new boolean[4][4];
    /** zmienne ustawiające odpowiednio aktualną predkość, max prędkość i zmienną pomocniczą */
    private int szybkosc, szybkoscMax, ustawSzybkosc;
    /** zmienne służące do zliczania odpowiednio punktów i ustalenia poziomu gry */
    private int wynikPunkty = 0, poziom = 1;
    /** obiekt odpowiedzialne za dźwięk przy zakończeniu ruchu klcka */
    private Dzwiek dzwiekKlocek;

    public RuchKlocka() {       //konstruktor
        szybkoscMax = 20;
        ustawSzybkosc = szybkoscMax;
        ustawGranice();
        dzwiekKlocek = new Dzwiek("/klocek.wav");
    }
    /**metoda klasy RuchKlocka ustaiająca granice planszy do gry
     *@return metoda nie zwraca żadnej wartości
     */
    private void ustawGranice(){        //metoda ustawia granice planszy do gry, co pozwoli na zabezpieczenie przed wychodzeniem klocka poza planszę
        for(int x = 0; x <12; x++) {
            tablica[x][0] = 9;
            tablica[x][21] = 9;
        }
        for(int y = 0; y <22; y++) {
            tablica[0][y] = 9;
            tablica[11][y] = 9;
        }
    }
    /**metoda klasy RuchKlocka sprawdzająca czy klocek napotkał przeszkodę na swojej drodze
     *@return metoda zwraca wartość boolean
     */
    public boolean napotkanyKlocek(int x, int y) {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j <4; j++)
                if(zwrocTab(j, i) && (tablica[x+j+1][y+i+1]>0 && tablica[x+j+1][y+i+1]<10))
                    return false;
        return true;
    }
    /**metoda klasy RuchKlocka odpowiadajaca za obrót klocka
     *@return metoda nie zwraca żadnej wartości
     */
    private void obrot() {
        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 4; y++)
                tabDod[x][y] = zwrocTab(x, y);

        if(zwrocktKlocek()==0 || zwrocktKlocek() == 1 || zwrocktKlocek() == 2) {    //obrót klocka "J", "L", "T"
            for (int x = 0; x < 3; x++)
                for (int y = 0; y < 3; y++)
                    pobierzTab(2 - y, x, tabDod[x][y]);
        }
        if(zwrocktKlocek()==3) {        //obrót klocka "----"
            if(zwrocTab(0,0)) {
                for (int x = 0; x < 4; x++)
                    pobierzTab(x, 0, false);
                for (int y = 0; y < 4; y++)
                    pobierzTab(2, y, true);
            }
            else if(!zwrocTab(0,0)) {
                for (int y = 0; y < 4; y++)
                    pobierzTab(2, y, false);
                for (int x = 0; x < 4; x++)
                    pobierzTab(x, 0, true);
            }
        }
        if(zwrocktKlocek()==5 || zwrocktKlocek()==6) {      //obrót klocka "z", "s"
            if (zwrocTab(0, 0)) {
                for (int x = 0; x < 3; x++)
                    for (int y = 0; y < 3; y++)
                        pobierzTab(2 - y, x, tabDod[x][y]);
            } else {
                for (int x = 0; x < 3; x++)
                    for (int y = 0; y < 3; y++)
                        pobierzTab(x, y, tabDod[2 - y][x]);
            }
        }
        if(!napotkanyKlocek(klocekX, klocekY)) {       // sprawdzenie czy po obrocie klocek nie wyszedł poza planszę lub nie wszedł na inne klocki
            for (int x = 0; x < 4; x++)                  // jeśli tak to wracamy do sytuacji przed obrotem klocka
                for (int y = 0; y < 4; y++)
                    pobierzTab(x, y, tabDod[x][y]);
        }
    }
    /**metoda klasy RuchKlocka wykonująca ruch klocka podczas naciśnięcia odpowiedniego przycisku
     *@return metoda nie zwraca żadnej wartości
     */
    public void nacisnietyPrzycisk(boolean kLewo, boolean kPrawo, boolean kGora, boolean kDol) {
        if(kLewo && napotkanyKlocek(klocekX-1,klocekY))       //wyknanie ruchu w lewo jesli naciśnięto lewą strzałkę i nie ma klocka na drodze
            klocekX--;
        if(kPrawo && napotkanyKlocek(klocekX+1,klocekY))       //wyknanie ruchu w prawo jesli naciśnięto lewą strzałkę i nie ma klocka na drodze
            klocekX++;
        if(kGora && napotkanyKlocek(klocekX, klocekY))              //wyknanie obrotu jesli naciśnięto górną strzałkę i nie ma klocka na drodze
            obrot();
        if(kDol)               //wyknanie szybkiego ruchu w dół jesli naciśnięto dolną strzałkę
            szybkoscMax = 0;
        else
            szybkoscMax = ustawSzybkosc;
    }
    /**metoda klasy RuchKlocka nadająca odpowiednią prędkość klocka w zależności od poziomu gry
     *@return metoda nie zwraca żadnej wartości
     */
    public void szybkoscKlocka() {
        if(szybkosc<szybkoscMax)
            szybkosc++;
        else{
            szybkosc = poziom;
                if (napotkanyKlocek(klocekX, klocekY+1))   // sprawdzenie czy przy opadaniu klocek nie napotkał wypełnionej planszy
                    klocekY++;                           //jeśli nie to poruszaj w dół
                else {                                   //jak napotkał to umieszczamy na stale na planszy i dodajemy punkty
                    wynikPunkty += 20;
                    dzwiekKlocek.start();
                    klocekKoniec();
                    nowyKlocek();
                }
        }
    }
    /**metoda klasy RuchKlocka kończąca ruch klocka
     *@return metoda nie zwraca żadnej wartości
     */
    private void klocekKoniec() {            // metoda w której sterowany klocek po dojściu do końca planszy
        for(int x = 0; x < 4; x++)          // lub napotkaniu klocka na planszu zostaje dodany
            for(int y = 0; y < 4; y++)      // do planszy i nie można nim już ruszać
                if(zwrocTab(x,y)) {
                    tablica[x+ 1 + klocekX][y + klocekY+1] = zwrocktKlocek()+1;
                }
    }
    /**metoda klasy RuchKlocka wywołująca nowy klocek na planszy
     *@return metoda nie zwraca żadnej wartości
     */
    private void nowyKlocek(){           //przeniesienie następnego klocka w kolejnosci
        klocekX = 3;                //ktory został wylosowany na planszę i
        klocekY = 0;                //teraz nim będzie sterował gracz
        for(int x = 0; x <4; x++)
            for(int y = 0; y < 4; y++)
                pobierzTab(x, y, zwrocTabNastepny(x, y));
        pobierzktKlocek(zwrocktKlocekNast());
        losujKlocekNast();      //wylosowanie następnego klocka w kolejności do gry
    }


    /**metoda klasy Klocek zwracająca położenie klocka na planszy w osi X
     *@return metoda zwraca wartość int
     */
    public int zwrocKlocekX() {
        return klocekX;
    }
    /**metoda klasy Klocek zwracająca położenie klocka na planszy w osi Y
     *@return metoda zwraca wartość int
     */
    public int zwrocKlocekY() {
        return klocekY;
    }
    /**metoda klasy Klocek modyfikująca położenie klocka na planszy w osi X
     *@return metoda nie zwraca żadnej wartości
     */
    public void pobierzKlocekX(int klocekX) {
        this.klocekX = klocekX;
    }
    /**metoda klasy Klocek modyfikująca położenie klocka na planszy w osi Y
     *@return metoda nie zwraca żadnej wartości
     */
    public void pobierzKlocekY(int klocekY) {
        this.klocekY = klocekY;
    }
    /**metoda klasy Klocek zwracająca wartość pole na planszy
     *@return metoda zwraca wartość int
     */
    public int zwrocTablica(int x, int y) {
        return tablica[x][y];
    }
    /**metoda klasy Klocek modyfikująca  tablicę określającą zajęte pola na planszy
     *@return metoda nie zwraca żadnej wartości
     */
    public void pobierzTablica(int x, int y, int wartosc) {
        tablica[x][y] = wartosc;
    }
    /**metoda klasy Klocek zwracają wynik gracza
     *@return metoda zwraca wartość int
     */
    public int zwrocWynikPunkty() {
        return wynikPunkty;
    }
    /**metoda klasy Klocek sumująca wynik gracza
     *@return metoda nie zwraca żadnej wartości
     */
    public void pobierzWynikPunkty() {
        wynikPunkty += 100;
    }
    /**metoda klasy Klocek zerująca wynik gracza
     *@return metoda nie zwraca żadnej wartości
     */
    public void zerujWynik() {
        this.wynikPunkty = 0;
    }
    /**metoda klasy Klocek zwracają osiągnięty poziom gry
     *@return metoda zwraca wartość int
     */
    public int zwrocPoziom()
    {
        return poziom;
    }
    /**metoda klasy Klocek zwiększająca poziom gry
     *@return metoda nie zwraca żadnej wartości
     */
    public void pobierzPoziom()
    {
        poziom++;
    }
    /**metoda klasy Klocek resetująca poziom gry
     *@return metoda nie zwraca żadnej wartości
     */
    public void resetujPoziom()
    {
        this.poziom = 1;
    }
}
