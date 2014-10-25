
package com.morcinek.uml.logic.object;

import com.morcinek.uml.relations.RelationType;
import com.morcinek.uml.logic.Type;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Class object is an instance which contain set of values describing its
 * associations with other classes
 *
 * @author Tomek Morcinek
 */
public class ClassObject extends DeclarationObject {

    private String classType;

    private String packageName;

    private List<DeclarationObject> declarations = new LinkedList<DeclarationObject>();

    private List<Type> implementsList = new LinkedList<Type>();

    private List<Type> extendsList = new LinkedList<Type>();


    public void addImplements(Type implementClass) {
        this.implementsList.add(implementClass);
    }

    public void addExtends(Type extendClass) {
        this.extendsList.add(extendClass);
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getFullClassName() {
        return packageName + "." + this.name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassType() {
        return classType;
    }


    public void addDeclaration(DeclarationObject declarationObject) {
        this.declarations.add(declarationObject);
    }

    //added to for printing
    public List<ClassObject> getClassDeclarations() {
        List<ClassObject> classObjects = new LinkedList<ClassObject>();
        for (DeclarationObject declarationObject : this.declarations) {
            if (declarationObject instanceof ClassObject) {
                classObjects.add((ClassObject) declarationObject);
            }
        }

        return classObjects;
    }

    public List<DeclarationObject> getDeclarations() {
        return this.declarations;
    }

    @Override
    public Map<String, Integer> globalTypes() {
        Map<String, Integer> typesMap = new HashMap<String, Integer>();
        addGeneralizations(typesMap);
        addImplementations(typesMap);
        addDeclarations(typesMap);
        return typesMap;
    }

    private void addGeneralizations(Map<String, Integer> typesMap) {
        for (Type type : extendsList) {
            if (type.getFullTypeName() != null) {
                typesMap.put(type.getFullTypeName(), RelationType.GENERALIZATION);
            }
        }
    }

    private void addImplementations(Map<String, Integer> typesMap) {
        for (Type type : implementsList) {
            if (type.getFullTypeName() != null) {
                typesMap.put(type.getFullTypeName(), RelationType.IMPLEMENTATION);
            }
        }
    }

    private void addDeclarations(Map<String, Integer> typesMap) {
        for (DeclarationObject declaration : declarations) {
            Map<String, Integer> declarationTypesMap = declaration.globalTypes();
            // iterujemy po kolejnych typach z deklaracji
            for (String typeName : declarationTypesMap.keySet()) {
                // if typeName already exist in typesMap
                if (typesMap.containsKey(typeName)) {
                    typesMap.put(typeName, typesMap.get(typeName) | declarationTypesMap.get(typeName));
                } else {
                    typesMap.put(typeName, declarationTypesMap.get(typeName));
                }

            }
        }
    }

    @Override
    public String toString() {
        String headline = this.range + " <<" + this.classType + ">> " + this.name + "\n";
        StringBuffer methods = new StringBuffer();
        StringBuffer fields = new StringBuffer();
        StringBuffer classes = new StringBuffer();

        for (DeclarationObject declarationObject : this.declarations) {
            // fields
            if (declarationObject instanceof FieldObject && !(declarationObject instanceof MethodObject)) {
                fields.append(declarationObject.toString());
                fields.append("\n");
            }
            // methods
            if (declarationObject instanceof MethodObject) {
                methods.append(declarationObject.toString());
                methods.append("\n");
            }
            // classes
            if (declarationObject instanceof ClassObject) {
                classes.append(declarationObject.toString());
            }
        }

        return headline + fields.toString() + methods.toString() + classes.toString();
    }
}
