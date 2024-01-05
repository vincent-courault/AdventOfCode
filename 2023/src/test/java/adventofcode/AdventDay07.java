package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay07 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(6440, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(254024898, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(5905, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(254115617, traitement(inputs, false));
    }

    public Long traitement(List<String> inputs, boolean etape1) {
        long resultat = 0L;

        List<Hand> hands = new ArrayList<>();
        for (String line : inputs) {
            hands.add(new Hand(line.split(" ")[0], Integer.parseInt(line.split(" ")[1]), etape1));
        }
        Collections.sort(hands);
        for (int i = 0; i < hands.size(); i++) {
            resultat += (long) hands.get(i).mise * (i + 1);
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

   static class Hand implements Comparable<Hand> {
        int mise;
        int forceDeLaMain;
        int[] frequence = new int[13];
        int[] cartes = new int[5];
        String[] ordre = new String[]{"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};

        public Hand(String cardsLine, int bid, boolean etape1) {
            if (!etape1) {
               ordre = new String[]{"A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J"};
            }
            this.mise = bid;
            int nbJokers=0;
            String[] carte = cardsLine.split("");
            for (int i = 0; i < carte.length; i++) {
                for (int j = 0; j < ordre.length; j++) {
                    if (carte[i].equals(ordre[j])) {
                        if (!carte[i].equals("J") || etape1) {
                            frequence[j]++;
                        }
                        if (carte[i].equals("J") && !etape1) {
                            nbJokers++;
                        }
                       cartes[i] = j;
                    }
                }
            }

            Arrays.sort(frequence);
            //Pour obtenir la main la plus forte, le nombre de joker est ajouté à la plus grande valeur présente dans la main
            frequence[frequence.length - 1] += nbJokers;

            // on attribue une force en fonction du nombre max de cartes identiques
            forceDeLaMain = 10 * frequence[frequence.length - 1];
            // on ajoute aussi le cas du deuxième plus grand nombre de cartes identiques (double paire ou full)
            if (frequence[frequence.length - 2] == 2) {
                forceDeLaMain += 5;
            }
        }

        @Override
        public int compareTo(Hand other) {
            if (forceDeLaMain != other.forceDeLaMain) {
                return forceDeLaMain - other.forceDeLaMain;
            } else {
                for (int i = 0; i < cartes.length; i++) {
                    if (cartes[i] != other.cartes[i]) {
                        return other.cartes[i] - cartes[i];
                    }
                }
                return 0;
            }
        }
    }
}
