package model;

public class Ride {
    private static int idCounter = 1;
    public int id;

    private boolean assigned;

    private Position start;
    private Position end;

    private int earliestStart;
    private int lastestFinish;


    public Ride(Position start, Position end, int earliestStart, int latestFinish) {
        this.assigned = false;
        this.start = start;
        this.end = end;
        this.earliestStart = earliestStart;
        this.lastestFinish = latestFinish;
    }

    public Ride(int id, boolean assigned, Position start, Position end, int earliestStart, int lastestFinish) {
        this.id = id;
        this.assigned = assigned;
        this.start = start;
        this.end = end;
        this.earliestStart = earliestStart;
        this.lastestFinish = lastestFinish;
    }

    public void setID() {
        this.id = idCounter++;
    }

    public void print() {
        System.out.println("Ride from [" + start.getX() + ", " + start.getY() + "] to [" + end.getX() + ", " + end.getY() + "]  Earliest Start: " + earliestStart + "; LatestFinish: " + lastestFinish);
    }

    public int getEarliestStart() {
        return earliestStart;
    }

    public int getLastestFinish() {
        return lastestFinish;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    /*distance of the ride*/
    public int getDistance () {
        return start.getDistanceTo(end);
    }

    /*distance to the next ride*/
    public int betweenRides(Ride next) {
        return end.getDistanceTo(next.start);
    }

    public void assign() {
        assigned = true;
    }

    public void unassign() {
        assigned = false;
    }

    public boolean isAssigned() {
        return assigned;
    }


    @Override
	public Ride clone() {
		return new Ride(id, assigned, start.clone(), end.clone(), earliestStart, lastestFinish);
	}
}
