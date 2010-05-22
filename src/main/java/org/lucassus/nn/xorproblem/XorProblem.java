package org.lucassus.nn.xorproblem;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class XorProblem {

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                // Get the native look and feel class name
                String nativeLF = UIManager.getSystemLookAndFeelClassName();

                // Install the look and feel
                try {
                    UIManager.setLookAndFeel(nativeLF);
                } catch (InstantiationException e) {
                } catch (ClassNotFoundException e) {
                } catch (UnsupportedLookAndFeelException e) {
                } catch (IllegalAccessException e) {
                }

                JFrame frame = new JFrame();
                frame.setTitle("XOR Problem");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                XorProblemPanel xor = new XorProblemPanel();

                frame.setJMenuBar(xor.getMenuBar());
                frame.setContentPane(xor);

                frame.setSize(new Dimension(600, 500));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
