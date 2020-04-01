import java.io.*;
import java.net.URL;
import java.util.*;

import common.Algorithm;
import common.Problem;
import common.Solution;
import common.evaluate_function.PointsEvaluator;
import common.initial_solution.GreedySolutionGenerator;
import common.initial_solution.InitialSolutionGenerator;
import common.initial_solution.RandomSolutionGenerator;
import common.neighborhood.AssignNeighborhood;
import common.neighborhood.Neighborhood;
import common.neighborhood.SwapNeighborhood;
import genetic_algorithm.GeneticAlgorithm;
import genetic_algorithm.PopulationGenerator;
import genetic_algorithm.MutationOperator;
import hill_climbing.HillClimbing;
import hill_climbing.SAHillClimbing;
import model.Car;
import model.Position;
import model.Ride;
import simulated_annealing.SimulatedAnnealing;
import tabu_search.TabuSearch;

import static java.lang.System.exit;

public class Menu {

    public static int errorStatus = 1;
    public Problem problem;
    private int row;
    private int col;
    private int nrCars;
    private int nrRides;
    private int bonus;
    private int steps;
    private PointsEvaluator evaluateFunction;
    private Algorithm<Solution> algorithm;

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage java Menu <file> ");
            exit(errorStatus);
        }


        // ===================== Read from input ==============================//
        Menu menu = new Menu();

        File file = menu.getFileFromResources(args[0]);

        try {
            menu.parseFile(file);
        } catch (IOException e) {
            System.out.println("Error while reading the file");
            e.printStackTrace();
        }
    }

    private void printMenu() {
        System.out.println("========================================================");
        System.out.println("1 - Hill Climbing");
        System.out.println("2 - Hill Climbing Steepest Ascent");
        System.out.println("3 - Simulated Annealing");
        System.out.println("4 - Tabu Search");
        System.out.println("5 - Genetic Algorithm");
        System.out.println("0 - Exit");
        System.out.println("========================================================");
        System.out.print("Option: ");
    }

    /**
     * Lets user chose with algorithm to use
     *
     * @return algorithm that the user chose
     */
    private void mainMenu() {
        evaluateFunction = new PointsEvaluator();
        Solution initialSolution = null, optimalSolution = null;
        Neighborhood<Solution> neighborhood;
        printMenu();

        Scanner myInput = new Scanner(System.in);

        while(true) {
            switch (myInput.nextInt()) {
                case 1:
                    initialSolution = this.initialSolution().initialSolution(this.problem);
                    neighborhood = this.neighborhood();
                    algorithm = new HillClimbing(evaluateFunction, neighborhood);
                    optimalSolution = algorithm.solve(initialSolution);
                    break;
                case 2:
                    initialSolution = this.initialSolution().initialSolution(this.problem);
                    neighborhood = this.neighborhood();
                    algorithm = new SAHillClimbing(evaluateFunction, neighborhood);
                    optimalSolution = algorithm.solve(initialSolution);
                    break;
                case 3:
                    initialSolution = this.initialSolution().initialSolution(this.problem);
                    neighborhood = this.neighborhood();
                    algorithm = new SimulatedAnnealing(evaluateFunction, neighborhood);
                    optimalSolution = algorithm.solve(initialSolution);
                    break;
                case 4:
                    initialSolution = this.initialSolution().initialSolution(this.problem);
                    neighborhood = this.neighborhood();
                    algorithm = new TabuSearch(evaluateFunction, neighborhood);
                    optimalSolution = algorithm.solve(initialSolution);
                    break;
                case 5:
                    PopulationGenerator populationGenerator = new PopulationGenerator(100);
                    GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(100, this.problem);
                    algorithm = geneticAlgorithm;
                    geneticAlgorithm.setPopulation(populationGenerator.generate(this.problem));
                    optimalSolution = geneticAlgorithm.solve(this.problem.getSolution());
                    break;
                case 0:
                    System.out.println("Goodbye!\n\n");
                    exit(0);
                default:
                    System.out.println("Invalid Option, please try again!\n\n");
                    printMenu();
                    continue;
            }
            break;
        }

        this.problem.setSolution(optimalSolution);
    }

    private InitialSolutionGenerator<Solution> initialSolution() {
        System.out.println("========================================================");
        System.out.println("1 - Random Initial Solution");
        System.out.println("2 - Greedy Initial Solution");
        System.out.println("========================================================");
        System.out.print("Option: ");

        Scanner myInput = new Scanner(System.in);

        switch (myInput.nextInt()) {
            case 1:
                return new RandomSolutionGenerator();
            case 2:
                return new GreedySolutionGenerator();
            default:
                break;
        }

        return null;
    }

    private Neighborhood<Solution> neighborhood() {
        System.out.println("========================================================");
        System.out.println("1 - Assign Neighborhood");
        System.out.println("2 - Swap Neighborhood");
        System.out.println("3 - Combine");
        System.out.println("========================================================");
        System.out.print("Option: ");

        Scanner myInput = new Scanner(System.in);

        switch (myInput.nextInt()) {
            case 1:
                return new AssignNeighborhood();
            case 2:
                return new SwapNeighborhood();
            case 3:
                return null;
            default:
                break;
        }

        return null;
    }

    /**
     * Gets input file with the problem representation
     *
     * @param fileName
     * @return the file
     */
    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("b_should_be_easy.in");
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    /**
     * Outputs to a file the representation of the solution
     *
     * @throws IOException
     */
    private void outputFile() throws IOException {
        Writer fileWriter = new FileWriter("output.txt", false);
        List<Car> state = problem.getSolution().getState();
        for(Car car : state) {
            String carRides = "";
            int ridesCounter = 0;
            for(Ride ride : car.getAssignedRides()) {
                carRides += " " + ride.id;
                ridesCounter++;
            }
            fileWriter.write("" + ridesCounter + carRides + '\n');
        }
        fileWriter.close();
    }
    /**
     * Outputs to a JSON file the solution
     *
     * @throws IOException
     */
    private void outputJSON() throws IOException {
        Writer fileWriter = new FileWriter("output.json", false);
        List<Car> state = problem.getSolution().getState();
        HashSet<Integer> bonus = PointsEvaluator.getRideWithBonus(state);
        fileWriter.write("{\"row\": " + this.row + ", \"column\" :" + this.col + ",\"rides\":[");

        for(int i = 0 ; i < state.size()- 1 ; i++) {
            Car car = state.get(i);
            String carRides = "[";
            for(Ride ride : car.getAssignedRides()) {
                carRides += " " + ride.getStart().getX() + ", " + ride.getStart().getY() +", " + ride.getEnd().getX() + ", " + ride.getEnd().getY();
                if (bonus.contains(ride.id)) {
                    carRides+= ", 101";
                }
                else {
                    carRides+=", 1";
                }

                fileWriter.write(carRides);
                fileWriter.write("],");
            }
        }

        Car car = state.get(state.size()-1);
        String carRides = "[";
        for(Ride ride : car.getAssignedRides()) {
            carRides += " " + ride.getStart().getX() + ", " + ride.getStart().getY() +", " + ride.getEnd().getX() + ", " + ride.getEnd().getY();
            if (bonus.contains(ride.id)) {
                carRides+= ", 101";
            }
            else {
                carRides+=", 1";
            }
        }
        fileWriter.write(carRides);
        fileWriter.write("]");

        fileWriter.write("]}");
        fileWriter.close();
    }


    /**
     * Parses file with the representation of the problem
     *
     * @param file
     * @throws IOException
     */
    private void parseFile(File file) throws IOException {

        if (file == null) return;
        List<Ride> rides = new ArrayList<>();

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {

            /*read first line*/
            String line = br.readLine();
            if (line == null) {
                System.out.println("The file is empty");
                exit(errorStatus);
            }

            /*parse global problem variables*/
            String[] values = line.split(" ");

            if (values.length != 6) {
                exit(errorStatus);
            }

            row = Integer.valueOf(values[0]);
            col = Integer.valueOf(values[1]);
            nrCars = Integer.valueOf(values[2]);
            nrRides = Integer.valueOf(values[3]);
            bonus = Integer.valueOf(values[4]);
            steps = Integer.valueOf(values[5]);


            /*parse rides*/
            while ((line = br.readLine()) != null) {

                values = line.split(" ");
                /*x, y, xf, yf, start, finish*/
                int x = Integer.valueOf(values[0]);
                int y = Integer.valueOf(values[1]);
                int xf = Integer.valueOf(values[2]);
                int yf = Integer.valueOf(values[3]);
                int start = Integer.valueOf(values[4]);
                int finish = Integer.valueOf(values[5]);
                /*create a ride*/
                rides.add(new Ride(new Position(x, y), new Position(xf, yf), start, finish));
            }

            if (rides.size() != nrRides) {
                System.out.println("Size mismatch in number of rides");
                exit(1);
            }

            this.problem = new Problem(nrCars, rides, steps, bonus);
            System.out.println("Created the problem");

            System.out.println("Trying to create a better solution...");
            long startTime = System.nanoTime();
            this.mainMenu();
            long estimatedTime = System.nanoTime() - startTime;
            System.out.println("Execution Time(ms): " + estimatedTime/1000000);
            if (algorithm.getClass() != GeneticAlgorithm.class) {
                System.out.println("Not assigned rides: " + this.problem.getSolution().getUnassignedRides().size());
                System.out.println("Total Points: " + evaluateFunction.evaluate(this.problem.getSolution()));
            }

            outputFile();
            outputCsv();
            outputJSON();
        }
    }

    /**
     * Outputs data with the value per iteration to a csv file
     *
     * @throws IOException
     */
    private void outputCsv() throws IOException {
        FileWriter csvWriter = new FileWriter("out.csv");
        for (int i = 0; i < algorithm.iteration; i++) {
            csvWriter.append(Integer.toString(i));
            csvWriter.append(",");
            csvWriter.append(Integer.toString(algorithm.values.get(i)));
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();
    }
}
