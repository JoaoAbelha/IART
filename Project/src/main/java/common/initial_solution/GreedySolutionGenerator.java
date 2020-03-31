package common.initial_solution;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import common.Problem;
import common.Solution;
import model.Car;
import model.Ride;

public class GreedySolutionGenerator implements InitialSolutionGenerator<Solution> {

  /**
   * Generates initial greedy solution for a problem
   *
   * @param problem
   * @return initial solution
   */
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

      chosen.addRide(rideToAssign);
      unassignedRides.remove(rideToAssign);
      rideToAssign.assign();
    }

    return new Solution(state, unassignedRides);
  }

}