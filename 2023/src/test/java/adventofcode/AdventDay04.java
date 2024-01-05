package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay04 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(13, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(25174, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(30, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(6420979, traitement2(inputs));
    }

    public int traitement(List<String> scratchCards) {
        int resultat = 0;

        for (String scratchCard : scratchCards) {
            int resultatCarte = 0;
            String[] cartesAGratter = scratchCard.split(":")[1].split("\\|");
            Set<String> tirage = new HashSet<>(Arrays.asList(cartesAGratter[0].trim().split(" ")));
            String[] mesNombres = cartesAGratter[1].trim().split(" ");
            tirage.remove("");
            for (String nombre : mesNombres) {
                if (tirage.contains(nombre)) {
                    if (resultatCarte == 0) {
                        resultatCarte = 1;
                    } else {
                        resultatCarte = 2 * resultatCarte;
                    }
                }
            }
            resultat = resultat + resultatCarte;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> scratchCards) {
        int resultat;

        Integer[] nombreCartes = new Integer[scratchCards.size()];
        Arrays.fill(nombreCartes, 0);
        int numCarte = 0;

        for (String scratchCard : scratchCards) {
            int resultatCarte = 1;
            if (nombreCartes[numCarte] == 0) {
                nombreCartes[numCarte] = 1;
            } else {
                nombreCartes[numCarte]++;
            }

            String[] cartesAGratter = scratchCard.split(":")[1].split("\\|");
            Set<String> tirage = new HashSet<>(Arrays.asList(cartesAGratter[0].trim().split(" ")));
            String[] mesNombres = cartesAGratter[1].trim().split(" ");
            tirage.remove("");

            for (String nombre : mesNombres) {
                if (tirage.contains(nombre)) {
                    nombreCartes[numCarte + resultatCarte] += nombreCartes[numCarte];
                    resultatCarte++;
                }
            }
            numCarte++;
        }
        resultat = Arrays.stream(nombreCartes).reduce(0, Integer::sum);
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}