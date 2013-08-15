package com.microstrategy.database.hbase.datasource;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HServerInfo;
import org.apache.hadoop.hbase.HServerLoad;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.HServerLoad.RegionLoad;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import util.hbaseutils;

import com.microstrategy.database.hbase.model.RegionOfTableOnEachRSServer;
import com.microstrategy.database.hbase.model.RegionServerStatus;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.awt.SunHints;

public class AdminDataSource {
    
    private Configuration conf;
    private HBaseAdmin admin;
    
    public AdminDataSource(Configuration conf) {
        this.conf = conf;
        try {
            admin = new HBaseAdmin(this.conf);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        }
    }
    
    public RegionOfTableOnEachRSServer getRTRS() {
        RegionOfTableOnEachRSServer lRtrs = new RegionOfTableOnEachRSServer();
        try {
            // time stamp, one ts for one fetch
            Timestamp ts = new Timestamp(new Date().getTime());
            // Cluster status
            ClusterStatus status = admin.getClusterStatus();
            // each Hserver information
            Collection<ServerName> serverInfos = status.getServers();
            for (ServerName si : serverInfos) {
                HServerLoad sload = status.getLoad(si);

                // each Region information of that server
                Map<byte[], RegionLoad> regionLoads = sload.getRegionsLoad();
                for (byte[] regionnm : regionLoads.keySet()) {
                    
                    lRtrs.setTimestamp(ts.toString());
                    // set region server name
                    RegionLoad rl = regionLoads.get(regionnm);
                    String rname = rl.getNameAsString();
                    
                    lRtrs.add(si.getServerName(),
                            hbaseutils.getTableNameInRegionName(rname), hbaseutils.getRegionName(rname),
                            rl.getMemStoreSizeMB(), rl.getStorefileSizeMB(),
                            rl.getStorefiles(), rl.getStorefileIndexSizeMB());
                }
            }
            return lRtrs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public RegionServerStatus getRSStatus() {
        try {
            RegionServerStatus lRSstatus = new RegionServerStatus();
            Timestamp ts = new Timestamp(new Date().getTime());
            lRSstatus.setTimestamp(ts.toString());
            ClusterStatus status = admin.getClusterStatus();
            Collection<ServerName> serverInfos = status.getServers();
            for (ServerName si : serverInfos) {
                HServerLoad sload = status.getLoad(si);
                lRSstatus.add(si.getServerName(), sload.getUsedHeapMB(), sload.getMaxHeapMB(), sload.getMemStoreSizeInMB(),
                        sload.getNumberOfRegions(), sload.getNumberOfRequests(), sload.getStorefileIndexSizeInMB(),
                        sload.getStorefileSizeInMB(), sload.getStorefiles());
            }
            // set simple metrix
            lRSstatus.setLiveDead(getLiveDead());
            return lRSstatus;
        } catch (IOException ex) {
            Logger.getLogger(AdminDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    private List<List<String>> getLiveDead() {
        List<List<String>> live_dead = new ArrayList<List<String>>();
        try {
            ClusterStatus status = admin.getClusterStatus();
            List<String> live = new ArrayList<String>();
            live_dead.add(live);
            List<String> dead = new ArrayList<String>();
            live_dead.add(dead);
            /////
            for (ServerName ls : status.getServers()) {
                live.add(ls.getServerName());
            }
            for (ServerName ds : status.getDeadServerNames()) {
                dead.add(ds.getServerName());
            }
            return live_dead;
        } catch (IOException ex) {
            Logger.getLogger(AdminDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
