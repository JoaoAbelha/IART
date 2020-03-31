package genetic_algorithm;

import common.Problem;
import common.Solution;
import common.initial_solution.InitialSolutionGenerator;

public class PopulationGenerator {
  private InitialSolutionGenerator<Solution> initialSolutionGenerator;
  private int populationSize;

  public PopulationGenerator(InitialSolutionGenerator<Solution> initialSolutionGenerator, int populationSize) {
    this.initialSolutionGenerator = initialSolutionGenerator;
    this.populationSize = populationSize;
  }

  public Population generate(Problem problem) {
    Population population = new Population();

    while(population.size() < populationSize) {
      population.addIndividual(initialSolutionGenerator.initialSolution(problem));
    }

    return population;
  }
}