package adventofcode;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay05 extends Commun {

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

    public long traitement(List<String> inputs, boolean etape1) throws ExecutionException, InterruptedException {
        long resultat = Long.MAX_VALUE;

        Long[] seeds = Arrays.stream(inputs.get(0).split(" ")).skip(1).map(Long::parseLong).toArray(Long[]::new);

        List<TransformationsMap> transformationsMaps = new ArrayList<>();
        List<Transformation> tmp = new ArrayList<>();

        for (int i = 2; i < inputs.size(); i++) {
            String line = inputs.get(i);
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

            List<ImmutablePair<Long, Long>> list = IntStream.iterate(0, i -> i < seeds.length, i -> i + 2)
                    .mapToObj(i -> new ImmutablePair<>(seeds[i], seeds[i + 1])).toList();

            List<Future<?>> futures = new ArrayList<>();
            ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

            for (Pair<Long, Long> pair : list) {
                pair.getRight();
                Future<?> future = executorService.submit(() -> {
                    long result = Long.MAX_VALUE;
                    for (long j = pair.getKey(); j < pair.getKey() + pair.getValue(); j++) {
                        long val = j;
                        for (TransformationsMap transformationsMap : transformationsMaps) {
                            val = transformationsMap.appliqueLaTransformation(val);
                        }
                        result = Math.min(val, result);
                    }
                    return result;
                });
                futures.add(future);
            }

            for (Future<?> future : futures) {
                resultat = Math.min(resultat, (long) future.get());
            }
            executorService.shutdown();
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
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
        List<Long> bornesInf;
        List<Long> bornesSup;
        List<Long> ecartsAAppliquer;

        public TransformationsMap(List<Transformation> transformations) {
            bornesInf = new ArrayList<>();
            bornesSup = new ArrayList<>();
            ecartsAAppliquer = new ArrayList<>();
            for (Transformation transformation : transformations) {
                bornesInf.add(transformation.source);
                ecartsAAppliquer.add(transformation.destination - transformation.source);
                bornesSup.add(transformation.source + transformation.longueur);
            }
        }

        public long appliqueLaTransformation(long val) {
            for (int i = 0; i < bornesInf.size(); i++)
                if (bornesInf.get(i) <= val && val < bornesSup.get(i))
                    return ecartsAAppliquer.get(i) + val;
            return val;
        }
    }

}
