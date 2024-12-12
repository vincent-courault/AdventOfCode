package adventofcode;

import java.util.Objects;

public class Coord {

    int ligne;
    int colonne;

    public Coord(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public int ligne() {
        return this.ligne;
    }

    public int colonne() {
        return this.colonne;
    }

    public Coord deplace(Direction d, int nbPas) {
        return new Coord(ligne() + (nbPas * d.diffLigne()), colonne() + (nbPas * d.diffColonne()));
    }

    public Coord deplace(Direction d) {
        return deplace(d, 1);
    }

    public Direction calculeEcart(Coord other) {
        return new Direction(other.ligne() - ligne(), other.colonne() - colonne());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return ligne == coord.ligne && colonne == coord.colonne;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ligne, colonne);
    }

    @Override
    public String toString() {
        return "Coord{ligne=" + ligne +", colonne=" + colonne +'}';
    }
}