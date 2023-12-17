package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay09 extends Commun {

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(114, traitement(inputs,true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1806615041, traitement(inputs,true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(2L, traitement(inputs,false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(1211, traitement(inputs,false));
    }

    public Long traitement(List<String> inputs,boolean etape1) {
        Long resultat = 0L;
        List<List<Long>> pyramide;
        for (String line : inputs) {
            List<Long> numLine =new ArrayList<>( Arrays.stream(line.split(" ")).map(Long::parseLong).toList());
            pyramide= new ArrayList<>();
            pyramide.add(numLine);

            boolean fin = false;
            while (!fin) {
                ArrayList<Long> nouvelleLigne = new ArrayList<>();
                List<Long> last = pyramide.getLast();
                for (int i = 0; i < last.size() - 1; i++) {
                    nouvelleLigne.add(last.get(i + 1) - last.get(i));
                }
                pyramide.add(nouvelleLigne);
                if (nouvelleLigne.get(0).equals(0L) && Collections.frequency(nouvelleLigne, nouvelleLigne.get(0)) == nouvelleLigne.size()) {
                    fin = true;
                }
            }
            for (int i = pyramide.size() - 2; i >= 0; i--) {
                List<Long> ligneActuelle = pyramide.get(i);
                ligneActuelle.add(pyramide.get(i+1).getLast()+ligneActuelle.getLast());
                ligneActuelle.addFirst(ligneActuelle.getFirst()-pyramide.get(i+1).getFirst());
            }

            if(etape1){
                resultat += pyramide.get(0).getLast();
            }else {
                resultat += pyramide.get(0).getFirst();
            }
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }
}
