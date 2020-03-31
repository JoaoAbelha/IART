package genetic_algorithm.mutation_operators;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import common.Solution;
import genetic_algorithm.Population;
import model.Car;
import model.Ride;
import genetic_algorithm.GeneticAlgorithm;

public class AssignMutation implements MutationOperator<Population> {
  private final double MUTATION_RATE = 0.001;

  @Override
  public Population mutate(Population population) {
    // Initialize new population
    Population newPopulation = new Population();

    // Loop over current population by fitness
    for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
      Solution solution = population.getFittest(populationIndex);

      // Skip mutation if this is an elite individual
      if (populationIndex >= GeneticAlgorithm.ELITISM_COUNT) {
        int size = solution.getState().size();
        // Loop over individual's genes
        for (int geneIndex = 0; geneIndex < size; geneIndex++) {
          // mutate gene with mutationRate
          if (MUTATION_RATE > Math.random()) {
            // flip random gene
            int newGenePos = (int) (Math.random() * solution.getUnassignedRides().size());
            Car car = solution.getState().get(geneIndex);
            this.flipGene(solution, car, newGenePos);
          }
        }
      }

      // Add individual to population
      newPopulation.addIndividual(solution);
    }

    // Return mutated population
    return newPopulation;
  }

  private void flipGene(Solution solution, Car car, int newGenePos) {
    if ((solution.getUnassignedRides().size() > 0) && (Math.random() > 0.2)) {
      this.addUnassignedRide(car.getAssignedRides(), solution.getUnassignedRides(), newGenePos);
    } else if(car.getAssignedRides().size() > 0){
      car.getAssignedRides().remove(newGenePos);
    }
  }

  private void addUnassignedRide(List<Ride> assignedRides, List<Ride> unassignedRides, int newGenePos) {
    Ride ride = unassignedRides.get(newGenePos);
    assignedRides.add(ride);
    Collections.sort(assignedRides, Comparator.comparingInt(Ride::getEarliestStart));
  }
  
}