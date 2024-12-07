package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay16 extends Commun {


    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(103, traitement(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(405, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        Map<String, Integer> scores = new HashMap<>();
        scores.put("children", 3);
        scores.put("cats", 7);
        scores.put("samoyeds", 2);
        scores.put("pomeranians", 3);
        scores.put("akitas", 0);
        scores.put("vizslas", 0);
        scores.put("goldfish", 5);
        scores.put("trees", 3);
        scores.put("cars", 2);
        scores.put("perfumes", 1);

        String sue = inputs.stream().filter(input -> {
            String[] row = input.split(": |, | ");
            int score = 0;
            for (int i = 0; i < 3; i++) {
                String prop = row[i * 2 + 2];
                int value = Integer.parseInt(row[i * 2 + 3]);
                int expect = scores.get(prop);
                if (expect == value) {
                    score++;
                }
            }
            return score == 3;
        }).findFirst().orElseThrow();
        System.out.println(sue);
        resultat = Integer.parseInt(sue.split(":")[0].split(" ")[1]);
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;
        Map<String, Integer> scores = new HashMap<>();
        scores.put("children", 3);
        scores.put("cats", 7);
        scores.put("samoyeds", 2);
        scores.put("pomeranians", 3);
        scores.put("akitas", 0);
        scores.put("vizslas", 0);
        scores.put("goldfish", 5);
        scores.put("trees", 3);
        scores.put("cars", 2);
        scores.put("perfumes", 1);

        String sue = inputs.stream().filter(input -> {
            String[] row = input.split(": |, | ");
            int score = 0;
            for (int i = 0; i < 3; i++) {
                String prop = row[i * 2 + 2];
                int value = Integer.parseInt(row[i * 2 + 3]);
                int expect = scores.get(prop);
                if ("cats".equals(prop) || "trees".equals(prop)) {
                    if (value > expect)
                        score++;
                } else if ("pomeranians".equals(prop) || "goldfish".equals(prop)) {
                    if (value < expect)
                        score++;
                } else if (expect == value) {
                    score++;
                }
            }
            return score == 3;
        }).findFirst().orElseThrow();
        System.out.println(sue);
        resultat = Integer.parseInt(sue.split(":")[0].split(" ")[1]);
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
