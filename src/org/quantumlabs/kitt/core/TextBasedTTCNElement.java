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

	protected int offset = -1;
	protected int len = 0;

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
		if (offset == -1) {
			offset = getCorrespondingParserRuleContext().start.getStartIndex();
		}
		return offset;
	}

	@Override
	public int getLen() {
		if (len == 0) {
			len = getCorrespondingParserRuleContext().stop.getStartIndex() - getOffset() + 1;
		}
		return len;
	}
}
