package genetic_algorithm;

import common.Algorithm;
import common.Solution;
import genetic_algorithm.crossover_operators.CrossoverOperator;
import genetic_algorithm.mutation_operators.MutationOperator;

public class GeneticAlgorithm extends Algorithm<Solution> {
    public static final double CROSSOVER_RATE = 0.7;
    public static final int ELITISM_COUNT = 5;

    private int maxGenerations;
    private Population population;
    private CrossoverOperator<Population> crossoverOperator;
    private MutationOperator<Population> mutationOperator;

    public GeneticAlgorithm(Population population, CrossoverOperator<Population> crossoverOperator, MutationOperator<Population> mutationOperator) {
        super();
        this.population = population;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
    }

    public boolean isTerminationConditionMet(int generationsCount) {
        return (generationsCount >= this.maxGenerations);
    }

    @Override
    public Solution solve(Solution initialSolution) {
        int generationsCounter = 0;
        while(!isTerminationConditionMet(generationsCounter++)) {
            this.population = this.crossoverOperator.reproduce(this.population);
            this.population = this.mutationOperator.mutate(this.population);
        }

        return this.population.getIndividuals().first();
    }
}