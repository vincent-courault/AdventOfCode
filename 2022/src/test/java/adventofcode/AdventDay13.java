package adventofcode;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class AdventDay13 {

    public static final char SEPARATEUR_PAQUET = ',';
    public static final char DEBUT_PAQUET = '[';
    public static final char FIN_PAQUET = ']';
    @Rule
    public TestName name = new TestName();

    private List<String> initTest() throws IOException, URISyntaxException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day13_exemple.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    private List<String> lectureDuFichier() throws URISyntaxException, IOException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day13.txt")).toURI();
        return Files.readAllLines(Paths.get(path));
    }

    @Test
    public void etape1_exemple() throws IOException, URISyntaxException {
        List<String> paquets = initTest();
        assertEquals(13, traitement(paquets, 1));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> paquets = lectureDuFichier();
        assertEquals(6415, traitement(paquets, 1));
    }

    @Test
    public void etape2_exemple() throws IOException, URISyntaxException {
        List<String> paquets = initTest();
        assertEquals(140, traitement(paquets, 2));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> paquets = lectureDuFichier();
        assertEquals(20056, traitement(paquets, 2));
    }

    public int traitement(@NotNull List<String> entrees, int etape) {

        int reponse = 0;
        int numeroDeLaPaire = 1;
        List<Paquet> paquets = new ArrayList<>();
        for (int index = 0; index < entrees.size(); index = index + 3) {
            Paquet paquet1 = new Paquet(entrees.get(index));
            Paquet paquet2 = new Paquet(entrees.get(index + 1));
            if (etape == 2) {
                paquets.add(paquet1);
                paquets.add(paquet2);
            }else {
                reponse += estDansLeBonOrdre(paquet1, paquet2) ? numeroDeLaPaire : 0;
            }
            numeroDeLaPaire++;
        }
        if (etape == 2) {
            reponse = 1;
            paquets.add(new Paquet("[[2]]"));
            paquets.add(new Paquet("[[6]]"));
            Collections.sort(paquets);
            for (int indice = 1; indice <= paquets.size(); indice++) {
                if (paquets.get(indice - 1).str.equals("[[2]]") || paquets.get(indice - 1).str.equals("[[6]]")) {
                    reponse *= indice;
                }
            }
        }

        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + reponse);
        return reponse;
    }

    private boolean estDansLeBonOrdre(Paquet paquet1, Paquet paquet2) {
        return paquet1.compareTo(paquet2) < 0;
    }

    static class Paquet implements Comparable<Paquet> {
        List<Paquet> enfants;
        int valeurNumerique;
        boolean estUnNombre;
        String str;

        public Paquet(String paquet) {
            str = paquet;
            enfants = new ArrayList<>();
            if (paquet.startsWith("[")) {
                estUnNombre = false;
                traitementDesPaquetsEnfants(paquet);
            } else {
                estUnNombre = true;
                valeurNumerique = Integer.parseInt(paquet);
            }
        }

        private void traitementDesPaquetsEnfants(String paquet) {
            paquet = paquet.substring(1, paquet.length() - 1);
            int niveau = 0;
            StringBuilder paquetTemporaire = new StringBuilder();
            for (char caractere : paquet.toCharArray()) {
                if (caractere == SEPARATEUR_PAQUET && niveau == 0) {
                    enfants.add(new Paquet(paquetTemporaire.toString()));
                    paquetTemporaire = new StringBuilder();
                } else {
                    paquetTemporaire.append(caractere);
                    if (caractere == DEBUT_PAQUET) {
                        niveau += 1;
                    }
                    if (caractere == FIN_PAQUET) {
                        niveau -= 1;
                    }
                }
            }
            if (!paquetTemporaire.toString().equals("")) {
                enfants.add(new Paquet(paquetTemporaire.toString()));
            }
        }

        @Override
        public int compareTo(@NotNull Paquet other) {
            if (estUnNombre && other.estUnNombre) {
                return valeurNumerique - other.valeurNumerique;
            }
            if (!estUnNombre && !other.estUnNombre) {
                return comparaisonDeTableaux(other);
            }
            return comparaisonCasMixte(other);
        }

        private int comparaisonDeTableaux(Paquet other) {
            for (int i = 0; i < Math.min(enfants.size(), other.enfants.size()); i++) {
                int val = enfants.get(i).compareTo(other.enfants.get(i));
                if (val != 0) {
                    return val;
                }
            }
            return enfants.size() - other.enfants.size();
        }

        private int comparaisonCasMixte(Paquet other) {
            Paquet paquet1 = estUnNombre ? new Paquet("[" + valeurNumerique + "]") : this;
            Paquet paquet2 = other.estUnNombre ? new Paquet("[" + other.valeurNumerique + "]") : other;
            return paquet1.compareTo(paquet2);
        }
    }

}
