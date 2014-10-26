package com.morcinek.uml.relations.extraction;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright 2014 Tomasz Morcinek. All rights reserved.
 */
public class Type {

    private List<Type> subTypes = new LinkedList<Type>();

    private String typeName;
    private String fullTypeName;

    private int dimension;

    public Type() {
    }

    public Type(String typeName) {
        this.typeName = typeName;
    }

    public void setName(String typeName) {
        this.typeName = typeName;
    }

    public void setFullTypeName(String fullTypeName) {
        this.fullTypeName = fullTypeName;
    }

    public void addDimension(int dimension) {
        this.dimension += dimension;
    }

    public void addType(Type type) {
        this.subTypes.add(type);
    }

    public int getDimension() {
        return dimension;
    }

    public List<String> getFullTypesNameList() {
        List<String> nameList = new LinkedList<String>();
        if (fullTypeName != null) {
            nameList.add(fullTypeName);
        }
        for (Type subType : subTypes) {
            nameList.addAll(subType.getFullTypesNameList());
        }
        return nameList;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getFullTypeName() {
        return fullTypeName;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(typeName);
        if (!subTypes.isEmpty()) {
            stringBuilder.append("<");
            addSubtypes(stringBuilder);
            stringBuilder.append(">");
        }
        addDimensions(stringBuilder);

        return stringBuilder.toString();
    }

    private void addDimensions(StringBuilder stringBuilder) {
        for (int i = 0; i < dimension; i++) {
            stringBuilder.append("[]");
        }
    }

    private void addSubtypes(StringBuilder stringBuilder) {
        for (int i = 0; i < subTypes.size(); i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(subTypes.get(i).toString());
        }
    }
}
