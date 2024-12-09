package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay03 extends Commun {


    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(161, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(187825547, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(48, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(85508223, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        for (String input : inputs) {
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                resultat += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }


    public int traitement2(List<String> inputs) {
        int resultat = 0;
        boolean compte = true;
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)");
        for (String input : inputs) {
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                if (matcher.group().startsWith("mul")) {
                    if (compte) {
                        resultat += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
                    }
                } else if (matcher.group().equals("do()")) {
                    compte = true;
                } else if (matcher.group().equals("don't()")) {
                    compte = false;
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
