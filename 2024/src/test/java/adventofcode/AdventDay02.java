package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay02 extends Commun {

    private static <T> List<List<T>> genererSousListes(List<T> liste) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < liste.size(); i++) {
            List<T> sousListe = new ArrayList<>(liste);
            sousListe.remove(i);
            result.add(sousListe);
        }
        return result;
    }

    private static boolean verifieRapport(List<String> rapport) {
        int diff0 = Integer.parseInt(rapport.get(1)) - Integer.parseInt(rapport.get(0));
        for (int i = 1; i < rapport.size(); i++) {
            int diff = Integer.parseInt(rapport.get(i)) - Integer.parseInt(rapport.get(i - 1));
            if (Math.signum(diff) != Math.signum(diff0)) {
                return false;
            }
            if (diff > 3 || diff == 0 || diff < -3) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(2, traitementEtape1(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(390, traitementEtape1(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4, traitementEtape2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(439, traitementEtape2(inputs));
    }

    public int traitementEtape2(List<String> inputs) {
        int resultat = 0;
        for (String rapport : inputs) {
            List<String> rapportEnListe = new java.util.ArrayList<>(List.of(rapport.split(" ")));
            boolean controleOK = verifieRapport(rapportEnListe);
            if (controleOK) {
                resultat++;
            } else {
                List<List<String>> sousListes = genererSousListes(rapportEnListe);
                for (List<String> sousListe : sousListes) {
                    controleOK = verifieRapport(sousListe);
                    if (controleOK) {
                        resultat++;
                        break;
                    }
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitementEtape1(List<String> inputs) {
        int resultat = 0;
        for (String rapport : inputs) {
            List<String> rapportEnListe = new java.util.ArrayList<>(List.of(rapport.split(" ")));
            boolean controleOK = verifieRapport(rapportEnListe);
            if (controleOK) {
                resultat++;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
