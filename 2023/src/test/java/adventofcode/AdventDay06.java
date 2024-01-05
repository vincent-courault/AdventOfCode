package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay06 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(288, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1312850, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(71503, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(36749103, traitement(inputs, false));
    }

    public Long traitement(List<String> inputs, boolean etape1) {
        String inputsDuree;
        String inputsRecords;

        if (etape1) {
            inputsDuree = inputs.get(0);
            inputsRecords = inputs.get(1);
        } else {
            inputsDuree = inputs.get(0).replace(" ", "");
            inputsRecords = inputs.get(1).replace(" ", "");
        }

        Pattern digitRegex = Pattern.compile("\\d+");
        Matcher numberMatcher = digitRegex.matcher(inputsDuree);
        Long[] durees = numberMatcher.results().map(MatchResult::group).map(Long::parseLong).toArray(Long[]::new);

        numberMatcher = digitRegex.matcher(inputsRecords);
        Long[] records = numberMatcher.results().map(MatchResult::group).map(Long::parseLong).toArray(Long[]::new);

        long resultat = 1L;
        for (int i = 0; i < durees.length; i++) {
            long nbRecordBattu = 0L;
            long duree = durees[i];
            long record = records[i];
            for (long j = duree; j >= 0; j--) {
                long distance = (duree - j) * j;
                if (distance > record) {
                    nbRecordBattu++;
                }
            }
            resultat = resultat * nbRecordBattu;
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
