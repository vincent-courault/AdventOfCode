package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay18 extends Commun {


    private static Grid<Character> metAJour(Grid<Character> grille, boolean etape2) {

        Grid<Character> grille2 = new Grid<>(grille);
        for (int ligne = 0; ligne < grille.getHeight(); ligne++) {
            for (int colonne = 0; colonne < grille.getWidth(); colonne++) {
                int nbVoisinOn = (int) grille.getNeighbours(ligne, colonne).stream().filter(s -> s == '#').count();
                if (grille.get(ligne, colonne) == '#') {
                    if (nbVoisinOn != 2 && nbVoisinOn != 3) {
                        grille2.set(ligne, colonne, '.');
                    }
                } else {
                    if (nbVoisinOn == 3) {
                        grille2.set(ligne, colonne, '#');
                    }
                }
            }
        }
        if (etape2) {
            grille2.set(0, 0, '#');
            grille2.set(0, grille2.getWidth() - 1, '#');
            grille2.set(grille2.getHeight() - 1, grille2.getWidth() - 1, '#');
            grille2.set(grille2.getHeight() - 1, 0, '#');
        }
        return grille2;
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4, traitement(inputs, 6, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1061, traitement(inputs, 100, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(17, traitement(inputs, 5, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1006, traitement(inputs, 100, true));
    }

    public int traitement(List<String> inputs, int nbIteration, boolean etape2) {
        int resultat = 0;
        Grid<Character> grille = new Grid<>(inputs, new Divider.Character());

        if (etape2) {
            grille.set(0, 0, '#');
            grille.set(0, grille.getWidth() - 1, '#');
            grille.set(grille.getHeight() - 1, grille.getWidth() - 1, '#');
            grille.set(grille.getHeight() - 1, 0, '#');
        }
        for (int i = 0; i < nbIteration; i++) {
            grille = metAJour(grille, etape2);
        }
        for (int ligne = 0; ligne < grille.getHeight(); ligne++) {
            for (int colonne = 0; colonne < grille.getWidth(); colonne++) {
                if (grille.get(ligne, colonne) == '#') {
                    resultat++;
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
