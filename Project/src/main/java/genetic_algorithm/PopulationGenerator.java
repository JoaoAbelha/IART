package genetic_algorithm;

import common.Problem;
import common.initial_solution.RandomSolutionGenerator;
import common.Solution;
import model.Car;
import model.Position;
import model.Ride;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopulationGenerator {
    private int populationSize;

    /**
     * PopulationGenerator constructor
     * @param populationSize
     */
    public PopulationGenerator(int populationSize) {
        this.populationSize = populationSize;
    }

    /**
     * generate initial population
     * @param problem
     * @return
     */
    public Population generate(Problem problem) {
        //Generate random valid solutions

        Population population = new Population(this.populationSize);

        // Loop over population size
        for (int individualCount = 0; individualCount < populationSize; individualCount++) {
            // Create individual
            Individual individual = generateValidIndividual(problem);
            // Add individual to population
            population.setIndividual(individualCount, individual);
        }

        return population;
    }

    /**
     * generate a valid individual for the initial population
     * @param problem
     * @return
     */
    private Individual generateValidIndividual(Problem problem) {
        int vehiclesNo = problem.getCars().size();
        int ridesNo = problem.getRides().size();

        List<Car> state = new ArrayList<>(problem.getCars());
        List<Ride> nonAssignedRides = new ArrayList<>(problem.getRides()); // non assigned rides
        Solution solution = new Solution(state, nonAssignedRides);

        for (int i = 0; i < ridesNo; i++) {
            int vehicleID = new Random().nextInt(vehiclesNo);
            Ride ride = problem.getRides().get(i);

            if (!(solution.getState().get(vehicleID).getAssignedRides().contains(ride))) {
                if (validAssignment(solution, vehicleID, ride)) {
                    solution.getUnassignedRides().remove(ride);
                }
            }
        }

        int[] chromosome = new int[vehiclesNo * ridesNo];
        int counter = 0;
        for (int i = 0; i < solution.getState().size(); i++) {
            for (int j = 0; j < problem.getRides().size(); j++) {
                if(solution.getState().get(i).getAssignedRides().contains(problem.getRides().get(j)))
                    chromosome[counter++] = 1;
                else chromosome[counter++] = 0;
            }
        }
        Individual individual = new Individual(chromosome);

        return individual;
    }

    /**
     * verify possible ride assignment
     * @param solution
     * @param vehicleID
     * @param unassignedRide
     * @return
     */
    private boolean validAssignment(Solution solution, int vehicleID, Ride unassignedRide) {
        //try add ride
        solution.getState().get(vehicleID).getAssignedRides().add(unassignedRide);

        if(!solution.isValid()) {
            solution.getState().get(vehicleID).getAssignedRides().remove(unassignedRide);
            return false;
        }

        return true;
    }

}