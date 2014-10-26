package com.morcinek.uml.relations;

import com.morcinek.uml.relations.extraction.Transform;
import com.morcinek.uml.parser.java.JavaParser;
import com.morcinek.uml.relations.filters.DirectoryFileFilter;
import com.morcinek.uml.relations.filters.JavaFileFilter;
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

    private final FileFilter javaFilter = new JavaFileFilter();

    private final FileFilter dirFilter = new DirectoryFileFilter();

    public Map<String, HashMap<String, Integer>> provideRelations(String pathName) {
        Transform transform = new Transform();
        processDirectoryFiles(getDirectoryFile(pathName), transform);
        return transform.getClassRelations();
    }

    private File getDirectoryFile(String pathName) {
        File dir = new File(pathName);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not a directory");
        }
        return dir;
    }

    private void processDirectoryFiles(File directory, Transform transform) {

        List<Element> parsedElements = new LinkedList<Element>();

        for (File javaFile : directory.listFiles(javaFilter)) {
            try {
                parsedElements.add(JavaParser.main(javaFile.getAbsolutePath()));
            } catch (Exception e) {
                System.out.println("Error in file: " + javaFile.getAbsolutePath());
                throw new RuntimeException(e);
            }
        }

        // adding all elements from package
        for (Element parsedElement : parsedElements) {
            transform.addFileElementToImports(parsedElement);
        }

        for (Element parsedElement : parsedElements) {
            transform.addFileElement(parsedElement);
        }

        for (File dir : directory.listFiles(dirFilter)) {
            processDirectoryFiles(dir, transform);
        }
    }
}
