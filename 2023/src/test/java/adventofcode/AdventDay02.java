package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay02 extends Commun {

    public static final int NOMBRE_MAX_CUBES_VERT = 13;
    public static final int NOMBRE_MAX_CUBES_ROUGE = 12;
    public static final int NOMBRE_MAX_CUBE_BLEU = 14;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(8, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2913, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(2286, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(55593, traitement2(inputs));
    }

    public int traitement(List<String> games) {
        int resultat = 0;
        for (String game : games) {
            String[] jeu = game.split(":");
            String numero = jeu[0].split(" ")[1];
            String[] parties = jeu[1].split(";");
            boolean partiePossible = true;
            for (String partie : parties) {
                String[] cubes = partie.split(",");
                for (String cube : cubes) {
                    if ((cube.contains("green") && retourneLeNombreDeCube(cube) > NOMBRE_MAX_CUBES_VERT)
                            || (cube.contains("red") && retourneLeNombreDeCube(cube) > NOMBRE_MAX_CUBES_ROUGE)
                            || (cube.contains("blue") && retourneLeNombreDeCube(cube) > NOMBRE_MAX_CUBE_BLEU))
                        partiePossible = false;
                }
            }
            if (partiePossible) {
                resultat = resultat + Integer.parseInt(numero);
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private int retourneLeNombreDeCube(String cube) {
        return Integer.parseInt(cube.split(" ")[1]);
    }

    public int traitement2(List<String> games) {
        int resultat = 0;
        for (String game : games) {
            String[] parties = game.split(":")[1].split(";");
            int maxRougeDansLeJeu = 0;
            int maxVertDansLeJeu = 0;
            int maxBleuDansLeJeu = 0;
            for (String partie : parties) {
                String[] cubes = partie.split(",");
                for (String cube : cubes) {
                    if (cube.contains("green"))
                        maxVertDansLeJeu = Math.max(retourneLeNombreDeCube(cube), maxVertDansLeJeu);
                    if (cube.contains("red"))
                        maxRougeDansLeJeu = Math.max(retourneLeNombreDeCube(cube), maxRougeDansLeJeu);
                    if (cube.contains("blue"))
                        maxBleuDansLeJeu = Math.max(retourneLeNombreDeCube(cube), maxBleuDansLeJeu);
                }
            }
            resultat = resultat + maxBleuDansLeJeu * maxRougeDansLeJeu * maxVertDansLeJeu;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
