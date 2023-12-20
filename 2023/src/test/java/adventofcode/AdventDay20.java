package adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventDay20 extends Commun {
    public static final String CONJUCTION_MODULE = "conj";
    public static final String FLIP_FLOP_MODULE = "flop";
    public static final String BROADCASTER_MODULE = "broadcaster";
    public static final String BUTTON = "button";
    HashMap<String, Module> modules = new HashMap<>();
    long compteurBouton;
    long compteurLowPulse;
    long compteurHighPulse;
    Map<String, Long> cycles = new HashMap<>();

    @Test
    public void etape1_exemple() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier(this, true);
        assertEquals(32000000, traitement(inputs, true));
    }

    @Test
    public void etape1_exemple2() throws URISyntaxException, IOException {
        List<String> inputs = lectureDuFichier2(this, true);
        assertEquals(11687500, traitement(inputs, true));
    }

    @Test
    public void etape1() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(899848294, traitement(inputs, true));
    }

    @Test
    public void etape2() throws IOException, URISyntaxException {
        List<String> inputs = lectureDuFichier(this, false);
        assertEquals(247454898168563L, traitement(inputs, false));
    }

    public long traitement(List<String> inputs, boolean etape1) {
        long resultat;
        for (String line : inputs) {
            Module m = new Module(line);
            modules.put(m.nom, m);
        }
        if (etape1) {
            resetAll();
            LinkedList<Pulse> queue = new LinkedList<>();
            while (compteurBouton < 1000) {
                queue.add(new Pulse(BUTTON, BROADCASTER_MODULE, false));
                while (!queue.isEmpty()) {
                    Pulse pulse = queue.pollFirst();
                    Module module = modules.get(pulse.destination);
                    if (module != null) {
                        queue.addAll(module.traitePulse(pulse));
                    }
                }
                compteurBouton++;
            }
            resultat = compteurLowPulse * compteurHighPulse;
        }else {
            // rx est déclenché par le conjunction module dn
            // dn est déclenché par 4 modules
            // il faut donc identifier pour chaque module appelant de dn la taille du cycle
            // il faudra ensuite calculer le ppcm des 4 valeurs obtenues
            // L'observation a montré que le cycle est depuis 0, pas de décalage avant le cycle

            resetAll();

            String declencheurDeRx = modules.values().stream()
                    .filter(d -> d.destinations.contains("rx")).findFirst().get().nom;

            int nombreDeclencheurFin = Math.toIntExact(modules.values().stream().flatMap(m -> m.destinations.stream())
                    .filter(d -> d.equals(declencheurDeRx)).count());

            LinkedList<Pulse> queue = new LinkedList<>();

            while (cycles.size() < nombreDeclencheurFin) {
                compteurBouton++;
                queue.add(new Pulse(BUTTON, BROADCASTER_MODULE, false));
                while (!queue.isEmpty()) {
                    Pulse pulse = queue.pollFirst();
                    Module module = modules.get(pulse.destination);
                    if (module != null) {
                        queue.addAll(module.traitePulse(pulse));
                    }
                }
            }

            List<Long> taillesCycles = new ArrayList<>(cycles.values());
            resultat = taillesCycles.getFirst();
            for (int i = 1; i < taillesCycles.size(); i++) {
                resultat = calculeLePPCM(resultat, taillesCycles.get(i));
            }
        }

        System.out.println(this.getClass().getSimpleName() + " " + name + " : " + resultat);
        return resultat;
    }

    public void resetAll() {
        compteurLowPulse = 0;
        compteurHighPulse = 0;
        compteurBouton = 0;
        modules.values().forEach(Module::reset);
    }

    public class Pulse {
        final String origine;
        final String destination;
        final boolean signal;
        public Pulse(String origine, String destination, boolean signal) {
            this.origine = origine;
            this.destination = destination;
            this.signal = signal;
            if (signal) {
                compteurHighPulse++;
            } else {
                compteurLowPulse++;
            }
        }
        public String toString() {
            return "Pulse from " + origine + " to " + destination + " " + signal;
        }
    }
    public class Module {
        String type;
        String nom;
        ArrayList<String> destinations = new ArrayList<>();
        HashMap<String, Boolean> conjTriggers;
        boolean etatFlipFlop = false;
        public Module(String line) {
            List<String> param = List.of(line.replace(",", "").split(" "));
            nom = param.get(0);
            if (nom.startsWith("%")) {
                type = FLIP_FLOP_MODULE;
                nom = nom.replace("%", "");
            } else if (nom.startsWith("&")) {
                type = CONJUCTION_MODULE;
                nom = nom.replace("&", "");
            } else if (nom.equals(BROADCASTER_MODULE)) {
                type = BROADCASTER_MODULE;
            }
            for (int i = 2; i < param.size(); i++) {
                destinations.add(param.get(i));
            }
        }
        public void reset() {
            etatFlipFlop = false;
            if (type.equals(CONJUCTION_MODULE)) {
                conjTriggers = new HashMap<>();
                modules.values().forEach(m -> m.destinations.stream().filter(d -> d.equals(nom)).forEach(_ -> conjTriggers.put(m.nom, false)));
            }
        }

        public ArrayList<Pulse> traitePulse(Pulse pulse) {
            ArrayList<Pulse> pulses = new ArrayList<>();
            if (type.equals(FLIP_FLOP_MODULE)) {
                if (!pulse.signal) {
                    etatFlipFlop = !etatFlipFlop;
                    for (String destination : destinations) {
                        pulses.add(new Pulse(nom, destination, etatFlipFlop));
                    }
                }
            }
            if (type.equals(CONJUCTION_MODULE)) {
                if (pulse.signal && nom.equals("dn") && !cycles.containsKey(pulse.origine)) {
                    //System.out.println(pulse + "  "+ compteurBouton);
                    cycles.put(pulse.origine, compteurBouton);
                }
                conjTriggers.put(pulse.origine, pulse.signal);
                boolean declencheurActifs = true;

                for (boolean value : conjTriggers.values()) {
                    if (!value) {
                        declencheurActifs = false;
                        break;
                    }
                }
                for (String destination : destinations) {
                    pulses.add(new Pulse(nom, destination, !declencheurActifs));
                }
            }
            if (type.equals(BROADCASTER_MODULE)) {
                for (String destination : destinations) {
                    pulses.add(new Pulse(nom, destination, pulse.signal));
                }
            }
            return pulses;
        }
    }
    static long calculeLePPCM(long a, long b) {
        return (a * b) / calculeLePGCD(a, b);
    }
    static long calculeLePGCD(long a, long b) {
        if (a < b) {
            return calculeLePGCD(b, a);
        }
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}