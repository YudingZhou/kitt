package org.quantumlabs.kitt.core;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.quantumlabs.kitt.core.parse.*;
import org.quantumlabs.kitt.core.parse.TesParser.AltStatContext;
import org.quantumlabs.kitt.core.parse.TesParser.AltStatPrimaryContext;
import org.quantumlabs.kitt.core.parse.TesParser.AltStepDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ArithmeticExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.ArrayDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.ArrayExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.AssignmentContext;
import org.quantumlabs.kitt.core.parse.TesParser.AttributeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.BitstringDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.BitstringExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.BitstringOprntContext;
import org.quantumlabs.kitt.core.parse.TesParser.BooleanDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.BooleanExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.BooleanOprntContext;
import org.quantumlabs.kitt.core.parse.TesParser.CharDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.CharstringDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.CompilationUnitContext;
import org.quantumlabs.kitt.core.parse.TesParser.ComponentDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ConstDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ControlDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.DoWhileStatContext;
import org.quantumlabs.kitt.core.parse.TesParser.ElseStatContext;
import org.quantumlabs.kitt.core.parse.TesParser.EnumeratedDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.EnumeratedTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.FloatDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.ForStatContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionActualParaContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionBodyContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionBuildInCallContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionCallStatContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionParaContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionParaPrimaryContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionRunsOnContext;
import org.quantumlabs.kitt.core.parse.TesParser.FunctionSystemBuildInCallContext;
import org.quantumlabs.kitt.core.parse.TesParser.GotoStatContext;
import org.quantumlabs.kitt.core.parse.TesParser.GroupDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.HexstringDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.IfPrimaryStatContext;
import org.quantumlabs.kitt.core.parse.TesParser.IfStatContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportAllAndSuppressionDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportByKindDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportByNameDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportFromContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportGroupDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportNormallyContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportOtherLanguageContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportRecusivelyContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportRestrictingContext;
import org.quantumlabs.kitt.core.parse.TesParser.ImportableTypeContext;
import org.quantumlabs.kitt.core.parse.TesParser.IntDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.LabelStatContext;
import org.quantumlabs.kitt.core.parse.TesParser.LengthExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.LiteralsContext;
import org.quantumlabs.kitt.core.parse.TesParser.LogicalExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.LogicalOprntContext;
import org.quantumlabs.kitt.core.parse.TesParser.ModuleContext;
import org.quantumlabs.kitt.core.parse.TesParser.ModuleParDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.Not4bOpContext;
import org.quantumlabs.kitt.core.parse.TesParser.NotOpContext;
import org.quantumlabs.kitt.core.parse.TesParser.NumericOprntContext;
import org.quantumlabs.kitt.core.parse.TesParser.ObjidDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.OctetstringDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.PortDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.PortDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.PortMessageDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.PrimitiveConstDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.PrimitiveDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.PrimitiveTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.PrimitiveVarDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.PropertyExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.RangeOfBitstringContext;
import org.quantumlabs.kitt.core.parse.TesParser.RangeOfCharContext;
import org.quantumlabs.kitt.core.parse.TesParser.RangeOfCharstringContext;
import org.quantumlabs.kitt.core.parse.TesParser.RangeOfHexstringContext;
import org.quantumlabs.kitt.core.parse.TesParser.RangeOfIntegerContext;
import org.quantumlabs.kitt.core.parse.TesParser.RangeOfOctetstringContext;
import org.quantumlabs.kitt.core.parse.TesParser.RecordDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.RecordOfTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.RecordTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.ReferenceContext;
import org.quantumlabs.kitt.core.parse.TesParser.ReferencePrimaryContext;
import org.quantumlabs.kitt.core.parse.TesParser.RelationalExpr2Context;
import org.quantumlabs.kitt.core.parse.TesParser.RelationalExpr3Context;
import org.quantumlabs.kitt.core.parse.TesParser.RelationalExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.RelationalOprntContext;
import org.quantumlabs.kitt.core.parse.TesParser.ReturnExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.SetDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.SetOfTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.SetTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.SignatureDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.StateContext;
import org.quantumlabs.kitt.core.parse.TesParser.StringExprContext;
import org.quantumlabs.kitt.core.parse.TesParser.StringOprntContext;
import org.quantumlabs.kitt.core.parse.TesParser.TemplateBlockContext;
import org.quantumlabs.kitt.core.parse.TesParser.TemplateDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.TemplateDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.TemplateInlineDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.TemplateParaContext;
import org.quantumlabs.kitt.core.parse.TesParser.TestCaseDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.TimerDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.TimerStatContext;
import org.quantumlabs.kitt.core.parse.TesParser.TldContext;
import org.quantumlabs.kitt.core.parse.TesParser.TypeDecContext;
import org.quantumlabs.kitt.core.parse.TesParser.TypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.UnaryContext;
import org.quantumlabs.kitt.core.parse.TesParser.UnionTypeDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.VarDefContext;
import org.quantumlabs.kitt.core.parse.TesParser.WhileStatContext;

public class BaseListenerPro extends TesBaseListener {
	 protected void handleError(Exception e,Object context){}
	 @Override public final void enterNot4bOp(Not4bOpContext ctx) { try { interalEnterNot4bOp(ctx); } catch (Exception e) { handleError(e, ctx); } }
	 @Override public final void exitNot4bOp(Not4bOpContext ctx) {try{ 	  	 internalExitNot4bOp(ctx);}catch(Exception e){handleError(e,ctx);}} 
	 @Override public final void enterForStat(ForStatContext ctx) {try{ 	  	 internalEnterForStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitForStat(ForStatContext ctx) {try{ 	  	 internalExitForStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRecordOfTypeDef(RecordOfTypeDefContext ctx) {try{ 	  	 internalEnterRecordOfTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRecordOfTypeDef(RecordOfTypeDefContext ctx) {try{ 	  	 internalExitRecordOfTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterPropertyExpr(PropertyExprContext ctx) {try{ 	  	 internalEnterPropertyExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitPropertyExpr(PropertyExprContext ctx) {try{ 	  	 internalExitPropertyExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterNotOp(NotOpContext ctx) {try{ 	  	 internalEnterNotOp(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitNotOp(NotOpContext ctx) {try{ 	  	 internalExitNotOp(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterLengthExpr(LengthExprContext ctx) {try{ 	  	 internalEnterLengthExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitLengthExpr(LengthExprContext ctx) {try{ 	  	 internalExitLengthExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterLabelStat(LabelStatContext ctx) {try{ 	  	 internalEnterLabelStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitLabelStat(LabelStatContext ctx) {try{ 	  	 internalExitLabelStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterUnionTypeDef(UnionTypeDefContext ctx) {try{ 	  	 internalEnterUnionTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitUnionTypeDef(UnionTypeDefContext ctx) {try{ 	  	 internalExitUnionTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterAttributeDef(AttributeDefContext ctx) {try{ 	  	 internalEnterAttributeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitAttributeDef(AttributeDefContext ctx) {try{ 	  	 internalExitAttributeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterArrayExpr(ArrayExprContext ctx) {try{ 	  	 internalEnterArrayExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitArrayExpr(ArrayExprContext ctx) {try{ 	  	 internalExitArrayExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterCharDec(CharDecContext ctx) {try{ 	  	 internalEnterCharDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitCharDec(CharDecContext ctx) {try{ 	  	 internalExitCharDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterUnary(UnaryContext ctx) {try{ 	  	 internalEnterUnary(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitUnary(UnaryContext ctx) {try{ 	  	 internalExitUnary(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterFunctionRunsOn(FunctionRunsOnContext ctx) {try{ 	  	 internalEnterFunctionRunsOn(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitFunctionRunsOn(FunctionRunsOnContext ctx) {try{ 	  	 internalExitFunctionRunsOn(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterIntDec(IntDecContext ctx) {try{ 	  	 internalEnterIntDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitIntDec(IntDecContext ctx) {try{ 	  	 internalExitIntDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterTemplateDec(TemplateDecContext ctx) {try{ 	  	 internalEnterTemplateDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitTemplateDec(TemplateDecContext ctx) {try{ 	  	 internalExitTemplateDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterBooleanExpr(BooleanExprContext ctx) {try{ 	  	 internalEnterBooleanExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitBooleanExpr(BooleanExprContext ctx) {try{ 	  	 internalExitBooleanExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterTemplateDef(TemplateDefContext ctx) {try{ 	  	 internalEnterTemplateDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitTemplateDef(TemplateDefContext ctx) {try{ 	  	 internalExitTemplateDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterFunctionActualPara(FunctionActualParaContext ctx) {try{ 	  	 internalEnterFunctionActualPara(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitFunctionActualPara(FunctionActualParaContext ctx) {try{ 	  	 internalExitFunctionActualPara(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterConstDef(ConstDefContext ctx) {try{ 	  	 internalEnterConstDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitConstDef(ConstDefContext ctx) {try{ 	  	 internalExitConstDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterBooleanDec(BooleanDecContext ctx) {try{ 	  	 internalEnterBooleanDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitBooleanDec(BooleanDecContext ctx) {try{ 	  	 internalExitBooleanDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterModuleParDef(ModuleParDefContext ctx) {try{ 	  	 internalEnterModuleParDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitModuleParDef(ModuleParDefContext ctx) {try{ 	  	 internalExitModuleParDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterLogicalExpr(LogicalExprContext ctx) {try{ 	  	 internalEnterLogicalExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitLogicalExpr(LogicalExprContext ctx) {try{ 	  	 internalExitLogicalExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterSetDec(SetDecContext ctx) {try{ 	  	 internalEnterSetDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitSetDec(SetDecContext ctx) {try{ 	  	 internalExitSetDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRangeOfHexstring(RangeOfHexstringContext ctx) {try{ 	  	 internalEnterRangeOfHexstring(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRangeOfHexstring(RangeOfHexstringContext ctx) {try{ 	  	 internalExitRangeOfHexstring(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRecordDec(RecordDecContext ctx) {try{ 	  	 internalEnterRecordDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRecordDec(RecordDecContext ctx) {try{ 	  	 internalExitRecordDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterAltStepDef(AltStepDefContext ctx) {try{ 	  	 internalEnterAltStepDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitAltStepDef(AltStepDefContext ctx) {try{ 	  	 internalExitAltStepDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRangeOfOctetstring(RangeOfOctetstringContext ctx) {try{ 	  	 internalEnterRangeOfOctetstring(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRangeOfOctetstring(RangeOfOctetstringContext ctx) {try{ 	  	 internalExitRangeOfOctetstring(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterDoWhileStat(DoWhileStatContext ctx) {try{ 	  	 internalEnterDoWhileStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitDoWhileStat(DoWhileStatContext ctx) {try{ 	  	 internalExitDoWhileStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterFunctionPara(FunctionParaContext ctx) {try{ 	  	 internalEnterFunctionPara(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitFunctionPara(FunctionParaContext ctx) {try{ 	  	 internalExitFunctionPara(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterAltStat(AltStatContext ctx) {try{ 	  	 internalEnterAltStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitAltStat(AltStatContext ctx) {try{ 	  	 internalExitAltStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterAltStatPrimary(AltStatPrimaryContext ctx) {try{ 	  	 internalEnterAltStatPrimary(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitAltStatPrimary(AltStatPrimaryContext ctx) {try{ 	  	 internalExitAltStatPrimary(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterIfStat(IfStatContext ctx) {try{ 	  	 internalEnterIfStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitIfStat(IfStatContext ctx) {try{ 	  	 internalExitIfStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterOctetstringDec(OctetstringDecContext ctx) {try{ 	  	 internalEnterOctetstringDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitOctetstringDec(OctetstringDecContext ctx) {try{ 	  	 internalExitOctetstringDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterBitstringDec(BitstringDecContext ctx) {try{ 	  	 internalEnterBitstringDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitBitstringDec(BitstringDecContext ctx) {try{ 	  	 internalExitBitstringDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterFunctionBuildInCall(FunctionBuildInCallContext ctx) {try{ 	  	 internalEnterFunctionBuildInCall(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitFunctionBuildInCall(FunctionBuildInCallContext ctx) {try{ 	  	 internalExitFunctionBuildInCall(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterStringOprnt(StringOprntContext ctx) {try{ 	  	 internalEnterStringOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitStringOprnt(StringOprntContext ctx) {try{ 	  	 internalExitStringOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRelationalExpr2(RelationalExpr2Context ctx) {try{ 	  	 internalEnterRelationalExpr2(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRelationalExpr2(RelationalExpr2Context ctx) {try{ 	  	 internalExitRelationalExpr2(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterPrimitiveVarDef(PrimitiveVarDefContext ctx) {try{ 	  	 internalEnterPrimitiveVarDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitPrimitiveVarDef(PrimitiveVarDefContext ctx) {try{ 	  	 internalExitPrimitiveVarDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterHexstringDec(HexstringDecContext ctx) {try{ 	  	 internalEnterHexstringDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitHexstringDec(HexstringDecContext ctx) {try{ 	  	 internalExitHexstringDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRelationalExpr3(RelationalExpr3Context ctx) {try{ 	  	 internalEnterRelationalExpr3(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRelationalExpr3(RelationalExpr3Context ctx) {try{ 	  	 internalExitRelationalExpr3(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterObjidDec(ObjidDecContext ctx) {try{ 	  	 internalEnterObjidDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitObjidDec(ObjidDecContext ctx) {try{ 	  	 internalExitObjidDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterPrimitiveTypeDef(PrimitiveTypeDefContext ctx) {try{ 	  	 internalEnterPrimitiveTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitPrimitiveTypeDef(PrimitiveTypeDefContext ctx) {try{ 	  	 internalExitPrimitiveTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterIfPrimaryStat(IfPrimaryStatContext ctx) {try{ 	  	 internalEnterIfPrimaryStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitIfPrimaryStat(IfPrimaryStatContext ctx) {try{ 	  	 internalExitIfPrimaryStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterEnumeratedTypeDef(EnumeratedTypeDefContext ctx) {try{ 	  	 internalEnterEnumeratedTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitEnumeratedTypeDef(EnumeratedTypeDefContext ctx) {try{ 	  	 internalExitEnumeratedTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRangeOfBitstring(RangeOfBitstringContext ctx) {try{ 	  	 internalEnterRangeOfBitstring(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRangeOfBitstring(RangeOfBitstringContext ctx) {try{ 	  	 internalExitRangeOfBitstring(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterGroupDef(GroupDefContext ctx) {try{ 	  	 internalEnterGroupDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitGroupDef(GroupDefContext ctx) {try{ 	  	 internalExitGroupDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterFunctionDef(FunctionDefContext ctx) {try{ 	  	 internalEnterFunctionDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitFunctionDef(FunctionDefContext ctx) {try{ 	  	 internalExitFunctionDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterStringExpr(StringExprContext ctx) {try{ 	  	 internalEnterStringExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitStringExpr(StringExprContext ctx) {try{ 	  	 internalExitStringExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterGotoStat(GotoStatContext ctx) {try{ 	  	 internalEnterGotoStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitGotoStat(GotoStatContext ctx) {try{ 	  	 internalExitGotoStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterControlDef(ControlDefContext ctx) {try{ 	  	 internalEnterControlDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitControlDef(ControlDefContext ctx) {try{ 	  	 internalExitControlDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterCompilationUnit(CompilationUnitContext ctx) {try{ 	  	 internalEnterCompilationUnit(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitCompilationUnit(CompilationUnitContext ctx) {try{ 	  	 internalExitCompilationUnit(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterFunctionCallStat(FunctionCallStatContext ctx) {try{ 	  	 internalEnterFunctionCallStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitFunctionCallStat(FunctionCallStatContext ctx) {try{ 	  	 internalExitFunctionCallStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterFunctionDec(FunctionDecContext ctx) {try{ 	  	 internalEnterFunctionDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitFunctionDec(FunctionDecContext ctx) {try{ 	  	 internalExitFunctionDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterState(StateContext ctx) {try{ 	  	 internalEnterState(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitState(StateContext ctx) {try{ 	  	 internalExitState(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterExpr(ExprContext ctx) {try{ 	  	 internalEnterExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitExpr(ExprContext ctx) {try{ 	  	 internalExitExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterBitstringExpr(BitstringExprContext ctx) {try{ 	  	 internalEnterBitstringExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitBitstringExpr(BitstringExprContext ctx) {try{ 	  	 internalExitBitstringExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterCharstringDec(CharstringDecContext ctx) {try{ 	  	 internalEnterCharstringDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitCharstringDec(CharstringDecContext ctx) {try{ 	  	 internalExitCharstringDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterNumericOprnt(NumericOprntContext ctx) {try{ 	  	 internalEnterNumericOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitNumericOprnt(NumericOprntContext ctx) {try{ 	  	 internalExitNumericOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterReferencePrimary(ReferencePrimaryContext ctx) {try{ 	  	 internalEnterReferencePrimary(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitReferencePrimary(ReferencePrimaryContext ctx) {try{ 	  	 internalExitReferencePrimary(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterTemplatePara(TemplateParaContext ctx) {try{ 	  	 internalEnterTemplatePara(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitTemplatePara(TemplateParaContext ctx) {try{ 	  	 internalExitTemplatePara(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterEnumeratedDec(EnumeratedDecContext ctx) {try{ 	  	 internalEnterEnumeratedDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitEnumeratedDec(EnumeratedDecContext ctx) {try{ 	  	 internalExitEnumeratedDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterFunctionSystemBuildInCall(FunctionSystemBuildInCallContext ctx) {try{ 	  	 internalEnterFunctionSystemBuildInCall(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitFunctionSystemBuildInCall(FunctionSystemBuildInCallContext ctx) {try{ 	  	 internalExitFunctionSystemBuildInCall(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterTemplateInlineDef(TemplateInlineDefContext ctx) {try{ 	  	 internalEnterTemplateInlineDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitTemplateInlineDef(TemplateInlineDefContext ctx) {try{ 	  	 internalExitTemplateInlineDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterTimerStat(TimerStatContext ctx) {try{ 	  	 internalEnterTimerStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitTimerStat(TimerStatContext ctx) {try{ 	  	 internalExitTimerStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterTypeDec(TypeDecContext ctx) {try{ 	  	 internalEnterTypeDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitTypeDec(TypeDecContext ctx) {try{ 	  	 internalExitTypeDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterTypeDef(TypeDefContext ctx) {try{ 	  	 internalEnterTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitTypeDef(TypeDefContext ctx) {try{ 	  	 internalExitTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterImportDef(ImportDefContext ctx) {try{ 	  	 internalEnterImportDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitImportDef(ImportDefContext ctx) {try{ 	  	 internalExitImportDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterFunctionParaPrimary(FunctionParaPrimaryContext ctx) {try{ 	  	 internalEnterFunctionParaPrimary(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitFunctionParaPrimary(FunctionParaPrimaryContext ctx) {try{ 	  	 internalExitFunctionParaPrimary(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRangeOfCharstring(RangeOfCharstringContext ctx) {try{ 	  	 internalEnterRangeOfCharstring(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRangeOfCharstring(RangeOfCharstringContext ctx) {try{ 	  	 internalExitRangeOfCharstring(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterArrayDec(ArrayDecContext ctx) {try{ 	  	 internalEnterArrayDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitArrayDec(ArrayDecContext ctx) {try{ 	  	 internalExitArrayDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterModule(ModuleContext ctx) {try{ 	  	 internalEnterModule(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitModule(ModuleContext ctx) {try{ 	  	 internalExitModule(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterLiterals(LiteralsContext ctx) {try{ 	  	 internalEnterLiterals(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitLiterals(LiteralsContext ctx) {try{ 	  	 internalExitLiterals(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRelationalOprnt(RelationalOprntContext ctx) {try{ 	  	 internalEnterRelationalOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRelationalOprnt(RelationalOprntContext ctx) {try{ 	  	 internalExitRelationalOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterBitstringOprnt(BitstringOprntContext ctx) {try{ 	  	 internalEnterBitstringOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitBitstringOprnt(BitstringOprntContext ctx) {try{ 	  	 internalExitBitstringOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterBooleanOprnt(BooleanOprntContext ctx) {try{ 	  	 internalEnterBooleanOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitBooleanOprnt(BooleanOprntContext ctx) {try{ 	  	 internalExitBooleanOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterReturnExpr(ReturnExprContext ctx) {try{ 	  	 internalEnterReturnExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitReturnExpr(ReturnExprContext ctx) {try{ 	  	 internalExitReturnExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRecordTypeDef(RecordTypeDefContext ctx) {try{ 	  	 internalEnterRecordTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRecordTypeDef(RecordTypeDefContext ctx) {try{ 	  	 internalExitRecordTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterLogicalOprnt(LogicalOprntContext ctx) {try{ 	  	 internalEnterLogicalOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitLogicalOprnt(LogicalOprntContext ctx) {try{ 	  	 internalExitLogicalOprnt(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterElseStat(ElseStatContext ctx) {try{ 	  	 internalEnterElseStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitElseStat(ElseStatContext ctx) {try{ 	  	 internalExitElseStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterFunctionBody(FunctionBodyContext ctx) {try{ 	  	 internalEnterFunctionBody(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitFunctionBody(FunctionBodyContext ctx) {try{ 	  	 internalExitFunctionBody(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterPrimitiveDec(PrimitiveDecContext ctx) {try{ 	  	 internalEnterPrimitiveDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitPrimitiveDec(PrimitiveDecContext ctx) {try{ 	  	 internalExitPrimitiveDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterSetOfTypeDef(SetOfTypeDefContext ctx) {try{ 	  	 internalEnterSetOfTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitSetOfTypeDef(SetOfTypeDefContext ctx) {try{ 	  	 internalExitSetOfTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRangeOfInteger(RangeOfIntegerContext ctx) {try{ 	  	 internalEnterRangeOfInteger(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRangeOfInteger(RangeOfIntegerContext ctx) {try{ 	  	 internalExitRangeOfInteger(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterReference(ReferenceContext ctx) {try{ 	  	 internalEnterReference(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitReference(ReferenceContext ctx) {try{ 	  	 internalExitReference(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterVarDef(VarDefContext ctx) {try{ 	  	 internalEnterVarDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitVarDef(VarDefContext ctx) {try{ 	  	 internalExitVarDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterArithmeticExpr(ArithmeticExprContext ctx) {try{ 	  	 internalEnterArithmeticExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitArithmeticExpr(ArithmeticExprContext ctx) {try{ 	  	 internalExitArithmeticExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterWhileStat(WhileStatContext ctx) {try{ 	  	 internalEnterWhileStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitWhileStat(WhileStatContext ctx) {try{ 	  	 internalExitWhileStat(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterAssignment(AssignmentContext ctx) {try{ 	  	 internalEnterAssignment(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitAssignment(AssignmentContext ctx) {try{ 	  	 internalExitAssignment(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRangeOfChar(RangeOfCharContext ctx) {try{ 	  	 internalEnterRangeOfChar(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRangeOfChar(RangeOfCharContext ctx) {try{ 	  	 internalExitRangeOfChar(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterTestCaseDef(TestCaseDefContext ctx) {try{ 	  	 internalEnterTestCaseDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitTestCaseDef(TestCaseDefContext ctx) {try{ 	  	 internalExitTestCaseDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterRelationalExpr(RelationalExprContext ctx) {try{ 	  	 internalEnterRelationalExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitRelationalExpr(RelationalExprContext ctx) {try{ 	  	 internalExitRelationalExpr(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterFloatDec(FloatDecContext ctx) {try{ 	  	 internalEnterFloatDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitFloatDec(FloatDecContext ctx) {try{ 	  	 internalExitFloatDec(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterSetTypeDef(SetTypeDefContext ctx) {try{ 	  	 internalEnterSetTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitSetTypeDef(SetTypeDefContext ctx) {try{ 	  	 internalExitSetTypeDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterPrimitiveConstDef(PrimitiveConstDefContext ctx) {try{ 	  	 internalEnterPrimitiveConstDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitPrimitiveConstDef(PrimitiveConstDefContext ctx) {try{ 	  	 internalExitPrimitiveConstDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterSignatureDef(SignatureDefContext ctx) {try{ 	  	 internalEnterSignatureDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitSignatureDef(SignatureDefContext ctx) {try{ 	  	 internalExitSignatureDef(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterTld(TldContext ctx) {try{ 	  	 internalEnterTld(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitTld(TldContext ctx) {try{ 	  	 internalExitTld(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterTemplateBlock(TemplateBlockContext ctx) {try{ 	  	 internalEnterTemplateBlock(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitTemplateBlock(TemplateBlockContext ctx) {try{ 	  	 internalExitTemplateBlock(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void enterEveryRule(ParserRuleContext ctx) {try{ 	  	 internalEnterEveryRule(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void exitEveryRule(ParserRuleContext ctx) {try{ 	  	 internalExitEveryRule(ctx);}catch(Exception e){handleError(e,ctx);}}
	 @Override public final void visitTerminal(TerminalNode node) {try{ 	  	 internalVisitTerminal(node);}catch(Exception e){handleError(e,node);}}
	 @Override public final void visitErrorNode(ErrorNode node) {try{ 	  	 internalVisitErrorNode(node);}catch(Exception e){handleError(e,node);}}

	 
	    @Override public final void enterImportRestricting(ImportRestrictingContext ctx){  try{ 	  	 internalEnterImportRestricting  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitImportRestricting(ImportRestrictingContext ctx){  try{ 	  	 internalExitImportRestricting  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterImportOtherLanguage(ImportOtherLanguageContext ctx){  try{ 	  	 internalEnterImportOtherLanguage  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitImportOtherLanguage(ImportOtherLanguageContext ctx){  try{ 	  	 internalExitImportOtherLanguage  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterImportByNameDef(ImportByNameDefContext ctx){  try{ 	  	 internalEnterImportByNameDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitImportByNameDef(ImportByNameDefContext ctx){  try{ 	  	 internalExitImportByNameDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterPortDec(PortDecContext ctx){  try{ 	  	 internalEnterPortDec  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitPortDec(PortDecContext ctx){  try{ 	  	 internalExitPortDec  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterPortDef(PortDefContext ctx){  try{ 	  	 internalEnterPortDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitPortDef(PortDefContext ctx){  try{ 	  	 internalExitPortDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterComponentDef(ComponentDefContext ctx){  try{ 	  	 internalEnterComponentDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitComponentDef(ComponentDefContext ctx){  try{ 	  	 internalExitComponentDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterImportFrom(ImportFromContext ctx){  try{ 	  	 internalEnterImportFrom  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitImportFrom(ImportFromContext ctx){  try{ 	  	 internalExitImportFrom  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterPortMessageDec(PortMessageDecContext ctx){  try{ 	  	 internalEnterPortMessageDec  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitPortMessageDec(PortMessageDecContext ctx){  try{ 	  	 internalExitPortMessageDec  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterTimerDef(TimerDefContext ctx){  try{ 	  	 internalEnterTimerDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitTimerDef(TimerDefContext ctx){  try{ 	  	 internalExitTimerDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterImportNormally(ImportNormallyContext ctx){  try{ 	  	 internalEnterImportNormally  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitImportNormally(ImportNormallyContext ctx){  try{ 	  	 internalExitImportNormally  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterImportAllAndSuppressionDef(ImportAllAndSuppressionDefContext ctx){  try{ 	  	 internalEnterImportAllAndSuppressionDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitImportAllAndSuppressionDef(ImportAllAndSuppressionDefContext ctx){  try{ 	  	 internalExitImportAllAndSuppressionDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterImportRecusively(ImportRecusivelyContext ctx){  try{ 	  	 internalEnterImportRecusively  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitImportRecusively(ImportRecusivelyContext ctx){  try{ 	  	 internalExitImportRecusively  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterImportGroupDef(ImportGroupDefContext ctx){  try{ 	  	 internalEnterImportGroupDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitImportGroupDef(ImportGroupDefContext ctx){  try{ 	  	 internalExitImportGroupDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterImportableType(ImportableTypeContext ctx){  try{ 	  	 internalEnterImportableType  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitImportableType(ImportableTypeContext ctx){  try{ 	  	 internalExitImportableType  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void enterImportByKindDef(ImportByKindDefContext ctx){  try{ 	  	 internalEnterImportByKindDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	@Override public final void exitImportByKindDef(ImportByKindDefContext ctx){  try{ 	  	 internalExitImportByKindDef  (ctx); }catch(Exception e){handleError(e,ctx); } }
	 
	 
	protected void internalEnterForStat(ForStatContext ctx) {}
	protected void internalExitForStat(ForStatContext ctx) {}
	protected void internalEnterRecordOfTypeDef(RecordOfTypeDefContext ctx) {}
	protected void internalExitRecordOfTypeDef(RecordOfTypeDefContext ctx) {}
	protected void internalEnterPropertyExpr(PropertyExprContext ctx) {}
	protected void internalExitPropertyExpr(PropertyExprContext ctx) {}
	protected void internalEnterNotOp(NotOpContext ctx) {}
	protected void internalExitNotOp(NotOpContext ctx) {}
	protected void internalEnterLengthExpr(LengthExprContext ctx) {}
	protected void internalExitLengthExpr(LengthExprContext ctx) {}
	protected void internalEnterLabelStat(LabelStatContext ctx) {}
	protected void internalExitLabelStat(LabelStatContext ctx) {}
	protected void internalEnterUnionTypeDef(UnionTypeDefContext ctx) {}
	protected void internalExitUnionTypeDef(UnionTypeDefContext ctx) {}
	protected void internalEnterAttributeDef(AttributeDefContext ctx) {}
	protected void internalExitAttributeDef(AttributeDefContext ctx) {}
	protected void internalEnterArrayExpr(ArrayExprContext ctx) {}
	protected void internalExitArrayExpr(ArrayExprContext ctx) {}
	protected void internalEnterCharDec(CharDecContext ctx) {}
	protected void internalExitCharDec(CharDecContext ctx) {}
	protected void internalEnterUnary(UnaryContext ctx) {}
	protected void internalExitUnary(UnaryContext ctx) {}
	protected void internalEnterFunctionRunsOn(FunctionRunsOnContext ctx) {}
	protected void internalExitFunctionRunsOn(FunctionRunsOnContext ctx) {}
	protected void internalEnterIntDec(IntDecContext ctx) {}
	protected void internalExitIntDec(IntDecContext ctx) {}
	protected void internalEnterTemplateDec(TemplateDecContext ctx) {}
	protected void internalExitTemplateDec(TemplateDecContext ctx) {}
	protected void internalEnterBooleanExpr(BooleanExprContext ctx) {}
	protected void internalExitBooleanExpr(BooleanExprContext ctx) {}
	protected void internalEnterTemplateDef(TemplateDefContext ctx) {}
	protected void internalExitTemplateDef(TemplateDefContext ctx) {}
	protected void internalEnterFunctionActualPara(FunctionActualParaContext ctx) {}
	protected void internalExitFunctionActualPara(FunctionActualParaContext ctx) {}
	protected void internalEnterConstDef(ConstDefContext ctx) {}
	protected void internalExitConstDef(ConstDefContext ctx) {}
	protected void internalEnterBooleanDec(BooleanDecContext ctx) {}
	protected void internalExitBooleanDec(BooleanDecContext ctx) {}
	protected void internalEnterModuleParDef(ModuleParDefContext ctx) {}
	protected void internalExitModuleParDef(ModuleParDefContext ctx) {}
	protected void internalEnterLogicalExpr(LogicalExprContext ctx) {}
	protected void internalExitLogicalExpr(LogicalExprContext ctx) {}
	protected void internalEnterSetDec(SetDecContext ctx) {}
	protected void internalExitSetDec(SetDecContext ctx) {}
	protected void internalEnterRangeOfHexstring(RangeOfHexstringContext ctx) {}
	protected void internalExitRangeOfHexstring(RangeOfHexstringContext ctx) {}
	protected void internalEnterRecordDec(RecordDecContext ctx) {}
	protected void internalExitRecordDec(RecordDecContext ctx) {}
	protected void internalEnterAltStepDef(AltStepDefContext ctx) {}
	protected void internalExitAltStepDef(AltStepDefContext ctx) {}
	protected void internalEnterRangeOfOctetstring(RangeOfOctetstringContext ctx) {}
	protected void internalExitRangeOfOctetstring(RangeOfOctetstringContext ctx) {}
	protected void internalEnterDoWhileStat(DoWhileStatContext ctx) {}
	protected void internalExitDoWhileStat(DoWhileStatContext ctx) {}
	protected void internalEnterFunctionPara(FunctionParaContext ctx) {}
	protected void internalExitFunctionPara(FunctionParaContext ctx) {}
	protected void internalEnterAltStat(AltStatContext ctx) {}
	protected void internalExitAltStat(AltStatContext ctx) {}
	protected void internalEnterAltStatPrimary(AltStatPrimaryContext ctx) {}
	protected void internalExitAltStatPrimary(AltStatPrimaryContext ctx) {}
	protected void internalEnterIfStat(IfStatContext ctx) {}
	protected void internalExitIfStat(IfStatContext ctx) {}
	protected void internalEnterOctetstringDec(OctetstringDecContext ctx) {}
	protected void internalExitOctetstringDec(OctetstringDecContext ctx) {}
	protected void internalEnterBitstringDec(BitstringDecContext ctx) {}
	protected void internalExitBitstringDec(BitstringDecContext ctx) {}
	protected void internalEnterFunctionBuildInCall(FunctionBuildInCallContext ctx) {}
	protected void internalExitFunctionBuildInCall(FunctionBuildInCallContext ctx) {}
	protected void internalEnterStringOprnt(StringOprntContext ctx) {}
	protected void internalExitStringOprnt(StringOprntContext ctx) {}
	protected void internalEnterRelationalExpr2(RelationalExpr2Context ctx) {}
	protected void internalExitRelationalExpr2(RelationalExpr2Context ctx) {}
	protected void internalEnterPrimitiveVarDef(PrimitiveVarDefContext ctx) {}
	protected void internalExitPrimitiveVarDef(PrimitiveVarDefContext ctx) {}
	protected void internalEnterHexstringDec(HexstringDecContext ctx) {}
	protected void internalExitHexstringDec(HexstringDecContext ctx) {}
	protected void internalEnterRelationalExpr3(RelationalExpr3Context ctx) {}
	protected void internalExitRelationalExpr3(RelationalExpr3Context ctx) {}
	protected void internalEnterObjidDec(ObjidDecContext ctx) {}
	protected void internalExitObjidDec(ObjidDecContext ctx) {}
	protected void internalEnterPrimitiveTypeDef(PrimitiveTypeDefContext ctx) {}
	protected void internalExitPrimitiveTypeDef(PrimitiveTypeDefContext ctx) {}
	protected void internalEnterIfPrimaryStat(IfPrimaryStatContext ctx) {}
	protected void internalExitIfPrimaryStat(IfPrimaryStatContext ctx) {}
	protected void internalEnterEnumeratedTypeDef(EnumeratedTypeDefContext ctx) {}
	protected void internalExitEnumeratedTypeDef(EnumeratedTypeDefContext ctx) {}
	protected void internalEnterRangeOfBitstring(RangeOfBitstringContext ctx) {}
	protected void internalExitRangeOfBitstring(RangeOfBitstringContext ctx) {}
	protected void internalEnterGroupDef(GroupDefContext ctx) {}
	protected void internalExitGroupDef(GroupDefContext ctx) {}
	protected void internalEnterFunctionDef(FunctionDefContext ctx) {}
	protected void internalExitFunctionDef(FunctionDefContext ctx) {}
	protected void internalEnterStringExpr(StringExprContext ctx) {}
	protected void internalExitStringExpr(StringExprContext ctx) {}
	protected void internalEnterGotoStat(GotoStatContext ctx) {}
	protected void internalExitGotoStat(GotoStatContext ctx) {}
	protected void internalEnterControlDef(ControlDefContext ctx) {}
	protected void internalExitControlDef(ControlDefContext ctx) {}
	protected void internalEnterCompilationUnit(CompilationUnitContext ctx) {}
	protected void internalExitCompilationUnit(CompilationUnitContext ctx) {}
	protected void internalEnterFunctionCallStat(FunctionCallStatContext ctx) {}
	protected void internalExitFunctionCallStat(FunctionCallStatContext ctx) {}
	protected void internalEnterFunctionDec(FunctionDecContext ctx) {}
	protected void internalExitFunctionDec(FunctionDecContext ctx) {}
	protected void internalEnterState(StateContext ctx) {}
	protected void internalExitState(StateContext ctx) {}
	protected void internalEnterExpr(ExprContext ctx) {}
	protected void internalExitExpr(ExprContext ctx) {}
	protected void internalEnterBitstringExpr(BitstringExprContext ctx) {}
	protected void internalExitBitstringExpr(BitstringExprContext ctx) {}
	protected void internalEnterCharstringDec(CharstringDecContext ctx) {}
	protected void internalExitCharstringDec(CharstringDecContext ctx) {}
	protected void internalEnterNumericOprnt(NumericOprntContext ctx) {}
	protected void internalExitNumericOprnt(NumericOprntContext ctx) {}
	protected void internalEnterReferencePrimary(ReferencePrimaryContext ctx) {}
	protected void internalExitReferencePrimary(ReferencePrimaryContext ctx) {}
	protected void internalEnterTemplatePara(TemplateParaContext ctx) {}
	protected void internalExitTemplatePara(TemplateParaContext ctx) {}
	protected void internalEnterEnumeratedDec(EnumeratedDecContext ctx) {}
	protected void internalExitEnumeratedDec(EnumeratedDecContext ctx) {}
	protected void internalEnterFunctionSystemBuildInCall(FunctionSystemBuildInCallContext ctx) {}
	protected void internalExitFunctionSystemBuildInCall(FunctionSystemBuildInCallContext ctx) {}
	protected void internalEnterTemplateInlineDef(TemplateInlineDefContext ctx) {}
	protected void internalExitTemplateInlineDef(TemplateInlineDefContext ctx) {}
	protected void internalEnterTimerStat(TimerStatContext ctx) {}
	protected void internalExitTimerStat(TimerStatContext ctx) {}
	protected void internalEnterTypeDec(TypeDecContext ctx) {}
	protected void internalExitTypeDec(TypeDecContext ctx) {}
	protected void internalEnterTypeDef(TypeDefContext ctx) {}
	protected void internalExitTypeDef(TypeDefContext ctx) {}
	protected void internalEnterImportDef(ImportDefContext ctx) {}
	protected void internalExitImportDef(ImportDefContext ctx) {}
	protected void internalEnterFunctionParaPrimary(FunctionParaPrimaryContext ctx) {}
	protected void internalExitFunctionParaPrimary(FunctionParaPrimaryContext ctx) {}
	protected void internalEnterRangeOfCharstring(RangeOfCharstringContext ctx) {}
	protected void internalExitRangeOfCharstring(RangeOfCharstringContext ctx) {}
	protected void internalEnterArrayDec(ArrayDecContext ctx) {}
	protected void internalExitArrayDec(ArrayDecContext ctx) {}
	protected void internalEnterModule(ModuleContext ctx) {}
	protected void internalExitModule(ModuleContext ctx) {}
	protected void internalEnterLiterals(LiteralsContext ctx) {}
	protected void internalExitLiterals(LiteralsContext ctx) {}
	protected void internalEnterRelationalOprnt(RelationalOprntContext ctx) {}
	protected void internalExitRelationalOprnt(RelationalOprntContext ctx) {}
	protected void internalEnterBitstringOprnt(BitstringOprntContext ctx) {}
	protected void internalExitBitstringOprnt(BitstringOprntContext ctx) {}
	protected void internalEnterBooleanOprnt(BooleanOprntContext ctx) {}
	protected void internalExitBooleanOprnt(BooleanOprntContext ctx) {}
	protected void internalEnterReturnExpr(ReturnExprContext ctx) {}
	protected void internalExitReturnExpr(ReturnExprContext ctx) {}
	protected void internalEnterRecordTypeDef(RecordTypeDefContext ctx) {}
	protected void internalExitRecordTypeDef(RecordTypeDefContext ctx) {}
	protected void internalEnterLogicalOprnt(LogicalOprntContext ctx) {}
	protected void internalExitLogicalOprnt(LogicalOprntContext ctx) {}
	protected void internalEnterElseStat(ElseStatContext ctx) {}
	protected void internalExitElseStat(ElseStatContext ctx) {}
	protected void internalEnterFunctionBody(FunctionBodyContext ctx) {}
	protected void internalExitFunctionBody(FunctionBodyContext ctx) {}
	protected void internalEnterPrimitiveDec(PrimitiveDecContext ctx) {}
	protected void internalExitPrimitiveDec(PrimitiveDecContext ctx) {}
	protected void internalEnterSetOfTypeDef(SetOfTypeDefContext ctx) {}
	protected void internalExitSetOfTypeDef(SetOfTypeDefContext ctx) {}
	protected void internalEnterRangeOfInteger(RangeOfIntegerContext ctx) {}
	protected void internalExitRangeOfInteger(RangeOfIntegerContext ctx) {}
	protected void internalEnterReference(ReferenceContext ctx) {}
	protected void internalExitReference(ReferenceContext ctx) {}
	protected void internalEnterVarDef(VarDefContext ctx) {}
	protected void internalExitVarDef(VarDefContext ctx) {}
	protected void internalEnterArithmeticExpr(ArithmeticExprContext ctx) {}
	protected void internalExitArithmeticExpr(ArithmeticExprContext ctx) {}
	protected void internalEnterWhileStat(WhileStatContext ctx) {}
	protected void internalExitWhileStat(WhileStatContext ctx) {}
	protected void internalEnterAssignment(AssignmentContext ctx) {}
	protected void internalExitAssignment(AssignmentContext ctx) {}
	protected void internalEnterRangeOfChar(RangeOfCharContext ctx) {}
	protected void internalExitRangeOfChar(RangeOfCharContext ctx) {}
	protected void internalEnterTestCaseDef(TestCaseDefContext ctx) {}
	protected void internalExitTestCaseDef(TestCaseDefContext ctx) {}
	protected void internalEnterRelationalExpr(RelationalExprContext ctx) {}
	protected void internalExitRelationalExpr(RelationalExprContext ctx) {}
	protected void internalEnterFloatDec(FloatDecContext ctx) {}
	protected void internalExitFloatDec(FloatDecContext ctx) {}
	protected void internalEnterSetTypeDef(SetTypeDefContext ctx) {}
	protected void internalExitSetTypeDef(SetTypeDefContext ctx) {}
	protected void internalEnterPrimitiveConstDef(PrimitiveConstDefContext ctx) {}
	protected void internalExitPrimitiveConstDef(PrimitiveConstDefContext ctx) {}
	protected void internalEnterSignatureDef(SignatureDefContext ctx) {}
	protected void internalExitSignatureDef(SignatureDefContext ctx) {}
	protected void internalEnterTld(TldContext ctx) {}
	protected void internalExitTld(TldContext ctx) {}
	protected void internalEnterTemplateBlock(TemplateBlockContext ctx) {}
	protected void internalExitTemplateBlock(TemplateBlockContext ctx) {}
	protected void internalEnterEveryRule(ParserRuleContext ctx) {}
	protected void internalExitEveryRule(ParserRuleContext ctx) {}
	protected void internalVisitErrorNode(ErrorNode node) {}
	protected void internalExitNot4bOp(ParserRuleContext ctx) {}
	protected void interalEnterNot4bOp(Not4bOpContext ctx){}
	protected void internalVisitTerminal(TerminalNode node) {}
	protected void internalEnterImportRestricting(ImportRestrictingContext ctx){}
	protected void internalExitImportRestricting(ImportRestrictingContext ctx){}
	protected void internalEnterImportOtherLanguage(ImportOtherLanguageContext ctx){}
	protected void internalExitImportOtherLanguage(ImportOtherLanguageContext ctx){}
	protected void internalEnterImportByNameDef(ImportByNameDefContext ctx){}
	protected void internalExitImportByNameDef(ImportByNameDefContext ctx){}
	protected void internalEnterPortDec(PortDecContext ctx){}
	protected void internalExitPortDec(PortDecContext ctx){}
	protected void internalEnterPortDef(PortDefContext ctx){}
	protected void internalExitPortDef(PortDefContext ctx){}
	protected void internalEnterComponentDef(ComponentDefContext ctx){}
	protected void internalExitComponentDef(ComponentDefContext ctx){}
	protected void internalEnterImportFrom(ImportFromContext ctx){}
	protected void internalExitImportFrom(ImportFromContext ctx){}
	protected void internalEnterPortMessageDec(PortMessageDecContext ctx){}
	protected void internalExitPortMessageDec(PortMessageDecContext ctx){}
	protected void internalEnterTimerDef(TimerDefContext ctx){}
	protected void internalExitTimerDef(TimerDefContext ctx){}
	protected void internalEnterImportNormally(ImportNormallyContext ctx){}
	protected void internalExitImportNormally(ImportNormallyContext ctx){}
	protected void internalEnterImportAllAndSuppressionDef(ImportAllAndSuppressionDefContext ctx){}
	protected void internalExitImportAllAndSuppressionDef(ImportAllAndSuppressionDefContext ctx){}
	protected void internalEnterImportRecusively(ImportRecusivelyContext ctx){}
	protected void internalExitImportRecusively(ImportRecusivelyContext ctx){}
	protected void internalEnterImportGroupDef(ImportGroupDefContext ctx){}
	protected void internalExitImportGroupDef(ImportGroupDefContext ctx){}
	protected void internalEnterImportableType(ImportableTypeContext ctx){}
	protected void internalExitImportableType(ImportableTypeContext ctx){}
	protected void internalEnterImportByKindDef(ImportByKindDefContext ctx){}
	protected void internalExitImportByKindDef(ImportByKindDefContext ctx){}
}
