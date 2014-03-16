package org.quantumlabs.kitt.core;

public interface IImportDeclaration extends ITTCNElement {
	String getSourceModule();

	String[] getImportedElements();

	boolean isImportAll();
}
