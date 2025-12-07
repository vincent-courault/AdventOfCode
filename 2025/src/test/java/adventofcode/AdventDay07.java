package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay07 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(21, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1585, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(40, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(16716444407407L, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean etape1) {
        long resultat;
        List<Coord> splitters = new ArrayList<>();
        Map<Coord, Long> rayons = new HashMap<>();
        int nbSplitterActivés = 0;
        for (int ligne = 0; ligne < inputs.size(); ligne++) {
            String[] line = inputs.get(ligne).split("");
            for (int colonne = 0; colonne < line.length; colonne++) {
                if (line[colonne].equals("^")) {
                    splitters.add(new Coord(ligne, colonne));
                }
                if (line[colonne].equals("S")) {
                    rayons.put(new Coord(ligne, colonne), 1L);
                }
            }
        }
        int maxLigne = inputs.size();
        while (rayons.keySet().iterator().next().ligne < maxLigne) {
            Map<Coord, Long> oldRayons = new HashMap<>(rayons);
            rayons = new HashMap<>();
            for (Coord rayon : oldRayons.keySet()) {
                Coord prochainePosition = new Coord(rayon.ligne + 1, rayon.colonne);
                if (splitters.contains(prochainePosition)) {
                    rayons.merge(new Coord(rayon.ligne + 1, rayon.colonne + 1), oldRayons.get(rayon), Long::sum);
                    rayons.merge(new Coord(rayon.ligne + 1, rayon.colonne - 1), oldRayons.get(rayon), Long::sum);
                    nbSplitterActivés ++;
                } else {
                    rayons.merge(prochainePosition, oldRayons.get(rayon), Long::sum);
                }
            }
        }
        if (etape1) {
            resultat = nbSplitterActivés;
        } else {
            resultat = rayons.values().stream().mapToLong(val -> val).sum();
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
