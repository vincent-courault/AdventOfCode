package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class AdventDay24 extends Commun {
    @Rule
    public TestName name = new TestName();
    int maxX, maxY;
    private Set<Coord> frontieres;
    private List<Blizzard> blizzards;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(18, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(279, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(54, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(762, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {
        blizzards = new ArrayList<>();
        frontieres = new HashSet<>();
        for (int y = 0; y < inputs.size(); y++) {
            String line = inputs.get(y);
            for (int x = 0; x < line.length(); x++) {
                switch (line.charAt(x)) {
                    case '#' -> frontieres.add(new Coord(x, y));
                    case '>' -> blizzards.add(new Blizzard(new Coord(x, y), Coord.RIGHT));
                    case '<' -> blizzards.add(new Blizzard(new Coord(x, y), Coord.LEFT));
                    case 'v' -> blizzards.add(new Blizzard(new Coord(x, y), Coord.DOWN));
                    case '^' -> blizzards.add(new Blizzard(new Coord(x, y), Coord.UP));
                }
            }
        }
        maxX = frontieres.stream().map(coord -> coord.x).flatMapToInt(IntStream::of).max().orElse(0);
        maxY = inputs.size() - 1;

        Coord depart = new Coord(1, 0);
        Coord arrivee = new Coord(maxX - 1, maxY);

        frontieres.add(depart.sum(Coord.UP));
        frontieres.add(arrivee.sum(Coord.DOWN));

        int nombreTotalDeMinutes = calculeLeTrajet(depart, arrivee);

        if (!etape1) {
            nombreTotalDeMinutes += calculeLeTrajet(arrivee, depart);
            nombreTotalDeMinutes += calculeLeTrajet(depart, arrivee);
        }

        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + nombreTotalDeMinutes);
        return nombreTotalDeMinutes;
    }

    private int calculeLeTrajet(Coord depart, Coord arrivee) {
        int nombreDeMinutes = 0;
        Set<Coord> atteignables = new HashSet<>();
        atteignables.add(depart);

        do {
            //met à jour la position des blizzards
            blizzards = new ArrayList<>(blizzards.stream().map(this::updateBlizzard).toList());
            Set<Coord> blizzardsPosition = blizzards.stream().map(blizzard -> blizzard.position).collect(Collectors.toSet());
            //détermine les positions atteignables
            Set<Coord> atteignablesAuProchainTour = new HashSet<>();
            for (Coord position : atteignables) {
                for (Coord voisin : position.recupereLesVoisinsDirects())
                    if (!frontieres.contains(voisin) && !blizzardsPosition.contains(voisin))
                        atteignablesAuProchainTour.add(voisin); //se déplacer
                if (!frontieres.contains(position) && !blizzardsPosition.contains(position))
                    atteignablesAuProchainTour.add(position); //rester sur place
            }
            atteignables = atteignablesAuProchainTour;
            nombreDeMinutes++;
        } while (!atteignables.contains(arrivee));
        return nombreDeMinutes;
    }

    public Blizzard updateBlizzard(Blizzard blizzard) {
        Coord coord = blizzard.position.sum(blizzard.orientation);
        if (frontieres.contains(coord)) {
            if (blizzard.orientation.equals(Coord.UP))
                return new Blizzard(new Coord(blizzard.position.x, maxY - 1), blizzard.orientation);
            if (blizzard.orientation.equals(Coord.DOWN))
                return new Blizzard(new Coord(blizzard.position.x, 1), blizzard.orientation);
            if (blizzard.orientation.equals(Coord.RIGHT))
                return new Blizzard(new Coord(1, blizzard.position.y), blizzard.orientation);
            if (blizzard.orientation.equals(Coord.LEFT))
                return new Blizzard(new Coord(maxX - 1, blizzard.position.y), blizzard.orientation);
            return null;
        } else {
            return new Blizzard(coord, blizzard.orientation);
        }
    }

    static class Coord {

        public static final Coord UP = new Coord(0, -1);
        public static final Coord DOWN = new Coord(0, 1);
        public static final Coord LEFT = new Coord(-1, 0);
        public static final Coord RIGHT = new Coord(1, 0);
        public int x;
        public int y;

        public Coord(int r, int c) {
            x = r;
            y = c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(y, x);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Coord other = (Coord) obj;
            if (x != other.x) return false;
            return y == other.y;
        }

        public String toString() {
            return "(" + x + "," + y + ")";
        }

        public Coord sum(Coord o) {
            return new Coord(x + o.x, y + o.y);
        }

        public List<Coord> recupereLesVoisinsDirects() {
            List<Coord> list = new ArrayList<>();
            list.add(new Coord(x, y - 1));
            list.add(new Coord(x, y + 1));
            list.add(new Coord(x - 1, y));
            list.add(new Coord(x + 1, y));

            return list;
        }

    }

    static class Blizzard {
        public Coord position, orientation;

        public Blizzard(Coord position, Coord orientation) {
            this.position = position;
            this.orientation = orientation;
        }
    }

}
