package org.lucassus.nn.bp.functions;

public class SigmoidFunction implements IActivationFunction {

    private double beta;

    public SigmoidFunction() {
        setBeta(1.0);
    }

    public SigmoidFunction(double beta) {
        setBeta(beta);
    }

    public double compute(double x) {
        return (1.0 / (1.0 + Math.pow(Math.E, -(beta * x))));
    }

    public double computeDerivative(double x) {
        return x * (1.0 - x);
    }

    /**
     * Returns sigmoid function Beta
     */
    public double getBeta() {
        return beta;
    }

    /**
     * Sets sigmoid function Beta
     */
    public void setBeta(double beta) {
        this.beta = beta;
    }
}
