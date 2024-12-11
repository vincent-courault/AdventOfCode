package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay11 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        //assertEquals(55312, traitement(inputs));
        assertEquals(55312, traitement2(inputs, 25));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        //assertEquals(183435, traitement(inputs));
        assertEquals(183435, traitement2(inputs, 25));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(65601038650482L, traitement2(inputs, 75));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(218279375708592L, traitement2(inputs, 75));
    }

    public int traitement(List<String> inputs) {
        int resultat;
        String input = inputs.getFirst();
        String[] valeurs = input.split(" ");
        List<String> pierres = new ArrayList<>(Arrays.asList(valeurs));

        for (int i = 0; i < 25; i++) {
            ArrayList<String> newPierre = new ArrayList<>();
            for (String pierre : pierres) {
                if (pierre.equals("0")) {
                    newPierre.add("1");
                } else if (pierre.length() % 2 == 0) {
                    String pierre1 = pierre.substring(0, pierre.length() / 2).replaceFirst("^0+(?!$)", "");
                    newPierre.add(pierre1);
                    String pierre2 = pierre.substring(pierre.length() / 2).replaceFirst("^0+(?!$)", "");
                    newPierre.add(pierre2);
                } else {
                    newPierre.add(String.valueOf(Long.parseLong(pierre) * 2024));
                }
            }
            pierres = newPierre;
        }
        resultat = pierres.size();
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public long traitement2(List<String> inputs, int blinks) {
        long resultat;
        List<Long> pierres = Arrays.stream(inputs.getFirst().split("\\s+")).map(Long::parseLong).collect(Collectors.toList());
        resultat = transformeEtCompteLesPierres(pierres, blinks);
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    List<Long> transforme(Long pierre) {
        String pierreStr = "" + pierre;
        if (pierre == 0L) {
            return List.of(1L);
        } else if (pierreStr.length() % 2 == 0) {
            return List.of(Long.parseLong(pierreStr.substring(0, pierreStr.length() / 2)),
                    Long.parseLong(pierreStr.substring(pierreStr.length() / 2)));
        } else {
            return List.of(pierre * 2024);
        }
    }

    long transformeEtCompteLesPierres(List<Long> pierres, int blinks) {
        List<Long> pierresSuivantes = new ArrayList<>(pierres);
        Map<Long, Long> nombresPierres = new HashMap<>();
        for (Long pierreSuivante : pierresSuivantes) {
            nombresPierres.put(pierreSuivante, nombresPierres.getOrDefault(pierreSuivante, 0L) + 1);
        }
        for (int i = 0; i < blinks; i++) {
            Map<Long, Long> nombresPierresSuivant = new HashMap<>();
            for (Long pierre : nombresPierres.keySet()) {
                pierresSuivantes = transforme(pierre);
                for (Long pierreSuivante : pierresSuivantes) {
                    nombresPierresSuivant.put(pierreSuivante,
                            nombresPierresSuivant.getOrDefault(pierreSuivante, 0L)
                                    + nombresPierres.get(pierre));
                }
            }
            nombresPierres = nombresPierresSuivant;
        }
        return nombresPierres.values().stream().mapToLong(l -> l).sum();
    }
}
