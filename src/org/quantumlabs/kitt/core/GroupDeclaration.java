package org.quantumlabs.kitt.core;

public class GroupDeclaration extends TextBasedTTCNElement implements IGroupDeclaration {
	public GroupDeclaration(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.GROUP_DECLARATION;
	}
}
