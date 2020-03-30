import common.Algorithm;

public class SA extends Algorithm {


    //Set initial temp
    final double TEMPERATURE_INITIAL = 100000;

    double temperature;

    //Cooling rate
    double coolingRate = 0.003;


    public SA() {
        super();
    }

    @Override
    public int[][] getNextState(int[][] state) {
        // insertion
        // swap type1 and swap type2

        return null;
    }

    @Override
    public void solve() {
        temperature = TEMPERATURE_INITIAL;

        int[][] currentState = super.state; // initial configuration
        int evaluation = evaluate(currentState);

        int currentSolution;
        int bestSolution = evaluation;


        while(temperature > 1) {

            int [][] new_state = getNextState(currentState);

            currentSolution = evaluate(new_state);

            // if is better, always accept
            if (currentSolution > bestSolution) {
                bestSolution = currentSolution;
                currentState = state;
            }
            else { // if is not better, accept based on probability
                int delta = bestSolution - currentSolution;
                double random = Math.random();
                if (random <  Math.exp(- delta/ temperature)) {
                    currentState = state;
                }
            }

            temperature *=  (1- coolingRate);
        }


    }

}
