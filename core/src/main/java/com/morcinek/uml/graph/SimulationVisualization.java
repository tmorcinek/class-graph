package com.morcinek.uml.graph;

import com.morcinek.uml.graph.components.RegexList;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulationVisualization extends JFrame implements ActionListener {

    private final HashMap<String, HashMap<String, Integer>> relations;

    private RegexList regexList;

    private VisualizationViewer<String, Integer> visualizationViewer;

    public SimulationVisualization(HashMap<String, HashMap<String, Integer>> relations) {
        super("title");
        this.relations = relations;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
        prepareGraph();
    }

    private void initialize() {
        Layout<String, Integer> layout = new FRLayout(new SimulationGraph<Integer>());
        layout.setSize(new Dimension(700, 500));

        visualizationViewer = new VisualizationViewer<String, Integer>(layout);
        visualizationViewer.setPreferredSize(new Dimension(1024, 640));

        visualizationViewer.getRenderContext().setVertexLabelTransformer(new VertexTransformer());
        visualizationViewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.EDITING);
        visualizationViewer.setGraphMouse(gm);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(getTopLayout(), BorderLayout.NORTH);
        mainPanel.add(visualizationViewer, BorderLayout.CENTER);
        getContentPane().add(mainPanel);
        pack();
    }

    private void prepareGraph() {
        prepareGraph(new ArrayList<String>());
    }

    private void prepareGraph(List<String> excludePattern) {
        SimulationGraph simulationGraph = new SimulationGraph<Integer>();
        visualizationViewer.getGraphLayout().setGraph(simulationGraph);
        System.gc();
        for (String mainName : relations.keySet()) {
            HashMap<String, Integer> map = relations.get(mainName);
            for (String name : map.keySet()) {
                if (!startWithPattern(mainName, name, excludePattern)) {
                    simulationGraph.addQuantityEdge(mainName, name, map.get(name));
                }
            }
        }
    }

    private boolean startWithPattern(String mainName, String name, List<String> excludePattern) {
        for (String pattern : excludePattern) {
            if (mainName.startsWith(pattern) || name.startsWith(pattern)) {
                return true;
            }
        }
        return false;
    }

    private void setGraphLayout(Layout<String, Integer> layout) {
        visualizationViewer.setGraphLayout(layout);
    }

    private JPanel getTopLayout() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Editor"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        final JComboBox petList = new JComboBox();
        panel.add(petList);
        petList.addItem(new LayoutWrapper(KKLayout.class));
        petList.addItem(new LayoutWrapper(FRLayout2.class));
        petList.addItem(new LayoutWrapper(CircleLayout.class));
//        petList.addItem(new LayoutWrapper(DAGLayout.class));
        petList.addItem(new LayoutWrapper(ISOMLayout.class));
        petList.addActionListener(this);

        regexList = new RegexList(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepareGraph(regexList.getRegexPatterns());
                visualizationViewer.repaint();
            }
        });
        panel.add(regexList);
        return panel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        LayoutWrapper selectedItem = (LayoutWrapper) ((JComboBox) e.getSource()).getSelectedItem();
        Object o = null;
        try {
            Graph<String, Integer> graph = visualizationViewer.getGraphLayout().getGraph();
            o = selectedItem.layout.getConstructor(new Class[]{Graph.class}).newInstance(graph);
            Layout<String, Integer> graphLayout = (Layout<String, Integer>) o;
            setGraphLayout(graphLayout);
        } catch (InstantiationException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private class VertexTransformer implements Transformer<String, String> {

        @Override
        public String transform(String s) {
            String[] nameParts = s.split("\\.");
            return nameParts[nameParts.length - 1];
        }
    }

    private class LayoutWrapper<T extends Layout> {

        private Class<T> layout;

        private LayoutWrapper(Class<T> layout) {
            this.layout = layout;
        }

        @Override
        public String toString() {
            return layout.getSimpleName();
        }
    }

}
