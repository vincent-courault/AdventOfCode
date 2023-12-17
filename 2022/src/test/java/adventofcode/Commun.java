package adventofcode;

import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class Commun {

    @Rule
    public TestName name = new TestName();

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

}
