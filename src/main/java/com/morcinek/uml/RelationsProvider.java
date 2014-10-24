package com.morcinek.uml;

import com.morcinek.uml.logic.Transform;
import com.morcinek.uml.parser.java.JavaParser;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2014 Tomasz Morcinek. All rights reserved.
 */
public class RelationsProvider {

    private FileFilter javaFilter = new FileFilter() {

        public boolean accept(File pathname) {
            String fileName = pathname.getName();
            return pathname.isFile() && fileName.endsWith(".java");
        }
    };

    private FileFilter dirFilter = new FileFilter() {

        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    };


    public Map<String, HashMap<String, Integer>> provideRelations(String pathName){
        Transform transform = new Transform();
        parseFiles(pathName, transform);
        return transform.getClassRelations();
    }

    private void findFiles(File p_dir, Transform p_transform) {

        List<Element> parsedElements = new LinkedList<Element>();

        for (File javaFile : p_dir.listFiles(javaFilter)) {
            try {
                parsedElements.add(JavaParser.main(javaFile.getAbsolutePath()));
            } catch (Exception e) {
                System.out.println("Error in file: " + javaFile.getAbsolutePath());
                throw new RuntimeException(e);
            }
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

    private void parseFiles(String p_pathName, Transform p_transform) throws IllegalArgumentException {

        File dir = new File(p_pathName);
        if (!dir.isDirectory())
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not a directory");

        findFiles(dir, p_transform);
    }
}
