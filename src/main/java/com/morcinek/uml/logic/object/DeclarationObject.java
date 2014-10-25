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
     * @return <code>Map<String,Integer></code> map which reflects <code>type name</code>
     * to <code>com.morcinek.uml.logic.RelationType</code> mapping.
     * @see com.morcinek.uml.logic.RelationType
     */
    public abstract Map<String, Integer> globalTypes();

    public abstract String toString();
}
