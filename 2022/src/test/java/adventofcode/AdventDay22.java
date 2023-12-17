package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdventDay22 extends Commun {

    public static int TAILLE_FACE = 50;
    @Rule
    public TestName name = new TestName();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(6032, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(31568, traitement(inputs));
    }

    //@Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(5031, traitementEtape2(inputs, 4));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(36540, traitementEtape2(inputs, 50));
    }

    public int traitement(List<String> inputs) {
        String[][] grille = initialiseLaGrille();
        alimenteLaGrilleAvecLesDonnees(inputs, grille);
        String[] consignesDeplacement = inputs.get(inputs.size() - 1).split("[RL]");
        String[] consignesRotation = inputs.get(inputs.size() - 1).split("\\d{1,3}");

        int positionDepart = determinePositionDeDepart(grille);
        int[] positionActuelle = new int[]{0, positionDepart};
        Sens sens = Sens.DROITE;

        for (int i = 0; i < consignesDeplacement.length; i++) {
            sens = determineLeSensAPartirDesConsignes(consignesRotation, sens, i);
            int nombreDePas = Integer.parseInt(consignesDeplacement[i]);

            for (int j = 0; j < nombreDePas; j++) {
                String positionPotentielle;
                try {
                    positionPotentielle = grille[positionActuelle[0] + sens.ligne][positionActuelle[1] + sens.colonne];
                } catch (ArrayIndexOutOfBoundsException exception) {
                    positionPotentielle = "";
                }
                if (positionPotentielle.equals(".")) {
                    positionActuelle[0] = positionActuelle[0] + sens.ligne;
                    positionActuelle[1] = positionActuelle[1] + sens.colonne;
                }
                if (positionPotentielle.equals("#")) {
                    break;
                }

                if (positionPotentielle.equals("") || positionPotentielle.equals(" ")) {
                    int[] sauvegarde = new int[]{positionActuelle[0], positionActuelle[1]};
                    while ((positionActuelle[0] - sens.ligne) >= 0 && (positionActuelle[1] - sens.colonne) >= 0 && !grille[positionActuelle[0] - sens.ligne][positionActuelle[1] - sens.colonne].equals(" ")) {
                        positionActuelle[0] = positionActuelle[0] - sens.ligne;
                        positionActuelle[1] = positionActuelle[1] - sens.colonne;
                    }
                    if (grille[positionActuelle[0]][positionActuelle[1]].equals("#")) {
                        positionActuelle[0] = sauvegarde[0];
                        positionActuelle[1] = sauvegarde[1];
                        break;
                    }
                }
            }
        }

        int resultat = (positionActuelle[0] + 1) * 1000 + (positionActuelle[1] + 1) * 4 + sens.valeur;
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }

    public int traitementEtape2(List<String> inputs, int taille) {
        TAILLE_FACE = taille;
        String[][] grille = initialiseLaGrille();
        alimenteLaGrilleAvecLesDonnees(inputs, grille);

        String[] consignesDeplacement = inputs.get(inputs.size() - 1).split("[RL]");
        String[] consignesRotation = inputs.get(inputs.size() - 1).split("\\d{1,3}");

        int positionDepart = determinePositionDeDepart(grille);
        int[] positionActuelle = new int[]{0, positionDepart};
        Sens sens = Sens.DROITE;

        for (int i = 0; i < consignesDeplacement.length; i++) {
            sens = determineLeSensAPartirDesConsignes(consignesRotation, sens, i);

            int nombreDePas = Integer.parseInt(consignesDeplacement[i]);
            for (int j = 0; j < nombreDePas; j++) {
                String positionPotentielle;
                try {
                    positionPotentielle = grille[positionActuelle[0] + sens.ligne][positionActuelle[1] + sens.colonne];
                } catch (ArrayIndexOutOfBoundsException exception) {
                    positionPotentielle = "";
                }
                if (positionPotentielle.equals(".")) {
                    positionActuelle[0] = positionActuelle[0] + sens.ligne;
                    positionActuelle[1] = positionActuelle[1] + sens.colonne;
                }
                if (positionPotentielle.equals("#")) {
                    break;
                }

                if (positionPotentielle.equals("") || positionPotentielle.equals(" ")) {
                    int[] sauvegarde = new int[]{positionActuelle[0], positionActuelle[1]};
                    Sens sensSauvegarde = sens;

                    boolean top = false;
                    if ((positionActuelle[0] == 2 * TAILLE_FACE && (positionActuelle[1] >= 0 && positionActuelle[1] < TAILLE_FACE
                            && sens.equals(Sens.HAUT)))) {
                        positionActuelle[0] = sauvegarde[1] + TAILLE_FACE;
                        positionActuelle[1] = TAILLE_FACE;
                        sens = Sens.DROITE;
                        top = true;
                    }
                    if (!top && ((positionActuelle[0] >= 2 * TAILLE_FACE && positionActuelle[0] < 3 * TAILLE_FACE)
                            && positionActuelle[1] == 0 && sens.equals(Sens.GAUCHE))) {
                        positionActuelle[0] = 3 * TAILLE_FACE - 1 - sauvegarde[0];
                        positionActuelle[1] = TAILLE_FACE;
                        sens = Sens.DROITE;
                        top = true;
                    }
                    if (!top && ((positionActuelle[0] >= 3 * TAILLE_FACE && positionActuelle[0] < 4 * TAILLE_FACE)
                            && positionActuelle[1] == 0 && sens.equals(Sens.GAUCHE))) {
                        positionActuelle[0] = 0;
                        positionActuelle[1] = sauvegarde[0] - 2 * TAILLE_FACE;
                        sens = Sens.BAS;
                        top = true;
                    }
                    if (!top && (positionActuelle[0] == 4 * TAILLE_FACE - 1
                            && (positionActuelle[1] >= 0 && positionActuelle[1] < TAILLE_FACE) && sens.equals(Sens.BAS))) {
                        positionActuelle[0] = 0;
                        positionActuelle[1] = sauvegarde[1] + 2 * TAILLE_FACE;
                        top = true;
                    }
                    if (!top && (positionActuelle[0] == 0
                            && (positionActuelle[1] >= TAILLE_FACE && positionActuelle[1] < 2 * TAILLE_FACE) && sens.equals(Sens.HAUT))) {
                        positionActuelle[0] = sauvegarde[1] + 2 * TAILLE_FACE;
                        positionActuelle[1] = 0;
                        sens = Sens.DROITE;
                        top = true;
                    }
                    if (!top && (positionActuelle[0] == 0
                            && (positionActuelle[1] >= 2 * TAILLE_FACE && positionActuelle[1] < 3 * TAILLE_FACE)
                            && sens.equals(Sens.HAUT))) {
                        positionActuelle[0] = 4 * TAILLE_FACE - 1;
                        positionActuelle[1] = sauvegarde[1] - 2 * TAILLE_FACE;
                        top = true;
                    }

                    if (!top && ((positionActuelle[0] >= TAILLE_FACE && positionActuelle[0] < 2 * TAILLE_FACE)
                            && positionActuelle[1] == 2 * TAILLE_FACE - 1 && sens.equals(Sens.DROITE))) {
                        positionActuelle[0] = TAILLE_FACE - 1;
                        positionActuelle[1] = sauvegarde[0] + TAILLE_FACE;
                        sens = Sens.HAUT;
                        top = true;
                    }
                    if (!top && ((positionActuelle[0] >= 2 * TAILLE_FACE && positionActuelle[0] < 3 * TAILLE_FACE)
                            && positionActuelle[1] == 2 * TAILLE_FACE - 1 && sens.equals(Sens.DROITE))) {
                        positionActuelle[0] = 3 * TAILLE_FACE - 1 - sauvegarde[0];
                        positionActuelle[1] = 3 * TAILLE_FACE - 1;
                        sens = Sens.GAUCHE;
                        top = true;
                    }
                    if (!top && (positionActuelle[0] == TAILLE_FACE - 1
                            && (positionActuelle[1] >= 2 * TAILLE_FACE && positionActuelle[1] < 3 * TAILLE_FACE)
                            && sens.equals(Sens.BAS))) {
                        positionActuelle[1] = 2 * TAILLE_FACE - 1;
                        positionActuelle[0] = sauvegarde[1] - TAILLE_FACE;
                        sens = Sens.GAUCHE;
                        top = true;
                    }
                    if (!top && (positionActuelle[0] == 3 * TAILLE_FACE - 1
                            && (positionActuelle[1] >= TAILLE_FACE && positionActuelle[1] < 2 * TAILLE_FACE)
                            && sens.equals(Sens.BAS))) {
                        positionActuelle[0] = sauvegarde[1] + 2 * TAILLE_FACE;
                        positionActuelle[1] = TAILLE_FACE - 1;
                        sens = Sens.GAUCHE;
                        top = true;
                    }
                    if (!top && ((positionActuelle[0] >= 0 && positionActuelle[0] < TAILLE_FACE)
                            && positionActuelle[1] == TAILLE_FACE && sens.equals(Sens.GAUCHE))) {
                        positionActuelle[0] = 3 * TAILLE_FACE - 1 - sauvegarde[0];
                        positionActuelle[1] = 0;
                        sens = Sens.DROITE;
                        top = true;
                    }
                    if (!top && ((positionActuelle[0] >= TAILLE_FACE && positionActuelle[0] < 2 * TAILLE_FACE)
                            && positionActuelle[1] == TAILLE_FACE && sens.equals(Sens.GAUCHE))) {
                        positionActuelle[0] = 2 * TAILLE_FACE;
                        positionActuelle[1] = sauvegarde[0] - TAILLE_FACE;
                        sens = Sens.BAS;
                        top = true;
                    }
                    if (!top && ((positionActuelle[0] >= 0 && positionActuelle[0] < TAILLE_FACE) && positionActuelle[1] == 3 * TAILLE_FACE - 1
                            && sens.equals(Sens.DROITE))) {
                        positionActuelle[0] = 3 * TAILLE_FACE - 1 - sauvegarde[0];
                        positionActuelle[1] = 2 * TAILLE_FACE - 1;
                        sens = Sens.GAUCHE;
                        top = true;
                    }
                    if (!top && ((positionActuelle[0] >= 3 * TAILLE_FACE && positionActuelle[0] < 4 * TAILLE_FACE)
                            && positionActuelle[1] == TAILLE_FACE - 1 && sens.equals(Sens.DROITE))) {
                        positionActuelle[0] = 3 * TAILLE_FACE - 1;
                        positionActuelle[1] = sauvegarde[0] - 2 * TAILLE_FACE;
                        sens = Sens.HAUT;
                        top = true;
                    }

                    if (!top) {
                        System.out.println("casKO" + positionActuelle[0] + "," + positionActuelle[1] + sens);
                    }

                    if (grille[positionActuelle[0]][positionActuelle[1]].equals("#")) {
                        positionActuelle[0] = sauvegarde[0];
                        positionActuelle[1] = sauvegarde[1];
                        sens = sensSauvegarde;
                        break;
                    }
                }
            }
        }
        int resultat = (positionActuelle[0] + 1) * 1000 + (positionActuelle[1] + 1) * 4 + sens.valeur;
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }

    private Sens determineLeSensAPartirDesConsignes(String[] consignesRotation, Sens sens, int indiceDeConsigne) {
        if (!consignesRotation[indiceDeConsigne].equals("")) {
            if (consignesRotation[indiceDeConsigne].equals("R")) {
                sens = switch (sens) {
                    case DROITE -> Sens.BAS;
                    case BAS -> Sens.GAUCHE;
                    case GAUCHE -> Sens.HAUT;
                    case HAUT -> Sens.DROITE;
                };
            } else {
                sens = switch (sens) {
                    case DROITE -> Sens.HAUT;
                    case BAS -> Sens.DROITE;
                    case GAUCHE -> Sens.BAS;
                    case HAUT -> Sens.GAUCHE;
                };
            }
        }
        return sens;
    }

    private int determinePositionDeDepart(String[][] grille) {
        int positionDepart = 0;
        for (int i = 0; i < 200; i++) {
            if (grille[0][i].equals(".")) {
                positionDepart = i;
                break;
            }
        }
        return positionDepart;
    }

    private void alimenteLaGrilleAvecLesDonnees(List<String> inputs, String[][] grille) {
        for (int i = 0; i < inputs.size() - 1; i++) {
            String input = inputs.get(i);
            for (int j = 0; j < input.length(); j++) {
                grille[i][j] = input.substring(j, j + 1);
            }
        }
    }

    private String[][] initialiseLaGrille() {
        String[][] grille = new String[201][201];
        for (int i = 0; i < 201; i++) {
            for (int j = 0; j < 201; j++) {
                grille[i][j] = " ";
            }
        }
        return grille;
    }

    public enum Sens {
        DROITE(0, 1, 0), BAS(1, 0, 1), GAUCHE(0, -1, 2), HAUT(-1, 0, 3);

        private final int ligne;
        private final int colonne;
        private final int valeur;

        Sens(int i, int i1, int valeur) {
            this.ligne = i;
            this.colonne = i1;
            this.valeur = valeur;
        }
    }
}
