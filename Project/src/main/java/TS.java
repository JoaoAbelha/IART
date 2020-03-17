
/*https://www.researchgate.net/profile/Fred_Glover/publication/226178849_A_Users_Guide_to_Tabu_Search/links/0a85e533969e6ab81f000000/A-Users-Guide-to-Tabu-Search.pdf*/

public class TS extends Algorithm {

    final private int MAX_ITERATIONS = 100000;

    private int currentIteration;

    public TS() {
        this.currentIteration = 0;
    }

    /*todo: see which data structure is better for a tabu list -> hash map? or a queue? circular queue?*/


    /*
    * @returns true if should stop
    */
    boolean stopCondition() {
        return currentIteration >= MAX_ITERATIONS;
    }

    private void addToTL() {

    }

    boolean containsInTL() {
      return false;
    }

    /*not obligatory*/
    private void updateTLSize(int newSize) {

    }



    @Override
    public void solve() {

        System.out.println("Tabu search algorithm");
        //store best and current solution
        //

        while(!stopCondition()) {

            // get list of candidate neighbors currentSolution.getNeighbors()
            // List solutionsTabu = tabuList.getTabuItems();
            // find best solution that is not in the tabu list(1)
            // check if is better than the best solution so far
            // add current solution to the TL
            // update current solution = (1)

            //update size of the tabu list
        }

        // return best solution found

    }

    @Override
    public int[][] getNextState(int[][] state) {
        return null;
    }
}
