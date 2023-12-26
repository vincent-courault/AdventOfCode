package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay23_2 extends Commun {

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(154, traitement(inputs));
    }

    @Test
    public void etape2() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(6682, traitement(inputs));
    }

    public long traitement(List<String> inputs) {

        Grille grille = new Grille(inputs);
        Graphe graphe = grille.construitLeGraphe();
        int resultat = graphe.calculeLeCheminLePlusLong();
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public enum Direction {
        N(0, -1),
        E(1, 0),
        S(0, 1),
        O(-1, 0);
        public static final List<Direction> DIRECTIONS_VOISINES = List.of(N, S, E, O);
        public final int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private record Point(int x, int y, char type) {
    }

    private static class Graphe {
        final Set<Node> nodes = new HashSet<>();
        final Node depart;
        Node fin;

        Graphe(Node depart) {
            this.depart = depart;
            nodes.add(depart);
        }

        int calculeLeCheminLePlusLong() {
            return calculeLeCheminLePlusLong(depart, new HashSet<>(), 0);
        }

        int calculeLeCheminLePlusLong(Node position, Set<Node> visited, int steps) {
            if (position == fin)
                return steps;
            visited.add(position);
            int maxLength = 0;
            for (Lien lien : position.liens) {
                if (!visited.contains(lien.cible)) {
                    int length = calculeLeCheminLePlusLong(lien.cible, visited, steps + lien.poids);
                    if (length > maxLength)
                        maxLength = length;
                }
            }
            visited.remove(position);
            return maxLength;
        }
    }

    private static class Node {
        final Point position;
        final List<Lien> liens = new ArrayList<>();

        Node(Point position) {
            this.position = position;
        }

        void ajouteLien(Node cible, int poids) {
            if (liens.stream().noneMatch(l -> l.cible == cible)) {
                liens.add(new Lien(cible, poids));
                cible.liens.add(new Lien(this, poids));
            }
        }
    }

    private record Lien(Node cible, int poids) {
    }

    private static class Grille {
        public final int largeur;
        public final int hauteur;
        final Point[][] carte;
        Point debut;
        Point fin;

        Grille(List<String> lines) {
            largeur = lines.get(0).length();
            hauteur = lines.size();
            carte = new Point[largeur][hauteur];
            for (int i = 0; i < hauteur; i++) {
                String line = lines.get(i);
                for (int j = 0; j < largeur; j++) {
                    carte[j][i] = new Point(j, i, line.charAt(j));
                }
            }
            debut = carte[1][0];
            fin = carte[largeur - 2][hauteur - 1];
        }

        Graphe construitLeGraphe() {
            Graphe graphe = new Graphe(new Node(debut));
            Set<Point> visitedNodes = new HashSet<>();
            Queue<Node> queue = new LinkedList<>();
            queue.add(graphe.depart);
            visitedNodes.add(debut);
            while (!queue.isEmpty()) {
                Node noeudCourant = queue.poll();
                List<Point> voisins = getPositionsSuivantes(noeudCourant.position, new HashSet<>());
                for (Point voisin : voisins) {
                    Set<Point> visited = new HashSet<>();
                    visited.add(noeudCourant.position);
                    visited.add(voisin);
                    Point cible = voisin;
                    List<Point> positionsSuivantes = getPositionsSuivantes(voisin, visited);
                    // Tant qu'il n'y a qu'un choix possible on est toujours sur le même morceau du graphe
                    while (positionsSuivantes.size() == 1) {
                        cible = positionsSuivantes.get(0);
                        visited.add(cible);
                        positionsSuivantes = getPositionsSuivantes(cible, visited);
                    }
                    Node noeudSuivant = null;
                    if (visitedNodes.contains(cible)) {
                        for (Node noeud : graphe.nodes) {
                            if (noeud.position == cible) {
                                noeudSuivant = noeud;
                                break;
                            }
                        }
                    } else {
                        noeudSuivant = new Node(cible);
                        graphe.nodes.add(noeudSuivant);
                        queue.add(noeudSuivant);
                        visitedNodes.add(cible);
                        if (cible.equals(fin))
                            graphe.fin = noeudSuivant;
                    }
                    noeudCourant.ajouteLien(noeudSuivant, visited.size() - 1);
                }
            }
            return graphe;
        }

        private List<Point> getPositionsSuivantes(Point courant, Set<Point> visited) {
            List<Point> positionsSuivantes = new ArrayList<>();

            for (Direction direction : Direction.DIRECTIONS_VOISINES) {
                int x = courant.x + direction.x;
                int y = courant.y + direction.y;
                if (estDansLaGrille(x, y) && carte[x][y].type != '#' && !visited.contains(carte[x][y]))
                    positionsSuivantes.add(carte[x][y]);
            }
            return positionsSuivantes;
        }

        public boolean estDansLaGrille(int x, int y) {
            return 0 <= x && x < largeur && 0 <= y && y < hauteur;
        }
    }
}

