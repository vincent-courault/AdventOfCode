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
        assertEquals(596734624269210L, traitement(inputs, false, 0));
    }

    public long traitement(List<String> lines, boolean etape1, int nbpas) {
        Point depart = null;

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
                        .flatMap(p -> Stream.of(p.add(Direction.N), p.add(Direction.S),
                                p.add(Direction.E), p.add(Direction.O)))
                        .filter(Point::estValidePartie1).collect(Collectors.toSet());
            }
        }
        long resultat = points.size();
        if (!etape1) {
            long valeurMaxPas = 26501365;
            long cycles = valeurMaxPas / largeur;
            int debutCycle = (int) (valeurMaxPas % largeur);
            points = new HashSet<>(Collections.singleton(depart));
            //On va mesurer les valeurs pour 65 (début du cycle), 65 + 131 (1 cycle) et 65 + 262 (2 cycles)
            // pour ensuite effectuer une régression polynomiale
            final List<Integer> pointsRegression = new ArrayList<>();
            int nombrePas = 0;
            for (int i = 0; i < 3; i++) {
                while (nombrePas < i * largeur + debutCycle) {
                    points = points.parallelStream()
                            .flatMap(p -> Stream.of(p.add(Direction.N), p.add(Direction.S),
                                    p.add(Direction.E), p.add(Direction.O)))
                            .filter(Point::estValidePartie2).collect(Collectors.toSet());
                    nombrePas++;
                }
                pointsRegression.add(points.size());
            }

            final WeightedObservedPoints obs = new WeightedObservedPoints();
            for (int i = 0; i < pointsRegression.size(); i++) {
                obs.add(i, pointsRegression.get(i));
            }
            final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
            final double[] coeff = fitter.fit(obs.toList());
            int coeffX2 = Math.toIntExact(Math.round(coeff[2]));
            int coeffX = Math.toIntExact(Math.round(coeff[1]));
            int constante = Math.toIntExact(Math.round(coeff[0]));
            resultat = cycles * cycles * coeffX2 + cycles * coeffX + constante;
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

        private boolean estValidePartie1() {
            if (x < 0 || x >= largeur) return false;
            if (y < 0 || y >= hauteur) return false;
            return carte[x][y] != '#';
        }

        private boolean estValidePartie2() {
            int newx = ((x % largeur) + largeur) % largeur;
            int newy = ((y % hauteur) + hauteur) % hauteur;
            return carte[newx][newy] != '#';
        }
    }

    public static class Direction {
        static final Point N = new Point(0, -1);
        static final Point S = new Point(0, 1);
        static final Point E = new Point(1, 0);
        static final Point O = new Point(-1, 0);
    }
}