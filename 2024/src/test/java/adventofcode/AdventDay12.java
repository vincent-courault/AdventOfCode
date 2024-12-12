package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay12 extends Commun {
    static Grid<Character> carte;
    Set<Coord> visited;

    private static int verifieLaPosition(Coord possible, Character valeurZone, Queue<Coord> queue) {
        int perimetre = 0;
        if (carte.isValid(possible.ligne, possible.colonne)
                && carte.get(possible.ligne, possible.colonne) == valeurZone) {
            queue.add(possible);
        } else {
            perimetre++;
        }
        return perimetre;
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1930, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1396298, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1206, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(853588, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {
        int resultat = 0;
        carte = new Grid<>(inputs, new Divider.Character());
        visited = new HashSet<>();
        for (int i = 0; i < carte.getHeight(); i++) {
            for (int j = 0; j < carte.getWidth(); j++) {
                if (!visited.contains(new Coord(i, j))) {
                    Coord depart = new Coord(i, j);
                    resultat += calculeLePrixDeLaRegion(depart, etape1);
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int calculeLePrixDeLaRegion(Coord start, boolean etape1) {
        Character valeurZone = carte.get(start.ligne, start.colonne);
        Set<Coord> positions = new HashSet<>();
        Queue<Coord> queue = new ArrayDeque<>();
        queue.add(start);
        int perimetre = 0;
        while (!queue.isEmpty()) {
            Coord coord = queue.poll();
            positions.add(coord);
            if (visited.contains(coord)) {
                continue;
            }
            visited.add(coord);
            Coord possible = coord.deplace(Direction.N);
            perimetre += verifieLaPosition(possible, valeurZone, queue);
            possible = coord.deplace(Direction.S);
            perimetre += verifieLaPosition(possible, valeurZone, queue);
            possible = coord.deplace(Direction.E);
            perimetre += verifieLaPosition(possible, valeurZone, queue);
            possible = coord.deplace(Direction.W);
            perimetre += verifieLaPosition(possible, valeurZone, queue);
        }
        int aire = positions.size();
        //on cherche le nombre de coins qui est égal au nombre de cotés
        int coins = calculerLeNombreDeCoins(positions);

        //System.out.println(valeurZone + ":" + aire + "," + perimetre + "," + coins);
        return etape1 ? aire * perimetre : aire * coins;
    }

    private int calculerLeNombreDeCoins(final Set<Coord> positions) {
        int coins = 0;
        for (final Coord position : positions) {
            //Coin en haut à gauche
            if (!aUnVoisinAuDessus(position, positions) && !aUnVoisinAGauche(position, positions)) {
                coins++;
            }
            //Coin en bas à gauche
            if (!aUnVoisinAuDessous(position, positions) && !aUnVoisinAGauche(position, positions)) {
                coins++;
            }
            //Coin en bas à droite
            if (!aUnVoisinAuDessous(position, positions) && !aUnVoisinADroite(position, positions)) {
                coins++;
            }
            //Coin en haut à droite
            if (!aUnVoisinAuDessus(position, positions) && !aUnVoisinADroite(position, positions)) {
                coins++;
            }

            //X  │
            //X──┘
            if (!aVoisinEnHautAGauche(position, positions) && aUnVoisinAuDessus(position, positions)
                    && aUnVoisinAGauche(position, positions)) {
                coins++;
            }

            //X│
            //X└──
            if (!aVoisinEnHautADroite(position, positions)
                    && aUnVoisinAuDessus(position, positions)
                    && aUnVoisinADroite(position, positions)) {
                coins++;
            }

            // X┌─
            // X│
            if (!aVoisinEnBasADroite(position, positions)
                    && aUnVoisinAuDessous(position, positions)
                    && aUnVoisinADroite(position, positions)) {
                coins++;
            }

            //──┐X
            //  │X
            if (!aVoisinEnBasAGauche(position, positions)
                    && aUnVoisinAuDessous(position, positions)
                    && aUnVoisinAGauche(position, positions)) {
                coins++;
            }
        }

        return coins;
    }

    private boolean aUnVoisinAuDessus(final Coord p, final Set<Coord> positions) {
        return positions.contains(p.deplace(Direction.N));
    }

    private boolean aUnVoisinAuDessous(final Coord p, final Set<Coord> positions) {
        return positions.contains(p.deplace(Direction.S));
    }

    private boolean aUnVoisinAGauche(final Coord p, final Set<Coord> positions) {
        return positions.contains(p.deplace(Direction.W));
    }

    private boolean aUnVoisinADroite(final Coord p, final Set<Coord> positions) {
        return positions.contains(p.deplace(Direction.E));
    }

    private boolean aVoisinEnHautAGauche(final Coord p, final Set<Coord> positions) {
        return positions.contains(p.deplace(Direction.NW));
    }

    private boolean aVoisinEnHautADroite(final Coord p, final Set<Coord> positions) {
        return positions.contains(p.deplace(Direction.NE));
    }

    private boolean aVoisinEnBasADroite(final Coord p, final Set<Coord> positions) {
        return positions.contains(p.deplace(Direction.SE));
    }

    private boolean aVoisinEnBasAGauche(final Coord p, final Set<Coord> positions) {
        return positions.contains(p.deplace((Direction.SW)));
    }
}
