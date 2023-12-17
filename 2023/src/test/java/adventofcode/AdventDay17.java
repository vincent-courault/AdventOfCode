package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay17 extends Commun {

    private static int indexEtat = 0;
    Map2D<Integer> carte = new Map2D<>(0);

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(102, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(817, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(94, traitement(inputs, false));
    }

    @Test
    public void etape2_exemple2() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier2(this, true);
        assertEquals(71, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(925, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean etape1) {
        int y = 0;
        long resultat;
        for (String line : inputs) {
            for (int x = 0; x < line.length(); x++) {
                carte.set(x, y, Integer.parseInt(String.valueOf(line.charAt(x))));
            }
            y++;
        }

        State etatFinal;
        if (etape1) {
            //on part dans les deux directions possibles depuis le point de départ
            etatFinal = chercheLeCheminLeMoinsCouteux(List.of(
                    new State(new Point(0, 0), 0, Direction.E, 0, indexEtat),
                    new State(new Point(0, 0), 0, Direction.S, 0, indexEtat)));
        } else {
            indexEtat = 0;
            //on part dans les deux directions possibles depuis le point de départ
            etatFinal = chercheLeCheminLeMoinsCouteux(List.of(
                    new StateP2(new Point(0, 0), 0, Direction.E, 0, indexEtat),
                    new StateP2(new Point(0, 0), 0, Direction.S, 0, indexEtat)));
        }
        assert etatFinal != null;
        resultat = etatFinal.getCout();
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public static State chercheLeCheminLeMoinsCouteux(List<State> departs) {
        PriorityQueue<State> queue = new PriorityQueue<>();
        indexEtat = 0;
        Set<String> visited = new HashSet<>();
        for (State depart : departs) {
            queue.add(depart);
            indexEtat++;
        }

        while (!queue.isEmpty()) {
            State etat = queue.poll();
            if (etat.estArriveALaFin()) {
                return etat;
            }
            String hash = etat.getHash();
            if (!visited.contains(hash)) {
                visited.add(hash);
                for (State etatSuivant : etat.determineLesEtatsSuivants()) {
                    queue.add(etatSuivant);
                    indexEtat++;
                }
            }
        }
        return null;
    }

    public static class Map2D<Integer> {
        private final Integer default_value;
        private final HashMap<Long, HashMap<Long, Integer>> map;
        protected long max_x;
        protected long max_y;

        public Map2D(Integer def) {
            this.default_value = def;
            map = new HashMap<>();
        }

        public Integer get(long x, long y) {
            if (map.get(x) == null) return default_value;
            if (map.get(x).get(y) == null) return default_value;
            return map.get(x).get(y);
        }

        public void set(long x, long y, Integer val) {
            map.computeIfAbsent(x, _ -> new HashMap<>());
            map.get(x).put(y, val);
            max_x = Math.max(max_x, x);
            max_y = Math.max(max_y, y);
        }

        public Integer get(Point p) {
            return get(p.x, p.y);
        }
    }

    public static class Point {
        final long x,y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "," + y ;
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

        public static Point tourneADroite(Point direction) {
            if (direction.equals(N)) return E;
            if (direction.equals(S)) return O;
            if (direction.equals(E)) return S;
            if (direction.equals(O)) return N;
            throw new RuntimeException("Direction impossible");
        }

        public static Point tourneAGauche(Point direction) {
            if (direction.equals(N)) return O;
            if (direction.equals(S)) return E;
            if (direction.equals(E)) return N;
            if (direction.equals(O)) return S;
            throw new RuntimeException("Direction impossible");
        }
    }

    public class State implements Comparable<State> {
        Point position;
        Point direction;
        int cout;
        int nb_pas_dans_la_direction;
        int index;

        public State(Point position, int cout, Point direction, int nb_pas, int index) {
            this.position = position;
            this.direction = direction;
            this.cout = cout;
            this.nb_pas_dans_la_direction = nb_pas;
            this.index = index;
        }

        public String getHash() {
            return " " + position + " " + direction + " " + nb_pas_dans_la_direction;
        }

        public long getCout() {
            return cout;
        }

        public boolean estArriveALaFin() {
            return position.x == carte.max_x
                    && position.y == carte.max_y
                    && nb_pas_dans_la_direction <= 3;
        }

        public List<State> determineLesEtatsSuivants() {
            List<State> etatsSuivants = new LinkedList<>();
            if (nb_pas_dans_la_direction > 3) return etatsSuivants;
            if (nb_pas_dans_la_direction <= 2) {
                Point nouveauPoint = position.add(direction);
                int coutDuPoint = carte.get(nouveauPoint);
                if (coutDuPoint > 0) {
                    etatsSuivants.add(new State(nouveauPoint, cout + coutDuPoint, direction, nb_pas_dans_la_direction + 1, indexEtat++));
                }
            }
            Point nouvelleDirection = Direction.tourneAGauche(direction);
            Point nouveauPoint = position.add(nouvelleDirection);
            int coutDuPoint = carte.get(nouveauPoint);
            if (coutDuPoint > 0) {
                etatsSuivants.add(new State(nouveauPoint, cout + coutDuPoint, nouvelleDirection, 1, indexEtat++));
            }

            nouvelleDirection = Direction.tourneADroite(direction);
            nouveauPoint = position.add(nouvelleDirection);
            coutDuPoint = carte.get(nouveauPoint);
            if (coutDuPoint > 0) {
                etatsSuivants.add(new State(nouveauPoint, cout + coutDuPoint, nouvelleDirection, 1, indexEtat++));
            }

            return etatsSuivants;
        }

        @Override
        public int compareTo(State o) {
            if (cout < o.cout) return -1;
            if (cout > o.cout) return 1;
            return Long.compare(index, o.index);
        }
    }

    public class StateP2 extends State {
        public StateP2(Point position, int cost, Point direction, int nb_pas, int index) {
            super(position, cost, direction, nb_pas, index);
        }

        @Override
        public boolean estArriveALaFin() {
            //Pour être arrivé, il faut avoir fait au moins 4 pas dans la direction et pas plus de 10
            return position.x == carte.max_x
                    && position.y == carte.max_y
                    && nb_pas_dans_la_direction >= 4
                    && nb_pas_dans_la_direction <= 10;
        }

        @Override
        public List<State> determineLesEtatsSuivants() {
            LinkedList<State> etatsSuivants = new LinkedList<>();
            if (nb_pas_dans_la_direction > 10) return etatsSuivants;
            if (nb_pas_dans_la_direction <= 9) {
                Point nouveauPoint = position.add(direction);
                int coutDuPoint = carte.get(nouveauPoint);
                if (coutDuPoint > 0) {
                    etatsSuivants.add(new StateP2(nouveauPoint, cout + coutDuPoint, direction, nb_pas_dans_la_direction + 1, indexEtat++));
                }
            }
            if (nb_pas_dans_la_direction >= 4) {
                Point nouvelleDirection = Direction.tourneAGauche(direction);
                Point nouveauPoint = position.add(nouvelleDirection);
                int coutDuPoint = carte.get(nouveauPoint);
                if (coutDuPoint > 0) {
                    etatsSuivants.add(new StateP2(nouveauPoint, cout + coutDuPoint, nouvelleDirection, 1, indexEtat++));
                }
                nouvelleDirection = Direction.tourneADroite(direction);
                nouveauPoint = position.add(nouvelleDirection);
                coutDuPoint = carte.get(nouveauPoint);
                if (coutDuPoint > 0) {
                    etatsSuivants.add(new StateP2(nouveauPoint, cout + coutDuPoint, nouvelleDirection, 1, indexEtat++));
                }
            }
            return etatsSuivants;
        }
    }
}