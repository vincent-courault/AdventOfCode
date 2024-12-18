package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingLong;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay18 extends Commun {
    Grid<Character> carte;
    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals("22", traitement(inputs, 7, 12, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("318", traitement(inputs, 71, 1024, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals("6,1", traitement(inputs, 7, 10, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("56,29", traitement(inputs, 71, 1024, true));
    }

    public String traitement(List<String> inputs, int tailleCarte, int nombreByteEtape1, boolean etape2) {
        String resultat;
        carte = new Grid<>(tailleCarte, tailleCarte, '.');
        IntStream.range(0, nombreByteEtape1).forEach(i -> carte.set(Integer.parseInt(inputs.get(i).split(",")[0]), Integer.parseInt(inputs.get(i).split(",")[1]), '#'));
        parcoursLaCarte(new Coord(0, 0), new Coord(tailleCarte - 1, tailleCarte - 1));
        resultat = String.valueOf(parcoursLaCarte(new Coord(0, 0), new Coord(tailleCarte - 1, tailleCarte - 1)));
        if (etape2) {
            int i;
            int borneMin = 0;
            int borneMax = inputs.size();
            // on va travailler par dichotomie sur l'intervalle
            while (Math.abs(borneMax - borneMin) != 1) {
                i = borneMin + (borneMax - borneMin) / 2; // on prend la moitié de l'intervalle et on rajoute des obstacles
                for (int j = borneMin; j < i; j++) {
                    carte.set(Integer.parseInt(inputs.get(j).split(",")[0]), Integer.parseInt(inputs.get(j).split(",")[1]), '#');
                }
                int resultatParcours = parcoursLaCarte(new Coord(0, 0), new Coord(tailleCarte - 1, tailleCarte - 1));

                if (resultatParcours == 0) {
                    // on n'a pas réussi à atteindre la fin donc la valeur cherchée est dans l'intervalle,
                    // on supprime les obstacles et on déplace la borne max de l'intervalle
                    for (int j = borneMin; j < i; j++) {
                        carte.set(Integer.parseInt(inputs.get(j).split(",")[0]), Integer.parseInt(inputs.get(j).split(",")[1]), '.');
                    }
                    borneMax = i;
                } else {
                    //la valeur cherchée n'est pas dans l'intervalle, on laisse les obstacles et déplace la borne min
                    borneMin = i;
                    resultat = inputs.get(borneMin);
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int parcoursLaCarte(Coord depart, Coord fin) {
        State departS = new State(depart, 0);
        Queue<State> queue = new PriorityQueue<>(comparingLong(s -> s.score));

        queue.add(departS);
        int resultat = Integer.MAX_VALUE;
        Map<String, Integer> visited = new TreeMap<>();
        visited.put(String.valueOf(depart), 0);
        while (!queue.isEmpty()) {
            State etat = queue.poll();

            if (etat.position.equals(fin)) {
                resultat = Math.min(resultat, etat.score);
                continue;
            }

            if (visited.getOrDefault(etat.position + "", Integer.MIN_VALUE) < etat.score) {
                continue;
            }

            // Tout droit
            for (Direction direction : Direction.CARDINAL_DIRECTIONS) {
                Coord suivant = etat.position.deplace(direction);
                if (carte.isValid(suivant) && carte.get(suivant) != '#') {
                    int new_cost = etat.score + 1;
                    if (new_cost < visited.getOrDefault(suivant + "", Integer.MAX_VALUE)) {
                        visited.put(suivant + "", new_cost);
                        queue.add(new State(suivant, new_cost));
                    }
                }
            }
        }
        return resultat != Integer.MAX_VALUE ? resultat : 0;
    }

    public record State(Coord position, Integer score) {
    }
}
