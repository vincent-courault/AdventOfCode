package adventofcode;

import com.microsoft.z3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay24 extends Commun {
    static double min;
    static double max;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(2, traitement(inputs, true, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(15593, traitement(inputs, true, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(757031940316991L, traitement(inputs, false, false));
    }

    public long traitement(List<String> inputs, boolean etape1, boolean exemple) {
        long resultat = 0;
        List<Hailstone> stones = new ArrayList<>();
        for (String line : inputs) {
            Hailstone tmp = new Hailstone(line);
            stones.add(tmp);
        }
        if (etape1) {
            min = exemple ? 7 : 200000000000000.0;
            max = exemple ? 27 : 400000000000000.0;
            for (int i = 0; i < stones.size(); i++) {
                for (int j = i + 1; j < stones.size(); j++) {
                    Hailstone pierre1 = stones.get(i);
                    Hailstone pierre2 = stones.get(j);
                    if (calculeLIntersectionEtVerifieLesBornes(pierre1, pierre2)) {
                        resultat++;
                    }
                }
            }
        } else {
            // Il existe une pierre de coordonnées x,y,z et de vitesse vx,vy,vz qui rentre en collision avec chacune des pierres
            // à un instant t0, t1, t2.
            // Connaissant les équations pour chacun des pierres, on peut écrire le système d'équation pour 3 pierres
            // on obtient ainsi 9 équations avec 9 inconnues (les 6 paramètres de la pierre qu'on cherche + les 3 instants d'impact)
            // on résoud avec des outils externes
            produitLesEquationsPourResolutionSage(stones);
            produitLesEquationsPourResolutionMathematica(stones);
            // ou résolution via Z3 Solver
            resultat = resolutionViaZ3(stones);
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public static boolean calculeLIntersectionEtVerifieLesBornes(Hailstone L1, Hailstone L2) {
        //Code adapté de https://paulbourke.net/geometry/pointlineplane/Helpers.cs
        double d = ((L2.yvitesse) * (L1.xvitesse)) - ((L2.xvitesse) * (L1.yvitesse));
        double n_a = (L2.xvitesse) * (L1.y - L2.y) - (L2.yvitesse) * (L1.x - L2.x);
        double n_b = (L1.xvitesse) * (L1.y - L2.y) - (L1.yvitesse) * (L1.x - L2.x);
        if (d == 0)
            return false;
        double ua = n_a / d;
        double ub = n_b / d;
        if (ua >= 0 && ub >= 0) { // On ne considère que les intersections à venir pas celles passées
            double X = L1.x + (ua * (L1.xvitesse));
            double Y = L1.y + (ua * (L1.yvitesse));
            return X > min && X < max && Y > min && Y < max;
        }
        return false;
    }

    private static void produitLesEquationsPourResolutionSage(List<Hailstone> stones) {
        StringBuilder equations;
        equations = new StringBuilder();
        equations.append("x,y,z,vx,vy,vz,t0,t1,t2= var('x,y,z,vx,vy,vz,t0,t1,t2')\n");
        for (int i = 0; i < 3; i++) {
            String t = "*t" + i;
            equations.append("eq").append(i * 3).append("=").append(stones.get(i).x).append(" + ").append(stones.get(i).xvitesse).append(t).append(" == x + vx").append(t).append("\n");
            equations.append("eq").append(i * 3 + 1).append("=").append(stones.get(i).y).append(" + ").append(stones.get(i).yvitesse).append(t).append(" == y + vy").append(t).append("\n");
            equations.append("eq").append(i * 3 + 2).append("=").append(stones.get(i).z).append(" + ").append(stones.get(i).zvitesse).append(t).append(" == z + vz").append(t).append("\n");
        }
        equations.append("solve([eq1,eq2,eq3,eq4,eq5,eq6,eq7,eq8,eq0],x,y,z,vx,vy,vz,t0,t1,t2)\n");
        String resolutionParSageMath = equations.substring(0, equations.length() - 1);
        System.out.println("Résolution Par Sage Math");
        System.out.println("https://sagecell.sagemath.org/");
        System.out.println("------------------------------------------------------");
        System.out.println(resolutionParSageMath);
        System.out.println("------------------------------------------------------");

    }

    private static void produitLesEquationsPourResolutionMathematica(List<Hailstone> stones) {
        StringBuilder equations = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            String t = "t" + i;
            equations.append(stones.get(i).x).append(" + ").append(stones.get(i).xvitesse).append(t).append(" == x + vx ").append(t).append(", ");
            equations.append(stones.get(i).y).append(" + ").append(stones.get(i).yvitesse).append(t).append(" == y + vy ").append(t).append(", ");
            equations.append(stones.get(i).z).append(" + ").append(stones.get(i).zvitesse).append(t).append(" == z + vz ").append(t).append(", ");
        }
        String resolutionParMathematica = "Solve[{" + equations.substring(0, equations.length() - 2) + "}, {x,y,z,vx,vy,vz,t0,t1,t2}]";
        System.out.println("Resolution par Mathematica");
        System.out.println("------------------------------------------------------");
        System.out.println(resolutionParMathematica);
        System.out.println("------------------------------------------------------");
    }

    private long resolutionViaZ3(List<Hailstone> stones) {

        Context ctx = new Context();
        Solver solver = ctx.mkSolver();

        //On crée les variables
        RealExpr x = ctx.mkRealConst(ctx.mkSymbol("x"));
        RealExpr y = ctx.mkRealConst(ctx.mkSymbol("y"));
        RealExpr z = ctx.mkRealConst(ctx.mkSymbol("z"));
        RealExpr vx = ctx.mkRealConst(ctx.mkSymbol("vx"));
        RealExpr vy = ctx.mkRealConst(ctx.mkSymbol("vy"));
        RealExpr vz = ctx.mkRealConst(ctx.mkSymbol("vz"));

        //Pour chaque pierre, on ajoute les équations
        for (int i = 0; i < 3; i++) {
            Hailstone stone = stones.get(i);
            Expr t = ctx.mkRealConst(ctx.mkSymbol("t" + i));

            solver.add(ctx.mkEq(
                    ctx.mkAdd(x, ctx.mkMul(t, vx)),
                    ctx.mkAdd(ctx.mkInt(stone.x), ctx.mkMul(t, ctx.mkInt(stone.xvitesse)))
            ));
            solver.add(ctx.mkEq(
                    ctx.mkAdd(y, ctx.mkMul(t, vy)),
                    ctx.mkAdd(ctx.mkInt(stone.y), ctx.mkMul(t, ctx.mkInt(stone.yvitesse)))
            ));
            solver.add(ctx.mkEq(
                    ctx.mkAdd(z, ctx.mkMul(t, vz)),
                    ctx.mkAdd(ctx.mkInt(stone.z), ctx.mkMul(t, ctx.mkInt(stone.zvitesse)))
            ));
        }

        //On résoud
        solver.check();

        long valeurX = recupereLaValeur(solver, x);
        long valeurY = recupereLaValeur(solver, y);
        long valeurZ = recupereLaValeur(solver, z);

        return valeurX + valeurY + valeurZ;
    }

    private static long recupereLaValeur(Solver solver, RealExpr inconnue) {
        long valeurX;
        Expr v = solver.getModel().getConstInterp(inconnue);
        if (v instanceof IntNum vi) {
            valeurX = vi.getInt64();
        } else {
            RatNum rn = (RatNum) v;
            valeurX = Math.round(Double.parseDouble(rn.toString()));
        }
        return valeurX;
    }

    public static class Hailstone {
        long x, y, z;
        int xvitesse, yvitesse, zvitesse;

        public Hailstone(String line) {
            String[] ends = line.split("@");
            x = Long.parseLong(ends[0].split(",")[0].trim());
            y = Long.parseLong(ends[0].split(",")[1].trim());
            z = Long.parseLong(ends[0].split(",")[2].trim());
            xvitesse = Integer.parseInt(ends[1].split(",")[0].trim());
            yvitesse = Integer.parseInt(ends[1].split(",")[1].trim());
            zvitesse = Integer.parseInt(ends[1].split(",")[2].trim());
        }
    }
}
