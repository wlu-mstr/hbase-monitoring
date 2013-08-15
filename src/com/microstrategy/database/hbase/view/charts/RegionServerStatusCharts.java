/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view.charts;

import com.microstrategy.database.hbase.dao.RegionServerStatusDAO;
import com.microstrategy.database.hbase.model.RegionServerStatus;
import java.util.List;
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
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONObject;

/**
 *
 * @author wlu
 */
public class RegionServerStatusCharts {

    public static void updateHeapStatusDataSet(ChartPanel iChartPanel, RegionServerStatus iRegionServerStatus) {
        JSONObject jsonval = iRegionServerStatus.getJsonMatrix();
        // create dataset
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        // max heap size
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        for (String rsname : (Set<String>) (jsonval.keySet())) {
            JSONObject jsonmatrix = jsonval.getJSONObject(rsname);
            dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.MEMSTORESIZE), "MemStore/MB", rsname);
            dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.USEDHEAPSIZE), "UsedHeap/MB", rsname);
            //
            dataset2.addValue(jsonmatrix.getInt(RegionServerStatus.MAXHEAPSIZE), "MaxHeap/MB", rsname);
        }

        iChartPanel.getChart().getCategoryPlot().setDataset(0, dataset1);
        iChartPanel.getChart().getCategoryPlot().setDataset(1, dataset2);
        iChartPanel.getChart().fireChartChanged();
        iChartPanel.repaint();
    }

    public static ChartPanel createHeapStatusChart(RegionServerStatus regionServerStatus) {
        JSONObject jsonval = regionServerStatus.getJsonMatrix();
        // create dataset
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        // max heap size
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        for (String rsname : (Set<String>) (jsonval.keySet())) {
            JSONObject jsonmatrix = jsonval.getJSONObject(rsname);
            dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.MEMSTORESIZE), "MemStore/MB", rsname);
            dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.USEDHEAPSIZE), "UsedHeap/MB", rsname);
            //
            dataset2.addValue(jsonmatrix.getInt(RegionServerStatus.MAXHEAPSIZE), "MaxHeap/MB", rsname);
        }


        // bar chart
        final CategoryItemRenderer renderer = new BarRenderer();
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());

        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(0, dataset1);
        plot.setRenderer(renderer);

        plot.setDomainAxis(new CategoryAxis("RS"));
        plot.setRangeAxis(new NumberAxis("Heap Usage/MB"));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        // line chart
        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);

        // create the third dataset and renderer...
        final ValueAxis rangeAxis2 = new NumberAxis("Max Heap/MB");
        plot.setRangeAxis(1, rangeAxis2);
        //plot.mapDatasetToRangeAxis(1, 1); // set data set to axis

        // generate a charchart
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("Heap status");

        return new ChartPanel(chart);
    }

    public static void updateStoreFileStatusChart(ChartPanel iChartPanel, RegionServerStatus iRegionServerStatus) {
        JSONObject jsonval = iRegionServerStatus.getJsonMatrix();
        // create dataset
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        // max heap size
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        for (String rsname : (Set<String>) (jsonval.keySet())) {
            JSONObject jsonmatrix = jsonval.getJSONObject(rsname);
            dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.STOREFILESIZE), "StoreFleSz/MB", rsname);
            dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.STOREINDEXSIZZ), "StoreFleIndexSz/MB", rsname);
            //
            dataset2.addValue(jsonmatrix.getInt(RegionServerStatus.NUMOFSTOREFILES), "#storefile", rsname);
        }

        iChartPanel.getChart().getCategoryPlot().setDataset(0, dataset1);
        iChartPanel.getChart().getCategoryPlot().setDataset(1, dataset2);
        iChartPanel.getChart().fireChartChanged();
        iChartPanel.repaint();
    }

    public static ChartPanel createStoreFileStatusChart(RegionServerStatus regionServerStatus) {
        JSONObject jsonval = regionServerStatus.getJsonMatrix();
        // create dataset
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        // max heap size
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        for (String rsname : (Set<String>) (jsonval.keySet())) {
            JSONObject jsonmatrix = jsonval.getJSONObject(rsname);
            dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.STOREFILESIZE), "StoreFleSz/MB", rsname);
            dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.STOREINDEXSIZZ), "StoreFleIndexSz/MB", rsname);
            //
            dataset2.addValue(jsonmatrix.getInt(RegionServerStatus.NUMOFSTOREFILES), "#storefile", rsname);
        }

        // bar chart
        final CategoryItemRenderer renderer = new BarRenderer();
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(0, dataset1);
        plot.setRenderer(renderer);

        plot.setDomainAxis(new CategoryAxis("RS"));
        plot.setRangeAxis(new NumberAxis("Store (Index) Size/MB"));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        // line chart
        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);

        // create the third dataset and renderer...
        final ValueAxis rangeAxis2 = new NumberAxis("# Store Files");
        plot.setRangeAxis(1, rangeAxis2);
        plot.mapDatasetToRangeAxis(1, 1); // set data set to axis

        // generate a charchart
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("Store Files status");

        return new ChartPanel(chart);
    }

    public static void updateNumReguestAndRegionDataSet(ChartPanel iChartPanel, RegionServerStatus iRegionServerStatus) {
        JSONObject jsonval = iRegionServerStatus.getJsonMatrix();
        // create dataset
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        // max heap size
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        for (String rsname : (Set<String>) (jsonval.keySet())) {
            JSONObject jsonmatrix = jsonval.getJSONObject(rsname);
            //dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.NUMOFREQUEST), "#Requests", rsname);
            dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.NUMOFREGIONS), "#Regions", rsname);
            //
            dataset2.addValue(jsonmatrix.getInt(RegionServerStatus.NUMOFREQUEST), "#Requests", rsname);
        }

        iChartPanel.getChart().getCategoryPlot().setDataset(0, dataset1);
        iChartPanel.getChart().getCategoryPlot().setDataset(1, dataset2);
        iChartPanel.getChart().fireChartChanged();
        iChartPanel.repaint();
    }

    private static List<RegionServerStatus> allRegionSS=null;
    public static ChartPanel createNumReguestAndRegionStatusChart(RegionServerStatus regionServerStatus) {
        JSONObject jsonval = regionServerStatus.getJsonMatrix();
        // create dataset
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        // max heap size
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        for (String rsname : (Set<String>) (jsonval.keySet())) {
            JSONObject jsonmatrix = jsonval.getJSONObject(rsname);
            //dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.NUMOFREQUEST), "#Requests", rsname);
            dataset1.addValue(jsonmatrix.getInt(RegionServerStatus.NUMOFREGIONS), "#Regions", rsname);
            //
            dataset2.addValue(jsonmatrix.getInt(RegionServerStatus.NUMOFREQUEST), "#Requests", rsname);
        }

        // bar chart
        final CategoryItemRenderer renderer = new BarRenderer();
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(0, dataset1);
        plot.setRenderer(renderer);

        plot.setDomainAxis(new CategoryAxis("RS"));
        plot.setRangeAxis(new NumberAxis("#Regions"));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        // line chart
        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);

        // create the third dataset and renderer...
        final ValueAxis rangeAxis2 = new NumberAxis("#Requests/Sec");
        plot.setRangeAxis(1, rangeAxis2);
        plot.mapDatasetToRangeAxis(1, 1); // set data set to axis

        // generate a charchart
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("#Regions and #Requests");

        ChartPanel l2bReturn = new ChartPanel(chart);
        l2bReturn.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                CategoryItemEntity entity = (CategoryItemEntity)event.getEntity();
                String lRkey = (String) entity.getRowKey();
                String lRsName = (String) entity.getColumnKey();
                if(lRkey.compareTo("#Regions") == 0){
                    // drill down to #request of each region on the region server (ignore table name)
                    if(allRegionSS == null){
                        RegionServerStatusDAO dao = new RegionServerStatusDAO();
                        allRegionSS = dao.selectAll();
                    }
                    // create static ts chart
                    
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
            }
        });
        
        return l2bReturn;
    }
    
    //private 
}
