package org.lucassus.nn.bp.functions;

public class LinearFunction implements IActivationFunction {

    private double alpha = 1.0;

    public LinearFunction() {
        alpha = 1.0;
    }

    public LinearFunction(double alpha) {
        this.alpha = alpha;
    }

    public double compute(double x) {
        return alpha * x;
    }

    public double computeDerivative(double x) {
        return alpha;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}
