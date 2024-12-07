package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay08 extends Commun {


    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(12, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1342, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(19, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2074, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;

        for (String input : inputs) {
            String inputDecoded = input.substring(1, input.length() - 1); //guillemets de début et fin
            inputDecoded = inputDecoded.replaceAll("\\\\\\\\", "_"); // double backslash
            inputDecoded = inputDecoded.replaceAll("\\\\\"", "_"); //guillemet
            inputDecoded = inputDecoded.replaceAll("\\\\x[0-9a-f][0-9a-f]", "_"); //Valeur hexa

            resultat += (input.length() - (inputDecoded.length()));
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;

        for (String input : inputs) {
            String inputEncoded = input;
            inputEncoded = inputEncoded.replaceAll("[\"]", "__"); // on remplace guillemet par \"
            inputEncoded = inputEncoded.replaceAll("[\\\\]", "__"); // on remplace \ par double backslash
            inputEncoded = "\"" + inputEncoded + "\""; //on rajoute des guillemets en début et fin
            resultat += (inputEncoded.length() - input.length());
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
