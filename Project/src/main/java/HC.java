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
        int[][] nextState;
        while (true) {
            System.out.println("oi");
             nextState = getNextState(currentState);
            if (nextState == null) //could not get any better state
                break;
            super.state = nextState;
        }
        System.out.println("bye");
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
        int rideID = rides.get(0).id;
        for (int[] car : state) {
            if (car[rideID - 1] == 0) {
                car[rideID - 1] = 1;
                if (validState(state)) {
                    System.out.println("Found new state");
                    return state;
                }
            }
        }
        System.out.println("Did not found new state");
        return null;
    }
}
