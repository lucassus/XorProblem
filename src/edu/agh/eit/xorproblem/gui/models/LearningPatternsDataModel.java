/*
 * LearningPatternsDataModel.java
 *
 * Created on 27 kwiecie� 2006, 21:18
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
public class LearningPatternsDataModel extends DefaultTableModel {
    
    private static String[] columnNames = {
        "A",
        "B",
        "Expected output"
    };
    
    // xor function
    private static Object[][] data = {
        {0, 0, 0},
        {0, 1, 1},
        {1, 0, 1},
        {1, 1, 0}
    };
    
    /** Creates a new instance of LearningPatternsDataModel */
    public LearningPatternsDataModel() {
        super(data, columnNames);
    }
    
    public LearningPatternsDataModel(Object[][] data) {
        super(data, columnNames);
    }

}