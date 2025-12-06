package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay06 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4277556, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(6503327062445L, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3263827, traitement2(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(9640641878593L, traitement2(inputs, false));
    }

    public long traitement(List<String> inputs) {
        long resultat = 0;
        Pattern pattern = Pattern.compile("\\d+");
        List<List<Integer>> nombres = new ArrayList<>();
        for (int i = 0; i < inputs.size() - 1; i++) {
            String input = inputs.get(i);
            List<Integer> nums = new ArrayList<>();
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                nums.add(Integer.parseInt(matcher.group()));
            }
            nombres.add(nums);
        }
        List<String> signes = List.of(inputs.getLast().replaceAll(" ", "").split(""));

        for (int i = 0; i < signes.size(); i++) {
            String signe = signes.get(i);
            long valeur;
            valeur = determineValeurAPartirDuSigne(signe);
            for (List<Integer> nombre : nombres) {
                if (signe.equals("+")) {
                    valeur += nombre.get(i);
                } else {
                    valeur *= nombre.get(i);
                }
            }
            resultat += valeur;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public long traitement2(List<String> inputs,boolean exemple) {
        long resultat = 0;
        List<String> nombres = new ArrayList<>();
        int indiceSigne;
        int indiceChiffre = inputs.getFirst().length()-1;
        for (int i = 0; i < inputs.size() - 1; i++) {
            String input = inputs.get(i);
            nombres.add(input);
        }
        List<String> signes = List.of(inputs.getLast().replaceAll(" ", "").split(""));
        indiceSigne = signes.size() - 1;

        String v1;
        String v2;
        String v3;
        String v4=" ";
        long valeur;
        valeur = determineValeurAPartirDuSigne(signes.get(indiceSigne));

        for (int i = indiceChiffre; i >= 0; i--) {
            v1 = String.valueOf(nombres.get(0).charAt(i));
            v2 = String.valueOf(nombres.get(1).charAt(i));
            v3 = String.valueOf(nombres.get(2).charAt(i));
            if(!exemple)
                v4 =  String.valueOf(nombres.get(3).charAt(i));
            String signe = signes.get(indiceSigne);
            if (v1.equals(" ") && v2.equals(" ") && v3.equals(" ") && v4.equals(" ")) {
                indiceSigne--;
                resultat += valeur;
                valeur = determineValeurAPartirDuSigne(signes.get(indiceSigne));
            } else {
                int valeurColonne = Integer.parseInt((v1 + v2 + v3 + v4).trim());
                if (signe.equals("+")) {
                    valeur += valeurColonne;
                } else {
                    valeur *= valeurColonne;
                }
            }
        }
        resultat+=valeur;
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static long determineValeurAPartirDuSigne(String signes) {
        long valeur;
        if (signes.equals("+")) {
            valeur = 0;
        } else {
            valeur = 1;
        }
        return valeur;
    }

}
