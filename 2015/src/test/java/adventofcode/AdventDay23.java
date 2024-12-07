package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay23 extends Commun {

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(307, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(160, traitement(inputs, true));
    }

    public int traitement(List<String> inputs, boolean etape2) {
        int resultat;
        int a = 0;
        if (etape2) {
            a = 1;
        }
        int b = 0;
        int nbInstruction = 0;
        String commande;
        String valeur;
        String registre;
        while (nbInstruction <= inputs.size() - 1) {
            String instruction = inputs.get(nbInstruction);
            commande = instruction.substring(0, 3);
            valeur = instruction.substring(4);

            switch (commande) {
                case "jmp":
                    nbInstruction += Integer.parseInt(valeur);
                    break;
                case "jie":
                    registre = valeur.split(",")[0];
                    if (registre.equals("a") && a % 2 == 0) {
                        nbInstruction += Integer.parseInt(valeur.split(" ")[1]);

                    } else {
                        if (registre.equals("b") && b % 2 == 0) {
                            nbInstruction += Integer.parseInt(valeur.split(" ")[1]);

                        } else {
                            nbInstruction++;
                        }
                    }
                    break;

                case "jio":
                    registre = valeur.split(",")[0];
                    if (registre.equals("a") && a == 1) {
                        nbInstruction += Integer.parseInt(valeur.split(" ")[1]);

                    } else {
                        if (registre.equals("b") && b == 1) {
                            nbInstruction += Integer.parseInt(valeur.split(" ")[1]);

                        } else {
                            nbInstruction++;
                        }
                    }
                    break;

                case "hlf":
                    registre = valeur.split(",")[0];
                    if (registre.equals("a")) {
                        a /= 2;
                    }
                    if (registre.equals("b")) {
                        b /= 2;
                    }
                    nbInstruction++;
                    break;
                case "tpl":
                    if (valeur.equals("a")) {
                        a = 3 * a;
                    }
                    if (valeur.equals("b")) {
                        b = 3 * b;
                    }
                    nbInstruction++;
                    break;
                case "inc":
                    //registre = valeur.split(",")[0];
                    if (valeur.equals("a")) {
                        a++;
                    }
                    if (valeur.equals("b")) {
                        b++;
                    }
                    nbInstruction++;
                    break;

            }

        }
        resultat = b;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
