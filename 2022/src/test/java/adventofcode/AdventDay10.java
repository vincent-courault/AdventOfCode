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
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class AdventDay10 {

    @Rule
    public TestName name = new TestName();

    private List<String> initTest() throws URISyntaxException, IOException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day10_exemple.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    private List<String> lectureDuFichier() throws URISyntaxException, IOException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day10.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = initTest();
        assertEquals(13140, etape1_main(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier();
        assertEquals(14720, etape1_main(inputs));
    }

    public int etape1_main(List<String> inputs) {

        List<Integer> signaux = new ArrayList<>();
        for (String input : inputs) {
            signaux.add(0);
            if (input.startsWith("addx")) {
                signaux.add(Integer.parseInt(input.split(" ")[1]));
            }
        }

        int sommeSignaux = 0;

        for (int numeroSignal = 20; numeroSignal < 260; numeroSignal += 40) {
            sommeSignaux += getSommeSignaux(signaux, numeroSignal);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + sommeSignaux);
        return sommeSignaux;
    }

    private int getSommeSignaux(List<Integer> signaux, int numeroSignal) {
        return (1 + signaux.subList(0, numeroSignal - 1).stream().flatMapToInt(IntStream::of).sum()) * numeroSignal;
    }

    public String etape2_main(List<String> inputs) {

        List<Integer> signaux = new ArrayList<>();
        for (String input : inputs) {
            signaux.add(0);
            if (input.startsWith("addx")) {
                signaux.add(Integer.parseInt(input.split(" ")[1]));
            }
        }
        List<Integer> spriteOn = new ArrayList<>();
        Integer valeuractuelle = 1;
        for (Integer integer : signaux) {
            spriteOn.add(valeuractuelle);
            valeuractuelle += integer;
        }
        StringBuilder ecran = new StringBuilder();

        for (int numeroLigne = 0; numeroLigne < 6; numeroLigne++) {
            ecritureParLigne(spriteOn, ecran, numeroLigne);
        }

        return ecran.toString();
    }

    private void ecritureParLigne(List<Integer> spriteOn, StringBuilder ecran, int ligne) {
        for (int pixel = ligne * 40; pixel < (ligne +1) * 40; pixel++) {
            int sprite = spriteOn.get(pixel) + ligne * 40;
            if (pixel == sprite || pixel == sprite + 1 || pixel == sprite - 1) {
                ecran.append("#");
            } else {
                ecran.append(".");
            }
        }
        ecran.append("\n");
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = initTest();
        String attendu = """
                ##..##..##..##..##..##..##..##..##..##..
                ###...###...###...###...###...###...###.
                ####....####....####....####....####....
                #####.....#####.....#####.....#####.....
                ######......######......######......####
                #######.......#######.......#######.....
                """;
        assertEquals(attendu, etape2_main(inputs));

    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier();
        StringBuilder attendu = new StringBuilder();
        attendu.append("####.####.###..###..###..####.####.####.").append("\n");
        attendu.append("#.......#.#..#.#..#.#..#.#.......#.#....").append("\n");
        attendu.append("###....#..###..#..#.###..###....#..###..").append("\n");
        attendu.append("#.....#...#..#.###..#..#.#.....#...#....").append("\n");
        attendu.append("#....#....#..#.#....#..#.#....#....#....").append("\n");
        attendu.append("#....####.###..#....###..#....####.#....").append("\n");
        assertEquals(attendu.toString(), etape2_main(inputs));
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : \n" + attendu);
    }

}
