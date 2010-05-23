package org.lucassus.nn.bp;

/**
 * Represents the Synapse.
 */
public class Synapse {

    private double weight = 1.0;
    private Neuron neuron;

    /**
     * Creates a new instance of Synapse.
     * @param neuron
     */
    public Synapse(Neuron neuron) {
        this.neuron = neuron;
    }

    /**
     * Returns synapse weight.
     * @return
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets synapse's weight.
     * @param weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Returns neuron connected to the synapse input.
     * @return 
     */
    public Neuron getNeuron() {
        return neuron;
    }
}
