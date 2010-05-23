package org.lucassus.nn.xorproblem;

import javax.swing.JApplet;
import javax.swing.UIManager;

public class XorProblemApplet extends JApplet {
    
    public XorProblemApplet() {
	// Get the native look and feel class name
	String nativeLF = UIManager.getSystemLookAndFeelClassName();
	
	// Install the look and feel
	try {
	    UIManager.setLookAndFeel(nativeLF);
	} catch (Exception e) {
	}
	
	getContentPane().add(new XorProblemPanel());
    }
    
}
