package org.quantumlabs.kitt.ui.text;

import java.util.HashMap;

import org.eclipse.jface.text.rules.IToken;

public class WordTokenMatcher {
	private HashMap<String, IToken> words;
	private IToken defaultToken;

	public WordTokenMatcher(IToken defaultToken) {
		this.defaultToken = defaultToken;
		words = new HashMap<String, IToken>();
	}

	public void add(String word, IToken token) {
		words.put(word, token);
	}

	public IToken match(String word) {
		return words.get(word) == null ? defaultToken : words.get(word);
	}
}
