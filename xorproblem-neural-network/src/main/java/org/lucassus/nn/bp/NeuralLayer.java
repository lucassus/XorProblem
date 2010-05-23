package org.lucassus.nn.bp;

import org.lucassus.nn.bp.functions.IActivationFunction;

/**
 * Represents the Neural Layer.
 */
public class NeuralLayer {

    private Neuron[] neurons;
    private int neuronsCount;

    /**
     * Creates a new instance of NeuralLayer.
     * @param neuronsCount
     */
    public NeuralLayer(int neuronsCount) {
        if (neuronsCount <= 0) {
            throw new IllegalArgumentException();
        }

        this.neuronsCount = neuronsCount;
        neurons = new Neuron[neuronsCount];
        for (int i = 0; i < neuronsCount; i++) {
            neurons[i] = new Neuron();
        }
    }

    /**
     * Creates a new instance of NeuralLayer.
     * @param neuronsCount
     * @param function
     */
    public NeuralLayer(int neuronsCount, IActivationFunction function) {
        this(neuronsCount);
        setActivationFunction(function);
    }

    /**
     * Connects the Neural Layer with another one.
     * @param otherLayer
     */
    public void connectWith(NeuralLayer otherLayer) {
        for (Neuron otherNeuron : otherLayer.neurons) {
            for (Neuron neuron : neurons) {
                otherNeuron.addSynapse(new Synapse(neuron));
            }
        }
    }

    /**
     * Gets neurons from the Layer.
     * @return
     */
    public Neuron[] getNeurons() {
        return neurons;
    }

    /**
     * Gets neurons count from the Layer.
     * @return
     */
    public int getNeuronsCount() {
        return neuronsCount;
    }

    /**
     * Sets the activation function for all neurons in the Layer.
     * @param function
     */
    void setActivationFunction(IActivationFunction function) {
        for (Neuron neuron : neurons) {
            neuron.setActivationFunction(function);
        }
    }
}
