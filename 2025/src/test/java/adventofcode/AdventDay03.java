package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay03 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(357, traitement(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(17452, traitement(inputs, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3121910778619L, traitement(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(173300819005913L, traitement(inputs, true));
    }

    public long traitement(List<String> inputs, boolean etape2) {
        long resultat = 0;
        for (String bank : inputs) {
            List<Integer> batteries = new ArrayList<>();
            for (String s : bank.split("")) {
                batteries.add(Integer.parseInt(s));
            }
            resultat += trouveLePlusGrandNombre(batteries, etape2 ? 12 : 2);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private int trouveIndiceMax(List<Integer> valeurs, int limite1, int limite2) {
        int valeurMax = -1;
        int indiceMax = -1;
        for (int i = limite1; i <= valeurs.size() - limite2; i++) {
            if (valeurs.get(i) > valeurMax) {
                valeurMax = valeurs.get(i);
                indiceMax = i;
            }
        }
        return indiceMax;
    }

    private long trouveLePlusGrandNombre(List<Integer> valeurs, int nombreDeChiffre) {
        StringBuilder resultat = new StringBuilder();
        int indice = 0;
        for (int i = nombreDeChiffre; i > 0; i--) {
            indice = trouveIndiceMax(valeurs, indice, i);
            resultat.append(valeurs.get(indice));
            indice++;
        }
        return Long.parseLong(resultat.toString());
    }
}
