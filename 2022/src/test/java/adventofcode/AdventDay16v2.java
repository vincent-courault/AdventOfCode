package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class AdventDay16v2 extends Commun {

    @Rule
    public TestName name = new TestName();
    private Map<State, Integer> cache = new HashMap<>();

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

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1707, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2261, traitement(inputs, false));
    }

    protected int traitement(final List<String> inputs, boolean etape1) {

        final List<Valve> valves = parseValves(inputs);
        final Valve start = valves.stream().filter(f -> f.getNom().equals("AA")).findFirst().orElseThrow(NoSuchElementException::new);
        cache = new HashMap<>();
        int resultat;
        if (etape1) {
            resultat = calculeLEtat(start, 30, new ArrayList<>(), valves, 0);
        } else {
            resultat = calculeLEtat(start, 26, new ArrayList<>(), valves, 1);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }

    private List<Valve> parseValves(final List<String> input) {
        final List<Valve> valves = new ArrayList<>();
        final Pattern namePattern = Pattern.compile("([A-Z]{2})");
        final Pattern flowPattern = Pattern.compile("\\d+");
        for (final String line : input) {
            final Matcher flowMatcher = flowPattern.matcher(line);
            boolean trouve = flowMatcher.find();
            if (!trouve) {
                throw new RuntimeException("Woops");
            }
            final int flow = Integer.parseInt(flowMatcher.group());
            final Matcher nameMatcher = namePattern.matcher(line);
            trouve = nameMatcher.find();
            if (!trouve) {
                throw new RuntimeException("Woops");
            }
            final String name = nameMatcher.group();
            final Valve valve = new Valve(name, flow);
            while (nameMatcher.find()) {
                final String nomValveVoisine = nameMatcher.group();
                valve.ajouteValveVoisine(nomValveVoisine);
            }
            valves.add(valve);
        }
        return valves;
    }

    public int calculeLEtat(final Valve valveEnCours, final int minute, final List<Valve> valvesOuvertes, final List<Valve> valves, final int nombreDePersonnesSupplementaires) {
        if (minute == 0) {
            if (nombreDePersonnesSupplementaires > 0) {
                final Valve aa = valves.stream().filter(f -> f.getNom().equals("AA")).findFirst().orElseThrow(NoSuchElementException::new);
                return calculeLEtat(aa, 26, valvesOuvertes, valves, nombreDePersonnesSupplementaires - 1);
            }
            return 0;
        }
        final State state = new State(valveEnCours, minute, valvesOuvertes, nombreDePersonnesSupplementaires);
        if (cache.containsKey(state)) {
            return cache.get(state);
        }

        int max = 0;
        if (valveEnCours.getFlowRate() > 0 && !valvesOuvertes.contains(valveEnCours)) {
            valvesOuvertes.add(valveEnCours);
            Collections.sort(valvesOuvertes);
            final int val = (minute - 1) * valveEnCours.getFlowRate() + calculeLEtat(valveEnCours, minute - 1, valvesOuvertes, valves, nombreDePersonnesSupplementaires);
            valvesOuvertes.remove(valveEnCours);
            max = val;
        }

        for (final String nomValveVoisine : valveEnCours.getValvesVoisines()) {
            final Valve valveVoisine = valves.stream().filter(valve -> valve.getNom().equals(nomValveVoisine)).findFirst().orElseThrow(NoSuchElementException::new);
            max = Math.max(max, calculeLEtat(valveVoisine, minute - 1, valvesOuvertes, valves, nombreDePersonnesSupplementaires));
        }
        cache.put(state, max);
        return max;
    }


    public record State(Valve valve, int minute, List<Valve> valvesOuvertes, int nombreDePersonnesSupplementaires) {
    }

    public static class Valve implements Comparable<Valve> {
        private final String nom;
        private final int flowRate;

        private final Set<String> valvesVoisines = new HashSet<>();

        public Valve(final String nom, final int flowRate) {
            this.nom = nom;
            this.flowRate = flowRate;
        }

        public void ajouteValveVoisine(final String valveVoisine) {
            valvesVoisines.add(valveVoisine);
        }

        public String getNom() {
            return this.nom;
        }

        public int getFlowRate() {
            return this.flowRate;
        }

        public Set<String> getValvesVoisines() {
            return this.valvesVoisines;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((nom == null) ? 0 : nom.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            final Valve other = (Valve) obj;
            if (nom == null) {
                return other.nom == null;
            } else return nom.equals(other.nom);
        }

        @Override
        public String toString() {
            return nom;
        }

        @Override
        public int compareTo(final Valve o) {
            return Integer.compare(this.flowRate, o.flowRate);
        }
    }
}