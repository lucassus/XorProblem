/*
 * CellGrid.java
 *
 * Created on 30 kwiecieñ 2006, 14:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.xorproblem.gui.panels;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author lucassus
 */
public class CellGrid extends JPanel {
    
    private Cell[][] cellArray;
    
    private boolean mouseHeldWhite;
    private boolean mouseHeldBlack;
    
    private int x;
    private int y;
    
    /** Creates a new instance of CellGrid */
    public CellGrid(int x, int y) {
        if (x <= 0 || y <= 0) {
            throw new IllegalArgumentException();
        }

        this.x = x;
        this.y = y;
        
        cellArray = new Cell[x][y];
        setLayout(new GridLayout(x, y, 0, 0));
        
        for (int i=0; i<x; i++) {
            for (int j=0; j<y; j++) {
                cellArray[i][j] = new Cell(this);
                add(cellArray[i][j]);
            }
        }
    }
    
    public void clearGrid() {
        for (int i=0; i<x; i++) {
            for (int j=0; j<y; j++) {
                cellArray[i][j].setValue(0);
            }
        }
    }

    public boolean isMouseHeldWhite() {
        return mouseHeldWhite;
    }

    public void setMouseHeldWhite(boolean mouseHeldWhite) {
        this.mouseHeldWhite = mouseHeldWhite;
    }

    public boolean isMouseHeldBlack() {
        return mouseHeldBlack;
    }

    public void setMouseHeldBlack(boolean mouseHeldBlack) {
        this.mouseHeldBlack = mouseHeldBlack;
    }
    
}
