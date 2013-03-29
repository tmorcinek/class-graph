package com.morcinek.uml.graph;

import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;
import java.util.List;

public class SimulationGraph<E extends Number> extends SparseMultigraph<String, QuantityEdge<E>> {

    public void addQuantityEdge(String fromVertex, String toVertex, E edge) {
        if (containsVertex(fromVertex)) {
            addVertex(fromVertex);
        }
        if (containsVertex(toVertex)) {
            addVertex(toVertex);
        }
        addEdge(new QuantityEdge<E>(edge), fromVertex, toVertex, EdgeType.DIRECTED);
    }

}

class QuantityEdge<E extends Number> {

    private E quantity;

    public QuantityEdge(E quantity) {
        this.quantity = quantity;
    }

    public String toString() { // Always good for debugging
        return quantity.toString();
    }

}
