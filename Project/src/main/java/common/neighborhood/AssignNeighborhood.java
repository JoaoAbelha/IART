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
      currJ = 0;
      currM = 0;
      this.solution = solution;
    }

    @Override
    public boolean hasNext() {
      if (currJ < solution.getUnassignedRides().size()) {
        return true;
      } else {
        return false;
      }
    }

    @Override
    public Solution next() {
      Solution neighbor = solution.clone();
      boolean assigned = this.assignRide(neighbor);

      // update neighborhood index
      if(currM + 1 > neighbor.getState().get(currI).getAssignedRides().size() - 1) {
        currI++;
        currM = 0;
      } else {
        currM++;
      }
      if(currI + 1 > neighbor.getState().size() - 1) {
        currJ++;
        currI = 0;
      }

      if(assigned) {
        return neighbor;
      } else return null;
    }

    private boolean assignRide(Solution neighbor) {
      Ride unassignedRide = neighbor.getUnassignedRides().get(currJ);
      List<Ride> currCarRides = neighbor.getState().get(currI).getAssignedRides();
      currCarRides.add(currM, unassignedRide);

      if(neighbor.isValid()) {
        neighbor.getUnassignedRides().remove(unassignedRide);
        return true;
      } else {
        currCarRides.remove(unassignedRide);
        return false;
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