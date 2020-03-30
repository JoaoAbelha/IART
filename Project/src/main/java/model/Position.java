package model;

public class Position {

    private int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getDistanceTo(Position position) {
        return Math.abs(this.y - position.y) + Math.abs(this.x - position.x);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) return true;

        if (!(obj instanceof Position)) {
            return false;
        }

        Position p = (Position) obj;

        return p.x == this.x && p.y == this.y;
    }

    @Override
	public Position clone() {
		return new Position(x, y);
	}
}
