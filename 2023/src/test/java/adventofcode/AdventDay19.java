package adventofcode;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class AdventDay19 extends Commun {

    Map<String, Workflow> workflows;

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(19114, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(331208, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(167409079868000L, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(121464316215623L, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean etape1) {

        boolean isWorkflows = true;
        workflows = new HashMap<>();
        List<Part> parts = new ArrayList<>();
        for (String line : inputs) {
            if (line.isEmpty()) {
                isWorkflows = false;
                continue;
            }
            if (isWorkflows) {
                Workflow wf = new Workflow(line);
                workflows.put(wf.nom, wf);
            } else {
                Part part = new Part(line);
                parts.add(part);
            }
        }
        long resultat = 0;
        if (etape1) {
            String wfName = "in";
            for (Part part : parts) {
                boolean fini = false;
                Workflow workflow = workflows.get(wfName);
                while (!fini) {
                    String next = workflow.applique(part);
                    if (next.equals("A")) {
                        fini = true;
                        resultat += part.x;
                        resultat += part.m;
                        resultat += part.a;
                        resultat += part.s;
                    } else if (next.equals("R")) {
                        fini = true;
                    } else {
                        workflow = workflows.get(next);
                    }
                }
            }
        } else {
            HashMap<String, HashSet<Integer>> valeursPossibles = new HashMap<>();
            valeursPossibles.put("x", new HashSet<>());
            valeursPossibles.put("m", new HashSet<>());
            valeursPossibles.put("a", new HashSet<>());
            valeursPossibles.put("s", new HashSet<>());

            IntStream.rangeClosed(1, 4000).forEach(i -> {
                valeursPossibles.get("x").add(i);
                valeursPossibles.get("m").add(i);
                valeursPossibles.get("a").add(i);
                valeursPossibles.get("s").add(i);
            });

            resultat = executeLEtapeDuWorkflow(valeursPossibles, "in", 0);


        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public long executeLEtapeDuWorkflow(HashMap<String, HashSet<Integer>> valeursPossibles, String workflow, int numeroEtape) {

        long resultat;
        if (workflow.equals("R"))
            return 0L;

        if (workflow.equals("A")) {
            resultat = 1L;
            resultat *= valeursPossibles.get("x").size();
            resultat *= valeursPossibles.get("m").size();
            resultat *= valeursPossibles.get("a").size();
            resultat *= valeursPossibles.get("s").size();
            return resultat;
        }

        EtapeWorkflow etape = workflows.get(workflow).etapes.get(numeroEtape);

        if (etape.operande == null) {
            return executeLEtapeDuWorkflow(valeursPossibles, etape.resultat, 0);
        }
        // On démarre les deux possibilités condition remplie ou non
        HashMap<String, HashSet<Integer>> mapConditionRemplie;
        HashSet<Integer> valeursPossiblesConditionRemplie;
        HashMap<String, HashSet<Integer>> mapConditionNonRemplie;
        HashSet<Integer> valeursPossiblesConditionNonRemplie;

        mapConditionRemplie = new HashMap<>(valeursPossibles);
        valeursPossiblesConditionRemplie = new HashSet<>(mapConditionRemplie.get(etape.parametreConditionne));
        etape.appliqueLaCondition(valeursPossiblesConditionRemplie);
        mapConditionRemplie.put(etape.parametreConditionne, valeursPossiblesConditionRemplie);
        // On démarre le worflow donné par la condition
        resultat = executeLEtapeDuWorkflow(mapConditionRemplie, etape.resultat, 0);

        mapConditionNonRemplie = new HashMap<>(valeursPossibles);
        valeursPossiblesConditionNonRemplie = new HashSet<>(mapConditionNonRemplie.get(etape.parametreConditionne));
        valeursPossiblesConditionNonRemplie.removeAll(valeursPossiblesConditionRemplie);
        mapConditionNonRemplie.put(etape.parametreConditionne, valeursPossiblesConditionNonRemplie);
        //On démarre l'étape suivante du workflow
        resultat += executeLEtapeDuWorkflow(mapConditionNonRemplie, workflow, numeroEtape + 1);

        return resultat;
    }

    public static class Workflow {
        private final String nom;
        ArrayList<EtapeWorkflow> etapes = new ArrayList<>();

        public Workflow(String line) {
            this.nom = line.split("\\{")[0];
            String[] rules = line.split("\\{")[1].split("}")[0].split(",");
            for (String rule : rules) {
                etapes.add(new EtapeWorkflow(rule));
            }
        }

        public String applique(Part part) {
            for (EtapeWorkflow etape : etapes) {
                if ("<".equals(etape.operande)) {
                    if (etape.parametreConditionne.equals("x") && (part.x < etape.valeurCondition)
                            || etape.parametreConditionne.equals("m") && (part.m < etape.valeurCondition)
                            || etape.parametreConditionne.equals("a") && (part.a < etape.valeurCondition)
                            || etape.parametreConditionne.equals("s") && (part.s < etape.valeurCondition)) {
                        return etape.resultat;
                    }
                } else if (">".equals(etape.operande)) {
                    if (etape.parametreConditionne.equals("x") && (part.x > etape.valeurCondition)
                            || etape.parametreConditionne.equals("s") && (part.s > etape.valeurCondition)
                            || etape.parametreConditionne.equals("a") && (part.a > etape.valeurCondition)
                            || etape.parametreConditionne.equals("m") && (part.m > etape.valeurCondition)) {
                        return etape.resultat;
                    }
                } else {
                    return etape.resultat;
                }
            }
            return null;
        }
    }

    public static class EtapeWorkflow {
        String parametreConditionne;
        String operande;
        long valeurCondition;
        String resultat;

        public EtapeWorkflow(String str) {
            if (str.contains(":")) {
                parametreConditionne = str.substring(0, 1);
                operande = str.substring(1, 2);
                valeurCondition = Long.parseLong(str.split(":")[0].substring(2));
                resultat = str.split(":")[1];
            } else {
                resultat = str.trim();
            }
        }

        public void appliqueLaCondition(HashSet<Integer> valeurs) {
            if (operande.equals(">")) {
                IntStream.iterate(1, i -> i <= valeurCondition, i -> i + 1).forEachOrdered(valeurs::remove);
            } else {
                IntStream.rangeClosed((int) valeurCondition, 4000).forEachOrdered(valeurs::remove);
            }
        }
    }

    public static class Part {
        int x, m, a, s;

        public Part(String line) {
            String[] properties = line.split("\\{")[1].split("}")[0].split(",");
            for (int i = 0; i < properties.length; i++) {
                String prop = properties[i];
                switch (i) {
                    case 0 -> x = Integer.parseInt(prop.split("=")[1]);
                    case 1 -> m = Integer.parseInt(prop.split("=")[1]);
                    case 2 -> a = Integer.parseInt(prop.split("=")[1]);
                    case 3 -> s = Integer.parseInt(prop.split("=")[1]);
                }
            }
        }
    }
}
