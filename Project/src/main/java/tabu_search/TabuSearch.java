package tabu_search;

import common.Algorithm;
import common.Solution;
import common.evaluate_function.EvaluateFunction;
import common.neighborhood.Neighborhood;

public class TabuSearch implements Algorithm<Solution> {
    final private int TENURE = 10;
    final private int MAX_ITERATIONS = 100000;
    private TabuList<Solution> tabuList = new TabuList<>();
    private EvaluateFunction<Solution> evaluateFunction;
    private Neighborhood<Solution> neighborhood;

    public TabuSearch(EvaluateFunction<Solution> evaluateFunction, Neighborhood<Solution> neighborhood) {
        this.evaluateFunction = evaluateFunction;
        this.neighborhood = neighborhood;
    }

    @Override
    public Solution solve(Solution initialSolution) {
        int iter = 0;
        tabuList.clear();
		Solution localBest = initialSolution;
		Solution globalBest = initialSolution;
		while (iter < MAX_ITERATIONS) {
			System.out.format("iter %d: %d\n", iter, evaluateFunction.evaluate(globalBest));
			Solution bestNeighbor = null;
			for (Solution neighbor : neighborhood.neighbors(localBest)) {
				if (!tabuList.contains(neighbor)) {
					if (neighbor == null ||
							((neighbor != null) && evaluateFunction.evaluate(neighbor) < evaluateFunction.evaluate(localBest))) {
						bestNeighbor = neighbor;
					}
				}
			}
			if (bestNeighbor != null) {
				// System.out.println(evalFunc.evaluate(bestNeighbor));
				localBest = bestNeighbor;
				tabuList.add(localBest, TENURE);
				if (evaluateFunction.evaluate(localBest) <= evaluateFunction.evaluate(globalBest)) {
					globalBest = localBest;
				}
			}
			tabuList.update();
			iter++;
		}
			
		return globalBest;
    }
}
