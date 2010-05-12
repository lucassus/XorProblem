package edu.agh.eit.neural;

import java.util.Random;
import edu.agh.eit.neural.functions.ActivationFunction;
import edu.agh.eit.neural.events.NeuralNetworkEventListener;
import edu.agh.eit.neural.events.NeuralNetworkLearningEvent;
import javax.swing.event.EventListenerList;

public class NeuralNetwork {

    /**
     * Mean squared error.
     */
    private double mse = 0.0;

    public enum NetworkState {

        /**
         * Network is newly created
         */
        NEWLY_CREATED,
        /**
         * Learning is in progress
         */
        LEARNING_IN_PROGRESS,
        /**
         * Learning process was terminated
         */
        LEARNING_TERMINATED,
        /**
         * Network is learned
         */
        LEARNED
    }
    private NetworkState networkState = NetworkState.NEWLY_CREATED;
    private int inputs;
    private int outputs;
    private NeuralLayer inputLayer = null;
    private NeuralLayer outputLayer = null;
    private NeuralLayer[] hiddenLayers = null;
    private boolean useBias = true;
    protected EventListenerList learningListeners = new EventListenerList();

    public NeuralNetwork(int inputs, int outputs, NeuralLayer[] hiddenLayers, ActivationFunction f) {
        if (inputs <= 0 || outputs <= 0) {
            throw new IllegalArgumentException();
        }

        this.inputs = inputs;
        inputLayer = new NeuralLayer(inputs, f);
        this.outputs = outputs;
        outputLayer = new NeuralLayer(outputs, f);

        if (hiddenLayers == null || hiddenLayers.length == 0) {
            // network without hidden layers
            inputLayer.connectWith(outputLayer);
        } else {
            // network with hidden layers
            this.hiddenLayers = hiddenLayers;

            inputLayer.connectWith(hiddenLayers[0]);
            for (int i = 0; i < hiddenLayers.length - 1; i++) {
                hiddenLayers[i].connectWith(hiddenLayers[i + 1]);
            }
            hiddenLayers[hiddenLayers.length - 1].connectWith(outputLayer);

        }

    }

    public void randomizeWeight(double min, double max) {
        Random rand = new Random();

        // randomize weights in hidden layers
        if (hiddenLayers != null) {
            for (int i = 0; i < hiddenLayers.length; i++) {
                for (Neuron n : hiddenLayers[i].getNeurons()) {
                    for (Synapse s : n.getInputSynapses()) {
                        s.setWeight((rand.nextDouble() * (max - min)) + min);
                    }

                    if (useBias) {
                        n.setBiasWeight((rand.nextDouble() * (max - min)) + min);
                    }
                }
            }
        }

        // randomize weights in output layer
        for (Neuron n : outputLayer.getNeurons()) {
            for (Synapse s : n.getInputSynapses()) {
                s.setWeight((rand.nextDouble() * (max - min)) + min);
            }

            if (useBias) {
                n.setBiasWeight((rand.nextDouble() * (max - min)) + min);
            }
        }

    }

    public double[] compute(double[] input) {
        double[] response = new double[outputs];

        Neuron[] inputNeurons = inputLayer.getNeurons();
        for (int i = 0; i < input.length; i++) {
            inputNeurons[i].setOutput(input[i]);
        }

        if (hiddenLayers != null) {
            for (int i = 0; i < hiddenLayers.length; i++) {         // foreach hidden layers
                for (Neuron n : hiddenLayers[i].getNeurons()) {  // foreach neurons in layer
                    n.compute();
                }
            }
        }

        Neuron[] outputNeurons = outputLayer.getNeurons();
        Neuron n;
        for (int i = 0; i < response.length; i++) {
            outputNeurons[i].compute();
            response[i] = outputNeurons[i].getOutput();
        }

        return response;
    }

    /**
     * Backpropagation learning.
     *
     * @param inputs
     * @param desiredOutput
     * @param epochs
     * @param learningRate
     */
    public void learn(double[][] inputs, double[][] desiredOutput, int epochs, double learningRate) {

        networkState = NetworkState.LEARNING_IN_PROGRESS;

        for (int i = 0; i < epochs; i++) {

            learn(inputs, desiredOutput, learningRate);

            if (i % 100 == 0) {
                fireLearningEvent(new NeuralNetworkLearningEvent(this, i, getMse()));
            }

        }

        networkState = NetworkState.LEARNED;
    }

    /**
     * Single step back propagation learning
     */
    public void learn(double[][] inputs, double[][] desiredOutput, double learningRate) {
        double globalError;
        double[] response;

        for (int j = 0; j < inputs.length; j++) {

            response = compute(inputs[j]);
            globalError = 0.0;

            for (int k = 0; k < outputLayer.getNeuronsCount(); k++) {
                Neuron outputNeuron = outputLayer.getNeurons()[k];
                double error = desiredOutput[j][k] - response[k];
                outputNeuron.setError(error);

                globalError += error * error;

                // back propagation
                for (Synapse s : outputNeuron.getInputSynapses()) {
                    s.getNeuron().setError(
                            s.getNeuron().getError()
                            + s.getWeight() * outputNeuron.getError());
                }

            }

            mse = Math.sqrt(globalError / (desiredOutput.length * outputs));

            if (hiddenLayers != null) {
                for (int k = hiddenLayers.length - 1; k > 0; k--) {
                    for (Neuron outputNeuron : hiddenLayers[k].getNeurons()) {
                        for (Synapse s : outputNeuron.getInputSynapses()) {
                            s.getNeuron().setError(
                                    s.getNeuron().getError()
                                    + s.getWeight() * outputNeuron.getError());
                        }
                    }
                }
            }

            // correct weights
            for (Neuron n : outputLayer.getNeurons()) {
                for (Synapse s : n.getInputSynapses()) {
                    s.setWeight(
                            s.getWeight()
                            + learningRate * n.getError() * n.getActivationFunction().computeDerivative(n.getOutput()) * s.getNeuron().getOutput());
                }

                if (useBias) {
                    // correct bias weight
                    n.setBiasWeight(n.getBiasWeight() + learningRate * n.getError() * n.getActivationFunction().computeDerivative(n.getOutput()));
                }

                // reset neuron globalError
                n.setError(0);
            }

            if (hiddenLayers != null) {
                for (int k = 0; k < hiddenLayers.length; k++) {
                    for (Neuron n : hiddenLayers[k].getNeurons()) {
                        for (Synapse s : n.getInputSynapses()) {
                            s.setWeight(
                                    s.getWeight()
                                    + learningRate * n.getError() * n.getActivationFunction().computeDerivative(n.getOutput()) * s.getNeuron().getOutput());
                        }

                        if (useBias) {
                            // correct bias weight
                            n.setBiasWeight(n.getBiasWeight() + learningRate * n.getError() * n.getActivationFunction().computeDerivative(n.getOutput()));
                        }

                        // reset neuron globalError
                        n.setError(0);
                    }
                }
            }
        }
    }

    public double getError() {
        return getMse();
    }

    public boolean isUseBias() {
        return useBias;
    }

    public void setUseBias(boolean useBias) {
        this.useBias = useBias;
        for (NeuralLayer hiddenLayer : hiddenLayers) {
            for (Neuron n : hiddenLayer.getNeurons()) {
                n.setUseBias(useBias);
            }
        }
    }

    public void addLearningListener(NeuralNetworkEventListener l) {
        learningListeners.add(NeuralNetworkEventListener.class, l);
    }

    public void removeLearningListener(NeuralNetworkEventListener l) {
        learningListeners.remove(NeuralNetworkEventListener.class, l);
    }

    private void fireLearningEvent(NeuralNetworkLearningEvent e) {
        Object[] listeners = learningListeners.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == NeuralNetworkEventListener.class) {
                ((NeuralNetworkEventListener) listeners[i + 1]).eventOccured(e);
            }
        }
    }

    public double getMse() {
        return mse;
    }

    public NetworkState getNetworkState() {
        return networkState;
    }

    public void setNetworkState(NetworkState networkState) {
        this.networkState = networkState;
    }
}
