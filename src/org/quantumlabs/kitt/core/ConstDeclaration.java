package org.quantumlabs.kitt.core;

public class ConstDeclaration extends TextBasedTTCNElement implements IConstantDeclaration {

	private String type;
	private String id;

	public ConstDeclaration(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.CONSTANT_DECLARATION;
	}

	@Override
	public String getID() {
		return id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setID(String id) {
		this.id = id;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getName() {
		return id;
	}
}
