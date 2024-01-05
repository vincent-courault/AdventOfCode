package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay12 extends Commun {
    Map<String, Long> cache = new HashMap<>();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(21, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(7622, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(525152, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(4964259839627L, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean etape1) {
        long resultat = 0;
        List<String> records = new ArrayList<>();
        List<List<Integer>> conditions = new ArrayList<>();
        for (String line : inputs) {
            records.add(line.split(" ")[0]);
            conditions.add(Arrays.stream(line.split(" ")[1].split(",")).map(Integer::parseInt).collect(Collectors.toList()));
        }
        if (!etape1) {
            List<String> newRecords = new ArrayList<>();
            List<List<Integer>> newConditions = new ArrayList<>();
            for (int i = 0; i < records.size(); i++) {
                String record = records.get(i);
                List<Integer> condition = conditions.get(i);
                newRecords.add(record + "?" + record + "?" + record + "?" + record + "?" + record);
                newConditions.add(IntStream.range(0, 5).mapToObj(_ -> condition).flatMap(Collection::stream).collect(Collectors.toList()));
            }
            records = newRecords;
            conditions = newConditions;
        }
        for (int i = 0; i < records.size(); i++) {
            List<Integer> condition = conditions.get(i);
            String record = records.get(i);
            resultat += calculeLesPossibilites(condition, record);
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    private long calculeLesPossibilites(List<Integer> conditions, String record) {
        long resultat;
        String cle = conditions.toString()+record;

        if (cache.containsKey(cle)) {
            return cache.get(cle);
        }

        Integer nbRessortsDansConditions = conditions.stream().reduce(Integer::sum).orElse(0);
        long nbRessortsPossiblesDansRecord = record.chars().filter(value -> value == '#' || value=='?').count() ;
        if (nbRessortsDansConditions > nbRessortsPossiblesDansRecord) {
            cache.put(cle, 0L);
            return 0;
        }else {
            if (conditions.isEmpty()) {
                if (record.contains("#")) {
                    cache.put(cle, 0L);
                    return 0;
                }
                cache.put(cle, 1L);
                return 1;
            }
        }
        switch (record.charAt(0)) {
            //le premier élément est indéfini
            case '?' -> {
                //On démarre les nouvelles branches avec les deux valeurs possibles
                String rec1 = "#" + record.substring(1);
                String rec2 = record.substring(1); //inutile de démarrer avec un point
                List<Integer> conditionsCopie = new ArrayList<>(conditions);
                resultat = calculeLesPossibilites(conditionsCopie, rec1) + calculeLesPossibilites(conditionsCopie, rec2);
                cache.put(cle, resultat);
                return resultat;
            }
            //le premier élément est inutile
            case '.' -> {
                //on démarre la branche avec le record raccourci
                List<Integer> conditionsCopie = new ArrayList<>(conditions);
                resultat = calculeLesPossibilites(conditionsCopie, record.substring(1));
                cache.put(cle, resultat);
                return resultat;
            }
            // le premier élément est ok
            case '#' -> {
                //S'il y a autant d'éléments à placer dans la premiere valeur que la longueur du record
                if (conditions.getFirst() == record.length()) {
                    // s'il y a d'autres éléments à placer ou s'il y a des "." dans le record => ko
                    if (conditions.size() > 1 || record.contains(".")) {
                        cache.put(cle, 0L);
                        return 0;
                    } else {
                        cache.put(cle, 1L);
                        return 1;
                    }
                }
                //Si plus d'éléments à placer dans la première valeur que la longueur du record
                if (conditions.getFirst() > record.length()) {
                    cache.put(cle, 0L);
                    return 0;
                }
                //Si le nombre d'éléments à placer est égal à 1
                if (conditions.getFirst() == 1) {
                    switch (record.charAt(1)) {
                        case '.', '?' -> {
                            List<Integer> conditionsCopie = new ArrayList<>(conditions);
                            conditionsCopie.removeFirst();//on supprime la condition remplie
                            //on démarre la nouvelle branche directement en supprimant aussi le point
                            // ou le ? car avec un # la condition serait ko
                            resultat = calculeLesPossibilites(conditionsCopie, record.substring(2));
                            cache.put(cle, resultat);
                            return resultat;
                        }
                        case '#'-> {
                            cache.put(cle, 0L);
                            return 0;
                        }
                        default -> {
                            return 0;
                        }
                    }
                }
                //Si le nombre d'éléments à placer est supérieur à 1
                if (conditions.getFirst() > 1) {
                    //Et que le caractère suivant dans le record est un . => ko
                    if (record.charAt(1) == '.') {
                        cache.put(cle, 0L);
                        return 0;
                    }
                    // sinon on démarre une nouvelle branche en décrémentant la nombre de valeurs à positionner et
                    // avec un # pour démarrer (on ne crée pas la branche avec un "." => condition précédente)
                    List<Integer> conditionsCopie = new ArrayList<>(conditions);
                    conditionsCopie.set(0, conditions.getFirst() - 1);
                    resultat = calculeLesPossibilites(conditionsCopie, "#" + record.substring(2));
                    cache.put(cle, resultat);
                    return resultat;
                }
                return 0;
            }
            default -> {
                return 0;
            }
        }
    }
}
