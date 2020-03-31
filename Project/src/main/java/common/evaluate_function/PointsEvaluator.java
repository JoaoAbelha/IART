package common.evaluate_function;

import java.util.List;

import common.Problem;
import common.Solution;
import model.Car;
import model.Position;
import model.Ride;

public class PointsEvaluator implements EvaluateFunction<Solution> {
  /**
   * Calculates value for a solution
   *
   * @param solution
   * @return value of the solution
   */
  @Override
  public int evaluate(Solution solution) {
    if(!solution.isValid())
      return -1;

    int points = 0;

    for (Car car : solution.getState()) {
      List<Ride> rides = car.getAssignedRides();

      if(rides.size() == 0)
        continue;

      int time = 0, i;
      Position pos = new Position(0, 0);

      for(i = 0; i < rides.size() - 1; ++i) {
          int distance = rides.get(i).getDistance();
          Position newPos = rides.get(i).getStart();
        points += distance;
        time += pos.getDistanceTo(newPos) + distance;
        pos = newPos;

        if(time <= rides.get(i + 1).getEarliestStart()) {
          points += Problem.perRideBonus;
        }
      }

      // last ride
      int distance = rides.get(i).getDistance();
      points += distance;
    }

    return points;
  }
}