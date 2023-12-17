package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay03 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(4361, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(537832, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(467835, traitementEtape2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(81939900, traitementEtape2(inputs));
    }

    public int traitement(List<String> inputs) {
        Point[][] carte = constitutionCarte(inputs);
        Set<String> parts = new HashSet<>();
        for (int indiceLigne = 0; indiceLigne < 140; indiceLigne++) {
            for (int indiceColonne = 0; indiceColonne < 140; indiceColonne++) {
                if (estUnSymbole(carte[indiceLigne][indiceColonne])) {
                    rechercheLaPresenceDunNombreDansLesVoisins(carte, parts, indiceColonne, indiceLigne);
                }
            }
        }
        int resultat = parts.stream().map(s -> Integer.valueOf(s.split("X")[1])).reduce(0, Integer::sum);
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int traitementEtape2(List<String> inputs) {
        int resultat = 0;
        Point[][] carte = constitutionCarte(inputs);
        Set<String> parts = new HashSet<>();

        for (int indiceLigne = 0; indiceLigne < 140; indiceLigne++) {
            for (int indiceColonne = 0; indiceColonne < 140; indiceColonne++) {
                if (estUnEngrenage(carte[indiceLigne][indiceColonne])) {
                    rechercheLaPresenceDunNombreDansLesVoisins(carte, parts, indiceColonne, indiceLigne);
                }
                if (parts.size() == 2) {
                    resultat += Integer.parseInt(parts.stream().findFirst().orElse("").split("X")[1])
                            * Integer.parseInt(parts.stream().skip(1).findFirst().orElse("").split("X")[1]);
                }
                parts.clear();
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private Point[][] constitutionCarte(List<String> inputs) {
        int indiceLigne = 0;
        Point[][] carte = new Point[140][140];
        for (String ligne : inputs) {
            List<String> numbers = Pattern.compile("\\d+").matcher(ligne).results().map(MatchResult::group).toList();
            int indiceColonne = 0;
            int numPart = 0;
            boolean partUsed = false;
            char[] charArray = ligne.toCharArray();
            for (Character point : charArray) {
                carte[indiceLigne][indiceColonne] = new Point(point);
                if (estUnChiffre(point)) {
                    //Préfixe pour éviter les problèmes avec les valeurs présentes sur plusieurs lignes
                    carte[indiceLigne][indiceColonne].setPartNumber("" + indiceLigne + numPart + "X" + numbers.get(numPart));
                    partUsed = true;
                } else if (partUsed) {
                    numPart++;
                    partUsed = false;
                }
                indiceColonne++;
            }
            indiceLigne++;
        }
        return carte;
    }

    private boolean estUnSymbole(Point point) {
        return point != null && point.valeur != '.' && !estUnChiffre(point.valeur);
    }

    private boolean estUnChiffre(Character point) {
        return point >= '0' && point <= '9';
    }

    private boolean estUnEngrenage(Point point) {
        return point != null && point.valeur == '*';
    }

    private void rechercheLaPresenceDunNombreDansLesVoisins(Point[][] carte, Set<String> parts, int j, int i) {
        //Recherche dans les voisins, s'il y a un nombre renseigné
        //au-dessus à gauche
        if (carte[i - 1][j - 1].partNumber != null) parts.add(carte[i - 1][j - 1].partNumber);
        //au-dessus
        if ((carte[i - 1][j].partNumber != null)) parts.add(carte[i - 1][j].partNumber);
        //au-dessus à droite
        if ((carte[i - 1][j + 1].partNumber != null)) parts.add(carte[i - 1][j + 1].partNumber);
        //à droite
        if ((carte[i][j - 1].partNumber != null)) parts.add(carte[i][j - 1].partNumber);
        //à gauche
        if ((carte[i][j + 1].partNumber != null)) parts.add(carte[i][j + 1].partNumber);
        //au-dessous à gauche
        if ((carte[i + 1][j - 1].partNumber != null)) parts.add(carte[i + 1][j - 1].partNumber);
        //au-dessous
        if ((carte[i + 1][j].partNumber != null)) parts.add(carte[i + 1][j].partNumber);
        //au-dessous à droite
        if ((carte[i + 1][j + 1].partNumber != null)) parts.add(carte[i + 1][j + 1].partNumber);
    }

    static class Point {
        Character valeur;
        String partNumber;

        public Point(Character valeur) {
            this.valeur = valeur;
        }

        public void setPartNumber(String partNumber) {
            this.partNumber = partNumber;
        }
    }
}
