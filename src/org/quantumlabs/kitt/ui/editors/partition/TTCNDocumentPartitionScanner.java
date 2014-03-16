package org.quantumlabs.kitt.ui.editors.partition;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.quantumlabs.kitt.core.util.SyntaxConstants;

public class TTCNDocumentPartitionScanner extends RuleBasedPartitionScanner {

    public TTCNDocumentPartitionScanner() {
	IToken mCommentToken = new Token(
		ITTCNPartitions.TTCN_MULTI_LINE_COMMENT);
	IToken sCommentToken = new Token(
		ITTCNPartitions.TTCN_SINGLE_LINE_COMMENT);
	IToken ttcnStringToken = new Token(ITTCNPartitions.TTCN_STRING);

	MultiLineRule mCommentRule = new MultiLineRule(
		SyntaxConstants.PARTITION_MULTIL_LINE_BEGIN,
		SyntaxConstants.PARTITION_MULTIL_LINE_END, mCommentToken);
	EndOfLineRule sCommentRule = new EndOfLineRule(
		SyntaxConstants.PARTITION_SINGLE_LINE_COMMENT, sCommentToken);
	SingleLineRule stringRule = new SingleLineRule("\"", "\"",
		ttcnStringToken);
	SingleLineRule stringRule2 = new SingleLineRule("'", "'",
		ttcnStringToken);
	setPredicateRules(new IPredicateRule[] { mCommentRule, sCommentRule,
		stringRule, stringRule2 });
    }

    public static String[] getLegalContentType() {
	return new String[] { ITTCNPartitions.TTCN_MULTI_LINE_COMMENT,
		ITTCNPartitions.TTCN_SINGLE_LINE_COMMENT,
		IDocument.DEFAULT_CONTENT_TYPE, ITTCNPartitions.TTCN_STRING };
    }
}
