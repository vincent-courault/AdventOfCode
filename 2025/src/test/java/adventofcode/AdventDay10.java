package adventofcode;

import org.junit.jupiter.api.Test;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay10 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(7, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(390, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(33, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(14677, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {
        int resultat = 0;

        for (String input : inputs) {
            List<String> machine = Arrays.stream(input.split(" ")).toList();
            List<int[]> actions = parseActions(machine.subList(1, machine.size() - 1));
            if (etape1) {
                int[] target = parseState(machine.getFirst());
                resultat += trouveLesActionsDonnantLEtatSouhaite(actions, target).size();
            } else {
                int[] target = parseJoltage(machine.getLast());
                resultat += resoudAvecOjalgo(actions, target);
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public int[] parseState(String s) {
        s = s.trim().substring(1, s.length() - 1).replaceAll("#", "1").replaceAll("\\.", "0");
        int[] bits = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            bits[i] = Character.getNumericValue(s.charAt(i));
        }
        return bits;
    }

    private boolean contains(int[] arr, int x) {
        for (int v : arr) if (v == x) return true;
        return false;
    }

    private List<int[]> parseActions(List<String> strings) {
        List<int[]> actions = new ArrayList<>();

        for (String s : strings) {
            String[] parts = s.trim().substring(1, s.length() - 1).split(",");
            int[] arr = Arrays.stream(parts).mapToInt(part -> Integer.parseInt(part.trim())).toArray();
            actions.add(arr);
        }

        return actions;
    }

    public int[] parseJoltage(String s) {
        String[] split = s.trim().substring(1, s.length() - 1).split(",");
        int[] joltages = new int[s.length()];
        for (int i = 0; i < split.length; i++) {
            joltages[i] = Integer.parseInt(split[i]);
        }
        return joltages;
    }

    // Convertit une action donnée comme tableau d'indices en bitmask
    private int actionToMask(int[] action) {
        int mask = 0;
        for (int i : action) {
            mask ^= (1 << i);
        }
        return mask;
    }

    // Retourne indices des actions de taille minimale qui produisent target
    // actionsIndices : liste d'actions, chaque action est int[] d'indices à toggler
    // on utilise des bitmasks et du xor
    public List<Integer> trouveLesActionsDonnantLEtatSouhaite(List<int[]> actionsIndices, int[] targetBits) {
        int nbBitsCible = targetBits.length;
        int nbActions = actionsIndices.size();

        // Construire masques d'actions
        int[] actMask = new int[nbActions];
        for (int i = 0; i < nbActions; i++) {
            actMask[i] = actionToMask(actionsIndices.get(i));
        }

        // Masque target
        int target = 0;
        for (int i = 0; i < targetBits.length; i++) {
            if (targetBits[i] != 0) {
                target |= (1 << i);
            }
        }

        // BFS sur les états (bitmask), on mémorise le parent: quel état précédent et quelle action l'a mené
        int maxState = 1 << nbBitsCible; //2^nbBitsCible
        boolean[] seen = new boolean[maxState];
        int[] parentState = new int[maxState];
        int[] parentAction = new int[maxState];

        Arrays.fill(parentState, -1);
        Arrays.fill(parentAction, -1);

        Queue<Integer> q = new ArrayDeque<>();
        int start = 0;
        q.add(start);
        seen[start] = true;

        while (!q.isEmpty()) {
            int cur = q.poll();
            if (cur == target) break;

            for (int i = 0; i < nbActions; i++) {
                int nxt = cur ^ actMask[i]; //on applique l'action sur la situation courante
                // pour créer la nouvelle situation et l'ajouter dans la pile si on n'est pas déjà passé
                if (!seen[nxt]) {
                    seen[nxt] = true;
                    parentState[nxt] = cur;
                    parentAction[nxt] = i;
                    q.add(nxt);
                }
            }
        }

        if (!seen[target]) {
            return Collections.emptyList(); // impossible d'atteindre la cible
        }

        // reconstruire chemin d'actions
        List<Integer> path = new ArrayList<>();
        int s = target;
        while (s != start) {
            int actIdx = parentAction[s];
            path.add(actIdx);
            s = parentState[s];
        }
        Collections.reverse(path);
        return path;
    }

    int resoudAvecOjalgo(List<int[]> actions, int[] target) {
        int longueurValeurCible = target.length;
        int nombreActions = actions.size();

        ExpressionsBasedModel model = new ExpressionsBasedModel();

        int max = Arrays.stream(target).max().orElseThrow();
        //on définit les variables
        Variable[] x = new Variable[nombreActions];
        for (int i = 0; i < nombreActions; i++) {
            x[i] = model.addVariable("x" + i)
                    .lower(0)
                    .upper(max)
                    .integer(true);
        }

        // on définit les contraintes à partir des valeurs attendues
        for (int j = 0; j < longueurValeurCible; j++) {
            Expression expr = model.addExpression("pos_" + j).level(target[j]);
            for (int i = 0; i < nombreActions; i++) {
                if (contains(actions.get(i), j)) {
                    expr.set(x[i], 1);
                }
            }
        }

        // on rajoute la contrainte sur la minimisation de la somme des inconnues
        Expression cost = model.addExpression("cost").weight(1.0);
        for (int i = 0; i < nombreActions; i++) {
            cost.set(x[i], 1.0);
        }

        //On fait le calcul
        Optimisation.Result result = model.minimise();

        if (!result.getState().isFeasible()) {
            System.out.println("Aucune solution réalisable.");
            throw new RuntimeException();
        }
        //on retourne la somme des inconnues trouvées
        return IntStream.range(0, nombreActions).map(i -> Math.toIntExact(Math.round(result.get(i).doubleValue()))).sum();
    }
}
