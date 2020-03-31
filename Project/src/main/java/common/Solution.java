package common;

import java.util.ArrayList;
import java.util.List;

import model.Car;
import model.Position;
import model.Ride;

public class Solution {
  private List<Car> state;
  private List<Ride> unassignedRides;

  public Solution(List<Car> state, List<Ride> unassigned) {
    this.state = state;
    this.unassignedRides = unassigned;
  }

  public List<Car> getState() {
    return state;
  }

  public void setState(List<Car> state) {
    this.state = state;
  }

  public List<Ride> getUnassignedRides() {
    return unassignedRides;
  }

  public boolean isValid() {
    for (Car car : state) {
      List<Ride> rides = car.getAssignedRides();
      int time = 0;

      if(rides.size() > 0) {
        time = rides.get(0).getStart().getDistanceTo(new Position(0,0));
      }

      for (int i = 0; i < rides.size() - 1; ++i) {
        Ride ride = rides.get(i);
        Ride nextRide = rides.get(i + 1);
        Position ridePos = ride.getEnd();
        Position nextRidePos = nextRide.getStart();

        time += ride.getDistance() + ridePos.getDistanceTo(nextRidePos);
        int latestStart = nextRide.getLastestFinish() - nextRide.getDistance();

        if (time > latestStart) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
	public Solution clone() {
		List<Car> newState = new ArrayList<>();
		for(int i = 0; i < state.size(); ++i) {
          newState.add(state.get(i).clone());
        }
    
        List<Ride> unassigned = new ArrayList<>();
        for(int i = 0; i < unassignedRides.size(); ++i) {
          unassigned.add(unassignedRides.get(i).clone());
        }

		return new Solution(newState, unassigned);
	}
}