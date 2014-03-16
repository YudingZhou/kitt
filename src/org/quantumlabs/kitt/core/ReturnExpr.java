package org.quantumlabs.kitt.core;

public class ReturnExpr extends AbstractTTCNElement implements IReturnExpr {

	private String expr;

	public ReturnExpr(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.RETURN_EXPR;
	}

	@Override
	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}
}
