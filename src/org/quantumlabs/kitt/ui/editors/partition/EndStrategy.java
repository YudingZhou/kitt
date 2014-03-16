package org.quantumlabs.kitt.ui.editors.partition;

import org.eclipse.jface.text.rules.ICharacterScanner;

/**
 * EndStrategy decides whether current scanning character sequence should end
 * while specific character occurred.
 * */
public class EndStrategy {
	protected char endChar;
	protected boolean breakOnEOF;

	public EndStrategy(char end) {
		this.endChar = end;
	}

	public EndStrategy(char end, boolean breakOnEOF) {
		this.breakOnEOF = breakOnEOF;
		this.endChar = end;
	}

	public boolean end(char c) {
		if (c == ICharacterScanner.EOF && breakOnEOF) {
			return true;
		}

		if (endChar == c) {
			return true;
		}
		return false;
	}

	public boolean end(ICharacterScanner scanner) {
		if (endChar == scanner.read()) {
			return true;
		}
		return false;
	}

	public void reset() {

	}
}