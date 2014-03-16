package org.quantumlabs.kitt.ui.editors.partition;

/**
 * SkipStrategy decides whether the given character should be skipped while
 * scanning character sequence.
 * */
public class SkipStrategy {
	protected char skipChar;

	public SkipStrategy(char skipChar) {
		this.skipChar = skipChar;
	}

	public boolean skip(char c) {
		if (skipChar == c) {
			return true;
		}
		return false;
	}

	public void reset() {

	};
}
