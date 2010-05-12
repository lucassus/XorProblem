/*
 * NeuralNetworkEvent.java
 *
 * Created on 29 kwiecieñ 2006, 17:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.neural.events;

import java.util.EventObject;

/**
 *
 * @author lucassus
 */
public class NeuralNetworkLearningEvent extends EventObject {
    
    private int epochs;
    private double error;
    
    public NeuralNetworkLearningEvent(Object source, int epochs, double error) {
        super(source);
        
        this.epochs = epochs;
        this.error = error;
    }

    public int getEpochs() {
        return epochs;
    }

    public double getError() {
        return error;
    }
    
}
