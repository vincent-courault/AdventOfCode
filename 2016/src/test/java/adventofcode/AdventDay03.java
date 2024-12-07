package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay03 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(983, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(6, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1836, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        for (String input : inputs) {
            int[] cotes = Arrays.stream(input.trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
            resultat += estUnTrianglePossible(cotes);
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
    public int traitement2(List<String> inputs) {
        int resultat = 0;
        for (int i = 0; i < inputs.size(); i=i+3) {
            int[] cotes1 = Arrays.stream(inputs.get(i).trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
            int[] cotes2 = Arrays.stream(inputs.get(i+1).trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
            int[] cotes3 = Arrays.stream(inputs.get(i+2).trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
            resultat += estUnTrianglePossible(new int[]{cotes1[0],cotes2[0],cotes3[0]});
            resultat += estUnTrianglePossible(new int[]{cotes1[1],cotes2[1],cotes3[1]});
            resultat += estUnTrianglePossible(new int[]{cotes1[2],cotes2[2],cotes3[2]});
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private int estUnTrianglePossible(int[] cotes) {
        boolean a = cotes[0] + cotes[1] > cotes[2];
        boolean b = cotes[0] + cotes[2] > cotes[1];
        boolean c = cotes[1] + cotes[2] > cotes[0];
        return a && b && c ? 1 : 0;

    }

}
