package adventofcode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdventDay20 extends Commun {

    @Rule
    public TestName name = new TestName();


    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(3, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(4224, traitement(inputs, true));
    }

    @Test
    public void etape2_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(1623178306L, traitement(inputs, false));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(861907680486L, traitement(inputs, false));
    }


    public long traitement(List<String> inputs, boolean etape1) throws FileNotFoundException {
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            long val = Integer.parseInt(inputs.get(i));
            elements.add(new Element(i, etape1 ? val : val * 811589153));
        }

        for (int k = 0; k < (etape1 ? 1 : 10); k++) {
            for (int i = 0; i < elements.size(); i++) {
                //on cherche l'élément à déplacer
                Element element = null;
                int indiceElementADeplacer = 0;
                for (int j = 0; j < elements.size(); j++) {
                    if (elements.get(j).positioninitiale == i) {
                        element = elements.get(j);
                        indiceElementADeplacer = j;
                    }
                }
                //On le supprime
                elements.remove(indiceElementADeplacer);
                // on calcule la position et on déplace
                int tailleElements = elements.size();
                assert element != null;
                int valeurDeplacement = (int) (element.valeur % (tailleElements));
                int nouvelIndice = (indiceElementADeplacer + valeurDeplacement + tailleElements) % tailleElements;
                elements.add(nouvelIndice, element);
            }
        }

        // on cherche la position de 0
        int positionDe0 = 0;
        for (int j = 0; j < elements.size(); j++) {
            if (elements.get(j).valeur == 0) {
                positionDe0 = j;
            }
        }

        long resultat = (elements.get((positionDe0 + 1000) % elements.size()).valeur
                + elements.get((positionDe0 + 2000) % elements.size()).valeur
                + elements.get((positionDe0 + 3000) % elements.size()).valeur);

        System.out.println(this.getClass().getSimpleName() + " " + name.getMethodName() + " : " + resultat);
        return resultat;
    }

    static class Element {
        int positioninitiale;
        long valeur;

        public Element(int positioninitiale, long val) {
            this.positioninitiale = positioninitiale;
            this.valeur = val;
        }
    }
}
