/*
 * HiddelLayersTableModel.java
 *
 * Created on 27 kwiecieñ 2006, 19:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.xorproblem.gui.models;

import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lucassus
 */
public class HiddenLayersTableModel extends DefaultTableModel {
    
    private static String[] columnNames = {
        "Hidden layer name",
        "Neurons count"
    };
    
    /** Creates a new instance of HiddelLayersTableModel */
    public HiddenLayersTableModel() {
        super(columnNames, 1);
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return "Hidden layer #" + (row + 1);
        } else if (column == 1) {
            Object v = super.getValueAt(row, 1);
            if (v != null) {
                return v;
            } else {
                return new Integer(2);
            }
        }
        
        return null;
    }

    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            return false;
        }
        return true;
    }

    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        } else if (columnIndex == 1) {
            return JSpinner.class;
        }
        
        return null;
    }

}
