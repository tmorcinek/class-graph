package com.morcinek.uml.relations.filters;

import java.io.File;
import java.io.FileFilter;

/**
 * Copyright 2014 Tomasz Morcinek. All rights reserved.
 */
public class DirectoryFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return pathname.isDirectory();
    }
}
