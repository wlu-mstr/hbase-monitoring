/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view.charts.timeseries;

import com.microstrategy.database.hbase.control.FetchRunnable;
import com.microstrategy.database.hbase.control.SignalPause;
import java.awt.Font;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 *
 * @author wlu
 */
public class DynamicTimeseriesChartPanel extends FetchRunnable implements SignalPause {

    private DataGenerator mOneGenerator;
    private Boolean mPause = false;
    private String mTitle = "";
    private TimeSeriesCollection dataset = new TimeSeriesCollection();
    private ChartPanel mChartPanel;
    private Thread innerThread;
    private String mDateAxisName;
    private String mNumberAxisName;

    public DynamicTimeseriesChartPanel() {
        mOneGenerator = null;
    }

    public void createChartPanel() {
        DateAxis domain = new DateAxis(mDateAxisName);
        NumberAxis range = new NumberAxis(mNumberAxisName);

        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, true);
        renderer.setSeriesShape(mPeriod, null);

        XYPlot lPlot = new XYPlot(dataset, domain, range, renderer);
        domain.setAutoRange(true);
        lPlot.setRangeGridlinesVisible(true);
        lPlot.setDomainGridlinesVisible(true);



        JFreeChart chart = new JFreeChart(mTitle,
                new Font("SansSerif", Font.BOLD, 24), lPlot, true);
        mChartPanel = new ChartPanel(chart, true);
    }

    public void start() {
        if (innerThread != null) {
            throw new RuntimeException("Has started, you can only pause/stop it");
        }
        innerThread = new Thread(this);
        innerThread.start();
    }

    public void stop() {
        if (innerThread != null) {
            try {
                innerThread.interrupt();
            } catch (Exception e) {
            }
        }
    }

    public ChartPanel getChartPanel() {
        return mChartPanel;
    }

    public void setTitle(String iTitle) {
        mTitle = iTitle;
    }

    public void setDateAxisName(String ida) {
        mDateAxisName = ida;
    }

    public void setNumAxisName(String ina) {
        mNumberAxisName = ina;
    }

    /**
     * if this is set, use one generator to generate data for all timeserious
     *
     * @param iDataGenerator
     */
    public void setDataGenerator(DataGenerator iDG) {
        mOneGenerator = iDG;
    }

    public void addTimeSeries(TimeSeries iTS) {
        dataset.addSeries(iTS);
    }

    public void deleteTimeSeries(int idx) {
        dataset.removeSeries(idx);
    }

    @Override
    public void run() {
        while (true) {
            if (!isPaused()) {
                for (int k = 0; k < dataset.getSeriesCount(); k++) {
                    if (mOneGenerator.getTimePeriod(k) == null) {
                    } else {
                        List<RegularTimePeriod> x =   mOneGenerator.getTimePeriod(k);
                        if(x == null || x.isEmpty()){
                            continue;
                        }
                        List<Double> d = mOneGenerator.getValue(k);
                        if(d == null || d.isEmpty()){
                            continue;
                        }
                        if(d.size() != x.size()){
                            continue;
                        }
                        for(int xx = 0; xx < x.size(); xx++){
                            dataset.getSeries(k).add(x.get(xx), d.get(xx));
                        }
                        
                    }
                }
            }
            if (isStopped()) {
                break;
            }

            sleep();
        }
    }

    @Override
    public void signalPause() {
        synchronized (mPause) {
            mPause = true;
        }
    }

    @Override
    public boolean isPaused() {
        synchronized (mPause) {
            return mPause;
        }
    }

    @Override
    public void designalPause() {
        synchronized (mPause) {
            mPause = false;
        }
    }
}
