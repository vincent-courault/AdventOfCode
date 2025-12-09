package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay09 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(50, traitement2(inputs,true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(4764078684L, traitement2(inputs,true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(24, traitement2(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1652344888, traitement2(inputs, false));
    }

    public long traitement2(List<String> inputs, boolean etape1) {
        long resultat = 0;
        //on récupère les points
        List<Coord> tiles = new ArrayList<>();
        for (String input : inputs) {
            String[] split = input.split(",");
            tiles.add(new Coord(split[0], split[1]));
        }
        //on construit les rectangles
        //pour la partie2, on fait en sorte que le rectangle soit défini par son coin en haut à gauche (mini)
        //et son coin en bas à droite (maxi).
        List<Rectangle> rectangles = new ArrayList<>();
        for (int i = 0; i < tiles.size() - 1; i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                Rectangle rectangle = Rectangle.depuisCoords(tiles.get(i), tiles.get(j));
                rectangles.add(rectangle);
            }
        }
        if (etape1) {
            resultat = rectangles.stream().map(Rectangle::calculeSurface).max(Comparator.naturalOrder()).orElseThrow();
        } else {
            // on crée des rectangles qui correspondent aux arêtes entre les deux points
            // même s'il s'agit de lignes, cela nous aide pour déterminer l'intersection
            List<Rectangle> greenTiles =
                    IntStream.range(0, tiles.size()).
                            mapToObj(i -> Rectangle.depuisCoords(tiles.get(i), tiles.get((i + 1) % tiles.size())))
                            .toList();
            //Pour chaque rectangle, on vérifie s'il y a une intersection avec une arête
            for (Rectangle rectangle : rectangles) {
                boolean estOk = true;
                for (Rectangle greenTile : greenTiles) {
                    if (rectangle.estEnIntersection(greenTile)) {
                        estOk = false;
                        break;
                    }
                }
                if (estOk) {
                    long area = rectangle.calculeSurface();
                    resultat = Math.max(area,resultat);
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
