package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay13 extends Commun {
    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(405, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(31265, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(400, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(39359, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {
        int resultat = 0;
        int valeurHorizontale;
        int valeurVerticale;

        List<List<String>> cartes = parseLesDonnees(inputs);

        for (List<String> carte : cartes) {
            if (etape1) {
                valeurHorizontale = recupereLaValeurAvantLaSymetrie(carte, false);
                valeurVerticale = recupereLaValeurAvantLaSymetrie(carte, true);
            } else {
                valeurHorizontale = recupereLaValeurAvantLaTacheOuSymetrie(carte,false);
                valeurVerticale = recupereLaValeurAvantLaTacheOuSymetrie(carte, true);
            }
            resultat += 100 * valeurHorizontale + valeurVerticale;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private List<List<String>> parseLesDonnees(final List<String> inputs) {
        List<List<String>> cartes = new ArrayList<>();
        List<String> carte = new ArrayList<>();
        for (String line : inputs) {
            if (line.isBlank()) {
                cartes.add(carte);
                carte = new ArrayList<>();
            } else {
                carte.add(line);
            }
        }
        cartes.add(carte);
        return cartes;
    }

    private int recupereLaValeurAvantLaSymetrie(final List<String> carte, boolean colonne) {
        int nombreLigneOuColonne = colonne ? carte.getFirst().length() : carte.size();
        for (int numeroLigneOuColonne = 1; numeroLigneOuColonne < nombreLigneOuColonne; numeroLigneOuColonne++) {
            //On récupère les colonnes
            String precedente = colonne ? recupereLaColonne(numeroLigneOuColonne - 1, carte) : carte.get(numeroLigneOuColonne - 1);
            String courante = colonne ? recupereLaColonne(numeroLigneOuColonne, carte) : carte.get(numeroLigneOuColonne);
            //On cherche les lignes ou colonnes identiques
            if (precedente.equals(courante)) {
                // Détermine Le nombre de lignes ou colonnes à vérifier
                int nombresLignesOuColonnesAVerifier = Math.min(numeroLigneOuColonne - 1, nombreLigneOuColonne - numeroLigneOuColonne - 1);
                boolean controlesOK = true;

                for (int idx = 0; idx < nombresLignesOuColonnesAVerifier; idx++) {
                    String premiere = colonne ? recupereLaColonne(numeroLigneOuColonne - idx - 2, carte) : carte.get(numeroLigneOuColonne - idx - 2);
                    String seconde = colonne ? recupereLaColonne(numeroLigneOuColonne + idx + 1, carte) : carte.get(numeroLigneOuColonne + idx + 1);
                    if (!premiere.equals(seconde)) {
                        controlesOK = false;
                        break;
                    }
                }
                if (controlesOK) {
                    return numeroLigneOuColonne;
                }
            }
        }
        return 0;
    }

    private int recupereLaValeurAvantLaTacheOuSymetrie(final List<String> carte, boolean colonne) {
        int nombreLignesOuColonnes = colonne ? carte.get(0).length() : carte.size();
        for (int numeroLigneOuColonne = 1; numeroLigneOuColonne < nombreLignesOuColonnes; numeroLigneOuColonne++) {

            String precedente = colonne ? recupereLaColonne(numeroLigneOuColonne - 1, carte) : carte.get(numeroLigneOuColonne - 1);
            String courante = colonne ? recupereLaColonne(numeroLigneOuColonne, carte) : carte.get(numeroLigneOuColonne);

            int nombreDifferences = calculeLeNombreDeDifferences(precedente, courante);
            int nombreDeLignesOuColonnesAVerifier = Math.min(numeroLigneOuColonne - 1, nombreLignesOuColonnes - numeroLigneOuColonne - 1);
            //Si on tombe sur la symétrie en premier on vérifie qu'il y a une paire avec un smudge (une valeur d'écart) à suivre
            if (nombreDifferences == 0) {
                boolean controlesOK = true;
                boolean smudgeTrouvee = false;
                for (int el = 0; el < nombreDeLignesOuColonnesAVerifier; el++) {
                    String premiere = colonne ? recupereLaColonne(numeroLigneOuColonne - el - 2, carte) : carte.get(numeroLigneOuColonne - el - 2);
                    String seconde = colonne ? recupereLaColonne(numeroLigneOuColonne + el + 1, carte) : carte.get(numeroLigneOuColonne + el + 1);
                    int nombreDeDifferences = calculeLeNombreDeDifferences(premiere, seconde);
                    if (nombreDeDifferences > 1) {
                        controlesOK = false;
                        break;
                    }
                    if (nombreDeDifferences == 1) {
                        smudgeTrouvee = true;
                    }
                }
                if (controlesOK && smudgeTrouvee) {
                    return numeroLigneOuColonne;
                }
                //Si on tombe en premier sur la paire avec le smudge, on vérifie que toutes les autres paires sont identiques
            } else if (nombreDifferences == 1) {
                boolean controlesOK = true;
                for (int idx = 0; idx < nombreDeLignesOuColonnesAVerifier; idx++) {
                    String premiere = colonne ? recupereLaColonne(numeroLigneOuColonne - idx - 2, carte) : carte.get(numeroLigneOuColonne - idx - 2);
                    String seconde = colonne ? recupereLaColonne(numeroLigneOuColonne + idx + 1, carte) : carte.get(numeroLigneOuColonne + idx + 1);
                    if (calculeLeNombreDeDifferences(premiere, seconde) >= 1) {
                        controlesOK = false;
                        break;
                    }
                }
                if (controlesOK) {
                    return numeroLigneOuColonne;
                }
            }
        }
        return 0;
    }

    private String recupereLaColonne(int x, final List<String> map) {
        return map.stream().map(line -> String.valueOf(line.charAt(x))).collect(Collectors.joining());
    }

    private int calculeLeNombreDeDifferences(String s1, String s2) {
        return Math.toIntExact(IntStream.range(0, s1.length()).filter(i -> s1.charAt(i) != s2.charAt(i)).count());
    }
}
