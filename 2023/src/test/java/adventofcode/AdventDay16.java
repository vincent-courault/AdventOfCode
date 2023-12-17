package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay16 extends Commun {

    Map2D<Character> carte = new Map2D<>(' ');
    Map2D<Character> carteEnergie = new Map2D<>(' ');

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(46, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(7496, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(51, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(7932, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean etape1) {
        int y = 0;
        long resultat;
        for (String line : inputs) {
            for (int x = 0; x < line.length(); x++) {
                carte.set(x, y, line.charAt(x));
            }
            y++;
        }

        if (etape1) {
            parcoursLaCarte(0L, 0L, Direction.E);
            resultat = carteEnergie.compteDiese();
        } else {
            resultat = 0;
            for (long x = 0; x <= carte.max_x; x++) {
                carteEnergie = new Map2D<>(' ');
                parcoursLaCarte(x, 0, Direction.S);
                resultat = Math.max(resultat, carteEnergie.compteDiese());

                carteEnergie = new Map2D<>(' ');
                parcoursLaCarte(x, carte.max_y, Direction.N);
                resultat = Math.max(resultat, carteEnergie.compteDiese());
            }

            for (long y1 = 0; y1 <= carte.max_y; y1++) {
                carteEnergie = new Map2D<>(' ');
                parcoursLaCarte(0, y1, Direction.E);
                resultat = Math.max(resultat, carteEnergie.compteDiese());

                carteEnergie = new Map2D<>(' ');
                parcoursLaCarte(carte.max_x, y1, Direction.O);
                resultat = Math.max(resultat, carteEnergie.compteDiese());
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public void parcoursLaCarte(long x, long y, Point direction) {
        State depart = new State(new Point(x, y), direction);
        HashSet<String> visited = new HashSet<>();
        LinkedList<State> queue = new LinkedList<>(List.of(depart));

        while (!queue.isEmpty()) {
            State etat = queue.pollFirst();
            String hash = etat.getHash();
            if (!visited.contains(hash)) {
                visited.add(hash);
                queue.addAll(etat.next());
            }
        }
    }

    public static class Map2D<Character> {
        private final Character default_value;
        private final HashMap<Long, HashMap<Long, Character>> map;
        protected long max_x;
        protected long max_y;

        public Map2D(Character def) {
            this.default_value = def;
            map = new HashMap<>();
        }

        public Character get(long x, long y) {
            if (map.get(x) == null) return default_value;
            if (map.get(x).get(y) == null) return default_value;
            return map.get(x).get(y);
        }

        public void set(long x, long y, Character val) {
            map.computeIfAbsent(x, _ -> new HashMap<>());
            map.get(x).put(y, val);
            max_x = Math.max(max_x, x);
            max_y = Math.max(max_y, y);
        }

        public Character get(Point p) {
            return get(p.x, p.y);
        }

        public void set(Point p, Character val) {
            set(p.x, p.y, val);
        }

        public List<Point> getAllPoints() {
            LinkedList<Point> lst = new LinkedList<>();
            map.forEach((key, value) -> value.keySet().stream().map(character -> new Point(key, character)).forEach(lst::add));
            return lst;
        }

        public Long compteDiese() {
            return getAllPoints().stream().filter(p -> get(p).equals('#')).count();
        }

    }

    public static class Point implements Comparable<Point> {
        final long x;
        final long y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return toString().equals(o.toString());
        }

        @Override
        public int compareTo(Point p) {
            if (x < p.x) return -1;
            if (x > p.x) return 1;
            return Long.compare(y, p.y);
        }

        public Point add(Point p) {
            return new Point(x + p.x, y + p.y);
        }
    }

    public static class Direction {
        public static final Point N = new Point(0, -1);
        public static final Point S = new Point(0, 1);
        public static final Point E = new Point(1, 0);
        public static final Point O = new Point(-1, 0);
    }

    public class State {
        Point position;
        Point direction;

        public State(Point position, Point direction) {
            this.position = position;
            this.direction = direction;
        }

        public String toString() {
            return position.toString() + "/" + direction.toString();
        }

        public String getHash() {
            return toString();
        }

        public List<State> next() {
            List<State> etatsSuivants = new ArrayList<>();
            switch (carte.get(position)) {
                case '.' -> etatsSuivants.add(new State(position.add(direction), direction));
                case '-' -> {
                    if (direction.equals(Direction.E)||direction.equals(Direction.O)) {
                        etatsSuivants.add(new State(position.add(direction), direction));
                    } else {
                        etatsSuivants.add(new State(position.add(Direction.E), Direction.E));
                        etatsSuivants.add(new State(position.add(Direction.O), Direction.O));
                    }
                }
                case '|' -> {
                    if (direction.equals(Direction.N)||direction.equals(Direction.S)) { // point on
                        etatsSuivants.add(new State(position.add(direction), direction));
                    } else {
                        etatsSuivants.add(new State(position.add(Direction.N), Direction.N));
                        etatsSuivants.add(new State(position.add(Direction.S), Direction.S));
                    }
                }
                case '/' -> {
                    Point out = null;
                    if (direction.equals(Direction.N)) out = Direction.E;
                    if (direction.equals(Direction.S)) out = Direction.O;
                    if (direction.equals(Direction.E)) out = Direction.N;
                    if (direction.equals(Direction.O)) out = Direction.S;
                    assert out != null;
                    etatsSuivants.add(new State(position.add(out), out));
                }
                case '\\' -> {
                    Point out = null;
                    if (direction.equals(Direction.N)) out = Direction.O;
                    if (direction.equals(Direction.S)) out = Direction.E;
                    if (direction.equals(Direction.E)) out = Direction.S;
                    if (direction.equals(Direction.O)) out = Direction.N;
                    assert out != null;
                    etatsSuivants.add(new State(position.add(out), out));
                }
                default -> {
                    return etatsSuivants;
                }
            }
            carteEnergie.set(position, '#');
            return etatsSuivants;
        }
    }
}
