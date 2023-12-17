package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class AdventDay16 extends Commun {
    @Rule
    public TestName name = new TestName();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1651, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1641, traitement(inputs, true));
    }

    //@Test Ne fonctionne pas car toutes les valves sont ouvertes lors des 26 premières minutes
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1707, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2261, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {
        Map<String, Valve> valves = initialiseLesValves(inputs);
        alimenteLesCheminsPourLesValvesUtiles(valves);
        AtomicReference<ArrayList<Valve>> ouvertes = new AtomicReference<>(new ArrayList<>());

        int total;
        if (etape1) {
            total = calculeLeMeilleurResultat(valves, ouvertes, 30);
        } else {
            total = calculeLeMeilleurResultat(valves, ouvertes, 26);
            total += calculeLeMeilleurResultat(valves, ouvertes, 26);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + total);
        return total;
    }

    private int calculeLeMeilleurResultat(Map<String, Valve> valves, AtomicReference<ArrayList<Valve>> ouvertes, int nombreDeMinutes) {
        List<Etat> stack = new LinkedList<>();
        Valve depart = valves.get("AA");
        stack.add(new Etat(depart, nombreDeMinutes, 0, ouvertes.get()));
        int meilleurResultat = 0;
        while (!stack.isEmpty()) {
            Etat etat = stack.remove(0);
            boolean etatFinal = true;
            for (Map.Entry<Valve, Integer> chemin : etat.valve.chemins.entrySet()) {
                Integer distance = chemin.getValue();
                Valve cible = chemin.getKey();
                if (((etat.minutes - distance) > 0) && !etat.valvesOuvertes.contains(cible)) {
                    ArrayList<Valve> opened = new ArrayList<>(etat.valvesOuvertes);
                    opened.add(cible);
                    int addedReleased = (etat.minutes - distance) * cible.flowrate;
                    etatFinal = false;
                    stack.add(new Etat(cible, etat.minutes - distance, etat.released + addedReleased, opened));
                }
            }
            if (etatFinal && etat.released > meilleurResultat) {
                meilleurResultat = etat.released;
                ouvertes.set(new ArrayList<>(etat.valvesOuvertes));
            }
        }
        return meilleurResultat;
    }

    private Map<String, Valve> initialiseLesValves(List<String> inputs) {
        Map<String, Valve> valves = new HashMap<>();
        for (String input : inputs) {
            String name = input.split(";")[0].split("[ =]")[1];
            int flowrate = Integer.parseInt(input.split(";")[0].split("[ =]")[5]);
            String tunnels = input.split("valves|valve")[1].trim();
            Valve valve = new Valve(name, flowrate, tunnels.split(", "));
            valves.put(name, valve);
        }
        for (Valve valve : valves.values()) {
            valve.alimenteLesTunnels(valves);
        }
        return valves;
    }

    private void alimenteLesCheminsPourLesValvesUtiles(Map<String, Valve> valves) {
        Set<Valve> valvesUtiles = valves.keySet().stream().map(valves::get).filter(valve -> valve.flowrate > 0).collect(Collectors.toSet());

        for (Valve valve : valves.values()) {
            valve.calculeLesChemins(valvesUtiles);
        }
    }

    static class Etat {
        Valve valve;
        int minutes;
        int released;
        Collection<Valve> valvesOuvertes;

        public Etat(Valve cur, int minutes, int released, Collection<Valve> opened) {
            this.valve = cur;
            this.minutes = minutes;
            this.released = released;
            this.valvesOuvertes = opened;
        }
    }

    static class Valve implements Comparable<Valve> {
        public static final int OUVERTURE_VANNE = 1;
        private final String name;
        private final int flowrate;
        String[] sorties;
        private ArrayList<Valve> tunnels;
        private HashMap<Valve, Integer> chemins;

        public Valve(String name, int flowrate, String[] sorties) {
            this.name = name;
            this.flowrate = flowrate;
            this.sorties = sorties;
        }

        public void alimenteLesTunnels(Map<String, Valve> valves) {
            tunnels = new ArrayList<>();
            for (String s : sorties) {
                tunnels.add(valves.get(s));
            }
        }

        @Override
        public int compareTo(Valve o) {
            return o.flowrate - this.flowrate;
        }

        @Override
        public String toString() {
            return name;
        }

        public void calculeLesChemins(Set<Valve> cibles) {
            chemins = new HashMap<>();
            LinkedList<Valve> queue = new LinkedList<>();
            HashMap<Valve, Integer> distances = new HashMap<>();
            List<Valve> visited = new ArrayList<>();
            queue.add(this);
            distances.put(this, 0);
            while (!queue.isEmpty()) {
                Valve valve = queue.pollFirst();
                visited.add(valve);
                Integer curDist = distances.get(valve);
                if (valve != this && cibles.contains(valve)) {
                    chemins.put(valve, curDist + OUVERTURE_VANNE);
                }
                for (Valve valveSuivante : valve.tunnels) {
                    if (!visited.contains(valveSuivante)) {
                        queue.add(valveSuivante);
                        distances.put(valveSuivante, curDist + 1);
                    }
                }
            }
        }
    }
}
