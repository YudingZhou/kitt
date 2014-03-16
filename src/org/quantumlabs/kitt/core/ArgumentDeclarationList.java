package org.quantumlabs.kitt.core;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionParaContext;
import org.quantumlabs.kitt.core.parse.TesParser.TemplateDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.TypeDecContext;

public class ArgumentDeclarationList extends TextBasedTTCNElement implements IArgumentDeclarationList {
	public ArgumentDeclarationList(ITTCNElement ancestor) {
		super(ancestor);
	}

	@Override
	public IArgumentDeclaration[] getArgumentDeclarations() {
		return getChildren(ITTCNElement.ARGUMENT_DECLARATION, new IArgumentDeclaration[0]);
	}

	@Override
	public ITTCNElement[] getChildren() {
		if (super.getChildren().length == 0) {
			FunctionParaContext paras = (FunctionParaContext) getCorrespondingParserRuleContext();
			for (int i = 0; i < paras.functionParaPrimary().size(); i++) {
				ArgumentDeclaration argumentDec = new ArgumentDeclaration(this);
				argumentDec.setCorrespondingParserRuleContext(paras.functionParaPrimary().get(i));
			}
		}
		return super.getChildren();
	}

	@Override
	public int getElementType() {
		return ITTCNElement.ARGUMENT_DECLARATION_LIST;
	}

	class ArgumentDeclaration extends TextBasedTTCNElement implements IArgumentDeclaration {
		private String argumentName = "";
		private String argumentType = "";

		public ArgumentDeclaration(ITTCNElement ancestor) {
			super(ancestor);
		}

		@Override
		public String getType() {
			if (!isParsed()) {
				parse(ArgumentDeclaration.this.getCorrespondingParserRuleContext());
			}
			return argumentType;
		}

		@Override
		public String getArgument() {
			if (!isParsed()) {
				parse(ArgumentDeclaration.this.getCorrespondingParserRuleContext());
			}
			return argumentName;

		}

		private boolean isParsed() {
			return !argumentName.equals("") && !argumentType.equals("");
		}

		@Override
		public void parse(ParserRuleContext context) {
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(new BaseListenerPro() {
				// in case template dec is consist of 'template' and
				// TypeDec,
				// avoid typeDec is passed twice.
				boolean isTemplateArgument = false;

				@Override
				protected void internalEnterTemplateDec(TemplateDecContext ctx) {
					isTemplateArgument = true;
					String[] typeAndName = ParseHelper.parseTypeDec(ctx.typeDec());
					argumentName = typeAndName[1];
					argumentType = typeAndName[0];
				}

				@Override
				protected void internalEnterTypeDec(TypeDecContext ctx) {
					if (isTemplateArgument) {
						return;
					}
					String[] typeAndName = ParseHelper.parseTypeDec(ctx);
					argumentName = typeAndName[1];
					argumentType = typeAndName[0];
				}

			}, context);
		}

		@Override
		public int getElementType() {
			return ITTCNElement.ARGUMENT_DECLARATION;
		}
	}
}
