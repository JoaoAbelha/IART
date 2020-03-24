import java.util.Random;

public class Individual {

    private int[] chromosome;
    private double fitness = -1;

    /**
     * Initializes individual with specific chromosome
     *
     * @param chromosome The chromosome to give individual
     */
    public Individual(int[] chromosome) {
        // Create individual chromosome
        this.chromosome = chromosome;
    }

    /**
     * Initializes random individual
     *
     * @param chromosomeLength The length of the individuals chromosome
     */
    public Individual(int chromosomeLength) {
        // Create random individual
        int[] individual;
        individual = new int[chromosomeLength];

        Random rand = new Random();
        for (int gene = 0; gene < chromosomeLength; gene++) {
            if(0.001 > Math.random())
                individual[gene] = 1;
            else
                individual[gene] = 0;
        }

        this.chromosome = individual;
    }

    /**
     * Gets individual's chromosome
     *
     * @return The individual's chromosome
     */
    public int[] getChromosome() {
        return this.chromosome;
    }

    /**
     * Gets individual's chromosome length
     *
     * @return The individual's chromosome length
     */
    public int getChromosomeLength() {
        return this.chromosome.length;
    }

    /**
     * Set gene at offset
     *
     * @param gene
     * @param offset
     */
    public void setGene(int offset, int gene) {
        this.chromosome[offset] = gene;
    }

    /**
     * Store individual's fitness
     *
     * @param fitness
     *            The individuals fitness
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Gets individual's fitness
     *
     * @return The individual's fitness
     */
    public double getFitness() {
        return this.fitness;
    }


    public String toString() {
        String output = "";
        for (int gene = 0; gene < this.chromosome.length; gene++) {
            output += this.chromosome[gene] + ",";
        }
        return output;
    }

    /**
     * Search for a specific integer gene in this individual.
     *
     * For instance, in a Traveling Salesman Problem where cities are encoded as
     * integers with the range, say, 0-99, this method will check to see if the
     * city "42" exists.
     *
     * @param gene
     * @return
     */
    public boolean containsGene(int gene) {
        for (int i = 0; i < this.chromosome.length; i++) {
            if (this.chromosome[i] == gene) {
                return true;
            }
        }
        return false;
    }

    public int getGene(int i) {
        return this.chromosome[i];
    }

    public void flipGene(int i) {
        this.chromosome[i] ^= 1;
    }
}
