package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static java.math.BigInteger.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay25 extends Commun {

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(19980801, traitement(inputs));
    }

    public int traitement(List<String> inputs) {
        int[] numbers = Arrays.stream(inputs.getFirst()
                        .replaceAll("[^0-9]", " ").split("\\s+"))
                .filter(s -> !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .toArray();
        int row = numbers[0];

        int col = numbers[1];
        int resultat = valueOf(252533).
                modPow(valueOf((row + col - 2) * (row + col - 1) / 2 + col - 1), valueOf(33554393)).
                multiply(valueOf(20151125)).
                mod(valueOf(33554393)).intValue();

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
