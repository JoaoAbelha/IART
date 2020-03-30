import java.io.*;
import java.net.URL;
import java.util.*;

/*
*** Java graph library for dynamic visualisation:

    JGraphT
    JUNG
    G
    yWorks
    BFG
    GEF
    gmgraphlib
    Scene Graph
    Piccolo2D
    JGraph
*
* */

public class Menu {

    public static int errorStatus = 1;
    public Algorithm problem;
    private int row;
    private int col;
    private int nrCars;
    private int nrRides;
    private int bonus;
    private int steps;

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage java Menu <file in the resources> ");
            System.exit(errorStatus);
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

    /*TODO: complete the menu*/
    private Algorithm mainMenu() {

        System.out.println("========================================================");
        System.out.println("1 - Hill Climbing");
        System.out.println("2 - Hill Climbing Steepest Ascent");
        System.out.println("3 - Simulated Annealing");
        System.out.println("4 - Tabu Search");
        System.out.println("5 - Genetic Algorithm");
        System.out.println("========================================================");
        System.out.print("Option: ");


        Scanner myInput = new Scanner(System.in);

        switch (myInput.nextInt()) {
            case 1:
                return new HC();
            case 2:
                return new SAHC();
            case 3:
                return new SA();
            case 4:
                return new TS();
            case 5:
                return new GeneticAlgorithm(10, nrRides, 0.001, 0.7, 5, 5, 1000);
            default:
                break;
        }

        return null;

    }

    // get file from classpath, resources folder
    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    private void outputFile() throws IOException {
        Writer fileWriter = new FileWriter("output.txt", false);
        int[][] state = problem.getState();
        for (int i = 0; i < state.length; i++) {
            String carRides = "";
            int[] car = state[i];
            int ridesCounter = 0;
            for (int j = 0; j < car.length; j++) {
                if (car[j] == 1) {
                    carRides += " " + j;
                    ridesCounter++;
                }
            }
            fileWriter.write("" + ridesCounter + carRides + '\n');
        }
        fileWriter.close();
    }

    private void parseFile(File file) throws IOException {

        if (file == null) return;
        List<Ride> rides = new ArrayList<>();

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {

            /*read first line*/
            String line = br.readLine();
            if (line == null) {
                System.out.println("The file is empty");
                System.exit(errorStatus);
            }

            /*parse global problem variables*/
            String[] values = line.split(" ");

            if (values.length != 6) {
                System.exit(errorStatus);
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
                System.exit(1);
            }

            /*todo: validate positions accordingly to the number of rows and columns */
            this.problem = mainMenu();
            this.problem.fillWithData(row, col, nrCars, rides, steps, bonus);
            System.out.println("Created the problem");
            this.problem.initialSolution();
            System.out.println("Trying to create a better solution...");
            long startTime = System.nanoTime();
            this.problem.solve();
            long estimatedTime = System.nanoTime() - startTime;
            System.out.println("Non assigned rides: " + this.problem.rides.size());
            System.out.println("Total Points: " + this.problem.evaluate(this.problem.getState()));
            System.out.println("Execution Time(ms): " + estimatedTime/1000000);
            outputFile();
        }
    }
}
