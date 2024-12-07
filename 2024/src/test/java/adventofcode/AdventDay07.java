package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay07 extends Commun {

    static boolean concatActif = false;

    static List<String> creeEquations(int[] nombres, char operateur) {
        if (nombres.length == 2) {
            switch (operateur) {
                case '+' -> {
                    return new ArrayList<>(List.of(nombres[0] + " + " + nombres[1]));
                }
                case '*' -> {
                    return new ArrayList<>(List.of(nombres[0] + " * " + nombres[1]));
                }
                case '|' -> {
                    return new ArrayList<>(List.of(nombres[0] + " | " + nombres[1]));
                }
            }
        }
        List<String> sousListe = new ArrayList<>();
        sousListe.addAll(creeEquations(Arrays.copyOfRange(nombres, 1, nombres.length), '+'));
        sousListe.addAll(creeEquations(Arrays.copyOfRange(nombres, 1, nombres.length), '*'));
        if (concatActif) {
            sousListe.addAll(creeEquations(Arrays.copyOfRange(nombres, 1, nombres.length), '|'));
        }

        List<String> equations = new ArrayList<>();
        for (String s : sousListe) {
            equations.add(nombres[0] + " " + operateur + " " + s);
        }

        return equations;
    }

    static long calcule(String operation) {
        String[] elements = operation.split(" ");
        long result = Long.parseLong(elements[0]);
        char operateur = elements[1].charAt(0);

        for (int i = 2; i < elements.length; i++) {
            if (i % 2 == 0) {
                long other = Long.parseLong(elements[i]);
                switch (operateur) {
                    case '+':
                        result += other;
                        break;
                    case '*':
                        result *= other;
                        break;
                    case '|':
                        result = Long.parseLong(result + "" + other);
                        break;
                }
            } else {
                operateur = elements[i].charAt(0);
            }
        }

        return result;
    }

    static boolean calculeModeDfs(long valeur, long[] nombres, int i, long total, boolean etape2) {

        if (i == nombres.length) {
            return total == valeur;
        }
        if (total > valeur) {
            return false;
        }
        // addition
        boolean resultat = calculeModeDfs(valeur, nombres, i + 1, total + nombres[i], etape2);
        // multiplication
        if(!resultat)
            resultat = calculeModeDfs(valeur, nombres, i + 1, total != 0 ? total * nombres[i] : nombres[i], etape2);
        // concatenation
        if (!resultat && etape2) {
            long totalConcat = Long.parseLong("" + total + nombres[i]);
            resultat = calculeModeDfs(valeur, nombres, i + 1, totalConcat, true);
        }
        return resultat;
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3749, traitement2(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2654749936343L, traitement2(inputs, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(11387, traitement2(inputs, true));
    }

    //@Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(124060392153684L, traitement(inputs, true));
    }

    @Test
    public void etape22() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(124060392153684L, traitement2(inputs, true));
    }
    //pas perf, on génère toutes les équations et on les calcule
    public long traitement(List<String> inputs, boolean etape2) {
        long resultat = 0;
        concatActif = etape2;
        for (String s : inputs) {
            String[] in = s.split(": ");
            long valeur = Long.parseLong(in[0]);
            int[] nums = Arrays.stream(in[1].split(" ")).mapToInt(Integer::parseInt).toArray();

            List<String> equations = new ArrayList<>();
            equations.addAll(creeEquations(nums, '+'));
            equations.addAll(creeEquations(nums, '*'));
            if (concatActif) {
                equations.addAll(creeEquations(nums, '|'));
            }
            for (String equation : equations) {
                if (calcule(equation) == valeur) {
                    resultat += valeur;
                    break; //on s'arrete à la première équation ok
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public long traitement2(List<String> inputs, boolean etape2) {
        long resultat = 0;
        for (String line : inputs) {
            String[] split = line.split(": ");
            long valeur = Long.parseLong(split[0]);
            long[] nombres = Arrays.stream(split[1].split(" ")).mapToLong(Long::parseLong).toArray();
            if (calculeModeDfs(valeur, nombres, 0, 0, etape2)) {
                resultat += valeur;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
