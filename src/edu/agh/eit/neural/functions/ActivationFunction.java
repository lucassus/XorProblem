/*
 * ActivationFunction.java
 *
 * Created on 14 kwiecieñ 2006, 11:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.neural.functions;

/**
 * Neuron activation function
 * @author Lukasz Bandzarewicz <lucassus@gmail.com>
 */
public interface ActivationFunction {
    
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
