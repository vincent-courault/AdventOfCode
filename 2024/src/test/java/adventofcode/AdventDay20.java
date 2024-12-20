package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay20 extends Commun {
    Grid<Character> carte;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1, traitement(inputs, 64, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1289, traitement(inputs, 100, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(285, traitement(inputs, 50, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(982425, traitement(inputs, 100, false));
    }

    public int traitement(List<String> inputs, int nb, boolean etape1) {
        int resultat = 0;
        carte = new Grid<>(inputs, new Divider.Character());
        Coord debut = carte.getStartingPoint('S');
        Coord fin = carte.getStartingPoint('E');
        List<Coord> coordChemin = parcoursLaCarte(debut, fin);
        if (etape1) {
            resultat = comptageEtape1(nb, coordChemin, resultat);
        } else {
            resultat = comptageEtape2(nb, coordChemin, resultat);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public List<Coord> parcoursLaCarte(Coord depart, Coord fin) {
        List<Coord> chemin = new ArrayList<>();
        Coord suivant = depart;
        chemin.add(depart);
        while (!suivant.equals(fin)) {
            for (Direction direction : Direction.CARDINAL_DIRECTIONS) {
                Coord possible = suivant.deplace(direction);
                if (!chemin.contains(possible) && carte.get(possible) != '#') {
                    chemin.add(possible);
                    suivant = possible;
                    break;
                }
            }
        }
        return chemin;
    }

    private int comptageEtape2(int nb, List<Coord> chemin, int resultat) {
        // on cherche les points du chemin qui sont au plus à 20 de distance
        for (int i = 0; i < chemin.size() - 1; i++) {
            for (int j = i + 1; j < chemin.size(); j++) {
                int ecart = chemin.get(i).distance(chemin.get(j));
                if (ecart <= 20) {
                    int tempsGagne = j - i - ecart;
                    if (tempsGagne >= nb) {
                        resultat++;
                    }
                }
            }
        }
        return resultat;
    }

    private int comptageEtape1(int nb, List<Coord> chemin, int resultat) {
        // pour l'étape 1, on cherche les points dans le chemin qui sont à une distance de 2
        // et sur la même ligne ou la même colonne
        for (int i = 0; i < chemin.size(); i++) {
            for (int j = i + 3; j < chemin.size(); j++) {
                if (chemin.get(i).colonne != chemin.get(j).colonne && chemin.get(i).ligne != chemin.get(j).ligne) {
                    continue;
                }
                if (chemin.get(i).distance(chemin.get(j)) == 2) {
                    int tempsGagne = j - i - 2;
                    if (tempsGagne >= nb) {
                        resultat++;
                    }
                }
            }
        }
        return resultat;
    }
}
