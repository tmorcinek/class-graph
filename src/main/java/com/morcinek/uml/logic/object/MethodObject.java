package com.morcinek.uml.logic.object;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public String toString() {
        String string = super.toString() + "(";
        int i = 0;
        for (Type type : arguments.keySet()) {
            if (i > 0) {
                string = string.concat(", ");
            }
            string = string.concat(type + " ");
            i++;
        }
        string = string.concat(")");
        return string;
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
            typesMap.put(typeName, 1);
        }
    }

    private void addMethodArguments(Map<String, Integer> typesMap) {
        for (Type argumentType : arguments.keySet()) {
            for (String typeName : argumentType.getFullTypesNameList()) {
                typesMap.put(typeName, 8);
            }
        }
    }

    private void addMethodBodyVariables(Map<String, Integer> typesMap) {
        for (Type variableType : variables) {
            for (String typeName : variableType.getFullTypesNameList()) {
                typesMap.put(typeName, 1);
            }
        }
    }
}