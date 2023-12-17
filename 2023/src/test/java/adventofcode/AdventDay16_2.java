package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay16_2 extends Commun {

    private static final int CASE_POINT = 1;
    private static final int CASE_SLASH = 2;
    private static final int CASE_BACKSLASH = 3;
    private static final int CASE_PIPE = 4;
    private static final int CASE_MOINS = 5;
    private static final int DIRECTION_EST = 10;
    private static final int DIRECTION_OUEST = 20;
    private static final int DIRECTION_SUD = 30;
    private static final int DIRECTION_NORD = 40;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(46, traitement(inputs, true));
    }

    @Test
    public void etape1() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(7496, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(51, traitement(inputs, false));
    }

    @Test
    public void etape2() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(7932, traitement(inputs, false));
    }

    public long traitement(List<String> in, boolean part1) {
        long resultat = 0;

        int[][] carte = new int[in.size()][in.get(0).length()];
        for (int i = 0; i < carte.length; i++) {
            for (int j = 0; j < carte[0].length; j++) {
                String val = in.get(j).split("")[i];
                switch (val) {
                    case "." -> carte[i][j] = CASE_POINT;
                    case "/" -> carte[i][j] = CASE_SLASH;
                    case "\\" -> carte[i][j] = CASE_BACKSLASH;
                    case "|" -> carte[i][j] = CASE_PIPE;
                    case "-" -> carte[i][j] = CASE_MOINS;
                }
            }
        }

        if (part1) {
            resultat = parcoursLaCarteDepuisLePointDOrigineEtLaDirection(carte, -1, 0, DIRECTION_EST);
        } else {
            for (int i = 0; i < carte.length; i++) {
                long resultatIntermediaire = parcoursLaCarteDepuisLePointDOrigineEtLaDirection(carte, -1, i, DIRECTION_EST);
                resultat = Math.max(resultat, resultatIntermediaire);
                resultatIntermediaire = parcoursLaCarteDepuisLePointDOrigineEtLaDirection(carte, carte.length, i, DIRECTION_OUEST);
                resultat = Math.max(resultat, resultatIntermediaire);
                resultatIntermediaire = parcoursLaCarteDepuisLePointDOrigineEtLaDirection(carte, i, -1, DIRECTION_SUD);
                resultat = Math.max(resultat, resultatIntermediaire);
                resultatIntermediaire = parcoursLaCarteDepuisLePointDOrigineEtLaDirection(carte, i, carte.length, DIRECTION_NORD);
                resultat = Math.max(resultat, resultatIntermediaire);
            }
        }
        return resultat;
    }

    private long parcoursLaCarteDepuisLePointDOrigineEtLaDirection(int[][] carte, int x, int y, int dir) {
        Set<Point> points = new HashSet<>();
        Set<Point> pointsVisites = new HashSet<>();
        int[][] carteEnergie = new int[carte.length][carte[0].length];
        Point depart = new Point(x, y, dir);
        points.add(depart);
        while (!points.isEmpty()) {
            points = determineLesPointSuivants(carte, carteEnergie, points, pointsVisites);
        }
        return compteLesValeursDEnergie(carteEnergie);
    }

    private long compteLesValeursDEnergie(int[][] carteEnergie) {
        return stream(carteEnergie).mapToLong(ints -> IntStream.range(0, carteEnergie[0].length).filter(j -> ints[j] > 0).count()).sum();
    }

    private Set<Point> determineLesPointSuivants(int[][] carte, int[][] carteEnergie, Set<Point> points, Set<Point> pointsParcourus) {
        Set<Point> pointsSuivants = new HashSet<>();
        for (Point point : points) {
            int direction = point.direction;
            int prochainX = point.x;
            int prochainY = point.y;
            switch (direction) {
                case DIRECTION_OUEST -> prochainX = prochainX - 1;
                case DIRECTION_EST -> prochainX = prochainX + 1;
                case DIRECTION_NORD -> prochainY = prochainY - 1;
                case DIRECTION_SUD -> prochainY = prochainY + 1;
            }
            if (prochainX < 0 || prochainX >= carte.length || prochainY < 0 || prochainY >= carte[0].length) {
                continue;
            }
            carteEnergie[prochainX][prochainY]++;
            int pointApresDeplacement = carte[prochainX][prochainY];
            switch (pointApresDeplacement) {
                case CASE_POINT -> pointsSuivants.add(new Point(prochainX, prochainY, direction));
                case CASE_SLASH -> {
                    int nouvelleDirection = 0;
                    if (direction == DIRECTION_NORD) nouvelleDirection = DIRECTION_EST;
                    if (direction == DIRECTION_SUD) nouvelleDirection = DIRECTION_OUEST;
                    if (direction == (DIRECTION_EST)) nouvelleDirection = DIRECTION_NORD;
                    if (direction == (DIRECTION_OUEST)) nouvelleDirection = DIRECTION_SUD;
                    pointsSuivants.add(new Point(prochainX, prochainY, nouvelleDirection));
                }
                case CASE_BACKSLASH -> {
                    int nouvelleDirection = 0;
                    if (direction == DIRECTION_NORD) nouvelleDirection = DIRECTION_OUEST;
                    if (direction == DIRECTION_SUD) nouvelleDirection = DIRECTION_EST;
                    if (direction == (DIRECTION_EST)) nouvelleDirection = DIRECTION_SUD;
                    if (direction == (DIRECTION_OUEST)) nouvelleDirection = DIRECTION_NORD;
                    pointsSuivants.add(new Point(prochainX, prochainY, nouvelleDirection));
                }
                case CASE_PIPE -> {
                    if (direction != DIRECTION_EST && direction != DIRECTION_OUEST) {
                        pointsSuivants.add(new Point(prochainX, prochainY, direction));
                    } else {
                        pointsSuivants.add(new Point(prochainX, prochainY, DIRECTION_NORD));
                        pointsSuivants.add(new Point(prochainX, prochainY, DIRECTION_SUD));
                    }
                }
                case CASE_MOINS -> {
                    if (direction == DIRECTION_EST || direction == DIRECTION_OUEST) {
                        pointsSuivants.add(new Point(prochainX, prochainY, direction));
                    } else {
                        pointsSuivants.add(new Point(prochainX, prochainY, DIRECTION_OUEST));
                        pointsSuivants.add(new Point(prochainX, prochainY, DIRECTION_EST));
                    }
                }
            }
        }
        pointsSuivants.removeAll(pointsParcourus);
        pointsParcourus.addAll(pointsSuivants);
        return pointsSuivants;
    }

    static class Point {
        int x;
        int y;
        int direction;

        public Point(int x, int y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y && direction == point.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, direction);
        }
    }
}