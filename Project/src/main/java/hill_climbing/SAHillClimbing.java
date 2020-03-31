package hill_climbing;

import common.Algorithm;
import common.Solution;
import common.evaluate_function.EvaluateFunction;
import common.neighborhood.Neighborhood;

public class SAHillClimbing extends Algorithm<Solution> {
  final private int MAX_ITERATIONS = 100000;
  private EvaluateFunction<Solution> evaluateFunction;
  private Neighborhood<Solution> neighborhood;

  public SAHillClimbing(EvaluateFunction<Solution> evaluateFunction, Neighborhood<Solution> neighborhood) {
    this.evaluateFunction = evaluateFunction;
    this.neighborhood = neighborhood;
  }

  @Override
  public Solution solve(Solution initialSolution) {
    int iter = 0;
    Solution globalBest = initialSolution;
    while (iter < MAX_ITERATIONS) {
      int bestValue = evaluateFunction.evaluate(globalBest);
      System.out.format("iter %d: %d\n", iter, bestValue);
      Solution bestNeighbor = null;
      for (Solution neighbor : neighborhood.neighbors(globalBest)) {
        if (bestNeighbor == null) {
          bestNeighbor = neighbor;
        } else if ((neighbor != null)
            && evaluateFunction.evaluate(neighbor) > evaluateFunction.evaluate(bestNeighbor)) {
          bestNeighbor = neighbor;
        }
      }
      if (bestNeighbor != null) {
        int bestNeighborValue = evaluateFunction.evaluate(bestNeighbor);
        if (bestNeighborValue > bestValue) {
          globalBest = bestNeighbor;
          bestValue = bestNeighborValue;
        }
      }
      iter++;
      values.add(bestValue);
    }

    return globalBest;
  }
}
