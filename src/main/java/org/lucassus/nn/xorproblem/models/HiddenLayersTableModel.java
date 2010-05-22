package org.lucassus.nn.xorproblem.models;

import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;

public class HiddenLayersTableModel extends DefaultTableModel {
    private static final int HIDDEN_LAYER_COLUMN_INDEX = 0;
    public static final int NEURONS_COUNT_COLUMN_INDEX = 1;

    private static String[] columnNames = {
        "Hidden layer name",
        "Neurons count"
    };

    /** Creates a new instance of HiddelLayersTableModel */
    public HiddenLayersTableModel() {
        super(columnNames, 1);
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return "Hidden layer #" + (row + 1);
        } else if (column == 1) {
            Object value = super.getValueAt(row, 1);
            if (value != null) {
                return value;
            } else {
                return new Integer(2);
            }
        }

        return null;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == HIDDEN_LAYER_COLUMN_INDEX) {
            return false;
        }
        
        return true;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == HIDDEN_LAYER_COLUMN_INDEX) {
            return String.class;
        } else if (columnIndex == NEURONS_COUNT_COLUMN_INDEX) {
            return JSpinner.class;
        }

        return null;
    }
}
