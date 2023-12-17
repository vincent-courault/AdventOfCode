package adventofcode;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AdventDay17 extends Commun {

    public static int TAILLE_CACHE = 10;
    @Rule
    public TestName name = new TestName();
    private int compteurJet;
    private HashSet<Coord> cave;
    private long nombreDeCycle;
    private int hauteurDuCycle;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3068, traitementEtape1(inputs.get(0)));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(3219, traitementEtape1(inputs.get(0)));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1514285714288L, traitementEtape2(inputs.get(0), 1000000000000L, 10));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1582758620701L, traitementEtape2(inputs.get(0), 1000000000000L, 50));
    }

    public int traitementEtape1(String inputs) {
        cave = new HashSet<>();
        initialiseLeSol(cave);
        List<Boolean> jetPattern = lectureDesJets(inputs);
        List<Set<Coord>> pierres = initialisationDesPierres();
        compteurJet = 0;

        for (int compteurDePierre = 0; compteurDePierre < 2022; compteurDePierre++) {
            int maxY = hauteurDeLaPile();
            Set<Coord> pierreCourante = new HashSet<>(pierres.get(compteurDePierre % 5));
            pierreCourante = pierreCourante.stream().map(x -> x.sum(new Coord(2, maxY + 4))).collect(Collectors.toSet());
            boolean estPosee = false;
            while (!estPosee) {
                if (jetVenantDeGauche(jetPattern)) {
                    if (pointLePlusADroite(pierreCourante) < 6) {
                        pierreCourante = effectueLeDeplacementSiPossible(pierreCourante, Coord.RIGHT);
                    }
                } else {
                    if (pointLePlusAGauche(pierreCourante) > 0) {
                        pierreCourante = effectueLeDeplacementSiPossible(pierreCourante, Coord.LEFT);
                    }
                }
                compteurJet++;
                estPosee = verifieSiLaPierreEstPosee(pierreCourante);
                if (!estPosee) {
                    pierreCourante = pierreCourante.stream().map(x -> x.sum(Coord.DOWN)).collect(Collectors.toSet());
                }
            }
        }
        Integer resultat = cave.stream().map(coord -> coord.y).max(Integer::compare).get();
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }

    public long traitementEtape2(String inputs, long nombrePierre, int tailleCache) {
        TAILLE_CACHE = tailleCache;
        cave = new HashSet<>();
        initialiseLeSol(cave);
        List<Boolean> jetPattern = lectureDesJets(inputs);
        List<Set<Coord>> pierres = initialisationDesPierres();
        compteurJet = 0;

        Map<Set<Coord>, Coord> cache = new HashMap<>();

        boolean cycleIdentifie = false;

        for (long compteurDePierre = 0; compteurDePierre < nombrePierre; compteurDePierre++) {
            int maxY = hauteurDeLaPile();
            Set<Coord> pierreCourante = new HashSet<>(pierres.get((int) (compteurDePierre % 5)));
            pierreCourante = pierreCourante.stream().map(x -> x.sum(new Coord(2, maxY + 4))).collect(Collectors.toSet());
            boolean estPosee = false;
            while (!estPosee) {
                if (jetVenantDeGauche(jetPattern)) {
                    if (pointLePlusADroite(pierreCourante) < 6) {
                        pierreCourante = effectueLeDeplacementSiPossible(pierreCourante, Coord.RIGHT);
                    }
                } else {
                    if (pointLePlusAGauche(pierreCourante) > 0) {
                        pierreCourante = effectueLeDeplacementSiPossible(pierreCourante, Coord.LEFT);
                    }
                }
                compteurJet++;
                estPosee = verifieSiLaPierreEstPosee(pierreCourante);
                if (!estPosee) {
                    pierreCourante = pierreCourante.stream().map(x -> x.sum(Coord.DOWN)).collect(Collectors.toSet());
                } else {
                    int hauteur = hauteurDeLaPile();
                    Set<Coord> cleCache = calculeLaCleDuCache(hauteur);
                    if (!cycleIdentifie && cache.containsKey(cleCache)) {
                        Coord info = cache.get(cleCache);
                        int nombreDePierrePourUnCycle = (int) (compteurDePierre - info.x);
                        hauteurDuCycle = hauteur - info.y;
                        nombreDeCycle = (nombrePierre - compteurDePierre) / nombreDePierrePourUnCycle;
                        compteurDePierre += nombreDeCycle * nombreDePierrePourUnCycle;
                        cycleIdentifie = true;
                    } else {
                        if (!cycleIdentifie) {
                            Coord info = new Coord((int) compteurDePierre, hauteur);
                            cache.put(cleCache, info);
                        }
                    }

                }
            }
        }
        long resultat = cave.stream().map(coord -> coord.y).max(Integer::compare).get() + nombreDeCycle * hauteurDuCycle;
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }

    public Set<Coord> calculeLaCleDuCache(int maxY) {
        return cave.stream().filter(x -> maxY - x.y <= TAILLE_CACHE).map(x -> new Coord(x.x, maxY - x.y)).collect(Collectors.toSet());
    }

    private void visualisation() {
        int max = hauteurDeLaPile() + 1;
        List<int[]> liste = cave.stream().map(coord -> new int[]{coord.x, coord.y}).toList();

        String[][] carte = new String[7][max];

        for (int j = 0; j < 7; j++) {
            for (int k = 0; k < max; k++) {
                carte[j][k] = " ";
            }
        }

        for (int[] coord : liste) {
            carte[coord[0]][coord[1]] = "X";
        }
        for (int j = max - 1; j >= max - 10; j--) {
            for (int k = 0; k < 7; k++) {
                System.out.print(carte[k][j]);
            }
            System.out.println();
        }

    }

    private Set<Coord> effectueLeDeplacementSiPossible(Set<Coord> pierreCourante, Coord direction) {
        Set<Coord> tentative = pierreCourante.stream().map(x -> x.sum(direction)).collect(Collectors.toSet());
        if (laCaveNeContientPas(tentative)) {
            return tentative;
        }
        return pierreCourante;
    }

    private boolean verifieSiLaPierreEstPosee(Set<Coord> pierreCourante) {
        for (Coord c : pierreCourante) {
            if (cave.contains(c.sum(Coord.DOWN))) {
                cave.addAll(pierreCourante);
                return true;
            }
        }
        return false;
    }

    private int pointLePlusAGauche(Set<Coord> pierreCourante) {
        return pierreCourante.stream().map(x -> x.x).min(Integer::compare).get();
    }

    private int pointLePlusADroite(Set<Coord> pierreCourante) {
        return pierreCourante.stream().map(x -> x.x).max(Integer::compare).get();
    }

    private int hauteurDeLaPile() {
        return cave.stream().map(x -> x.y).max(Integer::compare).get();
    }

    private boolean jetVenantDeGauche(List<Boolean> jetPattern) {
        return jetPattern.get(compteurJet % jetPattern.size());
    }

    private void initialiseLeSol(HashSet<Coord> cave) {
        for (int i = 0; i < 7; i++) {
            cave.add(new Coord(i, 0));
        }
    }

    private List<Boolean> lectureDesJets(String inputs) {
        List<Boolean> jetPattern = new ArrayList<>();
        for (char c : inputs.trim().toCharArray()) {
            jetPattern.add(c == '>');
        }
        return jetPattern;
    }

    private List<Set<Coord>> initialisationDesPierres() {
        List<Set<Coord>> pierres = new ArrayList<>();

        Set<Coord> ligneHorizontale = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            ligneHorizontale.add(new Coord(i, 0));
        }
        pierres.add(ligneHorizontale);

        HashSet<Coord> plus = new HashSet<>();
        plus.add(new Coord(0, 1));
        plus.add(new Coord(1, 1));
        plus.add(new Coord(2, 1));
        plus.add(new Coord(1, 2));
        plus.add(new Coord(1, 0));
        pierres.add(plus);

        HashSet<Coord> coin = new HashSet<>();
        coin.add(new Coord(0, 0));
        coin.add(new Coord(1, 0));
        coin.add(new Coord(2, 0));
        coin.add(new Coord(2, 1));
        coin.add(new Coord(2, 2));
        pierres.add(coin);

        HashSet<Coord> ligneVerticale = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            ligneVerticale.add(new Coord(0, i));
        }
        pierres.add(ligneVerticale);

        HashSet<Coord> carre = new HashSet<>();
        carre.add(new Coord(0, 0));
        carre.add(new Coord(0, 1));
        carre.add(new Coord(1, 1));
        carre.add(new Coord(1, 0));
        pierres.add(carre);
        return pierres;
    }

    public boolean laCaveNeContientPas(Set<Coord> small) {
        for (Coord c : small)
            if (cave.contains(c)) return false;
        return true;
    }

    static class Coord {
        public static final Coord DOWN = new Coord(0, -1);
        public static final Coord LEFT = new Coord(-1, 0);
        public static final Coord RIGHT = new Coord(1, 0);
        public int x;
        public int y;

        public Coord(int r, int c) {
            x = r;
            y = c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(y, x);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Coord other = (Coord) obj;
            if (x != other.x) return false;
            return y == other.y;
        }

        public String toString() {
            return "(" + x + "," + y + ")";
        }

        public Coord sum(Coord o) {
            return new Coord(x + o.x, y + o.y);
        }
    }

}