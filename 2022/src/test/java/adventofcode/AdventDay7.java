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

public class AdventDay7 {

    public static final long TAILLE_NECESSAIRE = 30000000L;
    public static final long TAILLE_TOTALE = 70000000L;
    @Rule
    public TestName name = new TestName();

    private List<String> initTest() {
        return List.of("$ cd /", "$ ls", "dir a", "14848514 b.txt", "8504156 c.dat", "dir d", "$ cd a", "$ ls", "dir e", "29116 f", "2557 g", "62596 h.lst", "$ cd e", "$ ls", "584 i", "$ cd ..", "$ cd ..", "$ cd d", "$ ls", "4060174 j", "8033020 d.log", "5626152 d.ext", "7214296 k");
    }

    private List<String> lectureDuFichier() throws URISyntaxException, IOException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day7.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    @Test
    public void etape1_exemple() {
        List<String> inputs = initTest();
        assertEquals(95437L, etape1_main(inputs));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier();
        assertEquals(1749646L, etape1_main(inputs));
    }

    public long etape1_main(List<String> entrees) {
        String repertoireParent;
        String repertoireCourant = "";
        Map<String, String> lienParentEnfantDesRepertoires = new HashMap<>();
        Map<String, List<String>> contenusParRepertoire = new HashMap<>();
        Map<Integer, List<String>> listeDesRepertoiresParNiveau = new HashMap<>();

        Integer niveau = 0;
        for (String ligne : entrees) {
            if (estUneCommande(ligne)) {
                if (estUneDescenteDansUnRepertoire(ligne)) {
                    repertoireParent = repertoireCourant;
                    repertoireCourant = determinerLeRepertoireCourant(lienParentEnfantDesRepertoires, ligne);
                    enregistrerLarborescence(repertoireParent, repertoireCourant, lienParentEnfantDesRepertoires);
                    initialiserLeContenuDuRepertoire(repertoireCourant, contenusParRepertoire);
                    enregistrerLeNiveauDuRepertoire(repertoireCourant, listeDesRepertoiresParNiveau, niveau);
                    niveau++;
                }
                if (estUneRemonteeeAuRepertoireParent(ligne)) {
                    repertoireCourant = lienParentEnfantDesRepertoires.get(repertoireCourant);
                    niveau--;
                }
            } else {
                contenusParRepertoire.get(repertoireCourant).add(ligne);
            }
        }

        Map<String, Long> tailleDesRepertoires = calculerLaTailleDesRepertoiresSansLesSousRepertoires(contenusParRepertoire);

        ajouterLaTailleDesSousRepertoiresALaTailleDuRepertoire(lienParentEnfantDesRepertoires, listeDesRepertoiresParNiveau, tailleDesRepertoires);

        return calculerLaSommeDeLaTailleDesRepertoiresInferieureAuSeuil(tailleDesRepertoires);
    }

    @Test
    public void etape2_exemple() {
        List<String> assignments = initTest();
        assertEquals(24933642L, etape2_main(assignments));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> assignments = lectureDuFichier();
        assertEquals(1498966L, etape2_main(assignments));
    }


    public long etape2_main(List<String> entrees) {
        String repertoireParent;
        String repertoireCourant = "";
        Map<String, String> lienParentEnfantDesRepertoires = new HashMap<>();
        Map<String, List<String>> contenusParRepertoire = new HashMap<>();
        Map<Integer, List<String>> listeDesRepertoiresParNiveau = new HashMap<>();

        Integer niveau = 0;
        for (String ligne : entrees) {
            if (estUneCommande(ligne)) {
                if (estUneDescenteDansUnRepertoire(ligne)) {
                    repertoireParent = repertoireCourant;
                    repertoireCourant = determinerLeRepertoireCourant(lienParentEnfantDesRepertoires, ligne);
                    enregistrerLarborescence(repertoireParent, repertoireCourant, lienParentEnfantDesRepertoires);
                    initialiserLeContenuDuRepertoire(repertoireCourant, contenusParRepertoire);
                    enregistrerLeNiveauDuRepertoire(repertoireCourant, listeDesRepertoiresParNiveau, niveau);
                    niveau++;
                }
                if (estUneRemonteeeAuRepertoireParent(ligne)) {
                    repertoireCourant = lienParentEnfantDesRepertoires.get(repertoireCourant);
                    niveau--;
                }
            } else {
                contenusParRepertoire.get(repertoireCourant).add(ligne);
            }
        }

        Map<String, Long> tailleDesRepertoires = calculerLaTailleDesRepertoiresSansLesSousRepertoires(contenusParRepertoire);
        ajouterLaTailleDesSousRepertoiresALaTailleDuRepertoire(lienParentEnfantDesRepertoires, listeDesRepertoiresParNiveau, tailleDesRepertoires);

        Long tailleNecessaire = calculerLaTailleNecessaire(tailleDesRepertoires);
        List<Long> taillesPossiblesDesRepertoires = determinerLesTaillesDesRepertoiresPotentiels(tailleDesRepertoires, tailleNecessaire);
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + Collections.min(taillesPossiblesDesRepertoires));
        return Collections.min(taillesPossiblesDesRepertoires);

    }


    private void enregistrerLarborescence(String repertoireParent, String repertoireCourant, Map<String, String> lienParentEnfantDesRepertoires) {
        lienParentEnfantDesRepertoires.put(repertoireCourant, repertoireParent);
    }

    private String determinerLeRepertoireCourant(Map<String, String> lienParentEnfantDesRepertoires, String ligne) {
        String repertoireCourant;
        repertoireCourant = ligne.split(" ")[2];
        repertoireCourant = gererLesDoublonsDeRepertoire(repertoireCourant, lienParentEnfantDesRepertoires);
        return repertoireCourant;
    }

    private void initialiserLeContenuDuRepertoire(String repertoireCourant, Map<String, List<String>> contenusParRepertoire) {
        contenusParRepertoire.put(repertoireCourant, new ArrayList<>());
    }

    private void enregistrerLeNiveauDuRepertoire(String repertoireCourant, Map<Integer, List<String>> listeDesRepertoiresParNiveau, Integer niveau) {
        listeDesRepertoiresParNiveau.putIfAbsent(niveau, new ArrayList<>());
        listeDesRepertoiresParNiveau.get(niveau).add(repertoireCourant);
    }

    private String gererLesDoublonsDeRepertoire(String repertoireCourant, Map<String, String> lienParentEnfantDesRepertoires) {
        //Pour eviter les problèmes des sous repertoires ayant le même nom
        while (lienParentEnfantDesRepertoires.containsKey(repertoireCourant)) {
            repertoireCourant = repertoireCourant + repertoireCourant;
        }
        return repertoireCourant;
    }

    private Long calculerLaSommeDeLaTailleDesRepertoiresInferieureAuSeuil(Map<String, Long> tailleDesRepertoires) {
        Long tailleRetournee = 0L;
        for (String nomRepertoire : tailleDesRepertoires.keySet()) {
            if (tailleDesRepertoires.get(nomRepertoire) < 100000L) {
                tailleRetournee += tailleDesRepertoires.get(nomRepertoire);
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + tailleRetournee);
        return tailleRetournee;
    }

    private void ajouterLaTailleDesSousRepertoiresALaTailleDuRepertoire(Map<String, String> lienParentEnfantDesRepertoires, Map<Integer, List<String>> listeDesRepertoiresParNiveau, Map<String, Long> tailleDesRepertoires) {
        for (int niveauRep = Collections.max(listeDesRepertoiresParNiveau.keySet()); niveauRep >= 0; niveauRep--) {
            for (String repertoires2 : listeDesRepertoiresParNiveau.get(niveauRep)) {
                if (!repertoires2.equals("/")) {
                    String parent = lienParentEnfantDesRepertoires.get(repertoires2);
                    tailleDesRepertoires.put(parent, tailleDesRepertoires.get(repertoires2) + tailleDesRepertoires.get(parent));
                }
            }
        }
    }

    private Map<String, Long> calculerLaTailleDesRepertoiresSansLesSousRepertoires(Map<String, List<String>> contenusParRepertoire) {
        Map<String, Long> tailleDesRepertoires = new HashMap<>();
        for (String rep : contenusParRepertoire.keySet()) {
            long taille = 0L;
            List<String> contenu = contenusParRepertoire.get(rep);
            for (String element : contenu) {
                if (!element.startsWith("dir")) {
                    taille += Long.parseLong(element.split(" ")[0]);
                }
            }
            tailleDesRepertoires.put(rep, taille);
        }
        return tailleDesRepertoires;
    }

    private boolean estUneRemonteeeAuRepertoireParent(String ligne) {
        return ligne.equals("$ cd ..");
    }

    private boolean estUneDescenteDansUnRepertoire(String ligne) {
        return ligne.contains("cd ") && !estUneRemonteeeAuRepertoireParent(ligne);
    }

    private boolean estUneCommande(String ligne) {
        return ligne.startsWith("$");
    }

    private Long calculerLaTailleNecessaire(Map<String, Long> tailleDesRepertoires) {
        return TAILLE_NECESSAIRE - (TAILLE_TOTALE - determinerLEspaceOccupe(tailleDesRepertoires));
    }

    private List<Long> determinerLesTaillesDesRepertoiresPotentiels(Map<String, Long> tailleDesRepertoires, Long tailleNecessaire) {
        List<Long> taillesPossiblesDesRepertoires = new ArrayList<>();
        for (String re : tailleDesRepertoires.keySet()) {
            if (tailleDesRepertoires.get(re) > tailleNecessaire) {
                taillesPossiblesDesRepertoires.add(tailleDesRepertoires.get(re));
            }
        }
        return taillesPossiblesDesRepertoires;
    }

    private Long determinerLEspaceOccupe(Map<String, Long> tailleDesRepertoires) {
        return tailleDesRepertoires.get("/");
    }

}
