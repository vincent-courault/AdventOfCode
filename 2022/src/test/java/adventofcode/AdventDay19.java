package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AdventDay19 extends Commun {

    @Rule
    public TestName name = new TestName();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(33, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1613, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3472, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(46816, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {
        List<BluePrint> blueprints = initialiseBluePrints(inputs);
        int resultat = etape1 ? 0 : 1;
        for (int i = 0; i < (etape1 ? blueprints.size() : Math.min(3, blueprints.size())); i++) {
            BluePrint.maxGeode = 0;
            if (etape1) {
                resultat += (i + 1) * blueprints.get(i).result(24, new int[]{1, 0, 0, 0}, new int[4]);

            } else {
                resultat *= blueprints.get(i).result(32, new int[]{1, 0, 0, 0}, new int[4]);
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }

    private List<BluePrint> initialiseBluePrints(List<String> inputs) {
        List<BluePrint> blueprints = new ArrayList<>();
        for (String input : inputs) {
            String[] line = input.split(" ");
            int oreRobotCostInOre = Integer.parseInt(line[6]);
            int clayRobotCostInOre = Integer.parseInt(line[12]);
            int obsidianRobotCostInOre = Integer.parseInt(line[18]);
            int geodeRobotCostInOre = Integer.parseInt(line[27]);
            int obsidianRobotCostInClay = Integer.parseInt(line[21]);
            int geodeRobotCostInObsidian = Integer.parseInt(line[30]);
            blueprints.add(new BluePrint(oreRobotCostInOre, clayRobotCostInOre, obsidianRobotCostInOre, geodeRobotCostInOre, obsidianRobotCostInClay, geodeRobotCostInObsidian));
        }
        return blueprints;
    }


    static class BluePrint {
        public static final int ORE = 0;
        public static final int CLAY = 1;
        public static final int OBSIDIAN = 2;
        public static final int GEODE = 3;
        public static final int NOMBRE_MATIERES = 4;
        static int maxGeode = 0;
        int nombreAppels = 0;
        int[] oreRobotCost = new int[4];
        int[] clayRobotCost = new int[4];
        int[] obsidianRobotCost = new int[4];
        int[] geodeRobotCost = new int[4];
        Map<String, Integer> cache = new HashMap<>();
        int maxOreCost;

        public BluePrint(int oreRobotCostInOre, int clayRobotCostInOre, int obsidianRobotCostInOre, int geodeRobotCostInOre, int obsidianRobotCostInClay, int geodeRobotCostInObsidian) {
            oreRobotCost[0] = oreRobotCostInOre;
            clayRobotCost[0] = clayRobotCostInOre;
            obsidianRobotCost[0] = obsidianRobotCostInOre;
            geodeRobotCost[0] = geodeRobotCostInOre;
            obsidianRobotCost[1] = obsidianRobotCostInClay;
            geodeRobotCost[2] = geodeRobotCostInObsidian;
            maxOreCost = Math.max(Math.max(oreRobotCost[0], clayRobotCost[0]), Math.max(obsidianRobotCost[0], geodeRobotCost[0]));
        }

        public int result(int minutes, int[] robots, int[] ressources) {
            String hash = minutes + " " + robots[0] + " " + robots[1] + " " + robots[2] + " " + robots[3] + " " + ressources[0] + " " + ressources[1] + " " + ressources[2] + " " + ressources[3] + " ";
            nombreAppels++;
            if (cache.containsKey(hash)) {
                return cache.get(hash);
            }
            int resultat = 0;
            if (minutes == 0) {
                if (ressources[GEODE] > maxGeode) {
                    maxGeode = ressources[GEODE];
                }
                return ressources[3];
            }
            //si en créant un nouveau robot géode par minute on ne dépasse pas le max déjà connu, pas la peine d'aller plus loin
            if (maxGeode > ressources[GEODE] + (minutes * (robots[GEODE] + minutes))) {
                return -1;
            }
            List<int[]> possibleRobots = new ArrayList<>();
            List<int[]> possibleRessources = new ArrayList<>();

            if (constructionPossible(ressources, geodeRobotCost)) {
                int[] ressourcesMisesAJour = consommeLesRessources(ressources, geodeRobotCost);
                possibleRessources.add(ressourcesMisesAJour);
                possibleRobots.add(new int[]{robots[ORE], robots[CLAY], robots[OBSIDIAN], robots[GEODE] + 1});
            } else {
                if (constructionPossible(ressources, obsidianRobotCost)) {
                    //on ne construit pas le robot si on a déjà plus de ressources que ce qu'on pourrait consommer
                    if (!(ressources[OBSIDIAN] > minutes * (geodeRobotCost[OBSIDIAN] - robots[OBSIDIAN]))) {
                        int[] ressourcesMisesAJour = consommeLesRessources(ressources, obsidianRobotCost);
                        possibleRessources.add(ressourcesMisesAJour);
                        possibleRobots.add(new int[]{robots[ORE], robots[CLAY], robots[OBSIDIAN] + 1, robots[GEODE]});
                    }
                }
                if (constructionPossible(ressources, clayRobotCost)) {
                    //on ne construit pas le robot si on a déjà plus de ressources que ce qu'on pourrait consommer
                    if (!(ressources[CLAY] > minutes * (obsidianRobotCost[CLAY] - robots[CLAY]))) {
                        int[] ressourcesMisesAJour = consommeLesRessources(ressources, clayRobotCost);
                        possibleRessources.add(ressourcesMisesAJour);
                        possibleRobots.add(new int[]{robots[ORE], robots[CLAY] + 1, robots[OBSIDIAN], robots[GEODE]});
                    }
                }
                if (constructionPossible(ressources, oreRobotCost)) {
                    if (!(ressources[ORE] > minutes * (maxOreCost - robots[ORE]))) {
                        int[] ressourcesMisesAJour = consommeLesRessources(ressources, oreRobotCost);
                        possibleRessources.add(ressourcesMisesAJour);
                        possibleRobots.add(new int[]{robots[ORE] + 1, robots[CLAY], robots[OBSIDIAN], robots[GEODE]});
                    }
                }
                possibleRessources.add(ressources);
                possibleRobots.add(robots);
            }

            for (int i = 0; i < possibleRobots.size(); i++) {
                int[] ressourcesMisesAJour = new int[NOMBRE_MATIERES];
                for (int j = 0; j < NOMBRE_MATIERES; j++) {
                    ressourcesMisesAJour[j] += robots[j] + possibleRessources.get(i)[j];
                }
                resultat = Math.max(resultat, result(minutes - 1, possibleRobots.get(i), ressourcesMisesAJour));
            }
            cache.put(hash, resultat);
            return resultat;
        }

        public int[] consommeLesRessources(int[] ressourcesActuelles, int[] coutRobot) {
            int[] ressourcesMisesAJour = new int[NOMBRE_MATIERES];
            for (int i = 0; i < NOMBRE_MATIERES; i++) {
                ressourcesMisesAJour[i] = ressourcesActuelles[i] - coutRobot[i];
            }
            return ressourcesMisesAJour;
        }

        public boolean constructionPossible(int[] ressourcesActuelles, int[] coutRobot) {
            for (int i = 0; i < 4; i++) {
                if (ressourcesActuelles[i] < coutRobot[i]) {
                    return false;
                }
            }
            return true;
        }
    }
}
