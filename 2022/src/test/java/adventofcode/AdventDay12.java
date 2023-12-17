package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class AdventDay12 {

    @Rule
    public TestName name = new TestName();
    String[][] carte;
    private int hauteur;
    private int largeur;
    private int lignePositionDepart;
    private int colonnePositionDepart;
    private int ligneCible;
    private int colonneCible;

    private List<String> initTest() throws IOException, URISyntaxException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day12_exemple.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    private List<String> lectureDuFichier() throws URISyntaxException, IOException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day12.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    @Test
    public void etape1_exemple() throws IOException, URISyntaxException {
        List<String> inputs = initTest();
        assertEquals(31, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> assignments = lectureDuFichier();
        assertEquals(534, traitement(assignments, true));
    }

    @Test
    public void etape2_exemple() throws IOException, URISyntaxException {
        List<String> assignments = initTest();
        assertEquals(29, traitement(assignments, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> assignments = lectureDuFichier();
        assertEquals(525, traitement(assignments, false));
    }

    public int traitement(List<String> description, boolean etape1) {
        lectureDeLaCarte(description);

        LinkedList<int[]> queue = new LinkedList<>();
        List<int[]> visited = new ArrayList<>();
        determineLeDebut(etape1, queue, visited);

        boolean destinationAtteinte = false;
        int valeurDeplacement = 0;
        while (!destinationAtteinte) {
            int[] positionActuelle = queue.pollFirst();
            assert positionActuelle != null;
            List<int[]> voisins = determineLesVoisins(positionActuelle);

            for (int[] voisin : voisins) {
                if (estHorsLimite(voisin)) {
                    continue;
                }
                if (aDejaEteVisite(visited, voisin)) {
                    continue;
                }
                if (mouvementNonAutorise(positionActuelle, voisin, etape1)) {
                    continue;
                }

                if (destinationAtteinte(voisin, etape1, colonneCible, ligneCible)) {
                    destinationAtteinte = true;
                    valeurDeplacement = positionActuelle[0] + 1;
                    break;
                }
                visited.add(voisin);
                queue.add(new int[]{positionActuelle[0] + 1, voisin[0], voisin[1]});
            }
        }

        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + valeurDeplacement);
        return valeurDeplacement;
    }

    private void lectureDeLaCarte(List<String> description) {
        hauteur = description.size();
        largeur = description.get(0).length();
        carte = new String[hauteur][largeur];

        for (int i = 0; i < description.size(); i++) {
            String ligne = description.get(i);
            for (int j = 0; j < ligne.length(); j++) {
                carte[i][j] = ligne.substring(j, j + 1);
                if (carte[i][j].equals("S")) {
                    lignePositionDepart = i;
                    colonnePositionDepart = j;
                    carte[i][j] = "a";
                }
                if (carte[i][j].equals("E")) {
                    ligneCible = i;
                    colonneCible = j;
                    carte[i][j] = "z";
                }
            }
        }
    }

    private void determineLeDebut(boolean etape1, LinkedList<int[]> queue, List<int[]> visited) {
        if (etape1) {
            queue.add(new int[]{0, lignePositionDepart, colonnePositionDepart});
            visited.add(new int[]{lignePositionDepart, colonnePositionDepart});
        } else {
            queue.add(new int[]{0, ligneCible, colonneCible});
            visited.add(new int[]{ligneCible, colonneCible});
        }
    }

    private List<int[]> determineLesVoisins(int[] valeur) {

        int ligne = valeur[1];
        int colonne = valeur[2];
        List<int[]> voisins = new ArrayList<>(3);
        voisins.add(new int[]{ligne + 1, colonne});
        voisins.add(new int[]{ligne - 1, colonne});
        voisins.add(new int[]{ligne, colonne + 1});
        voisins.add(new int[]{ligne, colonne - 1});
        return voisins;
    }

    private boolean estHorsLimite(int[] voisin) {
        return voisin[0] < 0 || voisin[1] < 0 || voisin[0] >= hauteur || voisin[1] >= largeur;
    }

    private boolean mouvementNonAutorise(int[] positionActuelle, int[] voisin, boolean etape1) {
        if (etape1) {
            return (carte[voisin[0]][voisin[1]].codePointAt(0) - carte[positionActuelle[1]][positionActuelle[2]].codePointAt(0)) > 1;
        } else {
            return (carte[positionActuelle[1]][positionActuelle[2]].codePointAt(0) - carte[voisin[0]][voisin[1]].codePointAt(0)) > 1;
        }
    }

    private boolean aDejaEteVisite(List<int[]> visited, int[] voisin) {
        for (int[] visite : visited) {
            if (visite[0] == voisin[0] && visite[1] == voisin[1]) return true;
        }
        return false;
    }

    private boolean destinationAtteinte(int[] voisin, boolean etape1, int colonneCible, int ligneCible) {
        if (etape1) {
            return voisin[0] == ligneCible && voisin[1] == colonneCible;
        } else {
            return carte[voisin[0]][voisin[1]].equals("a");
        }
    }
}
