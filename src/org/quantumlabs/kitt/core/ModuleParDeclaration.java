package org.quantumlabs.kitt.core;

public class ModuleParDeclaration extends TextBasedTTCNElement implements
	IModuleParDeclaration {
    public ModuleParDeclaration(ITTCNElement ancestor, String name) {
	super(ancestor, name);
    }

    @Override
    public int getElementType() {
	return ITTCNElement.MODULE_PAR_DECLARATION;
    }
}
