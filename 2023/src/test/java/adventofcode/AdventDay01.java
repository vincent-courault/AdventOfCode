package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay01 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(142, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(55029, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier2(this, true);
        assertEquals(281, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(55686, traitement(inputs, false));
    }


    public int traitement(List<String> inputs, boolean etape1) {
        int resultat = 0;
        Pattern digitRegex;
        if (etape1) {
            digitRegex = Pattern.compile("\\d");
        } else {
            digitRegex = Pattern.compile("\\d|twone|sevenine|eightwo|eighthree|oneight|threeight|fiveight|nineight|one|two|three|four|five|six|seven|eight|nine");
        }
        for (String input : inputs) {
            Matcher numberMatcher = digitRegex.matcher(input);
            long numberCount = numberMatcher.results().count();
            numberMatcher.reset();
            MatchResult premier = numberMatcher.results().findFirst().orElse(null);
            numberMatcher.reset();
            MatchResult dernier = numberMatcher.results().skip(numberCount - 1).findFirst().orElse(null);
            resultat += Integer.parseInt(retourneLaValeur(premier, true) + retourneLaValeur(dernier, false));
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private String retourneLaValeur(MatchResult numberMatcher, boolean premier) {

        return switch (numberMatcher.group()) {
            case "one" -> "1";
            case "two" -> "2";
            case "three" -> "3";
            case "four" -> "4";
            case "five" -> "5";
            case "six" -> "6";
            case "seven" -> "7";
            case "eight" -> "8";
            case "nine" -> "9";
            case "twone" -> premier ? "2" : "1";
            case "sevenine" -> premier ? "7" : "9";
            case "eightwo" -> premier ? "8" : "2";
            case "eighthree" -> premier ? "8" : "3";
            case "oneight" -> premier ? "1" : "8";
            case "threeight" -> premier ? "3" : "8";
            case "fiveight" -> premier ? "5" : "8";
            case "nineight" -> premier ? "9" : "8";
            default -> numberMatcher.group();
        };
    }
}
