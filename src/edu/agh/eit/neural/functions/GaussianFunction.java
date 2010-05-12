/*
 * GaussianFunction.java
 *
 * Created on 7 maj 2006, 18:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.neural.functions;

/**
 *
 * @author lucassus
 */
public class GaussianFunction implements ActivationFunction {
	
	private static double a = 1.0 / Math.sqrt(2.0 * Math.PI);
    
	public double compute(double x) {
	    return a * Math.exp(-(x * x) / 2);
	}

	public double computeDerivative(double x) {
	    return x * compute(x);
	}
    
}
