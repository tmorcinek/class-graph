package com.morcinek.uml.graph;

import java.util.HashMap;

public class SimulationVisualization {

    private final SimulationGraph<Integer> graph;

    public SimulationVisualization(HashMap<String, HashMap<String, Integer>> relations) {
        graph = new SimulationGraph<Integer>();
        prepareGraph(graph, relations);
    }

    public void showGraph() {
        graph.showGraph("Simulation: ");
    }

    private void prepareGraph(SimulationGraph<Integer> graph, HashMap<String, HashMap<String, Integer>> relations) {
        for (String mainName : relations.keySet()) {
            HashMap<String, Integer> map = relations.get(mainName);
            for (String name : map.keySet()) {
                graph.addQuantityEdge(mainName, name, map.get(name));
            }
        }
    }

}
