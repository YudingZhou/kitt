package org.quantumlabs.kitt.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.util.IPropertyChangeListener;

/**
 * Module path introduces a kind of conception which is java-like, called
 * "CLASS_PATH". It enables us to organize TTCN project in a certain manner than
 * compiler based one. Each project has its own IModulePathConfiguration.
 * */
public interface IModulePathConfiguration extends IPropertyChangeListener {

    /**
     * Files of ttcn code.
     * */
	IFile[] getSource();

    /**
     * E.g. library.
     * */
	IResource[] getDependency();

    /**
     * Resource should be source files than ttcn code..
     * */
	IFile[] getResource();

    /**
     * Root of project.
     * */
    IProject getProject();

    /**
     * Project name.
     * */
    String getName();
}
