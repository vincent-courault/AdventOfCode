package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay03 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4, traitement(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(115348, traitement(inputs, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3, traitement(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(188, traitement(inputs, true));
    }

    public long traitement(List<String> inputs, boolean etape2) {
        long resultat=0;
        Grid<Integer> grille = new Grid<>(1000, 1000, 0);
        Pattern pattern = Pattern.compile("\\d+");
        List<List<Integer>> formes = new ArrayList<>();

        for (String input : inputs) {
            List<Integer> nums = new ArrayList<>();
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                nums.add(Integer.parseInt(matcher.group()));
            }
            formes.add(nums);
            for (int i = nums.get(1); i < nums.get(1) + nums.get(3); i++) {
                for (int j = nums.get(2); j < nums.get(2) + nums.get(4); j++) {
                    if (grille.get(i, j) == 0) {
                        grille.set(i, j, nums.getFirst());
                    } else {
                        grille.set(i, j, -1);
                    }
                }
            }
        }
        if (etape2) {
            for (List<Integer> nums : formes) {
                if (grille.compte(nums.get(0)) == nums.get(3) * nums.get(4)) {
                    resultat = nums.getFirst();
                    break;
                }
            }
        }else{
            resultat = grille.compte(-1);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
