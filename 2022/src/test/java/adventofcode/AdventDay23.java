package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class AdventDay23 extends Commun {

    @Rule
    public TestName name = new TestName();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(110, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(4045, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(20, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(963, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {
        int resultat;
        List<Coord> elfes = initialiseLesElfes(inputs);
        if (etape1) {
            calculeLesDeplacements(elfes, true);
            resultat = calculeLeRectangleEtLesPositionsInnocuppees(elfes);
        } else {
            resultat = calculeLesDeplacements(elfes, false);
        }

        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }

    private List<Coord> initialiseLesElfes(List<String> lines) {
        List<Coord> elfes = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(0).length(); j++) {
                if (lines.get(i).charAt(j) == '#') {
                    elfes.add(new Coord(i, j));
                }
            }
        }
        return elfes;
    }

    public int calculeLesDeplacements(List<Coord> elfes, boolean etape1) {
        int counter = 0;

        boolean finTraitement = false;
        while (!finTraitement) {
            Coord[] propositions = new Coord[elfes.size()];
            for (int indiceElfe = 0; indiceElfe < elfes.size(); indiceElfe++) {
                Coord elfe = elfes.get(indiceElfe);
                boolean aucunElfeDansLeVoisinage = verifieLaPresenceDElfeDansLeVoisinage(elfes, elfe);
                if (aucunElfeDansLeVoisinage) {
                    propositions[indiceElfe] = null;
                    continue;
                }
                propositions[indiceElfe] = determineLaPropositionPossible(elfes, counter, indiceElfe);
            }
            boolean[] propositionEnDouble = verifieSiLesPropositionsSontEnDouble(propositions);
            if (etape1) {
                appliqueLesPropositions(elfes, propositions, propositionEnDouble);
                finTraitement = counter == 9;
            } else {
                finTraitement = appliqueLesPropositions(elfes, propositions, propositionEnDouble) == 0;
            }
            counter++;
        }
        return counter;
    }

    private boolean verifieLaPresenceDElfeDansLeVoisinage(List<Coord> elfes, Coord elf) {
        List<Coord> voisins = getAllVoisins(elf);
        boolean aucunElfeDansLeVoisinage = true;
        for (Coord voisin : voisins) {
            if (elfes.contains(voisin)) {
                aucunElfeDansLeVoisinage = false;
                break;
            }
        }
        return aucunElfeDansLeVoisinage;
    }

    private Coord determineLaPropositionPossible(List<Coord> elfes, int numeroDuTour, int indiceElfe) {
        Coord elf = elfes.get(indiceElfe);
        int tentative = 0;
        while (tentative < 4) {
            List<Coord> propositionsPossibles = recupereLesVoisins(elf, numeroDuTour, tentative);
            boolean pasDeVoisinPourLaProposition = true;
            for (Coord coord : propositionsPossibles) {
                if (elfes.contains(coord)) {
                    pasDeVoisinPourLaProposition = false;
                    break;
                }
            }
            if (pasDeVoisinPourLaProposition) {
                return propositionsPossibles.get(1);
            }
            tentative++;
        }
        return null;
    }

    private boolean[] verifieSiLesPropositionsSontEnDouble(Coord[] propositions) {
        boolean[] propositionEnDouble = new boolean[propositions.length];
        for (int j = 0; j < propositions.length; j++) {
            for (int k = j + 1; k < propositions.length; k++) {
                if (propositions[j] != null && propositions[j].equals(propositions[k])) {
                    propositionEnDouble[j] = true;
                    propositionEnDouble[k] = true;
                }
            }
        }
        return propositionEnDouble;
    }

    private int appliqueLesPropositions(List<Coord> elfes, Coord[] propositions, boolean[] propositionEnDouble) {
        int nombreDeplacementEffectue = 0;
        for (int j = 0; j < elfes.size(); j++) {
            if (!propositionEnDouble[j] && propositions[j] != null) {
                elfes.set(j, propositions[j]);
                nombreDeplacementEffectue++;
            }
        }
        return nombreDeplacementEffectue;
    }

    private int calculeLeRectangleEtLesPositionsInnocuppees(List<Coord> elves) {
        int resultat;
        int maxx, maxy, minx, miny;
        maxx = elves.stream().map(coord -> coord.x).flatMapToInt(IntStream::of).max().orElse(0);
        minx = elves.stream().map(coord -> coord.x).flatMapToInt(IntStream::of).min().orElse(0);
        maxy = elves.stream().map(coord -> coord.y).flatMapToInt(IntStream::of).max().orElse(0);
        miny = elves.stream().map(coord -> coord.y).flatMapToInt(IntStream::of).min().orElse(0);
        resultat = (maxy - miny + 1) * (maxx - minx + 1) - elves.size();
        return resultat;
    }

    public List<Coord> recupereLesVoisins(Coord elf, int tour, int tentative) {
        List<Coord> answer = new ArrayList<>();
        switch ((tour + tentative) % 4) {
            case 0 -> {
                answer.add(new Coord(elf.x - 1, elf.y - 1));
                answer.add(new Coord(elf.x - 1, elf.y));
                answer.add(new Coord(elf.x - 1, elf.y + 1));
            }
            case 1 -> {
                answer.add(new Coord(elf.x + 1, elf.y - 1));
                answer.add(new Coord(elf.x + 1, elf.y));
                answer.add(new Coord(elf.x + 1, elf.y + 1));
            }
            case 2 -> {
                answer.add(new Coord(elf.x + 1, elf.y - 1));
                answer.add(new Coord(elf.x, elf.y - 1));
                answer.add(new Coord(elf.x - 1, elf.y - 1));
            }
            case 3 -> {
                answer.add(new Coord(elf.x + 1, elf.y + 1));
                answer.add(new Coord(elf.x, elf.y + 1));
                answer.add(new Coord(elf.x - 1, elf.y + 1));
            }
        }
        return answer;
    }

    private List<Coord> getAllVoisins(Coord elf) {
        List<Coord> voisins = new ArrayList<>();
        voisins.add(new Coord(elf.x - 1, elf.y - 1));
        voisins.add(new Coord(elf.x - 1, elf.y));
        voisins.add(new Coord(elf.x - 1, elf.y + 1));
        voisins.add(new Coord(elf.x + 1, elf.y));
        voisins.add(new Coord(elf.x + 1, elf.y + 1));
        voisins.add(new Coord(elf.x + 1, elf.y - 1));
        voisins.add(new Coord(elf.x, elf.y - 1));
        voisins.add(new Coord(elf.x, elf.y + 1));
        return voisins;
    }


    static class Coord {
        int x;
        int y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other.getClass().equals(this.getClass())) {
                Coord o = (Coord) other;
                return x == o.x && y == o.y;
            }
            return false;
        }
    }
}