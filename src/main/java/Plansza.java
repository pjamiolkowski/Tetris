import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/** klasa odpowiadająca za planszę do gry
 *@author Pawel Jamiolkowski
 */
public class Plansza extends JPanel implements KeyListener  // klasa odpowiadająca za planszę do gry
{
    private Graphics2D g2d;
    /** zmienne określające rozmiar pojedynczego pola planszy */
    private final static int rozmiarKostki = 25;
    /** zmienne określające połozenie planszy względem okna */
    private final static int przesuniecie = 10;
    /** tabica zawierajaca kolory opisujace klocki i tło planszy */
    private final Color[] kolor = {Color.BLACK, Color.YELLOW, Color.RED, Color.ORANGE, Color.GREEN, Color.BLUE, Color.lightGray, Color.MAGENTA, Color.WHITE, Color.BLUE};  //tablica kolorów
    /** zmienne służące do sprawdzenia która strzałka na klawiaturze została naciśnięta */
    private boolean kPrawo, kLewo, kGora, kDol;
    /** zmienna zliczająca wszystkie zapełnione linie na planszy */
    private int wynikLinia;
    /** zmienna przywracająca planszę do stanu pooczątkowego */
    public boolean start = false;
    protected RuchKlocka ruchKlocka = new RuchKlocka();
    /** obiekty odpowiedzialne za dźwięk przy obrcie klocka i zapełnieniu linii */
    private Dzwiek dzwiekObrot, dzwiekLinia;

    public Plansza() {      //konstruktor
        super();
        setBackground(Color.DARK_GRAY);
        this.setLayout(null);
        this.setFocusable(true);
        addKeyListener(this);
        wynikLinia = 0;
        dzwiekObrot = new Dzwiek("/obrot.wav");
        dzwiekLinia = new Dzwiek("/pasek.wav");
    }

    public void paintComponent (Graphics g) {           // metoda rysująca planszę
        g2d = (Graphics2D) g.create();
        super.paintComponent(g2d);
        drukujPlansza();
        if(start) {
            drukKlocka(ruchKlocka.zwrocKlocekX(), ruchKlocka.zwrocKlocekY());
            drukNastepnyKlocek(12, 2);
            ruchKlocka.nacisnietyPrzycisk(kLewo, kPrawo, kGora, kDol);
            if (kGora)
                kGora = false;
            if (kLewo)
                kLewo = false;
            if (kPrawo)
                kPrawo = false;
            ruchKlocka.szybkoscKlocka();
            usunLinie();
            pelnaLinia();
        }

    }
    /**metoda klasy Plansza rysująca planszę do gry
     *@return metoda nie zwraca żadnej wartości
     */
    private void drukujPlansza() {      // metoda drukująca planszę
        for (int x = 1; x < 11; x++)
            for (int y = 1; y < 21; y++) {
                g2d.setColor(Color.BLACK);
                g2d.drawRect(przesuniecie + (x-1) * rozmiarKostki, przesuniecie + (y-1) * rozmiarKostki, rozmiarKostki, rozmiarKostki);
                g2d.setColor(kolor[ruchKlocka.zwrocTablica(x, y)]);
                g2d.fillRect(przesuniecie + (x-1) * rozmiarKostki + 1, przesuniecie + (y-1) * rozmiarKostki + 1, rozmiarKostki - 1, rozmiarKostki - 1);
            }
    }
    /**metoda klasy Plansza przywracająca planszę do gry do stanu początkowego
     *@return metoda nie zwraca żadnej wartości
     */
    public void czyscPlansze() {        //metoda czyszcząca planszę i osiągniete wyniki
        for (int x = 1; x < 11; x++)
            for (int y = 1; y < 21; y++)
                ruchKlocka.pobierzTablica(x, y, 0);
        ruchKlocka.losujKlocek();
        ruchKlocka.losujKlocekNast();
        ruchKlocka.pobierzKlocekY(0);
        ruchKlocka.pobierzKlocekX(3);
        ruchKlocka.zerujWynik();
        zerujWynikLinia();
        ruchKlocka.resetujPoziom();
        kDol = false;
        kGora = false;
        kLewo = false;
        kPrawo = false;
    }
    /**metoda klasy Plansza drukująca klocek na planszy
     *@return metoda nie zwraca żadnej wartości
     */
    private void drukKlocka(int x, int y) {
        for(int i=0; i < 4; i++)
            for(int j=0; j < 4; j++)
                if(ruchKlocka.zwrocTab(i,j))
                    drukKostka(x+i,y+j,ruchKlocka.zwrocktKlocek());

    }
    /**metoda klasy Plansza wyświetlająca następny klocek do gry
     *@return metoda nie zwraca żadnej wartości
     */
    private void drukNastepnyKlocek(int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(285, 25, 137, 125);                //ryswanie tła pod klocek
        for(int i=0; i < 4; i++)
            for(int j=0; j < 4; j++) {
                if (ruchKlocka.zwrocTabNastepny(i, j))  //rysuj kostkę jeśli tablica nie jest pusta
                    drukKostka(x + i, y + j, ruchKlocka.zwrocktKlocekNast());
            }
    }
    /**metoda klasy Plansza zapełniająca pojedyncze pole na planszy
     *@return metoda nie zwraca żadnej wartości
     */
    private void drukKostka(int x, int y, int k) {
        g2d.setColor(kolor[k+1]);
        g2d.fillRect(przesuniecie + x * rozmiarKostki, przesuniecie + y * rozmiarKostki, rozmiarKostki, rozmiarKostki);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(przesuniecie + x * rozmiarKostki, przesuniecie + y * rozmiarKostki, rozmiarKostki, rozmiarKostki);
    }
    /**metoda klasy Plansza sprawdzają czy linia na planszy została zapełniona
     *@return metoda nie zwraca żadnej wartości
     */
    private void pelnaLinia() {     //metoda sprawdzają czy linia na planszy została zapełniona
        int i = 0;
        for(int y=1; y < 21; y++) {
            for (int x=1; x<11; x++)
                if(ruchKlocka.zwrocTablica(x, y)>0)
                    i++;
            if(i == 10) {           //jeśli tak to zmień jej kolor na biały
                for (int x = 1; x < 11; x++)
                    ruchKlocka.pobierzTablica(x, y, 8);
                ruchKlocka.pobierzWynikPunkty();        //dodaj takze punkty za linię
                wynikLinia++;
                if(wynikLinia%15 == 0 && ruchKlocka.zwrocPoziom() < 20)     //co 15 zapełnionych linii zwiększaj poziom(szybkośc) gry
                    ruchKlocka.pobierzPoziom();
            }
            i = 0;
        }
    }
    /**metoda klasy Plansza usuwająca z ekranu zapełnioną linię
     *@return metoda nie zwraca żadnej wartości
     */
    private void usunLinie() {      //metoda usuwa z ekranu zapełnioną linię
        for(int y = 1; y<21; y++) {
            if(ruchKlocka.zwrocTablica(1,y) == 8) {     //jeśli kolor linii jest biały(biały oznacza zapełnioną linię)
                for (int i = y; i > 0; i--)
                    for (int x = 1; x < 11; x++)
                        ruchKlocka.pobierzTablica(x, i, ruchKlocka.zwrocTablica(x, i-1));       //to usuń linię z planszy i obniż do dołu klocki powyżej linii
                for (int x = 1; x < 11; x++)
                    ruchKlocka.pobierzTablica(x, 1, 0);     //dodaj na górze planszy pustą linię
                dzwiekLinia.start();
            }
        }
    }
    /**metoda klasy RuchKlocka sprawdzająca zwracająca ilość zapełnionych linii
     *@return metoda zwraca wartość int
     */
    public int zwrocWynikLinia() {
        return wynikLinia;
    }
    /**metoda klasy Plansza zerująca ilość zapełnionych linii
     *@return metoda nie zwraca żadnej wartości
     */
    public void zerujWynikLinia() {
        this.wynikLinia = 0;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {        //metoda sprawdzajaca który przycisk został naciśnięty na klawiaturze
        int key = e.getKeyCode();
        if(key ==e.VK_UP)       //sprawdza czy górna strzałka została naciśnięta
            kGora = true;
        if(key ==e.VK_DOWN)     //sprawdza czy dolna strzałka została naciśnięta
            kDol = true;
        if(key ==e.VK_RIGHT)    //sprawdza czy prawa strzałka została naciśnięta
            kPrawo = true;
        if(key ==e.VK_LEFT)     //sprawdza czy lewa strzałka została naciśnięta
            kLewo = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {        //metoda sprawdzajaca który przycisk został zwolniony na klawiaturze
        int key = e.getKeyCode();
        if(key ==e.VK_UP) {      //sprawdza czy górna strzałka została zwolniona
            kGora = false;
            dzwiekObrot.start();
        }
        if(key ==e.VK_DOWN)     //sprawdza czy dolna strzałka została zwolniona
            kDol = false;
        if(key ==e.VK_RIGHT)    //sprawdza czy prawa strzałka została zwolniona
            kPrawo = false;
        if(key ==e.VK_LEFT)     //sprawdza czy lewa strzałka została zwolniona
            kLewo = false;
    }
}
