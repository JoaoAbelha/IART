package genetic_algorithm;

import java.util.Arrays;

public class CrossoverOperator {
    private final int TOURNAMENT_SIZE = 5;

    /**
     * Selects parent for crossover using tournament selection
     *
     * @param population
     * @return The individual selected as a parent
     */
    public Individual selectParent(Population population) {
        // Create tournament
        Population tournament = new Population(TOURNAMENT_SIZE);

        // Add random individuals to the tournament
        population.shuffle();
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            Individual tournamentIndividual = population.getIndividual(i);
            tournament.setIndividual(i, tournamentIndividual);
        }

        // Return the best
        return tournament.getFittest(0);
    }

    /**
     * Crossover population
     *
     * @param population
     * @return The new population
     */

    public Population crossoverPopulation(Population population, int currentValue){
        // Create new population
        Population newPopulation = new Population(population.size());

        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            // Get parent1
            Individual parent1 = population.getFittest(populationIndex);
            if (populationIndex == 0)
                currentValue = parent1.getFitness();

            // Apply crossover to the individual if it is not Elite
            if (GeneticAlgorithm.CROSSOVER_RATE > Math.random() && populationIndex >= GeneticAlgorithm.ELITISM_COUNT) {
                // Find parent2 with tournament selection
                Individual parent2 = this.selectParent(population);

                // Create empty chromosome
                int emptyChromosome[] = new int[parent1.getChromosomeLength()];
                Arrays.fill(emptyChromosome, -1);
                Individual child = new Individual(emptyChromosome);

                // Get subset of parent chromosomes
                int intitPos = (int) (Math.random() * parent1.getChromosomeLength());
                int finalPos = (int) (Math.random() * parent1.getChromosomeLength());

                // make the smaller the start and the larger the end
                final int startSubstr = Math.min(intitPos, finalPos);
                final int endSubstr = Math.max(intitPos, finalPos);

                // add parent2 genes to the child
                for (int i = 0; i < parent1.getChromosomeLength(); i++) {
                    child.setGene(i, parent2.getGene(i));
                }

                // add parent1 genes to the child
                for (int i = startSubstr; i < endSubstr; i++) {
                    child.setGene(i, parent1.getGene(i));
                }

                // Add child
                newPopulation.setIndividual(populationIndex, child);
            } else {
                // Add individual to new population without applying crossover
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }
        return newPopulation;
    }
}
