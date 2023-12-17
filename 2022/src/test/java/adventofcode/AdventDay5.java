package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class AdventDay5 {
    public static final int POSITION_STACK_DEPART = 3;
    public static final int POSITION_STACK_ARRIVEE = 5;

    @Rule
    public TestName name = new TestName();

    private List<String> initTest() {
        return List.of("    [D]    ", "[N] [C]    ", "[Z] [M] [P]", " 1   2   3 ", "", "move 1 from 2 to 1", "move 3 from 1 to 3", "move 2 from 2 to 1", "move 1 from 1 to 2");
    }

    private List<String> lectureDuFichier() throws URISyntaxException, IOException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day5.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    @Test
    public void etape1_exemple() {
        List<String> assignments = initTest();
        assertEquals("CMZ", etape1_main(assignments));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> assignments = lectureDuFichier();
        assertEquals("FWSHSPJWM", etape1_main(assignments));
    }

    public String etape1_main(List<String> assignments) {
        List<String> chargementInitial = new ArrayList<>();
        List<String> consignes = new ArrayList<>();
        lectureInput(assignments, chargementInitial, consignes);
        int nombreStacks = determinerLeNombreDeStacks(chargementInitial);
        List<Stack<String>> stacks = initStacks(nombreStacks);

        lectureDuChargementInitial(chargementInitial, stacks);

        for (String consigne : consignes) {
            int nombre = Integer.parseInt(consigne.split(" ")[1]);
            Stack<String> stackDepart = determinationDeLaStack(consigne, POSITION_STACK_DEPART, stacks);
            Stack<String> stackArrivee = determinationDeLaStack(consigne, POSITION_STACK_ARRIVEE, stacks);
            appliquerLaConsigneEtape1(nombre, stackDepart, stackArrivee);
        }
        return preparerLeRetour(stacks);
    }


    @Test
    public void etape2_exemple() {
        List<String> assignments = initTest();
        assertEquals("MCD", etape2_main(assignments));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> assignments = lectureDuFichier();
        assertEquals("PWPWHGFZS", etape2_main(assignments));
    }

    public String etape2_main(List<String> assignments) {

        List<String> chargementInitial = new ArrayList<>();
        List<String> consignes = new ArrayList<>();

        lectureInput(assignments, chargementInitial, consignes);
        int nombreStacks = determinerLeNombreDeStacks(chargementInitial);

        List<Stack<String>> stacks = initStacks(nombreStacks);

        lectureDuChargementInitial(chargementInitial, stacks);

        for (String consigne : consignes) {
            int nombre = Integer.parseInt(consigne.split(" ")[1]);
            Stack<String> stackDepart = determinationDeLaStack(consigne, POSITION_STACK_DEPART, stacks);
            Stack<String> stackArrivee = determinationDeLaStack(consigne, POSITION_STACK_ARRIVEE, stacks);

            appliquerLaConsigneEtape2(nombre, stackDepart, stackArrivee);
        }

        return preparerLeRetour(stacks);
    }

    private void appliquerLaConsigneEtape1(int nombre, Stack<String> stackDepart, Stack<String> stackArrivee) {
        for (int i = 0; i < nombre; i++) {
            String caisse = stackDepart.pop();
            stackArrivee.add(caisse);
        }
    }

    private Stack<String> determinationDeLaStack(String consigne, int x, List<Stack<String>> stacks) {
        int nostackDepart = Integer.parseInt((consigne.split(" ")[x]));
        return stacks.get(nostackDepart - 1);
    }

    private int determinerLeNombreDeStacks(List<String> chargementInitial) {
        Collections.reverse(chargementInitial);
        String[] ligne1 = chargementInitial.get(0).split(" {2}");
        int nombreStacks = ligne1.length;
        chargementInitial.remove(0);
        return nombreStacks;
    }

    private List<Stack<String>> initStacks(int nombreStacks) {
        List<Stack<String>> stacks = new ArrayList<>();
        for (int n = 0; n < nombreStacks; n++) {
            stacks.add(new Stack<>());
        }
        return stacks;
    }

    private String preparerLeRetour(List<Stack<String>> stacks) {
        StringBuilder retour = new StringBuilder();
        for (Stack<String> stack : stacks) {
            retour.append(stack.peek());
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + retour);
        return retour.toString();
    }

    private void appliquerLaConsigneEtape2(int nombre, Stack<String> stackDepart, Stack<String> stackArrivee) {
        List<String> caisses = new ArrayList<>();
        for (int i = 0; i < nombre; i++) {
            String caisse = stackDepart.pop();
            caisses.add(caisse);
        }
        Collections.reverse(caisses);
        stackArrivee.addAll(caisses);
    }

    private void lectureDuChargementInitial(List<String> chargementInitial, List<Stack<String>> stacks) {
        for (String chargement : chargementInitial) {
            int longueur = chargement.length() / 4;
            for (int stackNumber = 0; stackNumber <= longueur; stackNumber++) {
                String valeur = chargement.substring(1 + stackNumber * 4, 2 + stackNumber * 4);
                if (!valeur.trim().isEmpty()) stacks.get(stackNumber).add(valeur);

            }
        }
    }

    private void lectureInput(List<String> assignments, List<String> chargementInitial, List<String> consignes) {
        for (String assig : assignments) {
            if (assig.startsWith("move")) {
                consignes.add(assig);
            } else if (!assig.isEmpty()) {
                chargementInitial.add(assig);
            }
        }
    }
}
