package genetic_algorithm.crossover_operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.Solution;
import genetic_algorithm.Population;
import model.Car;
import genetic_algorithm.GeneticAlgorithm;

public class RandomCrossover implements CrossoverOperator<Population> {
  private final int TOURNAMENT_SIZE = 5;

  @Override
  public Population reproduce(Population population) {
    // Create new population
    Population newPopulation = new Population();

    // Loop over current population by fitness
    for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
        // Get parent1
        Solution parent1 = population.getFittest(populationIndex);

        // Apply crossover to the individual if it is not Elite
        if (GeneticAlgorithm.CROSSOVER_RATE > Math.random() && populationIndex >= GeneticAlgorithm.ELITISM_COUNT) {
            // Find parent2 with tournament selection
            Solution parent2 = this.selectParent(population);
            Solution child = parent2.clone();
            int size = parent1.getState().size();

            // Get subset of parent chromosomes
            int intitPos = (int) (Math.random() * size);
            int finalPos = (int) (Math.random() * size);

            // make the smaller the start and the larger the end
            final int startSubstr = Math.min(intitPos, finalPos);
            final int endSubstr = Math.max(intitPos, finalPos);

            List<Car> childRemoveState = child.getState().subList(startSubstr, endSubstr);
            List<Car> parent1AddState = parent1.getState().subList(startSubstr, endSubstr);

            child.getState().removeAll(childRemoveState);
            child.getState().addAll(startSubstr, parent1AddState);

            // Add child
            newPopulation.addIndividual(child);
        } else {
            // Add individual to new population without applying crossover
            newPopulation.addIndividual(parent1);
        }
    }
    return newPopulation;
  }

  private Solution selectParent(Population population) {
    // Create tournament
    Population tournament = new Population();

    // Add random individuals to the tournament
    List<Solution> shuffleSolutions = new ArrayList<>(population.getIndividuals());
    Collections.shuffle(shuffleSolutions);
    for (int i = 0; i < TOURNAMENT_SIZE; i++) {
        Solution tournamentIndividual = shuffleSolutions.get(i);
        tournament.addIndividual(tournamentIndividual);
    }

    // Return the best
    return tournament.getFittest(0);
}
  
}