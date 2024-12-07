package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay17 extends Commun {

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(654, traitement(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(57, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        int[] buckets = inputs.stream().mapToInt(Integer::parseInt).toArray();
        int target = 150;
        // on veut tester toutes les combinaisons possibles donc on va faire
        // des opérations binaires avec les valeurs jusqu'à 2^bucket.length
        int bound = 1 << buckets.length;

        for (int a1 = 1; a1 < bound; a1++) {
            int sum = 0;
            for (int b = 0; b < buckets.length; b++) {
                if ((a1 & 1 << b) != 0) { // 1 << b équivalent à 2^b
                    // l'opération permet de vérifier si le bit est à 1 pour cette combinaison et donc si on remplit ce seau
                    sum += buckets[b];
                }
            }
            if (sum == target) {
                resultat++;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
    public int traitement2(List<String> inputs) {
        int nbBucketsMinimum = Integer.MAX_VALUE;
        int resultat = 0;
        int[] buckets = inputs.stream().mapToInt(Integer::parseInt).toArray();
        int target = 150;
        // on veut tester toutes les combinaisons possibles donc on va faire
        // des opérations binaires avec les valeurs jusqu'à 2^bucket.length
        int bound = 1 << buckets.length;
        // On commence par déterminer le nombre minimum de seaux
        for (int a1 = 1; a1 < bound; a1++) {
            int sum = 0;
            int nbBuckets=0;
            for (int b = 0; b < buckets.length; b++) {
                if ((a1 & 1 << b) != 0) {
                    sum += buckets[b];
                    nbBuckets++;
                }
            }
            if (sum == target) {
                nbBucketsMinimum=Math.min(nbBucketsMinimum,nbBuckets);
            }
        }
        // On recalcule pour savoir combien de combinaison correspondent à la valeur mini
        for (int a1 = 1; a1 < bound; a1++) {
            int sum = 0;
            int nbBuckets=0;
            for (int b = 0; b < buckets.length; b++) {
                if ((a1 & 1 << b) != 0) { // 1 << b équivalent à 2^b
                    sum += buckets[b];
                    nbBuckets++;
                }
            }
            if (sum == target && nbBuckets == nbBucketsMinimum) {
                resultat++;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
