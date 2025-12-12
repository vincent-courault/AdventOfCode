package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay12 extends Commun {

    //@Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(0, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(519, traitement(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;

        int[] cadeaux = new int[inputs.stream().filter(String::isEmpty).toList().size()];
        int indexCadeau = 0;
        int possible = 0;
        for (String input : inputs) {
            if (input.contains("#")) {
                cadeaux[indexCadeau] += input.replace(".", "").length();
            }
            if (input.isEmpty()) {
                indexCadeau++;
            }
            if (input.contains("x")) {
                String[] vals = input.split(":");
                int longueur = Integer.parseInt(vals[0].split("x")[0]);
                int largeur = Integer.parseInt(vals[0].split("x")[1]);
                String[] nums = vals[1].split(" ");
                int nbDiese = IntStream.range(1, nums.length).map(j -> Integer.parseInt(nums[j]) * cadeaux[j - 1]).sum();
                if (nbDiese < longueur * largeur) {
                    possible++;
                }
            }
        }
        resultat = possible;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
