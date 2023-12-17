package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdventDay25 extends Commun {

    @Rule
    public TestName name = new TestName();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals("2=-1=0", traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("2=--=0000-1-0-=1=0=2", traitement(inputs));
    }

    public String traitement(List<String> inputs) {
        String resultat;

        long somme = 0;
        for(String s : inputs) {
            String nombreSnafu = new StringBuffer(s).reverse().toString();
            long num = 0;
            for(int i = 0; i < nombreSnafu.length(); i++) {
                double coefficient = Math.pow(5,i);
                switch (nombreSnafu.charAt(i)) {
                    case '=' -> num += -2L * coefficient;
                    case '-' -> num += -1L * coefficient;
                    case '1' -> num += coefficient;
                    case '2' -> num += 2L * coefficient;
                }
            }
            somme += num;
        }
        resultat= convertirEnSnafu(somme);

        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }
    public String convertirEnSnafu(long num) {
        if(num == 0L)
            return "";
        return switch ((int) (num % 5)) {
            case 0 -> convertirEnSnafu(num / 5L) + "0";
            case 1 -> convertirEnSnafu(num / 5L) + "1";
            case 2 -> convertirEnSnafu(num / 5L) + "2";
            case 3 -> convertirEnSnafu((num + 5) / 5L) + "=";
            case 4 -> convertirEnSnafu((num + 5) / 5L) + "-";
            default -> null;
        };
    }
}
