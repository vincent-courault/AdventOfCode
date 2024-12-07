package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay04 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1514, traitement(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(158835, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(993, traitement(inputs, true));
    }

    public int traitement(List<String> inputs, boolean etape2) {
        int resultat = 0;

        Pattern pattern = Pattern.compile("^(.*)-([0-9]+)\\[([a-z]+)]$");
        Map<String, Integer> correctRoom = new HashMap<>();
        Map<String, Integer> decodedRoom = new HashMap<>();
        for (String room : inputs) {
            Matcher matcher = pattern.matcher(room);
            String checksum = "";
            String name = "";
            int id = 0;
            if (matcher.find()) {
                name = matcher.group(1);
                id = Integer.parseInt(matcher.group(2));
                checksum = matcher.group(3);
            }


            Map<String, Integer> letterCount = new HashMap<>();
            for (String lettre : name.replace("-", "").split("")) {
                letterCount.compute(lettre, (k, v) -> v == null ? 1 : v + 1);
            }
            Comparator<Map.Entry<String, Integer>> byCount = Comparator.comparingInt(Map.Entry::getValue);
            Comparator<Map.Entry<String, Integer>> byCountAndLexical = byCount.reversed().thenComparing(Map.Entry::getKey);
            String checksumToVerify = letterCount.entrySet().stream().sorted(byCountAndLexical).map(Map.Entry::getKey).limit(5).collect(Collectors.joining(""));

            if (checksumToVerify.equals(checksum)) {
                resultat += id;
                correctRoom.put(name, id);
            }
        }
        if (etape2) {
            for (Map.Entry<String, Integer> room : correctRoom.entrySet()) {
                String nom = room.getKey();
                int id = room.getValue();
                StringBuilder nomDecode = new StringBuilder();
                for (Character lettre : nom.replace("-", " ").toCharArray()) {
                    if (lettre != ' ') {
                        nomDecode.append(Character.toString((char) ((lettre - 'a' + id) % 26) + 'a'));
                    } else {
                        nomDecode.append(" ");
                    }
                }
                decodedRoom.put(String.valueOf(nomDecode), id);
                //System.out.println(nomDecode);
            }
            resultat = decodedRoom.get("northpole object storage");
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
