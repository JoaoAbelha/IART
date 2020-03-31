package tabu_search;

import common.Algorithm;
import common.Solution;
import common.evaluate_function.EvaluateFunction;
import common.neighborhood.Neighborhood;

public class TabuSearch extends Algorithm<Solution> {
	final private int TENURE = 10;
	final private int MAX_ITERATIONS = 100000;
	private TabuList<Solution> tabuList = new TabuList<>();
	private EvaluateFunction<Solution> evaluateFunction;
	private Neighborhood<Solution> neighborhood;

	public TabuSearch(EvaluateFunction<Solution> evaluateFunction, Neighborhood<Solution> neighborhood) {
		super();
		this.evaluateFunction = evaluateFunction;
		this.neighborhood = neighborhood;
	}

	@Override
	public Solution solve(Solution initialSolution) {
		tabuList.clear();
		Solution globalBest = initialSolution;
		while (iteration < MAX_ITERATIONS) {
			int bestValue = evaluateFunction.evaluate(globalBest);
			System.out.format("iter %d: %d\n", iteration, bestValue);
			Solution bestNeighbor = null;

			for (Solution neighbor : neighborhood.neighbors(globalBest)) {
				if (!tabuList.contains(neighbor)) {
					if (bestNeighbor == null) {
						bestNeighbor = neighbor;
					} else if ((neighbor != null)
							&& evaluateFunction.evaluate(neighbor) > evaluateFunction.evaluate(bestNeighbor)) {
						bestNeighbor = neighbor;
					}
				} else System.out.println("contains");
			}
			if (bestNeighbor != null) {
				tabuList.add(bestNeighbor, TENURE);
				int bestNeighborValue = evaluateFunction.evaluate(bestNeighbor);
				if (bestNeighborValue > bestValue) {
					globalBest = bestNeighbor;
					bestValue = bestNeighborValue;
				} else
					return globalBest;

			} else
				return globalBest;
			
			tabuList.update();
			values.add(bestValue);
			iteration++;
		}

		return globalBest;
	}
}
