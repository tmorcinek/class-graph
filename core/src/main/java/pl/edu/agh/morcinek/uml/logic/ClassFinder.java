package pl.edu.agh.morcinek.uml.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.w3c.dom.Element;

import pl.edu.agh.morcinek.uml.gui.GuiView;
import pl.edu.agh.morcinek.uml.logic.object.ClassObject;
import pl.edu.agh.morcinek.uml.parser.java.JavaParser;

public class ClassFinder {

	private static FileFilter javaFilter = new FileFilter() {

		public boolean accept(File pathname) {
			String fileName = pathname.getName();
			return pathname.isFile() && fileName.endsWith(".java");
		}
	};

	private static FileFilter dirFilter = new FileFilter() {

		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	};


	private static void findFiles(File p_dir, Transform p_transform) {

		List<Element> parsedElements = new LinkedList<Element>();

		for (File javaFile : p_dir.listFiles(javaFilter)) {
			parsedElements.add(JavaParser.main(javaFile.getAbsolutePath()));
		}

		// adding all elements from package
		for (Element parsedElement : parsedElements) {
			p_transform.addFileElementToImports(parsedElement);
		}

		for (Element parsedElement : parsedElements) {
			p_transform.addFileElement(parsedElement);
		}

		for (File dir : p_dir.listFiles(dirFilter)) {
			findFiles(dir, p_transform);
		}
	}

	private static void parseFiles(String p_pathName, Transform p_transform) throws IllegalArgumentException {

		File dir = new File(p_pathName);
		if (!dir.isDirectory())
			throw new IllegalArgumentException(dir.getAbsolutePath() + " is not a directory");

		findFiles(dir, p_transform);
	}
	
	private static String developmentMode(String[] args, boolean isDeveloped){
		String pathName;
		if(isDeveloped){
			  pathName = "Test1";
		} else {
			pathName = args[0];
		}
		
		return pathName;
	}

	public static void main(String[] args) {
		try {
			
			String pathName = developmentMode(args, true);

			Transform transform = new Transform();
			parseFiles(pathName, transform);
			final HashMap<String, HashMap<String, Integer>> relations = transform.getClassRelations();
			for (String mainName : relations.keySet()) {
				HashMap<String, Integer> map = relations.get(mainName);
				System.out.println(mainName);
				for (String name : map.keySet()) {
					System.out.println(" > " + name + " : " + map.get(name));
				}
			}
			final ClassObject files = transform.getObject();
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					GuiView guiView = new GuiView("diagram", 3, 3);
					for (ClassObject object : files.getClassDeclarations()) {
						guiView.addClass(object);
					}

					guiView.setRelations(relations);

					guiView.showDiagramGrid();
				}
			});
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error occured!!\nThere is no directory path in execution argument!");
		} catch (Exception e) {
			System.out.println("Error occured!!\n"+ e.getMessage() +"!");
		}
	}
}
