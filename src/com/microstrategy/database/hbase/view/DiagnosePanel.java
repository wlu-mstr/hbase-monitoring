/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view;

import com.microstrategy.database.hbase.dao.RegionServerStatusDAO;
import com.microstrategy.database.hbase.model.RegionServerStatus;
import com.microstrategy.database.hbase.view.charts.timeseries.DataGenerator;
import com.microstrategy.database.hbase.view.charts.timeseries.DynamicTimeseriesChartPanel;
import com.sun.java.swing.plaf.windows.WindowsTreeUI;
import java.awt.Color;
import java.awt.Component;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author wlu
 */
public class DiagnosePanel extends javax.swing.JPanel {

    class LiveDeadCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (((String) value).startsWith("[dead]")) {
                c.setForeground(Color.RED);
            } else {
                c.setForeground(Color.BLUE);
            }
            return c;
        }
    }

    class IncrementalHeapStatusDataGenerator extends DataGenerator {

        @Override
        public int getChanel() {
            return 3;
        }
        private List<RegionServerStatus> lAfter;

        @Override
        public List<RegularTimePeriod> getTimePeriod(int ith) {
            if (checkpoint == null) {
                return null;
            }
            // read data after checkpoint
            List<RegularTimePeriod> t = new ArrayList<RegularTimePeriod>();
            RegionServerStatusDAO dao = new RegionServerStatusDAO();
            lAfter = dao.selectAfter(checkpoint);
            allHistoryRsStatus.addAll(lAfter);
            // add after to ts
            for (RegionServerStatus rss : lAfter) {
                rss.decoding();
                t.add(new Millisecond(new Date(Timestamp.valueOf(rss.getTimestamp()).getTime())));
            }
            return t;
        }

        @Override
        public List<Double> getValue(int ith) {
            List<Double> d = new ArrayList<Double>();
            if (lAfter != null) {
                for (RegionServerStatus rss : lAfter) {
                    if (ith == 0) {
                        d.add(0.0 + rss.getAMemstoreSize(checkpoint));
                    } else if (ith == 1) {
                    } else if (ith == 2) {
                    }
                }
            }
            return d;
        }
    }

    private void addActionListener() {
        pStatusList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pStatusList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (lse.getValueIsAdjusting()) {
                    return;
                }
                String rsName = pStatusList.getSelectedValue().toString();
                rsName = rsName.substring("[live]".length());
                showTsChart(rsName);
                //System.out.println("selected " + rsName);
            }
        });
    }
    private String checkpoint = null;
    private List<RegionServerStatus> allHistoryRsStatus;
    private DynamicTimeseriesChartPanel mHeapstatusChartPanelShowing;

    private void showTsChart(String iRsName) {
        ////////////////////
        //// memstore size/used heap size/total heap size
        RegionServerStatusDAO dao = new RegionServerStatusDAO();
        // get 3 types of data of that region server along the whole time line
        if (checkpoint == null) {
            allHistoryRsStatus = dao.selectAll();
            int N = allHistoryRsStatus == null ? 0 : allHistoryRsStatus.size();
            if (N == 0) {
                return;
            } else {
                checkpoint = allHistoryRsStatus.get(N - 1).getTimestamp();
            }
        }

        DynamicTimeseriesChartPanel lHeapstatusChartPanel = mCached.get(iRsName);
        // clear the old chart
        if (lHeapstatusChartPanel == null) {
            // create a ts chart of base on the data
            lHeapstatusChartPanel = createHeapstatusDynamicTimeseriesChartPanel(iRsName);
            pHeapStatusTsPanel.add(lHeapstatusChartPanel.getChartPanel());
            mCached.put(iRsName, lHeapstatusChartPanel);
        } 
        if (mHeapstatusChartPanelShowing != null) {
            mHeapstatusChartPanelShowing.getChartPanel().setVisible(false);

        }
        mHeapstatusChartPanelShowing = lHeapstatusChartPanel;
        mHeapstatusChartPanelShowing.getChartPanel().setVisible(true);



        // add that chart to pHeapStatusTsPanel
        pHeapStatusTsPanel.revalidate();
        pHeapStatusTsPanel.repaint();

    }
    private Map<String, DynamicTimeseriesChartPanel> mCached = new HashMap<String, DynamicTimeseriesChartPanel>();

    private DynamicTimeseriesChartPanel createHeapstatusDynamicTimeseriesChartPanel(String iRsName) {
        if (mCached.get(iRsName) != null) {
            return mCached.get(iRsName);
        }
        DynamicTimeseriesChartPanel lDynamicTimeseriesChartPanel = new DynamicTimeseriesChartPanel();
        lDynamicTimeseriesChartPanel.setDataGenerator(null);
        lDynamicTimeseriesChartPanel.setTitle("Heap status");
        lDynamicTimeseriesChartPanel.setDateAxisName("time");
        lDynamicTimeseriesChartPanel.setNumAxisName("heap size/MB");
        TimeSeries lmemstoreTS = new TimeSeries("Memstore size");
        TimeSeries lusedheapTS = new TimeSeries("Used heap size");
        TimeSeries ltotalheapTS = new TimeSeries("Max heap size");
        for (RegionServerStatus rs : allHistoryRsStatus) {
            rs.decoding();
            Timestamp ltimestamp = Timestamp.valueOf(rs.getTimestamp());
            Millisecond lMillisecond = new Millisecond(new Date(ltimestamp.getTime()));
            lmemstoreTS.add(lMillisecond, rs.getAMemstoreSize(iRsName));
            lusedheapTS.add(lMillisecond, rs.getAUsedHeapSize(iRsName));
            ltotalheapTS.add(lMillisecond, rs.getAMaxHeapSize(iRsName));
        }
        lDynamicTimeseriesChartPanel.addTimeSeries(lmemstoreTS);
        lDynamicTimeseriesChartPanel.addTimeSeries(lusedheapTS);
        lDynamicTimeseriesChartPanel.addTimeSeries(ltotalheapTS);

        lDynamicTimeseriesChartPanel.createChartPanel();
        return lDynamicTimeseriesChartPanel;
    }

    /**
     * Creates new form DiagnosePanel
     */
    public DiagnosePanel() {
        initComponents();
        pStatusList.setModel(new DefaultListModel());
        pStatusList.setCellRenderer(new LiveDeadCellRenderer());
        addActionListener();
    }

    public void setLiveDeadList() {
        pStatusList.removeAll();
        if (live == null && dead == null) {
            return;
        }
        DefaultListModel model = (DefaultListModel) pStatusList.getModel();
        int i = 0;
        for (i = 0; i < live.length(); i++) {
            model.addElement("[live] " + live.getString(i));
        }
        for (i = 0; i < dead.length(); i++) {
            model.addElement("[dead] " + dead.getString(i));
        }

    }
    static JSONArray live, dead;

    private void getLatestStatus() {
        RegionServerStatusDAO dao = new RegionServerStatusDAO();
        String matrix = dao.selectRecentSimple();
        if (matrix == null) {
            live = null;
            dead = null;
            return;
        }
        JSONObject json = new JSONObject(matrix);
        live = (JSONArray) json.getJSONArray(RegionServerStatus.LIVE).get(0);
        dead = (JSONArray) json.getJSONArray(RegionServerStatus.DEAD).get(0);
    }

    public static DiagnosePanel createDiagnosePanel() {
        DiagnosePanel lDiagnosePanel = new DiagnosePanel();
        lDiagnosePanel.getLatestStatus();
        // show list
        lDiagnosePanel.setLiveDeadList();
        // when selected one, show memstore timeseries line
        // TODO
        return lDiagnosePanel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pStatusList = new javax.swing.JList();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pHeapStatusTsPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setResizeWeight(0.2);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(pStatusList);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel1);

        pHeapStatusTsPanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Memstore", pHeapStatusTsPanel);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 664, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 477, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab2", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 664, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 477, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab3", jPanel4);

        jSplitPane1.setRightComponent(jTabbedPane1);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel pHeapStatusTsPanel;
    private javax.swing.JList pStatusList;
    // End of variables declaration//GEN-END:variables
}
