package com.morcinek.uml.relations;

import com.morcinek.uml.relations.extraction.ObjectExtractor;
import com.morcinek.uml.parser.java.JavaParser;
import com.morcinek.uml.relations.extraction.RelationsExtractor;
import com.morcinek.uml.relations.filters.DirectoryFileFilter;
import com.morcinek.uml.relations.filters.JavaFileFilter;
import org.w3c.dom.Element;

import javax.xml.ws.Holder;
import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * Copyright 2014 Tomasz Morcinek. All rights reserved.
 */
public class RelationsProvider {

    private final FileFilter javaFilter = new JavaFileFilter();

    private final FileFilter dirFilter = new DirectoryFileFilter();

    public Map<String, HashMap<String, Integer>> provideRelations(String pathName, List<String> logMessages) {
        ObjectExtractor objectExtractor = new ObjectExtractor();
        processDirectoryFiles(getDirectoryFile(pathName), objectExtractor, logMessages);
        RelationsExtractor relationsExtractor = new RelationsExtractor(objectExtractor.getObject());
        return relationsExtractor.getClassRelations();
    }

    private File getDirectoryFile(String pathName) {
        File dir = new File(pathName);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not a directory");
        }
        return dir;
    }

    private void processDirectoryFiles(File directory, ObjectExtractor objectExtractor, List<String> logMessages) {

        List<Element> parsedElements = new LinkedList<Element>();

        for (File javaFile : directory.listFiles(javaFilter)) {
            Holder<String> stringHolder = new Holder<String>();
            try {
                parsedElements.add(JavaParser.main(javaFile.getAbsolutePath(), stringHolder));
            } catch (Exception e) {
                stringHolder.value = String.format("Other error '%s' in file '%s'.", e.getMessage(), javaFile.getAbsolutePath());
            } finally {
                if (logMessages != null) {
                    logMessages.add(stringHolder.value);
                }
            }
        }

        // adding all elements from package
        for (Element parsedElement : parsedElements) {
            objectExtractor.addFileElementToImports(parsedElement);
        }

        for (Element parsedElement : parsedElements) {
            objectExtractor.addFileElement(parsedElement);
        }

        for (File dir : directory.listFiles(dirFilter)) {
            processDirectoryFiles(dir, objectExtractor, logMessages);
        }
    }
}
