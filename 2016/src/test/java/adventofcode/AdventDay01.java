package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay01 extends Commun {

    private static int distance(int x, int y) {
        return Math.abs(x) + Math.abs(y);
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(8, traitement(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(242, traitement(inputs, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4, traitement(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(150, traitement(inputs, true));
    }

    public int traitement(List<String> inputs, boolean etape2) {
        int resultat;
        String[] commandes = inputs.getFirst().split(", ");
        int x = 0, y = 0;
        int dx = 0, dy = 1;
        boolean trouve = false;
        Set<String> positions = new HashSet<>();
        for (String commande : commandes) {
            if (trouve) {
                break;
            }
            char dir = commande.charAt(0);
            int nbPas = Integer.parseInt(commande.substring(1));
            int rotationDirection = (dir == 'R') ? 1 : -1;
            int temp = dx;
            dx = rotationDirection * dy;
            dy = (-rotationDirection) * temp;

            for (int step = 0; step < nbPas; step++) {
                x += dx;
                y += dy;
                if (etape2) {
                    String positionActuelle = x + "," + y;
                    if (positions.contains(positionActuelle)) {
                        trouve = true;
                        break;
                    } else {
                        positions.add(positionActuelle);
                    }
                }
            }
        }
        resultat = distance(x, y);
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
