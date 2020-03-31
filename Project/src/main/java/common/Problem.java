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

  /**
   * Problem constructor
   *
   * @param nrCars number of cars on the problem
   * @param rides number of rides on the problem
   * @param steps max number of steps
   * @param bonus per ride bonus
   */
  public Problem(int nrCars, List<Ride> rides, int steps, int bonus) {
    for(int i = 0; i < nrCars; i++) {
      this.cars.add(new Car(new Position(0,0)));
    }

    this.steps = steps;
    Problem.perRideBonus = bonus;
    this.rides = new ArrayList<>(rides);
  }

  /**
   * @return rides from the problem
   */
  public List<Ride> getRides() {
    return this.rides;
  }

  /**
   * @return cars from the problem
   */
  public List<Car> getCars() {
    return this.cars;
  }

  /**
   * @return steps from the problem
   */
  public int getSteps() {
    return this.steps;
  }

  /**
   * @return solution of the problem
   */
  public Solution getSolution() {
    return this.solution;
  }

  /**
   * Sets solution on the problem
   */
  public void setSolution(Solution solution) {
    this.solution = solution;
  }
}