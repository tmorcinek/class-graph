package com.morcinek.uml.logic.object;

import com.morcinek.uml.parser.java.JavaParser.ModifierSet;

import java.util.Map;

public abstract class DeclarationObject {

    final private ModifierSet modifierSet = new ModifierSet();

    protected String name;

    protected String range;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        int modifiersValue = Integer.valueOf(range);

        this.range = extractModifiers(modifiersValue) + prepareRange(modifiersValue);
    }

    private String extractModifiers(int modifiersValue) {
        StringBuilder stringBuilder = new StringBuilder();
        if (modifierSet.isFinal(modifiersValue)) {
            stringBuilder.append("f ");
        }
        if (modifierSet.isStatic(modifiersValue)) {
            stringBuilder.append("s ");
        }
        if (modifierSet.isSynchronized(modifiersValue)) {
            stringBuilder.append("sy ");
        }
        if (modifierSet.isAbstract(modifiersValue)) {
            stringBuilder.append("a ");
        }
        return stringBuilder.toString();
    }

    private String prepareRange(int modifiers) {
        if (modifierSet.isPublic(modifiers)) {
            return "+";
        } else if (modifierSet.isPrivate(modifiers)) {
            return "-";
        } else if (modifierSet.isProtected(modifiers)) {
            return "#";
        } else {
            return "~";
        }
    }

    /**
     * Values of integer:
     * 1 - found in body
     * 2 - found in field as association
     * 4 - found in field as composition
     * 8 - found in method argument
     *
     * @return
     */
    abstract public Map<String, Integer> globalTypes();

    abstract public String toString();
}
