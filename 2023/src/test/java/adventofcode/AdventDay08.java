package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay08 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(2, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(13301, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier2(this, true);
        assertEquals(6, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(7309459565207L, traitement(inputs, false));
    }


    public long traitement(List<String> inputs, boolean etape1) {
        long resultat;
        char[] directions = inputs.get(0).toCharArray();

        Map<String, String> gauche = new HashMap<>();
        Map<String, String> droite = new HashMap<>();
        for (int i = 2; i < inputs.size(); i++) {
            String cle = inputs.get(i).split(" ")[0];
            gauche.put(cle, inputs.get(i).split("\\(")[1].split(",")[0].trim());
            droite.put(cle, inputs.get(i).split(",")[1].split("\\)")[0].trim());
        }

        if (etape1) {
            String positionCourante = "AAA";
            int nombreEtapes = 0;
            while (!positionCourante.equals("ZZZ")) {
                String sens = String.valueOf(directions[nombreEtapes++ % directions.length]);
                if (sens.equals("R")) {
                    positionCourante = droite.get(positionCourante);
                } else {
                    positionCourante = gauche.get(positionCourante);
                }
            }
            resultat = nombreEtapes;
        } else {
            List<String> positionsInitiales = new ArrayList<>(gauche.keySet());
            positionsInitiales.removeIf((e) -> !e.endsWith("A"));

            int[] resultats = new int[positionsInitiales.size()];

            for (int i = 0; i < positionsInitiales.size(); i++) {
                String positionCourante = positionsInitiales.get(i);
                int nombreEtapes = 0;
                while (!positionCourante.endsWith("Z")) {
                    String sens = String.valueOf(directions[nombreEtapes++ % directions.length]);
                    if (sens.equals("R")) {
                        positionCourante = droite.get(positionCourante);
                    } else {
                        positionCourante = gauche.get(positionCourante);
                    }
                }
                resultats[i] = nombreEtapes;
            }
            resultat = resultats[0];
            for (int i = 1; i < resultats.length; i++) {
                resultat = calculeLePPCM(resultat, resultats[i]);
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public static long calculeLePPCM(long a, long b) {
        return (a * b) / calculeLePGCD(a, b);
    }

    public static long calculeLePGCD(long a, long b) {
        if (a < b) {
            return calculeLePGCD(b, a);
        }
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
