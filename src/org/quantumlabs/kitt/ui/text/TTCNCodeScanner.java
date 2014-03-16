package org.quantumlabs.kitt.ui.text;

import java.util.LinkedList;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.quantumlabs.kitt.core.util.Helper;
import org.quantumlabs.kitt.core.util.ITTCNKeyWords;
import org.quantumlabs.kitt.ui.editors.ColorManager;

public class TTCNCodeScanner extends AbstractTTCNScanner {

	public TTCNCodeScanner(ColorManager kColorManager,
			IPreferenceStore kPreferenceStore) {
		super(kColorManager, kPreferenceStore);
		initialize();
	}

	private String[] tokenProperties = new String[] {
			ITTCNColorConstants._DEFAULT_CODE_TOKEN,
			ITTCNColorConstants._TTCN_BRACKET,
			ITTCNColorConstants._TTCN_KEY_WORD,
			ITTCNColorConstants._TTCN_OPERATOR };

	private static final class OperatorRule implements IRule {

		private final char[] OPERATORS = { ';', '.', '/', '\\', '+', '-', '*',
				'<', '>', '?', '!', ',', '|', '&', '^', '%', '~' };
		/** Token to return for this rule */
		private final IToken fToken;

		/**
		 * Creates a new operator rule.
		 * 
		 * @param token
		 *            Token to use for this rule
		 */
		public OperatorRule(IToken token) {
			fToken = token;
		}

		/**
		 * Is this character an operator character?
		 * 
		 * @param character
		 *            Character to determine whether it is an operator character
		 * @return <code>true</code> iff the character is an operator,
		 *         <code>false</code> otherwise.
		 */
		public boolean isOperator(char character) {
			for (int index = 0; index < OPERATORS.length; index++) {
				if (OPERATORS[index] == character)
					return true;
			}
			return false;
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character = scanner.read();
			if (isOperator((char) character)) {
				do {
					character = scanner.read();
				} while (isOperator((char) character));
				scanner.unread();
				return fToken;
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}

	private static final class BracketRule implements IRule {
		private char[] BRACKET = new char[] { '{', '}', '(', ')' };
		private IToken kToken;

		public BracketRule(IToken token) {
			kToken = token;
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character = scanner.read();
			if (isBracket((char) character)) {
				do {
					character = scanner.read();
				} while (isBracket((char) character));
				scanner.unread();
				return kToken;
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}

		private boolean isBracket(char c) {
			for (int i = 0; i < BRACKET.length; i++) {
				if (BRACKET[i] == c) {
					return true;
				}
			}
			return false;
		}
	}

	public static final String[] TTCN_KEY_WORD;

        static {
    		TTCN_KEY_WORD = Helper.asString(Helper.getDeclaredSVariables(
    			ITTCNKeyWords.class, ITTCNKeyWords.class));
        }
	
	/**
	 * Composited operator, consist of more than 1 character, treat it as a key
	 * word, so it belongs to FlyWeightRule.
	 * */
	public static final String[] COMPOSITED_OPERATOR = { ":=" };

	/**
	 * Kinds of token are based on kind of TTCNColorConstants.
	 * */
	public IRule[] createRules() {
		LinkedList<IRule> rules = new LinkedList<IRule>();

		IToken defaultToken = getToken(ITTCNColorConstants._DEFAULT_CODE_TOKEN);//beside operator, bracket

		IToken opToken = getToken(ITTCNColorConstants._TTCN_OPERATOR);
		OperatorRule operatorRule = new OperatorRule(opToken);
		rules.add(operatorRule);

		IToken bracketToken = getToken(ITTCNColorConstants._TTCN_BRACKET);
		BracketRule bracketRule = new BracketRule(bracketToken);
		rules.add(bracketRule);

		WhitespaceRule whitespaceRule = new WhitespaceRule(
				new IWhitespaceDetector() {
					@Override
					public boolean isWhitespace(char c) {
						return Character.isWhitespace(c);
					}
				}, defaultToken);

		rules.add(whitespaceRule);

		rules.add(createFlyWeightRule(defaultToken));

		setDefaultReturnToken(defaultToken);

		return rules.toArray(new IRule[rules.size()]);
	}

	private WordRule createFlyWeightRule(IToken defaultToken) {

		/*
		 * FlyWeightRule flyWeightRule = new FlyWeightRule(new
		 * TTCNWordDetector());
		 * 
		 * // FlyWeightRule flyWeightRule = new FlyWeightRule(new //
		 * TTCNWordDetector(), // null);// since default token is the token
		 * except keyword, // // bracket, operator, so i will be default style.
		 * No // // need to specified. IToken kwToken =
		 * getToken(ITTCNColorConstants._TTCN_KEY_WORD); WordTokenMatcher
		 * keyWordMatcher = new WordTokenMatcher(null);// default // token // is
		 * // null, // expect // it's // matched for (String keyWord :
		 * TTCN_KEY_WORD) { keyWordMatcher.add(keyWord, kwToken); }
		 * 
		 * // WordTokenMatcher opMatcher = new WordTokenMatcher(null); // for
		 * (String op : COMPOSITED_OPERATOR) { // // treat it as key word token
		 * // opMatcher.add(op, kwToken);e // }
		 * 
		 * flyWeightRule.addWordMatcher(keyWordMatcher); //
		 * flyWeightRule.addWordMatcher(opMatcher); return flyWeightRule;
		 */

		IToken keyWordToken = getToken(ITTCNColorConstants._TTCN_KEY_WORD);
		WordRule wordRule = new WordRule(new UnionDetecor(), defaultToken);
		for (int i = 0; i < TTCN_KEY_WORD.length; i++) {
			wordRule.addWord(TTCN_KEY_WORD[i], keyWordToken);
		}

		return wordRule;
	}

	@Override
	protected String[] getTokenProperties() {
		return tokenProperties;
	}
}
