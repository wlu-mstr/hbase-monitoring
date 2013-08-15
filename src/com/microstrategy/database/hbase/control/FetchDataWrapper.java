/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.control;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wlu
 */
public class FetchDataWrapper {

    private List<FetchRunnable> mAllFetchRunnable;
    private boolean isRunning;

    public FetchDataWrapper() {
        init();
    }

    public void init() {
        mAllFetchRunnable = new ArrayList<FetchRunnable>();
    }

    public void addFetchRunnable(FetchRunnable iFetchRunnable) {
        if (mAllFetchRunnable == null) {
            mAllFetchRunnable = new ArrayList<FetchRunnable>();
        }
        mAllFetchRunnable.add(iFetchRunnable);
    }

    public void stopAll() {
        isRunning = false;
        for (FetchRunnable fr : mAllFetchRunnable) {
            fr.signalStop();
        }

    }

    public void startAll() {
        isRunning = true;
        for (FetchRunnable fr : mAllFetchRunnable) {
            Thread thread = new Thread(fr);
            thread.start();
        }
    }

    public void setPeriod(int iPeriod) {
        for (FetchRunnable fr : mAllFetchRunnable) {
            fr.setPeriod(iPeriod);
        }
    }

    /**
     * update data fetching status when is running
     * @return 
     */
    public boolean isRunning() {
        return isRunning;
    }
}
