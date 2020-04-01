package genetic_algorithm;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import common.Solution;
import model.Car;
import model.Ride;

public class MutationOperator {
    private final double MUTATION_RATE = 0.001;

    /**
     * mutate the population
     * @param population
     * @param populationSize
     * @return
     */
    public Population mutatePopulation(Population population, int populationSize) {
        // Initialize new population
        Population newPopulation = new Population(populationSize);

        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);

            // Skip mutation if this is an elite individual
            if (populationIndex >= GeneticAlgorithm.ELITISM_COUNT) {
                // Loop over individual's genes
                for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                    // mutate gene with mutationRate
                    if (MUTATION_RATE > Math.random()) {
                        // flip random gene
                        int newGenePos = (int) (Math.random() * individual.getChromosomeLength());
                        individual.flipGene(newGenePos);
                    }
                }
            }

            // Add individual to population
            newPopulation.setIndividual(populationIndex, individual);
        }

        // Return mutated population
        return newPopulation;
    }
}