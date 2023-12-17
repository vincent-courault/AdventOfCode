package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdventDay14 extends Commun {

    public static final int X_DEPART = 500;
    private final int largeur = 700;
    @Rule
    public TestName name = new TestName();
    private String[][] carte;
    private int maxY;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(24, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(838, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(93, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(27539, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {
        initialiseLaCarte();
        traceLesMurs(inputs);

        if (!etape1) {
            traceLePlancher();
        }

        boolean fin = false;
        int nombreSable = 0;
        while (!fin) {
            int[] sable = {X_DEPART,0};

            if (positionDeDepartDejaOccupeeParDuSable()) {
                fin = true;
                continue;
            }
            nombreSable++;

            boolean grainEnPlace = false;
            while (!grainEnPlace) {
                if (sable[1] > maxY + 1) {
                    fin = true;
                    nombreSable--;
                    break;
                }
                if (estLibre(enDessous(sable))) {
                    deplaceEnDessous(sable);
                    continue;
                }
                if (estLibre(aGaucheEnDessous(sable))) {
                    deplaceAGaucheEnDessous(sable);
                    continue;
                }
                if (estLibre(aDroiteEnDessous(sable))) {
                    deplaceADroiteEnDessous(sable);
                    continue;
                }
                enregistreLaPosition(sable);
                grainEnPlace = true;
            }
        }

        //visualisation();
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + nombreSable);

        return nombreSable;
    }

    private boolean positionDeDepartDejaOccupeeParDuSable() {
        return carte[X_DEPART][0].equals("o");
    }

    private void deplaceADroiteEnDessous(int[] sable) {
        libereLaCase(sable);
        sable[1]++;
        sable[0]++;
        enregistreLaPosition(sable);
    }

    private void deplaceAGaucheEnDessous(int[] sable) {
        libereLaCase(sable);
        sable[1]++;
        sable[0]--;
        enregistreLaPosition(sable);
    }

    private void deplaceEnDessous(int[] sable) {
        libereLaCase(sable);
        sable[1]++;
        enregistreLaPosition(sable);
    }

    private void enregistreLaPosition(int[] sable) {
        carte[sable[0]][sable[1]] = "o";
    }

    private void libereLaCase(int[] sable) {
        carte[sable[0]][sable[1]] = ".";
    }

    private boolean estLibre(String valeurCase) {
        return !(valeurCase.equals("o") || valeurCase.equals("#"));
    }

    private String aDroiteEnDessous(int[] sable) {
        return carte[sable[0] + 1][sable[1] + 1];
    }

    private String enDessous(int[] sable) {
        return carte[sable[0]][sable[1] + 1];
    }

    private String aGaucheEnDessous(int[] sable) {
        return carte[sable[0] - 1][sable[1] + 1];
    }

    private void initialiseLaCarte() {
        int hauteur = 180;
        carte = new String[largeur][hauteur];
        for (int i = 0; i < largeur; i++) {
            for (int j = 0; j < hauteur; j++) {
                carte[i][j] = ".";
            }
        }
    }

    private void traceLesMurs(List<String> inputs) {
        for (String mur : inputs) {
            String[] coords = mur.split(" -> ");
            String coord = coords[0];
            int[] pointPrecedent = lecturePoint(coord);

            for (int i = 1; i < coords.length; i++) {
                coord = coords[i];
                int[] point = lecturePoint(coord);
                carte[point[0]][point[1]] = "#";
                maxY = Math.max(maxY, point[1]);
                if (pointPrecedent[0] == point[0]) {
                    traceMurHorizontal(pointPrecedent, point);
                }
                if (pointPrecedent[1] == point[1]) {
                    traceMurVertical(pointPrecedent, point);
                }
                pointPrecedent = point;
            }
        }
    }

    private int[] lecturePoint(String coord) {
        return new int[]{Integer.parseInt(coord.split(",")[0]), Integer.parseInt(coord.split(",")[1])};
    }

    private void traceMurVertical(int[] pointPrecedent, int[] point) {
        int min = Math.min(pointPrecedent[0], point[0]);
        int max = Math.max(pointPrecedent[0], point[0]);
        maxY = Math.max(maxY, point[1]);
        for (int i = min; i <= max; i++) {
            carte[i][point[1]] = "#";
        }
    }

    private void traceMurHorizontal(int[] pointPrecedent, int[] point) {
        int min = Math.min(pointPrecedent[1], point[1]);
        int max = Math.max(pointPrecedent[1], point[1]);
        for (int i = min; i <= max; i++) {
            carte[point[0]][i] = "#";
            maxY = Math.max(maxY, i);
        }
    }

    private void traceLePlancher() {
        int ligne = maxY + 2;
        for (int i = 0; i < largeur; i++) {
            carte[i][ligne] = "#";

        }
    }
}
