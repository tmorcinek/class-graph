package com.morcinek.uml.logic.object;

import com.morcinek.uml.logic.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldObject extends DeclarationObject {

    protected int dimension;

    protected Type type;

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void plus(int dimension) {
        this.dimension += dimension;
    }

    public int getDimension() {
        return dimension;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Map<String, Integer> globalTypes() {
        Map<String, Integer> typesMap = new HashMap<String, Integer>();

        List<String> fullTypesNameList = type.getFullTypesNameList();
        if (type.getFullTypeName() != null) {
            typesMap.put(type.getFullTypeName(), getDimensionValue());
            fullTypesNameList.remove(type.getFullTypeName());
        }
        for (String type : fullTypesNameList) {
            typesMap.put(type, 4);
        }

        return typesMap;
    }

    /**
     * Values of integer:
     * 2 - object (association)
     * 4 - array (composition)
     *
     * @return <code>2</code> if field is object and <code>4</code> if filed is array.
     */
    private int getDimensionValue() {
        if (type.getDimension() + dimension > 0) {
            return 4;
        }
        return 2;
    }

    public String toString() {
        String string = range + " " + type + " " + name;
        for (int i = 0; i < dimension; i++) {
            string = string.concat("[]");
        }
        return string;
    }
}
