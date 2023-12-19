package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay19_2 extends Commun {

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
            HashMap<String, Integer> valeursPossibles = new HashMap<>();
            valeursPossibles.put("x_min", 1);
            valeursPossibles.put("m_min", 1);
            valeursPossibles.put("a_min", 1);
            valeursPossibles.put("s_min", 1);
            valeursPossibles.put("x_max", 4000);
            valeursPossibles.put("m_max", 4000);
            valeursPossibles.put("a_max", 4000);
            valeursPossibles.put("s_max", 4000);

            resultat = executeLEtapeDuWorkflow(valeursPossibles, "in", 0);
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public long executeLEtapeDuWorkflow(HashMap<String, Integer> valeursPossibles, String workflow, int numeroEtape) {
        long resultat;
        if (workflow.equals("R"))
            return 0L;

        if (workflow.equals("A")) {
            resultat = 1L;
            resultat *= (valeursPossibles.get("x_max") - valeursPossibles.get("x_min") + 1);
            resultat *= (valeursPossibles.get("m_max") - valeursPossibles.get("m_min") + 1);
            resultat *= (valeursPossibles.get("a_max") - valeursPossibles.get("a_min") + 1);
            resultat *= (valeursPossibles.get("s_max") - valeursPossibles.get("s_min") + 1);
            return resultat;
        }

        EtapeWorkflow etape = workflows.get(workflow).etapes.get(numeroEtape);

        if (etape.operande == null) {
            return executeLEtapeDuWorkflow(valeursPossibles, etape.resultat, 0);
        }
        // On démarre les deux possibilités condition remplie ou non
        HashMap<String, Integer> mapConditionRemplie;
        HashMap<String, Integer> mapConditionNonRemplie;

        mapConditionRemplie = new HashMap<>(valeursPossibles);
        if (etape.operande.equals(">")) {
            mapConditionRemplie.put(etape.parametreConditionne + "_min", (etape.valeurCondition + 1));
        } else {
            mapConditionRemplie.put(etape.parametreConditionne + "_max", (etape.valeurCondition - 1));
        }

        // On démarre le worflow donné par la condition
        resultat = executeLEtapeDuWorkflow(mapConditionRemplie, etape.resultat, 0);

        mapConditionNonRemplie = new HashMap<>(valeursPossibles);
        if (etape.operande.equals(">")) {
            mapConditionNonRemplie.put(etape.parametreConditionne + "_max", etape.valeurCondition);
        } else {
            mapConditionNonRemplie.put(etape.parametreConditionne + "_min", etape.valeurCondition);
        }
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
        int valeurCondition;
        String resultat;

        public EtapeWorkflow(String str) {
            if (str.contains(":")) {
                parametreConditionne = str.substring(0, 1);
                operande = str.substring(1, 2);
                valeurCondition = Integer.parseInt((str.split(":")[0].substring(2)));
                resultat = str.split(":")[1];
            } else {
                resultat = str.trim();
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
