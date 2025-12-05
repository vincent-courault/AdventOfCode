package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay04 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(240, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(146622, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4455, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(31848, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {
        int resultat;

        List<String> inputsTries = inputs.stream().sorted().toList();
        Map<Integer, List<Integer>> plages = new HashMap<>();
        int numeroGarde = 0;
        int fallAsleepAt = 0;
        int wakesUpAt;
        for (String input : inputsTries) {
            if (input.contains("Guard #")) {
                numeroGarde = Integer.parseInt(input.split("#")[1].split(" ")[0]);
            }
            if (input.contains("falls asleep")) {
                fallAsleepAt = Integer.parseInt(input.split(":")[1].split("]")[0]);
            }
            if (input.contains("wakes up")) {
                wakesUpAt = Integer.parseInt(input.split(":")[1].split("]")[0]);
                List<Integer> plage = plages.getOrDefault(numeroGarde, new ArrayList<>());
                for (int i = fallAsleepAt; i <= wakesUpAt; i++) {
                    plage.add(i);
                }
                plages.putIfAbsent(numeroGarde, plage);
            }
        }
        List<Garde> gardes = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> plage : plages.entrySet()) {
            Garde garde = new Garde();
            Optional<Map.Entry<Integer, Long>> plusFrequente = plage.getValue().stream()
                    .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue());
            if (plusFrequente.isPresent()) {
                garde.nombreMinutesEndormis = plage.getValue().size();
                garde.minuteLaPlusFrequente = plusFrequente.get().getKey();
                garde.nombreMinuteLaPlusFrequente = Math.toIntExact(plusFrequente.get().getValue());
                garde.numero = plage.getKey();
                gardes.add(garde);
            }
        }
        if (etape1) {
            gardes.sort(Comparator.comparing(o -> o.nombreMinutesEndormis));
        } else {
            gardes.sort(Comparator.comparing(o -> o.nombreMinuteLaPlusFrequente));
        }
        resultat = gardes.getLast().numero * gardes.getLast().minuteLaPlusFrequente;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static class Garde {
        int numero;
        int minuteLaPlusFrequente;
        Integer nombreMinuteLaPlusFrequente;
        Integer nombreMinutesEndormis;
    }

}
