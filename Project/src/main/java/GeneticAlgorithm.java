import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm  extends Algorithm {
    private final int tournamentSize;
    private final int chromosomeLength;
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;
    private int maxGenerations;
    private Population population;



    public boolean isTerminationConditionMet(int generationsCount) {
        return (generationsCount >= this.maxGenerations);
    }

    public GeneticAlgorithm(int populationSize, int chromosomeLength, double mutationRate, double crossoverRate, int elitismCount,
                            int tournamentSize, int maxGenerations) {
        super();
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
        this.maxGenerations = maxGenerations;
        this.chromosomeLength = chromosomeLength;
    }

    @Override
    public void solve() {
        this.population = initPopulation(chromosomeLength);

        evaluatePopulation(this.population);
        int generationsCounter = 0;

        while(!isTerminationConditionMet(generationsCounter++)) {
            this.population = crossoverPopulation(this.population);
            this.population = mutatePopulation(this.population);
            evaluatePopulation(this.population);
            // System.out.println("Iteration: " + generationsCounter + "\nTotal Points: " + currentValue + "\n");
            iteration++;
            values.add(currentValue);
        }


        Individual individual = this.population.getFittest(0);
        int[] chromosome = individual.getChromosome();

        int vehicle = 0;
        for (int i = 0; i < chromosome.length; i += allRides.size()) {
            int[] rides = Arrays.copyOfRange(chromosome, i, i + allRides.size());
            state[vehicle++] = rides;
        }

        if (validState(state))
            System.out.println("valid state: " + evaluate(state));
        else
            System.out.println("not valid state");

    }

    @Override
    public int[][] getNextState(int[][] state) {
        return new int[0][];
    }

    private void evaluatePopulation(Population population) {
        Individual[] individuals = population.getIndividuals();

        for (Individual individual : individuals) {
            int[][] state = new int[cars.size()][allRides.size()];
            int[] chromosome = individual.getChromosome();
            int vehicle = 0;

            for (int i = 0; i < chromosome.length; i += allRides.size()) {
                int[] rides = Arrays.copyOfRange(chromosome, i, i + allRides.size());
                state[vehicle++] = rides;
            }
            int fitness;
            if (validState(state))
                fitness = evaluate(state);
            else
                fitness = -1;

            individual.setFitness(fitness);
        }
    }

    /**
     * Initialize population
     *
     * @param chromosomeLength The length of the individuals chromosome
     * @return population The initial population generated
     */
    public Population initPopulation(int chromosomeLength){
        //Generate random valid solutions

        Population population = new Population(populationSize);

        // Loop over population size
        for (int individualCount = 0; individualCount < populationSize; individualCount++) {
            // Create individual
            Individual individual = generateValidIndividual();
            // Add individual to population
            population.setIndividual(individualCount, individual);
        }

        return population;
    }

    public Individual generateValidIndividual() {
        List<Ride> nonAssignedRides = new ArrayList<>(allRides); // non assigned rides
        Random rand = new Random();
        int vehiclesNo = cars.size();
        int ridesNo = allRides.size();
        int[][] individualState = new int[vehiclesNo][ridesNo];
        for (int i = 0; i < ridesNo; i++) {
            int vehicleID = rand.nextInt(vehiclesNo);

            if (individualState[vehicleID][i] == 0) {
                if (validAssignment(vehicleID, i)) {
                    individualState[vehicleID][i] = 1;
                    nonAssignedRides.remove(allRides.get(i));

                }
            }
        }

        int[] chromosome = new int[vehiclesNo * ridesNo];
        int counter = 0;
        for (int i = 0; i < individualState.length; i++) {
            for (int j = 0; j < individualState[0].length; j++) {
                chromosome[counter++] = individualState[i][j];
            }
        }
        Individual individual = new Individual(chromosome);

        return individual;
    }

    /**
     * Selects parent for crossover using tournament selection
     *
     * @param population
     * @return The individual selected as a parent
     */
    public Individual selectParent(Population population) {
        // Create tournament
        Population tournament = new Population(this.tournamentSize);

        // Add random individuals to the tournament
        population.shuffle();
        for (int i = 0; i < this.tournamentSize; i++) {
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

    public Population crossoverPopulation(Population population){
        // Create new population
        Population newPopulation = new Population(population.size());

        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            // Get parent1
            Individual parent1 = population.getFittest(populationIndex);
            if (populationIndex == 0)
                super.currentValue = parent1.getFitness();

            // Apply crossover to the individual if it is not Elite
            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
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

    public Population mutatePopulation(Population population){
        // Initialize new population
        Population newPopulation = new Population(this.populationSize);

        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);

            // Skip mutation if this is an elite individual
            if (populationIndex >= this.elitismCount) {
                // Loop over individual's genes
                for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                    // mutate gene with mutationRate
                    if (this.mutationRate > Math.random()) {
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
