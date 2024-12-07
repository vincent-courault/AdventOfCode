package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay20 extends Commun {


    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(776160, traitement(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(786240, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        final int TARGET = Integer.parseInt(inputs.getFirst());
        final int MAX = 1000000;
        int[] houses = new int[MAX];

        for (int elf = 1; elf < MAX; elf++) {
            for (int visited = elf; visited < MAX; visited += elf) {
                houses[visited] += elf * 10;
            }
        }

        for (int i = 0; i < MAX; i++) {
            if (houses[i] >= TARGET) {
                resultat = i;
                break;
            }
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;
        final int TARGET = 33100000;
        final int MAX = 1000000;
        int[] houses = new int[MAX];

        for (int elf = 1; elf < MAX; elf++) {
            for (int visited = elf; visited < 50 * elf && visited < MAX; visited += elf) {
                houses[visited] += elf * 11;
            }
        }

        for (int i = 0; i < MAX; i++) {
            if (houses[i] >= TARGET) {
                resultat = i;
                break;
            }
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
