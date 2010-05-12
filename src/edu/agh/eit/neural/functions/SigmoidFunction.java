/*
 * KrzywaLogistyczna.java
 *
 * Created on 14 kwiecieñ 2006, 11:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.neural.functions;

/**
 * 
 * @author Lukasz Bandzarewicz <lucassus@gmail.com>
 */
public class SigmoidFunction implements ActivationFunction {
  
    private double beta;
    
    /** Creates a new instance of KrzywaLogistyczna */
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
