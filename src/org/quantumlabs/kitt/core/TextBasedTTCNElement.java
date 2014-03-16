package org.quantumlabs.kitt.core;

/**
 * Content based ttcn element is the element in text form, such as
 * ImportDeclaration, FunctionDef. <br>
 * Feature:<br>
 * 1.Any of these has offset in text level. <br>
 * Notice! Since lots of method uses underlying RuleContext, so trying to call
 * {@link #setCorrespondingParserRuleContext(org.antlr.v4.runtime.ParserRuleContext)}
 * as soon as the element being instantiated. Otherwise, there will be
 * exception, you have my words... ; )
 * */
public abstract class TextBasedTTCNElement extends AbstractTTCNElement implements IOffsetable {

	public TextBasedTTCNElement(ITTCNElement ancestor) {
		super(ancestor);
	}

	@Deprecated
	public TextBasedTTCNElement(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	public TextBasedTTCNElement() {
		super();
	}

	@Override
	public int getOffset() {
		return getCorrespondingParserRuleContext().start.getStartIndex();
	}

	@Override
	public int getLen() {
		return getCorrespondingParserRuleContext().stop.getStopIndex() - getOffset() + 1;
	}
}
