package org.quantumlabs.kitt.core;

public interface ITypeDeclaration extends ITTCNElement,INamable{
	String getID();

	void setSuperType(String type);
}
