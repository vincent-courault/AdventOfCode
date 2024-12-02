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
        assertEquals(11, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2113135, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(31, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(19097157, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        List<Integer> liste1 = new ArrayList<>();
        List<Integer> liste2 = new ArrayList<>();
        for (String input : inputs) {
            String[] valeurs = input.split(" {3}");
            liste1.add(Integer.valueOf(valeurs[0]));
            liste2.add(Integer.valueOf(valeurs[1]));
        }
        Collections.sort(liste1);
        Collections.sort(liste2);
        for (int i = 0; i < liste1.size(); i++) {
            resultat += Math.abs(liste1.get(i) - liste2.get(i));
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;
        List<Integer> liste1 = new ArrayList<>();
        List<Integer> liste2 = new ArrayList<>();
        for (String input : inputs) {
            String[] valeurs = input.split(" {3}");
            liste1.add(Integer.valueOf(valeurs[0]));
            liste2.add(Integer.valueOf(valeurs[1]));
        }

        for (Integer integer : liste1) {
            resultat += Collections.frequency(liste2, integer) * integer;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
