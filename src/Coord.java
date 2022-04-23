package src;


public class Coord {
    private int x;
    private int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equal(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean equal(Coord other) {
        return this.x == other.x && this.y == other.y;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public void set_coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord next_coord(Direction d) {
        return switch (d) {
            case UP -> new Coord(this.x(), Math.max(0, this.y() - 1));
            case DOWN -> new Coord(this.x(), Math.min(6, this.y() + 1));
            case RIGHT -> new Coord(Math.min(6, this.x() + 1), this.y());
            case LEFT -> new Coord(Math.max(0, this.x() - 1), this.y());
            default -> throw new IllegalArgumentException("Unexpected value: " + d);
        };
    }

    public boolean is_in_bound() {
        boolean output = false;
        int i = this.x();
        int j = this.y();
        if (i == 2 || i == 3) {
            output = true;
        } else if ((i == 1 || i == 4) && 1 <= j && j <= 4) {
            output = true;
        } else if ((i == 0 || i == 5) && 2 <= j && j <= 3) {
            output = true;
        }
        return output;
    }
}
