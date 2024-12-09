package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay09 extends Commun {

    private static long comptage(List<Block> blocks) {
        long position = 0;
        long somme = 0;
        for (Block b : blocks) {
            //on compte le bloc initial
            for (int i = 0; i < b.longueur; i++) {
                somme += position * b.id;
                position++;
            }
            //on continue de compter avec les blocs qui ont été déplacés dans l'espace libre
            for (int n : b.espaceLibre) {
                somme += position * n;
                position++;
            }
            position += b.longueurLibre; // on incrémente la position de l'espace libre
        }
        return somme;
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1928, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(6395800119709L, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(2858, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(6418529470362L, traitement2(inputs));
    }

    public long traitement(List<String> inputs) {
        long resultat;
        List<Integer> liste = creeListe(inputs);

        //Remplissage des espaces vides
        List<Integer> listeMaj = new ArrayList<>();
        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i) == -1) {
                while (liste.getLast() == -1) {
                    liste.removeLast();
                }
                listeMaj.add(liste.getLast());
                liste.removeLast();
            } else {
                listeMaj.add(liste.get(i));
            }
        }
        resultat = IntStream.range(0, listeMaj.size()).mapToLong(i -> (long) i * listeMaj.get(i)).sum();
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static List<Integer> creeListe(List<String> inputs) {
        List<Integer> liste = new ArrayList<>();
        int id = 0;
        String[] data = inputs.getFirst().split("");
        for (int i = 0; i < data.length; i++) {
            int valeur = Integer.parseInt(data[i]);

            if (i % 2 == 0) {
                for (int j = 0; j < valeur; j++) {
                    liste.add(id);
                }
                id++;
            } else {
                for (int j = 0; j < valeur; j++) {
                    liste.add(-1);
                }
            }
        }
        return liste;
    }

    public long traitement2(List<String> inputs) {
        long resultat;
        int[] input = Arrays.stream(inputs.getFirst().split("")).mapToInt(Integer::parseInt).toArray();
        List<Block> blocks = new ArrayList<>();

        for (int i = 0; i < input.length; i++) {
            if (i % 2 == 0) {
                blocks.add(new Block(i / 2, input[i], 0));
            } else {
                blocks.getLast().longueurLibre = input[i];
            }
        }

        int depuisLeDebut;
        int depuisLaFin = blocks.size() - 1;
        while (depuisLaFin > 0) {
            depuisLeDebut = 0;
            while (depuisLeDebut < depuisLaFin) {
                if (blocks.get(depuisLeDebut).longueurLibre >= blocks.get(depuisLaFin).longueur) {
                    Block blocADeplacer = blocks.get(depuisLaFin);

                    for (int i = 0; i < blocADeplacer.longueur; i++) {
                        blocks.get(depuisLeDebut).espaceLibre.add(blocADeplacer.id);
                    }
                    //on décrémente l'espace libre de la longueur du bloc repositionné
                    blocks.get(depuisLeDebut).longueurLibre -= blocADeplacer.longueur;
                    // même si on a déplacé, on conserve l'espace, on le met à 0 pour ne pas l'inclure dans le comptage
                    blocADeplacer.id = 0;
                    break;
                }
                depuisLeDebut++;
            }
            depuisLaFin--;
        }

        resultat = comptage(blocks);
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);

        return resultat;
    }

    static class Block {
        int id, longueur, longueurLibre;
        List<Integer> espaceLibre;

        public Block(int id, int longueur, int longueurLibre) {
            this.id = id;
            this.longueur = longueur;
            this.longueurLibre = longueurLibre;
            espaceLibre = new ArrayList<>();
        }
    }
}