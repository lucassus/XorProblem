/*
 * NeuralLayer.java
 *
 * Created on 28 kwiecieñ 2006, 19:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.neural;

import java.util.ArrayList;
import edu.agh.eit.neural.functions.ActivationFunction;

/**
 * 
 * @author Lukasz Bandzarewicz <lucassus@gmail.com>
 */
public class NeuralLayer {
    
    private Neuron[] neurons = null;
    private int neuronsCount = 0;
    
    /** Creates a new instance of NeuralLayer */
    public NeuralLayer(int neuronsCount) {
        if (neuronsCount <= 0) {
            throw new IllegalArgumentException();
        }
        
        this.neuronsCount = neuronsCount;
        neurons = new Neuron[neuronsCount];
        
        for (int i=0; i<neuronsCount; i++) {
            neurons[i] = new Neuron();
        }
        
    }
    
    public NeuralLayer(int neuronsCount, ActivationFunction f) {
        this(neuronsCount);
        setActivationFunction(f);
    }
    
    /**
     * 
     */
    public void connectWith(NeuralLayer otherLayer) {
        for (Neuron otherNeurons: otherLayer.neurons) {
            for (Neuron n: neurons) {
                otherNeurons.addSynapse(new Synapse(n));
            }
        }
    }
      
    public Neuron[] getNeurons() {
        return neurons;
    }
    
    public int getNeuronsCount() {
        return neuronsCount;
    }

    void setActivationFunction(ActivationFunction f) {
        for (Neuron n: neurons) {
            n.setActivationFunction(f);
        }
    }
    
}
