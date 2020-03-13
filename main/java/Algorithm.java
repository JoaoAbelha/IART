/*parent class of all algorithm with generic information*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
*  Class that has the attributes common to the problem
*  creates an initial solution -> initial state (may be create class state)
*  child classes should aim to create better solutions using intelligent algorithms
* */
public abstract class Algorithm {

    public static Position start = new Position(0,0);

    private int steps;
    private List<Car> cars = new ArrayList<>();
    private List<Ride> rides;
    private int bonus;


    Algorithm() {}


    public void fillWithData(int rows, int cols, int nrCars, List <Ride> rides, int steps, int bonus) {
        for(int i = 0; i < nrCars; i++) {
            cars.add(new Car());
        }
        this.steps = steps;
        this.rides = rides;
        this.steps = steps;
        this.bonus = bonus;

    }


    /*GREEDY SOLUTION FOR START*/
    /*provides a non optimal initial solution that needs to get better*/
    /*simple solution that chooses a car that is free which distance is shorter to get to the next ride*/
    /*since rides are sorted the assignment of rides will be done incrementally by start time*/
    /*although is not a bad algorithm is not optimal: for instance there are left two rides for a car
    *  start time: 3 latest:8 dist = 3
    *  start time : 4 latest 9 dist = 5
    *
    *  if we are in the time = 3 we choose the first ride that gives us 3 points and we do not have time for the second one
    *  if we have chosen the second one we would get 5 points
    *
    *  by trying to choose the car which is closer to the next person to be transported(sorted) we aim to lose the least time possible
    * */
    /*
    * A State is constituted of the cars and its assigned rides + the rides that have not been assigned
    * Neighbor State is a State which have a different configuration from the current state
    * We should aim for the neighbor states that provide us more points (better solution)
    * note that the bonus is not taken into account
    *
    * */

    public void initialSolution() {
        Collections.sort(rides, Comparator.comparingInt(Ride::getEarliestStart));

        int currentStep = 0;

        while(currentStep < this.steps && this.rides.size() > 0) {

            Ride rideToAssign = rides.get(0);
            int minDistance = Integer.MAX_VALUE;
            Car chosen = null;
            currentStep++;
            for(Car car : this.cars) {
                if (car.isBusy(currentStep)) {
                    continue;
                }

                int distanceBetween = car.position.getDistanceTo(rideToAssign.getStart());

                if (minDistance > distanceBetween) {
                    if (distanceBetween + rideToAssign.getDistance() + currentStep > rideToAssign.getLastestFinish()) continue;
                    minDistance = distanceBetween;
                    chosen = car;
                }
            }

            if (chosen == null) {
                continue;
            }

            chosen.addRide(rideToAssign);
            rides.remove(rideToAssign);

            //System.out.println(chosen.busyUntil);

        }

        for( Car car : cars) {
            System.out.println("Car: ");
            for(Ride ride : car.assignedRides) {
                ride.print();
                //score += car.score;
            }

            System.out.println("----------------------------");
        }

        System.out.println("Not assigned rides:" + rides.size());

    }

    int calculatePoints() {
        /*todo*/
        return 0;
    }

    public abstract void solve();


    /*
    * The initial configuration, state, can be random and we simply should try to get better from this point
    * */
    public void initialRandomSolution() {

       /*
       * do we want to do this ?
       */

    }



}
