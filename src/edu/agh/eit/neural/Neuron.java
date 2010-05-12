package edu.agh.eit.neural;

import java.util.ArrayList;
import edu.agh.eit.neural.functions.ActivationFunction;
import edu.agh.eit.neural.functions.LinearFunction;

public class Neuron {

    private double output;
    private double error = 0.0;
    private boolean useBias = true;
    private double biasWeight = 0.0;
    private ArrayList<Synapse> inputSynapses = new ArrayList<Synapse>();
    private ActivationFunction activationFunction;

    /** Creates a new instance of Neuron */
    public Neuron() {
        setActivationFunction(new LinearFunction());
    }

    public Neuron(ActivationFunction f) {
        setActivationFunction(f);
    }

    public void addSynapse(Synapse s) {
        if (s != null) {
            inputSynapses.add(s);
        }
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
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
        for (Synapse s : inputSynapses) {
            output += s.getNeuron().getOutput() * s.getWeight();
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

    public ActivationFunction getActivationFunction() {
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
