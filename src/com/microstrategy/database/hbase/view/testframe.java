/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author wlu
 */
public class testframe extends javax.swing.JFrame {

    JFreeChart chart;
    ChartPanel chartPanel;
    DefaultCategoryDataset dataset2;
    /**
     * Creates new form testframe
     */
    public testframe() {
        initComponents();
        
         DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        dataset1.addValue(1.0, "S1", "Category 1");
        dataset1.addValue(4.0, "S1", "Category 2");
        dataset1.addValue(3.0, "S1", "Category 3");
        dataset1.addValue(5.0, "S1", "Category 4");
        dataset1.addValue(5.0, "S1", "Category 5");
        dataset1.addValue(7.0, "S1", "Category 6");
        dataset1.addValue(7.0, "S1", "Category 7");
        dataset1.addValue(8.0, "S1", "Category 8");

        dataset1.addValue(5.0, "S2", "Category 1");
        dataset1.addValue(7.0, "S2", "Category 2");
        dataset1.addValue(6.0, "S2", "Category 3");
        dataset1.addValue(8.0, "S2", "Category 4");
        dataset1.addValue(4.0, "S2", "Category 5");
        dataset1.addValue(4.0, "S2", "Category 6");
        dataset1.addValue(2.0, "S2", "Category 7");
        dataset1.addValue(1.0, "S2", "Category 8");


        // create the first renderer...
  //      final CategoryLabelGenerator generator = new StandardCategoryLabelGenerator();
        final CategoryItemRenderer renderer = new BarRenderer();
    //    renderer.setLabelGenerator(generator);
        renderer.setItemLabelsVisible(true);
        
        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset1);
        plot.setRenderer(renderer);
        
        plot.setDomainAxis(new CategoryAxis("Category"));
        plot.setRangeAxis(new NumberAxis("Value"));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        // now create the second dataset and renderer...
        dataset2 = new DefaultCategoryDataset();
        dataset2.addValue(9.0, "T1", "Category 1");
        dataset2.addValue(9.0, "T1", "Category 2");
        dataset2.addValue(9.0, "T1", "Category 3");
        dataset2.addValue(9.0, "T1", "Category 4");
        dataset2.addValue(9.0, "T1", "Category 5");
        dataset2.addValue(9.0, "T1", "Category 6");
        dataset2.addValue(0.0, "T1", "Category 7");
        dataset2.addValue(9.0, "T1", "Category 8");
//
//        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
//        plot.setDataset(1, dataset2);
//        plot.setRenderer(1, renderer2);

        // create the third dataset and renderer...
        final ValueAxis rangeAxis2 = new NumberAxis("Axis 2");
        plot.setRangeAxis(1, rangeAxis2);

        DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();
        dataset3.addValue(94.0, "R1", "Category 1");
        dataset3.addValue(75.0, "R1", "Category 2");
        dataset3.addValue(22.0, "R1", "Category 3");
        dataset3.addValue(74.0, "R1", "Category 4");
        dataset3.addValue(83.0, "R1", "Category 5");
        dataset3.addValue(9.0, "R1", "Category 6");
        dataset3.addValue(23.0, "R1", "Category 7");
        dataset3.addValue(98.0, "R1", "Category 8");

        plot.setDataset(2, dataset3);
        final CategoryItemRenderer renderer3 = new LineAndShapeRenderer();
        plot.setRenderer(2, renderer3);
        plot.mapDatasetToRangeAxis(2, 1);

        // change the rendering order so the primary dataset appears "behind" the 
        // other datasets...
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        chart = new JFreeChart(plot);
        chart.setTitle("Overlaid Bar Chart");
      //  chart.setLegend(new StandardLegend());

        // add the chart to a panel...
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        jPanel1.add(chartPanel);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1)
                .addGap(0, 325, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        chart.getCategoryPlot().setDataset(2, dataset2);
        chart.getCategoryPlot().mapDatasetToRangeAxis(2, 1);
        chart.fireChartChanged();
        chartPanel.repaint();
        jPanel1.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(testframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(testframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(testframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(testframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new testframe().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
