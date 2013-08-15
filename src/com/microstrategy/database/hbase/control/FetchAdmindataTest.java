package com.microstrategy.database.hbase.control;

public class FetchAdmindataTest {

    /**
     * test
     * @param args 
     */
    public static void main(String args[]) {
        FetchAdmindataRunnable runnable = new FetchAdmindataRunnable(
                "adcaj10.machine.wisdom.com",
                2181);
        Thread thread = new Thread(runnable);
        thread.start();
        
    }
}
