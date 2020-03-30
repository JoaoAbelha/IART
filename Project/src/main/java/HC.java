import java.util.Arrays;

public class HC extends Algorithm {
    public HC() {
        super();
    }

    @Override
    public void solve() {
        int[][] currentState = super.state;
        int[][] nextState = null;
        int iteration = 0;
        while (true) {
             nextState = getNextState(currentState);
            if (nextState == null) //could not get any better state
                break;
            currentState = nextState;

            System.out.println("Iteration: " + iteration++ + "\nTotal Points: " + currentValue + "\nNon Assigned Rides: " + rides.size() + "\n");
        }

        super.state = currentState;
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

            int newValue = evaluate(newState);
            if (newValue > currentValue) {
                super.currentValue = newValue;
                return newState;
            }
        }
    }
}
