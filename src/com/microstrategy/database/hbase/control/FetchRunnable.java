/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.control;

/**
 * not pausable
 * @author wlu
 */
public abstract class FetchRunnable implements Runnable, SignalStop {

    protected Integer mPeriod = 30 * 1000;
    protected Boolean mStop = false;

    public void setPeriod(int t) {
        synchronized (mPeriod) {
            this.mPeriod = t;
        }
    }

    public int getPeriod() {
        return mPeriod;
    }

    public void sleep() {
        try {
            synchronized (mPeriod) {
                Thread.sleep(mPeriod);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void signalStop() {
        synchronized (mStop) {
            mStop = true;
        }
    }

    public boolean isStopped() {
        synchronized (mStop) {
            return mStop;
        }
    }
}
