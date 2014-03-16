package org.quantumlabs.kitt.core.util;

public final class SyntaxConstants {
	/**
	 * TTCN document partition comment rules
	 * */
	public static final String PARTITION_SINGLE_LINE_COMMENT = "//";
	public static final String PARTITION_MULTIL_LINE_BEGIN = "/*";
	public static final String PARTITION_MULTIL_LINE_END = "*/";

	/**
	 * TTCN document partition basic type
	 * */
	public static final String PARTITION_BASIC_CHAR_BEGIN = "\"";
	public static final String PARTITION_BASIC_CHAR_END = "\"";

	/**
	 * TTCN document partition basic string rules
	 * */
	public static final String PARTITION_BASIC_BIT_STRING_BEGIN = "'";
	public static final String PARTITION_BASIC_BIT_STRING_END = "'B";
	public static final String PARTITION_BASIC_HEX_STRING_BEGIN = "'";
	public static final String PARTITION_BASIC_HEX_STRING_END = "'H";
	public static final String PARTITION_BASIC_OCT_STRING_BEGIN = "'";
	public static final String PARTITION_BASIC_OCT_STRING_END = "'O";
	public static final String PARTITION_BASIC_CHAR_STRING_BEGIN = "\"";
	public static final String PARTITION_BASIC_CHAR_STRING_END = "\"";

	/**
	 * TTCN document semantic block partition rules.
	 * */
	public static final String PARTITION_SEMANTIC_BLOCK_BEGIN = "{";
	public static final String PARTITION_SEMANTIC_BLOCK_END = "}";

	/**
	 * Semantic terminator;
	 * */
	public static final String TERMINATOR = ";";

	/**
	 * Semantic block key words
	 * */
	public static final String TYPE = "type";
	/**
	 * structured type key words
	 * */
	public static final String RECORD = "record";
	public static final String SET = "set";
	public static final String ENUMERATED = "enumerated";
	public static final String UNION = "union";

	/**
	 * Special data type key words
	 * */
	public static final String ANYTYPE = "anytype";

	/**
	 * Special configuration type key words
	 * */
	public static final String ADDRESS = "address";
	public static final String PORT = "port";
	public static final String COMPONET = "component";

	/**
	 * Special default type key words
	 * */
	public static final String DEAULT = "default";
	/**
	 * Basic string type key words
	 * */
	public static final String BITSTRING = "bitstring";
	public static final String HEXSTRING = "hexstring";
	public static final String OCTETSTRING = "octetstring";
	public static final String CHARSTRING = "charstring";

	/**
	 * Simple basic type key words
	 * */
	public static final String INTEGER = "integer";
	public static final String CHAR = "char";
	public static final String FLOAT = "float";
	public static final String BOOLEAN = "boolean";
	public static final String OBJID = "objid";
	public static final String VERDICTTYPE = "verdicttype";

	/**
	 * Other key words
	 * */
	public static final String UNIVERSAL = "universal";
	public static final String OF = "of";

	/**
	 * Rule pattern
	 * */
	public static final String[] PARTITION_TYPE_RECORD_RULE_PATTERN = new String[] {
			SyntaxConstants.TYPE, SyntaxConstants.RECORD,
			SackConstant.REGULAR_PARTTEN_ANY_ONE,
			SyntaxConstants.PARTITION_SEMANTIC_BLOCK_BEGIN,
			SyntaxConstants.PARTITION_SEMANTIC_BLOCK_END };
	public static final String[] PARTITION_TYPE_SET_RULE_PATTERN = new String[] {
			SyntaxConstants.TYPE, SyntaxConstants.SET,
			SackConstant.REGULAR_PARTTEN_ANY_ONE,
			SyntaxConstants.PARTITION_SEMANTIC_BLOCK_BEGIN,
			SyntaxConstants.PARTITION_SEMANTIC_BLOCK_END };
	public static final String[] PARTITION_TYPE_ENUMERATED_RULE_PATTERN = new String[] {
			SyntaxConstants.TYPE, SyntaxConstants.ENUMERATED,
			SackConstant.REGULAR_PARTTEN_ANY_ONE,
			SyntaxConstants.PARTITION_SEMANTIC_BLOCK_BEGIN,
			SyntaxConstants.PARTITION_SEMANTIC_BLOCK_END };
	public static final String[] PARTITION_TYPE_UNION_RULE_PATTERN = new String[] {
			SyntaxConstants.TYPE, SyntaxConstants.UNION,
			SackConstant.REGULAR_PARTTEN_ANY_ONE,
			SyntaxConstants.PARTITION_SEMANTIC_BLOCK_BEGIN,
			SyntaxConstants.PARTITION_SEMANTIC_BLOCK_END };
}
