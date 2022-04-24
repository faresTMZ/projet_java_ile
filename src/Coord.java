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

    public int x() { return this.x; }

    public int y() { return this.y; }

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
        int x = this.x();
        int y = this.y();
        if (x == 2 || x == 3) {
            output = true;
        } else if ((x == 1 || x == 4) && 1 <= y && y <= 4) {
            output = true;
        } else if ((x == 0 || x == 5) && 2 <= y && y <= 3) {
            output = true;
        }
        return output;
    }
}
