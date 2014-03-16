package org.quantumlabs.kitt.core;

public class SignatureDeclaration extends AbstractTTCNElement implements ISignatureDeclaration {

	public SignatureDeclaration(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.SIGNATURE_DECLARATION;
	}
}
