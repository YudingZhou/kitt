package org.quantumlabs.kitt.ui.text;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

@Deprecated
public class FlyWeightRule implements IRule {

	private IWordDetector detector;
	private IToken defaultToken; // currently, if not matcher matched, no
									// default
									// token will be returned, it will return
									// Undefined directly.
	private List<WordTokenMatcher> matchers = new LinkedList<WordTokenMatcher>();

	public FlyWeightRule(IWordDetector detector, IToken defaultToken) {
		this.detector = detector;
		this.defaultToken = defaultToken;
	}

	public FlyWeightRule(UnionDetecor detector) {
		this(detector, null);
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		IToken token = null;
		int c = scanner.read();

		if (ICharacterScanner.EOF == c) { // If the first word read is EOF, then
											// return Token.EOF.
			return Token.EOF;
		}

		StringBuilder buffer = new StringBuilder();
		if (detector.isWordStart((char) c)) {
			buffer.append((char) c);

			c = scanner.read();
			while (c != ICharacterScanner.EOF && detector.isWordPart((char) c)) {
				buffer.append((char) c);
				c = scanner.read();
			}
			scanner.unread();

			for (int index = 0; index < matchers.size(); index++) {
				token = matchers.get(index).match(buffer.toString());
				if (token != null) {
					break;
				}
			}

			if (token == null) {
				for (int read = buffer.length(); read > 0; read--) {
					scanner.unread();
				}
				// if (c == ICharacterScanner.EOF) { // While scanner reach the
				// EOF
				// // and not defined token
				// // matched, it should not
				// // return EOF than
				// // undefined, let the
				// // Scanner return it
				// // naturally.
				// // return Token.EOF;
				// return Token.UNDEFINED;
				// }
				return Token.UNDEFINED;
				// else {
				// return defaultToken;
				// }
				// currently, if not matcher matched, no
				// default
				// token will be returned, it will return
				// Undefined directly.
			} else {
				return token;
			}
		} else {
			scanner.unread();
			return Token.UNDEFINED;
		}
	}

	public void addWordMatcher(WordTokenMatcher matcher) {
		matchers.add(matcher);
	}
}
