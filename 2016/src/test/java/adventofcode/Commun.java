package adventofcode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class Commun {
    String name;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        name = testInfo.getDisplayName().substring(0, testInfo.getDisplayName().length() - 2);
    }

    List<String> lectureDuFichier(Commun classe, boolean exemple) throws URISyntaxException, IOException {
        String jour = classe.getClass().getSimpleName().substring(9,11);
        String nomFichier;
        if (exemple) {
            nomFichier = "input_day" + jour + "_exemple.txt";
        } else {
            nomFichier = "input_day" + jour + ".txt";
        }
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource(nomFichier)).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    List<String> lectureDuFichier2(Commun classe, boolean exemple) throws URISyntaxException, IOException {
        String jour = classe.getClass().getSimpleName().substring(9);
        String nomFichier;
        if (exemple) {
            nomFichier = "input_day" + jour + "_exemple2.txt";
        } else {
            nomFichier = "input_day" + jour + ".txt";
        }
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource(nomFichier)).toURI();
        return Files.readAllLines(Paths.get(path));
    }


}
