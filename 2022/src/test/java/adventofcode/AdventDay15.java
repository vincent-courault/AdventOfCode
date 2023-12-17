package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdventDay15 extends Commun {

    @Rule
    public TestName name = new TestName();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(26, traitementEtape1(inputs, 10));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(5083287, traitementEtape1(inputs, 2000000));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(56000011, traitementEtape2(inputs, 4, 20));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(13134039205729L, traitementEtape2(inputs, 40, 4000000));
    }

    public int traitementEtape1(List<String> inputs, Integer coordLigne) {
        List<int[]> sensorBeacons = new ArrayList<>();
        for (String input : inputs) {
            String[] coords = input.split("[=,:]");
            int[] sensor = new int[]{Integer.parseInt(coords[1]) + coordLigne, Integer.parseInt(coords[3]) + coordLigne};
            int[] beacon = new int[]{Integer.parseInt(coords[5]) + coordLigne, Integer.parseInt(coords[7]) + coordLigne};
            sensorBeacons.add(new int[]{sensor[0], sensor[1], beacon[0], beacon[1]});
        }

        int coordY = coordLigne * 2;
        int[] ligne = new int[coordLigne * 10];
        int distanceHorizontale;
        for (int[] sensorBeacon : sensorBeacons) {
            int distance = calculerDistanceManhattan(sensorBeacon);
            int distanceALaLigne = Math.abs(sensorBeacon[1] - coordY);
            if (distanceALaLigne < distance) {
                distanceHorizontale = (distance - distanceALaLigne);
                for (int j = 0; j <= distanceHorizontale; j++) {
                    ligne[sensorBeacon[0] + j] = 1;
                    ligne[sensorBeacon[0] - j] = 1;
                }
            }
        }

        int resultat = Arrays.stream(ligne).sum();

        List<Integer> dejaDecompte = new ArrayList<>();
        for (int[] sensorbeacon : sensorBeacons) {
            if (sensorbeacon[1] == coordY && !dejaDecompte.contains(sensorbeacon[0])) {
                resultat--;
                dejaDecompte.add(sensorbeacon[0]);
            }
            if (sensorbeacon[3] == coordY && !dejaDecompte.contains(sensorbeacon[2])) {
                resultat--;
                dejaDecompte.add(sensorbeacon[2]);
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }

    private int calculerDistanceManhattan(int[] sensorBeacon) {
        return Math.abs(sensorBeacon[0] - sensorBeacon[2]) + Math.abs(sensorBeacon[1] - sensorBeacon[3]);
    }

    public long traitementEtape2(List<String> inputs, int valeurBloc, int max) {
        List<int[]> sensors = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();
        for (String input : inputs) {
            String[] line = input.split("[=,:]");
            int x1 = Integer.parseInt(line[1]);
            int y1 = Integer.parseInt(line[3]);
            int x2 = Integer.parseInt(line[5]);
            int y2 = Integer.parseInt(line[7]);
            sensors.add(new int[]{x1, y1});
            distances.add(Math.abs(x1 - x2) + Math.abs(y1 - y2));
        }
        List<int[]> possibilities = new ArrayList<>();
        for (int x = 0; x < max; x += (valeurBloc * valeurBloc)) {
            for (int y = 0; y < max; y += (valeurBloc * valeurBloc)) {
                if (checkPossible(x, y, sensors, distances, valeurBloc * valeurBloc + 1)) {
                    possibilities.add(new int[]{x, y});
                }
            }
        }

        List<int[]> possibilities2 = new ArrayList<>();
        for (int[] coord : possibilities) {
            for (int x = coord[0]; x <= coord[0] + (valeurBloc * valeurBloc); x += valeurBloc) {
                for (int y = coord[1]; y <= coord[1] + (valeurBloc * valeurBloc); y += valeurBloc) {
                    if (checkPossible(x, y, sensors, distances, valeurBloc + 1)) {
                        possibilities2.add(new int[]{x, y});
                    }
                }
            }
        }

        for (int[] coord : possibilities2) {
            for (int x = coord[0]; x <= coord[0] + valeurBloc; x++) {
                for (int y = coord[1]; y <= coord[1] + valeurBloc; y++) {
                    if (checkPossible(x, y, sensors, distances, 0)) {
                        long resultat = (((long) x * 4000000) + (long) y);
                        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
                        return resultat;
                    }
                }
            }
        }
        return 0;
    }

    public boolean checkPossible(int x, int y, List<int[]> sensors, List<Integer> distances, int marge) {
        for (int i = 0; i < sensors.size(); i++) {
            if (Math.abs(x - sensors.get(i)[0]) + Math.abs(y - sensors.get(i)[1]) + marge <= distances.get(i)) {
                return false;
            }
        }
        return true;
    }

}

