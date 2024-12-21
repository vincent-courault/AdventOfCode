package adventofcode;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay21 extends Commun {

    static final int NIVEAU_CLAVIER_NUMERIQUE = 0;
    Map<String, Long> cache = new HashMap<>();
    Map<String, Coord> TOUCHES_CLAVIERS;
    Map<String, Direction> DIRECTIONS = Map.of("^", Direction.N, "v", Direction.S,
            "<", Direction.W, ">", Direction.E);

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(126384, traitement(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(184718, traitement(inputs, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(154115708116294L, traitement(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(228800606998554L, traitement(inputs, true));
    }

    public long traitement(List<String> inputs, boolean etape2) {

        initialiseLesClaviers();
        long resultat = 0;
        long longueur;
        for (String code : inputs) {
            if (etape2) {
                longueur = determineLaLongueurPourFaireLeCode(code, 25, 0);
            } else {
                longueur = determineLaLongueurPourFaireLeCode(code, 2, 0);
            }
            int valeurNumeriqueDuCode = Integer.parseInt(code.substring(0, 3));
            resultat += longueur * valeurNumeriqueDuCode;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private void initialiseLesClaviers() {
        TOUCHES_CLAVIERS = new HashMap<>();
        TOUCHES_CLAVIERS.put("7", new Coord(0, 0));
        TOUCHES_CLAVIERS.put("8", new Coord(0, 1));
        TOUCHES_CLAVIERS.put("9", new Coord(0, 2));
        TOUCHES_CLAVIERS.put("4", new Coord(1, 0));
        TOUCHES_CLAVIERS.put("5", new Coord(1, 1));
        TOUCHES_CLAVIERS.put("6", new Coord(1, 2));
        TOUCHES_CLAVIERS.put("1", new Coord(2, 0));
        TOUCHES_CLAVIERS.put("2", new Coord(2, 1));
        TOUCHES_CLAVIERS.put("3", new Coord(2, 2));
        TOUCHES_CLAVIERS.put("0", new Coord(3, 1));
        TOUCHES_CLAVIERS.put("A", new Coord(3, 2));
        TOUCHES_CLAVIERS.put("^", new Coord(0, 1));
        TOUCHES_CLAVIERS.put("<", new Coord(1, 0));
        TOUCHES_CLAVIERS.put("v", new Coord(1, 1));
        TOUCHES_CLAVIERS.put(">", new Coord(1, 2));
        TOUCHES_CLAVIERS.put("a", new Coord(0, 2));
    }

    private long determineLaLongueurPourFaireLeCode(String code, int nbRobot, int niveau) {
        String cle = code + nbRobot + niveau;
        if (cache.containsKey(cle)) {
            return cache.get(cle);
        }
        Coord positionAEviter;
        Coord positionCourante;
        if (niveau == NIVEAU_CLAVIER_NUMERIQUE) {
            positionAEviter = new Coord(3, 0);
            positionCourante = TOUCHES_CLAVIERS.get("A");
        } else {
            positionAEviter = new Coord(0, 0);
            positionCourante = TOUCHES_CLAVIERS.get("a");
        }
        long longueur = 0;
        for (String toucheAAppuyer : code.split("")) {
            Coord positionSuivante = TOUCHES_CLAVIERS.get(toucheAAppuyer);
            List<String> cheminsPossibles = determineLesCheminsPossibles(positionCourante, positionSuivante, positionAEviter);
            if (niveau == nbRobot) {
                longueur += cheminsPossibles.getFirst().length();
            } else {
                long mini = Long.MAX_VALUE;
                for (String chemin : cheminsPossibles) {
                    mini = Math.min(determineLaLongueurPourFaireLeCode(chemin, nbRobot, niveau + 1), mini);
                }
                longueur += mini;
            }
            positionCourante = positionSuivante;
        }
        cache.put(cle, longueur);
        return longueur;
    }

    private List<String> determineLesCheminsPossibles(Coord debut, Coord fin, Coord positionAEviter) {
        Direction delta = debut.calculeEcart(fin);
        String deplacementAEffectuer = "";
        int ecartLigne = delta.ligne;
        int ecartColonne = delta.colonne;
        deplacementAEffectuer += ecartLigne < 0 ? "^".repeat(abs(ecartLigne)) : "v".repeat(ecartLigne);
        deplacementAEffectuer += ecartColonne < 0 ? "<".repeat(abs(ecartColonne)) : ">".repeat(ecartColonne);

        List<String> permutations = determineLesPermutations(deplacementAEffectuer);

        return permutations.stream().
                filter(permutation -> valideLePermutation(permutation, debut, positionAEviter))
                .map(permutation -> permutation + "a").distinct().toList();
    }

    public boolean valideLePermutation(String code, Coord start, Coord positionAEviter) {
        // Il faut vérifier si on n'atteint pas une position impossible
        Coord position = new Coord(start.ligne, start.colonne);
        for (int i = 0; i < code.length(); i++) {
            position = position.deplace(DIRECTIONS.get(code.substring(i, i + 1)));
            if (position.equals(positionAEviter)) {
                return false;
            }
        }
        return true;
    }

    public List<String> determineLesPermutations(final String string) {
        return CollectionUtils.permutations(Arrays.stream(string.split("")).toList())
                .stream()
                .map(strings -> String.join("", strings)).distinct()
                .collect(Collectors.toList());
    }
}
