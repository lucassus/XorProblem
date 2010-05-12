/*
 * Synapse.java
 *
 * Created on 28 kwiecieñ 2006, 19:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.neural;

/**
 * 
 * @author Lukasz Bandzarewicz <lucassus@gmail.com>
 */
public class Synapse {
    
    private double weight = 1.0;
    private Neuron neuron;
    
    /** Creates a new instance of Synapse */
    public Synapse(Neuron neuron) {
        this.neuron = neuron;
    }

    /**
     * Returns synapse weight.
     */
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Returns neuron connected to the synapse input.
     */
    public Neuron getNeuron() {
        return neuron;
    }
    
}
