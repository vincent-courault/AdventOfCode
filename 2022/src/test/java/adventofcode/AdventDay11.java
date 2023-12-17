package adventofcode;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class AdventDay11 {

    @Rule
    public TestName name = new TestName();
    BigInteger coefGlobal = BigInteger.ONE;

    private List<String> initTest() throws IOException, URISyntaxException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day11_exemple.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    private List<String> lectureDuFichier() throws URISyntaxException, IOException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day11.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    @Test
    public void etape1_exemple() throws IOException, URISyntaxException {
        List<String> assignments = initTest();
        assertEquals("10605", traitement(assignments, 20, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> assignments = lectureDuFichier();
        assertEquals("61005", traitement(assignments, 20, true));
    }

    @Test
    public void etape2_exemple_20tours() throws IOException, URISyntaxException {
        List<String> assignments = initTest();
        assertEquals("10197", traitement(assignments, 20, false));
    }

    @Test
    public void etape2_exemple_1000tours() throws IOException, URISyntaxException {
        List<String> assignments = initTest();
        assertEquals("27019168", traitement(assignments, 1000, false));
    }

    @Test
    public void etape2_exemple_10000tours() throws IOException, URISyntaxException {
        List<String> assignments = initTest();
        assertEquals("2713310158", traitement(assignments, 10000, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> assignments = lectureDuFichier();
        assertEquals("20567144694", traitement(assignments, 10000, false));
    }

    public String traitement(List<String> description, Integer nombreTour, boolean etape1) {
        List<Singe> singes = new ArrayList<>();
        initialisation(description, singes);

        for (int numeroTour = 1; numeroTour <= nombreTour; numeroTour++) {
            executeUnTour(singes, etape1);
        }

        List<Integer> objetsInspectes = singes.stream().map(Singe::getNombreObjetsInspectes).sorted().collect(Collectors.toList());
        Collections.reverse(objetsInspectes);

        BigInteger score = BigInteger.valueOf(objetsInspectes.get(0)).multiply(BigInteger.valueOf(objetsInspectes.get(1)));
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + score);
        return score.toString();
    }

    private void initialisation(List<String> description, List<Singe> singes) {
        Singe singeEnCours;
        for (int n = 0; n < description.size(); n = n + 7) {
            singeEnCours = new Singe();
            singes.add(singeEnCours);
            String[] items = description.get(n + 1).split(": ")[1].split(", ");
            for (String item : items) {
                singeEnCours.items.add(BigInteger.valueOf(Integer.parseInt(item)));
            }
            singeEnCours.operation = description.get(n + 2).split(": ")[1];
            singeEnCours.operateur = singeEnCours.operation.split(" ")[3];
            String coefficientOperation = singeEnCours.operation.split(" ")[4];
            if (!coefficientOperation.equals("old")) {
                singeEnCours.coefficient = BigInteger.valueOf(Integer.parseInt(coefficientOperation));
            } else {
                singeEnCours.coefficient = BigInteger.ZERO;
            }

            singeEnCours.modulo = BigInteger.valueOf(Integer.parseInt(description.get(n + 3).split("by ")[1]));
            this.coefGlobal = coefGlobal.multiply(singeEnCours.modulo);

            singeEnCours.monkeyNumberIfTrue = Integer.parseInt(description.get(n + 4).split("monkey ")[1]);
            singeEnCours.monkeyNumberIfFalse = Integer.parseInt(description.get(n + 5).split("monkey ")[1]);

        }
    }

    private void executeUnTour(List<Singe> singes, boolean etape1) {
        for (Singe singe : singes) {
            for (BigInteger item : singe.items) {
                item = appliquerOperationSurItem(singe, item);
                if (etape1) {
                    item = item.divide(BigInteger.valueOf(3));
                }
                if ((item.mod(singe.modulo)).equals(BigInteger.ZERO)) {
                    singes.get(singe.monkeyNumberIfTrue).items.add(item);
                } else {
                    singes.get(singe.monkeyNumberIfFalse).items.add(item);
                }
                singe.nombreObjetsInspectes++;
            }
            singe.items = new ArrayList<>();
        }
    }

    private BigInteger appliquerOperationSurItem(Singe singe, BigInteger item) {
        BigInteger valeur;
        String operateur = singe.operateur;
        if (operateur.equals("*")) {
            if (singe.coefficient.equals(BigInteger.ZERO)) {
                valeur = item.multiply(item);
                valeur = valeur.mod(coefGlobal);
            } else {
                valeur = item.multiply(singe.coefficient);
            }
        } else {
            if (singe.coefficient.equals(BigInteger.ZERO)) {
                valeur = item.add(item);
            } else {
                valeur = item.add(singe.coefficient);
            }
        }
        return valeur;
    }

    private static class Singe {
        List<BigInteger> items = new ArrayList<>();
        String operation = "";
        String operateur;
        BigInteger coefficient;
        BigInteger modulo;
        int monkeyNumberIfTrue;
        int monkeyNumberIfFalse;
        int nombreObjetsInspectes = 0;

        public int getNombreObjetsInspectes() {
            return nombreObjetsInspectes;
        }

        @Contract(pure = true)
        @Override
        public @NotNull String toString() {
            return "Singe{" + "items=" + items + ", operation='" + operation + '\'' + ", modulo=" + modulo + ", monkeyNumberIfTrue=" + monkeyNumberIfTrue + ", monkeyNumberIfFalse=" + monkeyNumberIfFalse + ", nombreObjetsInspectes=" + nombreObjetsInspectes + '}';
        }
    }
}
