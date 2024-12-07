package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay06 extends Commun {

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(377891, traitement(inputs));
    }


    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(14110788, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat;
        Pattern compile = Pattern.compile("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)");
        int[][] lights = new int[1000][1000];
        for (String line : inputs) {
            Matcher matcher = compile.matcher(line);
            if (matcher.matches()) {
                for (int x = Integer.parseInt(matcher.group(2)); x <= Integer.parseInt(matcher.group(4)); x++) {
                    for (int y = Integer.parseInt(matcher.group(3)); y <= Integer.parseInt(matcher.group(5)); y++) {
                        if (matcher.group(1).equals("turn on")) {
                            lights[x][y] = 1;
                        }
                        if (matcher.group(1).equals("turn off")) {
                            lights[x][y] = 0;
                        }
                        if (matcher.group(1).equals("toggle")) {
                            lights[x][y] = 1 - lights[x][y];
                        }
                    }
                }
            }
        }
        resultat=Stream.of(lights).flatMapToInt(IntStream::of).sum();
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat;
        Pattern compile = Pattern.compile("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)");
        int[][] lights = new int[1000][1000];
        for (String line : inputs) {
            Matcher matcher = compile.matcher(line);
            if (matcher.matches()) {
                for (int x = Integer.parseInt(matcher.group(2)); x <= Integer.parseInt(matcher.group(4)); x++) {
                    for (int y = Integer.parseInt(matcher.group(3)); y <= Integer.parseInt(matcher.group(5)); y++) {
                        if (matcher.group(1).equals("turn on")) {
                            lights[x][y] += 1;
                        }
                        if (matcher.group(1).equals("turn off")) {
                            if (lights[x][y] > 0)
                                lights[x][y] -= 1;                        }
                        if (matcher.group(1).equals("toggle")) {
                            lights[x][y] += 2;                        }
                    }
                }
            }
        }
        resultat=Stream.of(lights).flatMapToInt(IntStream::of).sum();
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }


}
