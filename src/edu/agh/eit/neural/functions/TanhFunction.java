/*
 * TanhFunction.java
 *
 * Created on 14 kwiecieñ 2006, 18:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.neural.functions;

/**
 * 
 * @author Lukasz Bandzarewicz <lucassus@gmnail.com>
 */
public class TanhFunction implements ActivationFunction {
    
    private double beta;
    
    /** Creates a new instance of TanhFunction */
    public TanhFunction() {
        beta = 1.0;
    }
    
    public TanhFunction(double beta) {
        this.beta = beta;
    }

    public double compute(double x) {
        double var1 = Math.pow(Math.E, getBeta() * x);
        double var2 = Math.pow(Math.E, -(getBeta() * x));
        
        return (var1 - var2) / (var1 + var2);
    }

    public double computeDerivative(double x) {
        return 1 - x * x;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }
    
}
