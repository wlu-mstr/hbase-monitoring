/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.view.charts;

/**
 *
 * @author wlu
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class SubPanelWrapper {

    private JPanel mMailPanel = new JPanel();
    JPanel squares[][];

    public SubPanelWrapper(int iR, int iC) {
        mMailPanel.setLayout(new GridLayout(iR, iC));
        squares = new JPanel[iR][iC];
        for (int i = 0; i < iR; i++) {
            for (int j = 0; j < iC; j++) {
                squares[i][j] = new JPanel();

//                if ((i + j) % 2 == 0) {
//                    squares[i][j].setBackground(Color.white);
//                } else {
//                    squares[i][j].setBackground(Color.black);
//                }
                mMailPanel.add(squares[i][j]);
                squares[i][j].setLayout(new BorderLayout());
            }
        }
        mMailPanel.setVisible(true);
    }

    public void set(int r, int c, JPanel p) {
        if (squares[r][c].getComponentCount() > 0) {
            squares[r][c].remove(0);
        }

        squares[r][c].add(p, 0);
        p.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mMailPanel;
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("Exercise12_10");
        frame.setSize(640, 640);
        frame.add(new SubPanelWrapper(5, 3).getMainPanel());
        frame.setVisible(true);
    }
}


