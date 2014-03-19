package org.quantumlabs.kitt.core;

import org.antlr.v4.runtime.ParserRuleContext;
import org.quantumlabs.kitt.core.parse.TesParser.GroupDefContext;

public class GroupDeclaration extends TextBasedTTCNElement implements
		IGroupDeclaration, INamable {
	private String name;

	public GroupDeclaration(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.GROUP_DECLARATION;
	}

	@Override
	public void parse(ParserRuleContext context) {
		GroupDefContext ctx = (GroupDefContext) context;
		name = ctx.ID().getText();
		offset = ctx.ID().getSymbol().getStartIndex();
	}

	@Override
	public String getName() {
		checkDirtyStatus();
		return name;
	}

	@Override
	public int getOffset() {
		checkDirtyStatus();
		return offset;
	}
}
