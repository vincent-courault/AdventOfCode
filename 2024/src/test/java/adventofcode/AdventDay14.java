package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay14 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(12, traitement(inputs, 7, 11, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(208437768, traitement(inputs, 103, 101, true));
    }

    //Première version avec écriture dans un fichier pour identifier le sapin
    //@Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(0, traitement(inputs, 103, 101, false));
    }

    //La solution correspond à ne pas avoir 2 robots au même endroit
    // on utilise une grille et on compte jusqu'à avoir le nombre de cases occupées égal au nombre de robot
    @Test
    public void etape22() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(7492, traitement2(inputs, 103, 101));
    }

    // Solution alternative, on ne passe pas par une grille en ayant ajouté equals sur la classe Robot
    //@Test
    public void etape23() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(7492, traitement3(inputs, 103, 101));
    }

    public int traitement(List<String> inputs, int nbLigneGrille, int nbColonneGrille, boolean etape1) throws IOException {
        int resultat = 0;
        List<Robot> robots = inputs.stream().map(Robot::new).toList();

        if (!etape1) {
            FileWriter fileWriter = new FileWriter("outputDay14.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("Some String");
            printWriter.printf("Product name is %s and its price is %d $", "iPhone", 1000);
            List<List<Character>> init = new ArrayList<>();
            for (int i = 0; i < nbLigneGrille; i++) {
                List<Character> ligne = new ArrayList<>();
                for (int j = 0; j < nbColonneGrille; j++) {
                    ligne.add('.');
                }
                init.add(ligne);
            }

            for (int i = 1; i < 10000; i++) {
                printWriter.println("Résultat" + i);
                for (Robot robot : robots) {
                    robot.deplace(1);
                    robot.deplaceDansLaGrille(nbLigneGrille, nbColonneGrille);
                }
                Grid<Character> grille = new Grid<>(init);
                for (Robot robot : robots) {
                    grille.set(robot.ligne(), robot.colonne(), '#');
                }
                printWriter.println(grille);
            }
            printWriter.close();
        } else {
            for (Robot robot : robots) {
                robot.deplace(100);
                robot.deplaceDansLaGrille(nbLigneGrille, nbColonneGrille);
            }
            int cadran1 = 0, cadran2 = 0, cadran3 = 0, cadran4 = 0;
            for (Robot robot : robots) {
                if (robot.ligne() < nbLigneGrille / 2) {
                    if (robot.colonne() < nbColonneGrille / 2) {
                        cadran1++;
                    }
                    if (robot.colonne() > nbColonneGrille / 2) {
                        cadran2++;
                    }
                }
                if (robot.ligne() > nbLigneGrille / 2) {
                    if (robot.colonne() < nbColonneGrille / 2) {
                        cadran3++;
                    }
                    if (robot.colonne() > nbColonneGrille / 2) {
                        cadran4++;
                    }
                }
            }
            resultat = cadran1 * cadran2 * cadran3 * cadran4;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs, int nbLigneGrille, int nbColonneGrille) {
        int resultat = 0;
        List<Robot> robots = inputs.stream().map(Robot::new).toList();
        Grid<Character> grilleVide = new Grid<>(nbLigneGrille,nbColonneGrille,'.');

        for (int i = 1; i < 10000; i++) {
            for (Robot robot : robots) {
                robot.deplace(1);
                robot.deplaceDansLaGrille(nbLigneGrille, nbColonneGrille);
            }
            Grid<Character> grille = new Grid<>(grilleVide);
            robots.forEach(robot -> grille.set(robot.ligne(), robot.colonne(), '#'));
            if (grille.compte('#') == inputs.size()) {
                resultat = i;
                break;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement3(List<String> inputs, int nbLigneGrille, int nbColonneGrille) {
        int resultat = 0;
        List<Robot> robots = inputs.stream().map(Robot::new).toList();
        for (int i = 1; i < 10000; i++) {
            for (Robot robot : robots) {
                robot.deplace(1);
                robot.deplaceDansLaGrille(nbLigneGrille, nbColonneGrille);
            }
            if (robots.stream().distinct().toList().size() == inputs.size()) {
                resultat = i;
                break;
            }
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static class Robot {
        Coord position;
        Direction direction;

        public Robot(String desc) {
            this.position = new Coord(Integer.parseInt(desc.split(" ")[0].split("=")[1].split(",")[1]),
                    Integer.parseInt(desc.split(" ")[0].split("=")[1].split(",")[0]));
            this.direction = new Direction(Integer.parseInt(desc.split(" ")[1].split("=")[1].split(",")[1]),
                    Integer.parseInt(desc.split(" ")[1].split("=")[1].split(",")[0]));
        }

        public int ligne() {
            return this.position.ligne;
        }

        public int colonne() {
            return this.position.colonne;
        }

        @Override
        public String toString() {
            return "Robot{position=" + position + ", direction=" + direction + '}';
        }

        public void deplace(int nbFois) {
            position = position.deplace(direction, nbFois);
        }

        public void deplaceDansLaGrille(int nbLigneGrille, int nbColonneGrille) {
            position.ligne = position.ligne % nbLigneGrille < 0 ?
                    position.ligne % nbLigneGrille + nbLigneGrille : this.position.ligne % nbLigneGrille;
            position.colonne = position.colonne % nbColonneGrille < 0
                    ? position.colonne % nbColonneGrille + nbColonneGrille : position.colonne % nbColonneGrille;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Robot robot = (Robot) o;
            return Objects.equals(position, robot.position);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(position);
        }
    }
}
