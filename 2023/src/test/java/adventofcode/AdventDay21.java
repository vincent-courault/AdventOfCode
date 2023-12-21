package adventofcode;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Disabled
public class AdventDay21 extends Commun {

    Map2D carte = new Map2D(' ');
    private Integer maxPas = 1000;
    private Integer nbPosition;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(16, traitement(inputs, 6));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(3605, traitement(inputs, 64));
    }

    public long traitement(List<String> inputs, int nbpas) {
        int y = 0;
        this.maxPas = nbpas;
        this.nbPosition = 0;
        long resultat;
        for (String line : inputs) {
            for (int x = 0; x < line.length(); x++) {
                carte.set(x, y, line.charAt(x));
            }
            y++;
        }
        Point depart = new Point(0, 0);
        for (int i = 0; i < carte.max_x; i++) {
            for (int j = 0; j < carte.max_y; j++) {
                if (carte.get(i, j).equals('S')) {
                    depart = new Point(i, j);
                    break;
                }
            }
        }

        parcoursLaCarte(depart.x, depart.y);
        resultat = nbPosition;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public void parcoursLaCarte(long x, long y) {
        State depart = new State(new Point(x, y), 0);
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

    public static class Point {
        final long x;
        final long y;

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
    }

    public static class Direction {
        public static final Point N = new Point(0, -1);
        public static final Point S = new Point(0, 1);
        public static final Point E = new Point(1, 0);
        public static final Point O = new Point(-1, 0);
    }

    public static class Map2D {
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
            return get(((p.x % max_x) + max_x) % max_x, ((p.y % max_y) + max_y) % max_y);
        }
    }

    public class State {
        Point position;
        Integer nbpas;

        public State(Point position, Integer nbpas) {
            this.position = position;
            this.nbpas = nbpas;
        }

        public String toString() {
            return position.toString() + "/" + nbpas.toString();
        }

        public String getHash() {
            return toString();
        }

        public List<State> next() {
            List<State> etatsSuivants = new ArrayList<>();
            if (carte.get(position).equals('#') || carte.get(position).equals(' ') || nbpas > maxPas) {
                return etatsSuivants;
            }
            etatsSuivants.add(new State(position.add(Direction.N), nbpas + 1));
            etatsSuivants.add(new State(position.add(Direction.S), nbpas + 1));
            etatsSuivants.add(new State(position.add(Direction.E), nbpas + 1));
            etatsSuivants.add(new State(position.add(Direction.O), nbpas + 1));
            if (nbpas.equals(maxPas))
                nbPosition++;
            return etatsSuivants;
        }
    }
}

