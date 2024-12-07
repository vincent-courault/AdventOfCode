package adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid<T> {
    public List<List<T>> grid;

    public Grid(List<String> in, Divider<T> d) {
        grid = new ArrayList<>();
        for (String s : in) {
            grid.add(d.toList(s));
        }
    }

    public Grid(List<List<T>> g) {
        grid = g;
    }

    public Grid(Grid<T> grille) {

        this.grid = new ArrayList<>();
        for (List<T> row : grille.grid) {
            grid.add(new ArrayList<>(row)); // Copie chaque liste interne
        }
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

    public Grid<T> rotateCW() {
        List<List<T>> g = new ArrayList<>();
        for (int c = 0; c < getWidth(); c++) {
            List<T> col = new ArrayList<>();
            for (int r = getHeight() - 1; r >= 0; r--) {
                col.add(get(r, c));
            }
            g.add(col);
        }
        return new Grid<T>(g);
    }

    public Grid<T> rotateCCW() {
        List<List<T>> g = new ArrayList<>();
        for (int c = getWidth() - 1; c >= 0; c--) {
            List<T> col = new ArrayList<>();
            for (int r = 0; r < getHeight(); r++) {
                col.add(get(r, c));
            }
            g.add(col);
        }
        return new Grid<T>(g);
    }

    public Grid<T> transpose() {
        List<List<T>> g = new ArrayList<>();
        for (int c = 0; c < getWidth(); c++) {
            List<T> toRow = new ArrayList<>();
            for (int r = 0; r < getHeight(); r++) {
                toRow.add(get(r, c));
            }
            g.add(toRow);
        }
        return new Grid<T>(g);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (List<T> row : grid) {
            s.append(row.toString().replaceAll("[\\[\\],]", "")).append("\n");
        }
        return s.toString();
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

    public List<T> getNeighbours(int ligne, int colonne) {
        List<T> neighbours = new ArrayList<>(8);
        if (isValid(ligne + 1, colonne - 1)) {
            neighbours.add(this.get(ligne + 1, colonne - 1));
        }
        if (isValid(ligne + 1, colonne)) {
            neighbours.add(this.get(ligne + 1, colonne));
        }
        if (isValid(ligne + 1, colonne + 1)) {
            neighbours.add(this.get(ligne + 1, colonne + 1));
        }
        if (isValid(ligne - 1, colonne - 1)) {
            neighbours.add(this.get(ligne - 1, colonne - 1));
        }
        if (isValid(ligne - 1, colonne)) {
            neighbours.add(this.get(ligne - 1, colonne));
        }
        if (isValid(ligne - 1, colonne + 1)) {
            neighbours.add(this.get(ligne - 1, colonne + 1));
        }
        if (isValid(ligne, colonne + 1)) {
            neighbours.add(this.get(ligne, colonne + 1));
        }
        if (isValid(ligne, colonne - 1)) {
            neighbours.add(this.get(ligne, colonne - 1));
        }
        return neighbours;
    }
}