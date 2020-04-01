package hill_climbing;

import common.Algorithm;
import common.Solution;
import common.evaluate_function.EvaluateFunction;
import common.neighborhood.AssignNeighborhood;
import common.neighborhood.Neighborhood;
import common.neighborhood.SwapNeighborhood;

public class SAHillClimbing extends Algorithm<Solution> {
  final private int MAX_ITERATIONS = 100000;
  private EvaluateFunction<Solution> evaluateFunction;
  private Neighborhood<Solution> neighborhood;
  private Neighborhood<Solution> neighborhood2 = null;


  /**
   * Steepeset Ascent Hill Climbing constructor
   *
   * @param evaluateFunction
   * @param neighborhood
   */
  public SAHillClimbing(EvaluateFunction<Solution> evaluateFunction, Neighborhood<Solution> neighborhood) {
    this.evaluateFunction = evaluateFunction;
    this.neighborhood = neighborhood;
    if (neighborhood == null) {
      this.neighborhood = new AssignNeighborhood();
      neighborhood2 = new SwapNeighborhood();
    }
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
    while (iteration < MAX_ITERATIONS) {
      int bestValue = evaluateFunction.evaluate(globalBest);
      //System.out.format("iter %d: %d\n", iteration, bestValue);
      Solution bestNeighbor = null;
      for (Solution neighbor : neighborhood.neighbors(globalBest)) {
        if (bestNeighbor == null) {
          bestNeighbor = neighbor;
        } else if ((neighbor != null)
            && evaluateFunction.evaluate(neighbor) > evaluateFunction.evaluate(bestNeighbor)) {
          bestNeighbor = neighbor;
        }
      }
      if (bestNeighbor == null && neighborhood2 != null) {
        for (Solution neighbor : neighborhood2.neighbors(globalBest)) {
          if (bestNeighbor == null) {
            bestNeighbor = neighbor;
          } else if ((neighbor != null)
                  && evaluateFunction.evaluate(neighbor) > evaluateFunction.evaluate(bestNeighbor)) {
            bestNeighbor = neighbor;
          }
        }
      }


      if (bestNeighbor != null) {
        int bestNeighborValue = evaluateFunction.evaluate(bestNeighbor);
        if (bestNeighborValue > bestValue) {
          globalBest = bestNeighbor;
          bestValue = bestNeighborValue;
        } else
          return globalBest;
      } else
        return globalBest;

      iteration++;
      values.add(bestValue);
    }

    return globalBest;
  }
}
