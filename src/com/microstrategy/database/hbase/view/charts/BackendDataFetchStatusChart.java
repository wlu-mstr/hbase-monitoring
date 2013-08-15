/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view.charts;

import com.microstrategy.database.hbase.model.RegionServerStatus;
import com.microstrategy.database.hbase.view.charts.timeseries.DataGenerator;
import com.microstrategy.database.hbase.view.charts.timeseries.DynamicTimeseriesChartPanel;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartPanel;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;

/**
 *
 * @author wlu
 */
public class BackendDataFetchStatusChart {

    static class DataGenerator1 extends DataGenerator {

        @Override
        public int getChanel() {
            return 1;
        }

        @Override
        public List<RegularTimePeriod> getTimePeriod(int ith) {
            List<RegularTimePeriod> t = new ArrayList();
            t.add(new Millisecond());
            return t;
        }

        @Override
        public List<Double> getValue(int ith) {
            List<Double> d = new ArrayList<Double>();
            d.add(0.0 + RegionServerStatus.getCount());
            return d;
        }
    }

    public static DynamicTimeseriesChartPanel createBackendDataFetchStatusChart() {
        // 
        DynamicTimeseriesChartPanel dynamicTimeseriesChartPanel = new DynamicTimeseriesChartPanel();

        TimeSeries ts = new TimeSeries("getting #");
        ts.setMaximumItemAge(10000);
        dynamicTimeseriesChartPanel.addTimeSeries(ts);
        DataGenerator dg = new DataGenerator1();
        dynamicTimeseriesChartPanel.setDataGenerator(dg);
        dynamicTimeseriesChartPanel.setTitle("Backend status");
        dynamicTimeseriesChartPanel.setDateAxisName("Date");
        dynamicTimeseriesChartPanel.setNumAxisName("Count");
        dynamicTimeseriesChartPanel.setPeriod(1000);

        dynamicTimeseriesChartPanel.createChartPanel();
        ChartPanel cp = dynamicTimeseriesChartPanel.getChartPanel();
        dynamicTimeseriesChartPanel.start();

        return dynamicTimeseriesChartPanel;
    }
}
