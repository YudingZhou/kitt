package org.quantumlabs.kitt.ui.text;

import org.eclipse.jface.text.rules.IWordDetector;
import org.quantumlabs.kitt.core.util.Helper;

public class UnionDetecor implements IWordDetector {

	// Not used, just for reference
	public static final String TTCN_ALLOWED_WORD_START = "ABCDEFGHIJKLMNOPQRSTUVWZYZabcdefghijklmnopqrstuvwzyz_";
	public static final String TTCN_ALLOWED_WORD_PART = (TTCN_ALLOWED_WORD_START + "0123456789");

	/**
	 * 
	 * Check whether the character is allowed as begin of a word.
	 * 
	 * return false if following condition met.
	 * 
	 * <pre>
	 * 1. c != '_'
	 * 2. c &lt; 'A'
	 * 3. c &gt; 'z'
	 * 4. c &gt; 'Z' and c &lt; 'a'
	 * 5. c &gt; '9' and c &lt;'0'
	 * </pre>
	 * 
	 * Please reference to : {@link http://www.asciitable.com/}
	 * */

	@Override
	public boolean isWordPart(char c) {
		if (Helper.isUnderscore(c) || Helper.isLettersInLowerCase(c) || Helper.isLettersInUpperCase(c)
				|| Helper.isNumber(c)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isWordStart(char c) {
		return !Helper.isNumber(c) && isWordPart(c);
	}
}
