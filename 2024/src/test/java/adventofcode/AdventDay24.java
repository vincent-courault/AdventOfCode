package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay24 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(2024, traitement(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(50411513338638L, traitement(inputs));
    }

    @Test
    public void etape2() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals("gfv,hcm,kfs,tqm,vwr,z06,z11,z16", traitement2(inputs));
    }

    public long traitement(List<String> inputs) {
        long resultat;

        Map<String, Integer> valeurs = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        for (String input : inputs) {
            if (input.contains(":")) {
                String[] split = input.split(":");
                valeurs.put(split[0], Integer.parseInt(split[1].trim()));
            } else if (input.contains("->")) {
                String[] split = input.split("->");
                String valeur = split[1].trim();
                String[] operation = split[0].split(" ");

                String operande1 = operation[0].trim();
                String operande2 = operation[2].trim();
                String operateur = operation[1].trim();
                Integer valeurOperande1 = valeurs.get(operande1);
                Integer valeurOperande2 = valeurs.get(operande2);
                if (valeurOperande2 == null || valeurOperande1 == null) {
                    queue.add(input);
                    continue;
                }
                int resOp = switch (operateur) {
                    case "XOR" -> valeurOperande1 ^ valeurOperande2;
                    case "OR" -> valeurOperande1 | valeurOperande2;
                    case "AND" -> valeurOperande1 & valeurOperande2;
                    default -> throw new IllegalStateException("Unexpected value: " + operateur);
                };
                valeurs.put(valeur, resOp);
            }
        }
        while (!queue.isEmpty()) {
            String input = queue.poll();
            if ((!input.startsWith("x") && !input.startsWith("y")) && input.contains("->")) {
                String[] split = input.split("->");
                String valeur = split[1].trim();
                String[] operation = split[0].split(" ");

                String operande1 = operation[0].trim();
                String operande2 = operation[2].trim();
                String operateur = operation[1].trim();
                Integer valeurOperande1 = valeurs.get(operande1);
                Integer valeurOperande2 = valeurs.get(operande2);
                if (valeurOperande2 == null || valeurOperande1 == null) {
                    queue.add(input);
                    continue;
                }
                int resOp = switch (operateur) {
                    case "XOR" -> valeurOperande1 ^ valeurOperande2;
                    case "OR" -> valeurOperande1 | valeurOperande2;
                    case "AND" -> valeurOperande1 & valeurOperande2;
                    default -> throw new IllegalStateException("Unexpected value: " + operateur);
                };
                valeurs.put(valeur, resOp);
            }
        }
        List<String> valeursResultat = valeurs.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("z"))
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .map(Map.Entry::getValue).map(Object::toString)// Extraire les valeurs
                .toList();
        resultat = Long.parseLong(String.join("", valeursResultat), 2);

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    protected String traitement2(final List<String> input) {
        final List<PorteLogique> porteLogiques = creeLesPortesLogiques(input);
        final Set<PorteLogique> portesEnErreur = trouveLesPortesEnErreur(porteLogiques);
        String resultat = portesEnErreur.stream().map(porteLogique -> porteLogique.sortie)
                .sorted().collect(Collectors.joining(","));
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private List<PorteLogique> creeLesPortesLogiques(final List<String> input) {
        final List<PorteLogique> porteLogiques = new ArrayList<>();
        input.stream().filter(s -> s.contains("->")).toList().
                stream().map(line -> line.split("->")).forEach(split -> {
                    final String sortie = split[1].trim();
                    final String porte = split[0].trim();
                    porteLogiques.add(new PorteLogique(porte, sortie));
                });
        return porteLogiques;
    }

    private Set<PorteLogique> trouveLesPortesEnErreur(final List<PorteLogique> porteLogiques) {
        final Set<PorteLogique> porteLogiquesEnErreur = new HashSet<>();
        /*
         * https://en.wikipedia.org/wiki/Adder_(electronics)#Full_adder
         *
         * z = x xor y xor Cin (Cin retenue du bit précédent)
         * Cout = (x and y) or (Cin and (x xor y))
         *
         * On va chercher les portes qui ne permettent pas de faire le calcul.
         * Il y a 4 cas de portes en erreur
         *
         * 1. Si la sortie est un z, l'opérateur doit être XOR, sinon c'est une erreur (sauf pour le cas du dernier bit).
         * 2. Si la sortie n'est pas un fil z et que les entrées ne sont pas x et y, l'opérateur doit toujours
         * être AND ou OR, sinon c'est une erreur.
         * 3. Si la porte fait un XOR entre x et y, la sortie doit servir d'entrée sur une autre porte en xor
         * (xor avec la retenue précédente), sinon c'est une erreur.
         * (Cette condition n'est pas vraie pour le premier bit x00 et y00).
         * 4. Si la porte fait un AND entre x et y, la sortie doit servir d'entrée sur une autre porte en or
         * (calcul de la retenue suivante), sinon c'est une erreur.
         * (Cette condition n'est pas vraie pour le premier bit x00 et y00)
         */
        for (final PorteLogique porte : porteLogiques) {
            boolean premierBit = porte.entree1.endsWith("00") && porte.entree2.endsWith("00");
            boolean aXEtYenEntree = (porte.entree1.startsWith("x") || porte.entree1.startsWith("y"))
                    && (porte.entree2.startsWith("x") || porte.entree2.startsWith("y"));
            if (porte.sortie.startsWith("z") && !porte.operateur.equals("XOR") && !porte.sortie.equals("z45")) {
                porteLogiquesEnErreur.add(porte);
                continue;
            }
            if (!porte.sortie.startsWith("z") && porte.operateur.equals("XOR") && !aXEtYenEntree) {
                porteLogiquesEnErreur.add(porte);
                continue;
            }
            if (aXEtYenEntree && !premierBit) {
                if (porte.operateur.equals("XOR")) {
                    if (porteLogiques.stream().filter(c2 -> !c2.equals(porte))
                            .noneMatch(c2 -> (c2.entree1.equals(porte.sortie) || c2.entree2.equals(porte.sortie))
                                    && c2.operateur.equals("XOR"))) {
                        porteLogiquesEnErreur.add(porte);
                    }
                } else if (porte.operateur.equals("AND")) {
                    if (porteLogiques.stream().filter(c2 -> !c2.equals(porte))
                            .noneMatch(c2 -> (c2.entree1.equals(porte.sortie) || c2.entree2.equals(porte.sortie))
                                    && c2.operateur.equals("OR"))) {
                        porteLogiquesEnErreur.add(porte);
                    }
                }
            }
        }
        return porteLogiquesEnErreur;
    }

    public static class PorteLogique {

        public final String sortie;
        public final String entree1;
        public final String entree2;
        public final String operateur;

        public PorteLogique(final String porte, final String sortie) {
            this.sortie = sortie;
            entree1 = porte.split(" ")[0];
            operateur = porte.split(" ")[1];
            entree2 = porte.split(" ")[2];
        }

        @Override
        public String toString() {
            return entree1 + " " + operateur + " " + entree2 + " -> " + sortie;
        }
    }
}
