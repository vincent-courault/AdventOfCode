package adventofcode;

import org.apache.commons.collections4.ListUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class AdventDay3 {

    @Rule
    public TestName name = new TestName();

    @Test
    public void etape1_exemple() {
        List<String> rucksacks = List.of("vJrwpWtwJgWrhcsFMMfFFhFp",
                "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
                "PmmdzqPrVvPwwTWBwg",
                "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
                "ttgJtRGJQctTZtZT",
                "CrZsJsPPZsGzwwsLwLmpwMDw");

        assertEquals(157, etape1_main(rucksacks));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day3.txt")).toURI();
        List<String> rucksacks = Files.readAllLines(Paths.get(path));
        assertEquals(7824, etape1_main(rucksacks));
    }

    public int etape1_main(List<String> rucksacks) {
        int valeur = 0;
        for (String rucksack : rucksacks) {
            int longueur = rucksack.length();
            String firstCompartment = rucksack.substring(0, longueur / 2);
            String secondCompartment = rucksack.substring(longueur / 2);
            String commonItem = determinerItemCommun(firstCompartment, secondCompartment);
            int priority = determinerPriorite(commonItem);
            valeur += priority;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + valeur);

        return valeur;
    }

    private int determinerPriorite(String commonItem) {
        // Lowercase item types a through z have priorities 1 through 26.
        // Uppercase item types A through Z have priorities 27 through 52.
        if (estEnMinuscule(commonItem)) {
            return commonItem.codePointAt(0) - "a".codePointAt(0) + 1;
        }
        return commonItem.codePointAt(0) - "A".codePointAt(0) + 27;
    }

    private boolean estEnMinuscule(String commonItem) {
        return commonItem.codePointAt(0) > "a".codePointAt(0);
    }


    @Test
    public void etape2_exemple() {
        List<String> rucksacks = List.of("vJrwpWtwJgWrhcsFMMfFFhFp",
                "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
                "PmmdzqPrVvPwwTWBwg",
                "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
                "ttgJtRGJQctTZtZT",
                "CrZsJsPPZsGzwwsLwLmpwMDw");

        assertEquals(70, etape2_main(rucksacks));
    }


    @Test
    public void etape2() throws IOException, URISyntaxException {
        URI path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("input_day3.txt")).toURI();
        List<String> rucksacks = Files.readAllLines(Paths.get(path));
        assertEquals(2798, etape2_main(rucksacks));
    }

    private int etape2_main(List<String> rucksacks) {
        int valeur = 0;
        List<List<String>> groupes = ListUtils.partition(rucksacks, 3);
        for (List<String> groupe : groupes) {
            String itemsCommuns = determinerItemsCommuns(groupe.get(0), groupe.get(1));
            String itemcommunaugroupe = determinerItemCommun(groupe.get(2), itemsCommuns);
            int prio = determinerPriorite(itemcommunaugroupe);
            valeur += prio;
        }
        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + valeur);

        return valeur;
    }

    private String determinerItemCommun(String firstCompartment, String secondCompartment) {
        char[] firstArray = firstCompartment.toCharArray();

        for (Character caractere : firstArray) {
            if (secondCompartment.contains(caractere.toString())) {
                return caractere.toString();
            }
        }
        return null;
    }

    private String determinerItemsCommuns(String firstRucksack, String secondRucksack) {
        char[] firstArray = firstRucksack.toCharArray();
        StringBuilder itemscommuns = new StringBuilder();
        for (Character caractere : firstArray) {
            if (secondRucksack.contains(caractere.toString())) {
                itemscommuns.append(caractere);
            }
        }
        return itemscommuns.toString();
    }

}
