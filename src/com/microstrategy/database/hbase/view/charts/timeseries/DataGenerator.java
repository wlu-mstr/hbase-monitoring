/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view.charts.timeseries;

import java.util.List;
import org.jfree.data.time.RegularTimePeriod;

/**
 * generate data for one time series
 * @author wlu
 */
public abstract class DataGenerator {
    //// 
    public abstract int getChanel();
    public abstract List<RegularTimePeriod> getTimePeriod(int ith);
    public abstract List<Double> getValue(int ith);
}
