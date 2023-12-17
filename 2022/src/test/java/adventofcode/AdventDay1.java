package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdventDay1 {

    @Rule
    public TestName name = new TestName();

    @Test
    public void exemple() {
        List<String> lignes = List.of("1000", "2000", "3000", "", "4000", "", "5000", "6000", "", "7000", "8000", "9000", "", "10000", "");
        int[] resultats = etape1_main(lignes);
        assertEquals(24000, resultats[0]);
        assertEquals(45000, resultats[1]);
    }

    @Test
    public void entree() throws IOException, URISyntaxException {
        URI path = this.getClass().getClassLoader().getResource("input_day1.txt").toURI();
        List<String> lignes = Files.readAllLines(Paths.get(path));
        int[] resultats = etape1_main(lignes);
        assertEquals(70374, resultats[0]);
        assertEquals(204610, resultats[1]);
    }

    public int[] etape1_main(List<String> lignes) {
        int cumul = 0;
        List<Integer> valeursCumul = new ArrayList<>();
        for (String ligne : lignes) {
            if (ligne.isEmpty()) {
                valeursCumul.add(cumul);
                cumul = 0;
            } else {
                int valeur = Integer.parseInt(ligne);
                cumul += valeur;
            }
        }
        Collections.sort(valeursCumul);
        Collections.reverse(valeursCumul);
        int max = Collections.max((valeursCumul));
        int cumuleDes3PlusGrands = valeursCumul.get(0) + valeursCumul.get(1) + valeursCumul.get(2);
        System.out.println(this.getClass().getSimpleName() + " etape1" + name.getMethodName() + " : " + max);
        System.out.println(this.getClass().getSimpleName() + " etape2" + name.getMethodName() + " : " + cumuleDes3PlusGrands);

        return new int[]{max, cumuleDes3PlusGrands};
    }


}
