package com.morcinek.uml.relations.extraction.model;

import com.morcinek.uml.relations.RelationType;
import com.morcinek.uml.relations.extraction.Type;
import com.morcinek.uml.relations.extraction.model.DeclarationObject;

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
            typesMap.put(type, RelationType.FIELD_COMPOSITION);
        }

        return typesMap;
    }

    /**
     * Method checks if type is array.
     *
     * @return <code>RelationType.FIELD_ASSOCIATION</code> if field is object and <code>RelationType.FIELD_COMPOSITION</code> if filed is array.
     * @see com.morcinek.uml.relations.RelationType
     */
    private int getDimensionValue() {
        if (type.getDimension() + dimension > 0) {
            return RelationType.FIELD_COMPOSITION;
        }
        return RelationType.FIELD_ASSOCIATION;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(range);
        stringBuilder.append(" ");
        stringBuilder.append(type);
        stringBuilder.append(" ");
        stringBuilder.append(name);
        for (int i = 0; i < dimension; i++) {
            stringBuilder.append("[]");
        }
        return stringBuilder.toString();
    }
}
