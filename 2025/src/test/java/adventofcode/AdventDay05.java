package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay05 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(638, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(14, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(352946349407338L, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean etape1) {
        long resultat;

        List<long[]> intervalles = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (String input : inputs) {
            if (input.contains("-")) {
                String[] split = input.split("-");
                long[] range = {Long.parseLong(split[0]), Long.parseLong(split[1])};
                intervalles.add(range);
            } else if (!input.isEmpty()) {
                ids.add(Long.valueOf(input));
            }
        }
        //Traitement
        if (etape1) {
            resultat = ids.stream().filter(id -> estDansUnDesIntervalles(id, intervalles)).count();
        } else {
            resultat = compteValeursDistinctes(intervalles);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private boolean estDansUnDesIntervalles(Long valeur, List<long[]> intervalles) {
        for (long[] intervalle : intervalles) {
            if (valeur.compareTo(intervalle[0]) >= 0 && valeur.compareTo(intervalle[1]) <= 0) {
                return true;
            }
        }
        return false;
    }

    public long compteValeursDistinctes(List<long[]> intervalles) {
        // On trie les intervalles par min croissant
        intervalles.sort(Comparator.comparingLong(intervalle -> intervalle[0]));

        long total = 0;

        long debutCourant = intervalles.getFirst()[0];
        long finCourante = intervalles.getFirst()[1];

        for (int i = 1; i < intervalles.size(); i++) {
            long debut = intervalles.get(i)[0];
            long fin = intervalles.get(i)[1];

            if (debut <= finCourante + 1) {
                // chevauchement des intervalles → on les fusionne
                // on utilise max car l'intervalle peut être inclus dans l'autre
                finCourante = Math.max(finCourante, fin);
            } else {
                // intervalles disjoints → on ajoute la taille et on repart avec le nouveau
                total += (finCourante - debutCourant + 1);
                debutCourant = debut;
                finCourante = fin;
            }
        }
        // on ajoute le dernier intervalle fusionné
        total += (finCourante - debutCourant + 1);
        return total;
    }
}
