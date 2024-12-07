package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay11 extends Commun {


    private static boolean verifieLaPresenceDeDeuxPaires(String s) {
        String m0 = null;
        Matcher matcher = Pattern.compile("([a-z])\\1").matcher(s);
        while (matcher.find()) {
            if (m0 != null && !matcher.group(1).equals(m0)) return true;
            m0 = matcher.group(1);
        }
        return false;
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("vzbxxyzz", traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("vzcaabcc", traitement(inputs, true));
    }

    public String traitement(List<String> inputs, boolean etape2) {
        String resultat;

        String password = inputs.getFirst();
        //https://fr.wikipedia.org/wiki/Syst%C3%A8me_%C3%A0_base_36

        password = trouveLeMotDePasseSuivant(password);

        if (etape2) {
            password = trouveLeMotDePasseSuivant(password);
        }
        resultat = password;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static String trouveLeMotDePasseSuivant(String password) {
        do {
            password = Long.toString(Long.parseLong(password, 36) + 1, 36).replace('0', 'a');
        }
        while (!(verifieLaPresenceDeDeuxPaires(password) &&
                password.matches(".*(abc|bcd|cde|def|efg|fgh|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz).*")
                && !password.matches(".*[iol].*")));
        return password;
    }
}
