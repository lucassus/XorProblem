package org.lucassus.nn.xorproblem.models;

import javax.swing.table.DefaultTableModel;

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
