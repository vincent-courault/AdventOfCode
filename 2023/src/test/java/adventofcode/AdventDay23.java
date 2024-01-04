package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay23 extends Commun {

    Map2D carte = new Map2D(' ');
    int max = 0;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(94, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2366, traitement(inputs));
    }

    public long traitement(List<String> inputs) {
        int y = 0;
        long resultat;
        for (String line : inputs) {
            for (int x = 0; x < line.length(); x++) {
                carte.set(x, y, line.charAt(x));
            }
            y++;
        }
        Point depart = new Point(1, 0);
        Point fin = new Point(carte.max_x - 1, carte.max_y);

        parcoursLaCarte(depart, fin);
        resultat = max;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public void parcoursLaCarte(Point depart, Point fin) {
        State departS = new State(depart, fin, new TreeSet<>(), 0);
        List<State> queue = new ArrayList<>(List.of(departS));
        while (!queue.isEmpty()) {
            State etat = queue.removeFirst();
            queue.addAll(etat.next());
        }
    }

    public static class Point implements Comparable<Point> {
        final long x, y;

        public Point(long x, long y) {
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

        @Override
        public int compareTo(Point o) {
            return o.toString().compareTo(this.toString());
        }
    }

    public static class Direction {
        public static final Point N = new Point(0, -1);
        public static final Point S = new Point(0, 1);
        public static final Point E = new Point(1, 0);
        public static final Point O = new Point(-1, 0);
    }

    public static class Map2D {
        private final Character default_value;
        private final TreeMap<Long, TreeMap<Long, Character>> map;
        protected long max_x, max_y;

        public Map2D(Character def) {
            this.default_value = def;
            map = new TreeMap<>();
        }

        public Character get(long x, long y) {
            if (map.get(x) == null) return default_value;
            if (map.get(x).get(y) == null) return default_value;
            return map.get(x).get(y);
        }

        public void set(long x, long y, Character val) {
            map.computeIfAbsent(x, _ -> new TreeMap<>());
            map.get(x).put(y, val);
            max_x = Math.max(max_x, x);
            max_y = Math.max(max_y, y);
        }

        public Character get(Point p) {
            return get(p.x, p.y);
        }
    }

    public class State {
        Point position;
        Point fin;
        Integer nbSteps;
        Set<Point> visited;

        public State(Point position, Point fin, Set<Point> visited, Integer nbSteps) {
            this.position = position;
            this.nbSteps = nbSteps;
            this.fin = fin;
            this.visited = visited;
        }

        public String toString() {
            return position.toString() + "/" + nbSteps.toString();
        }

        public List<State> next() {
            List<State> etatsSuivants = new ArrayList<>();
            if (position.x == fin.x && position.y == fin.y) {
                max = Math.max(max, nbSteps);
                return etatsSuivants;
            }

            visited.add(position);
            if (carte.get(position).equals('.')) {
                Point pointSuivantN = position.add(Direction.N);
                if (!visited.contains(pointSuivantN) && !carte.get(pointSuivantN).equals('#'))
                    etatsSuivants.add(new State(pointSuivantN, fin, new TreeSet<>(visited), nbSteps + 1));
                Point pointSuivantS = position.add(Direction.S);
                if (!visited.contains(pointSuivantS) && !carte.get(pointSuivantS).equals('#'))
                    etatsSuivants.add(new State(pointSuivantS, fin, new TreeSet<>(visited), nbSteps + 1));
                Point pointSuivantE = position.add(Direction.E);
                if (!visited.contains(pointSuivantE) && !carte.get(pointSuivantE).equals('#'))
                    etatsSuivants.add(new State(pointSuivantE, fin, new TreeSet<>(visited), nbSteps + 1));
                Point pointSuivantO = position.add(Direction.O);
                if (!visited.contains(pointSuivantO) && !carte.get(pointSuivantO).equals('#'))
                    etatsSuivants.add(new State(pointSuivantO, fin, new TreeSet<>(visited), nbSteps + 1));
            }
            if (carte.get(position).equals('>')) {
                Point pointSuivantE = position.add(Direction.E);
                if (!visited.contains(pointSuivantE) && !carte.get(pointSuivantE).equals('#'))
                    etatsSuivants.add(new State(pointSuivantE, fin, new TreeSet<>(visited), nbSteps + 1));

            }
            if (carte.get(position).equals('v')) {
                Point pointSuivantS = position.add(Direction.S);
                if (!visited.contains(pointSuivantS) && !carte.get(pointSuivantS).equals('#'))
                    etatsSuivants.add(new State(pointSuivantS, fin, new TreeSet<>(visited), nbSteps + 1));
            }

            return etatsSuivants;
        }
    }

}

