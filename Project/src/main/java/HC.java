import java.util.Arrays;

public class HC extends Algorithm {
    public HC() {
        super();
    }

    @Override
    public void solve() {
        int[][] currentState = super.state;
        int bestValue = evaluate(currentState);
        boolean improving = true;
        int[][] nextState = null;
        while (true) {
             nextState = getNextState(currentState);
            if (nextState == null) //could not get any better state
                break;
            currentState = nextState;
        }

        super.state = currentState;

        for (int i = 0; i < state.length; i++) {
            System.out.println("Car: " + Arrays.toString(state[i]));
        }
    }

    @Override
    public int[][] getNextState(int[][] stateIn) {
        int currentValue = evaluate(state);
        int[][] state = Arrays.stream(stateIn).map(int[]::clone).toArray(int[][]::new); //copy 2D array

        while (true) {
            int[][] newState = null;
            if (rides.size() > 0) {
                newState = tryAssignRide(state);

                //if(newState == null)
                  //  newState = swapRide(state);
            }
            /* else {
                newState = swapRide(state);
                if (newState == null)
                    newState = tryAssignRide(state);
            } */
            if (newState == null)
                return null;

            if (evaluate(newState) > currentValue)
                return newState;
        }
    }

    private int[][] swapRide(int[][] state) {
        return state;
    }

    private int[][] tryAssignRide(int[][] state) {
        for(Ride ride : rides) {
            int rideID = ride.id;
            for (int i = 0; i < state.length; i++) {
                int[] car = state[i];
                if (car[rideID - 1] == 0) {
                    state[i][rideID - 1] = 1;
                    if (validState(state)) {
                        rides.remove(ride);
                        return state;
                    }
                    state[i][rideID - 1] = 0;
                }
            }
        }
        System.out.println("Did not found new state");
        return null;
    }
}
