package org.quantumlabs.kitt.core;

public class ControlPart extends TextBasedTTCNElement implements IControlPart {

	public ControlPart(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.CONTROL_PART;
	}
}
