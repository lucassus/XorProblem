package edu.agh.eit.neural;

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
