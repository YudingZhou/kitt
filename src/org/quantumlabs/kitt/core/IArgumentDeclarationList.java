package org.quantumlabs.kitt.core;

public interface IArgumentDeclarationList extends ITTCNElement {

	interface IArgumentDeclaration extends ITTCNElement {
		String getType();

		String getArgument();
	}

	IArgumentDeclaration[] getArgumentDeclarations();
}
