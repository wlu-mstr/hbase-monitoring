/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.control;

/**
 *
 * @author wlu
 */
public interface SignalPause {
    public void signalPause();
    public void designalPause();
    public boolean isPaused();
}
