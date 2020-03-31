package hill_climbing;

import common.Algorithm;
import common.Solution;
import common.evaluate_function.EvaluateFunction;
import common.neighborhood.Neighborhood;
import model.Car;
import model.Ride;

import java.util.List;

public class HillClimbing extends Algorithm<Solution> {
  final private int MAX_ITERATIONS = 100000;
  private EvaluateFunction<Solution> evaluateFunction;
  private Neighborhood<Solution> neighborhood;

  public HillClimbing(EvaluateFunction<Solution> evaluateFunction, Neighborhood<Solution> neighborhood) {
    super();
    this.evaluateFunction = evaluateFunction;
    this.neighborhood = neighborhood;
  }

  @Override
  public Solution solve(Solution initialSolution) {
    int iter = 0;
    Solution globalBest = initialSolution;
    values.add(evaluateFunction.evaluate(globalBest));
    while (iter < MAX_ITERATIONS) {
      int bestValue = evaluateFunction.evaluate(globalBest);
      System.out.println("-----------------------------------------------------------");
      System.out.format("iter %d: %d\n", iter, evaluateFunction.evaluate(globalBest));
      System.out.println("-----------------------------------------------------------");
      int counter = 0;
      for (Solution neighbor : neighborhood.neighbors(globalBest)) {
        if(neighbor != null) {
          int neighborValue = evaluateFunction.evaluate(neighbor);
          if(neighborValue > bestValue) {
            globalBest = neighbor;
            bestValue = neighborValue;
            counter++;
            break;
          }
        }
      }
      if (counter == 0)
        return globalBest;

      iter++;
      values.add(bestValue);
    }

    return globalBest;
  }
}
