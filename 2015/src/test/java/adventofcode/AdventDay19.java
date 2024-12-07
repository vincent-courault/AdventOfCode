package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay19 extends Commun {


    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(509, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(195, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;

        String input = inputs.getLast();
        List<String[]> replacements = new ArrayList<>();
        for (int i = 0; i < inputs.size() - 2; i++) {
            replacements.add(inputs.get(i).split(" => "));
        }

        Set<String> replaced = new HashSet<>();
        for (String[] replacement : replacements) {
            for (int i = 0; i < input.length(); i++) {
                if (input.startsWith(replacement[0], i)) {
                    replaced.add(input.substring(0, i) + replacement[1] + input.substring(i + replacement[0].length()));
                }
            }
        }
        resultat = replaced.size();

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat;
        String target = inputs.getLast();
        List<String[]> replacements = new ArrayList<>();
        for (int i = 0; i < inputs.size() - 2; i++) {
            replacements.add(inputs.get(i).split(" => "));
        }
        replacements.add(new String[]{"e", "H"});
        replacements.add(new String[]{"e", "O"});
        String input;
        //on aprt du résultat et on va appliquer les remplacements inverses
        // on mélange les remplacements pour arriver à la solution qui fonctionne
        while (true) {
            input = target;
            Collections.shuffle(replacements); // ouch
            int count = 0;
            while (true) {
                int startLength = input.length();
                for (String[] replacement : replacements) {
                    if (input.contains(replacement[1])) {
                        input = input.replaceFirst(replacement[1], replacement[0]);
                        count++;
                    }
                }
                if (input.equals("e") || startLength == input.length()) {
                    break; // soit on a trouvé soit on a échoué
                }
            }
            if (input.equals("e")) {
                resultat = count;
                break;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
