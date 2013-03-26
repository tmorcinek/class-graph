package com.morcinek.uml.graph;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class SimulationGraph<E extends Number> extends SparseMultigraph<String, QuantityEdge<E>> {

	Set<String> vertexSet = new HashSet<String>();

	public void addQuantityEdge(String fromVertex, String toVertex, E edge) {
		if (!vertexSet.contains(fromVertex)) {
			vertexSet.add(fromVertex);
			addVertex(fromVertex);
		}
		if (!vertexSet.contains(toVertex)) {
			vertexSet.add(toVertex);
			addVertex(toVertex);
		}

		addEdge(new QuantityEdge<E>(edge), fromVertex, toVertex, EdgeType.DIRECTED);
	}
	
	public void showGraph(String title) {

		Layout<String, Object> layout = new FRLayout(this);
		layout.setSize(new Dimension(400, 400)); // sets the initial size of the
													// space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		VisualizationViewer <String, Object> vv = new VisualizationViewer<String, Object>(layout);
		vv.setPreferredSize(new Dimension(500, 500)); // Sets the viewing area

		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(gm);

		
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
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