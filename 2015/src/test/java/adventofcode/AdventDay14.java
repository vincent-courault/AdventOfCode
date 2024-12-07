package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay14 extends Commun {
    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1120, traitement(inputs, 1000));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2660, traitement(inputs, 2503));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(689, traitement2(inputs, 1000));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1256, traitement2(inputs, 2503));
    }

    public Integer traitement2(List<String> inputs, int duration) {
        int resultat;
        Pattern reindeer = Pattern.compile("^([a-zA-Z0-9]+).*?(\\d+).*?(\\d+).*?(\\d+)");
        List<Renne> rennes = new ArrayList<>();
        for (String input : inputs) {
            Matcher matcher = reindeer.matcher(input);
            Renne renne = null;
            if (matcher.find()) {
                renne = new Renne(matcher.group(1),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4)));
            }
            rennes.add(renne);
        }
        for (int i = 1; i <= duration; i++) {
            int finalI = i;
            int best = rennes.stream().mapToInt(r -> r.calculeLaDistanceParcourue(finalI)).max().getAsInt();
            rennes.stream().filter(r -> r.calculeLaDistanceParcourue(finalI) == best).forEach(Renne::awardPoint);
        }

        resultat = rennes.stream().mapToInt(Renne::getPoints).max().getAsInt();

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public Integer traitement(List<String> inputs, int duration) {
        int resultat;
        Pattern reindeer = Pattern.compile("^([a-zA-Z0-9]+).*?(\\d+).*?(\\d+).*?(\\d+)");
        List<Renne> rennes = new ArrayList<>();
        for (String input : inputs) {
            Matcher matcher = reindeer.matcher(input);
            Renne renne = null;
            if (matcher.find()) {
                renne = new Renne(matcher.group(1), Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4))
                );
            }
            rennes.add(renne);
        }
        resultat = rennes.stream().mapToInt(r -> r.calculeLaDistanceParcourue(duration)).max().getAsInt();

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private static class Renne {
        int vitesse;
        int dureeVol;
        int dureeRepos;
        String nom;
        private int points;

        public Renne(String nom, int vitesse, int dureeVol, int dureeRepos) {
            this.vitesse = vitesse;
            this.dureeVol = dureeVol;
            this.dureeRepos = dureeRepos;
            this.nom = nom;
            this.points = 0;
        }

        public int calculeLaDistanceParcourue(int duree) {

            int nbCycle = Math.floorDiv(duree, dureeVol + dureeRepos);
            int dureeApresLesCycles = duree - nbCycle * (dureeVol + dureeRepos);
            return nbCycle * vitesse * dureeVol + Math.min(dureeApresLesCycles, dureeVol) * vitesse;
        }

        public int getPoints() {
            return this.points;
        }

        public void awardPoint() {
            this.points++;
        }
    }
}
