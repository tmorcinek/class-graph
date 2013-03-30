package com.morcinek.uml.gui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

import com.morcinek.uml.logic.object.ClassObject;

public class GuiView extends JFrame {

	private Map<String, GuiClass> classNameMap = new HashMap<String, GuiClass>();

	private HashMap<String, HashMap<String, Integer>> relationsMap;


	public GuiView(String p_name, int p_columns, int p_rows) throws HeadlessException {
		super(p_name);
		this.setSize(1024, 1024 * 3 / 5);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void addClass(ClassObject p_class) {
		this.classNameMap.put(p_class.getFullClassName(), new GuiClass(p_class));
	}

	public void setRelations(HashMap<String, HashMap<String, Integer>> p_relations) {
		this.relationsMap = p_relations;
	}

	public void showDiagramGrid() {
		int rows = (int) Math.sqrt((double) classNameMap.size()) + 1;
		int columns = (int) ((rows * 5 + 0.5) / 3);

		this.setLayout(new GridLayout(rows, columns, 10, 10));

		for (GuiClass guiClass : classNameMap.values()) {
			this.add(guiClass);
		}
		this.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;

		if (relationsMap != null) {
			int relationNumber = 0;
			for (String mainName : this.relationsMap.keySet()) {
				HashMap<String, Integer> map = this.relationsMap.get(mainName);
				for (String secondName : map.keySet()) {
					relationNumber++;
					int value = map.get(secondName);
					int k = new Random().nextInt(40);
					GuiClass mainClass = this.classNameMap.get(mainName);
					GuiClass secondClass = this.classNameMap.get(secondName);

					Point mainPoint = getEndPoint(mainClass, relationNumber);
					Point secondPoint = getEndPoint(secondClass, relationNumber);

					final int pointerSize = 16;
					final int gapSize = 5;
					if ((value & 16) > 0) { // dziedziczenie
						g2.draw(createPointer(secondPoint));
						Point newPoint = new Point(secondPoint.x, secondPoint.y + pointerSize);
						g2.draw(createLine(newPoint, mainPoint, k));

					} else if ((value & 32) > 0) { // implementacja
						g2.draw(createPointer(secondPoint));
						Point newPoint = new Point(secondPoint.x, secondPoint.y + pointerSize);

						BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
								new float[] { 10.0f }, 0.0f);
						g2.setStroke(dashed);
						g2.draw(createLine(newPoint, mainPoint, k));
						g2.setStroke(new BasicStroke());

					} else if ((value & (4 | 8)) == 4 + 8) { // aggregacja
						g2.draw(createRhombus(mainPoint));
						Point newPoint = new Point(mainPoint.x, mainPoint.y + pointerSize * 2);
						g2.draw(createLine(newPoint, secondPoint, k));
						g2.drawString("*", newPoint.x + gapSize, newPoint.y + gapSize * 2);
						g2.drawString("1", secondPoint.x + gapSize, secondPoint.y + gapSize * 2);

					} else if ((value & (1 | 4)) == 1 + 4) { // kompozycja
						g2.fill(createRhombus(mainPoint));
						Point newPoint = new Point(mainPoint.x, mainPoint.y + pointerSize * 2);
						g2.draw(createLine(newPoint, secondPoint, k));
						g2.drawString("*", newPoint.x + gapSize, newPoint.y + gapSize * 2);
						g2.drawString("1", secondPoint.x + gapSize, secondPoint.y + gapSize * 2);

					} else if ((value & 2) > 0) { // associacja
						g2.draw(createLine(mainPoint, secondPoint, k));
						g2.draw(createEmptyPointer(secondPoint));
						g2.drawString("1", mainPoint.x + gapSize, mainPoint.y);
						g2.drawString("1", secondPoint.x + gapSize, secondPoint.y + gapSize * 2);

					} else if ((value & 1) > 0 || (value & 8) > 0) {// zaleznosc-
																	// depencency(przerywana
																	// kreska)
						BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
								new float[] { 10.0f }, 0.0f);
						g2.setStroke(dashed);
						g2.draw(createLine(mainPoint, secondPoint, k));
						g2.setStroke(new BasicStroke());

					}
				}
			}
		}
	}

	private Point getEndPoint(GuiClass p_guiClass, int p_relationNumber) {
		Point point = p_guiClass.getEndPoint(p_relationNumber);
		point.x += p_guiClass.getLocation().x;
		point.y += p_guiClass.getLocation().y + 31;

		return point;
	}

	private Shape createPointer(Point point) {

		double size = 12;

		Path2D pointer = new Path2D.Double();
		pointer.moveTo(point.getX(), point.getY());
		pointer.lineTo(point.getX() + size, point.getY() + size * 4 / 3);
		pointer.lineTo(point.getX() - size, point.getY() + size * 4 / 3);
		pointer.lineTo(point.getX(), point.getY());

		return pointer;
	}

	private Shape createEmptyPointer(Point point) {

		double size = 12;

		Path2D pointer = new Path2D.Double();
		pointer.moveTo(point.getX(), point.getY());
		pointer.lineTo(point.getX() + size, point.getY() + size * 4 / 3);
		pointer.moveTo(point.getX(), point.getY());
		pointer.lineTo(point.getX() - size, point.getY() + size * 4 / 3);

		return pointer;
	}

	private Shape createRhombus(Point point) {

		double size = 12;

		Path2D pointer = new Path2D.Double();
		pointer.moveTo(point.getX(), point.getY());
		pointer.lineTo(point.getX() + size, point.getY() + size * 4 / 3);
		pointer.lineTo(point.getX(), point.getY() + size * 8 / 3);
		pointer.lineTo(point.getX() - size, point.getY() + size * 4 / 3);
		pointer.lineTo(point.getX(), point.getY());

		return pointer;

	}

	private Shape createLine(Point start, Point end, int number) {

		double prev;

		Path2D pointer = new Path2D.Double();
		pointer.moveTo(start.getX(), start.getY());
		pointer.lineTo(start.getX(), prev = start.getY() + 5 + number);
		pointer.lineTo(end.getX(), prev);
		pointer.lineTo(end.getX(), end.getY());

		return pointer;
	}
}
