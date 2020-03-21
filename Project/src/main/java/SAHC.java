import java.util.Arrays;

public class SAHC extends Algorithm {
    public SAHC() {
        super();
    }

    @Override
    public void solve() {
        int[][] currentState = super.state;
        int bestValue = evaluate(currentState);
        boolean improving = true;
        int[][] nextState = null;
        while (true) {
            System.out.println("oioi");
            nextState = getNextState(currentState);
            if (nextState == null) //could not get any better state
                break;
            currentState = nextState;
        }

        super.state = currentState;
    }

    @Override
    public int[][] getNextState(int[][] stateIn) {
        int bestValue = evaluate(state);
        int[][] state = Arrays.stream(stateIn).map(int[]::clone).toArray(int[][]::new); //copy 2D array

        int[][] newState = null;
        int[][] bestState = null;

        int counter = 0;
        while(true) {
            if (rides.size() > 0) {
                newState = tryAssignRide(state);

                //if(newState == null)
                //  newState = swapRide(state);
            } else
                break;
            /* else {
                newState = swapRide(state);
                if (newState == null)
                    newState = tryAssignRide(state);
            } */
            if (newState != null) {
                int value = evaluate(newState);
                if (value > bestValue) {
                    bestState = newState;
                    bestValue = value;
                }
            } else
                break;
        }

        if (newState == null)
            return null;

        return bestState;
    }
}
