package edu.agh.eit.neural;

import edu.agh.eit.neural.events.NeuralNetworkEventListener;
import edu.agh.eit.neural.events.NeuralNetworkLearningEvent;
import edu.agh.eit.neural.functions.IActivationFunction;
import edu.agh.eit.neural.functions.SigmoidFunction;

public class XorTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        double[][] inputs = new double[][]{
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        };

        double[][] desiredOutputs = new double[][]{
            {0.0},
            {1.0},
            {1.0},
            {0.0}
        };

        NeuralLayer[] hiddenLayers = new NeuralLayer[]{
            new NeuralLayer(2)
        };

        IActivationFunction f = new SigmoidFunction();
        NeuralNetwork nn = new NeuralNetwork(2, 1, hiddenLayers, f);
        //nn.setUseBias(false);
        nn.randomizeWeight(-1.0, 1.0);

        nn.addLearningListener(new NeuralNetworkEventListener() {

            public void eventOccured(NeuralNetworkLearningEvent e) {
                //System.out.println("Trail #" + e.getEpochs() + ", Error: " + e.getError());
            }
        });

        for (int i = 0; i < 10000; i++) {
            nn.learn(inputs, desiredOutputs, 0.9);
        }

        for (int i = 0; i < inputs.length; i++) {
            double[] output = nn.compute(inputs[i]);
            for (int j = 0; j < output.length; j++) {
                System.out.println(
                        inputs[i][0] + ";"
                        + inputs[i][1] + " = "
                        + output[j]);
            }
        }

    }
}
