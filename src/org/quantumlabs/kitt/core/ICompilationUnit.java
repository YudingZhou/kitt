package org.quantumlabs.kitt.core;

public interface ICompilationUnit extends ITTCNElement ,INamable{
	IImportDeclaration[] getImportDeclarations();

	ITemplate[] getTemplates();

	IFunctionDeclaraion[] getFunctions();

	ITestCase[] getTestCases();

	IControlPart getControlPart();

	IConstantDeclaration[] getConstants();

	ITypeDeclaration[] getTypeDeclarations();

	ISignatureDeclaration[] getSignatures();

	IModuleParDeclaration[] getModulePars();

	IAttributeDeclaration[] getAttributes();

	IGroupDeclaration[] getGroups();

	IAltStepDeclaration[] getAltSteps();

	String getID();
}
