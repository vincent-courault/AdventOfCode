package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay15 extends Commun {



    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(18965440, traitement(inputs,false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(15862900, traitement(inputs,true));
    }

    public int traitement(List<String> inputs,boolean etape2) {
        int resultat = 0;

        int[][] ar = {
                {4, -2, 0, 0, 5},
                {0, 5, -1, 0, 8},
                {-1, 0, 5, 0, 6},
                {0, 0, -2, 2, 1}
        };
        int max = 0;
        int nbcalorie;
        for (int frosting = 1; frosting < 96; frosting++) {
            for (int candy = 1; candy < 100 - frosting; candy++) {
                for (int butterscotch = 1; butterscotch < 100 - frosting - candy; butterscotch++) {
                    int sugar = 100 - frosting - candy - butterscotch;
                    int score = 1;
                    for (int i = 0; i < 4; i++) {
                        score *= Math.max(0,
                                ar[0][i] * frosting
                                        + ar[1][i] * candy
                                        + ar[2][i] * butterscotch
                                        + ar[3][i] * sugar);
                    }
                    nbcalorie = ar[0][4] * frosting
                            + ar[1][4] * candy
                            + ar[2][4] * butterscotch
                            + ar[3][4] * sugar;
                    if (nbcalorie<=500|| !etape2)
                        max = Math.max(score, max);
                }
            }
        }


        resultat = max;

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
