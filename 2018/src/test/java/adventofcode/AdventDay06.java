package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AdventDay06 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(17, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(3871, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(90, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(44667, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        List<int[]> coordonnees = new ArrayList<>();
        int maxX = 0;
        int maxY = 0;
        for (String line : inputs) {
            String[] split = line.split(",");
            int x = Integer.parseInt(split[0].trim());
            int y = Integer.parseInt(split[1].trim());
            coordonnees.add(new int[]{x, y});
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
        maxX++;
        maxY++;
        Map<String, Integer> map = new HashMap<>();
        Set<String> infinis = new HashSet<>();
        for (int x1 = -1; x1 < maxX + 1; x1++) {
            for (int y1 = -1; y1 < maxY + 1; y1++) {
                int[] minCoord = null;
                int minDistance = -1;
                for (int[] coordinate : coordonnees) {
                    int x2 = coordinate[0];
                    int y2 = coordinate[1];
                    int distance = distance(x1, y1, x2, y2);
                    if (minDistance == -1 || minDistance >= distance) {
                        minCoord = minDistance == distance ? null : coordinate;
                        minDistance = distance;
                    }
                }
                if (minCoord != null) {
                    String key = key(minCoord);
                    if (!map.containsKey(key)) {
                        map.put(key, 1);
                    } else {
                        map.put(key, map.get(key) + 1);
                    }
                    if (x1 < 0 || x1 >= maxX || y1 < 0 || y1 >= maxY) {
                        infinis.add(key);
                    }
                }
            }
        }
        for (String infinite : infinis) {
            map.remove(infinite);
        }
        int resultat = 0;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            resultat = Math.max(resultat, e.getValue());
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private int traitement2(List<String> inputs) {
        List<int[]> coordinates = new ArrayList<>();
        int maxX = 0;
        int maxY = 0;
        for (String line : inputs) {
            String[] split = line.split(",");
            int x = Integer.parseInt(split[0].trim());
            int y = Integer.parseInt(split[1].trim());
            coordinates.add(new int[]{x, y});
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
        maxX++;
        maxY++;
        int resultat = 0;
        for (int x1 = 0; x1 < maxX; x1++) {
            for (int y1 = 0; y1 < maxY; y1++) {
                int totalDistance = 0;
                for (int[] coordinate : coordinates) {
                    int x2 = coordinate[0];
                    int y2 = coordinate[1];
                    int distance = distance(x1, y1, x2, y2);
                    totalDistance += distance;
                }
                if (totalDistance < 10000) {
                    resultat++;
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static String key(int[] coordinate) {
        return coordinate[0] + "," + coordinate[1];
    }

    private static int distance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}