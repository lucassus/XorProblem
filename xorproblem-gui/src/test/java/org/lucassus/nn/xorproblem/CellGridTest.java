package org.lucassus.nn.xorproblem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.lucassus.nn.xorproblem.panels.CellGrid;

public class CellGridTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.setLayout(new BorderLayout());
                frame.add(new CellGrid(20, 20));
                frame.setPreferredSize(new Dimension(300, 300));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
