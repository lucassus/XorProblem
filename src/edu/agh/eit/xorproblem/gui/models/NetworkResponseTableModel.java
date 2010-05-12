/*
 * NetworkResponseTableModel.java
 *
 * Created on 2 maj 2006, 22:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.xorproblem.gui.models;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lucassus
 */
public class NetworkResponseTableModel extends DefaultTableModel {
    
    private static String[] columnNames = {
        "A",
        "B",
        "Output"
    };
    
    // xor function
    private static Object[][] data = {
        {0, 0, 0},
        {0, 1, 0},
        {1, 0, 0},
        {1, 1, 0}
    };
    
    /** Creates a new instance of LearningPatternsDataModel */
    public NetworkResponseTableModel() {
        super(data, columnNames);
        setDataVector(data, columnNames);
    }
    
    public NetworkResponseTableModel(Object[][] data) {
        super();
        setDataVector(data, columnNames);
    }
    
    public int getColumnCount() {
        return 3;
    }
    
    public boolean isCellEditable(int row, int column) {
       return false;
    }
    
}
