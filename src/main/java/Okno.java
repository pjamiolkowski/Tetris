import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/** klasa odpowiadająca za okno gry
 *@author Pawel Jamiolkowski
 */
public class Okno extends Thread implements ActionListener
{
    private JFrame okno;
    private Plansza plansza;
    /** etykiety do wyświetlenia wyniku, ilości pełnych linii, poziomu gry */
    private JLabel JWynik, JLinie, JPoziom;
    /** stworzenie obiektu czcionka zawierającego ustawienia czcionki */
    private Font czcionka = new Font("New Courier", Font.BOLD, 18);
    /** zmienna służąca do zatrzymania gry */
    private boolean pauza;
    /** przycisku do zatrzymania gry */
    private JButton przyciskPauza = new JButton("Pauza");
    /** przycisku do rozpoczęcia i resetu gry */
    private JButton przyciskReset = new JButton("Start");
    /** zmienna służąca ustawienia napisu przycisków w okienkach komunikacyjnych */
    private static final String[] opcjeTakNie = { "TAK", "NIE"};

    public Okno()       //konstruktor
    {
        okno = new JFrame("Tetris");            //ustawienie okna
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setSize(450,600);
        okno.setLocationRelativeTo(null);
        okno.setResizable(false);

        JWynik = new JLabel("Wynik: 0", JLabel.LEFT);       //ustawienia etykiety "Wynik"
        JWynik.setForeground(Color.WHITE);
        JWynik.setFont(czcionka);
        JWynik.setBounds(285, 200, 140, 20);

        JLinie = new JLabel("Linie: 0", JLabel.LEFT);       //ustawienia etykiety "Linie"
        JLinie.setForeground(Color.WHITE);
        JLinie.setBounds(285, 240, 120, 20);
        JLinie.setFont(czcionka);

        JPoziom = new JLabel("Poziom: 1", JLabel.LEFT);     //ustawienia etykiety "Poziom"
        JPoziom.setForeground(Color.WHITE);
        JPoziom.setBounds(285, 280, 120, 20);
        JPoziom.setFont(czcionka);

        przyciskPauza.setBounds(285,340,120,30);        //ustawienia przycisku "Pauza"
        przyciskPauza.setEnabled(false);
        przyciskPauza.addActionListener(this);
        przyciskPauza.setFont(czcionka);
        przyciskPauza.setFocusable(false);

        przyciskReset.setBounds(285,390,120,30);        //ustawienia przycisku "Reset"
        przyciskReset.addActionListener(this);
        przyciskReset.setFont(czcionka);
        przyciskReset.setFocusable(false);

        plansza = new Plansza();        //dodanie przycisków i etykiety na planszę
        plansza.add(JWynik);
        plansza.add(JLinie);
        plansza.add(JPoziom);
        plansza.add(przyciskPauza);
        plansza.add(przyciskReset);
        okno.add(plansza);      //dodanie planszy do okna

        okno.setVisible(true);
        pauza = false;
    }
    /**metoda główna klasy Okno
     *@param args tablica argumentów wejściowych
     *@return metoda nie zwraca żadnej wartości
     */
    public static void main(String[] args)
    {

        (new Okno()).start();
    }

    /**metoda run klasy Okno
     *@return metoda nie zwraca żadnej wartości
     */
    public void run()   //metoda run wywoływana przez wątek
    {
        while(true)
        {
            try {
                sleep(50);
                JWynik.setText("Wynik: " + String.valueOf(plansza.ruchKlocka.zwrocWynikPunkty()));      //zktualizuj wyświetlany wynik
                JLinie.setText("Linie: " + String.valueOf(plansza.zwrocWynikLinia()));      //zaktualizuj wyświetlaną ilość pełnych linii
                JPoziom.setText("Poziom: " + String.valueOf(plansza.ruchKlocka.zwrocPoziom())); //zaktualizuj wyświetlany poziom

                while(pauza)        //jeśli wciśnięty przycisk "pauza" to zatrzymaj grę
                    sleep(10);
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            plansza.repaint();      //odśwież planszę
            if(!plansza.ruchKlocka.napotkanyKlocek(plansza.ruchKlocka.zwrocKlocekX(), plansza.ruchKlocka.zwrocKlocekY())) { //sprawdź czy koniec gry
                if(JOptionPane.showOptionDialog(null, " Koniec gry\n Tw\u00f3j wynik: " + plansza.ruchKlocka.zwrocWynikPunkty()
                                + "\n Czy chcesz zacz\u0105\u0107 now\u0105 gr\u0119?", "Pytanie", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, opcjeTakNie, opcjeTakNie[0]) == JOptionPane.YES_OPTION) {
                    plansza.czyscPlansze();
                } else
                    System.exit(0);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getActionCommand().startsWith("Start"))       //sprawdź czy wciśnieto przycisk "Pauza"
        {
            przyciskPauza.setEnabled(true);
            plansza.start = true;
            przyciskReset.setText("Reset");
        }
        if(ae.getActionCommand().startsWith("Pauza"))       //sprawdź czy wciśnieto przycisk "Pauza"
        {
            pauza = true;
            przyciskPauza.setText("Wzn\u00f3w");
        }
        if(ae.getActionCommand().startsWith("Wzn\u00f3w"))  //sprawdź czy wcisnięto przycisk "Wznów"
        {
            pauza = false;
            przyciskPauza.setText("Pauza");
        }
        if(ae.getActionCommand().startsWith("Reset"))       //sprawdź czy wciśnięto przycisk "Reset"
        {
            pauza = true;
            if(JOptionPane.showOptionDialog(null, "Czy chcesz zacz\u0105\u0107 now\u0105 gr\u0119?",     //wyswietl komunika o końcu gry i pyta czy gracz chce zagrac jeszcze raz
                    "Koniec", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, opcjeTakNie, opcjeTakNie[0]) == JOptionPane.YES_OPTION){
                pauza = false;
                plansza.czyscPlansze();
            }
            pauza = false;
        }
    }
}
