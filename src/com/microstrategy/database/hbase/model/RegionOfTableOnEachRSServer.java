package com.microstrategy.database.hbase.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.microstrategy.database.hbase.dao.RegionOfTableOnEachRSDAO;

public class RegionOfTableOnEachRSServer {

    private String timestamp;
    // used for json string construction before/after db access
    private String metrics;
    public static final String MEMSZIE = "0";
    public static final String STORESIZE = "1";
    public static final String STORENUM = "2";
    public static final String STOREINDEXSIZE = "3";
    // used for json construction before saving to db
    private Map<String, Map<String, Map<String, Map<String, Integer>>>> map_metrics = new HashMap<String, Map<String, Map<String, Map<String, Integer>>>>();
    private JSONObject json_metrix = null;

    public void add(String rs, String tblnm, String regname, int memsize,
            int storesize, int storenum, int storeindsizz) {
        if (map_metrics.get(rs) == null) {
            map_metrics.put(rs,
                    new HashMap<String, Map<String, Map<String, Integer>>>());
        }
        Map<String, Map<String, Map<String, Integer>>> tblname_askey = map_metrics
                .get(rs);
        if (tblname_askey.get(tblnm) == null) {
            tblname_askey.put(tblnm,
                    new HashMap<String, Map<String, Integer>>());
        }
        Map<String, Map<String, Integer>> regionname_askey = tblname_askey
                .get(tblnm);
        // unique
        Map<String, Integer> int_map = new HashMap<String, Integer>();
        int_map.put(MEMSZIE, memsize);
        int_map.put(STORESIZE, storesize);
        int_map.put(STORENUM, storenum);
        int_map.put(STOREINDEXSIZE, storeindsizz);
        regionname_askey.put(regname, int_map);
    }

    public void encoding() {
        json_metrix = new JSONObject(map_metrics);
        this.metrics = json_metrix.toString();
        map_metrics.clear();
    }

    public JSONObject getJsonMatrix() {
        return json_metrix;
    }

    /**
     * must call coding first
     */
    public void Save() {
        RegionOfTableOnEachRSDAO dao = new RegionOfTableOnEachRSDAO();
        dao.insert(this);
    }

    public void deconding() {
        if (metrics == null) {
            return;
        }
        json_metrix = new JSONObject(metrics);

    }

    public RegionOfTableOnEachRSServer() {
        super();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static List<RegionOfTableOnEachRSServer> GetRecent(int n) {
        RegionOfTableOnEachRSDAO dao = new RegionOfTableOnEachRSDAO();
        return dao.selectRecentN(n);
    }

    public static RegionOfTableOnEachRSServer GetLatest() {
        RegionOfTableOnEachRSDAO dao = new RegionOfTableOnEachRSDAO();
        List<RegionOfTableOnEachRSServer> l = dao.selectRecentN(1);
        if (l == null || l.size() != 1) {
            return null;
        }
        l.get(0).deconding();
        return l.get(0);
    }

    @Override
    public String toString() {
        return "RegionOfTableOnEachRSServer [timestamp=" + timestamp
                + ", metrics=" + metrics + "]";
    }

    public static void main(String args[]) {

        RegionOfTableOnEachRSServer rtrs = new RegionOfTableOnEachRSServer();
        // set time stamp
        rtrs.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        String regionname = "user,1,12344542341";
        // add metrix
        rtrs.add("rs-name", "user", regionname + "0", 12, 300, 4, 10);

        for (int i = 1; i < 5; i++) {

            rtrs.add("rs-name", "user", regionname + i, 12 + i * 3,
                    300 + i * 3, 4 + i % 2, 10 + i);
        }
        for (int i = 6; i < 10; i++) {

            rtrs.add("rs-name2", "user", regionname + i, 12 + i * 3,
                    300 + i * 3, 4 + i % 2, 10 + i);
        }
        // coding and saving
        rtrs.encoding();
        rtrs.Save();

        // test select
        RegionOfTableOnEachRSServer rses = RegionOfTableOnEachRSServer
                .GetLatest();
        rses.deconding();
        System.out.println(rses);

    }
}
