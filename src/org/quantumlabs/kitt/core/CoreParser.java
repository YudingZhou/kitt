package org.quantumlabs.kitt.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.quantumlabs.kitt.core.parse.TesLexer;
import org.quantumlabs.kitt.core.parse.TesParser;
import org.quantumlabs.kitt.core.parse.TesParser.AltStepDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.AttributeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.CompilationUnitContext;
import org.quantumlabs.kitt.core.parse.TesParser.ConstDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ControlDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionParaContext;
import org.quantumlabs.kitt.core.parse.TesParser.GroupDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ModuleContext;
import org.quantumlabs.kitt.core.parse.TesParser.ModuleParDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ReturnExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.SignatureDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.TemplateDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.TestCaseDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.TypeDefContext;
import org.quantumlabs.kitt.ui.util.exception.StackTracableException;

/**
 * CoreParser is responsible for 1. Parsing input to Antlr4 parsing tree 2.
 * Transform a antlr4 parsing tree to an ITTCNElement
 * */
public class CoreParser {
	/**
	 * Binding rule context and corresponding parsing rule.
	 * */
	final protected static HashMap<Class<?>, Method> PARSE_ENTRY;
	/**
	 * Binding rule context and corresponding ttcn element
	 * */
	final protected static HashMap<Class<? extends RuleContext>, Class<? extends ITTCNElement>> RULE_ELEMENT_MAPPTING;
	static {
		PARSE_ENTRY = new HashMap<Class<?>, Method>();
		Method[] methods = TesParser.class.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getReturnType().getSimpleName().endsWith("Context"))
				PARSE_ENTRY.put(method.getReturnType(), method);
		}
		RULE_ELEMENT_MAPPTING = new HashMap<Class<? extends RuleContext>, Class<? extends ITTCNElement>>();
		RULE_ELEMENT_MAPPTING.put(AltStepDefContext.class, AltStepDeclaration.class);
		RULE_ELEMENT_MAPPTING.put(AttributeDefContext.class, AttributeDeclaration.class);
		RULE_ELEMENT_MAPPTING.put(CompilationUnitContext.class, CompilationUnit.class);
		RULE_ELEMENT_MAPPTING.put(ConstDefContext.class, ConstDeclaration.class);
		RULE_ELEMENT_MAPPTING.put(ControlDefContext.class, ControlPart.class);
		RULE_ELEMENT_MAPPTING.put(FunctionDefContext.class, FunctionDeclaration.class);
		RULE_ELEMENT_MAPPTING.put(GroupDefContext.class, GroupDeclaration.class);
		RULE_ELEMENT_MAPPTING.put(ModuleParDefContext.class, ModuleParDeclaration.class);
		RULE_ELEMENT_MAPPTING.put(ReturnExprContext.class, ReturnExpr.class);
		RULE_ELEMENT_MAPPTING.put(SignatureDefContext.class, SignatureDeclaration.class);
		RULE_ELEMENT_MAPPTING.put(TemplateDefContext.class, Template.class);
		RULE_ELEMENT_MAPPTING.put(TestCaseDefContext.class, TestCase.class);
		RULE_ELEMENT_MAPPTING.put(TypeDefContext.class, TypeDeclaration.class);
	}

	/**
	 * Parse input char stream into give RuleContext.
	 * 
	 * @param in
	 *            Input char stream.
	 * @param clazz
	 *            The rule context wanted.
	 * @return Corresponding RuleContext.
	 * */
	@SuppressWarnings("unchecked")
	public <T extends RuleContext> T parse(CharStream in, Class<T> clazz) {
		try {
			Method m = getParseEntry(clazz);
			Assert.isNotNull(m);
			TesLexer lexer = new TesLexer(in);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			TesParser parser = new TesParser(tokens);
			return (T) m.invoke(parser);
		} catch (Exception e) {
			throw new StackTracableException(e);
		}
	}

	@Deprecated
	public CompilationUnit buildCompilation(CharStream in) {
		ModuleContext module = parse(in, ModuleContext.class);
		CompilationUnit cp = new CompilationUnit();
		cp.parse(module);
		return cp;
	}

	private Method getParseEntry(Class<?> clazz) {
		return PARSE_ENTRY.get(clazz);
	}

	public static CharStream openStream(String content) throws IOException {
		return new ANTLRInputStream(new StringReader(content));
	}

	public static CharStream openStreamFromFile(String filePath) throws IOException {
		return new ANTLRFileStream(filePath);
	}

	public static CharStream openStream(IFile source) throws CoreException, IOException {
		ITextFileBuffer buffer = ITextFileBufferManager.DEFAULT.getTextFileBuffer(source.getLocation(),
				LocationKind.IFILE);
		InputStream in = buffer.getFileStore().openInputStream(EFS.NONE, new NullProgressMonitor());
		return new ANTLRInputStream(in);
	}
}
