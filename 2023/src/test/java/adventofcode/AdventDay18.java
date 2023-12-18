package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay18 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(62, traitement(inputs, true));
    }

    @Test
    public void etape1_exemple_0() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier2(this, true);
        assertEquals(43, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(61865, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(952408144115L, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(40343619199142L, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean etape1) {
        long resultat;
        List<Instruction> instructions = new ArrayList<>();
        int perimetre;
        for (String line : inputs) {
            instructions.add(new Instruction(line, etape1));
        }
        perimetre = instructions.stream().mapToInt(value -> value.distance).sum();

        Point sommet = new Point(0, 0);
        List<Point> sommets = new ArrayList<>();
        for (Instruction instruction : instructions) {
            sommet = sommet.deplace(instruction.direction, instruction.distance);
            sommets.add(sommet);
        }

        //Shoelace formula
        resultat = IntStream.range(0, sommets.size() - 1)
                .mapToLong(i -> sommets.get(i + 1).x * sommets.get((i)).y - sommets.get(i).x * sommets.get((i + 1)).y)
                .sum();
        // inutile si valeur initiale à x=0 et y=0
        resultat += sommets.get(0).x * sommets.get(sommets.size() - 1).y
                - sommets.get(sommets.size() - 1).x * sommets.get(0).y;
        resultat =Math.abs(resultat) / 2;

        // Correction pour prendre en compte que la coordonnée du point utilisée par la formule
        // correspond au centre du carré, on ne compte ainsi en moyenne que la moitié de la case
        // pour le contour de la zone
        resultat +=  (perimetre / 2 + 1);

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);

        return resultat;
    }

    static class Instruction {
        int distance;
        Point direction;

        public Instruction(String line, boolean etape1) {
            if (etape1) {
                distance = Integer.parseInt(line.split(" ")[1]);
                switch (line.split(" ")[0]) {
                    case "R" -> direction = Direction.R;
                    case "L" -> direction = Direction.L;
                    case "U" -> direction = Direction.U;
                    case "D" -> direction = Direction.D;
                }
            } else {
                String hex = line.split("[#)]")[1];
                switch (hex.substring(hex.length() - 1)) {
                    case "0" -> direction = Direction.R;
                    case "1" -> direction = Direction.D;
                    case "2" -> direction = Direction.L;
                    case "3" -> direction = Direction.U;
                }
                distance = Integer.parseInt(hex.substring(0, hex.length() - 1), 16);
            }
        }
    }

    public static class Point {
        final long x;
        final long y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public Point deplace(Point direction, int distance) {
            return new Point(x + direction.x * distance, y + direction.y * distance);
        }
    }

    public static class Direction {
        public static final Point U = new Point(0, 1);
        public static final Point D = new Point(0, -1);
        public static final Point R = new Point(1, 0);
        public static final Point L = new Point(-1, 0);
    }
}
