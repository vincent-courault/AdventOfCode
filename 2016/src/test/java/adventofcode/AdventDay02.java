package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay02 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1985, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(47978, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals("5DB3", traitement2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("659AD", traitement2(inputs));
    }

    public int traitement(List<String> inputs) {
        int resultat = 0;
        Grid<Character> clavier = new Grid<>(List.of("123", "456", "789"), new Divider.Character());
        int x = 1, y = 1;
        StringBuilder resultatString = new StringBuilder();
        for (String input : inputs) {
            String[] commandes = input.split("");
            for (String commande : commandes) {
                switch (commande) {
                    case "D" -> {
                        if (clavier.isValid(x + 1, y)) {
                            x++;
                        }
                    }
                    case "U" -> {
                        if (clavier.isValid(x - 1, y)) {
                            x--;
                        }
                    }
                    case "R" -> {
                        if (clavier.isValid(x, y + 1)) {
                            y++;
                        }
                    }
                    case "L" -> {
                        if (clavier.isValid(x, y - 1)) {
                            y--;
                        }
                    }
                }
            }

            resultatString.append(clavier.get(x, y));
        }
        resultat = Integer.parseInt(resultatString.toString());
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public String traitement2(List<String> inputs) {
        String resultat = "";
        Grid<Character> clavier = new Grid<>(List.of("  1  ", " 234 ", "56789", " ABC ", "  D  "), new Divider.Character());
        int x = 2, y = 0;
        StringBuilder resultatString = new StringBuilder();
        for (String input : inputs) {
            String[] commandes = input.split("");
            for (String commande : commandes) {
                switch (commande) {
                    case "D" -> {
                        if (clavier.isValid(x + 1, y) && clavier.get(x + 1, y) != ' ') {
                            x++;
                        }
                    }
                    case "U" -> {
                        if (clavier.isValid(x - 1, y) && clavier.get(x - 1, y) != ' ') {
                            x--;
                        }
                    }
                    case "R" -> {
                        if (clavier.isValid(x, y + 1) && clavier.get(x, y + 1) != ' ') {
                            y++;
                        }
                    }
                    case "L" -> {
                        if (clavier.isValid(x, y - 1) && clavier.get(x, y - 1) != ' ') {
                            y--;
                        }
                    }
                }
            }

            resultatString.append(clavier.get(x, y));
        }
        resultat = (resultatString.toString());
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

}
