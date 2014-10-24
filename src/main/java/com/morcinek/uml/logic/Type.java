package com.morcinek.uml.logic;

import java.util.LinkedList;
import java.util.List;

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

    public String getFullTypeName() {
        return fullTypeName;
    }

    public String toString() {
        String ret = typeName;
        // adds subtypes
        if (!this.subTypes.isEmpty()) {
            ret = ret.concat("<");
            int i = 0;
            for (Type subType : this.subTypes) {
                if (i > 0) ret = ret.concat(", ");
                ret = ret.concat(subType.toString());
                i++;
            }
            ret = ret.concat(">");
        }
        // adds array
        for (int i = 0; i < this.dimension; i++) {
            ret = ret.concat("[]");
        }

        return ret;
    }

}
