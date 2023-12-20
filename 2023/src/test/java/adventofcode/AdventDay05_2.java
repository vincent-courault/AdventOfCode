package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay05_2 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(35, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(165788812, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(46, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1928058, traitement(inputs, false));
    }

    public long traitement(List<String> in, boolean etape1) {
        long resultat = Long.MAX_VALUE;
        String[] stringSeeds = in.get(0).split(" ");
        long[] seeds = new long[stringSeeds.length - 1];
        IntStream.range(1, stringSeeds.length).forEach(i -> seeds[i - 1] = Long.parseLong(stringSeeds[i]));

        List<TransformationsMap> transformationsMaps = new ArrayList<>();
        List<Transformation> tmp = new ArrayList<>();
        for (int i = 2; i < in.size(); i++) {
            String line = in.get(i);
            if (line.isEmpty()) {
                continue;
            }
            if (line.contains("map")) {
                if (!tmp.isEmpty()) {
                    transformationsMaps.add(new TransformationsMap(tmp));
                }
                tmp = new ArrayList<>();
            } else {
                tmp.add(new Transformation(line));
            }
        }
        transformationsMaps.add(new TransformationsMap(tmp));
        if (etape1) {
            for (Long seed : seeds) {
                long val = seed;
                for (TransformationsMap transformationsMap : transformationsMaps) {
                    val = transformationsMap.appliqueLaTransformation(val);
                }
                resultat = Math.min(val, resultat);

            }
        } else {
            //on parcourt les intervalles d'entrée
            for (int indiceBorne = 0; indiceBorne < seeds.length; indiceBorne += 2) {
                //on récupére pour chaque intervalle le minimum non transformé
                // l'intervalle initial est découpé en fonction des étapes de trasnformation
                for (long valeur = seeds[indiceBorne]; valeur < seeds[indiceBorne] + seeds[indiceBorne + 1]; valeur++) {
                    long[] retourTransformations =
                            retourneLaValeurMinimumNonTransformeeEtLIntervalleDeMiseAJour(valeur, transformationsMaps);
                    resultat = Math.min(retourTransformations[0], resultat);
                    valeur += retourTransformations[1];
                }
            }
        }
        return resultat;
    }

    private long[] retourneLaValeurMinimumNonTransformeeEtLIntervalleDeMiseAJour(long valeur, List<TransformationsMap> transformationsMaps) {
        long borne = Long.MAX_VALUE;
        for (TransformationsMap transformationsMap : transformationsMaps) {
            long[] retourTransformation =
                    transformationsMap.retourneLaValeurMinimumNonTransformeeEtLIntervalleDeMiseAJour(valeur);
            borne = Math.min(borne, retourTransformation[1]);
            valeur = retourTransformation[0];
        }
        return new long[]{valeur, borne};
    }

    static class Transformation {
        long destination;
        long source;
        long longueur;

        public Transformation(String line) {
            String[] pieces = line.split(" ");
            destination = Long.parseLong(pieces[0]);
            source = Long.parseLong(pieces[1]);
            longueur = Long.parseLong(pieces[2]);
        }
    }

    static class TransformationsMap {
        List<Long> sourcesDeparts;
        List<Long> sourcesFins;
        List<Long> destinations;
        List<Long> longueurs;
        List<Long> ecarts;

        public TransformationsMap(List<Transformation> transformations) {
            sourcesDeparts = new ArrayList<>();
            sourcesFins = new ArrayList<>();
            destinations = new ArrayList<>();
            longueurs = new ArrayList<>();
            ecarts = new ArrayList<>();
            for (Transformation transformation : transformations) {
                sourcesDeparts.add(transformation.source);
                destinations.add(transformation.destination);
                longueurs.add(transformation.longueur);
                ecarts.add(transformation.destination - transformation.source);
                sourcesFins.add(transformation.source + transformation.longueur);
            }
        }

        public long appliqueLaTransformation(long val) {
            for (int i = 0; i < sourcesDeparts.size(); i++) {
                if (estDansLIntervalleDeTransfo(val, i)) {
                    return val + ecarts.get(i);
                }
            }
            return val;
        }

        public long[] retourneLaValeurMinimumNonTransformeeEtLIntervalleDeMiseAJour(long valeur) {
            long intervalleDeMiseAJour = Long.MAX_VALUE;
            for (int indiceDeTransfo = 0; indiceDeTransfo < sourcesDeparts.size(); indiceDeTransfo++) {
                // pas dans l'intervalle de transformation

                if (valeur < sourcesDeparts.get(indiceDeTransfo)) {
                    //On détermine l'intervalle qui permet de rester dans la même zone de non transformation
                    intervalleDeMiseAJour = Math.min(intervalleDeMiseAJour, sourcesDeparts.get(indiceDeTransfo) - valeur);
                }
                if (estDansLIntervalleDeTransfo(valeur, indiceDeTransfo)) {
                    return new long[]{(valeur + ecarts.get(indiceDeTransfo)), sourcesFins.get(indiceDeTransfo) - valeur - 1};
                }
            }
            return new long[]{valeur, intervalleDeMiseAJour == 10000000000L ? 0 : intervalleDeMiseAJour};
        }

        private boolean estDansLIntervalleDeTransfo(long val, int i) {
            return sourcesDeparts.get(i) <= val && val < sourcesFins.get(i);
        }
    }
}