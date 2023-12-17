package adventofcode;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class AdventDay6 {
    @Rule
    public TestName name = new TestName();

    private List<String> lectureDuFichier() throws URISyntaxException, IOException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day6.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    @Test
    public void etape1_exemple() {
        assertEquals(7, etape1_main("mjqjpqmgbljsphdztnvjfqwrcgsmlb"));
        assertEquals(5, etape1_main("bvwbjplbgvbhsrlpgdmjqwftvncz"));
        assertEquals(6, etape1_main("nppdvjthqldpwncqszvftbrmjlhg"));
        assertEquals(10, etape1_main("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"));
        assertEquals(11, etape1_main("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"));

    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> messages = lectureDuFichier();
        assertEquals(1647, etape1_main(messages.get(0)));
    }

    public int etape1_main(String message) {
        return detectionMarqueur(message, 4);
    }

    @Test
    public void etape2_exemple() {
        assertEquals(19, etape2_main("mjqjpqmgbljsphdztnvjfqwrcgsmlb"));
        assertEquals(23, etape2_main("bvwbjplbgvbhsrlpgdmjqwftvncz"));
        assertEquals(23, etape2_main("nppdvjthqldpwncqszvftbrmjlhg"));
        assertEquals(29, etape2_main("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"));
        assertEquals(26, etape2_main("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> assignments = lectureDuFichier();
        assertEquals(2447, etape2_main(assignments.get(0)));
    }

    public int etape2_main(String message) {
        return detectionMarqueur(message, 14);
    }

    private Integer detectionMarqueur(String message, int longueurMarqueur) {
        for (int i = 0; i < message.length() - longueurMarqueur; i++) {
            Character[] tableau = ArrayUtils.toObject(message.substring(i, i + longueurMarqueur).toCharArray());
            Set<Character> morceauMessage = new HashSet<>(Arrays.asList(tableau));
            if (morceauMessage.size() == longueurMarqueur) {
                System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + (i + longueurMarqueur));

                return i + longueurMarqueur;
            }
        }
        return null;
    }

}
