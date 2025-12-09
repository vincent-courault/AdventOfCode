package adventofcode;

record Rectangle(Coord mini, Coord maxi) {

    static Rectangle depuisCoords(Coord a, Coord b) {
        return new Rectangle(new Coord(Math.min(a.x, b.x), Math.min(a.y, b.y)),
                new Coord(Math.max(a.x, b.x), Math.max(a.y, b.y)));
    }

    boolean estEnIntersection(Rectangle other) {
        return mini.x < other.maxi.x
                && maxi.x > other.mini.x
                && mini.y < other.maxi.y
                && maxi.y > other.mini.y;
    }

    Long calculeSurface() {
        return (long) (maxi.x - mini.x + 1) * (maxi.y - mini.y + 1);
    }
}