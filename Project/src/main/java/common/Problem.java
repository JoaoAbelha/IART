package common;

import java.util.ArrayList;
import java.util.List;

import model.Car;
import model.Position;
import model.Ride;

public class Problem {
  private int steps;
  private List<Car> cars = new ArrayList<>();
  private List<Ride> rides; // non assigned rides
  public static int perRideBonus;
  private Solution solution;

  public Problem(int nrCars, List<Ride> rides, int steps, int bonus) {
    for(int i = 0; i < nrCars; i++) {
      this.cars.add(new Car(new Position(0,0)));
    }

    this.steps = steps;
    Problem.perRideBonus = bonus;
    this.rides = new ArrayList<>(rides);
  }

  public List<Ride> getRides() {
    return this.rides;
  }

  public List<Car> getCars() {
    return this.cars;
  }

  public int getSteps() {
    return this.steps;
  }

  public Solution getSolution() {
    return this.solution;
  }

  public void setSolution(Solution solution) {
    this.solution = solution;
  }
}