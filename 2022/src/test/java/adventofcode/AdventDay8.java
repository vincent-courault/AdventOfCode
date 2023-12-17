package adventofcode;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class AdventDay8 {

    @Rule
    public TestName name = new TestName();

    private List<String> initTest() {
        return List.of("30373",
                "25512",
                "65332",
                "33549",
                "35390");
    }

    private List<String> lectureDuFichier() throws URISyntaxException, IOException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day8.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    @Test
    public void etape1_exemple() {
        List<String> arbres = initTest();
        Assert.assertEquals(21, (etape1_main(arbres)));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> arbres = lectureDuFichier();
        Assert.assertEquals(1835, (etape1_main(arbres)));
    }

    public int etape1_main(List<String> arbres) {
        int tailleForet = arbres.size();
        Arbre[][] foret = creationForet(arbres, tailleForet);

        int nbvisible = 2 * tailleForet + 2 * (tailleForet - 2);
        for (int ligneArbreCourant = 1; ligneArbreCourant < tailleForet - 1; ligneArbreCourant++) {
            for (int colonneArbreCourant = 1; colonneArbreCourant < tailleForet - 1; colonneArbreCourant++) {
                Arbre arbreCourant = foret[ligneArbreCourant][colonneArbreCourant];
                //Arbre à droite
                boolean visibleDroite = true;
                for (int i = colonneArbreCourant + 1; i < tailleForet; i++) {
                    if (foret[ligneArbreCourant][i].hauteur >= arbreCourant.hauteur) {
                        visibleDroite = false;
                        break;
                    }
                }
                //Arbre à gauche
                boolean visibleGauche = true;
                for (int i = colonneArbreCourant - 1; i >= 0; i--) {
                    if (foret[ligneArbreCourant][i].hauteur >= arbreCourant.hauteur) {
                        visibleGauche = false;
                        break;
                    }
                }
                //Arbre au dessous
                boolean visibleDessous = true;
                for (int i = ligneArbreCourant + 1; i < tailleForet; i++) {
                    if (foret[i][colonneArbreCourant].hauteur >= arbreCourant.hauteur) {
                        visibleDessous = false;
                        break;
                    }
                }
                boolean visibleDessus = true;

                for (int i = ligneArbreCourant - 1; i >= 0; i--) {
                    if (foret[i][colonneArbreCourant].hauteur >= arbreCourant.hauteur) {
                        visibleDessus = false;
                        break;
                    }
                }
                arbreCourant.visible = visibleDessous
                        || visibleDessus || visibleGauche || visibleDroite;
                if (arbreCourant.visible) {
                    nbvisible++;
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + nbvisible);

        return nbvisible;
    }

    @Test
    public void etape2_exemple() {
        List<String> assignments = initTest();
        Assert.assertEquals(8, (etape2_main(assignments)));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> assignments = lectureDuFichier();
        Assert.assertEquals(263670, (etape2_main(assignments)));
    }

    public int etape2_main(List<String> arbres) {
        int tailleForet = arbres.size();
        Arbre[][] foret = creationForet(arbres, tailleForet);
        int maxScore = 0;
        for (int ligneArbreCourant = 1; ligneArbreCourant < tailleForet - 1; ligneArbreCourant++) {
            for (int colonneArbreCourant = 1; colonneArbreCourant < tailleForet - 1; colonneArbreCourant++) {
                Arbre arbreCourant = foret[ligneArbreCourant][colonneArbreCourant];
                //Arbre à droite
                int scoreDroite = tailleForet - (colonneArbreCourant + 1);
                for (int i = colonneArbreCourant + 1; i < tailleForet; i++) {
                    if (foret[ligneArbreCourant][i].hauteur >= arbreCourant.hauteur) {
                        scoreDroite = i - colonneArbreCourant;
                        break;
                    }
                }
                //Arbre à gauche
                int scoreGauche = colonneArbreCourant;
                for (int i = colonneArbreCourant - 1; i >= 0; i--) {
                    if (foret[ligneArbreCourant][i].hauteur >= arbreCourant.hauteur) {
                        scoreGauche = colonneArbreCourant - i;
                        break;
                    }
                }
                //Arbre au dessous
                int scoreDessous = tailleForet - (ligneArbreCourant + 1);
                for (int i = ligneArbreCourant + 1; i < tailleForet; i++) {
                    if (foret[i][colonneArbreCourant].hauteur >= arbreCourant.hauteur) {
                        scoreDessous = i - ligneArbreCourant;
                        break;
                    }
                }
                int scoreDessus = ligneArbreCourant;

                for (int i = ligneArbreCourant - 1; i >= 0; i--) {
                    if (foret[i][colonneArbreCourant].hauteur >= arbreCourant.hauteur) {
                        scoreDessus = ligneArbreCourant - i;
                        break;
                    }
                }
                arbreCourant.score = scoreDessus * scoreDessous * scoreDroite * scoreGauche;
                if (arbreCourant.score > maxScore) {
                    maxScore = arbreCourant.score;
                }

            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + maxScore);

        return maxScore;
    }

    private Arbre[][] creationForet(List<String> arbres, int tailleForet) {
        int j =0;
        Arbre[][] foret = new Arbre[tailleForet][tailleForet];
        for (String arbre : arbres) {
            for (int i = 0; i < arbre.length(); i++) {
                foret[j][i] = new Arbre();
                foret[j][i].hauteur = Integer.parseInt(arbre.substring(i, i + 1));
            }
            j++;
        }
        return foret;
    }

    public static class Arbre {
        public int score;
        Integer hauteur;
        Boolean visible = true;
    }

}
