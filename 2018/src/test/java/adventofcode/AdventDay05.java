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
        assertEquals(10, traitement(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(9060, traitement(inputs, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4, traitement(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(6310, traitement(inputs, true));
    }

    public int traitement(List<String> inputs, boolean etape2) {
        int resultat;
        String polymere = inputs.getFirst();

        polymere = reaction(polymere);
        resultat = polymere.length();
        if (etape2) {
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            for (String caractere : alphabet.split("")) {
                String polymereMisAJour = polymere.replaceAll(caractere, "");
                polymereMisAJour = polymereMisAJour.replaceAll(caractere.toUpperCase(), "");
                resultat = Math.min(resultat, reaction(polymereMisAJour).length());

            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }


    private String reaction(String polymere) {
        int nbRemoved = Integer.MAX_VALUE;
        while (nbRemoved > 0) {
            String polymereReduit = supprimeLesCouplesDeLettresAvecUneCaseDifferente(polymere);
            nbRemoved = polymere.length() - polymereReduit.length();
            polymere = polymereReduit;
        }
        return polymere;
    }

    private String supprimeLesCouplesDeLettresAvecUneCaseDifferente(String s) {
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
                    i += 2; // On saute la paire → suppression
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
