package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay07 extends Commun {

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(46065, traitement(inputs));
    }


    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(14134, traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        Map<String, String> commandes = new HashMap<>();
        for (String input : inputs) {
            String[] command = input.split("-> ");
            commandes.put(command[1].trim(), command[0].trim());
        }

        resultat = calcule(commandes, "a");

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitement2(List<String> inputs) {
        int resultat = 0;
        Map<String, String> commandes = new HashMap<>();
        for (String input : inputs) {
            String[] command = input.split("-> ");
            commandes.put(command[1].trim(), command[0].trim());
        }
        commandes.put("b", String.valueOf(46065));
        resultat = calcule(commandes, "a");

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private int calcule(Map<String, String> commandes, String valeur) {
        String cle = valeur;
        if (commandes.containsKey(valeur)) {
            valeur = commandes.get(valeur);
        }

        if (valeur.contains(" OR ")) {
            String[] valeurs = valeur.split(" OR ");
            int valeur1 = calcule(commandes, valeurs[0]);
            int valeur2 = calcule(commandes, valeurs[1]);
            commandes.put(cle, String.valueOf(valeur1 | valeur2));
            return valeur1 | valeur2;
        }
        if (valeur.contains(" AND ")) {
            String[] valeurs = valeur.split(" AND ");
            int valeur1 = calcule(commandes, valeurs[0]);
            int valeur2 = calcule(commandes, valeurs[1]);
            commandes.put(cle, String.valueOf(valeur1 & valeur2));
            return valeur1 & valeur2;
        }

        if (valeur.contains(" RSHIFT ")) {
            String[] valeurs = valeur.split(" RSHIFT ");
            int valeur1 = calcule(commandes, valeurs[0]);
            int valeur2 = calcule(commandes, valeurs[1]);
            commandes.put(cle, String.valueOf(valeur1 >> valeur2));
            return valeur1 >> valeur2;
        }

        if (valeur.contains(" LSHIFT ")) {
            String[] valeurs = valeur.split(" LSHIFT ");
            int valeur1 = calcule(commandes, valeurs[0]);
            int valeur2 = calcule(commandes, valeurs[1]);
            commandes.put(cle, String.valueOf(valeur1 << valeur2));

            return valeur1 << valeur2;
        }

        if (valeur.contains("NOT ")) {
            String[] valeurs = valeur.split("NOT ");
            int valeur1 = calcule(commandes, valeurs[1]);
            commandes.put(cle, String.valueOf(~valeur1));
            return ~valeur1;
        }


        try {
            commandes.put(cle, valeur);
            return Integer.parseInt(valeur);
        } catch (NumberFormatException nfe) {
            int result = calcule(commandes, valeur);
            return calcule(commandes, String.valueOf(result));
        }
    }

}
