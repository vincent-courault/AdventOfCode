package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay03 extends Commun {


    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2081, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3, traitementEtape2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2341, traitementEtape2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat;

        HashSet<String> positionsVisitees = new HashSet<>();
        positionsVisitees.add("0,0");
        int x = 0;
        int y = 0;
        String input = inputs.getFirst();

        for (int i = 0; i < input.length(); i++) {
            switch (input.charAt(i)) {
                case '>':
                    x++;
                    break;
                case 'v':
                    y--;
                    break;
                case '<':
                    x--;
                    break;
                case '^':
                    y++;
                    break;
            }
            positionsVisitees.add("" + x + ',' + y);
        }
        resultat = positionsVisitees.size();

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitementEtape2(List<String> inputs) {
        int resultat;

        HashSet<String> positionsVisitees = new HashSet<>();
        positionsVisitees.add("0,0");
        int xSanta = 0;
        int ySanta = 0;
        int xRobot = 0;
        int yRobot = 0;
        String input = inputs.getFirst();

        for (int i = 0; i < input.length(); i++) {
            char valeur = input.charAt(i);
            if (i % 2 == 0) {
                switch (valeur) {
                    case '>':
                        xSanta++;
                        break;
                    case 'v':
                        ySanta--;
                        break;
                    case '<':
                        xSanta--;
                        break;
                    case '^':
                        ySanta++;
                        break;
                }

                positionsVisitees.add("" + xSanta + ',' + ySanta);
            }else{
                switch (valeur) {
                    case '>':
                        xRobot++;
                        break;
                    case 'v':
                        yRobot--;
                        break;
                    case '<':
                        xRobot--;
                        break;
                    case '^':
                        yRobot++;
                        break;
                }

                positionsVisitees.add("" + xRobot + ',' + yRobot);
            }
        }
        resultat = positionsVisitees.size();

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
