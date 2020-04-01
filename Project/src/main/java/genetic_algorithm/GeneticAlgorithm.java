package genetic_algorithm;

import common.Algorithm;
import common.Problem;
import common.Solution;
import model.Car;
import model.Position;
import model.Ride;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneticAlgorithm extends Algorithm<Solution> {
    public static final double CROSSOVER_RATE = 0.7;
    public static final int ELITISM_COUNT = 5;
    private final int maxGenerations = 200;
    private final int populationSize;
    public int currentValue = 0;

    private Population population;
    private Problem problem;
    private CrossoverOperator crossoverOperator = new CrossoverOperator();
    private MutationOperator mutationOperator = new MutationOperator();

    /**
     * GeneticAlgorithm constructor
     * @param populationSize
     * @param problem
     */
    public GeneticAlgorithm(int populationSize, Problem problem) {
        super();
        this.populationSize = populationSize;
        this.problem = problem;
    }

    /**
     * set the population
     * @param population
     */
    public void setPopulation(Population population) {
        this.population = population;
    }

    /**
     * check terminal condition
     * @param generationsCount
     * @return
     */
    public boolean isTerminationConditionMet(int generationsCount) {
        return (generationsCount >= this.maxGenerations);
    }

    /**
     * Optimizes Solution
     *
     * @param initialSolution to optimize
     * @return optimized solution
     */
    @Override
    public Solution solve(Solution initialSolution) {
        evaluatePopulation(this.population);
        int generationsCounter = 0;

        while(!isTerminationConditionMet(generationsCounter++)) {
            Object[] temp = crossoverOperator.crossoverPopulation(this.population);
            this.population = (Population) temp[0];
            this.currentValue = (Integer) temp[1];
            this.population = mutationOperator.mutatePopulation(this.population, populationSize);
            evaluatePopulation(this.population);
            iteration++;
            values.add(currentValue);
        }


        Individual individual = this.population.getFittest(0);
        int[] chromosome = individual.getChromosome();
        int[][] state = new int[problem.getCars().size()][problem.getRides().size()];
        int vehicle = 0;
        for (int i = 0; i < chromosome.length; i += problem.getRides().size()) {
            int[] rides = Arrays.copyOfRange(chromosome, i, i + problem.getRides().size());
            state[vehicle++] = rides;
        }

        List<Car> cars = problem.getCars();
        List<Ride> unassigndeRide = problem.getRides();
        for(int i = 0; i < problem.getCars().size(); ++i) {
            List<Ride> rides = new ArrayList<>();
            for(int j = 0; j < problem.getRides().size(); ++j) {
                if(state[i][j] == 1) {
                    rides.add(problem.getRides().get(j));
                    unassigndeRide.remove(problem.getRides().get(j));
                }
            }
            cars.get(i).setAssignedRides(rides);
        }
        return new Solution(cars, unassigndeRide);
    }

    /**
     * evaluates current population
     * @param population
     */
    private void evaluatePopulation(Population population) {
        Individual[] individuals = population.getIndividuals();

        for (Individual individual : individuals) {
            int[][] state = new int[problem.getCars().size()][problem.getRides().size()];
            int[] chromosome = individual.getChromosome();
            int vehicle = 0;

            for (int i = 0; i < chromosome.length; i += problem.getRides().size()) {
                int[] rides = Arrays.copyOfRange(chromosome, i, i + problem.getRides().size());
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
     * evaluate a individual state
     * @param state
     * @return
     */
    private int evaluate(int[][] state) {
        int points = 0;
        Ride ride = problem.getRides().get(0);
        for (int i = 0; i < state.length; i++) {
            int[] car = state[i];
            int time = 0;
            Position pos = new Position(0, 0);
            int j;
            for (j = 0; j < car.length - 1; j++) {
                if (car[j] == 1) {
                    int distance = problem.getRides().get(j).getDistance();
                    Position newPos = problem.getRides().get(j).getStart();
                    points += distance;
                    time += pos.getDistanceTo(newPos) + distance;
                    pos = newPos;

                    if (time <= problem.getRides().get(j + 1).getEarliestStart())
                        points += Problem.perRideBonus;
                }
            }

            // last ride
            if (car[j] == 1) {
                int distance = problem.getRides().get(j).getDistance();
                Position newPos = problem.getRides().get(j).getStart();
                points += distance;
                time += pos.getDistanceTo(newPos) + distance;
                pos = newPos;
            }
        }
        return points;
    }

    /**
     * validate individual state
     * @param state
     * @return
     */
    protected boolean validState(int[][] state) {
        int rideCounter = 0;
        for (int[] vehicleRides : state) {
            int[] rides = Arrays.stream(vehicleRides).filter(ride -> ride == 1).toArray();
            int time = 0;
            for (int i = 0; i < rides.length - 1; i++) {
                rideCounter++;
                Ride actualRide = problem.getRides().get(rides[i]);
                Ride nextRide = problem.getRides().get(rides[i + 1]);
                Position currentPos = actualRide.getEnd();
                Position nextRideStart = nextRide.getStart();

                time += actualRide.getDistance() + currentPos.getDistanceTo(nextRideStart);
                int latestStart = nextRide.getLastestFinish() - nextRide.getDistance();

                if (time > latestStart) {
                    return false;
                }
            }
        }
        if (rideCounter > problem.getAllRides().size())
            return false;
        System.out.println(rideCounter);

        return true;
    }
}