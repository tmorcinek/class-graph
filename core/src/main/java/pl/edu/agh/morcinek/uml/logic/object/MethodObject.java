package pl.edu.agh.morcinek.uml.logic.object;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.edu.agh.morcinek.uml.logic.Type;

/**
 * Can contain methods and constructors
 * if is constructor
 *     this.type = "constructor"
 *     this.name = "ClassName"
 *     
 * @author Tomek
 *
 */
public class MethodObject extends FieldObject {
	
	protected Map<Type, String> arguments = new HashMap<Type, String>();
	
	protected List<Type> variables = new LinkedList<Type>();
	
	public void addArgument(Type p_type, String p_name){
		this.arguments.put(p_type, p_name);
	}
	
	public void addVariable(Type p_type){
		this.variables.add(p_type);
	}
	
	public Map<Type, String> getArguments(){
		return this.arguments;
	}
	
	public String toString(){
		String string = super.toString() +"(";
		int i =0;
		for(Type type: this.arguments.keySet()){
			if(i > 0) string = string.concat(", ");
			string = string.concat(type +" ");
			i++;
		}
		string = string.concat(")");
		return string;
	}
	
	
	public Map<String, Integer> globalTypes(){
		Map<String, Integer> typesMap = new HashMap<String, Integer>();
		
		// wartosc zwracana 
		for(String typeName: this.type.getFullTypesNameList()){
			typesMap.put(typeName, 1);
		}
		
		// argumenty metody
		for(Type type: this.arguments.keySet()){
			for(String typeName: type.getFullTypesNameList()){
				typesMap.put(typeName, 8);
			}
		}
		
		// zmienne wewnatrz ciala funkcji
		for(Type type: this.variables){
			for(String typeName: type.getFullTypesNameList()){
				typesMap.put(typeName, 1);
			}
		}
		
		return typesMap;
	}
}
