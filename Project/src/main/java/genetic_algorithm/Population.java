package genetic_algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import common.Solution;
import common.evaluate_function.EvaluateFunction;
import common.evaluate_function.PointsEvaluator;

public class Population {
    private SortedSet<Solution> population;
    private EvaluateFunction<Solution> evaluateFunction = new PointsEvaluator();
    private double populationFitness = -1;

    public Population() {
        Comparator<Solution> comparator = (ps1, ps2) -> evaluateFunction.evaluate(ps1)
					- evaluateFunction.evaluate(ps2);
        this.population = new TreeSet<>(comparator);
    }

    /**
     * Get individuals from the population
     *
     * @return individuals Individuals in population
     */
    public SortedSet<Solution> getIndividuals() {
        return this.population;
    }

	public void addIndividual(Solution individual) {
        this.population.add(individual);
	}

    /**
     * Find fittest individual in the population
     *
     * @param offset
     * @return individual Fittest individual at offset
     */
    public Solution getFittest(int offset) {
        // Return the fittest individual
        return new ArrayList<>(this.population).get(offset);
    }

    /**
     * Set population's fitness
     *
     * @param fitness The population's total fitness
     */
    public void setPopulationFitness(double fitness) {
        this.populationFitness = fitness;
    }

    /**
     * Get population's fitness
     *
     * @return populationFitness The population's total fitness
     */
    public double getPopulationFitness() {
        return this.populationFitness;
    }

    /**
     * Get population's size
     *
     * @return size The population's size
     */
    public int size() {
        return this.population.size();
    }
}