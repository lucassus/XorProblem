/*
 * Cell.java
 *
 * Created on 30 kwiecie� 2006, 14:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.xorproblem.gui.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author lucassus
 */
public class Cell extends JPanel implements MouseListener  {
    
    private CellGrid parent;
    
    private int value;
    private Color bgColor;
    
    /** Creates a new instance of Cell */
    public Cell(CellGrid parent) {
        if (parent == null) {
            throw new IllegalArgumentException();
        }
        
        this.parent = parent;
        
        value = 0;
        addMouseListener(this);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D graphics2d = (Graphics2D)g;
        
        switch (getValue()) {
            case 0:
                bgColor = Color.WHITE;
                break;
            case 1:
                bgColor = Color.BLACK;
                break;
            default:
                break;
        }
        
        graphics2d.setBackground(bgColor);
        graphics2d.clearRect(0, 0, getSize().width, getSize().height);
        graphics2d.drawLine(0, 0, getSize().width, 0);
        graphics2d.drawLine(0, 0, 0, getSize().height);
        
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
        switch (getValue()) {
            case 0:
                setValue(1);
                parent.setMouseHeldBlack(true);
                break;
            case 1:
                setValue(0);
                parent.setMouseHeldWhite(true);
                break;
            default:
                break;
        }
        repaint();
    }
    
    public void mouseReleased(MouseEvent e) {
        parent.setMouseHeldWhite(false);
        parent.setMouseHeldBlack(false);
    }
    
    public void mouseEntered(MouseEvent e) {
        if (parent.isMouseHeldBlack()) {
            value = 1;
            repaint();
        } else if (parent.isMouseHeldWhite()) {
            value = 0;
            repaint();
        }
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
        repaint();
    }
    
}