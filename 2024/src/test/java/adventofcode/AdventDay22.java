package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay22 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(37327623, traitement(inputs, false));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(20401393616L, traitement(inputs, false));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true, 2);
        assertEquals(23, traitement(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(2272, traitement(inputs, true));
    }

    public long traitement(List<String> inputs, boolean etape2) {
        long resultat = 0;
        List<Long> nombresSecrets = inputs.stream().mapToLong(Long::parseLong).boxed().toList();

        if (etape2) {
            resultat = calculeSequencesEtSommesCorrespondantes(nombresSecrets);
        } else {
            for (Long nombreSecret : nombresSecrets) {
                resultat += calculeNombreSecret(nombreSecret);
            }
        }
        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private long calculeNombreSecret(long nombre) {
        for (int i = 0; i < 2000; i++) {
            nombre = calculeNombreSuivant(nombre);
        }
        return nombre;
    }

    private long calculeNombreSuivant(long nombreSecret) {

        nombreSecret = (((nombreSecret << 6) ^ nombreSecret) & 0xFFFFFF);
        nombreSecret = (((nombreSecret >> 5) ^ nombreSecret) & 0xFFFFFF);
        nombreSecret = (((nombreSecret << 11) ^ nombreSecret) & 0xFFFFFF);
        return nombreSecret;
    }

    private Map<String, Integer> calculeSequences(long nombreSecret) {
        Map<String, Integer> result = new HashMap<>();
        long nombrePrecedent = nombreSecret;
        int prixPrecedent = (int) (nombreSecret % 10L);
        int prix;
        int diff;
        long nombreSuivant;
        List<Integer> collector = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {

            nombreSuivant = calculeNombreSuivant(nombrePrecedent);
            prix = (int) (nombreSuivant % 10);
            diff = prix - prixPrecedent;
            collector.add(diff);
            if (collector.size() == 4) {
                result.putIfAbsent(collector.toString(), prix);
                collector.removeFirst();
            }
            prixPrecedent = prix;
            nombrePrecedent = nombreSuivant;
        }
        return result;
    }

    public long calculeSequencesEtSommesCorrespondantes(List<Long> nombresSecrets) {
        Map<String, Integer> mapDesPrix = new HashMap<>();
        nombresSecrets.forEach(nombreSecret -> calculeSequences(nombreSecret).
                forEach((key, value) -> mapDesPrix.merge(key, value, Integer::sum)));
        return mapDesPrix.values().stream().mapToInt(Integer::intValue).max().orElseThrow();
    }
}
