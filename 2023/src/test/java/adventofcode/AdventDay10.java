package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay10 extends Commun {
    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(8, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(7012, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier2(this, true);
        assertEquals(10, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(395, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {
        String[][] carteResolue;
        String[][] carteOrigine;
        int resultat;
        carteOrigine = new String[inputs.size()][inputs.size()];
        carteResolue = new String[inputs.size()][inputs.size()];
        int ligneDepart = 0;
        int colonneDepart = 0;

        for (int i = 0; i < inputs.size(); i++) {
            carteResolue[i] = inputs.get(i).replace("7", "T").split("");
            if (inputs.get(i).contains("S")) {
                ligneDepart = i;
                colonneDepart = inputs.get(i).indexOf("S");
            }
        }
        int nbEtape = resoudLaBoucle(ligneDepart, colonneDepart, carteResolue);
        if (etape1) {
            resultat = (nbEtape / 2 + 1);
        } else {
            for (int i = 0; i < inputs.size(); i++)
                carteOrigine[i] = inputs.get(i).replace("7", "T").split("");
            resultat = calculeLeNombreDePointInterieur(carteOrigine, carteResolue);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private int resoudLaBoucle(int ligneDepart, int colonneDepart, String[][] carteAResoudre) {
        int nbEtape = 0;
        carteAResoudre[ligneDepart][colonneDepart] = String.valueOf(nbEtape);
        int ligne = ligneDepart;
        int colonne = colonneDepart + 1;
        char sensLigne = '+';
        char sensColonne = '+';
        boolean finBoucle = false;
        while (!finBoucle) {
            String signe = carteAResoudre[ligne][colonne];
            carteAResoudre[ligne][colonne] = String.valueOf(nbEtape);
            switch (signe) {
                case "-" -> {
                    if (sensColonne == '+') {
                        colonne++;
                    } else {
                        colonne--;
                    }
                }
                case "|" -> {
                    if (sensLigne == '+') {
                        ligne++;
                    } else {
                        ligne--;
                    }
                }
                case "T" -> {
                    if (faitPartieDeLaBoucle(carteAResoudre[ligne + 1][colonne])) {
                        colonne--;
                        sensColonne = '-';
                    } else {
                        ligne++;
                        sensLigne = '+';
                    }
                }
                case "J" -> {
                    if (faitPartieDeLaBoucle(carteAResoudre[ligne][colonne - 1])) {
                        ligne--;
                        sensLigne = '-';
                    } else {
                        colonne--;
                        sensColonne = '-';
                    }
                }
                case "L" -> {
                    if (faitPartieDeLaBoucle(carteAResoudre[ligne - 1][colonne])) {
                        colonne++;
                        sensColonne = '+';
                    } else {
                        ligne--;
                        sensLigne = '-';
                    }
                }
                case "F" -> {
                    if (faitPartieDeLaBoucle(carteAResoudre[ligne][colonne + 1])) {
                        ligne++;
                        sensLigne = '+';
                    } else {
                        colonne++;
                        sensColonne = '+';
                    }
                }
            }
            if (colonne == colonneDepart && ligne == ligneDepart) {
                finBoucle = true;
            }
            nbEtape++;
        }
        return nbEtape;
    }

    private static int calculeLeNombreDePointInterieur(String[][] carteOrigine, String[][] carteResolue) {
        int nbInterieur = 0;
        for (int i = 0; i < carteResolue.length; i++) {
            boolean in = false;
            for (int j = 0; j < carteResolue[i].length; j++) {
                String valeurActuelle = carteResolue[i][j];
                String valeurOrigine = carteOrigine[i][j];
                if (faitPartieDeLaBoucle(valeurActuelle) &&
                        ("|".equals(valeurOrigine) || "L".equals(valeurOrigine) || "J".equals(valeurOrigine))) {
                    in = !in;
                } else if (in && !faitPartieDeLaBoucle(valeurActuelle)) {
                    nbInterieur++;
                }
            }
        }
        return nbInterieur;
    }

    private static boolean faitPartieDeLaBoucle(String valeurActuelle) {
        return valeurActuelle.matches("\\d*");
    }
}