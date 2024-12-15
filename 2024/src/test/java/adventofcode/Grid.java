package adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid<T> {
    public List<List<T>> grid;

    public Grid(List<String> in, Divider<T> d) {
        grid = new ArrayList<>();
        in.forEach(s -> grid.add(d.toList(s)));
    }

    public Grid(List<List<T>> in) {
        grid = new ArrayList<>();
        in.forEach(s -> grid.add(new ArrayList<>(s)));
    }

    public Grid(int height, int width, T valeurInit) {
        grid = IntStream.range(0, height).
                mapToObj(_ -> IntStream.range(0, width).
                        mapToObj(_ -> valeurInit).collect(Collectors.toList())).
                collect(Collectors.toList());
    }

    public Grid(Grid<T> grilleVide) {
        this(grilleVide.grid);
    }

    public int getHeight() {
        return grid.size();
    }

    public int getWidth() {
        return grid.getFirst().size();
    }

    public T get(int row, int col) {
        return grid.get(row).get(col);
    }

    public void set(int row, int col, T val) {
        grid.get(row).set(col, val);
    }

    public boolean isValid(int row, int col) {
        return 0 <= row && row < getHeight() && 0 <= col && col < getWidth();
    }

    public boolean isValid(Coord c) {
        return isValid(c.ligne(), c.colonne());
    }

    public int compte(T valeur) {
        int resultat = 0;
        for (int ligne = 0; ligne < getHeight(); ligne++) {
            for (int colonne = 0; colonne < getWidth(); colonne++) {
                if (get(ligne, colonne).equals(valeur)) {
                    resultat++;
                }
            }
        }
        return resultat;
    }

    public String toString() {
        return grid.stream()
                .map(row -> row.toString().replaceAll("[\\[\\], ]", "") + "\n")
                .collect(Collectors.joining());
    }

    public Coord getStartingPoint(T value) {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (this.get(i, j).equals(value)) {
                    return new Coord(i, j);
                }
            }
        }
        return new Coord(0,0);
    }

    @Override
    public int hashCode() {
        return grid.hashCode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Grid<?> o)) {
            return false;
        }
        if (o.get(0, 0).getClass() == this.get(0, 0).getClass()) {
            return false;
        }
        return Arrays.equals(((Grid<T>) other).grid.toArray(), this.grid.toArray());
    }

    public T get(Coord c) {
        return get(c.ligne(), c.colonne());
    }
}
