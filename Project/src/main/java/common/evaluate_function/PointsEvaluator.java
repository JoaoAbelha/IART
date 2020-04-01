package common.evaluate_function;

import java.util.HashSet;
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

  /**
   * Given a list of cars calculates which rides have bonus
   *
   * @param list of cars
   * @return the ID of rides with bonus
   */
  public static HashSet<Integer> getRideWithBonus(List<Car> cars) {

    HashSet<Integer> bonusCardId = new HashSet<Integer>();

    for (Car car : cars) {
      List<Ride> rides = car.getAssignedRides();

      if (rides.size() == 0)
        continue;

      int time = 0, i;
      Position pos = new Position(0, 0);

      for (i = 0; i < rides.size() - 1; ++i) {
        int distance = rides.get(i).getDistance();
        Position newPos = rides.get(i).getStart();
        time += pos.getDistanceTo(newPos) + distance;
        pos = newPos;

        if (time <= rides.get(i + 1).getEarliestStart()) {
          bonusCardId.add(rides.get(i).id);
        }
      }

    }
    return bonusCardId;
  }
}
