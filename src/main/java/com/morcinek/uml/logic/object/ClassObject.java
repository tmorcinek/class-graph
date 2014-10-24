
package com.morcinek.uml.logic.object;

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


    public void addDeclaration(DeclarationObject p_field) {
        this.declarations.add(p_field);
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

        // extendsClass
        for (Type type : this.extendsList) {
//			List<String> fullTypesNameList = type.getFullTypesNameList();
            if (type.getFullTypeName() != null) {
                typesMap.put(type.getFullTypeName(), 16);
//				fullTypesNameList.remove(type.getFullTypeName());			
            }

//			for(String typeName: fullTypesNameList){
//				typesMap.put(typeName, 4);
//			}
        }

        // implementsClass
        for (Type type : this.implementsList) {
//			List<String> fullTypesNameList = type.getFullTypesNameList();
            if (type.getFullTypeName() != null) {
                typesMap.put(type.getFullTypeName(), 32);
//				fullTypesNameList.remove(type.getFullTypeName());			
            }

//			for(String typeName: fullTypesNameList){
//				typesMap.put(typeName, 4);
//			}
        }

        // declarations
        for (DeclarationObject declaration : this.declarations) {
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

        return typesMap;
    }


    //XXX poprawic
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