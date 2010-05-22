package org.lucassus.nn.xorproblem.models;

import javax.swing.table.DefaultTableModel;

public class NetworkResponseTableModel extends DefaultTableModel {
    public static final int OUTPU_COLUMN_INDEX = 3;

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

    @Override
    public int getColumnCount() {
        return OUTPU_COLUMN_INDEX;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
