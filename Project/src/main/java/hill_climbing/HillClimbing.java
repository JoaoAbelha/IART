package hill_climbing;

import common.Algorithm;
import common.Solution;
import common.evaluate_function.EvaluateFunction;
import common.neighborhood.Neighborhood;

public class HillClimbing extends Algorithm<Solution> {
  final private int MAX_ITERATIONS = 100000;
  private EvaluateFunction<Solution> evaluateFunction;
  private Neighborhood<Solution> neighborhood;

  /**
   * Hill Climbing constructor
   *
   * @param evaluateFunction
   * @param neighborhood
   */
  public HillClimbing(EvaluateFunction<Solution> evaluateFunction, Neighborhood<Solution> neighborhood) {
    super();
    this.evaluateFunction = evaluateFunction;
    this.neighborhood = neighborhood;
  }

  /**
   * Optimizes Solution
   *
   * @param initialSolution to optimize
   * @return optimized solution
   */
  @Override
  public Solution solve(Solution initialSolution) {
    Solution globalBest = initialSolution;
    values.add(evaluateFunction.evaluate(globalBest));
    while (iteration < MAX_ITERATIONS) {
      int bestValue = evaluateFunction.evaluate(globalBest);
      System.out.println("-----------------------------------------------------------");
      System.out.format("iter %d: %d\n", iteration, evaluateFunction.evaluate(globalBest));
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

      iteration++;
      values.add(bestValue);
    }

    return globalBest;
  }
}
