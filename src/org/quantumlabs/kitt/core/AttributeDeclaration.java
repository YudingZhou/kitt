package org.quantumlabs.kitt.core;

public class AttributeDeclaration extends TextBasedTTCNElement implements IAttributeDeclaration {

	public AttributeDeclaration(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.ATTRIBUTE_DECLARATION;
	}
}
