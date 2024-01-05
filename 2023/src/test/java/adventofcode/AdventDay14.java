package adventofcode;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AdventDay14 extends Commun {

    @Test
    @Order(1)
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(136, traitement(inputs, true));
    }

    @Test
    @Order(2)
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(105982, traitement(inputs, true));
    }

    @Test
    @Order(3)
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(64, traitement(inputs, false));
    }

    @Test
    @Order(3)
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(85175, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean etape1) {
        long resultat;
        String[][] carte = new String[inputs.get(0).length()][inputs.size()];
        for (int i = 0; i < inputs.size(); i++) {
            String line = inputs.get(i);
            carte[i] = line.split("");
        }
        Map<String, Long> valeursCarte = new HashMap<>();
        if (etape1) {
            versNord(carte);
            resultat = calculeValeurCarte(carte);
        } else {
            Long debutCycle = 0L;
            long longueurCycle = 0L;
            for (long numeroTourActuel = 1L; numeroTourActuel < 10000; numeroTourActuel++) {
                faitUnTour(carte);
                //On calcule une clé pour la carte qui correspond aux positions des "O"
                String valeurDeLaCarte = calculeValeurCarte(carte) + "|" + calculeValeurCarteParColonne(carte);

                //Si la clé est déjà présente on récupère le début du cycle et on calcule la taille du cycle
                if (valeursCarte.containsKey(valeurDeLaCarte)) {
                    debutCycle = valeursCarte.get(valeurDeLaCarte);
                    longueurCycle = numeroTourActuel - debutCycle;
                    break;
                }
                valeursCarte.put(valeurDeLaCarte, numeroTourActuel);
            }
            long max = 1000000000L;
            Long positionCarteALaFinDesTours = debutCycle + (max - debutCycle) % (longueurCycle);
            String valeurCarteALaFin = valeursCarte.entrySet().stream().filter(entry -> positionCarteALaFinDesTours.equals(entry.getValue())).map(Map.Entry::getKey).findFirst().orElse("");
            resultat = Long.parseLong(valeurCarteALaFin.split("\\|")[0]);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static void faitUnTour(String[][] carte) {
        versNord(carte);
        versOuest(carte);
        versSud(carte);
        versEst(carte);
    }

    private static void versNord(String[][] carte) {
        for (int x = 1; x < carte[0].length; x++) {
            for (int y = 0; y < carte[0].length; y++) {
                if (carte[x][y].equals("O")) {
                    int x2 = x;
                    while (x2 > 0) {
                        if (carte[x2 - 1][y].equals(".")) {
                            x2--;
                        } else {
                            break;
                        }
                    }
                    if (x2 != x) {
                        carte[x2][y] = "O";
                        carte[x][y] = ".";
                    }
                }
            }
        }
    }

    private static void versEst(String[][] carte) {
        for (int x = carte[0].length - 1; x >= 0; x--) {
            for (int y = carte[0].length - 1; y >= 0; y--) {
                if (carte[x][y].equals("O")) {
                    int y2 = y;
                    while (y2 < carte[0].length - 1) {
                        if (carte[x][y2 + 1].equals(".")) {
                            y2++;
                        } else {
                            break;
                        }
                    }
                    if (y2 != y) {
                        carte[x][y2] = "O";
                        carte[x][y] = ".";
                    }
                }
            }
        }
    }

    private static void versSud(String[][] carte) {
        for (int x = carte[0].length - 1; x >= 0; x--) {
            for (int y = carte[0].length - 1; y >= 0; y--) {
                if (carte[x][y].equals("O")) {
                    int x2 = x;
                    while (x2 < carte[0].length - 1) {
                        if (carte[x2 + 1][y].equals(".")) {
                            x2++;
                        } else {
                            break;
                        }
                    }
                    if (x2 != x) {
                        carte[x2][y] = "O";
                        carte[x][y] = ".";
                    }
                }
            }
        }
    }

    private static void versOuest(String[][] carte) {
        for (int x = 0; x < carte[0].length; x++) {
            for (int y = 0; y < carte[0].length; y++) {
                if (carte[x][y].equals("O")) {
                    int y2 = y;
                    while (y2 > 0) {
                        if (carte[x][y2 - 1].equals(".")) {
                            y2--;
                        } else {
                            break;
                        }
                    }
                    if (y2 != y) {
                        carte[x][y2] = "O";
                        carte[x][y] = ".";
                    }
                }
            }
        }
    }

    private static long calculeValeurCarte(String[][] carte) {
        long resultat = 0;
        for (int i = 0; i < carte.length; i++) {
            long compte = Arrays.stream(carte[i]).filter(s -> s.equals("O")).count();
            resultat += (compte * (carte.length - i));
        }
        return resultat;
    }

    private static long calculeValeurCarteParColonne(String[][] carte) {
        long resultat = 0;
        for (int y = 0; y < carte.length; y++) {
            for (String[] strings : carte) {
                if (strings[y].equals("O")) {
                    resultat += ((carte.length - y));
                }
            }
        }
        return resultat;
    }
}
