package org.quantumlabs.kitt.core;

import org.antlr.v4.runtime.tree.TerminalNode;

class FunctionDeclaration extends TextBasedTTCNElement implements IFunctionDeclaraion {
	private String id;
	private ReturnExpr returnExpr;
	private String runsOnType;
	private ArgumentDeclarationList arguments;

	public FunctionDeclaration(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getOffset() {
		return  ((TerminalNode)getCorrespondingParserRuleContext().getChild(0).getChild(1)).getSymbol().getStartIndex();
	}

	@Override
	public int getElementType() {
		return ITTCNElement.FUNCTION;
	}

	@Override
	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	@Override
	public ReturnExpr getReturnExpr() {
		return returnExpr;
	}

	public void setReturnExpr(ReturnExpr returnExpr) {
		this.returnExpr = returnExpr;
	}

	public void setArguments(ArgumentDeclarationList args) {
		this.arguments = args;
	}

	@Override
	public String getRunsOnType() {
		return runsOnType;
	}

	public void setRunsOnType(String runsOnType) {
		this.runsOnType = runsOnType;
	}

	@Override
	public ArgumentDeclarationList getParameters() {
		return arguments;
	}

	@Override
	public String getName() {
		return id;
	}
}
