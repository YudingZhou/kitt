package org.quantumlabs.kitt.core;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.quantumlabs.kitt.core.parse.TesParser.AltStepDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.AttributeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ConstDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ControlDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.EnumeratedTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionParaContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionRunsOnContext;
import org.quantumlabs.kitt.core.parse.TesParser.GroupDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ModuleContext;
import org.quantumlabs.kitt.core.parse.TesParser.ModuleParDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.PrimitiveTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.RecordOfTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.RecordTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ReturnExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.SetOfTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.SetTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.SignatureDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.TemplateDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.TestCaseDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.TypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.UnionTypeDefContext;
import org.quantumlabs.kitt.core.util.Callback;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class CompilationUnit extends AbstractTTCNElement implements ICompilationUnit {
	private String id;

	public CompilationUnit(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	public CompilationUnit() {
	}

	public CompilationUnit(ITTCNElement ancestor) {
		super(ancestor);
	}

	@Override
	public IImportDeclaration[] getImportDeclarations() {
		return getChildren(ITTCNElement.IMPORT_DECLARATION, new IImportDeclaration[0]);
	}

	@Override
	public IConstantDeclaration[] getConstants() {
		return getChildren(ITTCNElement.CONSTANT_DECLARATION, new IConstantDeclaration[0]);
	}

	@Override
	public ITypeDeclaration[] getTypeDeclarations() {
		return getChildren(ITTCNElement.TYPE, new ITypeDeclaration[0]);
	}

	@Override
	public ITemplate[] getTemplates() {
		return getChildren(ITTCNElement.TEMPLATE, new ITemplate[0]);
	}

	@Override
	public IFunctionDeclaraion[] getFunctions() {
		return getChildren(ITTCNElement.FUNCTION, new IFunctionDeclaraion[0]);
	}

	@Override
	public ITestCase[] getTestCases() {
		return getChildren(ITTCNElement.TEST_CASE, new ITestCase[0]);
	}

	@Override
	public IControlPart getControlPart() {
		IControlPart[] controlPart = getChildren(ITTCNElement.CONTROL_PART, new IControlPart[0]);
		return (controlPart == null || controlPart.length == 0) ? null : controlPart[0];
	}

	@Override
	public ISignatureDeclaration[] getSignatures() {
		return getChildren(ITTCNElement.SIGNATURE_DECLARATION, new ISignatureDeclaration[0]);
	}

	@Override
	public IModuleParDeclaration[] getModulePars() {
		return getChildren(ITTCNElement.MODULE_PAR_DECLARATION, new IModuleParDeclaration[0]);
	}

	@Override
	public IAttributeDeclaration[] getAttributes() {
		return getChildren(ITTCNElement.ATTRIBUTE_DECLARATION, new IAttributeDeclaration[0]);
	}

	@Override
	public IGroupDeclaration[] getGroups() {
		return getChildren(ITTCNElement.GROUP_DECLARATION, new IGroupDeclaration[0]);
	}

	@Override
	public IAltStepDeclaration[] getAltSteps() {
		return getChildren(ITTCNElement.ALTSTEP_DECLARATION, new IAltStepDeclaration[0]);
	}

	public void setID(String id) {
		this.id = id;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public void parse(ParserRuleContext context) {
		ModuleContext moduleContext = (ModuleContext) context;
		setID(moduleContext.ID().getText());
		setCorrespondingParserRuleContext(moduleContext);
		ParseTreeWalker walker = new ParseTreeWalker();
		try {
			walker.walk(new BaseListenerPro() {

				@Override
				protected void handleError(Exception e, Object context) {
					CompilationUnit.this.handleError(e, context);
				}

				@Override
				public void internalEnterImportDef(ImportDefContext ctx) {
					ImportDeclaration importDec = new ImportDeclaration(CompilationUnit.this, "NONE");
					importDec.setCorrespondingParserRuleContext(ctx);
				}

				@Override
				public void internalEnterFunctionDef(FunctionDefContext ctx) {
					try {
						FunctionDefContext funDef = ctx;
						funDef.start.getStartIndex();
						String funName = funDef.functionDec().ID().getText();
						ReturnExprContext returnExpr = funDef.returnExpr();
						FunctionParaContext paraContext = funDef.functionPara();
						FunctionRunsOnContext runsOnContext = funDef.functionRunsOn();
						FunctionDeclaration fun = new FunctionDeclaration(CompilationUnit.this, funName);
						fun.setCorrespondingParserRuleContext(funDef);
						fun.setID(funName);
						if (returnExpr != null) {
							ReturnExpr tReturnExpr = new ReturnExpr(fun, "No name//TODO");
							tReturnExpr.setExpr(returnExpr.reference().getText());
							fun.setReturnExpr(tReturnExpr);
						}
						if (paraContext != null) {
							ArgumentDeclarationList paras = new ArgumentDeclarationList(fun);
							paras.setCorrespondingParserRuleContext(paraContext);
						}
						if (runsOnContext != null) {
							fun.setRunsOnType(runsOnContext.ID(runsOnContext.ID().size() - 1).getText());
						}
					} catch (Exception e) {
						System.out.println();
					}
				}

				@Override
				public void internalEnterTypeDef(TypeDefContext ctx) {
					TypeDefContext typeDef = ctx;
					PrimitiveTypeDefContext primitiveDef = typeDef.primitiveTypeDef();
					RecordTypeDefContext recordDef = typeDef.recordTypeDef();
					SetTypeDefContext setDef = typeDef.setTypeDef();
					RecordOfTypeDefContext recordOfDef = typeDef.recordOfTypeDef();
					SetOfTypeDefContext setOfDef = typeDef.setOfTypeDef();
					UnionTypeDefContext union = typeDef.unionTypeDef();
					EnumeratedTypeDefContext enumerated = typeDef.enumeratedTypeDef();
					String id = null;
					String type = null;
					// For syntax structure, please reference to G4
					// definition of typeDef
					if (primitiveDef != null) {
						id = primitiveDef.getChild(2).getChild(1).getText();
						type = primitiveDef.getChild(2).getChild(1).getText();
					} else if (recordDef != null) {
						id = recordDef.recordDec().ID().getText();
						type = "record";
					} else if (setDef != null) {
						id = setDef.setDec().ID().getText();
						type = "set";
					} else if (recordOfDef != null) {
						id = recordOfDef.typeDec().ID(recordOfDef.typeDec().ID().size() - 1).getText();
						type = recordOfDef.typeDec().ID(recordOfDef.typeDec().ID().size() - 2).getText();
					} else if (setOfDef != null) {
						id = setOfDef.typeDec().ID(setOfDef.typeDec().ID().size() - 1).getText();
						type = setOfDef.typeDec().ID(setOfDef.typeDec().ID().size() - 2).getText();
					} else if (union != null) {
						id = union.typeDec(0).ID(union.typeDec(0).ID().size() - 1).getText();
						type = union.typeDec(0).ID(union.typeDec(0).ID().size() - 2).getText();
					} else if (enumerated != null) {
						id = enumerated.enumeratedDec().ID().getText();
						type = "enumerated";
					}
					TypeDeclaration typeDeclaration = new TypeDeclaration(CompilationUnit.this, id);
					typeDeclaration.setCorrespondingParserRuleContext(typeDef);
					typeDeclaration.setID(id);
					typeDeclaration.setSuperType(type);

				}

				@Override
				public void internalEnterConstDef(ConstDefContext ctx) {
					ConstDefContext consDef = ctx;
					String id = null;
					String type = null;
					if (consDef.primitiveConstDef().start.getText().equals("external")) {
						id = consDef.primitiveConstDef().getChild(2).getChild(1).getText();
						type = consDef.primitiveConstDef().getChild(2).getChild(0).getText();
					} else {
						id = consDef.primitiveConstDef().getChild(1).getChild(1).getText();
						type = consDef.primitiveConstDef().getChild(0).getChild(0).getText();
					}
					ConstDeclaration constDec = new ConstDeclaration(CompilationUnit.this, id);
					constDec.setCorrespondingParserRuleContext(consDef);
					constDec.setID(id);
					constDec.setType(type);

				}

				@Override
				public void internalEnterAttributeDef(AttributeDefContext ctx) {
					AttributeDefContext attributeDef = ctx;
					AttributeDeclaration attribute = new AttributeDeclaration(CompilationUnit.this, "NONE//TODO");
					attribute.setCorrespondingParserRuleContext(attributeDef);
				}

				@Override
				public void internalEnterTemplateDef(TemplateDefContext ctx) {

					TemplateDefContext templateDef = ctx;
					String id = templateDef.templateDec().typeDec()
							.ID((templateDef.templateDec().typeDec().ID().size() - 1)).getText();
					String type = templateDef.templateDec().typeDec()
							.ID((templateDef.templateDec().typeDec().ID().size() - 2)).getText();
					Template template = new Template(CompilationUnit.this, id);
					template.setCorrespondingParserRuleContext(templateDef);
					template.setType(type);

				}

				@Override
				public void internalEnterModuleParDef(ModuleParDefContext ctx) {
					ModuleParDefContext moduleParDef = ctx;
					ModuleParDeclaration par = new ModuleParDeclaration(CompilationUnit.this, "NONE//TODO");
					par.setCorrespondingParserRuleContext(moduleParDef);

				}

				@Override
				public void internalEnterGroupDef(GroupDefContext ctx) {
					GroupDefContext groupDef = ctx;
					GroupDeclaration group = new GroupDeclaration(CompilationUnit.this, "NONE");
					group.setCorrespondingParserRuleContext(groupDef);
				}

				@Override
				public void internalEnterControlDef(ControlDefContext ctx) {
					ControlDefContext controlDef = ctx;
					ControlPart control = new ControlPart(CompilationUnit.this, "NONE//TODO");
					control.setCorrespondingParserRuleContext(controlDef);
				}

				@Override
				public void internalEnterSignatureDef(SignatureDefContext ctx) {
					SignatureDefContext sigDef = ctx;
					SignatureDeclaration signature = new SignatureDeclaration(CompilationUnit.this, "NONE//TODO");
					signature.setCorrespondingParserRuleContext(sigDef);
				}

				@Override
				public void internalEnterAltStepDef(AltStepDefContext ctx) {
					AltStepDefContext altstepDef = ctx;
					AltStepDeclaration alt = new AltStepDeclaration(CompilationUnit.this, "NONE");
					alt.setCorrespondingParserRuleContext(altstepDef);
				}

				@Override
				public void internalEnterTestCaseDef(TestCaseDefContext ctx) {
					TestCaseDefContext testCaseDef = ctx;
					String id = testCaseDef.ID(0).getText();
					TestCase testCase = new TestCase(CompilationUnit.this, "NONE");
					testCase.setCorrespondingParserRuleContext(testCaseDef);
					testCase.setID(id);
				}

				@Override
				public void internalVisitErrorNode(ErrorNode node) {
					errors.add(node.getSymbol());
				}
			}, moduleContext);
		} finally {
			fireEventToCallBacks();
		}

	}

	protected void handleError(Exception e, Object ctx) {
		if (Logger.isErrorEnable()) {
			Logger.logError(String.format("CompilationUnit parsing : %s", ctx.getClass()));
		}
	}

	private void fireEventToCallBacks() {
		for (Callback callBack : callBacks) {
			callBack.call(this);
		}
	}

	@Override
	public void clear() {
		children2.clear();
		setID("");
	}

	@Override
	public int getElementType() {
		return ITTCNElement.COMPILATION_UNIT;
	}

	@Override
	public String getName() {
		return id;
	}
}
