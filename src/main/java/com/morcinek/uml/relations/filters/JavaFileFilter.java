package com.morcinek.uml.relations.filters;

import java.io.File;
import java.io.FileFilter;

/**
 * Copyright 2014 Tomasz Morcinek. All rights reserved.
 */
public class JavaFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return pathname.isFile() && pathname.getName().endsWith(".java");
    }
}
