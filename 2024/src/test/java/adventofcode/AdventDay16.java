package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static java.util.Comparator.comparingLong;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay16 extends Commun {
    Grid<Character> carte;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(7036, traitement(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(106512, traitement(inputs, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(45, traitement(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(563, traitement(inputs, true));
    }

    public int traitement(List<String> inputs, boolean etape2) {
        int resultat;
        carte = new Grid<>(inputs, new Divider.Character());
        Coord depart = carte.getStartingPoint('S');
        Coord fin = carte.getStartingPoint('E');
        Map<String, Integer> casesVisitees = parcoursLaCarte(depart, fin);
        resultat = casesVisitees.keySet().stream().
                filter(s -> s.startsWith(fin.toString())).distinct().
                map(s -> casesVisitees.getOrDefault(s, Integer.MAX_VALUE)).
                min(Integer::compareTo).orElseThrow();
        if (etape2) {
            resultat = parcoursLeCheminInverse(casesVisitees, fin, resultat);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public Map<String, Integer> parcoursLaCarte(Coord depart, Coord fin) {
        State departS = new State(depart, Direction.E, 0);
        Queue<State> queue = new PriorityQueue<>(comparingLong(s -> s.score));
        queue.add(departS);
        int resultat = Integer.MAX_VALUE;
        Map<String, Integer> visited = new TreeMap<>();
        visited.put(depart + "" + Direction.E, 0);
        while (!queue.isEmpty()) {
            State etat = queue.poll();

            if (etat.position.equals(fin)) {
                resultat = Math.min(resultat, etat.score);
                continue;
            }

            if (visited.getOrDefault(etat.position + "" + etat.direction, Integer.MIN_VALUE) < etat.score) {
                continue;
            }

            // Tout droit
            Coord suivant = etat.position.deplace(etat.direction);
            if (carte.isValid(suivant) && carte.get(suivant) != '#') {
                int new_cost = etat.score + 1;
                if (new_cost < visited.getOrDefault(suivant + "" + etat.direction, Integer.MAX_VALUE)) {
                    visited.put(suivant + "" + etat.direction, new_cost);
                    queue.add(new State(suivant, etat.direction, new_cost));
                }
            }
            // A Gauche
            Direction directionSuivante = getDirectionSuivanteGauche(etat);
            ajouteLesEtapesSuivantes(etat, directionSuivante, visited, queue);
            // A droite
            directionSuivante = getDirectionSuivanteDroite(etat);
            ajouteLesEtapesSuivantes(etat, directionSuivante, visited, queue);
        }
        return visited;
    }

    private void ajouteLesEtapesSuivantes(State etat, Direction directionSuivante, Map<String, Integer> visited, Queue<State> queue) {
        Coord suivantGauche = etat.position;
        if (carte.isValid(suivantGauche) && carte.get(suivantGauche) != '#') {
            int new_cost = etat.score + 1000;
            if (new_cost <= visited.getOrDefault(suivantGauche + "" + directionSuivante, Integer.MAX_VALUE)) {
                visited.put(suivantGauche + "" + directionSuivante, new_cost);
                queue.add(new State(suivantGauche, directionSuivante, new_cost));
            }
        }
    }

    private Direction getDirectionSuivanteGauche(State etat) {
        if (etat.direction == Direction.E) {
            return Direction.N;
        } else if (etat.direction == Direction.N) {
            return Direction.W;
        } else if (etat.direction == Direction.W) {
            return Direction.S;
        } else {
            return Direction.E;
        }
    }

    private Direction getDirectionSuivanteDroite(State etat) {
        if (etat.direction == Direction.E) {
            return Direction.S;
        } else if (etat.direction == Direction.S) {
            return Direction.W;
        } else if (etat.direction == Direction.W) {
            return Direction.N;
        } else {
            return Direction.E;
        }
    }

    private Direction getDirectionOpposee(State etat) {
        Direction opp = null;
        if (etat.direction == Direction.N)
            opp = Direction.S;
        if (etat.direction == Direction.S)
            opp = Direction.N;
        if (etat.direction == Direction.E)
            opp = Direction.W;
        if (etat.direction == Direction.W)
            opp = Direction.E;
        return opp;
    }

    public int parcoursLeCheminInverse(Map<String, Integer> visited, Coord fin, int longueurParcours) {

        Set<String> surLeChemin = new HashSet<>();
        Queue<State> queue = new LinkedList<>();

        //On cherche la direction ayant conduit à l'arrivée avec le meilleur score
        for (Direction direction : Direction.CARDINAL_DIRECTIONS) {
            String endState = fin + "" + direction;
            if (visited.containsKey(endState) && visited.get(endState) == longueurParcours) {
                surLeChemin.add(endState);
                queue.add(new State(fin, direction, 0));
            }
        }

        while (!queue.isEmpty()) {
            State etat = queue.poll();
            int scoreCourant = visited.get(etat.position + "" + etat.direction);

            Direction opp = getDirectionOpposee(etat);
            Coord newPosition = etat.position.deplace(opp);
            if (carte.isValid(newPosition) && carte.get(newPosition) != '#') {
                int scorePrecedent = scoreCourant - 1;
                if (visited.getOrDefault(newPosition + "" + etat.direction, Integer.MAX_VALUE) == scorePrecedent) {
                    if (!surLeChemin.contains(newPosition + "" + etat.direction)) {
                        State etatPrecedent = new State(newPosition, etat.direction, scorePrecedent);
                        surLeChemin.add(newPosition + "" + etat.direction);
                        queue.add(etatPrecedent);
                    }
                }
            }

            Direction directionSuivante;
            directionSuivante = getDirectionSuivanteGauche(etat);
            if (carte.isValid(etat.position) && carte.get(etat.position) != '#') {
                int scorePrecedent = scoreCourant - 1000;
                if (visited.getOrDefault(etat.position + "" + directionSuivante, Integer.MAX_VALUE) == scorePrecedent) {
                    if (!surLeChemin.contains(etat.position + "" + directionSuivante)) {
                        State etatPrecedent = new State(etat.position, directionSuivante, scorePrecedent);
                        surLeChemin.add(etat.position + "" + directionSuivante);
                        queue.add(etatPrecedent);
                    }
                }
            }

            directionSuivante = getDirectionSuivanteDroite(etat);
            if (carte.isValid(etat.position) && carte.get(etat.position) != '#') {
                int scorePrecedent = scoreCourant - 1000;
                if (visited.getOrDefault(etat.position + "" + directionSuivante, Integer.MAX_VALUE) == scorePrecedent) {
                    if (!surLeChemin.contains(etat.position + "" + directionSuivante)) {
                        State etatPrecedent = new State(etat.position, directionSuivante, scorePrecedent);
                        surLeChemin.add(etat.position + "" + directionSuivante);
                        queue.add(etatPrecedent);
                    }
                }
            }
        }
        return Math.toIntExact(surLeChemin.stream().map(s -> s.split("}")[0]).distinct().count());
    }

    public record State(Coord position, Direction direction, Integer score) {
    }
}
