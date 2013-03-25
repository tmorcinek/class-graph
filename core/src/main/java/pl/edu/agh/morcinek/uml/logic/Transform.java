package pl.edu.agh.morcinek.uml.logic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import pl.edu.agh.morcinek.uml.logic.object.ClassObject;
import pl.edu.agh.morcinek.uml.logic.object.FieldObject;
import pl.edu.agh.morcinek.uml.logic.object.MethodObject;
import pl.edu.agh.morcinek.uml.parser.java.JavaParser.ModifierSet;
import pl.edu.agh.morcinek.uml.util.TreeUtil;

/**
 * 
 * @author Tomek
 *
 */
public class Transform {
	
	// keep all objects
	private ClassObject mainObject = new ClassObject();
	
	// global processed element's importsMap
	private Map<String,String> currentClassNameImportNameMap = new HashMap<String, String>();
	
	// current processed element's package
	private String currentPackage = null;
	
	
	public Map<String,ClassObject> getClassObjectsMap() {
		Map<String,ClassObject> classObjectsMap = new HashMap<String, ClassObject>();
		
		for(ClassObject classObject: this.mainObject.getClassDeclarations()){
			classObjectsMap.put(classObject.getFullClassName(), classObject);
		}
		
		return classObjectsMap;
	}
		
	/**
	 * association	2 
	 * composition 	1 & 4
	 * aggregation 	4 & 8
	 * dependency	1 | 8
	 * extends		16
	 * implements	32 
	 * 
	 * @return
	 */
	public Map<String[],Integer> getRelations(){
		
		Map<String[],Integer> relations = new HashMap<String[], Integer>();
		
		Map<String,ClassObject> classObjectMap = getClassObjectsMap();	// wszystkie klasy z wszystkich plikow
		for(ClassObject classObject: classObjectMap.values()){
			Map<String,Integer> typesMap = classObject.globalTypes();	// wszystkie typy które wystêpuj¹ w przegladanej klasie 'classObject'
			
			for(String typeName: typesMap.keySet()){		// dany typ wystêpuj¹cy w klasie 'classObject'
				
				// dany typ jest w mapie wszystkich klas, drugi warunek odnosnie stworzenie instancji obiektu w funkcji statycznej
				if(classObjectMap.containsKey(typeName) && !classObject.getFullClassName().equals(typeName)){
					String[] pair = { classObject.getFullClassName(), typeName };
					Integer value = typesMap.get(typeName);
					relations.put(pair, value);
				}
			}
		}
		
		return relations;
	}
	
	public HashMap<String, HashMap<String, Integer>> getClassRelations(){
		
		HashMap<String, HashMap<String, Integer>> classRelations = new HashMap<String, HashMap<String,Integer>>();
		Map<String[],Integer> relations = getRelations();
		for(String[] classes: relations.keySet()){
			
			if(classRelations.containsKey(classes[0])){
				classRelations.get(classes[0]).put(classes[1], relations.get(classes));
			} else {
				HashMap<String, Integer> relMap = new HashMap<String, Integer>();
				relMap.put(classes[1], relations.get(classes));
				classRelations.put(classes[0], relMap);
			}
		}
		
		return classRelations;
	}
	
	public void addFileElementToImports(Element p_element){
		
		List<Element> types = TreeUtil.getChildElementsByName(p_element, "type");
		
		// setting package
		this.currentPackage = getPackageName(p_element);
		
		// add types to class name imports map
		for(Element type: types){
			String typeName = type.getAttribute("name");
			String importName = this.currentPackage +"."+ typeName;
			this.currentClassNameImportNameMap.put(typeName, importName);
		}
	}
	
	public void addFileElement(Element p_element){
		
		// setting package
		this.currentPackage = getPackageName(p_element);
		
		// process imports
		for(String importName: getImportsNameList(p_element)){
			String[] packagesNames = importName.split("\\.");
			String className = packagesNames[packagesNames.length-1];
			if(!className.startsWith("*")){
				this.currentClassNameImportNameMap.put(className, importName);
			}
		}
		
		List<Element> types = TreeUtil.getChildElementsByName(p_element, "type");
		
		// processing types
		for(Element type: types){
			processType(type, this.mainObject);
		}
	}
	
	public void processType(Element p_element, ClassObject p_object){
		
		// create ClassObject
		ClassObject newClass = new ClassObject();
		newClass.setPackageName(this.currentPackage);
		newClass.setName(p_element.getAttribute("name"));
		
		// inserting newClass to map
		p_object.addDeclaration(newClass);
		
		// informations about type from modifiers
		processModifiers(p_element, newClass);
		
		// set extends 
		for(Element extend: TreeUtil.getChildElementsByName(p_element, "extends")){
			// getting type element
			for(Element type: TreeUtil.getChildElementsByName(extend, "type")){
				Type newType = createType(type);
				newClass.addExtends(newType);
			}
		}
		
		// sets implements
		for(Element implement: TreeUtil.getChildElementsByName(p_element, "implements")){
			for(Element type: TreeUtil.getChildElementsByName(implement, "type")){
				Type newType = createType(type);
				newClass.addImplements(newType);
			}			
		}
		
		// sets declarations
		Element declarations = TreeUtil.getChildElementsByName(p_element, "declarations").get(0);
		// fields 
		for(Element field : TreeUtil.getChildElementsByName(declarations, "field")){
			processField(field, newClass);
		}
		
		// methods
		for(Element method : TreeUtil.getChildElementsByName(declarations, "method")){
			processMethod(method, newClass);
		}
		
		// constructors
		for(Element constructor : TreeUtil.getChildElementsByName(declarations, "constructor")){
			processMethod(constructor, newClass);
		}
		
		// process class inside
		for(Element type : TreeUtil.getChildElementsByName(declarations, "type")){
			processType(type, newClass);
		}
	}

	private void processModifiers(Element p_element, ClassObject newClass) {
		// getModifiers
		String modifiers = p_element.getAttribute("modifiers");
		ModifierSet m = new ModifierSet();
		
		// set if abstract
		if(m.isAbstract(Integer.valueOf(modifiers))){
			newClass.setClassType("abstract " + p_element.getAttribute("type"));
		} else {
			newClass.setClassType(p_element.getAttribute("type"));
		}
		
		// setting range
		newClass.setRange(modifiers);		
	}
	
	private void processField(Element p_field, ClassObject classObject) {
		// dodajemy do compozitoin

		// get type
		Type type = createType(TreeUtil.getFirstChildElementByName(p_field, "type"));
		
		// get modifiers
		String modifiers = p_field.getAttribute("modifiers");
		
		// set declarations
		for(Element declarator: TreeUtil.getChildElementsByName(p_field, "declarator")){
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
	
	private void processMethod(Element p_method, ClassObject p_classObject) {
		
		MethodObject methodObject = new MethodObject();
		
		// get type
		Type type;
		Element resultType = TreeUtil.getFirstChildElementByName(p_method, "type");
		if(resultType != null){
			type = createType(resultType);
		} else {
			type = new Type("void");
		}
		methodObject.setType(type);
		
		// get modifiers
		methodObject.setRange(p_method.getAttribute("modifiers"));
		
		// get name
		methodObject.setName(p_method.getAttribute("name"));
		
		// get parameters
		for(Element parameter: TreeUtil.getChildElementsByName(p_method, "parameter")){
			// get parameter type
			Type paramType = createType(TreeUtil.getFirstChildElementByName(parameter, "type"));
			
			// get declarator
			Element declarator = TreeUtil.getFirstChildElementByName(parameter, "declarator");
			String name = declarator.getAttribute("name");
			// dimension added to type
			int dimension = Integer.valueOf(declarator.getAttribute("dimension"));
			paramType.addDimension(dimension);
			
			// adding parameter to methodObject
			methodObject.addArgument(paramType, name);
		}
		
		// get throw's --adding as variable
		for(Element throwClass : TreeUtil.getChildElementsByName(p_method, "throw")){
			// creating type
			Type throwType = new Type(throwClass.getAttribute("name"));
			String fullTypeName = this.currentClassNameImportNameMap.get(throwType);
			if (fullTypeName != null) {
				type.setFullTypeName(fullTypeName);
			}
			
			methodObject.addVariable(throwType);
		}
		
		// get block variables
		processBlock(TreeUtil.getFirstChildElementByName(p_method,"block"), methodObject);
		
		p_classObject.addDeclaration(methodObject);
	}
	
	/**
	 * 
	 * @param p_block assuming that this parameter is not null
	 * @param p_methodObject 
	 */
	private void processBlock(Element p_block, MethodObject p_methodObject){
		
		// get types from type
		for(Element type: TreeUtil.getChildElementsByName(p_block, "type")){
			Type newType = createType(type);
			p_methodObject.addVariable(newType);
		}
		
		
		// get types from localVariable's
		for(Element localVariable: TreeUtil.getChildElementsByName(p_block, "localVariable")){
			// type
			Element type = TreeUtil.getFirstChildElementByName(localVariable, "type");
			Type newType = createType(type);
			p_methodObject.addVariable(newType);
			
			// get classType's
			for(Element classType: TreeUtil.getChildElementsByName(localVariable, "classType")){
				// decorating 'classType' with 'type'
				type = TreeUtil.createElement(classType,"type");
				type.appendChild(classType.cloneNode(true));
				
				// creating new type
				newType = createType(type);
				p_methodObject.addVariable(newType);
			}
		}
		
		// get classType
		for(Element classType: TreeUtil.getChildElementsByName(p_block, "classType")){
			Element type = TreeUtil.createElement(classType,"type");
			type.appendChild(classType.cloneNode(true));
			Type newType = createType(type);
			p_methodObject.addVariable(newType);
		}
	}
	
	/**
	 * create object of class Type
	 * @param p_type
	 * @return
	 */
	private Type createType(Element p_type){
		Type type = new Type();
		
		int dimension = 0;
		if (p_type.hasAttribute("dimension")) {
			dimension = Integer.valueOf(p_type.getAttribute("dimension"));
		}
		type.addDimension(dimension);
		
		String typeName;
		if(p_type.hasAttribute("name")){
			// primitive type
			typeName = p_type.getAttribute("name");
			type.setName(typeName);
			
		} else {
			// reference type
			Element clasType = TreeUtil.getFirstChildElementByName(p_type, "classType");
			
			typeName = clasType.getAttribute("name");
			type.setName(typeName);
			
			Element arguments = TreeUtil.getFirstChildElementByName(clasType, "arguments");
			if(arguments != null){
				for(Element childType: TreeUtil.getChildElementsByName(arguments, "type")){
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
	 * 
	 * @param p_element
	 * @return package name or empty string if no package is defined
	 */
	private static String getPackageName(Element p_element){
		List<Element> packages = TreeUtil.getChildElementsByName(p_element,"package");
		String name = "";
		if(!packages.isEmpty()){
			name = packages.get(0).getAttribute("name");
		}
		return name;
	}
	
	private static List<String> getImportsNameList(Element p_element){
		List<String> nameList = new LinkedList<String>();
		Element importsElement = TreeUtil.getChildElementsByName(p_element,"imports").get(0);
		for(Element importElement: TreeUtil.getChildElementsByName(importsElement, "import")){
			nameList.add(importElement.getAttribute("name"));
		}
		return nameList;
	}
	
	//hasn't finished yet
	public ClassObject getObject(){
		return this.mainObject;
	}

}


