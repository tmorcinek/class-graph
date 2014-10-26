package com.morcinek.uml.logic;

import com.morcinek.uml.relations.extraction.Type;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.LinkedList;

/**
 * Copyright 2014 Tomasz Morcinek. All rights reserved.
 */
public class TypeTest {

    @Test
    public void typeTest() throws Exception {
        Type type = createType("Type", "com.morcinek.uml.logic.Type");

        Assertions.assertThat(type.toString()).isEqualTo("Type");
    }

    @Test
    public void typeWithDimensionTest() throws Exception {
        Type type = createType("Type", "com.morcinek.uml.logic.Type");
        type.addDimension(1);

        Assertions.assertThat(type.toString()).isEqualTo("Type[]");
    }

    @Test
    public void typeWithTwoDimensionTest() throws Exception {
        Type type = createType("Type", "com.morcinek.uml.logic.Type");
        type.addDimension(2);

        Assertions.assertThat(type.toString()).isEqualTo("Type[][]");
    }

    @Test
    public void typeWithSubTypesTest() throws Exception {
        Type type = new Type("Type");
        type.addType(new Type("Button"));

        Assertions.assertThat(type.toString()).isEqualTo("Type<Button>");
    }

    @Test
    public void typeWithTwoSubTypesTest() throws Exception {
        Type type = new Type("Type");
        type.addType(new Type("Button"));
        type.addType(new Type("Label"));

        Assertions.assertThat(type.toString()).isEqualTo("Type<Button, Label>");
    }

    @Test
    public void typeWithTwoSubTypeArrayTest() throws Exception {
        Type type = new Type("Type");
        Type button = new Type("Button");
        button.addDimension(1);
        type.addType(button);
        type.addType(new Type("Label"));

        Assertions.assertThat(type.toString()).isEqualTo("Type<Button[], Label>");
    }

    @Test
    public void typeWithTwoSubTypeWithSubTypeTest() throws Exception {
        Type type = new Type("Type");
        Type button = new Type("Button");
        button.addType(new Type("Map"));
        type.addType(button);
        type.addType(new Type("Label"));

        Assertions.assertThat(type.toString()).isEqualTo("Type<Button<Map>, Label>");
    }

    @Test
    public void getFullTypesNameListTest() throws Exception {
        Type type = new Type("Type");

        LinkedList<Object> expected = new LinkedList<Object>();
        Assertions.assertThat(type.getFullTypesNameList()).isEqualTo(expected);
    }

    @Test
    public void getFullTypesNameListWithSubTypesTest() throws Exception {
        Type type = createType("Type", "com.morcinek.uml.logic.Type");
        type.addType(createType("Button", "java.awt.Button"));
        type.addType(createType("Label", "java.awt.Label"));

        LinkedList<Object> expected = new LinkedList<Object>();
        expected.add("com.morcinek.uml.logic.Type");
        expected.add("java.awt.Button");
        expected.add("java.awt.Label");
        Assertions.assertThat(type.getFullTypesNameList()).isEqualTo(expected);
    }

    private Type createType(String typeName, String fullTypeName) {
        Type type = new Type(typeName);
        type.setFullTypeName(fullTypeName);
        return type;
    }
}
