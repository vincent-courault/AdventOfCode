package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay08 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(14, traitement(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(390, traitement(inputs, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(34, traitement(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1246, traitement(inputs, true));
    }

    public int traitement(List<String> inputs, boolean etape2) {
        int resultat;
        Grid<Character> grille = new Grid<>(inputs, new Divider.Character());

        Map<Character, List<Coord>> antennes = new HashMap<>();
        for (int numLigne = 0; numLigne < grille.getHeight(); numLigne++) {
            for (int numColonne = 0; numColonne < grille.getWidth(); numColonne++) {
                char antenne = grille.get(numLigne, numColonne);
                if (antenne != '.') {
                    antennes.putIfAbsent(antenne, new ArrayList<>());
                    antennes.get(antenne).add(new Coord(numLigne, numColonne));
                }
            }
        }

        Set<Coord> antinodes = new HashSet<>();
        for (char c : antennes.keySet()) {
            List<Coord> antenne = antennes.get(c);
            for (int i = 0; i < antenne.size(); i++) {
                for (int j = 0; j < antenne.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    Direction d = antenne.get(i).calculeEcart(antenne.get(j));
                    if (grille.isValid(antenne.get(j).deplace(d))) {
                        antinodes.add(antenne.get(j).deplace(d));
                    }
                    if (etape2) {
                        Coord positionCourante = antenne.get(i);

                        while (grille.isValid(positionCourante.deplace(d))) {
                            antinodes.add(positionCourante.deplace(d));
                            positionCourante = positionCourante.deplace(d);
                        }
                    }
                }
            }
        }
        resultat = antinodes.size();

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
