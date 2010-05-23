package org.lucassus.nn.bp;

import java.util.ArrayList;
import java.util.List;
import org.lucassus.nn.bp.functions.IActivationFunction;
import org.lucassus.nn.bp.functions.LinearFunction;

/**
 * Represents the single Neuron.
 */
public class Neuron {

    private double output;
    private double error = 0.0;
    private boolean useBias = true;
    private double biasWeight = 0.0;
    private List<Synapse> synapses;
    private IActivationFunction activationFunction;

    /**
     * Creates a new instance of Neuron.
     */
    public Neuron() {
        this(new LinearFunction());
    }

    /**
     * Creates a new instance of Neuron.
     * @param function
     */
    public Neuron(IActivationFunction function) {
        setActivationFunction(function);
        synapses = new ArrayList<Synapse>();
    }

    /**
     * Add a Synapse to the Neuron.
     * @param synapse
     */
    public void addSynapse(Synapse synapse) {
        synapses.add(synapse);
    }

    public void setActivationFunction(IActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    public List<Synapse> getSynapses() {
        return synapses;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    /**
     * Computes synapse's output.
     */
    public void compute() {
        output = 0.0;
        for (Synapse synapse : synapses) {
            output += synapse.getNeuron().getOutput() * synapse.getWeight();
        }

        if (useBias) {
            output += biasWeight * 1.0;
        }

        output = activationFunction.compute(output);
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public IActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public double getBiasWeight() {
        return biasWeight;
    }

    public void setBiasWeight(double biasWeight) {
        this.biasWeight = biasWeight;
    }

    public boolean isUseBias() {
        return useBias;
    }

    public void setUseBias(boolean useBias) {
        this.useBias = useBias;
    }
}
