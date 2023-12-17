package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class AdventDay4 {

    @Rule
    public TestName name = new TestName();

    @Test
    public void etape1_exemple() {
        List<String> assignments = List.of("2-4,6-8",
                "2-3,4-5",
                "5-7,7-9",
                "2-8,3-7",
                "6-6,4-6",
                "2-6,4-8");
        assertEquals(2, etape1_main(assignments));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day4.txt")).toURI();
        List<String> assignments = Files.readAllLines(Paths.get(path));
        assertEquals(576, etape1_main(assignments));
    }

    public int etape1_main(List<String> assignments) {
        int valeur = 0;
        for (String assignment : assignments) {
            String[] plages = assignment.split(",");
            int debutPlage1 = Integer.parseInt(plages[0].split("-")[0]);
            int finPlage1 = Integer.parseInt(plages[0].split("-")[1]);

            int debutPlage2 = Integer.parseInt(plages[1].split("-")[0]);
            int finPlage2 = Integer.parseInt(plages[1].split("-")[1]);

            if (debutPlage2 >= debutPlage1 && finPlage2 <= finPlage1
                    || debutPlage1 >= debutPlage2 && finPlage1 <= finPlage2) {
                valeur++;

            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + valeur);
        return valeur;
    }

    @Test
    public void etape2_exemple() {
        List<String> assignments = List.of("2-4,6-8",
                "2-3,4-5",
                "5-7,7-9",
                "2-8,3-7",
                "6-6,4-6",
                "2-6,4-8");
        assertEquals(4, etape2_main(assignments));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day4.txt")).toURI();
        List<String> assignments = Files.readAllLines(Paths.get(path));
        assertEquals(905, etape2_main(assignments));
    }

    public int etape2_main(List<String> assignments) {
        int intersections = 0;
        for (String assignment : assignments) {
            String[] plages = assignment.split(",");
            Arrays.sort(plages);
            List<Integer> liste1 = transformeEnListe(plages, 0);
            List<Integer> liste2 = transformeEnListe(plages, 1);
            Set<Integer> intersection = liste1.stream()
                    .filter(liste2::contains)
                    .collect(Collectors.toSet());
            if (!intersection.isEmpty()) {
                intersections++;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + intersections);
        return intersections;
    }

    private List<Integer> transformeEnListe(String[] plages, int numeroPlage) {
        int debutPlage1 = Integer.parseInt(plages[numeroPlage].split("-")[0]);
        int finPlage1 = Integer.parseInt(plages[numeroPlage].split("-")[1]);
        return IntStream.rangeClosed(debutPlage1, finPlage1)
                .boxed().collect(Collectors.toList());
    }

}
