/*
 * LinearFunction.java
 *
 * Created on 14 kwiecieñ 2006, 19:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.agh.eit.neural.functions;

/**
 *
 * @author Lukasz Bandzarewicz <lucassus@gmail.com>
 */
public class LinearFunction implements ActivationFunction {
    
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
