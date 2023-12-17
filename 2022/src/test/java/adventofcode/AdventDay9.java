package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class AdventDay9 {
    private static final int[][] directions;

    static {
        directions = new int['U' + 1][];
        directions['U'] = new int[]{+1, +0};
        directions['D'] = new int[]{-1, +0};
        directions['L'] = new int[]{+0, -1};
        directions['R'] = new int[]{+0, +1};
    }

    @Rule
    public TestName name = new TestName();

    private static void deplace(int[] noeud, int[] vecteurDeplacement) {
        noeud[0] += vecteurDeplacement[0];//ligne
        noeud[1] += vecteurDeplacement[1];//colonne
    }

    private static void poursuite(int[] tail, int[] head) {
        int distanceLigne = head[0] - tail[0];
        int distanceColonne = head[1] - tail[1];
        if (sontAdjacents(tail, head, distanceLigne, distanceColonne)) {
            deplace(tail, new int[]{Integer.signum(distanceLigne), Integer.signum(distanceColonne)});
        }
    }

    private static boolean sontAdjacents(int[] tail, int[] head, int distanceLigne, int distanceColonne) {
        int distanceAuCarre = distanceColonne * distanceColonne + distanceLigne * distanceLigne;
        return (sontSurLaMemeLigne(tail, head) && !(distanceColonne <= 1)) ||
                (sontSurLaMemeColonne(tail, head) && !(distanceLigne <= 1)) ||
                !(distanceAuCarre <= 2);
    }

    private static boolean sontSurLaMemeColonne(int[] tail, int[] head) {
        return tail[1] == head[1];
    }

    private static boolean sontSurLaMemeLigne(int[] tail, int[] head) {
        return tail[0] == head[0];
    }

    private List<String> initTest() {
        return List.of("R 4", "U 4", "L 3", "D 1", "R 4", "D 1", "L 5", "R 2");
    }

    private List<String> lectureDuFichier() throws URISyntaxException, IOException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day9.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    @Test
    public void etape1_exemple() {
        List<String> inputs = initTest();
        assertEquals(13, (etape1_main(inputs, 60)));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier();
        assertEquals(6376, (etape1_main(inputs, 2000)));
    }

    public int etape1_main(List<String> inputs, int taille) {

        CaseDuPont[][] pont = initialiseLePont(taille);

        int ligneHead = taille / 2;
        int colonneHead = taille / 2;
        int ligneTail = taille / 2;
        int colonneTail = taille / 2;
        pont[ligneHead][colonneHead].etatTail = "s";
        int ligneHeadAvant;
        int colonneHeadAvant;

        for (String consigne : inputs) {
            String direction = consigne.split(" ")[0];
            int nbmvt = Integer.parseInt(consigne.split(" ")[1]);
            for (int i = 0; i < nbmvt; i++) {
                ligneHeadAvant = ligneHead;
                colonneHeadAvant = colonneHead;
                switch (direction) {
                    case "U" -> ligneHead--;
                    case "D" -> ligneHead++;
                    case "R" -> colonneHead++;
                    case "L" -> colonneHead--;
                }

                //déplacement de tail
                int distance = Math.abs(colonneHead - colonneTail) * Math.abs(colonneHead - colonneTail) + Math.abs(ligneHead - ligneTail) * Math.abs(ligneHead - ligneTail);

                if (distance > 2) {
                    ligneTail = ligneHeadAvant;
                    colonneTail = colonneHeadAvant;
                    pont[ligneTail][colonneTail].etatTail = "#";

                }
            }
        }
        return comptagePont(taille, pont);
    }

    private CaseDuPont[][] initialiseLePont(int taille) {
        CaseDuPont[][] pont = new CaseDuPont[taille][taille];
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                pont[i][j] = new CaseDuPont();
            }
        }
        return pont;
    }

    private int comptagePont(int taillePont, CaseDuPont[][] pont) {
        int nbTail = 0;

        for (int ligne = 0; ligne < taillePont; ligne++) {
            for (int colonne = 0; colonne < taillePont; colonne++) {
                if (!(pont[ligne][colonne].etatTail.equals("."))) {
                    nbTail++;
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + nbTail);

        return nbTail;
    }

    @Test
    public void etape1_exemple_v2() {
        List<String> inputs = initTest();
        assertEquals(13, (etape2_main(2, inputs)));
    }

    @Test
    public void etape1_v2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier();
        assertEquals(6376, (etape2_main(2, inputs)));
    }

    @Test
    public void etape2_exemple1() {
        assertEquals(1, etape2_main(10, List.of("R 4", "U 4", "L 3", "D 1", "R 4", "D 1", "L 5", "R 2")));
    }

    @Test
    public void etape2_exemple2() {
        assertEquals(36, etape2_main(10, List.of("R 5", "U 8", "L 8", "D 3", "R 17", "D 10", "L 25", "U 20")));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        assertEquals(2607, etape2_main(10, lectureDuFichier()));
    }

    public int etape2_main(int nombreNoeuds, List<String> inputs) {
        int[][] noeuds = new int[nombreNoeuds][2];
        Set<String> tailPositions = new HashSet<>();
        enregistreLaPositionDeTail(nombreNoeuds, noeuds, tailPositions);

        for (String input : inputs) {
            int[] dir = directions[input.split(" ")[0].codePointAt(0)];
            int nombreMouvement = Integer.parseInt(input.split(" ")[1]);
            for (int n = nombreMouvement; n > 0; n--) {
                deplace(noeuds[0], dir); // move head

                for (int k = 1; k < nombreNoeuds; k++) {
                    poursuite(noeuds[k], noeuds[k - 1]);
                }
                enregistreLaPositionDeTail(nombreNoeuds, noeuds, tailPositions);
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + tailPositions.size());

        return tailPositions.size();
    }

    private void enregistreLaPositionDeTail(int nombreNoeuds, int[][] noeuds, Set<String> tailPositions) {
        tailPositions.add(Arrays.toString(noeuds[nombreNoeuds - 1]));
    }

    public static class CaseDuPont {
        String etatTail = ".";
    }
}
