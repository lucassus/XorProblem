package edu.agh.eit.xorproblem.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class CellGridTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                f.setLayout(new BorderLayout());
                f.add(new CellGrid(20, 20));
                f.setPreferredSize(new Dimension(300, 300));
                f.pack();
                f.setVisible(true);
            }
        });
    }
}
