import javax.sound.sampled.*;
import java.net.URL;
/** klasa odpowiadająca za dźwięki podczas gry
 *@author Pawel Jamiolkowski
 */
public class Dzwiek {
    /** obiekt odpowiedzialne za załadowanie dźwięku */
    Clip clip;
    /**konstruktor
     **@param nazwa String pliku wejsciowego
     *@return metoda nie zwraca żadnej wartości
     */
    Dzwiek(String nazwa) {  //konstruktor
        try {
            URL url = Dzwiek.class.getResource(nazwa);  //zmienna URL zawierająca ścieżkę pliku dźwiękowego
            AudioInputStream strumien = AudioSystem.getAudioInputStream(url);   //tworzenie strumienia wejściowego
            clip =  AudioSystem.getClip();
            clip.open(strumien);  //załadowanie dźwięku
        } catch (NullPointerException npe) {
        } catch (Exception e) {
        }
    }
    /**metoda klasy Dźwięk odtwarzająca muzykę
     *@return metoda nie zwraca żadnej wartości
     */
    public void start() {
        clip.setFramePosition(0);       //przewinięcie do początku odtwarzania
        clip.start();                   //uruchomienie odtwarzania
    }
}
