package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay10 extends Commun {

    private static String getValeurSuivante(String valeurInitiale) {
        int nb = 1;
        StringBuilder valeurSuivante = new StringBuilder();
        char prev = valeurInitiale.charAt(0);
        for (int i = 1; i < valeurInitiale.length(); i++) {
            if (valeurInitiale.charAt(i) == prev) {
                nb++;
            } else {
                valeurSuivante.append(nb).append(prev);
                nb = 1;
                prev = valeurInitiale.charAt(i);
            }
        }
        valeurSuivante.append(nb).append(prev);
        return valeurSuivante.toString();
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(6, traitement(inputs, 5));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(360154, traitement(inputs, 40));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(5103798, traitement(inputs, 50));
    }

    public int traitement(List<String> inputs, int cycle) {
        int resultat;
        String valeurInitiale = inputs.getFirst();
        int compteur = 0;
        while (compteur < cycle) {
            valeurInitiale = getValeurSuivante(valeurInitiale);
            compteur++;
        }
        resultat = valeurInitiale.length();
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
