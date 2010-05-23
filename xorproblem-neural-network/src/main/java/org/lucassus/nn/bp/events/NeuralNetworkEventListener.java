package org.lucassus.nn.bp.events;

import java.util.EventListener;

public interface NeuralNetworkEventListener extends EventListener {

    public void eventOccured(NeuralNetworkLearningEvent e);
}
