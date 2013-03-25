package pl.edu.agh.morcinek.uml.logic.object;

import java.util.Map;

import pl.edu.agh.morcinek.uml.parser.java.JavaParser.ModifierSet;

public abstract class DeclarationObject{
	
	protected String name;
	
	protected String range;
	
	protected String modifiers = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getRange() {
		return range;
	}
	
	public void setRange(String p_modifiers){
		int modifiers = Integer.valueOf(p_modifiers);
		// getModifiers
		ModifierSet m = new ModifierSet();
		
		// set range
		if(m.isPublic(modifiers)){
			this.range = "+";
		} else if(m.isPrivate(modifiers)){
			this.range = "-";
		} else if(m.isProtected(modifiers)){
			this.range = "#";
		} else {
			this.range = "~";
		}
		
		if(m.isFinal(modifiers)){
			this.modifiers = this.modifiers.concat("f ");
		}
		if(m.isStatic(modifiers)){
			this.modifiers = this.modifiers.concat("s ");
		}
		if(m.isSynchronized(modifiers)){
			this.modifiers = this.modifiers.concat("sy ");
		}
		if(m.isAbstract(modifiers)){
			this.modifiers = this.modifiers.concat("a ");
		}
		
		this.range = this.modifiers + this.range;
	}
	
	/**
	 * Values of integer:
	 * 1 - found in body
	 * 2 - found in field as association
	 * 4 - found in field as composition
	 * 8 - found in method argument 
	 * @return
	 */
	abstract public Map<String, Integer> globalTypes();
	
	abstract public String toString();
}
