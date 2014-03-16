package org.quantumlabs.kitt.core;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.quantumlabs.kitt.Activator;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.util.Callback;
import org.quantumlabs.kitt.core.util.Helper;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.text.ITTCNColorConstants;
import org.quantumlabs.kitt.ui.text.TTCNCodeScanner;
import org.quantumlabs.kitt.ui.util.exception.StackTracableException;

/**
 * 1. setup identifiers indexing 1). same words indexing
 * <p>
 * 2. listen to document change and update identifiers indexing
 * */
public class SingleDocumentIndexer {

	private InternalDriver driver;

	private Map<String, SortedSet<CallbackImp>> lineNOsOfWord;

	/**
	 * 1. listen to document change.
	 * <p>
	 * 2. translate document replace command to identifiers which could be found
	 * in minus region.
	 * 
	 * <pre>
	 * e.g.
	 * type MyType runs on DNS {} 
	 * delete
	 * type My|Type runs o|n DNS {}
	 * will be translated to"MyType","runs", "on" tree affected identifiers and generate one new identifier "Myn"
	 * 
	 * <pre>
	 * <p>
     * 3. drive indexer to update indexing.
	 * */
	class InternalDriver implements IDocumentListener {

		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
			IRegion damagedRegion = damage(event);
			repairer(damagedRegion, event);
		}

		private void repairer(IRegion damagedRegion, DocumentEvent event) {
			try {
				IDocument doc = event.getDocument();
				int offset = damagedRegion.getOffset();
				int len = event.getText().length();
				// Only damage, no need to repairer.
				if (len == 0) {
					return;
				}
				int startLine = doc.getLineOfOffset(offset);
				int endLine = doc.getLineOfOffset(offset + len);
				int affectedLineCount = startLine == endLine ? 1 : endLine - startLine + 1;

				// Update word indexing, add new lines
				for (int i = 0; i < affectedLineCount; i++) {
					String[] affected = extractWords(doc.getLineOffset(startLine), doc.getLineLength(startLine), doc);
					CallbackImp target = null;
					SortedSet<CallbackImp> insert = null;
					for (int j = 0; j < affected.length; j++) {
						// find already indexed lines.
						insert = lineNOsOfWord.get(affected[j]);
						if (insert == null) {
							insert = new TreeSet<CallbackImp>();
						}
						// index a new line.
						target = new CallbackImp(startLine + i);
						insert.add(target);
					}

					for (CallbackImp callback : insert) {
						callback.call(1);
					}
				}

			} catch (BadLocationException e) {

			}
		}

		private IRegion damage(DocumentEvent event) {
			IDocument doc = event.getDocument();
			int offset = event.getOffset();
			int len = event.getLength();

			int wordBefore = searchWord(offset, doc, false);
			int wordAfter = searchWord(offset + len, doc, true);

			return damageLine(wordBefore, wordAfter, doc);
		}

		private IRegion damageLine(int offset, int length, IDocument doc) {
			try {
				int startLine = doc.getLineOfOffset(offset);
				int endLine = doc.getLineOfOffset(offset + length);
				int startLineStartOfst = doc.getLineOffset(startLine);
				int endLineEndOfst;
				int affectedLineCount = startLine == endLine ? 1 : endLine - startLine + 1;

				endLineEndOfst = doc.getLineLength(endLine) + doc.getLineLength(endLine);

				String[] affectedWords = extractWords(startLineStartOfst, endLineEndOfst - startLineStartOfst, doc);
				for (int i = 0; i < affectedWords.length; i++) {
					SortedSet<CallbackImp> indexedLines = lineNOsOfWord.get(affectedWords[i]);
					CallbackImp target = null;
					for (int j = startLine; j <= endLine; j++) {
						target = new CallbackImp(j);
						indexedLines.remove(target);
					}

					for (CallbackImp callback : indexedLines.tailSet(target)) {
						callback.call(0 - affectedLineCount);
					}
				}
				return new Region(startLineStartOfst, endLineEndOfst - startLineStartOfst);
			} catch (BadLocationException e) {
				if (KITTParameter.isBETA()) {
					throw new StackTracableException(e);
				}
				if (Logger.isErrorEnable()) {
					Logger.logError("SingleDocumentIndexer.extractWords()", e);
				}
				return null;
			}
		}

		private String[] extractWords(int offset, int length, IDocument doc) throws BadLocationException {
			SortedSet<String> affectedWords = new TreeSet<String>();
			TTCNCodeScanner scanner = (TTCNCodeScanner) Activator.instance().getTextTools()
					.createScanner(TTCNCodeScanner.class);
			scanner.setRange(doc, offset, length);
			int lastStart = offset;
			IToken word = scanner.nextToken();
			while (word != Token.EOF) {
				if (word.equals(scanner.getToken(ITTCNColorConstants._DEFAULT_CODE_TOKEN))) {
					int len = scanner.getTokenLength();
					String current = doc.get(lastStart, len);
					if (isWord(current)) {
						affectedWords.add(current);
					}
				}
			}
			return affectedWords.toArray(new String[affectedWords.size()]);
		}

		private boolean isWord(String current) {
			// TODO Auto-generated method stub
			return false;
		}

		private int searchWord(int start, IDocument target, boolean forward) {
			try {
				IRegion line = target.getLineInformationOfOffset(start);
				if (forward) {
					int end = line.getOffset() + line.getLength();
					for (int i = start; i < end; i++) {
						if (!Helper.isWordPart(target.getChar(i))) {
							return i;
						}
					}
					return end;
				} else {
					for (int i = start; i > line.getOffset(); i--) {
						if (!Helper.isWordPart(target.getChar(i))) {
							return i;
						}
					}
					return line.getOffset();
				}
			} catch (BadLocationException e) {
				throw new StackTracableException(e);
			}
		}

		@Override
		public void documentChanged(DocumentEvent event) {

		}

	}

	class DocumentEventExt extends DocumentEvent {
	}

	public void install(IDocument document) {
		document.addDocumentListener(driver);
	}

	static class CallbackImp implements Callback, Comparable<CallbackImp> {
		int line = 0;

		public CallbackImp(int i) {
			line = i;
		}

		@Override
		public Object call(Object... args) {
			Assert.isTrue(args.length == 1);
			int delta = (Integer) args[0];
			line = line + delta;
			return null;
		}

		public boolean equals(Object o) {
			return line == ((CallbackImp) o).line;
		}

		@Override
		// smaller to greater.
		public int compareTo(CallbackImp o) {
			return o.line - line;
		}
	}
}
