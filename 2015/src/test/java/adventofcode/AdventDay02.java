package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay02 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(101, traitementEtape1(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1598415, traitementEtape1(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(48, traitementEtape2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(3812909, traitementEtape2(inputs));
    }

    public int traitementEtape2(List<String> inputs) {
        int resultat = 0;
        for (String paquet : inputs) {
            String[] paquetTableau = paquet.split("x");
            int longueur1 = Integer.parseInt(paquetTableau[0]);
            int longueur2 = Integer.parseInt(paquetTableau[1]);
            int longueur3 = Integer.parseInt(paquetTableau[2]);
            resultat += 2 *
                    (longueur1+longueur2+longueur3-Math.max(Math.max(longueur1, longueur2), longueur3)) //somme des deux plus petits cotés
                    +longueur1*longueur2*longueur3; // longueur pour le noeud

        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitementEtape1(List<String> inputs) {
        int resultat = 0;
        for (String paquet : inputs) {
            String[] paquetTableau = paquet.split("x");
            int surface1 = Integer.parseInt(paquetTableau[0]) * Integer.parseInt(paquetTableau[1]);
            int surface2 = Integer.parseInt(paquetTableau[1]) * Integer.parseInt(paquetTableau[2]);
            int surface3 = Integer.parseInt(paquetTableau[2]) * Integer.parseInt(paquetTableau[0]);
            resultat += 2 * (surface1 + surface2 + surface3) + Math.min(Math.min(surface1, surface2), surface3);

        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
