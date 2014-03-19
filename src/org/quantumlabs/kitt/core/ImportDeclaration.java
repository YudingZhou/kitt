package org.quantumlabs.kitt.core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.quantumlabs.kitt.core.parse.TesParser.ImportDefContext;
import org.quantumlabs.kitt.ui.util.exception.StackTracableException;

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
		checkDirtyStatus();
		return offset;
	}

	@Override
	public int getElementType() {
		return ITTCNElement.IMPORT_DECLARATION;
	}

	@Override
	public String getSourceModule() {
		checkDirtyStatus();
		return sourceModuleName;
	}

	@Override
	public String[] getImportedElements() {
		checkDirtyStatus();
		return importedElements.toArray(new String[importedElements.size()]);
	}

	@Override
	public boolean isImportAll() {
		checkDirtyStatus();
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

	@Override
	public void parse(ParserRuleContext context) {
		ImportDefContext importDef = (ImportDefContext) context;
		// @see .g4 "importFrom" rule
		setSourceModule(importDef.importFrom().ID().getText());
		offset = ((TerminalNode)importDef.importFrom().ID()).getSymbol().getStartIndex();
		if (importDef.importAllAndSuppressionDef() != null) {
			//TODO : analyze imported elements
		} else if (importDef.importNormally() != null) {
			//TODO : analyze imported elements
		} else if (importDef.importOtherLanguage() != null) {
			//TODO : analyze imported elements
		} else if (importDef.importRecusively() != null) {
			//TODO : analyze imported elements
		} else {
			throw new StackTracableException("Alternative of import definite rule context is not right.");
		}
	}
}
