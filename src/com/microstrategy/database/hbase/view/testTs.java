package com.microstrategy.database.hbase.view;

/**
 *
 * @author wlu
 */
/* --------------------
 * MemoryUsageDemo.java
 * --------------------
 * (C) Copyright 2002-2006, by Object Refinery Limited.
 */

import com.microstrategy.database.hbase.control.FetchRunnable;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.apache.hadoop.hbase.mapreduce.hadoopbackport.TotalOrderPartitioner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A demo application showing a dynamically updated chart that displays the
 * current JVM memory usage.
 * <p>
 * IMPORTANT NOTE: THIS DEMO IS DOCUMENTED IN THE JFREECHART DEVELOPER GUIDE. DO
 * NOT MAKE CHANGES WITHOUT UPDATING THE GUIDE ALSO!!
 */
public class testTs extends JPanel {

    /**
     * Time series for total memory used.
     */
    private TimeSeries total;
    /**
     * Time series for free memory.
     */
    private TimeSeries free;

    /**
     * Creates a new application.
     *
     * @param maxAge the maximum age (in milliseconds).
     */
    public testTs(int maxAge) {

        super(new BorderLayout());

        // create two series that automatically discard data more than 30
        // seconds old...
        this.total = new TimeSeries("Total Memory");
        this.total.setMaximumItemAge(maxAge);
        this.free = new TimeSeries("Free Memory");
        this.free.setMaximumItemAge(maxAge);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(this.total);
        dataset.addSeries(this.free);

        DateAxis domain = new DateAxis("Date");
        NumberAxis range = new NumberAxis("Memory");
        //domain.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        //range.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        //domain.setLabelFont(new Font("SansSerif", Font.PLAIN, 30));
        //range.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));

        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);

//        renderer.setSeriesStroke(0, new BasicStroke(3f, BasicStroke.CAP_BUTT,
//                BasicStroke.JOIN_BEVEL));
//        renderer.setSeriesStroke(1, new BasicStroke(3f, BasicStroke.CAP_BUTT,
//                BasicStroke.JOIN_BEVEL));
        XYPlot plot = new XYPlot(dataset, domain, range, renderer);
        domain.setAutoRange(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        

        JFreeChart chart = new JFreeChart("JVM Memory Usage",
                new Font("SansSerif", Font.BOLD, 24), plot, true);
        
        

        ChartPanel chartPanel = new ChartPanel(chart, true);
        
        add(chartPanel);

    }

    /**
     * Adds an observation to the 'total memory' time series.
     *
     * @param y the total memory used.
     */
    private void addTotalObservation(double y) {
        this.total.add(new Millisecond(), y);
    }

    /**
     * Adds an observation to the 'free memory' time series.
     *
     * @param y the free memory.
     */
    private void addFreeObservation(double y) {
        this.free.add(new Millisecond(), y);
    }

    /**
     * The data generator.
     */
    class DataGenerator extends FetchRunnable {


        @Override
        public void run() {
            long f = Runtime.getRuntime().freeMemory();
            long t = Runtime.getRuntime().totalMemory();
            
            addTotalObservation(t);
            addFreeObservation(f);
            try {
                Thread.sleep(mPeriod);
            } catch (InterruptedException ex) {
                Logger.getLogger(testTs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void signalStop() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        

        /**
         * Adds a new free/total memory reading to the dataset.
         *
         * @param event the action event.
         */
//        public void actionPerformed(ActionEvent event) {
//            long f = Runtime.getRuntime().freeMemory();
//            long t = Runtime.getRuntime().totalMemory();
//            addTotalObservation(t);
//            addFreeObservation(f);
//        }
    }

    /**
     * Entry point for the sample application.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {

//        JFrame frame = new JFrame("Memory Usage Demo");
//        testTs panel = new testTs(30000);
//        frame.getContentPane().add(panel, BorderLayout.CENTER);
//        frame.setBounds(200, 120, 600, 280);
//        frame.setVisible(true);
//        DataGenerator dataGenerator = panel.new DataGenerator();
//        dataGenerator.setPeriod(4000);
//        (new Thread(dataGenerator)).start();
//
//        frame.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                System.exit(0);
//            }
//        });
        
        ////////////////////////////////////////////
//        List a = new ArrayList();
//        a.add("a");
//        a.add("xx");
//        JSONObject json = new JSONObject();
//        json.append("alist", new JSONArray(a));
//        System.out.println(json.toString());
//        json = new JSONObject(json.toString());
//        System.out.println(json.get("alist").getClass());
        String s = "a.b.c";
        System.out.print(s.substring(0, s.indexOf(".")));
    }
}