package adventofcode;

import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay05 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals("18f47a30", traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("f77a0e6e", traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals("05ace8e3", traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("999828ec", traitement2(inputs));
    }

    public String traitement(List<String> inputs) {
        StringBuilder resultat;
        String input = inputs.getFirst();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        int i = 0;
        List<String> valeurs = new ArrayList<>();
        while (valeurs.size() != 8) {
            i++;
            if (DatatypeConverter.printHexBinary(md5.digest((input + i).getBytes())).startsWith("00000")) {
                valeurs.add(DatatypeConverter.printHexBinary(md5.digest((input + i).getBytes())));
            }
        }
        resultat = new StringBuilder();
        for (String valeur : valeurs) {
            resultat.append(valeur.charAt(5));
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat.toString().toLowerCase(Locale.ROOT);
    }

    public String traitement2(List<String> inputs) {
        String input = inputs.getFirst();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        int i = 0;
        Map<Integer, String> valeurs = new HashMap<>();
        while (valeurs.size() != 8) {
            i++;
            if (DatatypeConverter.printHexBinary(md5.digest((input + i).getBytes())).startsWith("00000")) {
                String hash = DatatypeConverter.printHexBinary(md5.digest((input + i).getBytes()));
                if (hash.charAt(5) < '8') {
                    valeurs.putIfAbsent(Integer.parseInt(String.valueOf(hash.charAt(5))), String.valueOf(hash.charAt(6)));
                }
            }
        }
        String resultat = valeurs.values().stream().map(String::toLowerCase).collect(Collectors.joining());

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
