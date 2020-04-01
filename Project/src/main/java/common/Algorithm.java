package common;
/*
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;*/

import java.util.ArrayList;
import java.util.List;

public abstract class Algorithm<T> {
    public List<Integer> values = new ArrayList<>();
    double[][] initdata = new double[][]{{1}, {1}};
    boolean isRunning = true;
    public int iteration = 0;

    /**
     * Algorithm Constructor
     */
    public Algorithm() {
        /*
        // Create Chart
        final XYChart chart = QuickChart.getChart("Value per Iteration", "Iteration", "Value", "value", initdata[0], initdata[1]);
        final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
        sw.displayChart();

        Thread thread = new Thread(){
            public void run(){
                int counter = 0;
                while (isRunning) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final double[][] data;

                    if (counter < iteration) {
                        counter++;
                    }
                    try {
                        data = getValues(counter);
                        chart.updateXYSeries("value", data[0], data[1], null);
                        sw.repaintChart();
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        return;
                    }

                }
            }
        };
        thread.start();*/
    }

    /**
     * Gets values to be displayed on chart
     *
     * @param counter max iteration
     * @return data to be displayed
     */
    public double[][] getValues(int counter) {
        double[] xData = new double[counter];
        double[] yData = new double[counter];
        for (int i = 0; i < counter; i++) {
            yData[i] = values.get(i);
            xData[i] = i;
        }
        return new double[][] { xData, yData };
    }

    public abstract T solve(T initialSolution);
}