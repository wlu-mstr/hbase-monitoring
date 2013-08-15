/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view;

import com.microstrategy.database.hbase.dao.RegionServerStatusDAO;
import com.microstrategy.database.hbase.model.RegionServerStatus;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author wlu
 */
public class testdiag {

    public static void main(String args[]) {
        RegionServerStatusDAO dao = new RegionServerStatusDAO();
        List<RegionServerStatus> all = dao.selectAll();
        for (RegionServerStatus rss : all) {
            rss.decoding();
//            JSONObject json = rss.getJsonMatrix();
            String rs[] = {"adcbe19.machine.wisdom.com,60020,1363120145058",
                "adcbg19.machine.wisdom.com,60020,1363105517200",
                "adcbe23.machine.wisdom.com,60020,1363105516853",
                "adcbg18.machine.wisdom.com,60020,1363105517012",
                "adcbe21.machine.wisdom.com,60020,1363120145555",
                "adcbg20.machine.wisdom.com,60020,1363154663644",
                "adcbe20.machine.wisdom.com,60020,1363120144903",
                "adcbg17.machine.wisdom.com,60020,1363120144701",
                "adcbg21.machine.wisdom.com,60020,1363154690220",
                "adcbe22.machine.wisdom.com,60020,1363120144215"};
            System.out.print(rss.getTimestamp() + ",");
            for (String r : rs) {
                System.out.print(rss.getAUsedHeapSize(r) + ",");
            }
            System.out.println();
        }
    }
}
