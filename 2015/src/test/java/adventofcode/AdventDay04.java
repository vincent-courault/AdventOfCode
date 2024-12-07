package adventofcode;

import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay04 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(609043, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(282749, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(6742839, traitementEtape2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(9962624, traitementEtape2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat;
        String input = inputs.getFirst();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        int i = 0;
        while (!DatatypeConverter.printHexBinary(md5.digest((input + i).getBytes())).startsWith("00000")) {
            i++;
        }
        resultat = i;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitementEtape2(List<String> inputs) {
        int resultat;
        String input = inputs.getFirst();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        int i = 0;
        while (!DatatypeConverter.printHexBinary(md5.digest((input + i).getBytes())).startsWith("000000")) {
            i++;
        }
        resultat = i;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
