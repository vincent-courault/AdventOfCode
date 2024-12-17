package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.join;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay17 extends Commun {
    private static long A, B, C;
    long resultatEtape2 = Long.MAX_VALUE;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals("4,6,3,5,6,3,5,2,1,0", traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("2,1,0,1,7,2,5,0,3", traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, true, 2);
        assertEquals("117440", traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false, 2);
        assertEquals("267265166222235", traitement(inputs, false));
    }

    public String traitement(List<String> inputs, boolean etape1) {
        String resultat;
        A = Integer.parseInt(inputs.get(0).split(": ")[1]);

        List<Long> program = Arrays.stream(inputs.get(4).split(":")[1].trim().split(",")).mapToLong(Long::parseLong).boxed().toList();
        List<Long> resultatEtape1 = calcule(program, A);
        resultat = join(",", resultatEtape1.stream().map(String::valueOf).toArray(String[]::new));

        if (!etape1) {
            calculeRecursifEtape2(program, 0, 0);
            resultat = String.valueOf(resultatEtape2);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    // Le programme en s'exécutant fait ce calcul
    //tant que A !=0
    //x=((((A%8)^7)^(A/128))^7)%8
    //ecrit x
    //A=A/8
    //fin
    // La valeur de x correspond en fin au modulo 8 de A

    private List<Long> calcule(List<Long> program, long depart) {
        A = depart;
        B = 0;
        C = 0;
        int indice = 0;
        List<Long> output = new ArrayList<>();

        while (true) {
            if (indice >= program.size()) {
                return output;
            }
            long opcode = program.get(indice);
            long op = program.get(indice + 1);
            long combo = getCombo(op);

            switch (opcode) {
                case 0L -> {
                    A = A / (1L << combo);
                    indice += 2;
                }
                case 1L -> {
                    B = B ^ op;
                    indice += 2;
                }
                case 2L -> {
                    B = combo % 8;
                    indice += 2;
                }
                case 3L -> {
                    if (A != 0) {
                        indice = Math.toIntExact(op);
                    } else {
                        indice += 2;
                    }
                }
                case 4L -> {
                    B = B ^ C;
                    indice += 2;
                }
                case 5L -> {
                    output.add(combo % 8);
                    indice += 2;
                }
                case 6L -> {
                    B = A / (1L << combo);
                    indice += 2;
                }
                case 7L -> {
                    C = A / (1L << combo);
                    indice += 2;
                }
                default -> throw new IllegalArgumentException("Oups " + opcode);
            }
        }
    }

    private long getCombo(long x) {
        if (x == 0 || x == 1 || x == 2 || x == 3) {
            return x;
        } else if (x == 4) {
            return A;
        } else if (x == 5) {
            return B;
        } else if (x == 6) {
            return C;
        }
        return -1;
    }

    void calculeRecursifEtape2(List<Long> program, long cur, int pos) {
        // A cause du modulo 8 on teste avec les différentes valeurs
        for (int i = 0; i < 8; i++) {
            long nextNum = (cur * 8) + i;
            List<Long> resultat = calcule(program, nextNum);
            // si le résultat du calcul correspond au programme, on peut passer au digit suivant
            if (!program.subList(program.size() - pos - 1, program.size()).equals(resultat)) {
                continue;
            }
            if (pos == program.size() - 1) {
                resultatEtape2 = nextNum;
                return;
            }
            calculeRecursifEtape2(program, nextNum, pos + 1);
        }
    }
}