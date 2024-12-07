package adventofcode;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.tour.RandomTourTSP;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay13 extends Commun {


    public static Map<Set<String>, Integer> parseRelationships(List<String> input) {
        HashMap<Set<String>, Integer> map = new HashMap<>();
        Pattern p = Pattern.compile("(\\w+) would (\\w+) (\\d+) happiness units by sitting next to (\\w+).");

        for (String line : input) {
            Matcher m = p.matcher(line);
            while (m.find()) {
                Set<String> couple = new HashSet<>();
                couple.add(m.group(1));
                couple.add(m.group(4));
                int value = Integer.parseInt(m.group(3)) * ((m.group(2).equals("gain")) ? 1 : -1);
                map.put(couple, map.getOrDefault(couple, 0) + value);
            }
        }
        return map;
    }

    public static Graph<String, DefaultWeightedEdge> constructGraphFromMap(Map<Set<String>, Integer> map) {
        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        for (Map.Entry<Set<String>, Integer> entry : map.entrySet()) {
            String[] couple = entry.getKey().toArray(new String[2]);
            Graphs.addEdgeWithVertices(graph, couple[0], couple[1], entry.getValue());
        }

        return graph;
    }

    private static int determineHighestHappiness(Graph<String, DefaultWeightedEdge> graph) {
        RandomTourTSP<String, DefaultWeightedEdge> randomTour = new RandomTourTSP<>();
        int i = 0;
        int highestHappiness = 0;
        while (i < 200000) {
            GraphPath<String, DefaultWeightedEdge> path = randomTour.getTour(graph);
            int happiness = path.getEdgeList().stream().mapToInt(edge -> (int) graph.getEdgeWeight(edge)).sum();
            if (happiness > highestHappiness) {
                highestHappiness = happiness;
                i = 0;
            }

            i++;
        }
        return highestHappiness;
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(330, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(733, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(286, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(725, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat;
        Map<Set<String>, Integer> relationships = parseRelationships(inputs);
        Graph<String, DefaultWeightedEdge> graph = constructGraphFromMap(relationships);

        resultat = determineHighestHappiness(graph);

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat;
        Map<Set<String>, Integer> relationships = parseRelationships(inputs);
        Graph<String, DefaultWeightedEdge> graph = constructGraphFromMap(relationships);

        String[] sommets = graph.vertexSet().toArray(new String[0]);
        for (String sommet : sommets) {
            Graphs.addEdgeWithVertices(graph, "me", sommet, 0);
        }
        resultat = determineHighestHappiness(graph);

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
