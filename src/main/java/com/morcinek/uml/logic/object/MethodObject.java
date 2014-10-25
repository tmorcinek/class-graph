package com.morcinek.uml.logic.object;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.morcinek.uml.relations.RelationType;
import com.morcinek.uml.logic.Type;

/**
 * Can contain methods and constructors
 * if is constructor
 * this.type = "constructor"
 * this.name = "ClassName"
 *
 * @author Tomek
 */
public class MethodObject extends FieldObject {

    protected Map<Type, String> arguments = new HashMap<Type, String>();

    protected List<Type> variables = new LinkedList<Type>();

    public void addArgument(Type type, String name) {
        arguments.put(type, name);
    }

    public void addVariable(Type type) {
        this.variables.add(type);
    }

    public Map<Type, String> getArguments() {
        return arguments;
    }

    public Map<String, Integer> globalTypes() {
        Map<String, Integer> typesMap = new HashMap<String, Integer>();
        addMethodReturnType(typesMap);
        addMethodArguments(typesMap);
        addMethodBodyVariables(typesMap);
        return typesMap;
    }


    private void addMethodReturnType(Map<String, Integer> typesMap) {
        for (String typeName : type.getFullTypesNameList()) {
            typesMap.put(typeName, RelationType.FIELD_BODY);
        }
    }

    private void addMethodArguments(Map<String, Integer> typesMap) {
        for (Type argumentType : arguments.keySet()) {
            for (String typeName : argumentType.getFullTypesNameList()) {
                typesMap.put(typeName, RelationType.METHOD_ASSOCIATION);
            }
        }
    }

    private void addMethodBodyVariables(Map<String, Integer> typesMap) {
        for (Type variableType : variables) {
            for (String typeName : variableType.getFullTypesNameList()) {
                typesMap.put(typeName, RelationType.FIELD_BODY);
            }
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        int i = 0;
        for (Type type : arguments.keySet()) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(type);
            stringBuilder.append(" ");
            i++;
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
