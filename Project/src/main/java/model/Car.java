package model;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private static int idCounter = 1;
    public int id;
    private List<Ride> assignedRides = new ArrayList<>();
    private Position position = new Position(0,0);
    private int busyUntil = 0;

    public Car(Position position) {
        this.id = idCounter++;
        this.position = position;
    }

    public Car(int id, List<Ride> assigned, Position position, int busyUntil) {
        this.id = id;
        this.assignedRides = assigned;
        this.position = position;
        this.busyUntil = busyUntil;
    }

    public void setAssignedRides(List<Ride> assignedRides) {
            this.assignedRides = assignedRides;
    }

    public List<Ride> getAssignedRides() {
        return this.assignedRides;
    }

    public int getDistanceTo(Ride r) {
        return this.position.getDistanceTo(r.getStart());
    }

    public void addRide(Ride r) {
        int stepsNeededToGetPerson = this.position.getDistanceTo(r.getStart());
        this.assignedRides.add(r);
        this.busyUntil+= stepsNeededToGetPerson + r.getDistance();
    }

    public boolean isBusy(int step) {
       return this.busyUntil > step;
    }

    @Override
	public Car clone() {
        List<Ride> assigned = new ArrayList<>();
        for(int i = 0; i < assignedRides.size(); ++i) {
            assigned.add(this.assignedRides.get(i).clone());
        }
		return new Car(id, assigned, position.clone(), busyUntil);
	}
}
