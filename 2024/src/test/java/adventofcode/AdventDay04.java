package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay04 extends Commun {


    static int cherche2MAS(Grid<Character> grille, int ligne, int colonne) {
        int mas = 0;
        if (grille.isValid(ligne - 1, colonne - 1)
                && grille.isValid(ligne + 1, colonne + 1)) {
            // les 2 M en haut
            if (grille.get(ligne - 1, colonne - 1) == 'M' &&
                    grille.get(ligne - 1, colonne + 1) == 'M' &&
                    grille.get(ligne + 1, colonne - 1) == 'S' &&
                    grille.get(ligne + 1, colonne + 1) == 'S') {
                mas++;
            }
            // Ms à gauche
            if (grille.get(ligne - 1, colonne - 1) == 'M' &&
                    grille.get(ligne - 1, colonne + 1) == 'S' &&
                    grille.get(ligne + 1, colonne - 1) == 'M' &&
                    grille.get(ligne + 1, colonne + 1) == 'S') {
                mas++;
            }
            // M en bas
            if (grille.get(ligne - 1, colonne - 1) == 'S' &&
                    grille.get(ligne - 1, colonne + 1) == 'S' &&
                    grille.get(ligne + 1, colonne - 1) == 'M' &&
                    grille.get(ligne + 1, colonne + 1) == 'M') {
                mas++;
            }
            // M à droite
            if (grille.get(ligne - 1, colonne - 1) == 'S' &&
                    grille.get(ligne - 1, colonne + 1) == 'M' &&
                    grille.get(ligne + 1, colonne - 1) == 'S' &&
                    grille.get(ligne + 1, colonne + 1) == 'M') {
                mas++;
            }
        }
        return mas;
    }

    static int chercheXMAS(Grid<Character> grille, int ligne, int colonne) {
        int xmas = 0;
        // Vers le haut
        if (grille.isValid(ligne - 3, colonne)) {
            if (grille.get(ligne - 1, colonne) == 'M' &&
                    grille.get(ligne - 2, colonne) == 'A' &&
                    grille.get(ligne - 3, colonne) == 'S') {
                xmas++;
            }
        }
        // Vers le bas
        if (grille.isValid(ligne + 3, colonne)) {
            if (grille.get(ligne + 1, colonne) == 'M' &&
                    grille.get(ligne + 2, colonne) == 'A' &&
                    grille.get(ligne + 3, colonne) == 'S') {
                xmas++;
            }
        }
        // Vers la gauche
        if (grille.isValid(ligne, colonne - 3)) {
            if (grille.get(ligne, colonne - 1) == 'M' &&
                    grille.get(ligne, colonne - 2) == 'A' &&
                    grille.get(ligne, colonne - 3) == 'S') {
                xmas++;
            }
        }
        // Vers la droite
        if (grille.isValid(ligne, colonne + 3)) {
            if (grille.get(ligne, colonne + 1) == 'M' &&
                    grille.get(ligne, colonne + 2) == 'A' &&
                    grille.get(ligne, colonne + 3) == 'S') {
                xmas++;
            }
        }
        // En haut à gauche
        if (grille.isValid(ligne - 3, colonne - 3)) {
            if (grille.get(ligne - 1, colonne - 1) == 'M' &&
                    grille.get(ligne - 2, colonne - 2) == 'A' &&
                    grille.get(ligne - 3, colonne - 3) == 'S') {
                xmas++;
            }
        }
        // En haut à droite
        if (grille.isValid(ligne - 3, colonne + 3)) {
            if (grille.get(ligne - 1, colonne + 1) == 'M' &&
                    grille.get(ligne - 2, colonne + 2) == 'A' &&
                    grille.get(ligne - 3, colonne + 3) == 'S') {
                xmas++;
            }
        }
        // En bas à droite
        if (grille.isValid(ligne + 3, colonne + 3)) {
            if (grille.get(ligne + 1, colonne + 1) == 'M' &&
                    grille.get(ligne + 2, colonne + 2) == 'A' &&
                    grille.get(ligne + 3, colonne + 3) == 'S') {
                xmas++;
            }
        }
        // En bas à gauche
        if (grille.isValid(ligne + 3, colonne - 3)) {
            if (grille.get(ligne + 1, colonne - 1) == 'M' &&
                    grille.get(ligne + 2, colonne - 2) == 'A' &&
                    grille.get(ligne + 3, colonne - 3) == 'S') {
                xmas++;
            }
        }
        return xmas;
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(18, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2464, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(9, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1982, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        Grid<Character> grille = new Grid<>(inputs, new Divider.Character());

        for (int ligne = 0; ligne < grille.getHeight(); ligne++) {
            for (int colonne = 0; colonne < grille.getWidth(); colonne++) {
                if (grille.get(ligne, colonne) == 'X') {
                    resultat += chercheXMAS(grille, ligne, colonne);
                }
            }
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;
        Grid<Character> grille = new Grid<>(inputs, new Divider.Character());

        for (int ligne = 0; ligne < grille.getHeight(); ligne++) {
            for (int colonne = 0; colonne < grille.getWidth(); colonne++) {
                if (grille.get(ligne, colonne) == 'A') {
                    resultat += cherche2MAS(grille, ligne, colonne);
                }
            }
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
