import java.util.Random;
/** klasa zawierająca klocki do gry i metody ich wyboru
 *@author Pawel Jamiolkowski
 */
public class Klocek
{
    /** tablica zawierając aktualny klocek do gry */
    private boolean[][] tabAktualnyKlocek = new boolean[4][4];
    /** tablica zawierając następny klocek do gry */
    private boolean[][] tabNastepnyKlocek = new boolean[4][4];
    /** zmienna określająca rodzaj aktualnego klocka */
    private int ktKlocek;
    /** zmienna określająca rodzaj nastepnego klocka */
    private int ktKlocekNast;
    private Random losuj = new Random();            //losowanie klocka
    /** tablica przechowująca wszystkie możliwe klocki w grze */
    private static final boolean[][][] KLOCKI = {
            {
                    {true,  false, false, false},   //klocek "J"
                    {true,  true,  true,  false},
                    {false, false, false, false},
                    {false, false, false, false},
            },
            {
                    {false, false, true,  false},   //klocek "L"
                    {true,  true,  true,  false},
                    {false, false, false, false},
                    {false, false, false, false},
            },
            {
                    {false, true,  false, false},   //klocek "T"
                    {true,  true,  false, false},
                    {false, true,  false, false},
                    {false, false, false, false},
            },
            {
                    {true,  true,  true,   true},   //klocek "----"
                    {false, false, false, false},
                    {false, false, false, false},
                    {false, false, false, false},
            },
            {
                    {false, true,  true,  false},   //klocek "O",
                    {false, true,  true,  false},
                    {false, false, false, false},
                    {false, false, false, false},
            },
            {
                    {true,  true,  false, false},   //klocek "z",
                    {false, true,  true,  false},
                    {false, false, false, false},
                    {false, false, false, false},
            },
            {
                    {false, true,  true,  false},   //klocek "s",
                    {true,  true,  false, false},
                    {false, false, false, false},
                    {false, false, false, false},
            }
    };

    public Klocek() {  //konstruktor
        losujKlocek();
        losujKlocekNast();
    }
    /**metoda klasy Klocek losująca aktualny klocek do gry
     *@return metoda nie zwraca żadnej wartości
     */
    public void losujKlocek() {                 //metoda losująca klocek
        ktKlocek = losuj.nextInt(7);            //wybór 1 z 7 klocków
        wybierzRodzajKlocka(tabAktualnyKlocek, ktKlocek);
    }
    /**metoda klasy Klocek losująca następny klocek do gry
     *@return metoda nie zwraca żadnej wartości
     */
    public void losujKlocekNast() {             //losowanie nastęnego klocka do gry
        ktKlocekNast = losuj.nextInt(7);
        wybierzRodzajKlocka(tabNastepnyKlocek, ktKlocekNast);
    }
    /**metoda przepisująca do tablicy wylocsowany klocek
     *@return metoda nie zwraca żadnej wartości
     */

    private void wybierzRodzajKlocka(boolean[][] tab, int ktKlocek) {       //metoda przypisujaca do tablicy tabAktualnyKlocek wylosowany klocek
        for(int x =0; x < 4; x++)
            for(int y =0; y <4; y++) {
                tab[y][x] = KLOCKI[ktKlocek][x][y];
            }
    }
    /**metoda klasy Klocek losująca aktualny klocek do gry
     *@return metoda nie zwraca żadnej wartości
     */
    public boolean zwrocTab(int x, int y) {
        return tabAktualnyKlocek[x][y];
    }
    /**metoda klasy Klocek pozwaląjąca modyfikować tablicę tabAktualnyKlocek
     *@return metoda nie zwraca żadnej wartości
     */
    public void pobierzTab(int x, int y, boolean wartosc) {
        tabAktualnyKlocek[x][y] = wartosc;
    }
    /**metoda klasy Klocek zwracająca tablicę tabAktualnyKlocek
     *@return metoda zwraca wartość boolean
     */
    public boolean zwrocTabNastepny(int x, int y) {
        return tabNastepnyKlocek[x][y];
    }
    /**metoda klasy Klocek zwracająca zmienną ktKlocek
     *@return metoda zwraca wartość int
     */
    public int zwrocktKlocek() {
        return ktKlocek;
    }
    /**metoda klasy Klocek pozwaląjąca modyfikować zmienną ktKlocek
     *@return metoda nie zwraca żadnej wartości
     */
    public void pobierzktKlocek(int wartosc) {
        ktKlocek = wartosc;
    }
    /**metoda klasy Klocek zwracająca zmienną ktKloceknast
     *@return metoda zwraca wartość int
     */
    public int zwrocktKlocekNast() {
        return ktKlocekNast;
    }

}
