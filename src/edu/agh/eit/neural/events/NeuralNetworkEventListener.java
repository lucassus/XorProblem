/*
 * NeuralNetworkEventListener.java
 *
 * Created on 29 kwiecieñ 2006, 17:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.neural.events;

import java.util.EventListener;

/**
 *
 * @author lucassus
 */
public interface NeuralNetworkEventListener extends EventListener {
    
    public void eventOccured(NeuralNetworkLearningEvent e);
    
}
