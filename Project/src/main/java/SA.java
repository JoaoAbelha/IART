import java.util.Arrays;

public class SA extends Algorithm {


    //Set initial temp
    final double TEMPERATURE_INITIAL = 500000;

    double temperature; /*current temperature*/

    //Cooling rate
    double coolingRate = 0.001;


    public SA() {
        super();
    }

    @Override
    public int[][] getNextState(int[][] current_state) {
        int[][] state = Arrays.stream(current_state).map(int[]::clone).toArray(int[][]::new);

        return tryAssignRide(state);
    }

    @Override
    public void solve() {
        temperature = TEMPERATURE_INITIAL;

        int[][] currentState = super.state; // initial configuration
        int evaluation = evaluate(currentState);

        int currentSolution;
        int bestSolution = evaluation;

        int [][] best_state = currentState;


        while(temperature > 1) {

            if(this.rides.size() == 0) break;

            int [][] new_state = getNextState(currentState);

            currentSolution = evaluate(new_state);

            // if is better, always accept
            if (currentSolution > bestSolution) {
                bestSolution = currentSolution;
                currentState = new_state;
                best_state = currentState;
            }
            else { // if is not better, accept based on probability
                int delta = bestSolution - currentSolution;
                double random = Math.random();
                if (random <  Math.exp(- delta/ temperature)) {
                    currentState = new_state;
                }
            }

            temperature *=  (1- coolingRate);
        }

        super.state = best_state;

    }

}
