package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay25 extends Commun {

    private List<List<Integer>> cles;
    private List<List<Integer>> serrures;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(3483, traitement(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat;
        List<List<String>> groupes = groupeLesLignes(inputs);
        this.cles = compteToutesLesColonnes(groupes.stream().filter(l -> l.getLast().charAt(0) == '#').toList());
        this.serrures = compteToutesLesColonnes(groupes.stream().filter(l -> l.getFirst().charAt(0) == '#').toList());
        resultat = compteLesCouplesSerruresClesOK();
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public List<List<String>> groupeLesLignes(List<String> inputs) {
        List<List<String>> groupes = new ArrayList<>();
        List<String> courant = new ArrayList<>();
        for (String line : inputs) {
            if (line.isBlank()) {
                groupes.add(courant);
                courant = new ArrayList<>();
            } else {
                courant.add(line);
            }
        }
        if (!courant.isEmpty()) {
            groupes.add(courant);
        }
        return groupes;
    }

    public int compteLesCouplesSerruresClesOK() {
        int resultat = 0;
        for (List<Integer> serrure : serrures) {
            for (List<Integer> cle : cles) {
                List<Integer> l = IntStream.range(0, cle.size()).mapToObj(i1 -> serrure.get(i1) + cle.get(i1)).toList();
                if (l.stream().allMatch(i -> i <= 5)) {
                    resultat++;
                }
            }
        }
        return resultat;
    }


    private List<Integer> compteLesColonnes(List<String> cleOuSerrure) {
        Integer[] comptages = new Integer[cleOuSerrure.getFirst().length()];
        for (int i = 0; i < cleOuSerrure.getFirst().length(); i++) {
            int compte = 0;
            for (String s : cleOuSerrure) {
                if (s.charAt(i) == '#') {
                    compte++;
                }
            }
            comptages[i] = compte - 1;
        }
        return List.of(comptages);
    }

    private List<List<Integer>> compteToutesLesColonnes(List<List<String>> cleOuSerrure) {
        return cleOuSerrure.stream().map(this::compteLesColonnes).toList();
    }
}
