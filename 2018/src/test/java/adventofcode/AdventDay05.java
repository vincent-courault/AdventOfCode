package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay05 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(10, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(9060, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(6310, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat=Integer.MAX_VALUE;
        String polymere = inputs.getFirst();
        resultat = reaction(polymere,resultat);
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = Integer.MAX_VALUE;
        String polymere = inputs.getFirst();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (String caractere : alphabet.split("")) {
            String polymereMisAJour = polymere.replaceAll(caractere, "");
            polymereMisAJour = polymereMisAJour.replaceAll(caractere.toUpperCase(), "");
            resultat = reaction(polymereMisAJour, resultat);

        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }


    private static int reaction(String polymere, int resultat) {
        int nbRemoved = Integer.MAX_VALUE;
        while (nbRemoved > 0) {
            String polymereReduit = supprimeLesCouplesDeLettresAvecUneCaseDifferente(polymere);
            nbRemoved = polymere.length() - polymereReduit.length();
            polymere = polymereReduit;
        }
        resultat = Math.min(polymere.length(), resultat);
        return resultat;
    }

    public static String supprimeLesCouplesDeLettresAvecUneCaseDifferente(String s) {
        StringBuilder result = new StringBuilder();

        int i = 0;
        while (i < s.length()) {
            // Si on peut regarder une paire adjacente
            if (i + 1 < s.length()) {
                char c1 = s.charAt(i);
                char c2 = s.charAt(i + 1);

                // Vérifie si c1 et c2 sont la même lettre mais avec casse différente
                if (Character.toLowerCase(c1) == Character.toLowerCase(c2)
                        && c1 != c2) {
                    i += 2; // On saute la paire -> suppression
                    continue;
                }
            }

            // Sinon on ajoute c1
            result.append(s.charAt(i));
            i++;
        }
        return result.toString();
    }

}
