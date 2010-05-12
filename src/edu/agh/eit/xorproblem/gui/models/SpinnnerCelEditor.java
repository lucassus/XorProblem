/*
 * SpinnnerCellRenderer.java
 *
 * Created on 27 kwiecieñ 2006, 19:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.xorproblem.gui.models;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author lucassus
 */
public class SpinnnerCelEditor extends AbstractCellEditor implements TableCellEditor {
    
    JComponent spinner = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
    
    public Object getCellEditorValue() {
        return ((JSpinner)spinner).getValue();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        
        Integer v = new Integer(value.toString());
        ((JSpinner)spinner).setValue(v);
        return spinner;
        
    }
    
}
