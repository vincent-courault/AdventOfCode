package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay05 extends Commun {


    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(2, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(236, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(0, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(51, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        Pattern vowels = compile("[aeiou].*[aeiou].*[aeiou]");
        Pattern doubles = compile("([a-z])\\1");
        Pattern excluded = compile("ab|cd|pq|xy");
        for (String input : inputs) {
            if (vowels.matcher(input).find()
                    && doubles.matcher(input).find() && !excluded.matcher(input).find()) {
                resultat++;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
public int traitement2(List<String> inputs) {
        int resultat = 0;

        Pattern pair = compile("([a-z][a-z]).*\\1");
        Pattern repeat = compile("([a-z]).\\1");
        for (String input : inputs) {
            if (pair.matcher(input).find()
                    && repeat.matcher(input).find()) {
                resultat++;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
