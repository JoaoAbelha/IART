package common.neighborhood;

import java.util.Iterator;
import java.util.List;

import common.Solution;
import model.Position;
import model.Ride;

public class AssignNeighborhood implements Neighborhood<Solution> {
  @Override
  public Iterable<Solution> neighbors(Solution solution) {
    return new IterableNeighborhood(solution);
  }

  private class NeighborhoodItarator implements Iterator<Solution> {
    private int currI;
    private int currJ;
    private int currM;
    private final Solution solution;

    public NeighborhoodItarator(Solution solution) {
      currI = 0;
      currJ = 1;
      currM = 0;
      this.solution = solution;
    }

    @Override
    public boolean hasNext() {
      if (currI < solution.getState().size() - 1) {
        return true;
      } else {
        return false;
      }
    }

    @Override
    public Solution next() {
      Solution neighbor = solution.clone();

      Ride unassignedRide = solution.getUnassignedRides().get(currJ);
      List<Ride> currCarRides = solution.getState().get(currI).getAssignedRides();
      this.assignRide(currCarRides, unassignedRide, currM);

      // update neighborhood index
      if (currJ + 1 > solution.getState().size() - 1) {
        currI++;
        currJ = 0;
      } else if(currM + 1 > currCarRides.size() - 1) {
        currJ++;
        currM = 0;
      } else {
        currM++;
      }

      return neighbor;
    }

    private void assignRide(List<Ride> currCarRides, Ride unassignedRide, int currM) {
      for(; currM < currCarRides.size(); ++currM) {
        if(validAssignment(currCarRides, unassignedRide, currM)) {
          currCarRides.add(currM, unassignedRide);
          return;
        }
      }
    }

    private boolean validAssignment(List<Ride> currCarRides, Ride unassignedRide, int currM) {
      Ride previousRide = getPreviouRide(currM, currCarRides);
      Ride nextRide = getNextRide(currM, currCarRides);

      if (previousRide != null) {
        Position previousPos = previousRide.getEnd();
        Position currentRideStart = unassignedRide.getStart();

        int latestStart = unassignedRide.getLastestFinish() - unassignedRide.getDistance();

        if (previousPos.getDistanceTo(currentRideStart) > latestStart)
          return false;
      }

      if (nextRide != null) {
        Position currentPos = unassignedRide.getEnd();
        Position nextRideStart = nextRide.getStart();

        int latestStart = nextRide.getLastestFinish() - nextRide.getDistance();

        if (currentPos.getDistanceTo(nextRideStart) > latestStart) {
          return false;
        }
      }

      return true;
    }

    private Ride getPreviouRide(int index, List<Ride> carRides) {
      if(index > 0) {
        return carRides.get(index - 1);
      } else return null;
    }

    private Ride getNextRide(int index, List<Ride> carRides) {
      if(index < carRides.size() - 1) {
        return carRides.get(index + 1);
      } else return null;
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
  /*
   * @Override public Solution neighbors(Solution solution) {
   * 
   * List<Ride> unassignedRides = solution.getUnassignedRides();
   * 
   * for (Ride ride : unassignedRides) { int rideID = ride.id; for (int i = 0; i <
   * state.length; i++) { int[] car = state[i]; if (car[rideID - 1] == 0) { if
   * (validAssignment(i, rideID - 1)) { state[i][rideID - 1] = 1;
   * rides.remove(ride);
   * 
   * // create new node (state) // int newNodeId = ++nodeCounter; //
   * graph.addNode(Integer.toString(newNodeId)); //
   * graph.addEdge(Integer.toString(newNodeId), Integer.toString(newNodeId), //
   * Integer.toString(currentNode)); // currentNode = newNodeId;
   * 
   * return state; } } } } System.out.println("Did not found new state"); return
   * null; }
   */
}