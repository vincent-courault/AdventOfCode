package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay22 extends Commun {

    int[][][] carte;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(5, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(451, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(7, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(66530, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean part1) {
        long resultat = 0;
        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;
        int[] vecX = new int[]{1, 0, 0};
        int[] vecY = new int[]{0, 1, 0};
        int[] vecZ = new int[]{0, 0, 1};
        List<Brique> briques = new ArrayList<>();
        //lecture du fichier et création des briques
        for (String line : inputs) {
            Brique b = new Brique(line);
            briques.add(b);
            maxX = Math.max(maxX, b.x + b.taille);
            maxY = Math.max(maxY, b.y + b.taille);
            maxZ = Math.max(maxZ, b.z + b.taille);
        }
        //Initialisation de la carte 3D
        carte = new int[maxX][maxY][maxZ];
        for (int i = 0; i < carte.length; i++) {
            for (int j = 0; j < carte[0].length; j++) {
                for (int k = 0; k < carte[0][0].length; k++) {
                    carte[i][j][k] = -1;
                }
            }
        }
        //on trie
        Collections.sort(briques);
        //On numérote les briques
        for (int i = 0; i < briques.size(); i++) {
            briques.get(i).id = i;
        }
        // on met à jour la carte avec les id des briques
        for (Brique b : briques) {
            for (int i = 0; i < b.taille; i++) {
                carte[b.x + i * vecX[b.direction]][b.y + i * vecY[b.direction]][b.z + i * vecZ[b.direction]] = b.id;
            }
        }

        //on fait descendre les briques jusqu'à ce qu'il y ait un point d'appui
        for (Brique brique : briques) {
            boolean briqueEnAppui = brique.z == 1;
            while (!briqueEnAppui) {
                briqueEnAppui = brique.z == 1;
                // on vérifie si au moins une des valeurs en z-1 est occupée par une brique
                for (int k = 0; k < brique.taille; k++) {
                    if (carte[brique.x + k * vecX[brique.direction]][brique.y + k * vecY[brique.direction]][brique.z - 1] != -1) {
                        briqueEnAppui = true;
                        break;
                    }
                }
                if (!briqueEnAppui) {
                    //on descend la brique d'une position et on met à jour chacune des positions correspondantes dans la carte
                    for (int k = 0; k < brique.taille; k++) {
                        int x = brique.x + k * vecX[brique.direction];
                        int y = brique.y + k * vecY[brique.direction];
                        int z = brique.z + k * vecZ[brique.direction];
                        carte[x][y][z - 1] = brique.id;
                        carte[x][y][z] = -1;
                    }
                    brique.z = brique.z - 1;
                }
            }
        }
        // on met à jour les dépendances entre les briques
        for (Brique brique : briques) {
            if (brique.direction == 2) { // brique orientée sur Z
                if (carte[brique.x][brique.y][brique.z + brique.taille] != -1) {
                    briques.get(carte[brique.x][brique.y][brique.z + brique.taille]).dependances.add(brique.id);
                    briques.get(brique.id).dependants.add(carte[brique.x][brique.y][brique.z + brique.taille]);
                }
            } else {
                for (int k = 0; k < brique.taille; k++) {
                    int x = brique.x + k * vecX[brique.direction];
                    int y = brique.y + k * vecY[brique.direction];
                    int z = brique.z + 1;
                    if (carte[x][y][z] != -1) {
                        briques.get(carte[x][y][z]).dependances.add(brique.id);
                        briques.get(brique.id).dependants.add(carte[x][y][z]);
                    }
                }
            }
        }
        if (part1) {
            // On identifie les briques avec une seule dépendance
            // si la brique correspondante disparait, notre brique ne tient plus
            // le résultat est stocké dans un tableau :
            // si la brique 3 dépend uniquement de la brique 1, on met à jour le premier élément du tableau
            int[] dependanceUnique = new int[briques.size()];
            for (Brique b : briques) {
                if (b.dependances.size() == 1) {
                    for (Integer i : b.dependances) {
                        dependanceUnique[i]++;
                    }
                }
            }
            // on veut le nombre de brique dont la valeur de dépendance unique est de 0
            // (on peut la supprimer sans déstabilisier une ou plusieurs briques s'appuyant uniquement dessus)
            resultat = Arrays.stream(dependanceUnique).filter(element -> element == 0).count();
        } else {
            for (Brique brique : briques) {
                Set<Integer> briquesTombees = new HashSet<>();
                briquesTombees.add(brique.id);
                Set<Integer> briquesAVerifier = brique.dependants;
                while (!briquesAVerifier.isEmpty()) {
                    Set<Integer> futuresBriquesAVerifier = new HashSet<>();
                    for (Integer j : briquesAVerifier) {
                        Set<Integer> dependances = briques.get(j).dependances;
                        if (briquesTombees.containsAll(dependances)) {
                            briquesTombees.add(j);
                            futuresBriquesAVerifier.addAll(briques.get(j).dependants);
                        }
                        briquesAVerifier = futuresBriquesAVerifier;
                    }
                }
                resultat += briquesTombees.size() - 1;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    static class Brique implements Comparable<Brique> {
        int id;
        int x, y, z;
        int taille;
        int direction;
        //Id des briques qui s'appuient sur la brique
        Set<Integer> dependants = new HashSet<>();
        //Id des briques dont la brique est dépendante (la brique s'appuie sur ces briques)
        Set<Integer> dependances = new HashSet<>();
        public Brique(String line) {
            String[] ends = line.split("~");
            int x1 = Integer.parseInt(ends[0].split(",")[0]);
            int y1 = Integer.parseInt(ends[0].split(",")[1]);
            int z1 = Integer.parseInt(ends[0].split(",")[2]);
            int x2 = Integer.parseInt(ends[1].split(",")[0]);
            int y2 = Integer.parseInt(ends[1].split(",")[1]);
            int z2 = Integer.parseInt(ends[1].split(",")[2]);
            x = Math.min(x1, x2);
            y = Math.min(y1, y2);
            z = Math.min(z1, z2);
            if (x1 != x2) {
                direction = 0;
                taille = Math.abs(x1 - x2);
            }
            if (y1 != y2) {
                direction = 1;
                taille = Math.abs(y1 - y2);
            }
            if (z1 != z2) {
                direction = 2;
                taille = Math.abs(z1 - z2);
            }
            taille++;
        }
        @Override
        public int compareTo(Brique o) {
            return z - o.z;
        }
    }
}