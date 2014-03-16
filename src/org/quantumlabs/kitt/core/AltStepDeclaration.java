package org.quantumlabs.kitt.core;

public class AltStepDeclaration extends TextBasedTTCNElement implements IAltStepDeclaration {

	public AltStepDeclaration(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.ALTSTEP_DECLARATION;
	}
}
