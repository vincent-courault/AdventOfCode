package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay01 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(969, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(6, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(5887, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        int position = 50;

        for (String input : inputs) {
            if (position == 0) {
                resultat++;
            }
            int increment = Integer.parseInt(input.substring(1));
            if (input.charAt(0) == 'L') {
                position = (position - increment) % 100;
                if (position < 0) {
                    position = position + 100;
                }
            } else {
                position = (position + increment) % 100;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;
        int position = 50;

        for (String input : inputs) {
            int increment = Integer.parseInt(input.substring(1));
            if (input.charAt(0) == 'L') {
                for (int i = 0; i < increment; i++) {
                    position--;
                    if (position == 0) {
                        resultat++;
                    }
                    if (position < 0) {
                        position = position + 100;
                    }
                }
            } else {
                for (int i = 0; i < increment; i++) {
                    position++;
                    if (position == 100) {
                        resultat++;
                        position = 0;
                    }
                }
            }
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
