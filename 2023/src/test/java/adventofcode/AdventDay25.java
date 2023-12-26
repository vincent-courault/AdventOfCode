package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay25 extends Commun {

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(562912, traitement(inputs));
    }

    public int traitement(List<String> inputs){
        Map<String, Composant> composants = new HashMap<>();
        Set<String> sommets = new HashSet<>();
        Set<Lien> liens = new HashSet<>();
        int resultat;
        StringBuilder graphe = new StringBuilder("graph{" + "\n");
        for (String line : inputs) {
            composants.put(line.split(":")[0], new Composant(line.split(":")));
            String appelant = line.split(":")[0];
            for (String appele : line.split(":")[1].trim().split(" ")) {
                graphe.append(appelant).append("--").append(appele).append("\n");
            }
        }
        graphe.append("}");
        // System.out.println(graphe);
        // graphe à mettre dans un fichier input_day25.dot
        // Commande pour visualiser le graph avec graphviz
        // dot -Tsvg -Ksfdp input_day25.dot > day25.svg

        Map<String, Composant> tmp = new HashMap<>();
        for (String s : composants.keySet()) {
            tmp.putIfAbsent(s, new Composant(s));
            tmp.get(s).connexions.addAll(composants.get(s).connexions);
            Composant c = composants.get(s);
            for (String s2 : c.connexions) {
                tmp.putIfAbsent(s2, new Composant(s2));
                tmp.get(s2).connexions.add(s);
                sommets.add(s);
                sommets.add(s2);
                liens.add(new Lien(s, s2));
                liens.add(new Lien(s2, s));
            }
        }
        composants = tmp;
        // Lien à supprimer identifiés dans la visualisation
        Lien lien1 = new Lien("lms", "tmc");
        Lien lien2 = new Lien("txf", "xnn");
        Lien lien3 = new Lien("jjn", "nhg");
        liens.remove(lien1);
        liens.remove(lien2);
        liens.remove(lien3);
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        // On part ensuite d'un des sommets et on identifie tous les sommets liés
        queue.add(sommets.stream().findFirst().orElse(""));
        while (!queue.isEmpty()) {
            String sommet = queue.poll();
            visited.add(sommet);
            for (String connexion : composants.get(sommet).connexions) {
                if (!visited.contains(connexion) && liens.contains(new Lien(sommet, connexion))) {
                    queue.add(connexion);
                }
            }
        }
        resultat = visited.size() * (sommets.size() - visited.size());

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    static class Composant {
        Set<String> connexions = new HashSet<>();
        String nom;
        public Composant(String s) {
            nom = s;
        }
        public Composant(String[] line) {
            nom = line[0];
            for (String s : line[1].trim().split(" ")) {
                connexions.add(s.trim());
            }
        }
    }

    static class Lien {
        String debut;
        String fin;
        public Lien(String debut, String fin) {
            this.debut = (debut.compareTo(fin) > 0) ? debut : fin;
            this.fin = (debut.compareTo(fin) > 0) ? fin : debut;
        }
        public String toString() {
            return debut + " " + fin;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Lien lien = (Lien) o;
            return debut.equals(lien.debut) && fin.equals(lien.fin);
        }
        @Override
        public int hashCode() {
            return Objects.hash(debut, fin);
        }
    }
}