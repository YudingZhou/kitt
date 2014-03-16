package org.quantumlabs.kitt.core;

public class Template extends TextBasedTTCNElement implements ITemplate {

	private String type;

	public Template(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.TEMPLATE;
	}

	@Override
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}
}
