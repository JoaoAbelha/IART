/*parent class of all algorithm with generic information*/

import java.util.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;

/*
*  Class that has the attributes common to the problem
*  creates an initial solution -> initial state (may be create class state)
*  child classes should aim to create better solutions using intelligent algorithms
* */
public abstract class Algorithm {

    public static Position start = new Position(0,0);

    protected int steps;
    protected List<Car> cars = new ArrayList<>();
    protected List<Ride> allRides;
    protected List<Ride> rides; //non assigned rides
    protected int[][] state;
    protected int perRideBonus;
    protected Graph graph;
    protected int currentNode;
    protected int nodeCounter;
    protected Random randomGenerator;


    Algorithm() {
        //graph = new MultiGraph("Tutorial 1");
        currentNode = 0;
        randomGenerator = new Random();
        //graph.addNode(Integer.toString(currentNode));
        //Viewer viewer = graph.display();
        //viewer.enableAutoLayout();
    }


    public void fillWithData(int rows, int cols, int nrCars, List <Ride> rides, int steps, int bonus) {
        for(int i = 0; i < nrCars; i++) {
            cars.add(new Car());
        }
        this.steps = steps;
        this.rides = new ArrayList<>(rides);
        this.allRides = rides;
        this.steps = steps;
        this.perRideBonus = bonus;
        this.state = new int[nrCars][rides.size()];
        System.out.println("n cars = " + nrCars + "; n rides = " + rides.size());
        System.out.println(this.state[0][0]);
    }


    /*GREEDY SOLUTION FOR START*/
    /*provides a non optimal initial solution that needs to get better*/
    /*simple solution that chooses a car that is free which distance is shorter to get to the next ride*/
    /*since rides are sorted the assignment of rides will be done incrementally by start time*/
    /*although is not a bad algorithm is not optimal: for instance there are left two rides for a car
    *  start time: 3 latest:8 dist = 3
    *  start time : 4 latest 9 dist = 5
    *
    *  if we are in the time = 3 we choose the first ride that gives us 3 points and we do not have time for the second one
    *  if we have chosen the second one we would get 5 points
    *
    *  by trying to choose the car which is closer to the next person to be transported(sorted) we aim to lose the least time possible
    * */
    /*
    * A State is constituted of the cars and its assigned rides + the rides that have not been assigned
    * Neighbor State is a State which have a different configuration from the current state
    * We should aim for the neighbor states that provide us more points (better solution)
    * note that the bonus is not taken into account
    *
    * */

    public void initialSolution() {
        Collections.sort(rides, Comparator.comparingInt(Ride::getEarliestStart));
        for (Ride ride : rides) ride.setID();

        int currentStep = 0;
        int carsSkipped = 0;

        while(currentStep < this.steps && this.rides.size() > carsSkipped) {

            Ride rideToAssign = rides.get(carsSkipped);
            int minDistance = Integer.MAX_VALUE;
            Car chosen = null;
            currentStep++;
            for(Car car : this.cars) {
                if (car.isBusy(currentStep)) {
                    continue;
                }

                int distanceBetween = car.position.getDistanceTo(rideToAssign.getStart());

                if (minDistance > distanceBetween) {
                    if (distanceBetween + rideToAssign.getDistance() + currentStep > rideToAssign.getLastestFinish()) continue;
                    minDistance = distanceBetween;
                    chosen = car;
                }
            }

            if (chosen == null) {
                carsSkipped++;
                continue;
            }

            state[chosen.id - 1][rideToAssign.id - 1] = 1;
            chosen.addRide(rideToAssign);
            rides.remove(rideToAssign);
            rideToAssign.assign();
            //System.out.println(chosen.busyUntil);
        }

        for( Car car : cars) {
            System.out.println("Car: ");
            for(Ride ride : car.assignedRides) {
                ride.print();
                //score += car.score;
            }
            System.out.println("----------------------------");
        }

        System.out.println("Not assigned rides: " + rides.size());
        System.out.println("Total Points: " + evaluate(state));
    }

    int evaluate(int[][] state) {
        int points = 0;
        Ride ride = allRides.get(0);
        for (int i = 0; i < state.length; i++) {
            int[] car = state[i];
            int time = 0;
            Position pos = new Position(0, 0);
            int j;
            for (j = 0; j < car.length - 1; j++) {
                if (car[j] == 1) {
                    int distance = allRides.get(j).getDistance();
                    Position newPos = allRides.get(j).getStart();
                    points += distance;
                    time += pos.getDistanceTo(newPos) + distance;
                    pos = newPos;

                    if (time <= allRides.get(j + 1).getEarliestStart())
                        points += perRideBonus;
                }
            }

            // last ride
            if (car[j] == 1) {
                int distance = allRides.get(j).getDistance();
                Position newPos = allRides.get(j).getStart();
                points += distance;
                time += pos.getDistanceTo(newPos) + distance;
                pos = newPos;
            }
        }
        return points;
    }

    public abstract void solve();

    public abstract int[][] getNextState(int[][] state);

    /*
    * The initial configuration, state, can be random and we simply should try to get better from this point
    * */
    public void initialRandomSolution() {

       /*
       * do we want to do this ?
       */

    }

    public int[][] getState() {
        return this.state;
    }

    protected boolean validState(int[][] state) {
        for (int[] vehicleRides : state) {
            int[] rides = Arrays.stream(vehicleRides).filter(ride -> ride == 1).toArray();
            int time = 0;
            for (int i = 0; i < rides.length - 1; i++) {
                Ride actualRide = allRides.get(rides[i]);
                Ride nextRide = allRides.get(rides[i + 1]);
                Position currentPos = actualRide.getEnd();
                Position nextRideStart = nextRide.getStart();

                time += actualRide.getDistance() + currentPos.getDistanceTo(nextRideStart);
                int latestStart = nextRide.getLastestFinish() - nextRide.getDistance();

                if (time > latestStart) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean validAssignment(int vehicleID, int rideID) {
        int previousRideID = getPreviousRide(vehicleID, rideID);
        int nextRideID = getNextRide(vehicleID, rideID);

        Ride actualRide = allRides.get(rideID);

        if (previousRideID != -1) {
            Ride previousRide = allRides.get(previousRideID);
            Position previousPos = previousRide.getEnd();
            Position currentRideStart = actualRide.getStart();

            int latestStart = actualRide.getLastestFinish() - actualRide.getDistance();

            if (previousPos.getDistanceTo(currentRideStart) > latestStart)
                return false;
        }

        if (nextRideID != -1) {
            Ride nextRide = allRides.get(nextRideID);
            Position currentPos = actualRide.getEnd();
            Position nextRideStart = nextRide.getStart();

            int latestStart = nextRide.getLastestFinish() - nextRide.getDistance();

            if (currentPos.getDistanceTo(nextRideStart) > latestStart) {
                return false;
            }
        }

        return true;
    }

    private int getPreviousRide(int vehicleID, int rideID) {
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


    //===============================================================
    //=======================OPERATORS===============================
    //===============================================================

    protected int [][] trySwapRandom(int [][] state) {
       if (state[0] == null) return null;
       int car_index1 = randomGenerator.nextInt(state.length);
       int car_index2 = randomGenerator.nextInt(state.length);


       final double deviation_percentage = 0.15;
       int range = (int) Math.floor(deviation_percentage * state[0].length);

       int index1 =  randomGenerator.nextInt(state[0].length);
       int index2 = Math.max(0, Math.min(randomGenerator.nextInt(index1 + range) -range, state[0].length));

       while(state[car_index1][index1] == 1 || state[car_index2][index2] == 1 ) {
           index1 =  randomGenerator.nextInt(state[0].length);
           index2 = Math.max(0, Math.min(randomGenerator.nextInt(index1 + range) -range, state[0].length));
       }

       state[car_index1][index1] = state[car_index1][index1] ^ state[car_index2][index2];
       state[car_index2][index2] = state[car_index1][index1] ^ state[car_index2][index2];
       state[car_index1][index1] = state[car_index1][index1] ^ state[car_index2][index2];

       return state;
    }

    protected int[][] trySwapRide(int[][] state) {
        for(Ride ride : rides) {
            int rideID = ride.id;
            for (int j = 0; j < state.length - 1; j++) {

            }
        }
        return null;
    }

    protected int[][] tryAssignRide(int[][] state) {
        for(Ride ride : rides) {
            int rideID = ride.id;
            for (int i = 0; i < state.length; i++) {
                int[] car = state[i];
                if (car[rideID - 1] == 0) {
                    if (validAssignment(i, rideID - 1)) {
                        state[i][rideID - 1] = 1;
                        rides.remove(ride);

                        // create new node (state)
                        //int newNodeId = ++nodeCounter;
                        //graph.addNode(Integer.toString(newNodeId));
                        //graph.addEdge(Integer.toString(newNodeId), Integer.toString(newNodeId), Integer.toString(currentNode));
                        //currentNode = newNodeId;

                        return state;
                    }
                }
            }
        }
        System.out.println("Did not found new state");
        return null;
    }

    protected int[][] getBestAssignState(int[][] state) {
        int[][] bestState = null;
        int bestValue = evaluate(state);
        Ride rideToRemove = null;
        for(Ride ride : rides) {
            int rideID = ride.id;
            for (int i = 0; i < state.length; i++) {
                int[] car = state[i];
                if (car[rideID - 1] == 0) {
                    if (validAssignment(i, rideID - 1)) {
                        int[][] possibleState = Arrays.stream(state).map(int[]::clone).toArray(int[][]::new); //copy 2D array
                        possibleState[i][rideID - 1] = 1;
                        int stateValue = evaluate(possibleState);
                        if (stateValue > bestValue) {
                            rideToRemove = ride;
                            bestValue = stateValue;
                            bestState = possibleState;
                        }
                    }
                }
            }
        }
        System.out.println(bestValue);
        rides.remove(rideToRemove);
        return bestState;
    }


}
