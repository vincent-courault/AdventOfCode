package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay21 extends Commun {


    private static boolean fight(int[] boss, int[] player) {
        while (true) {
            boss[0] -= Math.max(1, player[1] - boss[2]); // toujours au moins un point de perdu
            if (boss[0] <= 0) return true;
            player[0] -= Math.max(1, boss[1] - player[2]); // toujours au moins un point de perdu
            if (player[0] <= 0) return false;
        }
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(91, traitement(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(158, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {
        int resultat = 0;

        int[][] weapons = {
                {8, 4, 0},
                {10, 5, 0},
                {25, 6, 0},
                {40, 7, 0},
                {74, 8, 0},
        };
        // l'armure est facultative donc on rajoute la combinaison vide
        int[][] armors = {
                {0, 0, 0},
                {13, 0, 1},
                {31, 0, 2},
                {53, 0, 3},
                {75, 0, 4},
                {102, 0, 5},
        };

        int[][] rings = {
                {0, 0, 0},
                {25, 1, 0},
                {50, 2, 0},
                {100, 3, 0},
                {20, 0, 1},
                {40, 0, 2},
                {80, 0, 3},
        };

        int min = 500;
        int max = 0;
        for (int[] weapon : weapons) {
            for (int[] armor : armors) {
                for (int[] ring1 : rings) {
                    for (int[] ring2 : rings) {
                        if (ring1[0] != 0 && ring1 == ring2) continue; //on ne peut pas acheter 2 fois la même bague

                        int[] boss = new int[3];
                        for (int i = 0; i < inputs.size(); i++) {
                            boss[i] = Integer.parseInt(inputs.get(i).split(":")[1].trim());
                        }
                        int[] player = {100,
                                weapon[1] + armor[1] + ring1[1] + ring2[1], //attaque
                                weapon[2] + armor[2] + ring1[2] + ring2[2]}; //défense
                        int cost = weapon[0] + armor[0] + ring1[0] + ring2[0];

                        if (fight(boss, player)) {
                            min = Math.min(min, cost);
                        } else {
                            max = Math.max(max, cost);
                        }
                    }
                }
            }
        }

        System.out.println(min);
        System.out.println(max);

        if (etape1) {
            resultat = min;
        } else {
            resultat = max;
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
