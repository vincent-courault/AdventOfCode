package adventofcode;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AdventDay11 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(374, traitement(inputs, 1));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(9556896, traitement(inputs, 1));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1030, traitement(inputs, 9));
    }

    @Test
    public void etape2_exemple2() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(8410, traitement(inputs, 99));
    }
    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(685038186836L, traitement(inputs, 999999));
    }

    public long traitement(List<String> inputs, int increment) {
        long resultat = 0;
        List<List<String>> espace = new ArrayList<>();
        List<Coordonnees2D> galaxies = new ArrayList<>();
        List<Integer> colonnesVides = new ArrayList<>();
        List<Integer> lignesVides = new ArrayList<>();

        for (int j = 0; j < inputs.size(); j++) {
            String in = inputs.get(j);
            List<String> line = Arrays.stream(in.split("")).toList();
            for (int i = 0; i < line.size(); i++) {
                if (line.get(i).equals("#"))
                    galaxies.add(new Coordonnees2D(i, j));
            }
            if (!in.contains("#"))
                lignesVides.add(j);
            espace.add(line);
        }
        for (int i = espace.size() - 1; i >= 0; i--) {
            boolean noGalaxy = true;
            for (List<String> strings : espace) {
                if (strings.get(i).equals("#")) {
                    noGalaxy = false;
                    break;
                }
            }
            if (noGalaxy) {
                colonnesVides.add(i);
            }
        }

        Set<Pair<Coordonnees2D, Coordonnees2D>> pairesUniques = new HashSet<>();
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                Pair<Coordonnees2D, Coordonnees2D> paire = new ImmutablePair<>(galaxies.get(i), galaxies.get(j));
                pairesUniques.add(paire);
            }
        }

        for (Pair<Coordonnees2D, Coordonnees2D> galaxiesParPaires : pairesUniques) {
            Coordonnees2D galaxie1 = galaxiesParPaires.getKey();
            Coordonnees2D galaxie2 = galaxiesParPaires.getValue();
            long distance = Math.abs(galaxie1.x - galaxie2.x) + Math.abs(galaxie1.y - galaxie2.y);
            distance += colonnesVides.stream()
                    .filter(i -> (galaxie1.x > i && i > galaxie2.x) || (galaxie1.x < i && i < galaxie2.x))
                    .mapToLong(_ -> increment).sum();
            distance += lignesVides.stream()
                    .filter(i -> (galaxie1.y > i && i > galaxie2.y) || (galaxie1.y < i && i < galaxie2.y))
                    .mapToLong(_ -> increment).sum();
            resultat += distance;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static class Coordonnees2D {
        public int x;
        public int y;

        public Coordonnees2D(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Coordonnees2D{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
