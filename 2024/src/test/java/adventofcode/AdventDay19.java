package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay19 extends Commun {
    Map<String, Long> cache = new HashMap<>();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(6, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(278, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(16, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(569808947758890L, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean etape1) {
        long resultat;
        List<String> patterns = Arrays.stream(inputs.getFirst().split(","))
                .map(String::trim).toList();
        List<String> designs = inputs.subList(2, inputs.size());
        List<Long> resultats = designs.stream().
                map(design -> realiseLeDesign(patterns, design)).toList();

        if (etape1) {
            resultat = resultats.stream().filter(r -> r > 0).count();
        } else {
            resultat = resultats.stream().mapToLong(r -> r).sum();
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private long realiseLeDesign(List<String> patterns, String design) {
        if (design.isEmpty()) { // c'est le fait d'être au bout du design qui déclenche le "comptage" récursivement
            return 1;
        }
        //on vérifie si on n'a pas déjà réalisé ce design
        if (cache.containsKey(design)) {
            return cache.get(design);
        }
        long nbMorceaux = 0;
        // on vérifie pour chaque pattern si le design commence par lui
        // si c'est le cas appel récursif sur le design tronqué du pattern
        for (String pattern : patterns) {
            if (design.startsWith(pattern)) {
                nbMorceaux += realiseLeDesign(patterns, design.substring(pattern.length()));
            }
        }
        // on met en cache le résultat
        cache.put(design, nbMorceaux);
        return nbMorceaux;
    }
}
