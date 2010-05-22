package org.lucassus.nn.bp;

import org.lucassus.nn.bp.functions.IActivationFunction;

public class NeuralLayer {

    private Neuron[] neurons;
    private int neuronsCount;

    /** Creates a new instance of NeuralLayer */
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

    public NeuralLayer(int neuronsCount, IActivationFunction function) {
        this(neuronsCount);
        setActivationFunction(function);
    }

    public void connectWith(NeuralLayer otherLayer) {
        for (Neuron otherNeuron : otherLayer.neurons) {
            for (Neuron neuron : neurons) {
                otherNeuron.addSynapse(new Synapse(neuron));
            }
        }
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public int getNeuronsCount() {
        return neuronsCount;
    }

    void setActivationFunction(IActivationFunction function) {
        for (Neuron neuron : neurons) {
            neuron.setActivationFunction(function);
        }
    }
}
