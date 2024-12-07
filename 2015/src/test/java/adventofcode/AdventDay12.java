package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay12 extends Commun {


    private static int calculeLaSomme(String input) {
        Matcher matcher = Pattern.compile("-?\\d+").matcher(input);
        int resultat = 0;
        while (matcher.find()) {
            resultat += Integer.parseInt(matcher.group(0));
        }
        return resultat;
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(33, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(156366, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(16, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(96852, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        String input = inputs.getFirst();
        int resultat = calculeLaSomme(input);

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        String input = inputs.getFirst();
        Pattern obj = Pattern.compile("\\{[^{}]*}"); //on cherche
        // les objets json {} contenant éventuellement un array may pas un autre objet
        while (input.contains("{")) {
            Matcher m = obj.matcher(input);
            if (m.find()) {
                input = input.replace(m.group(), m.group().contains(":\"red\"") ? "0" : Integer.toString(calculeLaSomme(m.group())));
            }
        }
        int resultat = calculeLaSomme(input);

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
