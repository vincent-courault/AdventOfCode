package adventofcode;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay21_2 extends Commun {

    static int largeur = 0;
    static int hauteur = 0;
    static char[][] carte;

    private static boolean estValidePartie1(char[][] map, Point p) {
        if (p.x < 0 || p.x >= largeur) return false;
        if (p.y < 0 || p.y >= hauteur) return false;
        return map[p.x][p.y] != '#';
    }

    private static boolean estValidePartie2(char[][] map, Point p) {
        int x = ((p.x % largeur) + largeur) % largeur;
        int y = ((p.y % hauteur) + hauteur) % hauteur;
        return map[x][y] != '#';
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(16, traitement(inputs, true, 6));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(3605, traitement(inputs, true, 64));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(596734624269210L, traitement(inputs, false, 1000));
    }

    public long traitement(List<String> lines, boolean etape1, int nbpas) {
        Point depart = null;

        // Build map
        largeur = lines.get(0).length();
        hauteur = lines.size();

        int y = 0;
        carte = new char[largeur][hauteur];
        for (String line : lines) {
            for (int x = 0; x < line.length(); x++) {
                carte[x][y] = line.charAt(x);
                if (carte[x][y] == 'S') {
                    depart = new Point(x, y);
                    carte[x][y] = '.';
                }
            }
            y++;
        }

        Set<Point> points = new HashSet<>(Collections.singleton(depart));
        if (etape1) {
            for (int i = 0; i < nbpas; i++) {
                points = points.parallelStream()
                        .flatMap(p -> Stream.of(p.add(Direction.N), p.add(Direction.S), p.add(Direction.E), p.add(Direction.O)))
                        .filter(p -> estValidePartie1(carte, p)).collect(Collectors.toSet());
            }
        }
        long resultat = points.size();
        if (!etape1) {

            long valeurMaxPas = 26501365;
            long cycles = valeurMaxPas / (largeur * 2L);
            long debutCycle = valeurMaxPas % largeur;

            points = new HashSet<>(Collections.singleton(depart));
            //On va mesurer les valeurs pour 65 (début du cycle), 65 + 131 (0.5 cycle) et 65 + 262 (1 cycle)
            // pour ensuite effectuer une régression polynomiale
            final List<Integer> pointsRegression = new ArrayList<>();
            int pas = 0;
            for (int i = 0; i < 3; i++) {
                while (pas < (long) i * largeur + debutCycle) {
                    points = points.parallelStream().flatMap(p -> Stream.of(p.add(Direction.N), p.add(Direction.S), p.add(Direction.E), p.add(Direction.O))).filter(p -> estValidePartie2(carte, p)).collect(Collectors.toSet());
                    pas++;
                }
                pointsRegression.add(points.size());
            }

            final WeightedObservedPoints obs = new WeightedObservedPoints();
            obs.add(0, pointsRegression.get(0));
            obs.add(0.5, pointsRegression.get(1));
            obs.add(1, pointsRegression.get(2));

            final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
            final double[] coeff = fitter.fit(obs.toList());
            int coeffX2 = Math.toIntExact(Math.round(coeff[2]));
            int coeffX1 = Math.toIntExact(Math.round(coeff[1]));
            int coeffX0 = Math.toIntExact(Math.round(coeff[0]));
            resultat = cycles * cycles * coeffX2 + cycles * coeffX1 + coeffX0;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static class Point {
        public int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return x + 1000 * y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        public Point add(Point p) {
            return new Point(x + p.x, y + p.y);
        }
    }

    public static class Direction {
        static final Point N = new Point(0, -1);
        static final Point S = new Point(0, 1);
        static final Point E = new Point(1, 0);
        static final Point O = new Point(-1, 0);
    }
}