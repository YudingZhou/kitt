package org.quantumlabs.kitt.ui.editors.partition;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.quantumlabs.kitt.core.util.SyntaxConstants;
import org.quantumlabs.kitt.ui.editors.SemanticMultiLineRule;

/**
 * Use ITTCNPartition as substitution.
 * */
@Deprecated
public enum TTCNPartition {
	SINGLE_LINE_COMMENT(RuleHolder.SINGLE_LINE_COMMENT_RULE,
			ITTCNPartitions.TTCN_SINGLE_LINE_COMMENT),

	MULTI_LINE_COMMENT(new MultiLineRule(
			SyntaxConstants.PARTITION_MULTIL_LINE_BEGIN,
			SyntaxConstants.PARTITION_MULTIL_LINE_END, new Token(
					ITTCNPartitions.TTCN_MULTI_LINE_COMMENT), '/'),
			ITTCNPartitions.TTCN_MULTI_LINE_COMMENT);// '/'
	// should
	// be
	// escaped
	// to
	// void
	// single
	// line
	// comment
	// inside
	// multiple
	// line
	// comments
	// .
	// WHITE_SPACE(RuleHolder.WHITE_SPACE_RULE, ITTCNPartitions.WHITE_SPACE);

	static class RuleHolder {
		static IPredicateRule WHITE_SPACE_RULE;
		static EndOfLineRule SINGLE_LINE_COMMENT_RULE;
		static {
			// WHITE_SPACE_RULE = new PredictableWhitespaceRule(
			// new WhiteSpaceDetector(), new Token(
			// ITTCNPartitions.WHITE_SPACE));
			SINGLE_LINE_COMMENT_RULE = new EndOfLineRule(
					SyntaxConstants.PARTITION_SINGLE_LINE_COMMENT, new Token(
							ITTCNPartitions.TTCN_SINGLE_LINE_COMMENT));
		}
	}

	static class PredictableWhitespaceRule extends WhitespaceRule implements
			IPredicateRule {

		private IToken token;

		public PredictableWhitespaceRule(IWhitespaceDetector detector,
				IToken token) {
			super(detector);
			PredictableWhitespaceRule.this.token = token;
		}

		@Override
		public IToken getSuccessToken() {
			return token;
		}

		@Override
		public IToken evaluate(ICharacterScanner scanner, boolean resume) {
			return super.evaluate(scanner);
		}

	}

	static class WhiteSpaceDetector implements IWhitespaceDetector {
		@Override
		public boolean isWhitespace(char c) {
			return Character.isWhitespace(c);
		}
	}

	/*
	 * BASIC_CHAR(new SingleLineRule(SyntaxConstants.PARTITION_BASIC_CHAR_BEGIN,
	 * SyntaxConstants.PARTITION_BASIC_CHAR_END, new Token( "TTCN_BASIC_CHAR")),
	 * "TTCN_BASIC_CHAR"),
	 * 
	 * BASIC_CHAR_STRING(new SingleLineRule(
	 * SyntaxConstants.PARTITION_BASIC_CHAR_STRING_BEGIN,
	 * SyntaxConstants.PARTITION_BASIC_CHAR_STRING_END, new Token(
	 * "TTCN_BASIC_CHAR_STRING")), "TTCN_BASIC_CHAR_STRING"), //
	 * FIXME:specialsituation // that // charstring // inside //
	 * charstring:"abc"d"ef"
	 * 
	 * BASIC_BIT_STRING(new SingleLineRule(
	 * SyntaxConstants.PARTITION_BASIC_BIT_STRING_BEGIN,
	 * SyntaxConstants.PARTITION_BASIC_BIT_STRING_END, new Token(
	 * "TTCN_BASIC_BIT_STRING")), "TTCN_BASIC_BIT_STRING"),
	 * 
	 * BASIC_HEX_STRING(new SingleLineRule(
	 * SyntaxConstants.PARTITION_BASIC_HEX_STRING_BEGIN,
	 * SyntaxConstants.PARTITION_BASIC_HEX_STRING_END, new Token(
	 * "TTCN_BASIC_HEX_STRING")), "TTCN_BASIC_HEX_STRING"),
	 * 
	 * BASIC_OCT_STRING(new SingleLineRule(
	 * SyntaxConstants.PARTITION_BASIC_OCT_STRING_BEGIN,
	 * SyntaxConstants.PARTITION_BASIC_OCT_STRING_END, new Token(
	 * "TTCN_BASIC_OCT_STRING")), "TTCN_BASIC_OCT_STRING");
	 */
	/*
	 * TYPE_RECORD_DEFINE(new SemanticMultiLineRule(
	 * SyntaxConstants.PARTITION_TYPE_RECORD_RULE_PATTERN, new Token(
	 * "TYPE_RECORD_DEFINE"), TTCNPartition.getWhilespaceSkipStrategy(),
	 * TTCNPartition.getOccurredTimesMatchEndStrategy(), 0),
	 * "TTCN_TYPE_DEFINE"),
	 * 
	 * TYPE_SET_DEFINE(new SemanticMultiLineRule(
	 * SyntaxConstants.PARTITION_TYPE_SET_RULE_PATTERN, new Token(
	 * "TYPE_SET_DEFINE"), TTCNPartition.getWhilespaceSkipStrategy(),
	 * TTCNPartition.getOccurredTimesMatchEndStrategy(), 1), "TYPE_SET_DEFINE"),
	 * 
	 * TYPE_ENUMERATED(new SemanticMultiLineRule(
	 * SyntaxConstants.PARTITION_TYPE_ENUMERATED_RULE_PATTERN, new Token(
	 * "TYPE_ENUMERATED_DEFINE"), TTCNPartition.getWhilespaceSkipStrategy(),
	 * TTCNPartition.getOccurredTimesMatchEndStrategy(), 1),
	 * "TTCN_ENUMERATED_DEFINE"),
	 * 
	 * TYPE_UNION_DEFINE(new SemanticMultiLineRule(
	 * SyntaxConstants.PARTITION_TYPE_UNION_RULE_PATTERN, new Token(
	 * "TYPE_UNION_DEFINE"), TTCNPartition.getWhilespaceSkipStrategy(),
	 * TTCNPartition.getOccurredTimesMatchEndStrategy(), 1),
	 * "TYPE_UNION_DEFINE");
	 */

	public final IPredicateRule rule;
	/**
	 * Token sup
	 * */
	public final String partitionType;
	public final IToken token;

	/**
	 * <strong>The the name of token of the rule must be the same to
	 * partitionType</strong>
	 * */
	TTCNPartition(IPredicateRule rule, String partitionType) {
		this.rule = rule;
		this.partitionType = partitionType;
		this.token = rule.getSuccessToken();
	}

	private static EndStrategy getOccurredTimesMatchEndStrategy() {
		return new EndStrategy(
				SyntaxConstants.PARTITION_SEMANTIC_BLOCK_END.toCharArray()[0],
				true/* end strategy should end on EOF */) {
			int expectedTimes;
			int currentTimes;

			public boolean end(char c) {
				throw new UnsupportedOperationException(
						"OccurredTimesMatchEndStrategy does support ending with character, please use EndStrategy.end(ICharacter c)");
			}

			public boolean end(ICharacterScanner scanner) {
				int c = scanner.read();
				if (c == ICharacterScanner.EOF && breakOnEOF) {
					return true;
				}

				if (c == SyntaxConstants.PARTITION_SEMANTIC_BLOCK_BEGIN
						.toCharArray()[0]) {
					expectedTimes++;
					return false;
				} else if (endChar == c && expectedTimes == 1) {
					scanner.unread();
					return true;
				} else if (endChar == c
						&& (++currentTimes >= (expectedTimes - 1))) {
					return true;
				} else {
					return false;
				}
			}

			@Override
			public void reset() {
				expectedTimes = 0;
			}
		};
	}

	private static SkipStrategy getWhilespaceSkipStrategy() {
		return new SkipStrategy(' ');
	}

}
