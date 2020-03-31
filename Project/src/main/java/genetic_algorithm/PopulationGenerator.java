package genetic_algorithm;

import common.Problem;
import model.Position;
import model.Ride;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopulationGenerator {
    private int populationSize;

    public PopulationGenerator(int populationSize) {
        this.populationSize = populationSize;
    }

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

    private Individual generateValidIndividual(Problem problem) {
        List<Ride> nonAssignedRides = new ArrayList<>(problem.getRides()); // non assigned rides
        Random rand = new Random();
        int vehiclesNo = problem.getCars().size();
        int ridesNo = problem.getRides().size();
        int[][] individualState = new int[vehiclesNo][ridesNo];
        for (int i = 0; i < ridesNo; i++) {
            int vehicleID = rand.nextInt(vehiclesNo);

            if (individualState[vehicleID][i] == 0) {
                if (validAssignment(vehicleID, i, problem)) {
                    individualState[vehicleID][i] = 1;
                    nonAssignedRides.remove(problem.getRides().get(i));

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

  private boolean validAssignment(int vehicleID, int rideID, Problem problem) {
    int previousRideID = getPreviousRide(vehicleID, rideID, problem);
    int nextRideID = getNextRide(vehicleID, rideID);

    Ride actualRide = problem.getRides().get(rideID);

    if (previousRideID != -1) {
      Ride previousRide = problem.getRides().get(previousRideID);
      Position previousPos = previousRide.getEnd();
      Position currentRideStart = actualRide.getStart();

      int latestStart = actualRide.getLastestFinish() - actualRide.getDistance();

      if (previousPos.getDistanceTo(currentRideStart) > latestStart)
        return false;
    }

    if (nextRideID != -1) {
      Ride nextRide = problem.getRides().get(nextRideID);
      Position currentPos = actualRide.getEnd();
      Position nextRideStart = nextRide.getStart();

      int latestStart = nextRide.getLastestFinish() - nextRide.getDistance();

      if (currentPos.getDistanceTo(nextRideStart) > latestStart) {
        return false;
      }
    }

    return true;
  }

  private int getPreviousRide(int vehicleID, int rideID, Problem problem) {
    int[] vehicleRides = state[vehicleID];
    for (int i = rideID; i > 0; i--) {
      if (vehicleRides[i] == 1)
        return i;
    }
    return -1;
  }

  private int getNextRide(int vehicleID, int rideID) {
    int[] vehicleRides = state[vehicleID];
    for (int i = rideID; i > vehicleRides.length; i++) {
      if (vehicleRides[i] == 1)
        return i;
    }
    return -1;
  }

}