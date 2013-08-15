/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view;

/**
 *
 * @author wlu
 */
import org.jfree.ui.*;
import org.jfree.chart.*;
import org.jfree.data.category.*;
import org.jfree.chart.plot.PlotOrientation;

public class testScack extends ApplicationFrame {
        public testScack(final String title) {
                super(title);
                final CategoryDataset dataset = createDataset();
                final JFreeChart chart = createChart(dataset);
                final ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
                setContentPane(chartPanel);
        }

        private CategoryDataset createDataset() {
                DefaultCategoryDataset result = new DefaultCategoryDataset();

                result.addValue(10, "Girls", "Chess");
                result.addValue(15, "Boys", "Chess");
                result.addValue(17, "Girls", "Cricket");
                result.addValue(23, "Boys", "Cricket");
                result.addValue(25, "Girls", "FootBall");
                result.addValue(20, "Boys", "FootBall");
                result.addValue(40, "Girls", "Badminton");
                result.addValue(30, "Boys", "Badminton");
                result.addValue(35, "Girls", "Hockey");
                result.addValue(32, "Boys", "Hockey");
                return result;
        }

        private JFreeChart createChart(final CategoryDataset dataset) {
                final JFreeChart chart = ChartFactory.createStackedBarChart(
                                "Stacked Bar Chart", "Games", "No. of students", dataset,
                                PlotOrientation.VERTICAL, true, true, false);
                return chart;
        }

        public static void main(final String[] args) {
                final testScack demo = new testScack("Stacked Bar Chart");
                demo.pack();
                RefineryUtilities.centerFrameOnScreen(demo);
                demo.setVisible(true);
        }
}
