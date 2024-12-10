package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay10 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(36, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(816, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(81, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1960, traitement(inputs, false));
    }


    public int calculeLeScoreOuLeRating(Grid<Character> carte, Coord start, boolean etape1) {
        Set<Coord> visited = new HashSet<>();
        Queue<Coord> queue = new ArrayDeque<>();
        queue.add(start);
        var score = 0;

        while (!queue.isEmpty()) {
            Coord coord = queue.poll();
            if (etape1) {
                if (visited.contains(coord)) continue;
                visited.add(coord);
            }
            if (carte.get(coord.ligne, coord.colonne) == '9') {
                score++;
                continue;
            }

            Coord possible = coord.deplace(Direction.N);
            if (carte.isValid(possible.ligne, possible.colonne) && carte.get(possible.ligne, possible.colonne) == carte.get(coord.ligne, coord.colonne) + 1) {
                queue.add(possible);
            }
            possible = coord.deplace(Direction.S);
            if (carte.isValid(possible.ligne, possible.colonne) && carte.get(possible.ligne, possible.colonne) == carte.get(coord.ligne, coord.colonne) + 1) {
                queue.add(possible);
            }
            possible = coord.deplace(Direction.E);
            if (carte.isValid(possible.ligne, possible.colonne) && carte.get(possible.ligne, possible.colonne) == carte.get(coord.ligne, coord.colonne) + 1) {
                queue.add(possible);
            }
            possible = coord.deplace(Direction.W);
            if (carte.isValid(possible.ligne, possible.colonne) && carte.get(possible.ligne, possible.colonne) == carte.get(coord.ligne, coord.colonne) + 1) {
                queue.add(possible);
            }
        }
        return score;
    }

    public int traitement(List<String> inputs, boolean etape1) {
        int resultat = 0;
        Grid<Character> carte = new Grid<>(inputs, new Divider.Character());
        List<Coord> departs = identifieLesDeparts(carte);
        // L'algo est de type BFS (parcours en largeur)
        // Pour l'étape 1 on calcule le fait d'arriver à atteindre le plus haut depuis le départ
        // Pour l'étape 2 on calcule le nomrbe de trajets pour l'atteindre
        // Donc dans l'étape 1 on mémorise le fait d'être passé à un endroit pas dans l'étape 2
        for (Coord depart : departs) {
            resultat += calculeLeScoreOuLeRating(carte, depart, etape1);
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static List<Coord> identifieLesDeparts(Grid<Character> carte) {
        List<Coord> departs = new ArrayList<>();
        for (int numLigne = 0; numLigne < carte.getHeight(); numLigne++) {
            for (int numColonne = 0; numColonne < carte.getWidth(); numColonne++) {
                char antenne = carte.get(numLigne, numColonne);
                if (antenne == '0') {
                    departs.add(new Coord(numLigne, numColonne));
                }
            }
        }
        return departs;
    }
}
