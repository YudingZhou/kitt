package org.quantumlabs.kitt.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.rules.IWordDetector;
import org.quantumlabs.kitt.Activator;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.util.Helper;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.text.TTCNCodeScanner;
import org.quantumlabs.kitt.ui.text.UnionDetecor;

public class LineBasedDocumentIndex implements IDocumentIndex {

	private final HashMap<String, Word> index;
	private ArrayList<Line> lines;
	private IDocument doc;
	public static IWordDetector WORD_DETECTOR = new UnionDetecor();

	private final InternalDriver driver;

	public LineBasedDocumentIndex() {
		index = new HashMap<String, Word>();
		driver = new InternalDriver();
	}

	@Override
	/**
	 * Associate index with given document.
	 * */
	public void install(IDocument target) {
		Assert.isNotNull(target);
		IDocument doc = IDocument.class.cast(target);
		try {
			lines = new ArrayList<Line>(doc.getLineOfOffset(doc.getLength()));
		} catch (BadLocationException e) {
			lines = new ArrayList<Line>();
		}
		doc.addDocumentListener(driver);
	}

	class InternalDriver implements IDocumentListener {
		TTCNCodeScanner scanner;
		DocumentRewriteSession rewrite;

		void startRewrite(DocumentRewriteSession session) {
			Assert.isTrue(rewrite == null);
			rewrite = session;
		}

		private boolean isRewriting() {
			return rewrite != null;
		}

		void stopRewrite(Document doc) {
			Assert.isNotNull(doc);
			if (rewrite != null) {
				doc.stopRewriteSession(rewrite);
				rewrite = null;
			}
		}

		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
			try {
				damage(event);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void documentChanged(DocumentEvent event) {
			IDocument doc = event.getDocument();
			if (isRewriting()) {
				return;
			}
			if (LineBasedDocumentIndex.this.doc == null || !LineBasedDocumentIndex.this.doc.equals(doc)) {
				inputChanged(event);
				return;
			}
			if (event.getDocument().getLength() == 0) {
				return;
			}
			try {
				repairer(event);
			} catch (BadLocationException e) {
				if (Logger.isErrorEnable()) {
					Logger.logError(LineBasedDocumentIndex.this.toString(), e,
							"LineBasedDocumentIndex$InternalDriver.documentChanged()");
				}
			} finally {
				stopRewrite((Document) event.getDocument());
			}
		}
	}

	private void repairer(DocumentEvent event) throws BadLocationException {
		// if (isRewriteNeeded(event)) {
		// rewrite(event);
		// }
		int startLine = doc.getLineOfOffset(event.getOffset());
		int endLine = computeLastLineNeedToRepairer(event);
		int affectedCount = endLine - startLine + 1;
		for (; startLine <= endLine; startLine++) {
			setupLineIndex(startLine, event.getDocument());
		}

		for (; startLine < lines.size(); startLine++) {
			lines.get(startLine).notifyLineNumberChanged(affectedCount);
		}
	}

	@Deprecated
	private void logIndex() {
		StringBuilder sBuilder = new StringBuilder();
		for (Entry<String, Word> entry : index.entrySet()) {
			sBuilder.append(entry.getValue().toString()).append("\n");
		}
		System.out.println(sBuilder.toString());
	}

	@Deprecated
	private void rewrite(DocumentEvent event) throws BadLocationException {
		Document doc = (Document) event.getDocument();
		String replacement = event.getText();
		driver.startRewrite(doc.startRewriteSession(DocumentRewriteSessionType.SEQUENTIAL));
		doc.replace(event.getOffset(), replacement.length(), replacement);
		driver.stopRewrite(doc);
	}

	private boolean isRewriteNeeded(DocumentEvent event) {
		int ofst = event.getOffset();
		int len = event.getDocument().getLength();
		String replacement = event.getText();
		return len < ofst + replacement.length();
	}

	public void inputChanged(DocumentEvent event) {
		doc = event.getDocument();
		if (Logger.isDebugEnable()) {
			Logger.logDebug(this.toString(), "LineBasedDocumentIndex.inputChanged()", "old" + this.doc, "new" + doc);
		}

		if (doc == null) {
			dispose();
			return;
		}

		doc.addDocumentListener(driver);
		try {
			repairer(event);
		} catch (BadLocationException e) {
			if (Logger.isErrorEnable()) {
				Logger.logError(this.toString(), e);
			}
		}
	}

	private void damage(DocumentEvent event) throws BadLocationException {
		IDocument doc = event.getDocument();
		if (doc.getLength() == 0) {
			return;
		}
		int offset = event.getOffset();
		int len = event.getLength();
		int startLine = doc.getLineOfOffset(offset);
		int endLine = doc.getLineOfOffset(offset + len);
		int damagedCount = endLine - startLine + 1;
		for (int i = 0; i < damagedCount; i++) {
			Line damagedLine = lines.remove(startLine);
			damagedLine.notifyRemove();
		}

		for (; startLine < lines.size(); startLine++) {
			lines.get(startLine).notifyLineNumberChanged(0 - damagedCount);
		}

		if (Logger.isDebugEnable()) {
			Logger.logDebug(toString(), "damager()", index.toString());
		}
	}

	private void setupLineIndex(int lineNo, IDocument doc) throws BadLocationException {
		Line previous = lineNo == 0 ? null : lines.get(lineNo - 1);
		Line next = lineNo > lines.size() - 1 ? null : lines.get(lineNo);
		Line line = new Line(lineNo);
		line.notifyInsert(doc, previous, next);
	}

	// include the last line
	private int computeLastLineNeedToRepairer(DocumentEvent event) throws BadLocationException {
		int len = event.getText().length();
		int lastOfst = event.getOffset() + len;
		int affectedLastLine = event.getDocument().getLineOfOffset(lastOfst);
		return affectedLastLine;
	}

	class Line implements Comparable<Line> {
		private int number;
		private final ArrayList<Observer> obs;
		private Line next;

		public Line(int number) {
			this.number = number;
			obs = new ArrayList<Observer>();
		}

		@Override
		public String toString() {
			return String.valueOf(number);
		}

		public void notifyRemove() {
			Observer[] obsCopy = obs.toArray(new Observer[obs.size()]);
			for (Observer ob : obsCopy) {
				ob.update(null, this);
			}
			// next.notifyLineNumberChanged(-1);
		}

		public void notifyLineNumberChanged(int delta) {
			number = number + delta;
			// System.out.println(number);
			// if (next != null) {
			// next.notifyLineNumberChanged(delta);
			// }
		}

		public void notifyInsert(IDocument doc, Line previous, Line next) throws BadLocationException {
			if (previous != null) {
				previous.next = this;
			}
			this.next = next;
			int lineOfst = doc.getLineOffset(number);
			int lineLen = doc.getLineLength(number);
			lines.add(number, this);
			// if (next != null) {
			// next.notifyLineNumberChanged(1);
			// }
			insert(doc, lineOfst, lineLen);
		}

		private void insert(IDocument doc, int lineOfst, int lineLen) throws BadLocationException {
			StringBuilder buffer = new StringBuilder();
			for (int ofst = lineOfst; ofst < lineOfst + lineLen; ofst++) {
				char c = doc.getChar(ofst);
				if (WORD_DETECTOR.isWordPart(c)) {
					buffer.append(c);
				} else if (buffer.length() > 0 && WORD_DETECTOR.isWordPart(c)) {
					buffer.append(c);
				} else {
					String word = buffer.toString();
					if (buffer.length() > 0 && !Helper.isXInY(word, TTCNCodeScanner.TTCN_KEY_WORD)) {
						addIndex(word, this);
					}
					buffer = new StringBuilder();
					// skip some special characters(' ', '\r', '\n') and
					// keywords.
				}
			}
			if (buffer.length() > 0 && !Helper.isXInY(buffer.toString(), TTCNCodeScanner.TTCN_KEY_WORD)) {
				addIndex(buffer.toString(), this);
			}
		}

		@Override
		public int compareTo(Line o) {
			return number - o.number;
		}

		public void addObserver(Word word) {
			obs.add(word);
		}
	}

	class Word implements Observer {
		private final SortedSet<Line> references = new TreeSet<Line>();
		private String value;

		@Override
		public String toString() {
			return new StringBuilder().append("\"").append(value).append("\"").append("->")
					.append(references.toString()).toString();
		}

		@Override
		public void update(Observable o, Object arg) {
			Assert.isTrue(arg instanceof Line);
			references.remove(arg);
			if (references.isEmpty()) {
				fireRemoveEvent(this);
			}
		}

		public void refernceTo(Line line) {
			references.add(line);
			line.addObserver(this);
		}

		private void fireRemoveEvent(Word word) {
			tearDownIndex(word.value);
		}
	}

	private int[] getReferenceLine(String value) {
		Word word = index.get(value);
		if (word == null) {
			return new int[0];
		}

		int[] lines = new int[word.references.size()];
		int i = 0;
		for (Line line : word.references) {
			lines[i++] = line.number;
		}
		return lines;
	}

	private void dispose() {
		for (int i = 0; i < lines.size(); i++) {
			lines.remove(i).notifyRemove();
		}
	}

	protected IRegion[] getOccurrence(String identifier) throws BadLocationException {
		if (doc == null) {
			throw new IllegalStateException("No IDocument installed!");
		}
		LinkedList<IRegion> regions = new LinkedList<IRegion>();

		int[] nos = getReferenceLine(identifier);
		for (int i = 0; i < nos.length; i++) {
			int ofst = doc.getLineOffset(nos[i]);
			int lineLen = doc.getLineLength(nos[i]);
			String text = doc.get(ofst, lineLen);
			int bingo = text.indexOf(identifier, 0);
			Assert.isTrue(bingo > -1);
			if (wrapped(bingo, identifier.length(), text)) {
				regions.add(new Region(bingo + ofst, identifier.length()));
			}
			while (bingo < text.length() - identifier.length()) {
				bingo = text.indexOf(identifier, bingo + identifier.length());
				if (bingo == -1) {
					break;
				}
				if (wrapped(bingo, identifier.length(), text)) {
					regions.add(new Region(bingo + ofst, identifier.length()));
				}
			}
		}

		return regions.toArray(new IRegion[regions.size()]);
	}

	private boolean wrapped(int offset, int length, String text) {
		if (offset == 0 && length == text.length()) {
			return true;
		}

		if (offset > 0 && offset + length < text.length()) {
			int before = text.charAt(offset - 1);
			int after = text.charAt(offset + length);
			return !isIdentifierPart(before) && !isIdentifierPart(after);
		} else if (offset == 0) {
			int after = text.charAt(offset + length);
			return !isIdentifierPart(after);
		} else if (offset + length == text.length()) {
			int before = text.charAt(offset - 1);
			return !isIdentifierPart(before);
		} else {
			throw new IllegalArgumentException(String.format("Error offset : %s, or length : %s", offset, length));
		}
	}

	private boolean isIdentifierPart(int c) {
		return Helper.isLettersInLowerCase(c) || Helper.isLettersInUpperCase(c) || Helper.isNumber(c)
				|| Helper.isUnderscore(c);
	}

	private void addIndex(String value, Line line) {
		if (!(index.containsKey(value))) {
			setupIndex(value, line);
		} else {
			index.get(value).refernceTo(line);
		}
	}

	private void setupIndex(String value, Line line) {
		Assert.isTrue(index.get(value) == null);
		Word word = new Word();
		word.value = value;
		word.refernceTo(line);
		index.put(value, word);
	}

	private void tearDownIndex(String value) {
		Assert.isNotNull(index.get(value));
		index.remove(value);
	}

	@Override
	public IRegion[] find(Object element) {
		if (!canFind(element)) {
			return new IRegion[0];
		}
		try {
			return getOccurrence((String) element);
		} catch (Exception e) {
			if (Logger.isErrorEnable()) {
				Logger.logError(toString(), e, "Document index failed. I have to re-index all.");
			}
			Job job = new Job("Re-index document") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					reIndex();
					return new Status(IStatus.OK, Activator.PLUGIN_ID, "Indexing complete");
				}
			};
			job.setPriority(Job.DECORATE);
			job.schedule();
		}
		return new IRegion[0];
	}

	private boolean canFind(Object element) {
		return element instanceof String && !Helper.isWhitespace(element.toString());
	}

	private void reIndex() {
		try {
			String content = doc.get(0, doc.getLength());
			DocumentEvent event = new DocumentEvent(doc, 0, doc.getLength(), content);
			driver.documentChanged(event);
		} catch (BadLocationException e) {
			if (Logger.isErrorEnable()) {
				Logger.logError(toString(), e,
						"Document index reIndex failed! I can't handle it anymore, ignore the exception.");
			}
		}
	}
}
