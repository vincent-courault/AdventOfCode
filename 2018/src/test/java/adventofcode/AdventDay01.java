package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        assertEquals(420, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(2, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(227, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        for (String input : inputs) {
            resultat += Integer.parseInt(input);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;
        int frequence = 0;
        Set<Integer> frequences = new HashSet<>();
        int index = 0;
        boolean frequenceTrouvee = false;
        while (!frequenceTrouvee) {
            String input = inputs.get(index);
            frequence += Integer.parseInt(input);
            if (frequences.contains(frequence)) {
                resultat = frequence;
                frequenceTrouvee = true;
            } else {
                frequences.add(frequence);
            }
            index++;
            if (index == inputs.size()) {
                index = 0;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
