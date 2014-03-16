package org.quantumlabs.kitt.core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.quantumlabs.kitt.core.parse.TesLexer;
import org.quantumlabs.kitt.core.parse.TesParser;
import org.quantumlabs.kitt.core.parse.TesParser.ImportDefContext;

public class ImportDeclaration extends TextBasedTTCNElement implements IImportDeclaration {

	private String sourceModuleName;
	private List<String> importedElements;
	private boolean all;

	public ImportDeclaration(ITTCNElement ancestor, String name) {
		super(ancestor, name);
		importedElements = new LinkedList<String>();
	}

	@Override
	public int getOffset() {
		// import from ID. We define the first ID as the start offset of
		// import declaration.
		return ((TerminalNode) getCorrespondingParserRuleContext().getChild(2)).getSymbol().getStartIndex();
	}

	@Override
	public int getElementType() {
		return ITTCNElement.IMPORT_DECLARATION;
	}

	@Override
	public String getSourceModule() {
		return sourceModuleName;
	}

	@Override
	public String[] getImportedElements() {
		return importedElements.toArray(new String[importedElements.size()]);
	}

	@Override
	public boolean isImportAll() {
		return all;
	}

	public void setImportAll(boolean all) {
		this.all = all;
	}

	public void setSourceModule(String name) {
		sourceModuleName = name;
	}

	public void addImportElement(String... names) {
		importedElements.addAll(Arrays.asList(names));
	}
}
