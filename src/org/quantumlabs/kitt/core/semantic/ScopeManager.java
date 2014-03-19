package org.quantumlabs.kitt.core.semantic;

import org.antlr.v4.runtime.RuleContext;
import org.quantumlabs.kitt.core.util.SackConstant;

/**
 * ScopeManager provide ways to manage syntax scope, node depth of parser tree
 * etc.
 * */
public class ScopeManager {
	public static boolean isTLD(RuleContext context) {
		return context.depth() == SackConstant.RULE_CONTEXT_DEPTH_TLD;
	}
}
