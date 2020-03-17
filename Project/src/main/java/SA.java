
public class SA extends Algorithm {


    //Set initial temp
    double temperature = 100000;

    //Cooling rate
    double coolingRate = 0.003;


    public SA() {
        super();
    }

    public static double acceptanceProbability(int currentDistance, int newDistance, double temperature) {
        // If the new solution is better, accept it
        if (newDistance < currentDistance) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((currentDistance - newDistance) / temperature);
    }


    @Override
    public void solve() {

        System.out.println("Search Annealing algorithm");


        int currentSolution = 0;
        int bestSolution = 0;


        while(temperature > 1) {

            // create a neighbour state
            // calculate the new solution value
            // check if we should accept it based on probability
            // keep track of the best solution found


            temperature *=  (1- coolingRate);
        }


    }

    @Override
    public int[][] getNextState(int[][] state) {
        return null;
    }
}
