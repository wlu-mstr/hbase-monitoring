/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author wlu
 */
public class RestartableTimer {
    private Timer timer;
    
    public void cancel(){
        if(timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
    
    public void schedule(TimerTask task, long pd){
        if(timer != null){
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(task, pd, pd);
    }
    
}
