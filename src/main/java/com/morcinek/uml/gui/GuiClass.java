package com.morcinek.uml.gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JComponent;

public class GuiClass extends JComponent {

	private static final int MAX_NUMBER = 5;
	private static final int HORIZONTAL_SKIP_SIZE = 8;
	private static final int VERTICAL_SKIP_SIZE = 10;
	private static final int FONT_SIZE = 14;
	private static final int MAX_STRING_WIDTH = 40;

	private Map<Integer, Integer> relations = new HashMap<Integer, Integer>();

	private ClassPrinter classPrinter;

	private int width;

	public GuiClass(ClassPrinter p_object) {
		super();
		this.classPrinter = p_object;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Courier New", Font.PLAIN, FONT_SIZE));

		String[] classText = classPrinter.toString().split("\n");
		for (int i = 0; i < classText.length; i++) {
			String line = (classText[i].length() > MAX_STRING_WIDTH) ? classText[i].substring(0, MAX_STRING_WIDTH)
					+ "..." : classText[i];
			this.width = Math.max(this.width, line.length());
			g2.drawString(line, 1, (i + 1) * VERTICAL_SKIP_SIZE);
		}

		int prev = 0;
		for (Integer line : classPrinter.getLineSeparators()) {
			g2.drawLine(1, (line+prev) * VERTICAL_SKIP_SIZE + 1, this.width * HORIZONTAL_SKIP_SIZE + 1, (line+prev)
					* VERTICAL_SKIP_SIZE + 1);
			prev += line;
		}

		g2.drawRect(1, 1, this.width * HORIZONTAL_SKIP_SIZE + 1, (classText.length) * VERTICAL_SKIP_SIZE);

	}

	public Point getEndPoint(int p_relationNumber) {
		if (relations.size() >= MAX_NUMBER)
			throw new NullPointerException("Too many connections with class");

		int number;
		if (relations.containsKey(p_relationNumber)) {
			number = relations.get(p_relationNumber);
		} else {
			number = relations.size() + 1;
			relations.put(p_relationNumber, number);
		}

		int width = this.width * HORIZONTAL_SKIP_SIZE;
		int height = (classPrinter.toString().split("\n").length) * VERTICAL_SKIP_SIZE;
		int newWidth = number * width/ MAX_NUMBER ;
		Point point = new Point(newWidth + new Random().nextInt(MAX_NUMBER), height);

		return point;
	}

}
