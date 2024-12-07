package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay06 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(41, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(5404, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(6, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1984, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat;
        Grid<Character> grille = new Grid<>(inputs, new Divider.Character());
        Grid<Character> grilleVisitee = new Grid<>(inputs, new Divider.Character());
        int gardeLigne = 0;
        int gardeColonne = 0;
        char sens = 'N';
        for (int ligne = 0; ligne < grille.getHeight(); ligne++) {
            for (int colonne = 0; colonne < grille.getWidth(); colonne++) {
                if (grille.get(ligne, colonne) == '^') {
                    gardeLigne = ligne;
                    gardeColonne = colonne;
                    grilleVisitee.set(gardeLigne, gardeColonne, '+');
                    break;
                }
            }
        }

        while (grille.isValid(gardeLigne, gardeColonne)) {
            if (grille.get(gardeLigne, gardeColonne) == '#') {
                sens = switch (sens) {
                    case 'N' -> {
                        gardeLigne++;
                        yield 'E';
                    }
                    case 'S' -> {
                        gardeLigne--;
                        yield 'O';
                    }
                    case 'E' -> {
                        gardeColonne--;
                        yield 'S';
                    }
                    case 'O' -> {
                        gardeColonne++;
                        yield 'N';
                    }
                    default -> sens;
                };
            } else {
                grilleVisitee.set(gardeLigne, gardeColonne, '+');
            }
            switch (sens) {
                case 'N' -> gardeLigne--;
                case 'S' -> gardeLigne++;
                case 'E' -> gardeColonne++;
                case 'O' -> gardeColonne--;
            }
        }
        resultat = grilleVisitee.compte('+');

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;
        Grid<Character> grille = new Grid<>(inputs, new Divider.Character());
        Grid<Character> grilleVisitee = new Grid<>(inputs, new Divider.Character());
        int gardeLigne = 0;
        int gardeColonne = 0;
        char sens = 'N';
        for (int ligne = 0; ligne < grille.getHeight(); ligne++) {
            for (int colonne = 0; colonne < grille.getWidth(); colonne++) {
                if (grille.get(ligne, colonne) == '^') {
                    gardeLigne = ligne;
                    gardeColonne = colonne;
                    grilleVisitee.set(gardeLigne, gardeColonne, '+');
                    break;
                }
            }
        }
        int gardeLigneInit = gardeLigne;
        int gardeColonneInit = gardeColonne;

        for (int ligne = 0; ligne < grille.getHeight(); ligne++) {
            for (int colonne = 0; colonne < grille.getWidth(); colonne++) {
                // on positionne un obstacle sur un emplacement vide
                if (grille.get(ligne, colonne) == '.') {
                    grille.set(ligne, colonne, '#');
                    int nb = 0;

                    while (grille.isValid(gardeLigne, gardeColonne) && nb < 7000) {
                        nb++;
                        if (grille.get(gardeLigne, gardeColonne) == '#') {
                            sens = switch (sens) {
                                case 'N' -> {
                                    gardeLigne++;
                                    yield 'E';
                                }
                                case 'S' -> {
                                    gardeLigne--;
                                    yield 'O';
                                }
                                case 'E' -> {
                                    gardeColonne--;
                                    yield 'S';
                                }
                                case 'O' -> {
                                    gardeColonne++;
                                    yield 'N';
                                }
                                default -> sens;
                            };
                        }
                        switch (sens) {
                            case 'N' -> gardeLigne--;
                            case 'S' -> gardeLigne++;
                            case 'E' -> gardeColonne++;
                            case 'O' -> gardeColonne--;
                        }
                    }

                    if (grille.isValid(gardeLigne, gardeColonne)) {
                        resultat++;
                    }
                    // On se remet dans la situation initiale
                    gardeLigne = gardeLigneInit;
                    gardeColonne = gardeColonneInit;
                    sens = 'N';
                    // on repositionne la valeur initiale sur la position testée
                    grille.set(ligne, colonne, '.');
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}