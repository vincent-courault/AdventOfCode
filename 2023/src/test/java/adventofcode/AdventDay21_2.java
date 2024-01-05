package adventofcode;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay21_2 extends Commun {

    static int[][] carte;
    private static HashSet<String> visited;

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
        assertEquals(596734624269210L, traitement(inputs, false, 590));
    }

    public long traitement(List<String> input, boolean etape1, int nbpas) {
        long resultat;
        List<String[]> tmp = new ArrayList<>();
        for (String line : input) {
            tmp.add(line.split(""));
        }
        int departX = 0;
        int departY = 0;
        carte = new int[tmp.get(0).length][tmp.size()];
        for (int i = 0; i < carte.length; i++) {
            for (int j = 0; j < carte[0].length; j++) {
                if (tmp.get(j)[i].equals("S")) {
                    carte[i][j] = 9999;
                    departX = i;
                    departY = j;
                } else {
                    carte[i][j] = tmp.get(j)[i].equals("#") ? -1 : 9999;
                }
            }
        }

        List<Point> points = new ArrayList<>();
        Point depart = new Point(departX, departY);
        points.add(depart);
        visited = new HashSet<>();
        visited.add(depart.toString());
        long nombreDePointsAtteints = 0;
        List<Long> valeurs = new ArrayList<>();

        int index = 0;
        while (index < nbpas) {
            index++;
            List<Point> futursPoints = new ArrayList<>();
            for (Point point : points) {
                Point pointPotentiel = point.add(Direction.N);
                determineSiLePointEstAtteignableEtSiOnYEstPasEncorePasse(pointPotentiel, futursPoints);
                pointPotentiel = point.add(Direction.S);
                determineSiLePointEstAtteignableEtSiOnYEstPasEncorePasse(pointPotentiel, futursPoints);
                pointPotentiel = point.add(Direction.E);
                determineSiLePointEstAtteignableEtSiOnYEstPasEncorePasse(pointPotentiel, futursPoints);
                pointPotentiel = point.add(Direction.O);
                determineSiLePointEstAtteignableEtSiOnYEstPasEncorePasse(pointPotentiel, futursPoints);
            }
            if (index % 2 == (etape1 ? 0 : 1)) {
                nombreDePointsAtteints += futursPoints.size();
                //Pour l'étape 2, on a une périodicité de 262 (2 * la longueur de la carte) avec une valeur initiale à 65
                // Les 65 premiers pas correspondent à l'init et ensuite la situation se reproduit
                // on récupère les valeurs pour faire une régression polynomiale
                if (!etape1 && index % 262 == 65) {
                    valeurs.add(nombreDePointsAtteints);
                }
            }
            points = futursPoints;
        }
        if (etape1) {
            resultat = nombreDePointsAtteints + 1;
        } else {
            final WeightedObservedPoints obs = new WeightedObservedPoints();
            for (int i = 0; i < valeurs.size(); i++) {
                obs.add(i, valeurs.get(i));
            }
            final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
            final double[] coeff = fitter.fit(obs.toList());
            long nbLoop = (26501365 - 65) / 262;
            int coeffX2 = Math.toIntExact(Math.round(coeff[2]));
            int coeffX1 = Math.toIntExact(Math.round(coeff[1]));
            int coeffX0 = Math.toIntExact(Math.round(coeff[0]));
            resultat = nbLoop * nbLoop * coeffX2 + nbLoop * coeffX1 + coeffX0;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static void determineSiLePointEstAtteignableEtSiOnYEstPasEncorePasse(Point candidate, List<Point> futursPoints) {
        if (!visited.contains(candidate.toString())) {
            if (carte[((candidate.x % carte.length) + carte.length) % carte.length]
                    [((candidate.y % carte.length) + carte.length) % carte.length] != -1) {
                futursPoints.add(candidate);
                visited.add(candidate.toString());
            }
        }
    }

    public static class Direction {
        public static final Point N = new Point(0, -1);
        public static final Point S = new Point(0, 1);
        public static final Point E = new Point(1, 0);
        public static final Point O = new Point(-1, 0);
    }

    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }

        public Point add(Point p) {
            return new Point(x + p.x, y + p.y);
        }
    }
}