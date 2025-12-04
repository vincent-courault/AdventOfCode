package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay04 extends Commun {

     @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(13, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1508, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(43, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(8538, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat;
        Grid<Character> grille = new Grid<>(inputs, new Divider.Character());
        resultat = enleveLesRouleauxEtRetourneLeNombreDEnleve(grille, new Grid<>(grille));

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;
        Grid<Character> grille = new Grid<>(inputs, new Divider.Character());
        Grid<Character> grilleMiseAJour = new Grid<>(inputs, new Divider.Character());

        int nbRouleau = Integer.MAX_VALUE;
        while (nbRouleau > 0) {
            nbRouleau = enleveLesRouleauxEtRetourneLeNombreDEnleve(grille, grilleMiseAJour);
            grille = grilleMiseAJour;
            grilleMiseAJour = new Grid<>(grilleMiseAJour);
            resultat += nbRouleau;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
    private int enleveLesRouleauxEtRetourneLeNombreDEnleve(Grid<Character> grille, Grid<Character> grilleMiseAJour) {
        int resultat = 0;
        for (int ligne = 0; ligne < grille.getHeight(); ligne++) {
            for (int colonne = 0; colonne < grille.getWidth(); colonne++) {
                if (grille.get(ligne, colonne) == '@') {
                    if (grille.getNbVoisins(ligne, colonne,'@') < 4) {
                        resultat++;
                        grilleMiseAJour.set(ligne, colonne, '.');
                    }
                }
            }
        }
        return resultat;
    }
}
