package org.lucassus.nn.bp;

import java.util.ArrayList;
import org.lucassus.nn.bp.functions.IActivationFunction;
import org.lucassus.nn.bp.functions.LinearFunction;

public class Neuron {

    private double output;
    private double error = 0.0;
    private boolean useBias = true;
    private double biasWeight = 0.0;
    private ArrayList<Synapse> inputSynapses = new ArrayList<Synapse>();
    private IActivationFunction activationFunction;

    /** Creates a new instance of Neuron */
    public Neuron() {
        setActivationFunction(new LinearFunction());
    }

    public Neuron(IActivationFunction function) {
        setActivationFunction(function);
    }

    public void addSynapse(Synapse synapse) {
        if (synapse != null) {
            inputSynapses.add(synapse);
        }
    }

    public void setActivationFunction(IActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    public ArrayList<Synapse> getInputSynapses() {
        return inputSynapses;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public void compute() {
        output = 0.0;
        for (Synapse synapse : inputSynapses) {
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
