package com.morcinek.uml.gui;

import java.util.List;

public interface ClassPrinter {
	public String toString();
	public int getMaximumWidth();
	public int getNumberOfFields();
	public int getNumberOfMethods();
	public List<String> getGeneralize();
	public List<Integer> getLineSeparators();
}
