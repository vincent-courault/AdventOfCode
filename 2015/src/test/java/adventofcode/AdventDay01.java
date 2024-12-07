package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay01 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(-1, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(138, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(11, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1771, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat;

        int countPlus = (int) inputs.getFirst().chars().filter(ch -> ch == '(').count();
        int countMoins = (int) inputs.getFirst().chars().filter(ch -> ch == ')').count();

        resultat = countPlus - countMoins;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;
        int position = 0;
        String input = inputs.getFirst();
        for (int i = 0; i < input.length(); i++) {
            char valeur = input.charAt(i);
            if (valeur == '(')
                position ++;
            if (valeur==')')
                position--;
            if (position ==-1) {
                resultat = i + 1;
                break;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
