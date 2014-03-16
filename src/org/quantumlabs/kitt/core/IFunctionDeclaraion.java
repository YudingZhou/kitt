package org.quantumlabs.kitt.core;

public interface IFunctionDeclaraion extends ITTCNElement,INamable {
	ReturnExpr getReturnExpr();

	ArgumentDeclarationList getParameters();

	String getRunsOnType();

	String getID();
}
