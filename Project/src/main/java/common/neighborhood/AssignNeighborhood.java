package common.neighborhood;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import common.Problem;
import common.Solution;
import genetic_algorithm.GeneticAlgorithm;
import genetic_algorithm.Population;
import model.Car;
import model.Position;
import model.Ride;

public class AssignNeighborhood implements Neighborhood<Solution> {
    /**
     * solution neighborhood
     * @param solution
     * @return
     */
    @Override
    public Iterable<Solution> neighbors(Solution solution) {
        return new IterableNeighborhood(solution);
    }

    private class NeighborhoodItarator implements Iterator<Solution> {
        private final double UNASSIGN_PROBABILITY = 0.3333;
        private int currI;
        private int currJ;
        private final Solution solution;

        public NeighborhoodItarator(Solution solution) {
            currI = 0;
            currJ = 0;
            this.solution = solution;
        }

        @Override
        public boolean hasNext() {
            if (currI < solution.getState().size()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Solution next() {
            Solution neighbor = this.solution.clone();
            this.assignRide(neighbor);

            if(currJ + 1 > solution.getUnassignedRides().size() - 1) {
                currI++;
                currJ = 0;
            } else {
                currJ++;
            }

            return neighbor;
        }

        private void assignRide(Solution neighbor) {
            int assignedRidesSize = neighbor.getState().get(currI).getAssignedRides().size();
            if ((neighbor.getUnassignedRides().size() > 0) && (Math.random() > UNASSIGN_PROBABILITY)) {
                this.tryAssignRides(neighbor);
            } else if (assignedRidesSize > 0) {
                int random = (int) (Math.random() * assignedRidesSize);
                Ride ride = neighbor.getState().get(currI).getAssignedRides().remove(random);
                neighbor.getUnassignedRides().add(ride);
            }
        }

        private void tryAssignRides(Solution solution) {
            Car car = solution.getState().get(currI);
            Ride unassignedRide = solution.getUnassignedRides().get(currJ);
            int rand = (int) Math.random() * car.getAssignedRides().size();
            car.getAssignedRides().add(rand, unassignedRide);

            if(solution.isValid()) {
                solution.getUnassignedRides().remove(unassignedRide);
            } else {
                car.getAssignedRides().remove(unassignedRide);
            }
        }
    }


    private class IterableNeighborhood implements Iterable<Solution> {
        private final Solution solution;

        public IterableNeighborhood(Solution solution) {
            this.solution = solution;
        }

        @Override
        public Iterator<Solution> iterator() {
            return new NeighborhoodItarator(solution);

        }
    }
}