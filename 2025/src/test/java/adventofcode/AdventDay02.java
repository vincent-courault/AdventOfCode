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
        assertEquals(1227775554L, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(31839939622L, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4174379265L, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(41662374059L, traitement2(inputs));
    }

    public long traitement(List<String> inputs) {
        long resultat = 0;
        String[] plages = inputs.getFirst().split(",");
        for (String plage : plages) {
            String[] parts = plage.split("-");
            long debut = Long.parseLong(parts[0]);
            long fin = Long.parseLong(parts[1]);
            // Pour chaque valeur de la plage, on coupe la chaine en 2 et on compare.
            // Si la longueur est impaire, c'est forcément ko.
            for (long valeur = debut; valeur <= fin; valeur++) {
                String valeurATester = String.valueOf(valeur);
                int longueur = valeurATester.length();
                if (longueur % 2 == 0) {
                    int milieu = valeurATester.length() / 2;
                    if (valeurATester.substring(0, milieu).equals(valeurATester.substring(milieu))) {
                        resultat += valeur;
                    }
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public long traitement2(List<String> inputs) {
        long resultat = 0;
        String[] plages = inputs.getFirst().split(",");

        for (String plage : plages) {
            String[] parts = plage.split("-");
            long debut = Long.parseLong(parts[0]);
            long fin = Long.parseLong(parts[1]);
            // pour chaque valeur de la plage, on double la chaine et on enlève les bords
            // et on vérifie la présence de la chaine d'origine
            for (long valeur = debut; valeur <= fin; valeur++) {
                String valeurATester = String.valueOf(valeur);
                if ((valeurATester + valeurATester).substring(1, 2 * valeurATester.length() - 1).contains(valeurATester)) {
                    resultat += valeur;
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
