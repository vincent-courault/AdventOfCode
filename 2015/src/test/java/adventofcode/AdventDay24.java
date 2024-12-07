package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay24 extends Commun {

    private static long divide(int[] poids, int divisions) {
        int cible = IntStream.of(poids).sum() / divisions;
        long min = Long.MAX_VALUE;
        for (int l = 1; l < poids.length; l++) {
            // on veut tester les différentes combinaisons mais on ne les teste pas toutes dans l'ordre
            // on les teste par nombre de bit activés puisqu'on le plus petit nombre de paquet à mettre
            // 1 bit puis 2 puis 3 ...
            // pour cela on part de (1 << l) - 1 qui vaut 1, 3, 7, 15, 31, ...
            // et ensuite on utilise la fonction next qui permet d'obtenir la prochaine valeur avec le même nombre de bit activé
            // "next lexicographical permutation of bits"
            // on fait ça jusqu'à atteindre la valeur max du int et revenir à -1
            // on passe ensuite au nombre de bit activé suivant
            // Si on a atteint la cible on va chercher toutes les valeurs possibles pour le nombre de bit activé et on prend
            // la valeur la plus petite
            for (int i = (1 << l) - 1; i > 0; i = next(i)) {

                long sum = 0;
                long prod = 1;
                for (int b = 0; b < poids.length; b++) {
                    if ((i & (1 << b)) != 0) {
                        sum += poids[b];
                        prod *= poids[b];
                    }
                }
                if (sum == cible) {
                    min = Math.min(min, prod);
                }
            }
            if (min != Long.MAX_VALUE) {
                return min;
            }
        }
        throw new RuntimeException();
    }

    private static int next(int x) {
        int rightOne = x & -x;
        int nextHigherOneBit = x + rightOne;
        int rightOnesPattern = x ^ nextHigherOneBit;
        rightOnesPattern = (rightOnesPattern) / rightOne;
        rightOnesPattern >>= 2;
        return nextHigherOneBit | rightOnesPattern;
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(10439961859L, traitementOpti(inputs, 3));
        //assertEquals(10439961859L, traitement(inputs, 3));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(72050269L, traitementOpti(inputs, 4));
        //assertEquals(72050269L, traitement(inputs, 4));
    }

    public long traitementOpti(List<String> inputs, int parties) {
        long resultat;
        int[] poids = inputs.stream().mapToInt(Integer::parseInt).toArray();
        resultat = divide(poids, parties);
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public long traitement(List<String> inputs, int parties) {
        long resultat = 0;
        int poidsTotal = inputs.stream().mapToInt(Integer::parseInt).sum();
        int[] poids = inputs.stream().mapToInt(Integer::parseInt).toArray();
        long min = Long.MAX_VALUE;
        int target = poidsTotal / parties;
        int bound = 1 << poids.length;
        int nbMin = 200;
        for (int a1 = 1; a1 < bound; a1++) {
            long sum = 0;
            long product = 1;
            int nb = 0;
            for (int b = 0; b < poids.length; b++) {
                if ((a1 & 1 << b) != 0) {
                    sum += poids[b];
                    product *= poids[b];
                    nb++;
                }
            }
            if (sum == target && product > 0) { //si product > 0 c'est qu'on a dépassé la capacité du type long
                resultat++;
                if (nb <= nbMin) {
                    min = Math.min(min, product);
                    nbMin = nb;
                }
            }

        }
        resultat = min;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
