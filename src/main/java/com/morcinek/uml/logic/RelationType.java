package com.morcinek.uml.logic;

/**
 * Copyright 2014 Tomasz Morcinek. All rights reserved.
 * <p/>
 * association	2
 * composition 	1 & 4
 * aggregation 	4 & 8
 * dependency	1 | 8
 * extends		16
 * implements	32
 */
public class RelationType {

    public static final int FIELD_BODY = 0x0001;
    public static final int FIELD_ASSOCIATION = 0x0002;
    public static final int FIELD_COMPOSITION = 0x0004;
    public static final int METHOD_ASSOCIATION = 0x0008;
    public static final int GENERALIZATION = 0x0010;
    public static final int IMPLEMENTATION = 0x0020;
}
