/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view.charts;

import com.microstrategy.database.hbase.model.RegionOfTableOnEachRSServer;
import com.microstrategy.database.hbase.model.filtering.Filter;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.Range;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONObject;

/**
 *
 * @author wlu
 */
public class RegionOfTableOnEachRSServerCharts {
    // create stack bar chart for mem_size, store size, #store, store index size

    private static Number getDSValue(DefaultCategoryDataset iDS, Comparable ir, Comparable ic) {
        Number lMemSizzVal;
        try {
            lMemSizzVal = iDS.getValue(ir, ic);
        } catch (Exception e) {
            lMemSizzVal = 0;
        }
        if (lMemSizzVal == null) {
            lMemSizzVal = 0;
        }
        return lMemSizzVal;
    }

    public static void UpdateRTRSStackedBarChart(ChartPanel iChartPanel[], RegionOfTableOnEachRSServer rtrs, Filter tablenameFilter) {
        // 1. memstore size for each table on each region server. 
        // get memstore for each region server and each table
        JSONObject jsonMatrix = rtrs.getJsonMatrix();

        ////////////
        DefaultCategoryDataset lMemSizzDataset = (DefaultCategoryDataset) iChartPanel[0].getChart().getCategoryPlot().getDataset();      // 0
        ////////////
        DefaultCategoryDataset lStoreFileNumDataset = (DefaultCategoryDataset) iChartPanel[2].getChart().getCategoryPlot().getDataset();; // 2
        DefaultCategoryDataset lStoreFileSizzDataset = (DefaultCategoryDataset) iChartPanel[1].getChart().getCategoryPlot().getDataset();;//1
        ////////////  #region
        DefaultCategoryDataset lRegionNumDataset = new DefaultCategoryDataset();

        for (String rsName : (Set<String>) (jsonMatrix.keySet())) {
            JSONObject json_tblname_askey = jsonMatrix.getJSONObject(rsName);
            for (String tblName : (Set<String>) (json_tblname_askey.keySet())) {
                // if tablename doesnot match, pass
                if (!tablenameFilter.matches(tblName)) {
                    continue;
                }
                JSONObject json_rgname_askey = json_tblname_askey.getJSONObject(tblName);
                for (String rgname : (Set<String>) (json_rgname_askey.keySet())) {
                    JSONObject jsonval = json_rgname_askey.getJSONObject(rgname);
                    ///// memsizz/region/table/rs
                    updateIntV(jsonval, RegionOfTableOnEachRSServer.MEMSZIE, lMemSizzDataset, tblName, rsName);
                    ///// #region/region/table/rs
                    Number lNumRegion = getDSValue(lRegionNumDataset, tblName, rsName);
                    lRegionNumDataset.setValue(lNumRegion.intValue() + 1, tblName, rsName);
                    ///// #storefile/region/table/rs
                    updateIntV(jsonval, RegionOfTableOnEachRSServer.STORENUM, lStoreFileNumDataset, tblName, rsName);
                    // store size/region/table/rs
                    updateIntV(jsonval, RegionOfTableOnEachRSServer.STORESIZE, lStoreFileNumDataset, tblName, rsName);
                    // store index size/region/table/rs
                    // skip
                }
            }
        }

        iChartPanel[0].getChart().getCategoryPlot().setDataset(lMemSizzDataset);
        iChartPanel[0].getChart().fireChartChanged();
        iChartPanel[0].repaint();

        iChartPanel[1].getChart().getCategoryPlot().setDataset(lStoreFileSizzDataset);
        iChartPanel[1].getChart().fireChartChanged();
        iChartPanel[1].repaint();

        iChartPanel[2].getChart().getCategoryPlot().setDataset(lStoreFileNumDataset);
        iChartPanel[2].getChart().fireChartChanged();
        iChartPanel[2].repaint();

        iChartPanel[3].getChart().getCategoryPlot().setDataset(lRegionNumDataset);
        iChartPanel[3].getChart().fireChartChanged();
        iChartPanel[3].repaint();


    }

    public static ChartPanel[] CreateRTRSStackedBarChart(RegionOfTableOnEachRSServer rtrs, Filter tablenameFilter) {
        // 1. memstore size for each table on each region server. 
        // get memstore for each region server and each table
        JSONObject jsonMatrix = rtrs.getJsonMatrix();


        ////////////  memsize
        DefaultCategoryDataset lMemSizzDataset = new DefaultCategoryDataset();
        ////////////  storefile
        DefaultCategoryDataset lStoreFileNumDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset lStoreFileSizzDataset = new DefaultCategoryDataset();
        ////////////  #region
        DefaultCategoryDataset lRegionNumDataset = new DefaultCategoryDataset();


        for (String rsName : (Set<String>) (jsonMatrix.keySet())) {
            JSONObject json_tblname_askey = jsonMatrix.getJSONObject(rsName);
            for (String tblName : (Set<String>) (json_tblname_askey.keySet())) {
                // if tablename doesnot match, pass
                if (!tablenameFilter.matches(tblName)) {
                    continue;
                }
                JSONObject json_rgname_askey = json_tblname_askey.getJSONObject(tblName);
                for (String rgname : (Set<String>) (json_rgname_askey.keySet())) {
                    JSONObject jsonval = json_rgname_askey.getJSONObject(rgname);
                    ///// memsizz/region/table/rs
                    updateIntV(jsonval, RegionOfTableOnEachRSServer.MEMSZIE, lMemSizzDataset, tblName, rsName);
                    ///// #region/region/table/rs
                    Number lNumRegion = getDSValue(lRegionNumDataset, tblName, rsName);
                    lRegionNumDataset.setValue(lNumRegion.intValue() + 1, tblName, rsName);
                    ///// #storefile/region/table/rs
                    updateIntV(jsonval, RegionOfTableOnEachRSServer.STORENUM, lStoreFileNumDataset, tblName, rsName);
                    // store size/region/table/rs
                    updateIntV(jsonval, RegionOfTableOnEachRSServer.STORESIZE, lStoreFileNumDataset, tblName, rsName);
                    // store index size/region/table/rs
                    // skip
                }
            }
        }
        /////// memsizz chart
        JFreeChart lMemSizzChart = ChartFactory.createStackedBarChart("Memsize of each RS", "", "Memsize/MB",
                lMemSizzDataset, PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot plot0 = lMemSizzChart.getCategoryPlot();
        plot0.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        plot0.setRangeGridlinesVisible(true);
        plot0.setDomainGridlinesVisible(true);

        ChartPanel chartPanel0 = new ChartPanel(lMemSizzChart);

        ////// store file chart
        final CategoryItemRenderer renderer = new StackedBarRenderer();
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(0, lStoreFileSizzDataset);
        plot.setRenderer(renderer);

        plot.setDomainAxis(new CategoryAxis("RS"));
        plot.setRangeAxis(new NumberAxis("Store (Index) Size/MB"));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        // generate a charchart
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final JFreeChart lStoreFileChart = new JFreeChart(plot);
        lStoreFileChart.setTitle("Store Files status");
        ChartPanel chartPanel1 = new ChartPanel(lStoreFileChart);

        // store file num chart

        final CategoryItemRenderer renderer3 = new StackedBarRenderer();
        renderer3.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        final CategoryPlot plot3 = new CategoryPlot();
        plot3.setDataset(0, lStoreFileNumDataset);
        plot3.setRenderer(renderer3);

        plot3.setDomainAxis(new CategoryAxis("RS"));
        plot3.setRangeAxis(new NumberAxis("#Store file"));

        plot3.setOrientation(PlotOrientation.VERTICAL);
        plot3.setRangeGridlinesVisible(true);
        plot3.setDomainGridlinesVisible(true);

        // generate a charchart
        plot3.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        plot3.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final JFreeChart lStoreFileNumChart = new JFreeChart(plot3);
        lStoreFileNumChart.setTitle("Store Files Numbers");
        ChartPanel chartPanel2 = new ChartPanel(lStoreFileNumChart);

        ////// # regions
        JFreeChart lNumRegionsChart = ChartFactory.createStackedBarChart("#Regions of each Table", "", "#",
                lRegionNumDataset, PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot plot4 = lNumRegionsChart.getCategoryPlot();
        plot4.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        plot4.setRangeGridlinesVisible(true);
        plot4.setDomainGridlinesVisible(true);

        ChartPanel chartPanel04 = new ChartPanel(lNumRegionsChart);
        chartPanel04.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                // sub plot for each rs. each region and size
                // 1. ge ttable name getRowKey()
                CategoryItemEntity entity = (CategoryItemEntity) event.getEntity();
                String lTableName = entity.getRowKey().toString();

                // 2. create #r bar charts
                ChartPanel[] lRegionSizeOfTheTable = createRegionsizeOfTheTableCharts(lTableName);
                // 3. pop up a frame
                RegionSizeOnEachRSDialog subFrame = new RegionSizeOnEachRSDialog(null, true, lRegionSizeOfTheTable);
                subFrame.setTitle(lTableName);
                subFrame.setVisible(true);
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
            }
        });

        return new ChartPanel[]{chartPanel0, chartPanel1, chartPanel2, chartPanel04};
    }

    private static String padding(String src, int m) {
        if (src.length() < m) {
            for (int i = 0; i < m - src.length(); i++) {
                src += "X";
            }
        } else {
            src = src.substring(1, m);
        }
        return src + " ..";
    }

    public static ChartPanel[] createRegionsizeOfTheTableCharts(String iTableName) {
        RegionOfTableOnEachRSServer rtrs = RegionOfTableOnEachRSServer.GetLatest();
        JSONObject jsonMatrix = rtrs.getJsonMatrix();
        int RSN = jsonMatrix.keySet().size();
        // create array
        ChartPanel[] lret = new ChartPanel[RSN];
        JFreeChart[] lretFreeCharts = new JFreeChart[RSN];
        DefaultCategoryDataset[] lretDataset = new DefaultCategoryDataset[RSN];
        for (int i = 0; i < RSN; i++) {
            lretDataset[i] = new DefaultCategoryDataset();
        }
        String[] rsnames = new String[RSN];
        int index = -1;
        //
        int max_regionsize = -1;
        for (String rsName : (Set<String>) (jsonMatrix.keySet())) {
            rsnames[++index] = rsName;

            JSONObject json_tblname_askey = jsonMatrix.getJSONObject(rsName);
            for (String tblName : (Set<String>) (json_tblname_askey.keySet())) {
                // if is the input table name
                if (tblName.compareToIgnoreCase(iTableName) != 0) {
                    continue;
                }

                JSONObject json_rgname_askey = json_tblname_askey.getJSONObject(tblName);
                for (String rgname : (Set<String>) (json_rgname_askey.keySet())) {
                    JSONObject jsonval = json_rgname_askey.getJSONObject(rgname);
                    String lrgname = rgname.substring(rgname.indexOf(",")).trim();
                    lrgname = padding(lrgname, 5);
                    ///// memsizz/region/table/rs
                    int memstore_size = jsonval.getInt(RegionOfTableOnEachRSServer.MEMSZIE);
                    Number lRgsize = getDSValue(lretDataset[index], "Region Size/MB", lrgname);
                    lRgsize = memstore_size + lRgsize.intValue();
                    /////// store size/region/table/rs
                    int lStoreFlSzz = jsonval.getInt(RegionOfTableOnEachRSServer.STORESIZE);
                    lRgsize = lStoreFlSzz + lRgsize.intValue();
                    lretDataset[index].setValue(lRgsize.intValue(), "Region Size/MB", lrgname);
                }
            }
            for (Iterator it = lretDataset[index].getColumnKeys().iterator(); it.hasNext();) {
                String s = (String) it.next();
                int m = lretDataset[index].getValue("Region Size/MB", s).intValue();
                max_regionsize = m > max_regionsize ? m : max_regionsize;
                System.out.println(rsName + "\t" + s + "\t" + m);
            }

        }
        // create #RS charts 
        for (int i = 0; i < RSN; i++) {

            final CategoryItemRenderer renderer = new BarRenderer();
            renderer.setSeriesPaint(0, i % 2 == 0 ? Color.RED : Color.BLUE);
            renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());

            final CategoryPlot plot = new CategoryPlot();
            plot.setDataset(0, lretDataset[i]);
            plot.setRenderer(renderer);

            plot.setDomainAxis(new CategoryAxis("Region " + rsnames[i].substring(0, rsnames[i].indexOf("."))));
            NumberAxis ax = new NumberAxis("Region size");
            ax.setRange(new Range(0, max_regionsize), true, false);
            plot.setRangeAxis(ax);


            plot.setOrientation(PlotOrientation.HORIZONTAL);

            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinesVisible(true);

            lretFreeCharts[i] = new JFreeChart(plot);
            //lretFreeCharts[i].setTitle(rsnames[i]);
            lretFreeCharts[i].removeLegend();
            lret[i] = new ChartPanel(lretFreeCharts[i]);

        }
        return lret;
    }

    private static void updateIntV(JSONObject jsonval, String MEMSZIE, DefaultCategoryDataset lMemSizzDataset, String tblName, String rsName) {
        int memstore_size = jsonval.getInt(MEMSZIE);
        Number lMemSizzVal = getDSValue(lMemSizzDataset, tblName, rsName);
        lMemSizzVal = memstore_size + lMemSizzVal.intValue();
        lMemSizzDataset.setValue(lMemSizzVal, tblName, rsName);
    }
}
