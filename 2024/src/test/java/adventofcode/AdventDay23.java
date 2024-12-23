package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AdventDay23 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals("7", traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("1314", traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true, 2);
        assertEquals("co,de,ka,ta", traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("bg,bu,ce,ga,hw,jw,nf,nt,ox,tj,uu,vk,wp", traitement(inputs, false));
    }

    public String traitement(List<String> inputs, boolean etape1) {
        String resultat;
        Map<String, List<String>> connexions = new HashMap<>();
        for (String input : inputs) {
            String[] split = input.split("-");
            if (!connexions.containsKey(split[0])) {
                connexions.put(split[0], new ArrayList<>(Collections.singleton(split[1])));
            } else {
                connexions.get(split[0]).add(split[1]);
            }
            if (!connexions.containsKey(split[1])) {
                connexions.put(split[1], new ArrayList<>(Collections.singleton(split[0])));
            } else {
                connexions.get(split[1]).add(split[0]);
            }
        }
        if (etape1) {
            resultat = String.valueOf(identifieLesTriplesInterconnectesCommencantParT(connexions).size());
        } else {
            Set<String> groupeLePlusLong = chercheLesGroupes(connexions).stream().max(Comparator.comparingInt(Set::size)).orElseThrow();
            resultat = groupeLePlusLong.stream().sorted().collect(Collectors.joining(","));
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private Set<Set<String>> identifieLesTriplesInterconnectesCommencantParT(Map<String, List<String>> connexions) {
        Set<Set<String>> triplesInterconnectes = new HashSet<>();
        for (String ordinateur1 : connexions.keySet()) {
            if (ordinateur1.startsWith("t")) {
                connexions.get(ordinateur1)
                        .forEach(ordinateur2 -> connexions.get(ordinateur2).stream()
                                .filter(ordinateur3 -> !ordinateur3.equals(ordinateur1) && connexions.get(ordinateur3).contains(ordinateur1))
                                .forEach(ordinateur3 -> triplesInterconnectes.add(Set.of(ordinateur1, ordinateur2, ordinateur3))));
            }
        }
        return triplesInterconnectes;
    }

    private List<Set<String>> chercheLesGroupes(Map<String, List<String>> connexions) {
        List<Set<String>> groupes = new ArrayList<>();
        for (String ordinateur : connexions.keySet()) {
            Set<String> groupe = new HashSet<>();
            Queue<String> queue = new ArrayDeque<>();
            queue.add(ordinateur);
            while (!queue.isEmpty()) {
                String ordinateurSuivant = queue.poll();
                if (connexions.get(ordinateurSuivant).containsAll(groupe)) {
                    groupe.add(ordinateurSuivant);
                    queue.addAll(connexions.get(ordinateurSuivant));
                }
            }
            groupes.add(groupe);
        }
        return groupes;
    }
}