package com.morcinek.uml.relations.extraction;

import com.morcinek.uml.relations.extraction.model.ClassObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2014 Tomasz Morcinek. All rights reserved.
 */
public class RelationsExtractor {

    private final ClassObject mainObject;

    public RelationsExtractor(ClassObject mainObject) {
        this.mainObject = mainObject;
    }

    public Map<String, ClassObject> getClassObjectsMap() {
        Map<String, ClassObject> classObjectsMap = new HashMap<String, ClassObject>();

        for (ClassObject classObject : mainObject.getClassDeclarations()) {
            classObjectsMap.put(classObject.getFullClassName(), classObject);
        }

        return classObjectsMap;
    }

    public HashMap<String, HashMap<String, Integer>> getClassRelations() {

        HashMap<String, HashMap<String, Integer>> classRelations = new HashMap<String, HashMap<String, Integer>>();
        Map<String[], Integer> relations = getRelations();
        for (String[] classes : relations.keySet()) {

            if (classRelations.containsKey(classes[0])) {
                classRelations.get(classes[0]).put(classes[1], relations.get(classes));
            } else {
                HashMap<String, Integer> relMap = new HashMap<String, Integer>();
                relMap.put(classes[1], relations.get(classes));
                classRelations.put(classes[0], relMap);
            }
        }

        return classRelations;
    }

    private Map<String[], Integer> getRelations() {

        Map<String[], Integer> relations = new HashMap<String[], Integer>();

        Map<String, ClassObject> classObjectMap = getClassObjectsMap();    // wszystkie klasy z wszystkich plikow
        for (ClassObject classObject : classObjectMap.values()) {
            Map<String, Integer> typesMap = classObject.globalTypes();    // wszystkie typy kt�re wyst�puj� w przegladanej klasie 'classObject'

            for (String typeName : typesMap.keySet()) {        // dany typ wyst�puj�cy w klasie 'classObject'

                // dany typ jest w mapie wszystkich klas, drugi warunek odnosnie stworzenie instancji obiektu w funkcji statycznej
                if (classObjectMap.containsKey(typeName) && !classObject.getFullClassName().equals(typeName)) {
                    String[] pair = {classObject.getFullClassName(), typeName};
                    Integer value = typesMap.get(typeName);
                    relations.put(pair, value);
                }
            }
        }

        return relations;
    }
}
