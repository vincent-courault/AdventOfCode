package adventofcode;

import org.apache.commons.math3.linear.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay13 extends Commun {

    //Une résolution avec Apache Math 3 et une résolution algébrique
    @Test
    public void etape1_exemple1() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(480, traitement(inputs, false));
    }

    @Test
    public void etape11() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(36838, traitement(inputs, false));
    }

    @Test
    public void etape2_exemple1() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(875318608908L, traitement(inputs, true));
    }

    @Test
    public void etape21() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(83029436920891L, traitement(inputs, true));
    }

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(480, traitement2(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(36838, traitement2(inputs, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(875318608908L, traitement2(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(83029436920891L, traitement2(inputs, true));
    }

    public long traitement(List<String> inputs, boolean etape2) {
        long resultat = 0;

        double[] constants = new double[2];

        double[][] coefficients = {
                {0, 0},
                {0, 0}
        };
        for (int i = 0; i < inputs.size(); i = i + 4) {
            String s = inputs.get(i).split(",")[0].split("\\+")[1];
            String s2 = inputs.get(i).split(",")[1].split("\\+")[1];

            coefficients[0][0] = Double.parseDouble(s);
            coefficients[1][0] = Double.parseDouble(s2);

            s = inputs.get(i + 1).split(",")[0].split("\\+")[1];
            s2 = inputs.get(i + 1).split(",")[1].split("\\+")[1];
            coefficients[0][1] = Double.parseDouble(s);
            coefficients[1][1] = Double.parseDouble(s2);

            s = inputs.get(i + 2).split(",")[0].split("=")[1];
            s2 = inputs.get(i + 2).split(",")[1].split("=")[1];
            constants[0] = Double.parseDouble(s);
            constants[1] = Double.parseDouble(s2);
            if (etape2) {
                constants[0] = constants[0] + 10000000000000L;
                constants[1] = constants[1] + 10000000000000L;
            }
            RealMatrix matrix = new Array2DRowRealMatrix(coefficients);
            RealVector vector = new ArrayRealVector(constants);

            DecompositionSolver solver = new LUDecomposition(matrix).getSolver();
            RealVector solution = solver.solve(vector);

            //Les résultats ne sont pas des entiers il faut vérifier que le calcul avec les arrondis est ok
            long nbA = Math.round(solution.getEntry(0));
            long nbB = Math.round(solution.getEntry(1));

            if (nbA * coefficients[0][0] + nbB * coefficients[0][1] == constants[0]
                    && nbA * coefficients[1][0] + nbB * coefficients[1][1] == constants[1]) {
                //System.out.println("Solution entière :");
                //System.out.println("x = " + Math.round(nbA));
                //System.out.println("y = " + Math.round(nbB));
                resultat += nbA * 3L + nbB;
            } else {
                //System.out.println("Pas de solution entière.");
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public long traitement2(List<String> inputs, boolean etape2) {
        long resultat = 0;

        long xBoutonA, yBoutonA, xBoutonB, yBoutonB, xCible, yCible;

        long nbA, nbB;

        for (int i = 0; i < inputs.size(); i = i + 4) {
            String s = inputs.get(i).split(",")[0].split("\\+")[1];
            String s2 = inputs.get(i).split(",")[1].split("\\+")[1];

            xBoutonA = Long.parseLong(s);
            yBoutonA = Long.parseLong(s2);

            s = inputs.get(i + 1).split(",")[0].split("\\+")[1];
            s2 = inputs.get(i + 1).split(",")[1].split("\\+")[1];
            xBoutonB = Long.parseLong(s);
            yBoutonB = Long.parseLong(s2);

            s = inputs.get(i + 2).split(",")[0].split("=")[1];
            s2 = inputs.get(i + 2).split(",")[1].split("=")[1];
            xCible = Long.parseLong(s);
            yCible = Long.parseLong(s2);
            if (etape2) {
                xCible += 10000000000000L;
                yCible += 10000000000000L;
            }
            // nbA * xBoutonA + nbB * xBoutonB = xCible
            // nbA * yBoutonA + nbB * yBoutonB = yCible

            //nbB = ((xCible * yBoutonA - yCible * xBoutonA) / (yBoutonA * xBoutonB - yBoutonB * xBoutonA));
            //nbA = (yCible - yBoutonB * nbB) / yBoutonA;

            long determinant = xBoutonA * yBoutonB - xBoutonB * yBoutonA;
            long detA = xCible * yBoutonB - xBoutonB * yCible;
            long detB = xBoutonA * yCible - xCible * yBoutonA;
            nbA = detA / determinant;
            nbB = detB / determinant;

            if (nbA * xBoutonA + nbB * xBoutonB == xCible && nbA * yBoutonA + nbB * yBoutonB == yCible) {
                resultat += 3 * nbA + nbB;
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
