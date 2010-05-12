/*
 * XorProblemApplet.java
 *
 * Created on 28 kwiecieñ 2006, 00:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import edu.agh.eit.xorproblem.gui.XorProblemPanel;
import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author lucassus
 */
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
