package com.microstrategy.database.hbase.control;

import org.apache.hadoop.conf.Configuration;

import com.microstrategy.database.hbase.datasource.AdminDataSource;
import com.microstrategy.database.hbase.model.RegionOfTableOnEachRSServer;
import com.microstrategy.database.hbase.model.RegionServerStatus;

public class FetchAdmindataRunnable extends FetchRunnable {

    static {
        System.setProperty(
                "javax.xml.parsers.DocumentBuilderFactory",
                "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        System.setProperty(
                "javax.xml.parsers.SAXParserFactory",
                "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
    }
    AdminDataSource mAdminsource;
    private Boolean mStopped = false;
    private String mZookeeper;
    private int mPort;

    public FetchAdmindataRunnable(String zk, int port) {
        mZookeeper = zk;
        mPort = port;
        Configuration configuration = new Configuration();
        configuration.set("hbase.zookeeper.quorum", zk);
        configuration.set("hbase.zookeeper.property.clientPort", port + "");

        mAdminsource = new AdminDataSource(configuration);
    }

    @Override
    public void run() {
        while (true) {
            // stop the thread
            synchronized (mStopped) {
                if (mStopped) {
                    break;
                }
            }
            // detailed information at region scale
            RegionOfTableOnEachRSServer ls = mAdminsource.getRTRS();
            if (ls == null) {   // when connection abnormal, sleep and then skip
                sleep();
                continue;
            }
            ls.encoding();
            ls.Save();
            // global status of each region server
            RegionServerStatus rss = mAdminsource.getRSStatus();
            if (rss == null) {// when connection abnormal, sleep and then skip
                sleep();
                continue;
            }
            rss.encoding();
            rss.Save();

            sleep();
        }
    }
}
