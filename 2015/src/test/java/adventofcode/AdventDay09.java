package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.tour.RandomTourTSP;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay09 extends Commun {


    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(605, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(141, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(982, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(736, traitement2(inputs));
    }
    private static double calculeLeCheminLePlusLong(Graph<String, DefaultWeightedEdge> graph) {
        RandomTourTSP<String, DefaultWeightedEdge> tourGenerator = new RandomTourTSP<>();

        int i = 0;
        double optimalWeight = 0;
        while (true) {
            GraphPath<String, DefaultWeightedEdge> path = tourGenerator.getTour(graph);
            List<DefaultWeightedEdge> edgeList = path.getEdgeList();
            double weightWithoutFirst = path.getWeight() - graph.getEdgeWeight(edgeList.getFirst());
            double weightWithoutLast = path.getWeight() - graph.getEdgeWeight(edgeList.getLast());
            double heighestWeight = Math.max(weightWithoutFirst, weightWithoutLast);
            if (heighestWeight > optimalWeight) {
                //System.out.printf("Found higher weight %d after %d iterations%n", (int) heighestWeight, i);
                optimalWeight = heighestWeight;
                i = 0;
            }

            if (i == 100000) {
                break;
            }

            i++;
        }
        return optimalWeight;
    }

    private static double calculeLeCheminLePlusCourt(Graph<String, DefaultWeightedEdge> graph) {
        RandomTourTSP<String, DefaultWeightedEdge> tourGenerator = new RandomTourTSP<>();
        int i = 0;
        double longueurOptimale = 10000;
        while (true) {
            GraphPath<String, DefaultWeightedEdge> path = tourGenerator.getTour(graph);
            List<DefaultWeightedEdge> edgeList = path.getEdgeList();
            double weightWithoutFirst = path.getWeight() - graph.getEdgeWeight(edgeList.getFirst());
            double weightWithoutLast = path.getWeight() - graph.getEdgeWeight(edgeList.getLast());
            double lowestWeight = Math.min(weightWithoutFirst, weightWithoutLast);
            if (lowestWeight < longueurOptimale) {
                //System.out.printf("Found lower weight %d after %d iterations%n", (int) lowestWeight, i);
                longueurOptimale = lowestWeight;
                i = 0;
            }

            if (i == 100000) {
                break;
            }

            i++;
        }
        return longueurOptimale;
    }

    private static Graph<String, DefaultWeightedEdge> construitLeGraphe(List<String> inputs) {
        Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        for (String input : inputs) {
            Pattern p = Pattern.compile("(\\w+) to (\\w+) = (\\d+)");
            Matcher m = p.matcher(input);
            if (m.find()) {
                String loc1 = m.group(1);
                String loc2 = m.group(2);
                int dist = Integer.parseInt(m.group(3));
                Graphs.addEdgeWithVertices(graph, loc1, loc2, dist);
            } else {
                throw new RuntimeException("problème avec les données");
            }
        }
        return graph;
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        Graph<String, DefaultWeightedEdge> graph = construitLeGraphe(inputs);

        double lowestWeight = calculeLeCheminLePlusCourt(graph);
        resultat= (int) lowestWeight;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
    public int traitement2(List<String> inputs) {
        int resultat = 0;
        Graph<String, DefaultWeightedEdge> graph = construitLeGraphe(inputs);

        double lowestWeight = calculeLeCheminLePlusLong(graph);
        resultat= (int) lowestWeight;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
