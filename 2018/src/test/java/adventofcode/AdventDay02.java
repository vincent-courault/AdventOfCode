package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay02 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(12, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(4920, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true, 2);
        assertEquals("fgij", traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("fonbwmjquwtapeyzikghtvdxl", traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat;
        int troisfois = 0;
        int deuxfois = 0;
        for (String input : inputs) {
            Map<Character, Integer> freq = new TreeMap<>();

            for (char c : input.toCharArray()) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }

            for (int count : freq.values()) {
                if (count == 3) {
                    troisfois++;
                    break;
                }
            }
            for (int count : freq.values()) {
                if (count == 2) {
                    deuxfois++;
                    break;
                }
            }
        }
        resultat = deuxfois * troisfois;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public String traitement2(List<String> inputs) {
        String resultat = "";
        int size = inputs.size();

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                String a = inputs.get(i);
                String b = inputs.get(j);

                if (differeDUnCaractere(a, b)) {
                    resultat= renvoieLaPartieCommune(a,b);
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
    public  boolean differeDUnCaractere(String a, String b) {
        int diff = 0;
        for (int k = 0; k < a.length(); k++) {
            if (a.charAt(k) != b.charAt(k)) {
                diff++;
                if (diff > 1) return false;
            }
        }
        return true;
    }

    public String renvoieLaPartieCommune(String a, String b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(i)) {
                sb.append(a.charAt(i));
            }
        }
        return sb.toString();
    }
}
