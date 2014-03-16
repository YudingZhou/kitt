package org.quantumlabs.kitt.core;

@Deprecated
public class Type extends AbstractTTCNElement implements IType {

	private boolean isPrimitive;
	private IType superType;

	public Type(ICompilationUnit ancestor, String name, boolean isPrimitive, IType superType) {
		super(ancestor, name);
		this.isPrimitive = isPrimitive;
		this.superType = superType;
	}

	public Type(String name) {
		this(null, name, true, null);
	}

	@Override
	public boolean isPrimitive() {
		return isPrimitive;
	}

	@Override
	public IType getSuper() {
		return superType;
	}

	@Override
	public int getElementType() {
		return ITTCNElement.TYPE;
	}
}
