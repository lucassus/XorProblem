package org.lucassus.nn.bp.events;

import java.util.EventObject;

public class NeuralNetworkLearningEvent extends EventObject {

    private int epochs;
    private double error;

    public NeuralNetworkLearningEvent(Object source, int epochs, double error) {
        super(source);

        this.epochs = epochs;
        this.error = error;
    }

    public int getEpochs() {
        return epochs;
    }

    public double getError() {
        return error;
    }
}
