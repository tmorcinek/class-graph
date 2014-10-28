package com.morcinek.uml.relations.extraction;

import com.morcinek.uml.parser.java.JavaParser.ModifierSet;
import com.morcinek.uml.relations.extraction.model.ClassObject;
import com.morcinek.uml.relations.extraction.model.FieldObject;
import com.morcinek.uml.relations.extraction.model.MethodObject;
import com.morcinek.uml.util.ElementUtil;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2014 Tomasz Morcinek. All rights reserved.
 */
public class ObjectExtractor {

    // keep all objects
    private ClassObject mainObject = new ClassObject();

    // global processed element's importsMap
    private Map<String, String> currentClassNameImportNameMap = new HashMap<String, String>();

    // current processed element's package
    private String currentPackage;

    public ClassObject getObject() {
        return mainObject;
    }

    public void addFileElementToImports(Element element) {

        List<Element> types = ElementUtil.getChildElementsByName(element, "type");

        // setting package
        this.currentPackage = getPackageName(element);

        // add types to class name imports map
        for (Element type : types) {
            String typeName = type.getAttribute("name");
            String importName = this.currentPackage + "." + typeName;
            this.currentClassNameImportNameMap.put(typeName, importName);
        }
    }

    public void addFileElement(Element fileElement) {

        // setting package
        this.currentPackage = getPackageName(fileElement);

        // process imports
        for (String importName : getImportsNameList(fileElement)) {
            String[] packagesNames = importName.split("\\.");
            String className = packagesNames[packagesNames.length - 1];
            if (!className.startsWith("*")) {
                this.currentClassNameImportNameMap.put(className, importName);
            }
        }

        List<Element> types = ElementUtil.getChildElementsByName(fileElement, "type");

        // processing types
        for (Element type : types) {
            processType(type, this.mainObject);
        }
    }

    private void processType(Element typeElement, ClassObject classObject) {

        // create ClassObject
        ClassObject newClass = new ClassObject();
        newClass.setPackageName(this.currentPackage);
        newClass.setName(typeElement.getAttribute("name"));

        // inserting newClass to map
        classObject.addDeclaration(newClass);

        // informations about type from modifiers
        processModifiers(typeElement, newClass);

        // set extends
        for (Element extend : ElementUtil.getChildElementsByName(typeElement, "extends")) {
            // getting type element
            for (Element type : ElementUtil.getChildElementsByName(extend, "type")) {
                Type newType = createType(type);
                newClass.addExtends(newType);
            }
        }

        // sets implements
        for (Element implement : ElementUtil.getChildElementsByName(typeElement, "implements")) {
            for (Element type : ElementUtil.getChildElementsByName(implement, "type")) {
                Type newType = createType(type);
                newClass.addImplements(newType);
            }
        }

        // sets declarations
        Element declarations = ElementUtil.getChildElementsByName(typeElement, "declarations").get(0);
        // fields
        for (Element field : ElementUtil.getChildElementsByName(declarations, "field")) {
            processField(field, newClass);
        }

        // methods
        for (Element method : ElementUtil.getChildElementsByName(declarations, "method")) {
            processMethod(method, newClass);
        }

        // constructors
        for (Element constructor : ElementUtil.getChildElementsByName(declarations, "constructor")) {
            processMethod(constructor, newClass);
        }

        // process class inside
        for (Element type : ElementUtil.getChildElementsByName(declarations, "type")) {
            processType(type, newClass);
        }
    }

    private void processModifiers(Element element, ClassObject classObject) {
        // getModifiers
        String modifiers = element.getAttribute("modifiers");
        ModifierSet m = new ModifierSet();

        // set if abstract
        if (m.isAbstract(Integer.valueOf(modifiers))) {
            classObject.setClassType("abstract " + element.getAttribute("type"));
        } else {
            classObject.setClassType(element.getAttribute("type"));
        }

        // setting range
        classObject.setRange(modifiers);
    }

    private void processField(Element fieldElement, ClassObject classObject) {
        // dodajemy do compozitoin

        // get type
        Type type = createType(ElementUtil.getFirstChildElementByName(fieldElement, "type"));

        // get modifiers
        String modifiers = fieldElement.getAttribute("modifiers");

        // set declarations
        for (Element declarator : ElementUtil.getChildElementsByName(fieldElement, "declarator")) {
            String name = declarator.getAttribute("name");
            Integer dimension = Integer.valueOf(declarator.getAttribute("dimension"));
            // seting filed object
            FieldObject fieldObject = new FieldObject();
            fieldObject.setType(type);
            fieldObject.setRange(modifiers);
            fieldObject.setName(name);
            fieldObject.setDimension(dimension);
            classObject.addDeclaration(fieldObject);
        }
    }

    private void processMethod(Element methodElement, ClassObject classObject) {

        MethodObject methodObject = new MethodObject();

        // get type
        Type type;
        Element resultType = ElementUtil.getFirstChildElementByName(methodElement, "type");
        if (resultType != null) {
            type = createType(resultType);
        } else {
            type = new Type("void");
        }
        methodObject.setType(type);

        // get modifiers
        methodObject.setRange(methodElement.getAttribute("modifiers"));

        // get name
        methodObject.setName(methodElement.getAttribute("name"));

        // get parameters
        for (Element parameter : ElementUtil.getChildElementsByName(methodElement, "parameter")) {
            // get parameter type
            Type paramType = createType(ElementUtil.getFirstChildElementByName(parameter, "type"));

            // get declarator
            Element declarator = ElementUtil.getFirstChildElementByName(parameter, "declarator");
            String name = declarator.getAttribute("name");
            // dimension added to type
            int dimension = Integer.valueOf(declarator.getAttribute("dimension"));
            paramType.addDimension(dimension);

            // adding parameter to methodObject
            methodObject.addArgument(paramType, name);
        }

        // get throw's --adding as variable
        for (Element throwClass : ElementUtil.getChildElementsByName(methodElement, "throw")) {
            // creating type
            Type throwType = new Type(throwClass.getAttribute("name"));
            String fullTypeName = this.currentClassNameImportNameMap.get(throwType.getTypeName());
            if (fullTypeName != null) {
                type.setFullTypeName(fullTypeName);
            }

            methodObject.addVariable(throwType);
        }

        // get block variables
        processBlock(ElementUtil.getFirstChildElementByName(methodElement, "block"), methodObject);

        classObject.addDeclaration(methodObject);
    }

    private void processBlock(Element blockElement, MethodObject methodObject) {

        // get types from type
        for (Element type : ElementUtil.getChildElementsByName(blockElement, "type")) {
            Type newType = createType(type);
            methodObject.addVariable(newType);
        }


        // get types from localVariable's
        for (Element localVariable : ElementUtil.getChildElementsByName(blockElement, "localVariable")) {
            // type
            Element type = ElementUtil.getFirstChildElementByName(localVariable, "type");
            Type newType = createType(type);
            methodObject.addVariable(newType);

            // get classType's
            for (Element classType : ElementUtil.getChildElementsByName(localVariable, "classType")) {
                // decorating 'classType' with 'type'
                type = ElementUtil.createElement(classType, "type");
                type.appendChild(classType.cloneNode(true));

                // creating new type
                newType = createType(type);
                methodObject.addVariable(newType);
            }
        }

        // get classType
        for (Element classType : ElementUtil.getChildElementsByName(blockElement, "classType")) {
            Element type = ElementUtil.createElement(classType, "type");
            type.appendChild(classType.cloneNode(true));
            Type newType = createType(type);
            methodObject.addVariable(newType);
        }
    }

    private Type createType(Element typeElement) {
        Type type = new Type();

        int dimension = 0;
        if (typeElement.hasAttribute("dimension")) {
            dimension = Integer.valueOf(typeElement.getAttribute("dimension"));
        }
        type.addDimension(dimension);

        String typeName;
        if (typeElement.hasAttribute("name")) {
            // primitive type
            typeName = typeElement.getAttribute("name");
            type.setName(typeName);

        } else {
            // reference type
            Element clasType = ElementUtil.getFirstChildElementByName(typeElement, "classType");

            typeName = clasType.getAttribute("name");
            type.setName(typeName);

            Element arguments = ElementUtil.getFirstChildElementByName(clasType, "arguments");
            if (arguments != null) {
                for (Element childType : ElementUtil.getChildElementsByName(arguments, "type")) {
                    type.addType(createType(childType));
                }
            }
            String fullTypeName = this.currentClassNameImportNameMap.get(typeName);
            if (fullTypeName != null) {
                type.setFullTypeName(fullTypeName);
            }
        }

        return type;
    }

    /**
     * @return package name or empty string if no package is defined
     */
    private static String getPackageName(Element element) {
        List<Element> packages = ElementUtil.getChildElementsByName(element, "package");
        String name = "";
        if (!packages.isEmpty()) {
            name = packages.get(0).getAttribute("name");
        }
        return name;
    }

    private static List<String> getImportsNameList(Element element) {
        List<String> nameList = new LinkedList<String>();
        List<Element> imports = ElementUtil.getChildElementsByName(element, "imports");
        if (!imports.isEmpty()) {
            Element importsElement = imports.get(0);
            for (Element importElement : ElementUtil.getChildElementsByName(importsElement, "import")) {
                nameList.add(importElement.getAttribute("name"));
            }
        }
        return nameList;
    }
}


