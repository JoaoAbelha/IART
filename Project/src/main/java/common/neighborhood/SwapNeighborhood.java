package common.neighborhood;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import common.Solution;
import model.Ride;
import model.Position;

public class SwapNeighborhood implements Neighborhood<Solution> {
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
    private int currI;
    private int currJ;
    private final Solution solution;

    public NeighborhoodItarator(Solution solution) {
      currI = 0;
      currJ = 1;
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
      //update neighborhood index
      if (currJ + 1 > solution.getState().size() - 1) {
        currI++;
        currJ = 0;
      } else {
        currJ++;
      }

      if(currJ == currI && currJ == solution.getState().size() - 1) {
        currJ = 0;
      } else {
        currJ++;
      }

      List<Ride> firstCarRides = neighbor.getState().get(currI).getAssignedRides();
      List<Ride> secondCarRides = neighbor.getState().get(currJ).getAssignedRides();



      if(firstCarRides.size() > 0 && secondCarRides.size() > 0) {
        //two random indexes
        int index1 = (int) (Math.random() * (firstCarRides.size() - 1));
        int index2 = (int) (Math.random() * (secondCarRides.size() - 1));
        this.swapRides(neighbor, index1, index2);
      }
      //verify second car rides

      return neighbor;
    }

    private void swapRides(Solution solution, int index1, int index2) {
      Ride firstCarRide = solution.getState().get(currI).getAssignedRides().get(index1);
      Ride secondCarRide = solution.getState().get(currJ).getAssignedRides().get(index2);

      solution.getState().get(currI).getAssignedRides().set(index1, secondCarRide);
      solution.getState().get(currJ).getAssignedRides().set(index2, firstCarRide);
    }

    private List<Ride> invalidRides(List<Ride> secondCarRides) {
      List<Ride> invalidRides = new ArrayList<>();
      int time = 0;

      for(int i = 0; i < secondCarRides.size() - 1; ++i) {
        Ride ride = secondCarRides.get(i);
        Ride nextRide = secondCarRides.get(i + 1);
        Position ridePos = ride.getEnd();
        Position nextRidePos = nextRide.getStart();

        time += ride.getDistance() + ridePos.getDistanceTo(nextRidePos);
        int latestStart = nextRide.getLastestFinish() - nextRide.getDistance();

        if (time > latestStart) {
          invalidRides.add(ride);
        }
      }

      return invalidRides;
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

    private int[] findReplacement(Ride previousRide, Ride nextRide, List<Ride> secondCRides) {
      int firstIndex = this.replacementFirstIndex(previousRide, secondCRides);
      int secondIndex = this.replacementSecondIndex(nextRide, secondCRides);
      int indexs[] = {firstIndex, secondIndex};
      return indexs;
    }

    private int replacementFirstIndex(Ride previousRide, List<Ride> secondCRides) {
      int firstIndex = 0;
      if(previousRide != null) {
        int previousTime = previousRide.getEarliestStart() + previousRide.getDistance();
        Position previousPosition = previousRide.getEnd();
        for(int i = 0; i < secondCRides.size(); ++i) {
          Ride ride = secondCRides.get(i);
          if(previousTime + previousPosition.getDistanceTo(ride.getStart()) <= ride.getEarliestStart()) {
            firstIndex = i;
            break;
          }
        }
      }

      return firstIndex;
    }

    private int replacementSecondIndex(Ride nextRide, List<Ride> secondCRides) {
      int secondIndex = secondCRides.size() - 1;
      if(nextRide != null) {
        for(int i = secondCRides.size() - 1; i >= 0; --i) {
          Ride ride = secondCRides.get(i);
          int rideTime = ride.getEarliestStart() + ride.getDistance();
          if(rideTime + ride.getEnd().getDistanceTo(nextRide.getStart()) <= nextRide.getEarliestStart()) {
            secondIndex = i;
            break;
          }
        }
      }

      return secondIndex;
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