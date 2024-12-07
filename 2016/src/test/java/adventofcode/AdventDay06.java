package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay06 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals("easter", traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("xdkzukcf", traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals("advent", traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("cevsgyvd", traitement(inputs, false));
    }

    public String traitement(List<String> inputs, boolean etape1) {
        List<List<Character>> listes = new ArrayList<>();
        for (int i = 0; i < inputs.getFirst().length(); i++) {
            listes.add(new ArrayList<>());
        }
        for (String input : inputs) {
            char[] array = input.toCharArray();
            for (int i = 0; i < array.length; i++) {
                Character car = array[i];
                listes.get(i).add(car);
            }
        }
        StringBuilder resultat = new StringBuilder();
        if (etape1) {
            for (List<Character> liste : listes) {

                resultat.append(liste.stream()
                        .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                        .entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(null));
            }
        }else{
            for (List<Character> liste : listes) {

                resultat.append(liste.stream()
                        .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                        .entrySet().stream()
                        .min(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(null));
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat.toString();
    }

}
