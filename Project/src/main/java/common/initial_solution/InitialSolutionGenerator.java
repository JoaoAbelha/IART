package common.initial_solution;

import common.Problem;

public interface InitialSolutionGenerator<T> {
  public T initialSolution(Problem problem);
}