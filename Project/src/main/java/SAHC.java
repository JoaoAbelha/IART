import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            nextState = getNextState(currentState);
            if (nextState == null) //could not get any better state
                break;
            currentState = nextState;

            //System.out.println("Iteration: " + iteration + "\nTotal Points: " + currentValue + "\nNon Assigned Rides: " + rides.size() + "\n");
            values.add(currentValue);
            iteration++;
        }

        super.state = currentState;
    }

    @Override
    public int[][] getNextState(int[][] stateIn) {
        int[][] nextState = null;

        if (rides.size() > 0)
             nextState = getBestAssignState(stateIn);
        else {
            // getBestSwap
        }
        return nextState;
    }
}
