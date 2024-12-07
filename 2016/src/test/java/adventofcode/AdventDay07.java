package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay07 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(2, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(115, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(0, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(231, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        Pattern pattern = Pattern.compile("([a-z])([a-z])\\2\\1");
        String partie;
        String[] parties;
        boolean insideBracket;
        boolean outsideBracket;
        for (String input : inputs) {
            insideBracket = false;
            outsideBracket = false;
            parties = input.split("[\\[\\]]");

            for (int i = 0; i < parties.length; i++) {
                partie = parties[i];
                Matcher matcher = pattern.matcher(partie);
                while (matcher.find()) {
                    if (matcher.group().charAt(0) != matcher.group().charAt(1)) {
                        if (i % 2 == 0) {
                            outsideBracket = true;
                        } else {
                            insideBracket = true;
                        }
                    }
                }
            }
            if (outsideBracket && !insideBracket) {
                resultat++;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;
        Pattern pattern = Pattern.compile("([a-z])([a-z])\\1");
        String partie;
        String[] parties;
        List<String> insideBracket;
        List<String> outsideBracket;
        for (String input : inputs) {
            insideBracket = new ArrayList<>();
            outsideBracket = new ArrayList<>();
            parties = input.split("[\\[\\]]");

            for (int i = 0; i < parties.length; i++) {
                partie = parties[i];
                for (int j = 0; j <= partie.length() - 3; j++) {
                    Matcher matcher = pattern.matcher(partie.substring(j));
                    while (matcher.find()) {
                        if (matcher.group().charAt(0) != matcher.group().charAt(1)) {
                            if (i % 2 == 0) {
                                outsideBracket.add(matcher.group());
                            } else {
                                insideBracket.add(matcher.group());
                            }
                        }
                    }
                }
            }
            if (!insideBracket.isEmpty() && !outsideBracket.isEmpty()) {
                for (String s : insideBracket) {
                    String bab = String.valueOf(s.charAt(1)) + s.charAt(0) + s.charAt(1);
                    if (outsideBracket.contains(bab)) {
                        resultat++;
                        break;
                    }
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}