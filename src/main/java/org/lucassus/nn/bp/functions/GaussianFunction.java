package org.lucassus.nn.bp.functions;

public class GaussianFunction implements IActivationFunction {

    private static double a = 1.0 / Math.sqrt(2.0 * Math.PI);

    @Override
    public double compute(double x) {
        return a * Math.exp(-(x * x) / 2);
    }

    @Override
    public double computeDerivative(double x) {
        return x * compute(x);
    }
}
