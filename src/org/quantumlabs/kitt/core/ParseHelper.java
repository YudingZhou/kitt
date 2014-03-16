package org.quantumlabs.kitt.core;

import org.quantumlabs.kitt.core.parse.TesParser.TypeDecContext;

public class ParseHelper {
	/**
	 * <pre>
	 * typeDec
	 * 	:primitiveDec
	 * 	|ID ( DOT ID )* ID user defined type declaration
	 * 	|arrayDec
	 * 	TODO : record, set, user defined type
	 * 	;
	 * </pre>
	 * 
	 * @return An array of [typeName, argumentId]
	 * */
	public static String[] parseTypeDec(TypeDecContext typeDec) {
		String[] typeAndName = new String[2];
		if (typeDec.primitiveDec() != null) {
			// typeDec :: primitiveDec :: integerDec|charDec|... :: LITERAL ID
			typeAndName[0] = typeDec.primitiveDec().children.get(0).getChild(1).getText();
			typeAndName[1] = typeDec.primitiveDec().children.get(0).getChild(0).getText();
		} else if (typeDec.arrayDec() != null) {
			typeAndName[0] = typeDec.arrayDec().ID().getText();
			typeAndName[1] = typeDec.arrayDec().arrayExpr().getText();
		} else if (typeDec.ID() != null && typeDec.ID().size() > 0) {
			int idCount = typeDec.ID().size();
			typeAndName[0] = typeDec.ID().get(idCount - 2).getText();
			typeAndName[1] = typeDec.ID().get(idCount - 1).getText();
		} else {
			// TODO : TO BE CONTINUE : record, set, user defined type etc.
		}
		return typeAndName;
	}
}
