package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class AdventDay2 {

    public static final String PIERRE = "A";
    public static final String PAPIER = "B";
    public static final String CISEAUX = "C";
    public static final String CISEAUX_J1 = "Z";
    public static final String PIERRE_J1 = "X";
    public static final String PAPIER_J1 = "Y";
    public static final String DEFAITE = "X";
    public static final String MATCH_NUL = "Y";
    public static final String VICTOIRE = "Z";
    public static final int SCORE_VICTOIRE = 6;
    public static final int SCORE_MATCH_NUL = 3;
    @Rule
    public TestName name = new TestName();

    @Test
    public void etape1_exemple() {
        List<String> manches = List.of("A Y", "B X", "C Z");

        assertEquals(15, etape1_main(manches));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day2.txt")).toURI();
        List<String> manches = Files.readAllLines(Paths.get(path));
        assertEquals(13052, etape1_main(manches));
    }

    public int etape1_main(List<String> manches) {

        int cumulScore = 0;
        for (String manche : manches) {
            String[] joueurs = manche.split(" ");
            String joueur1 = joueurs[0];
            String joueur2 = joueurs[1];
            int scoreMancheJoueur2 = 0;
            switch (joueur2) {
                case PIERRE_J1 -> {
                    scoreMancheJoueur2 = 1;
                    scoreMancheJoueur2 = switch (joueur1) {
                        case PIERRE -> matchNul(scoreMancheJoueur2);
                        case PAPIER -> defaite(scoreMancheJoueur2);
                        case CISEAUX -> victoire(scoreMancheJoueur2);
                        default -> scoreMancheJoueur2;
                    };
                }
                case PAPIER_J1 -> {
                    scoreMancheJoueur2 = 2;
                    scoreMancheJoueur2 = switch (joueur1) {
                        case PIERRE -> victoire(scoreMancheJoueur2);
                        case PAPIER -> matchNul(scoreMancheJoueur2);
                        case CISEAUX -> defaite(scoreMancheJoueur2);
                        default -> scoreMancheJoueur2;
                    };
                }
                case CISEAUX_J1 -> {
                    scoreMancheJoueur2 = 3;
                    scoreMancheJoueur2 = switch (joueur1) {
                        case PIERRE -> defaite(scoreMancheJoueur2);
                        case PAPIER -> victoire(scoreMancheJoueur2);
                        case CISEAUX -> matchNul(scoreMancheJoueur2);
                        default -> scoreMancheJoueur2;
                    };
                }
            }
            cumulScore += scoreMancheJoueur2;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + cumulScore);

        return cumulScore;
    }

    private int defaite(int scoreMancheJoueur2) {
        return scoreMancheJoueur2;
    }

    private int matchNul(int scoreMancheJoueur2) {
        scoreMancheJoueur2 = joueCiseaux(scoreMancheJoueur2);
        return scoreMancheJoueur2;
    }

    private int victoire(int scoreMancheJoueur2) {
        scoreMancheJoueur2 += 6;
        return scoreMancheJoueur2;
    }

    @Test
    public void etape2_exemple() throws Exception {
        List<String> manches = List.of("A Y", "B X", "C Z");
        int valeur = etape2_main(manches);
        assertEquals(valeur, 12);
    }

    @Test
    public void etape2() throws Exception {

        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day2.txt")).toURI();
        List<String> manches = Files.readAllLines(Paths.get(path));
        int valeur = etape2_main(manches);
        assertEquals(13693, valeur);
    }

    public int etape2_main(List<String> manches) {

        int cumulScore = 0;
        for (String manche : manches) {
            String[] joueurs = manche.split(" ");
            String joueur1 = joueurs[0];
            String joueur2 = joueurs[1];

            int scoreMancheJoueur2 = 0;
            switch (joueur2) {
                case DEFAITE -> scoreMancheJoueur2 = switch (joueur1) {
                    case PIERRE -> joueCiseaux(scoreMancheJoueur2);
                    case PAPIER -> jouePierre(scoreMancheJoueur2);
                    case CISEAUX -> jouePapier(scoreMancheJoueur2);
                    default -> scoreMancheJoueur2;
                };
                case MATCH_NUL -> {
                    scoreMancheJoueur2 = SCORE_MATCH_NUL;
                    scoreMancheJoueur2 = switch (joueur1) {
                        case PIERRE -> jouePierre(scoreMancheJoueur2);
                        case PAPIER -> jouePapier(scoreMancheJoueur2);
                        case CISEAUX -> joueCiseaux(scoreMancheJoueur2);
                        default -> scoreMancheJoueur2;
                    };
                }
                case VICTOIRE -> {
                    scoreMancheJoueur2 = SCORE_VICTOIRE;
                    scoreMancheJoueur2 = switch (joueur1) {
                        case PIERRE -> jouePapier(scoreMancheJoueur2);
                        case PAPIER -> joueCiseaux(scoreMancheJoueur2);
                        case CISEAUX -> jouePierre(scoreMancheJoueur2);
                        default -> scoreMancheJoueur2;
                    };
                }
            }
            cumulScore += scoreMancheJoueur2;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + cumulScore);

        return cumulScore;
    }

    private int jouePapier(int scoreMancheJoueur2) {
        scoreMancheJoueur2 += 2;
        return scoreMancheJoueur2;
    }

    private int jouePierre(int scoreMancheJoueur2) {
        scoreMancheJoueur2 += 1;
        return scoreMancheJoueur2;
    }

    private int joueCiseaux(int scoreMancheJoueur2) {
        scoreMancheJoueur2 += 3;
        return scoreMancheJoueur2;
    }
}
