package com.morcinek.uml.logic;

import java.util.LinkedList;
import java.util.List;

public class Type{
	
	private String typeName;
	private String fullTypeName;
	private int dimension;
	private List<Type> subTypes = new LinkedList<Type>();
	
	public Type(){}
	
	public Type(String p_typeName){
		this.typeName = p_typeName;
	}
	
	public void setName(String p_typeName){
		this.typeName = p_typeName;
	}
	
	public void setFullTypeName(String p_fullTypeName){
		this.fullTypeName = p_fullTypeName;
	}
	
	public void addDimension(int p_dimension){
		this.dimension += p_dimension;
	}
	
	public void addType(Type p_subType){
		this.subTypes.add(p_subType);
	}
	
	public int getDimension(){
		return this.dimension;
	}
	
	public List<String> getFullTypesNameList(){
		List<String> nameList = new LinkedList<String>();
		if(this.fullTypeName != null) nameList.add(this.fullTypeName);
		for(Type subType: subTypes){
			nameList.addAll(subType.getFullTypesNameList());
		}
		return nameList;
	}
	
	public String getFullTypeName(){
		return this.fullTypeName;
	}

	public String toString(){
		String ret = this.typeName;
		// adds subtypes
		if(!this.subTypes.isEmpty()){
			ret = ret.concat("<");
			int i = 0;
			for(Type subType : this.subTypes){
				if(i > 0) ret = ret.concat(", ");
				ret = ret.concat(subType.toString());
				i++;
			}
			ret = ret.concat(">");
		}
		// adds array
		for(int i = 0 ; i < this.dimension; i++){
			ret = ret.concat("[]");
		}
		
		return ret; 
	}
	
}
