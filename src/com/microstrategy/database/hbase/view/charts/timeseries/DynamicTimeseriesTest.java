/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view.charts.timeseries;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;


class DataG extends DataGenerator {

    @Override
    public int getChanel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<RegularTimePeriod> getTimePeriod(int ith) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Double> getValue(int ith) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
//    public int getChanel() {
//        return 1;
//    }
//
//    @Override
//    public RegularTimePeriod getTimePeriod(int ith) {
//        return new ArrayList().add(new Millisecond());
//    }
//
//    @Override
//    public double getValue(int ith) {
//        if (ith == 0) {
//            return (new Random()).nextDouble();
//        } else {
//            return 10 + (new Random()).nextDouble();
//        }
//    }
}

/**
 *
 * @author wlu
 */
public class DynamicTimeseriesTest {

    public static void main(String args[]) throws InterruptedException {
        JFrame frame = new JFrame("Dtime Demo");
        DynamicTimeseriesChartPanel dt = new DynamicTimeseriesChartPanel();
        TimeSeries ts = new TimeSeries("time series name");
        TimeSeries ts2 = new TimeSeries("time series name2");
        ts.setMaximumItemAge(10000);
        ts2.setMaximumItemAge(10000);
        dt.addTimeSeries(ts);
        dt.addTimeSeries(ts2);
        DataGenerator dg = new DataG();
        dt.setDataGenerator(dg);
        dt.setTitle("test dt");
        dt.setDateAxisName("Date");
        dt.setNumAxisName("Random");
        dt.setPeriod(1000);

        dt.createChartPanel();
        ChartPanel cp = dt.getChartPanel();
        dt.start();

        System.err.println(">>>>>>>>>>>>>>>>>>>");
        frame.getContentPane().add(cp, BorderLayout.CENTER);
        frame.setBounds(200, 120, 600, 280);
        frame.setVisible(true);

        Thread.sleep(15000);
        System.err.println("pause 5 s");
        dt.signalPause();
        Thread.sleep(5000);
        System.err.println("resume pause");
        dt.designalPause();
        Thread.sleep(5000);
        System.err.println("stop");
        dt.signalStop();
    }
}
