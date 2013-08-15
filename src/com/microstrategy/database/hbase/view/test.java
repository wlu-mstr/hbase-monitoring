/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a bar chart overlaid
 * with a line chart.
 */
public class test extends ApplicationFrame {

    /**
     * Default constructor.
     *
     * @param title the frame title.
     */
    public test(final String title) {

        super(title);

        // create the first dataset...
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        dataset1.addValue(1.0, "S1", "Category 1");
        dataset1.addValue(4.0, "S1", "Category 2");
        dataset1.addValue(3.0, "S1", "Category 3");
        dataset1.addValue(5.0, "S1", "Category 4");
        dataset1.addValue(5.0, "S1", "Category 5");
        dataset1.addValue(7.0, "S1", "Category 6");
        dataset1.addValue(7.0, "S1", "Category 7");
        dataset1.addValue(8.0, "S1", "Category 8");

        dataset1.addValue(5.0, "S2", "Category 1");
        dataset1.addValue(7.0, "S2", "Category 2");
        dataset1.addValue(6.0, "S2", "Category 3");
        dataset1.addValue(8.0, "S2", "Category 4");
        dataset1.addValue(4.0, "S2", "Category 5");
        dataset1.addValue(4.0, "S2", "Category 6");
        dataset1.addValue(2.0, "S2", "Category 7");
        dataset1.addValue(1.0, "S2", "Category 8");


        // create the first renderer...
        //      final CategoryLabelGenerator generator = new StandardCategoryLabelGenerator();
        final CategoryItemRenderer renderer = new BarRenderer();
        //    renderer.setLabelGenerator(generator);
        renderer.setItemLabelsVisible(true);

        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset1);
        plot.setRenderer(renderer);

        plot.setDomainAxis(new CategoryAxis("Category"));
        plot.setRangeAxis(new NumberAxis("Value"));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        // now create the second dataset and renderer...
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        dataset2.addValue(9.0, "T1", "Category 1");
        dataset2.addValue(7.0, "T1", "Category 2");
        dataset2.addValue(2.0, "T1", "Category 3");
        dataset2.addValue(6.0, "T1", "Category 4");
        dataset2.addValue(6.0, "T1", "Category 5");
        dataset2.addValue(9.0, "T1", "Category 6");
        dataset2.addValue(5.0, "T1", "Category 7");
        dataset2.addValue(4.0, "T1", "Category 8");
//
//        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
//        plot.setDataset(1, dataset2);
//        plot.setRenderer(1, renderer2);

        // create the third dataset and renderer...
        final ValueAxis rangeAxis2 = new NumberAxis("Axis 2");
        plot.setRangeAxis(1, rangeAxis2);

        DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();
        dataset3.addValue(94.0, "R1", "Category 1");
        dataset3.addValue(75.0, "R1", "Category 2");
        dataset3.addValue(22.0, "R1", "Category 3");
        dataset3.addValue(74.0, "R1", "Category 4");
        dataset3.addValue(83.0, "R1", "Category 5");
        dataset3.addValue(9.0, "R1", "Category 6");
        dataset3.addValue(23.0, "R1", "Category 7");
        dataset3.addValue(98.0, "R1", "Category 8");

        plot.setDataset(2, dataset3);
        final CategoryItemRenderer renderer3 = new LineAndShapeRenderer();
        plot.setRenderer(2, renderer3);
        plot.mapDatasetToRangeAxis(2, 1);

        // change the rendering order so the primary dataset appears "behind" the 
        // other datasets...
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("Overlaid Bar Chart");
        //  chart.setLegend(new StandardLegend());

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

        System.out.println("change dataset");
        chart.getCategoryPlot().setDataset(2, dataset2);
        chart.fireChartChanged();
        chartPanel.repaint();
    }
    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
    Integer ii = 0;

    void addStatus() {
        synchronized (ii) {
            ii++;
        }
    }

    void resetStatus() {
        synchronized (ii) {
            ii = 0;
        }
    }

    class task extends TimerTask {

        @Override
        public void run() {
            addStatus();
            System.out.println("task:\t" + ii);
        }
    }

    class task2 extends TimerTask {

        @Override
        public void run() {
            resetStatus();
            System.out.println("task2:\tcleaned");
        }
    }

    static class testrunnable implements Runnable {

        private Boolean stop = false;
        private  Integer pe = 500;

        public void signalStop() {
            synchronized (stop) {
                stop = true;
            }
        }

        public void setPe(int p) {
            synchronized (pe) {
                pe = p;
            }
        }

        @Override
        public void run() {
            while (true) {
                synchronized (stop) {
                    if (stop) {
                        break;
                    }
                }
                System.out.println("runnintg: " + pe);
                try {
                    synchronized (pe) {
                        Thread.sleep(pe);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void test_timer() {
        Timer t = new Timer();
        t.schedule(new task(), 0, 500);
        Timer t2 = new Timer();
        t2.schedule(new task2(), 0, 2000);
    }

    public static void main(final String[] args) throws InterruptedException {

//        final test demo = new test("Overlaid Bar Chart Demo");
//        demo.pack();
//        RefineryUtilities.centerFrameOnScreen(demo);
//        demo.setVisible(true);
//        test t = new test(null);
//        t.test_timer();
//        Integer i = 22;
//        System.err.println((int) (i / (float) 300 * 100));

        testrunnable tr = new testrunnable();
        Thread thread = new Thread(tr);
        thread.start();
        Thread.sleep(250);
        for(int i = 0; i < 10; i++){
            Thread.sleep(4000);
            if(i%2==0){
                System.out.println("setttt\t" + 1000);
                tr.setPe(1000);
            }
            else{
                System.out.println("setttt\t" + 500);
                tr.setPe(500);
            }
        }
        Thread.sleep(5);
        tr.signalStop();

    }
}