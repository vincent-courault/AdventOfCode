package adventofcode;

import java.util.Objects;

public class Coord implements Comparable<Coord> {

    int x;
    int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord(String x, String y) {
        this.x = Integer.parseInt(x);
        this.y = Integer.parseInt(y);
    }

    public static Coord add(Coord a, Coord b) {
        return new Coord(a.x + b.x, a.y + b.y);
    }

    public int ligne() {
        return this.x;
    }

    public int colonne() {
        return this.y;
    }

    public Coord deplace(Direction d, int nbPas) {
        return new Coord(ligne() + (nbPas * d.diffLigne()), colonne() + (nbPas * d.diffColonne()));
    }

    public Coord deplace(Direction d) {
        return deplace(d, 1);
    }

    public Coord add(Coord o) {
        return add(this, o);
    }

    public Direction calculeEcart(Coord other) {
        return new Direction(other.ligne() - ligne(), other.colonne() - colonne());
    }

    public int distance(Coord other) {
        return Math.abs(other.ligne() - ligne()) + Math.abs(other.colonne() - colonne());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coord{ligne=" + x + ", colonne=" + y + '}';
    }

    @Override
    public int compareTo(Coord o) {
        return o.toString().compareTo(this.toString());
    }
}