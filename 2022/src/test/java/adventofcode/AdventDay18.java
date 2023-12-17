package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AdventDay18 extends Commun {

    @Rule
    public TestName name = new TestName();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(64, traitementEtape1(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(4548, traitementEtape1(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(58, traitementEtape2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2588, traitementEtape2(inputs));
    }

    public int traitementEtape1(List<String> inputs) {
        HashSet<Coord3D> gouttes = initialiseLEspaceDesGouttes(inputs);

        int surface = 0;
        for (Coord3D goutte : gouttes) {
            int facesLibres = 6;
            for (Coord3D voisin : goutte.recupereLesVoisins())
                if (gouttes.contains(voisin)) {
                    facesLibres--;
                }
            surface += facesLibres;
        }

        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + surface);
        return surface;
    }

    public int traitementEtape2(List<String> inputs) {
        HashSet<Coord3D> gouttes = initialiseLEspaceDesGouttes(inputs);

        Coord3D pointDepart = chercheUnPremierPointALAirLibre(gouttes);
        Set<Coord3D> blocsDAir = determineLesBlocsDAirPotentiels(gouttes, pointDepart);

        int surface = 0;
        for (Coord3D coord : blocsDAir) {
            for (Coord3D voisin : coord.recupereLesVoisins()) {
                if (gouttes.contains(voisin)) {
                    surface++;
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + surface);
        return surface;
    }

    private Set<Coord3D> determineLesBlocsDAirPotentiels(HashSet<Coord3D> gouttes, Coord3D pointDepart) {
        LinkedList<Coord3D> queue = new LinkedList<>();
        queue.add(pointDepart);
        Set<Coord3D> blocsDAir = new HashSet<>();
        while (queue.size() > 0) {
            Coord3D coord = queue.poll();
            blocsDAir.add(coord);
            for (Coord3D voisin : coord.recupereLesVoisins()) {
                if (!blocsDAir.contains(voisin) && !gouttes.contains(voisin) && !queue.contains(voisin)
                        && calculeLaDistanceALaGoutteLaPlusProche(gouttes, voisin) <= 2) {
                    queue.add(voisin);
                }
            }
        }
        return blocsDAir;
    }

    private HashSet<Coord3D> initialiseLEspaceDesGouttes(List<String> inputs) {
        HashSet<Coord3D> gouttes = new HashSet<>();
        for (String input : inputs) {
            String[] coords = input.split(",");
            gouttes.add(new Coord3D(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])));
        }
        return gouttes;
    }

    private Coord3D chercheUnPremierPointALAirLibre(HashSet<Coord3D> gouttes) {
        Coord3D minimum = Collections.min(gouttes);

        Coord3D pointDepart = null;
        for (Coord3D d : minimum.recupereLesVoisins()) {
            if (!gouttes.contains(d)) {
                pointDepart = d;
                break;
            }
        }
        return pointDepart;
    }

    public int calculeLaDistanceALaGoutteLaPlusProche(HashSet<Coord3D> gouttes, Coord3D pos) {
        return gouttes.stream().map(goutte -> goutte.calculeDistanceManhattan(pos)).min(Integer::compare).get();
    }

    private record Coord3D(int x, int y, int z) implements Comparable<Coord3D> {

        public String toString() {
            return "(" + x + "," + y + "," + z + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Coord3D other = (Coord3D) obj;
            return x == other.x && y == other.y && z == other.z;
        }

        @Override
        public int compareTo(Coord3D o2) {
            return Integer.compare(x + y + z, o2.x + o2.y + o2.z);
        }

        public List<Coord3D> recupereLesVoisins() {
            List<Coord3D> voisins = new ArrayList<>();
            for (int deltaX = -1; deltaX <= 1; deltaX++) {
                for (int deltaY = -1; deltaY <= 1; deltaY++) {
                    for (int deltaZ = -1; deltaZ <= 1; deltaZ++) {
                        if (!(deltaZ == 0 && deltaX == 0 && deltaY == 0) && (Math.abs(deltaX) + Math.abs(deltaY) + Math.abs(deltaZ) < 2)) {
                            voisins.add(new Coord3D(x + deltaX, y + deltaY, z + deltaZ));
                        }
                    }
                }
            }
            return voisins;
        }

        public int calculeDistanceManhattan(Coord3D o) {
            return Math.abs(o.x - x) + Math.abs(o.y - y) + Math.abs(o.z - z);
        }
    }
}
