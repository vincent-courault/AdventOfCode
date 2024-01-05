package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay15 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1320, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(516804, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(145, traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(231844, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        List<String> etapes = Arrays.stream(inputs.getFirst().split(",")).toList();
        for (String etape : etapes) {
            resultat += calculeHASH(etape);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat;
        List<String> etapes = Arrays.stream(inputs.getFirst().split(",")).toList();
        List<Map<String, String>> boites = IntStream.range(0, 256).<Map<String, String>>mapToObj(_ -> new LinkedHashMap<>()).collect(Collectors.toList());

        for (String etape : etapes) {
            String lentille;
            String valeur = "";
            if (etape.contains("=")) {
                lentille = etape.split("=")[0];
                valeur = etape.split("=")[1];
            } else {
                lentille = etape.split("-")[0];
            }
            int numeroBoite = calculeHASH(lentille);
            Map<String, String> boite = boites.get(numeroBoite);
            if (boite.containsKey(lentille)) {
                if (valeur.isEmpty()) {
                    boite.remove(lentille);
                } else {
                    boite.replace(lentille, valeur);
                }
            } else if (!valeur.isEmpty()) {
                boite.put(lentille, valeur);
            }
        }

        resultat = calculeResultat(boites);

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static int calculeHASH(String valeurAHasher) {
        int currentValue = 0;
        for (char caractere : valeurAHasher.toCharArray()) {
            currentValue = ((currentValue + caractere) * 17) % 256;
        }
        return currentValue;
    }

    private static int calculeResultat(List<Map<String, String>> boites) {
        int resultat=0;
        for (int numeroBoite = 0; numeroBoite < boites.size(); numeroBoite++) {
            Map<String, String> boite = boites.get(numeroBoite);
            int j = 1;
            for (String lentille : boite.values()) {
                resultat += (numeroBoite + 1) * j * Integer.parseInt(lentille);
                j++;
            }
        }
        return resultat;
    }
}
