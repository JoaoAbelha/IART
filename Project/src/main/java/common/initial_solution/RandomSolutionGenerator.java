package common.initial_solution;

import java.util.*;

import common.Problem;
import common.Solution;
import model.Car;
import model.Ride;

public class RandomSolutionGenerator implements InitialSolutionGenerator<Solution> {

  /**
   * Generates initial greedy solution for a problem
   *
   * @param problem
   * @return initial solution
   */
  @Override
  public Solution initialSolution(Problem problem) {
    Collections.sort(problem.getRides(), Comparator.comparingInt(Ride::getEarliestStart));
    int vehiclesNo = problem.getCars().size();
    int ridesNo = problem.getRides().size();

    List<Car> state = new ArrayList<>(problem.getCars());
    List<Ride> nonAssignedRides = new ArrayList<>(problem.getRides()); // non assigned rides
    Solution solution = new Solution(state, nonAssignedRides);

    for (int i = 0; i < ridesNo; i++) {
      int vehicleID = new Random().nextInt(vehiclesNo);
      Ride ride = problem.getRides().get(i);

      if (!(solution.getState().get(vehicleID).getAssignedRides().contains(ride))) {
        if (validAssignment(solution, vehicleID, ride)) {
          solution.getUnassignedRides().remove(ride);
        }
      }
    }

    return solution;
  }

  private boolean validAssignment(Solution solution, int vehicleID, Ride unassignedRide) {
    //try add ride
    solution.getState().get(vehicleID).getAssignedRides().add(unassignedRide);

    if(!solution.isValid()) {
      solution.getState().get(vehicleID).getAssignedRides().remove(unassignedRide);
      return false;
    }

    return true;
  }
}