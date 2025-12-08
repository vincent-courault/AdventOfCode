package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay08 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(40, traitement(inputs, 10));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(163548, traitement(inputs, 1000));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(25272, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(772452514, traitement2(inputs));
    }

    public int traitement(List<String> inputs, int k2) {
        int resultat = 1;
        List<Coord3D> points = new ArrayList<>();
        for (String input : inputs) {
            String[] split = input.split(",");
            points.add(new Coord3D(split[0], split[1], split[2]));
        }
        List<PaireDePoints> paireDePoints = trouveLesXPairesDePointsLesPlusProches(points, k2);

        // Création des réseaux
        UnionFind unionFind = new UnionFind(points.size());

        for (PaireDePoints pp : paireDePoints) {
            unionFind.union(pp.i, pp.j);
        }

        // Comptage de la taille de chaque réseau
        Map<Integer, Integer> tailleDesReseaux = new HashMap<>();
        for (int i = 0; i < points.size(); i++) {
            int root = unionFind.find(i);
            tailleDesReseaux.put(root, tailleDesReseaux.getOrDefault(root, 0) + 1);
        }

        // On ne garde que les 3 plus grands réseaux
        List<Map.Entry<Integer, Integer>> taillesTriees = new ArrayList<>(tailleDesReseaux.entrySet());
        taillesTriees.sort((a, b) -> b.getValue() - a.getValue());

        for (int k = 0; k < 3; k++) {
            Map.Entry<Integer, Integer> e = taillesTriees.get(k);
            resultat *= e.getValue();
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    //On cherche l'arbre couvrant de poids minimal
    //https://fr.wikipedia.org/wiki/Arbre_couvrant_de_poids_minimal
    //Utilisation de l'algorithme de Kruskal
    //https://fr.wikipedia.org/wiki/Algorithme_de_Kruskal
    public int traitement2(List<String> inputs) {
        int resultat;
        List<Coord3D> points = new ArrayList<>();
        for (String input : inputs) {
            String[] split = input.split(",");
            points.add(new Coord3D(split[0], split[1], split[2]));
        }
        int taille=points.size();
        // Calcul de toutes les distances
        List<PaireDePoints> paireDePoints = new ArrayList<>(taille * (taille - 1) / 2);
        for (int i = 0; i < taille; i++) {
            for (int j = i + 1; j < taille; j++) {
                paireDePoints.add(new PaireDePoints(i, j, points.get(i).distanceTo(points.get(j))));
            }
        }
        // Tri des distances
        paireDePoints.sort(Comparator.comparingDouble(e -> e.distance));
        // Création du réseau
        UnionFind unionFind = new UnionFind(taille);
        int components = taille;
        PaireDePoints derniereLiaison = null;
        for (PaireDePoints e : paireDePoints) {
            if (unionFind.union(e.i, e.j)) {
                components--;
                if (components == 1) {
                    derniereLiaison = e;
                    break;  // tout est connecté
                }
            }
        }

        assert derniereLiaison != null;
        Coord3D p1 = points.get(derniereLiaison.i);
        Coord3D p2 = points.get(derniereLiaison.j);
        resultat = (int) (p1.x * p2.x);
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    //Représente les jonctions entre 2 points identifiés par les indices
    static class PaireDePoints {
        int i, j;           // index des points
        double distance;

        public PaireDePoints(int i, int j, double d) {
            this.i = i;
            this.j = j;
            this.distance = d;
        }

        @Override
        public String toString() {
            return i + "," + j;
        }
    }

    //Classe Union-Find
    //https://fr.wikipedia.org/wiki/Union-find
    static class UnionFind {
        int[] parent, size;

        public UnionFind(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        boolean union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return false;
            if (size[a] < size[b]) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            parent[b] = a;
            size[a] += size[b];
            return true;
        }
    }

    private List<PaireDePoints> trouveLesXPairesDePointsLesPlusProches(List<Coord3D> points, int x) {
        PriorityQueue<PaireDePoints> pile = new PriorityQueue<>(
                (a, b) -> Double.compare(b.distance, a.distance) // max-heap
        );
        int n = points.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double distance = points.get(i).distanceTo(points.get(j));
                PaireDePoints paireDePoints = new PaireDePoints(i, j, distance);

                if (pile.size() < x) {
                    pile.add(paireDePoints);
                } else {
                    assert pile.peek() != null;
                    if (distance < pile.peek().distance) {
                        pile.poll();
                        pile.add(paireDePoints);
                    }
                }
            }
        }

        List<PaireDePoints> resultat = new ArrayList<>(pile);
        resultat.sort(Comparator.comparingDouble(p -> p.distance));
        return resultat;
    }
}
