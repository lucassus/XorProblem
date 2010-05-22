package edu.agh.eit.neural.functions;

public interface IActivationFunction {

    /**
     * Computes activation function value
     * @return activation function value
     */
    double compute(double x);

    /**
     * Computes activation function derivative
     * @return activation function derivative value
     */
    double computeDerivative(double x);
}
