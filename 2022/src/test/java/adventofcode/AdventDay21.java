package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class AdventDay21 extends Commun {

    @Rule
    public TestName name = new TestName();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(152, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(145167969204648L, traitement(inputs));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(301, traitementEtape2(inputs));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(3330805295850L, traitementEtape2(inputs));
    }

    public long traitement(List<String> inputs) {
        Map<String, Monkey> singes = new HashMap<>();
        for (String input : inputs) {
            String nom = input.split(": ")[0];
            String action = input.split(": ")[1];
            if (action.contains(" ")) {
                singes.put(nom, new Monkey(nom, action.split(" ")[0], action.split(" ")[1], action.split(" ")[2]));
            } else {
                singes.put(nom, new Monkey(nom, Long.parseLong(action)));
            }
        }
        List<Monkey> listeSinges = singes.values().stream().toList();

        while (singes.get("root").valeur == null) {
            for (Monkey monkey : listeSinges) {
                if (monkey.valeur == null) {
                    Long valeurSinge1 = singes.get(monkey.singe1).valeur;
                    Long valeurSinge2 = singes.get(monkey.singe2).valeur;
                    if (valeurSinge2 != null && valeurSinge1 != null) {
                        switch (monkey.operateur) {
                            case "+" -> monkey.valeur = valeurSinge1 + valeurSinge2;
                            case "*" -> monkey.valeur = valeurSinge1 * valeurSinge2;
                            case "-" -> monkey.valeur = valeurSinge1 - valeurSinge2;
                            case "/" -> monkey.valeur = valeurSinge1 / valeurSinge2;
                        }
                    }
                }
            }
        }
        Long resultat = singes.get("root").valeur;
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }

    public long traitementEtape2(List<String> inputs) {
        Map<String, Monkey> singes = new HashMap<>();
        for (String input : inputs) {
            String nom = input.split(": ")[0];
            String action = input.split(": ")[1];
            if (action.contains(" ")) {
                singes.put(nom, new Monkey(nom, action.split(" ")[0], action.split(" ")[1], action.split(" ")[2]));
            } else {
                singes.put(nom, new Monkey(nom, Long.parseLong(action)));
            }
        }
        Monkey singe1 = singes.get(singes.get("root").singe1);
        Monkey singe2 = singes.get(singes.get("root").singe2);
        String calculSinge1 = singe1.getCalcul(singes);
        String calculSinge2 = singe2.getCalcul(singes);
        Long resultat = solveEquation(calculSinge1, calculSinge2);
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }

    private Long solveEquation(String calculSinge1, String calculSinge2) {
        Long resultat = 0L;
        String membreAvecVariable = calculSinge1.contains("x") ? calculSinge1 : calculSinge2;
        long valeurNumerique = !calculSinge1.contains("x") ? Long.valueOf(calculSinge1) : Long.valueOf(calculSinge2);
        LinkedList<String> membreAParser = new LinkedList<>();
        for (String part : membreAvecVariable.split("")) {
            if (!part.isEmpty()) membreAParser.add(part);
        }
        while (membreAParser.size() > 2) {
            for (int i = 0; i < 15; i++) {
                StringBuilder premier = new StringBuilder(Objects.requireNonNull(membreAParser.pollFirst()));
                StringBuilder dernier = new StringBuilder(Objects.requireNonNull(membreAParser.pollLast()));
                if (Objects.equals(premier.toString(), "(") && Objects.equals(dernier.toString(), ")")) {
                    break;
                }
                if (Objects.equals(premier.toString(), "(")) {
                    while (!Objects.equals(membreAParser.peekLast(), ")")) {
                        dernier.insert(0, membreAParser.pollLast());
                    }
                    membreAParser.pollLast();
                    long valeurNum = Long.parseLong(dernier.substring(1));
                    String operateur = dernier.substring(0, 1);
                    valeurNumerique = switch (operateur) {
                        case "/" -> valeurNumerique * valeurNum;
                        case "*" -> valeurNumerique / valeurNum;
                        case "-" -> valeurNumerique + valeurNum;
                        case "+" -> valeurNumerique - valeurNum;
                        default -> valeurNumerique;
                    };
                    break;
                }

                if (Objects.equals(dernier.toString(), ")")) {
                    while (!Objects.equals(membreAParser.peekFirst(), "(")) {
                        premier.append(membreAParser.pollFirst());
                    }
                    membreAParser.pollFirst();
                    long valeurNum = Long.parseLong(premier.substring(0, (premier.length()) - 1));
                    String operateur = premier.substring(premier.length() - 1);
                    valeurNumerique = switch (operateur) {
                        case "/" -> valeurNum / valeurNumerique;
                        case "*" -> valeurNumerique / valeurNum;
                        case "-" -> valeurNum - valeurNumerique;
                        case "+" -> valeurNumerique - valeurNum;
                        default -> valeurNumerique;
                    };
                    break;
                }

                if (dernier.toString().equals("x") || premier.toString().equals("x")) {

                    StringBuilder equation = new StringBuilder(premier.toString());
                    while (!membreAParser.isEmpty()) {
                        equation.append(membreAParser.pollFirst());
                    }
                    equation.append(dernier);
                    if (equation.toString().startsWith("x")) {
                        String operateur = equation.substring(1, 2);
                        long valeurNum = Long.parseLong(equation.substring(2));
                        valeurNumerique = switch (operateur) {
                            case "/" -> valeurNumerique * valeurNum;
                            case "*" -> valeurNumerique / valeurNum;
                            case "-" -> valeurNumerique + valeurNum;
                            case "+" -> valeurNumerique - valeurNum;
                            default -> valeurNumerique;
                        };
                    }
                    return valeurNumerique;
                }
            }
        }
        return resultat;
    }


    static class Monkey {
        String nom;
        Long valeur;
        String singe1;
        String operateur;
        String singe2;

        public Monkey(String nom, long valeur) {
            this.nom = nom;
            this.valeur = valeur;
        }

        public Monkey(String nom, String singe1, String operateur, String singe2) {
            this.nom = nom;
            this.singe1 = singe1;
            this.singe2 = singe2;
            this.operateur = operateur;
        }

        @Override
        public String toString() {
            return "Monkey{" + "nom='" + nom + '\'' + ", valeur=" + valeur + ", singe1='" + singe1 + '\'' + ", operateur='" + operateur + '\'' + ", singe2='" + singe2 + '\'' + '}';
        }

        public String getCalcul(Map<String, Monkey> singes) {
            if (valeur != null) {
                return String.valueOf(valeur);
            } else {
                String calculSinge1 = singes.get(singe1).getCalcul(singes);
                String calculSinge2 = singes.get(singe2).getCalcul(singes);
                if (singe1.equals("humn")) {
                    return "(" + "x" + operateur + calculSinge2 + ")";
                } else if (singe2.equals("humn")) {
                    return "(" + calculSinge1 + operateur + "x" + ")";
                }
                if (!calculSinge1.contains("x") && !calculSinge2.contains("x")) {
                    Long aRetourner = 0L;
                    Long valeurSinge1 = Long.valueOf(calculSinge1);
                    Long valeurSinge2 = Long.valueOf(calculSinge2);
                    switch (operateur) {
                        case "+" -> aRetourner = valeurSinge1 + valeurSinge2;
                        case "*" -> aRetourner = valeurSinge1 * valeurSinge2;
                        case "-" -> aRetourner = valeurSinge1 - valeurSinge2;
                        case "/" -> aRetourner = valeurSinge1 / valeurSinge2;
                    }
                    return String.valueOf(aRetourner);
                }
                return "(" + calculSinge1 + operateur + calculSinge2 + ")";
            }
        }
    }

}
