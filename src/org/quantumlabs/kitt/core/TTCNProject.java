package org.quantumlabs.kitt.core;

public class TTCNProject extends AbstractTTCNElement implements ITTCNProject {

	public TTCNProject(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.PROJECT;
	}

	@Override
	public String getName() {
		return name;
	}
}
