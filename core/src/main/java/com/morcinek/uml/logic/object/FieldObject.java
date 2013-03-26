package com.morcinek.uml.logic.object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.morcinek.uml.logic.Type;

public class FieldObject extends DeclarationObject{

	protected int dimension;
	
	protected Type type;
	
	
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
	
	public void plus(int p_dimension){
		this.dimension += p_dimension;
	}
	
	public int getDimension() {
		return this.dimension;
	}
	
	public Type getType(){
		return this.type;
	}
	
	public void setType(Type p_type){
		this.type = p_type;
	}
	
	public Map<String, Integer> globalTypes(){
		Map<String, Integer> typesMap = new HashMap<String, Integer>();
		
		List<String> fullTypesNameList = this.type.getFullTypesNameList();
		if(this.type.getFullTypeName() != null){	// sprawdzanie typu glownego
			// sprawdzamy czy nie jest tablica 
			int dim = 2;
			if(this.type.getDimension() + this.dimension > 0){
				dim = 4;
			}
			
			typesMap.put(this.type.getFullTypeName(), dim);
			fullTypesNameList.remove(this.type.getFullTypeName());			
		}
		for(String type: fullTypesNameList){
			typesMap.put(type, 4);
		}
		
		return typesMap;
	}
	
	public String toString(){
		String string = this.range +" "+ this.type +" "+ this.name;
		for(int i = 0; i < this.dimension; i++){
			string = string.concat("[]");
		}
		return string;
	}
}
