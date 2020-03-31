package simulated_annealing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import common.Algorithm;
import common.Solution;
import common.evaluate_function.EvaluateFunction;
import common.neighborhood.Neighborhood;

public class SimulatedAnnealing extends Algorithm<Solution> {
  final double TEMPERATURE_INITIAL = 500000;
  private EvaluateFunction<Solution> evaluateFunction;
  private Neighborhood<Solution> neighborhood;
  double coolingRate = 0.001;

  /**
   * Simulated Annealing constructor
   *
   * @param evaluateFunction
   * @param neighborhood
   */
  public SimulatedAnnealing(EvaluateFunction<Solution> evaluateFunction, Neighborhood<Solution> neighborhood) {
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
    double temperature = TEMPERATURE_INITIAL;
    Solution globalBest = initialSolution;
    
    while (temperature > 1) {
      List<Solution> neighborhoodList = new ArrayList<>();
      int globalSolution = evaluateFunction.evaluate(globalBest);
      
      for (Solution neighbor : neighborhood.neighbors(globalBest)) {
        neighborhoodList.add(neighbor);
      }

      if (neighborhoodList.size() > 0) {
        int random = new Random().nextInt(neighborhoodList.size());
        Solution neighbor = neighborhoodList.get(random);
        if(neighbor != null) {
          int neighborSolution = evaluateFunction.evaluate(neighbor);
          if(neighborSolution > globalSolution) {
            globalBest = neighbor;
            globalSolution = neighborSolution;
          } else {
            int delta = globalSolution - neighborSolution;
            double randomDouble = new Random().nextDouble();
            if(randomDouble < Math.exp(-delta/temperature)) {
              globalBest = neighbor;
              globalSolution = neighborSolution;
            }
          }
        }
      }

      temperature *= (1 - coolingRate);
      iteration++;
      values.add(globalSolution);
    }

    return globalBest;
  }
}
