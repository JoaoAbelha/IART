package common.initial_solution;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import common.Problem;
import common.Solution;
import model.Car;
import model.Ride;

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

public class GreedySolutionGenerator implements InitialSolutionGenerator<Solution> {

  @Override
  public Solution initialSolution(Problem problem) {
    // problem variables
    int steps = problem.getSteps();

    // solution variables
    List<Car> state = problem.getCars();
    List<Ride> unassignedRides = problem.getRides();

    Collections.sort(unassignedRides, Comparator.comparingInt(Ride::getEarliestStart));
    for (Ride ride : unassignedRides)
      ride.setID();

    int currentStep = 0;
    int carsSkipped = 0;

    while (currentStep < steps && unassignedRides.size() > carsSkipped) {

      Ride rideToAssign = unassignedRides.get(carsSkipped);
      int minDistance = Integer.MAX_VALUE;
      Car chosen = null;
      currentStep++;
      for (Car car : state) {
        if (car.isBusy(currentStep)) {
          continue;
        }

        int distanceBetween = car.getDistanceTo(rideToAssign);

        if (minDistance > distanceBetween) {
          if (distanceBetween + rideToAssign.getDistance() + currentStep > rideToAssign.getLastestFinish())
            continue;
          minDistance = distanceBetween;
          chosen = car;
        }
      }

      if (chosen == null) {
        carsSkipped++;
        continue;
      }

      state.get(chosen.id - 1).addRide(rideToAssign);
      chosen.addRide(rideToAssign);
      unassignedRides.remove(rideToAssign);
      rideToAssign.assign();
    }

    for (Car car : state) {
      System.out.println("Car: ");
      for (Ride ride : car.getAssignedRides()) {
        ride.print();
        // score += car.score;
      }
      System.out.println("----------------------------");
    }

    return new Solution(state, unassignedRides);
  }

}