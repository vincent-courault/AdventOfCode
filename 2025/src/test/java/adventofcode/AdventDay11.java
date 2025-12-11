package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay11 extends Commun {
    Map<State, Long> cache = new HashMap<>();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(5, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(643, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true, 2);
        assertEquals(2, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(417190406827152L, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean etape1) {
        long resultat;

        Map<String, List<String>> graphe = creeGraphe(inputs);
        if (etape1) {
            resultat = dfsSimple("you", "out", graphe, new HashSet<>());
        } else {
            cache.clear();
            Set<String> obligatoire = new HashSet<>();
            obligatoire.add("fft");
            obligatoire.add("dac");
            resultat = dfsAvecEtat("svr", "out", graphe, new HashSet<>(), obligatoire);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    Map<String, List<String>> creeGraphe(List<String> inputs) {
        Map<String, List<String>> graphe = new HashMap<>();

        inputs.stream().map(line -> line.split(":")).forEach(parts -> {
            String noeud = parts[0];
            List<String> voisins = Arrays.stream(parts[1].split(" ")).collect(Collectors.toList());
            graphe.put(noeud, voisins);
        });
        return graphe;
    }

    int dfsSimple(String current, String end, Map<String, List<String>> graph, Set<String> visited) {
        if (current.equals(end)) {
            return 1;
        }

        visited.add(current);
        int count = 0;

        for (String next : graph.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(next)) {
                count += dfsSimple(next, end, graph, visited);
            }
        }

        visited.remove(current);
        return count;
    }

    private long dfsAvecEtat(String current, String end, Map<String, List<String>> graph,
                             Set<String> visited, Set<String> remaining) {
        State state = new State(current, remaining);
        if (cache.containsKey(state)) {
            return cache.get(state);
        }

        if (current.equals(end)) {
            long ok = remaining.isEmpty() ? 1L : 0L;
            cache.put(state, ok);
            return ok;
        }

        visited.add(current);
        Set<String> updated = new HashSet<>(remaining);
        updated.remove(current);

        long total = 0;

        for (String next : graph.getOrDefault(current, List.of())) {
            if (!visited.contains(next)) {
                total += dfsAvecEtat(next, end, graph, visited, updated);
            }
        }

        visited.remove(current);
        cache.put(state, total);
        return total;
    }

    static class State {
        String node;
        String keyMandatory;

        State(String node, Set<String> remaining) {
            this.node = node;
            List<String> sorted = new ArrayList<>(remaining);
            Collections.sort(sorted);
            this.keyMandatory = String.join(",", sorted);
        }

        @Override
        public int hashCode() {
            return Objects.hash(node, keyMandatory);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof State s)) {
                return false;
            }
            return node.equals(s.node) && keyMandatory.equals(s.keyMandatory);
        }
    }
}
