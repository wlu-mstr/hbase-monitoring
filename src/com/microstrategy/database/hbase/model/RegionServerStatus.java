/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.model;

import com.microstrategy.database.hbase.dao.RegionServerStatusDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author wlu global status of a region server, no drill down to each region or
 * table
 */
public class RegionServerStatus {

    private String timestamp;
    private String metrics;
    private JSONObject jsonMatrix;
    private Map<String, Map<String, Integer>> mapMatrix;
    public static final String USEDHEAPSIZE = "0", DEAD = "0", LIVE = "1";
    public static final String MAXHEAPSIZE = "1";
    public static final String MEMSTORESIZE = "2";
    public static final String NUMOFREGIONS = "3";
    public static final String NUMOFREQUEST = "4";
    public static final String STOREINDEXSIZZ = "5";
    public static final String STOREFILESIZE = "6";
    public static final String NUMOFSTOREFILES = "7";

    public RegionServerStatus() {
        mapMatrix = new HashMap<String, Map<String, Integer>>();
    }
    private List<String> mLiveServers;
    private List<String> mDeadServers;
    private JSONObject jsonLiveDead;
    private String metricsLiveDead;

    public void setLiveDead(List<List<String>> iLD) {
        mLiveServers = iLD.get(0);
        mDeadServers = iLD.get(1);
    }

    public void add(String rsname, int usedheapsize, int maxheapsize, int memstoresize, int numregions, int numrequests,
            int storeindexsizz, int storefilesizz, int numofstorefiles) {

        if (mapMatrix.get(rsname) == null) {
            mapMatrix.put(rsname, new HashMap<String, Integer>());
        }
        Map<String, Integer> val = mapMatrix.get(rsname);
        val.put(USEDHEAPSIZE, usedheapsize);
        val.put(MAXHEAPSIZE, maxheapsize);
        val.put(MEMSTORESIZE, memstoresize);
        val.put(NUMOFREGIONS, numregions);
        val.put(NUMOFREQUEST, numrequests);
        val.put(STOREINDEXSIZZ, storeindexsizz);
        val.put(STOREFILESIZE, storefilesizz);
        val.put(NUMOFSTOREFILES, numofstorefiles);

    }

    public void encoding() {
        // matrix
        jsonMatrix = new JSONObject(mapMatrix);
        metrics = jsonMatrix.toString();
        // livedead
        jsonLiveDead = new JSONObject();
        jsonLiveDead.append(LIVE, new JSONArray(mLiveServers));
        jsonLiveDead.append(DEAD, new JSONArray(mDeadServers));
        metricsLiveDead = jsonLiveDead.toString();
    }

    public String getMetricsLiveDead() {
        return metricsLiveDead;
    }

    public void setMetricsLiveDead(String metricsLiveDead) {
        this.metricsLiveDead = metricsLiveDead;
    }
    private boolean decoded = false;

    public void decoding() {
        if (decoded) {
            return;
        }
        if (metrics != null) {
            jsonMatrix = new JSONObject(metrics);
            decoded = true;
        }

    }

    public Integer getNumRequest(String irsname){
        decoding();
        try {
            JSONObject lrsval = jsonMatrix.getJSONObject(irsname.trim());
            return lrsval.getInt(NUMOFREQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Integer getAMemstoreSize(String irsname) {
        decoding();
        try {
            JSONObject lrsval = jsonMatrix.getJSONObject(irsname.trim());
            return lrsval.getInt(MEMSTORESIZE);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getAUsedHeapSize(String irsname) {
        decoding();
        try {
            JSONObject lrsval = jsonMatrix.getJSONObject(irsname.trim());
            return lrsval.getInt(USEDHEAPSIZE);
        } catch (JSONException e) {
            //e.printStackTrace();
            return null;
        }
    }

    public Integer getAMaxHeapSize(String irsname) {
        decoding();
        try {
            JSONObject lrsval = jsonMatrix.getJSONObject(irsname.trim());
            return lrsval.getInt(MAXHEAPSIZE);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void Save() {
        RegionServerStatusDAO dao = new RegionServerStatusDAO();
        dao.insert(this);
        dao = new RegionServerStatusDAO();
        dao.insertSimple(this);
    }

    public static RegionServerStatus GetLatest() {
        RegionServerStatusDAO dao = new RegionServerStatusDAO();
        List<RegionServerStatus> l = dao.selectRecentN(1);
        dao = new RegionServerStatusDAO();
       // String lsimplestatus = dao.selectRecentSimple();

        if (l != null && l.size() == 1) {
           // l.get(0).setMetricsLiveDead(lsimplestatus);
            l.get(0).decoding();
            return l.get(0);
        }
        return null;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public JSONObject getJsonMatrix() {
        return jsonMatrix;
    }

    public void setJsonMatrix(JSONObject jsonMatrix) {
        this.jsonMatrix = jsonMatrix;
    }

    public static int getCount() {
        RegionServerStatusDAO dao = new RegionServerStatusDAO();
        return dao.getCount();
    }

    public static void main(String args[]) {
        //System.out.print(RegionServerStatus.getCount());
        RegionServerStatus rs = new RegionServerStatus();
        List ld = new ArrayList<List<String>>();

        List dd = new ArrayList();
        List lv = new ArrayList();
        dd.add("dead");
        lv.add("live");
        ld.add(lv);
        ld.add(dd);
        rs.setLiveDead(ld);
        rs.encoding();
        RegionServerStatusDAO dao = new RegionServerStatusDAO();
        dao.insertSimple(rs);
    }
}
