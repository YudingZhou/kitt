package org.quantumlabs.kitt.core;

public class TypeDeclaration extends TextBasedTTCNElement implements ITypeDeclaration {

	private String id;
	private String superType;

	public TypeDeclaration(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.TYPE;
	}

	@Override
	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	@Override
	public void setSuperType(String type) {
		this.superType = type;
	}

	public String getSuperType() {
		return superType;
	}

	@Override
	public String getName() {
		return id;
	}
}
