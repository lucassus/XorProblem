package org.lucassus.nn.xorproblem;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class XorProblemApplet extends JApplet {
    
    public XorProblemApplet() {
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
	
	getContentPane().add(new XorProblemPanel());
    }
    
}
