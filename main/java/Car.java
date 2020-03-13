import java.util.ArrayList;
import java.util.List;

public class Car {

    List<Ride> assignedRides = new ArrayList<>();

    Position position = new Position(0,0);

    int busyUntil = 0;

    int getDistanceTo(Ride r) {
        return  position.getDistanceTo(r.getStart());
    }

    void addRide(Ride r) {

        int stepsNeededToGetPerson = position.getDistanceTo(r.getStart());

        assignedRides.add(r);

        busyUntil+= stepsNeededToGetPerson + r.getDistance();

    }

    boolean isBusy(int step) {

       return busyUntil > step;

    }


}
