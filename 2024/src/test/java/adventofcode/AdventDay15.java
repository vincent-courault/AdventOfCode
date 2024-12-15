package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay15 extends Commun {

    Grid<Character> carte;

    Map<String, Direction> COMMANDES_DIRECTIONS = Map.of("^", Direction.N,
            ">", Direction.E, "v", Direction.S,
            "<", Direction.W);

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(2028, traitement(inputs, false));
    }

    @Test
    public void etape1_exemple2() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true, 2);
        assertEquals(10092, traitement(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1552879, traitement(inputs, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1751, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple2() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true, 2);
        assertEquals(9021, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple3() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true, 3);
        assertEquals(618, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple4() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true, 4);
        assertEquals(1721, traitement(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1561175, traitement(inputs, true));
    }

    public int traitement(List<String> inputs, boolean etape2) {
        int resultat = 0;
        List<String> instructions = creeLaCarteEtRecupereLesInstructions(inputs, etape2);
        Coord robot = carte.getStartingPoint('@');

        for (String instruction : instructions) {
            String[] commandes = instruction.split("");
            for (String commande : commandes) {
                if (etape2) {
                    robot = deplaceRobotEtCaissesLarges(robot, COMMANDES_DIRECTIONS.get(commande));
                } else {
                    robot = deplaceRobotEtCaisses(robot, COMMANDES_DIRECTIONS.get(commande));
                }
            }
        }

        for (int i = 0; i < carte.getHeight(); i++) {
            for (int j = 0; j < carte.getWidth(); j++) {
                if (carte.get(i, j) == 'O' || carte.get(i, j) == '(') {
                    resultat += (100 * i + j);
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private List<String> creeLaCarteEtRecupereLesInstructions(List<String> inputs, boolean etape2) {
        List<String> inputCarte = new ArrayList<>();
        List<String> inputInstructions = new ArrayList<>();
        boolean c = true;
        for (String input : inputs) {
            if (input.isEmpty()) {
                c = false;
            } else if (c) {
                if (etape2) {
                    inputCarte.add(input.replace("O", "()")
                            .replace("#", "##")
                            .replace(".", "..")
                            .replace("@", "@."));
                } else {
                    inputCarte.add(input);
                }
            } else {
                inputInstructions.add(input);
            }
        }
        carte = new Grid<>(inputCarte, new Divider.Character());
        return inputInstructions;
    }

    private Coord deplaceRobotEtCaisses(Coord robot, Direction direction) {

        if (carte.get(robot.deplace(direction)) == '.') {
            carte.set(robot.ligne, robot.colonne, '.');
            carte.set(robot.deplace(direction).ligne, robot.deplace(direction).colonne, '@');
            return robot.deplace(direction);
        }
        if (carte.get(robot.deplace(direction)) == 'O') {
            Coord caisse = new Coord(robot.ligne, robot.colonne).deplace(direction).deplace(direction);
            while (carte.get(caisse) == 'O') {
                caisse = caisse.deplace(direction);
            }
            if (carte.get(caisse) == '.') { // on a trouvé un emplacement libre
                carte.set(caisse.ligne, caisse.colonne, 'O');
                carte.set(robot.deplace(direction).ligne, robot.deplace(direction).colonne, '@');
                carte.set(robot.ligne, robot.colonne, '.');
                return robot.deplace(direction);
            }
        }
        return robot;
    }

    private Coord deplaceRobotEtCaissesLarges(Coord robot, Direction direction) {

        if (carte.get(robot.deplace(direction)) == '.') {
            carte.set(robot.ligne, robot.colonne, '.');
            carte.set(robot.deplace(direction).ligne, robot.deplace(direction).colonne, '@');
            return robot.deplace(direction);
        } else if (carte.get(robot.deplace(direction)) == '#') {
            return robot;
        } else if (direction == Direction.W) {
            return deplaceLargeOuest(robot, direction);
        } else if (direction == Direction.E) {
            return deplaceLargeEst(robot, direction);
        } else if (direction == Direction.N) {
            return deplaceLargeSudOuNord(robot, direction);
        } else if (direction == Direction.S) {
            return deplaceLargeSudOuNord(robot, direction);
        }
        return robot; // cas normalement impossible
    }

    private Coord deplaceLargeSudOuNord(Coord robot, Direction direction) {
        Queue<Coord> aDeplacer = new LinkedList<>();
        Set<Coord> aDeplacerLignePrecedente = new HashSet<>();
        aDeplacer.add(new Coord(robot.ligne, robot.colonne));
        aDeplacerLignePrecedente.add(robot.deplace(direction));
        if (carte.get(robot.deplace(direction)) == '(') {
            aDeplacerLignePrecedente.add(new Coord(robot.deplace(direction).ligne, robot.deplace(direction).colonne + 1));
        } else {
            aDeplacerLignePrecedente.add(new Coord(robot.deplace(direction).ligne, robot.deplace(direction).colonne - 1));
        }
        aDeplacer.addAll(aDeplacerLignePrecedente);

        Set<Coord> aDeplacerLigneBas;
        boolean reponseTrouvee = false;
        while (!reponseTrouvee) {
            aDeplacerLigneBas = new HashSet<>();
            int nbPoint = 0;
            for (Coord coord : aDeplacerLignePrecedente) {
                switch (carte.get(coord.deplace(direction))) {
                    case '(' -> {
                        aDeplacerLigneBas.add(new Coord(coord.deplace(direction).ligne, coord.colonne));
                        aDeplacerLigneBas.add(new Coord(coord.deplace(direction).ligne, coord.colonne + 1));
                    }
                    case ')' -> {
                        aDeplacerLigneBas.add(new Coord(coord.deplace(direction).ligne, coord.colonne));
                        aDeplacerLigneBas.add(new Coord(coord.deplace(direction).ligne, coord.colonne - 1));
                    }
                    case '#' -> {
                        return robot; //il ya un obstacle on ne pourra pas déplacer
                    }
                    case '.' -> nbPoint++;
                    case null, default -> {
                    }
                }
            }
            if (nbPoint == aDeplacerLignePrecedente.size()) {
                reponseTrouvee = true;
            } else {
                aDeplacer.addAll(aDeplacerLigneBas);
                aDeplacerLignePrecedente = aDeplacerLigneBas;
            }
        }
        if (direction == Direction.S) {
            for (Coord coord : aDeplacer.stream()
                    .sorted(Comparator.comparing(Coord::ligne).reversed().
                            thenComparing(Coord::colonne)).toList()) {

                carte.set(coord.deplace(direction).ligne, coord.colonne, carte.get(coord));
                if (!aDeplacer.contains(coord.deplace(Direction.N))) {
                    carte.set(coord.ligne, coord.colonne, '.');
                }
            }
        } else {
            for (Coord coord : aDeplacer.stream()
                    .sorted(Comparator.comparing(Coord::ligne).
                            thenComparing(Coord::colonne)).toList()) {

                carte.set(coord.deplace(direction).ligne, coord.colonne, carte.get(coord));
                if (!aDeplacer.contains(coord.deplace(Direction.S))) {
                    carte.set(coord.ligne, coord.colonne, '.');
                }
            }
        }
        return robot.deplace(direction);
    }

    private Coord deplaceLargeOuest(Coord robot, Direction direction) {
        Coord caisse = new Coord(robot.ligne, robot.colonne).deplace(direction).deplace(direction);
        while ((carte.get(caisse) + "" + carte.get(caisse.deplace(direction))).equals("()") || (carte.get(caisse) + "" + carte.get(caisse.deplace(direction))).equals(")(")) {
            caisse = caisse.deplace(direction);
        }
        caisse = caisse.deplace(direction);
        if (carte.get(caisse) == '.') { // on a trouvé un emplacement libre
            Coord position = new Coord(robot.ligne, robot.colonne).deplace(direction).deplace(direction);
            while (position.colonne > caisse.colonne) {
                carte.set(position.ligne, position.colonne, ')');
                position = position.deplace(direction);
                carte.set(position.ligne, position.colonne, '(');
                position = position.deplace(direction);
            }

            carte.set(robot.deplace(direction).ligne, robot.deplace(direction).colonne, '@');
            carte.set(robot.ligne, robot.colonne, '.');
            return robot.deplace(direction);
        }
        return robot;
    }

    private Coord deplaceLargeEst(Coord robot, Direction direction) {
        Coord caisse = new Coord(robot.ligne, robot.colonne).deplace(direction).deplace(direction);
        while ((carte.get(caisse) + "" + carte.get(caisse.deplace(direction))).equals("()") ||
                (carte.get(caisse) + "" + carte.get(caisse.deplace(direction))).equals(")(")) {
            caisse = caisse.deplace(direction);
        }
        caisse = caisse.deplace(direction);
        if (carte.get(caisse) == '.') { // on a trouvé un emplacement libre
            Coord position = new Coord(robot.ligne, robot.colonne).deplace(direction).deplace(direction);
            while (position.colonne < caisse.colonne) {
                carte.set(position.ligne, position.colonne, '(');
                position = position.deplace(direction);
                carte.set(position.ligne, position.colonne, ')');
                position = position.deplace(direction);
            }
            carte.set(robot.deplace(direction).ligne, robot.deplace(direction).colonne, '@');
            carte.set(robot.ligne, robot.colonne, '.');
            return robot.deplace(direction);
        } else {
            return robot;
        }
    }
}
