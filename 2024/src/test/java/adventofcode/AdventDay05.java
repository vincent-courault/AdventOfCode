package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay05 extends Commun {


    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(143, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(4766, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(123, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(6257, traitement(inputs, false));
    }

    public int traitement(List<String> inputs, boolean etape1) {

        Map<Integer, Page> rules = new HashMap<>();
        List<String> pagesToProduce = new ArrayList<>();
        for (String input : inputs) {
            if (input.contains("|")) {
                int[] in = Arrays.stream(input.split("\\|")).mapToInt(Integer::parseInt).toArray();
                rules.putIfAbsent(in[0], new Page(in[0]));
                rules.putIfAbsent(in[1], new Page(in[1]));
                rules.get(in[0]).apres.add(in[1]);
                rules.get(in[1]).avant.add(in[0]);
            }
            if (input.contains(",")) {
                pagesToProduce.add(input);
            }
        }

        int sommeDejaTrie = 0, sommeApresTri = 0, resultat;
        for (String s : pagesToProduce) {
            int[] pages = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
            boolean ordered = true;

            for (int page = 0; page < pages.length; page++) {
                for (int pre = 0; pre < page; pre++) {
                    if (rules.get(pages[page]).apres.contains(pages[pre])) {
                        ordered = false;
                    }
                }
                for (int post = page + 1; post < pages.length; post++) {
                    if (rules.get(pages[page]).avant.contains(pages[post])) {
                        ordered = false;
                    }
                }
            }

            if (ordered) {
                sommeDejaTrie += pages[pages.length / 2];
            } else {
                List<Page> list = Arrays.stream(pages).mapToObj(rules::get).sorted().toList();
                sommeApresTri += (list.get((list.size() - 1) / 2).page);
            }
        }

        if (etape1) {
            resultat = sommeDejaTrie;
        } else {
            resultat = sommeApresTri;
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);

        return resultat;
    }


    static class Page implements Comparable<Page> {
        int page;
        List<Integer> avant = new ArrayList<>();
        List<Integer> apres = new ArrayList<>();

        public Page(int page) {
            this.page = page;
        }

        @Override
        public int compareTo(Page o) {
            if (this.page == o.page) {
                return 0;
            }
            if (this.avant.contains(o.page)) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
