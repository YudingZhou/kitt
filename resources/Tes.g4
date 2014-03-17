grammar Tes;


@header{
package org.quantumlabs.kitt.core.parse;
}


WS
	:[ \n\r\t]+ -> skip
	;
COMMENT
    :   ( '//' ~[\r\n]* '\r'? '\n'
        | '/*' .*? '*/'
        ) -> skip 
    ;
BRACE_L
	:'{'
	;
BRACE_R
	:'}'
	;
PARENTHESE_L
	:'('
	;
PARENTHESE_R
	:')'
	;
BRACKET_L
	:'['
	;
BRACKET_R
	:']'
	;
/*
MORE_THAN
	:'>'
	;
LESS_THAN	
	:'<'
	;
MORE_THAN_EQUAL
	:'=>'
	;
LESS_THAN_EQUAL
	:'<='
	;
MORE_THAN_STRING
	:'@>'
	;
LESS_THAN_STRING
	:'<@'
	;
MORE_THAN_MORE
	:'>>'
	;
LESS_THAN_LESS
	:'<<'
	;
*/
STRING_OPRTR
	:'&'
	|'>>'
	|'<<'
	|'@>'
	|'@>'
	;
BIT_OPRTR
	:'not4b'
	|'and4b'
	|'xor4b'
	|'or4b'
	;
LOGICAL_OPRTR
	:'not'
	|'and'
	|'or'
	|'xor'
	;
RELATIONAL_OPRTR
	:EQUALTO
	|'>'
	|'<'
	|'!='
	|'>='
	|'<='
	;
ARITHMETIC_OPRTR
	:'+'
	|'-'
	|'*'
	|'/'
	|'rem'
	|'mod'
	;
ANYONE
	:'?'
	;
/*
NOT
	:'not'
	;
AND
	:'and'
	;
XOR
	:'xor'
	;
OR
	:'or'
	;
*/
OMIT
	:'omit'
	;
UNSPECIFIED
	:'unspecified'
	;
COLON
	:':'
	;
/*
MOD
	:'mod'
	;
REM
	:'rem'
	;
N4B
	:'not4b'
	;
A4B
	:'and4b'
	;
X4B
	:'xor4b'
	;
O4B
	:'or4b'
	;
CAT
	:'&'
	;
*/
POINT
	:'->'
	;
/**expression construct*/
notOp
	:'not' BOOLEAN_LITERAL
	;
not4bOp
	:'not4b' BITSTRING_LITERAL
	;
unary
	:notOp
	|not4bOp
	;
numericOprnt
	:INTEGER_LITERAL
	|FLOAT_LITERAL
	|reference
	|functionCallStat
	;
arithmeticExpr
	:numericOprnt
	|arithmeticExpr ARITHMETIC_OPRTR arithmeticExpr
	|PARENTHESE_L arithmeticExpr PARENTHESE_R
	;
relationalOprnt
	:reference
	|'true'
	|'false'
	|functionCallStat
	;
relationalExpr3
	:stringExpr
	|relationalExpr3 RELATIONAL_OPRTR relationalExpr3
	;
relationalExpr2 
	:arithmeticExpr 
	|relationalExpr2 RELATIONAL_OPRTR relationalExpr2
	;
/**
*Notice!In case, only Two or more than two arithmeticExpr(or stringExpr) can be operated by relation operator (>,<,==,!=), relationExpr2/3 stands for that. This
*can avoid that relationalExpr derives to a single arithmeticExpr or stringExpr
*	relationalExpr
*		-relationExpr2
*			-- arithmeticExpr RELATIONAL_OPRTR arithmeticExpr 
*				eg.  (a+2) > (3 rem 1)
*which is right

*   relationExpr
*		-relationOprnt
*			-- arithmeticExpr
*				eg. (1+1) 
*which is totally wrong!!! :(
*
*/
relationalExpr
	:relationalOprnt
	|relationalExpr RELATIONAL_OPRTR relationalExpr
	|PARENTHESE_L relationalExpr PARENTHESE_R
	|relationalExpr2
	|relationalExpr3
	;
logicalOprnt
	:reference
	|'not' logicalOprnt
	|'true'
	|'false'
	|functionCallStat
	;
logicalExpr
	:logicalOprnt
	|logicalExpr LOGICAL_OPRTR logicalExpr
	|PARENTHESE_L logicalExpr PARENTHESE_R
	;
bitstringOprnt
	:BITSTRING_LITERAL
	|reference
	|'not4b' bitstringOprnt
	|functionCallStat
	;
bitstringExpr
	:bitstringOprnt
	|bitstringExpr BIT_OPRTR bitstringExpr
	|PARENTHESE_L bitstringExpr PARENTHESE_R
	;
stringOprnt
	:CHARSTRING_LITERAL
	|BITSTRING_LITERAL
	|HEXSTRING_LITERAL
	|OCTETSTRING_LITERAL
	|reference
	|functionCallStat
	;
stringExpr
	:stringOprnt
	|stringExpr STRING_OPRTR stringExpr
	|PARENTHESE_L stringExpr PARENTHESE_R 
	;
expr
	:PARENTHESE_L ( functionCallStat | stringExpr | bitstringExpr | logicalExpr | relationalExpr | arithmeticExpr ) PARENTHESE_R 
	|functionCallStat | stringExpr | bitstringExpr | logicalExpr | relationalExpr | arithmeticExpr
	;
lengthExpr
	:'length' rangeOfInteger 
	;
propertyExpr
	:'self' COLON ( reference | functionBuildInCall )
	|ID COLON ( reference | functionBuildInCall )
	;
fragment
LETER_LITERAL
	:'A'..'Z'
	|'a'..'z'
	;
fragment
DIGIT_LITERAL
	:[0-9] /*{System.out.println("DIGIT_LITERAL " + _tokenStartCharIndex);}*/
	;
STATEND
	:';'
	;
EQUALTO
	:'=='
	;
NOTEQUALTO
	:'!='
	;
DOT
	:'.'
	;
PRIMITIVE
	:'integer'
	|'universal'* 'char'
	|'float'
	|'boolean'
	|'objid'
	|'verdicttype'
	;
PRIMITIVESTRING
	:'bitstring'
	|'hexstring'
	|'octetstring'
	|'universal'* 'charstring'
	;	
CHAR_LITERAL
	:'"'LETER_LITERAL'"'
	;	
INTEGER_LITERAL
	:DIGIT_LITERAL+ /*{System.out.println("integer literal : " + _tokenStartCharIndex);}*/
	/*|NEGTIVE_INTEGER_LITERAL*/
	;	
FLOAT_LITERAL
	:(DIGIT_LITERAL'.'DIGIT_LITERAL+ 
	|'-'DIGIT_LITERAL'.'DIGIT_LITERAL+ )/*{System.out.println("integer literal : " + _tokenStartCharIndex);}*/
	;

BOOLEAN_LITERAL
	:'true'
	|'false'
	;
VERDICTTYPE_LITERAL
	:'none'
	|'error'
	|'fail'
	|'inconc'
	|'pass'
	;
BITSTRING_LITERAL	
	:'\''[01]+'\'B'
	;
HEXSTRING_LITERAL	
	:'\''[0-9A-F]*'\'B'
	;	
OCTETSTRING_LITERAL
	:'\''[0-9A-F]*'\'O'/* {System.out.println("OCT : " + _tokenStartCharIndex);}*/
	;
CHARSTRING_LITERAL
	:( '"' ( ~('\n'|'\r') )*? '"'
	|'oct2str' PARENTHESE_L CHARSTRING_LITERAL PARENTHESE_R)/*{System.out.println("CHARSTRING matched " + _tokenStartCharIndex);}*/
	;
literals
	:INTEGER_LITERAL
	|CHAR_LITERAL
	|BOOLEAN_LITERAL
	|FLOAT_LITERAL
	|VERDICTTYPE_LITERAL
	|BITSTRING_LITERAL
	|HEXSTRING_LITERAL
	|OCTETSTRING_LITERAL
	|CHARSTRING_LITERAL
	;
	/*Escape a sequential double quotes. E.g. "Hello ""Junior"" X!", effected resuLESS_THAN will be /Hello "Junior" X!/*/
PRIMITIVE_TYPE_LITERAL
	:'integer'
	|'float'
	|'boolean'
	|'anytype'
	|'verdicttype'
	|'objid'
	|'char'
	|'charstring'
	|'hexstring'
	|'octetstring'
	|'bitstring'
	;	
ID 
	:LETER_LITERAL+ 
		(LETER_LITERAL
		|DIGIT_LITERAL
		|'_'
		)* /*{System.out.printf("\nID matched [%s,%s]",_tokenStartCharIndex,_tokenStartLine);}*/
	;
/*TYPE_ID
	:ID
	#TODO more kind of type ID
	;*/
ASSIGN
	:':='
	;
intDec
	:'integer' ID
	;
floatDec
	:'float' ID
	;
booleanDec
	:'boolean' ID
	;
bitstringDec
	:'bitstring' ID
	;
hexstringDec
	:'hexstring' ID
	;
octetstringDec
	:'octetstring' ID
	;
charDec
	:'universal'? 'char' ID
	;
charstringDec
	:'universal'? 'charstring' ID
	;
objidDec
	:'objid' ID
	;
recordDec
	:'record' ID
	;
setDec
	:'set' ID
	;
enumeratedDec
	:'enumerated' ID
	;
primitiveDec
	:intDec
	|floatDec
	|booleanDec
	|bitstringDec
	|hexstringDec
	|octetstringDec
	|charDec
	|charstringDec
	|objidDec
	;
arrayDec
	:ID arrayExpr
	;
typeDec
	:primitiveDec
	|ID ( DOT ID )* ID /*user defined type declaration*/
	|arrayDec
	/*TODO : record, set, user defined type*/
	;
templateDec
	:'template' typeDec
	;
primitiveVarDef
	:'var' 
		(
			(intDec ( ASSIGN ( INTEGER_LITERAL | arithmeticExpr ) )? )
			|(floatDec ( ASSIGN ( FLOAT_LITERAL | arithmeticExpr )  )? )
			|(booleanDec ( ASSIGN ( BOOLEAN_LITERAL | booleanExpr ) )? )
			|(bitstringDec ( ASSIGN ( BITSTRING_LITERAL | bitstringExpr ) )? )
			|(hexstringDec ( ASSIGN HEXSTRING_LITERAL )? )
			|(octetstringDec ( ASSIGN OCTETSTRING_LITERAL )? )
			|(charDec ASSIGN ( CHAR_LITERAL )? )
			|(charstringDec ( ASSIGN ( CHARSTRING_LITERAL | stringExpr) )? )
		) 
	;
varDef
	:primitiveVarDef
	|'var' typeDec( ASSIGN ( literals | expr | reference  ) )?
	|'var' templateDec
	;
primitiveConstDef
	:'const' 
		(intDec ASSIGN INTEGER_LITERAL
		|floatDec ASSIGN FLOAT_LITERAL
		|booleanDec ASSIGN BOOLEAN_LITERAL
		|bitstringDec ASSIGN BITSTRING_LITERAL
		|hexstringDec ASSIGN HEXSTRING_LITERAL
		|octetstringDec ASSIGN OCTETSTRING_LITERAL
		|charDec ASSIGN CHAR_LITERAL
		|charstringDec ASSIGN CHARSTRING_LITERAL
		) 
	|'external' 'const'
		(intDec
		|floatDec
		|booleanDec
		|bitstringDec
		|hexstringDec
		|octetstringDec
		|charDec
		|charstringDec
		) 
	;
constDef
	:primitiveConstDef
	;
rangeOfInteger
	:PARENTHESE_L INTEGER_LITERAL '..' INTEGER_LITERAL PARENTHESE_R
	|PARENTHESE_L INTEGER_LITERAL (',' INTEGER_LITERAL)* PARENTHESE_R
	;
rangeOfBitstring
	:PARENTHESE_L BITSTRING_LITERAL '..' BITSTRING_LITERAL PARENTHESE_R
	|PARENTHESE_L BITSTRING_LITERAL (',' BITSTRING_LITERAL )* PARENTHESE_R
	;
rangeOfHexstring
	:PARENTHESE_L HEXSTRING_LITERAL '..' HEXSTRING_LITERAL PARENTHESE_R
	|PARENTHESE_L HEXSTRING_LITERAL (',' HEXSTRING_LITERAL )* PARENTHESE_R
	;
rangeOfOctetstring
	:PARENTHESE_L OCTETSTRING_LITERAL '..' OCTETSTRING_LITERAL PARENTHESE_R
	|PARENTHESE_L OCTETSTRING_LITERAL (',' OCTETSTRING_LITERAL )* PARENTHESE_R
	;
rangeOfChar
	:PARENTHESE_L CHAR_LITERAL '..' CHAR_LITERAL PARENTHESE_R
	|PARENTHESE_L CHAR_LITERAL (',' CHAR_LITERAL )* PARENTHESE_R
	;
rangeOfCharstring
	:PARENTHESE_L CHARSTRING_LITERAL '..' CHARSTRING_LITERAL PARENTHESE_R
	|PARENTHESE_L CHARSTRING_LITERAL (',' CHARSTRING_LITERAL )* PARENTHESE_R
	;
primitiveTypeDef
	:'type'
		(intDec rangeOfInteger
		|bitstringDec rangeOfBitstring
		|hexstringDec rangeOfHexstring
		|octetstringDec rangeOfOctetstring
		|charDec rangeOfChar
		|charstringDec rangeOfCharstring
		) 
	;
recordTypeDef
	:'type' recordDec BRACE_L (|typeDec (',' typeDec 'optional')*) BRACE_R 
	;
setTypeDef
	:'type' setDec BRACE_L (|typeDec (',' typeDec 'optional')*) BRACE_R 
	;
recordOfTypeDef
	:'type' 'record' lengthExpr? 'of' typeDec
	/*TODO : other type for record of*/
	;
setOfTypeDef
	:'type' 'set' lengthExpr? 'of' typeDec 
	/*TODO : other type for record of*/
	;
unionTypeDef
	:'type' 'union' typeDec BRACE_L typeDec (',' typeDec 'optional')* BRACE_R 
	/*TODO :other type for union def*/
	;
enumeratedTypeDef
	:'type' enumeratedDec BRACE_L 
		(ID (',' ID)*
		|ID PARENTHESE_L INTEGER_LITERAL PARENTHESE_R (',' ID PARENTHESE_L INTEGER_LITERAL PARENTHESE_R)* 
		) BRACE_R 
	;
signatureDef
	:'signature' ID BRACE_L .*? /*TODO*/ BRACE_R
	;
moduleParDef
	:'modulepar' primitiveDec /*TODO*/
	;
templatePara
	: 'in'? (typeDec|templateDec) 'optional'? 
	;
templateBlock
	:BRACE_L (	( ( ID ASSIGN )? (ID
								 |INTEGER_LITERAL
								 |BOOLEAN_LITERAL
								 |BITSTRING_LITERAL
								 |HEXSTRING_LITERAL
								 |OCTETSTRING_LITERAL
								 |CHAR_LITERAL
								 |CHARSTRING_LITERAL
								 |ANYONE
								 |UNSPECIFIED
								 |OMIT
								 |functionBuildInCall
								 |'*'
								 )
					( ',' 	( ID ASSIGN )? (ID
									       |INTEGER_LITERAL
										   |BOOLEAN_LITERAL
										   |BITSTRING_LITERAL
										   |HEXSTRING_LITERAL
										   |OCTETSTRING_LITERAL
										   |CHAR_LITERAL
										   |CHARSTRING_LITERAL
										   |ANYONE
										   |UNSPECIFIED
										   |OMIT
										   |functionBuildInCall
										   |'*') )*
				)* BRACE_R )
	/*TODO*/
	;
templateInlineDef
	:ID COLON templateBlock 
	;
templateDef 
	:templateDec 
		( PARENTHESE_L templatePara ( ',' templatePara )* PARENTHESE_R )? 
			( ASSIGN 
				(ID|templateBlock)  )?
	;
functionParaPrimary
	:( 'in' | 'out' | 'inout')? ( typeDec | templateDec ) 'optional'?
	|
	;
functionPara
	: functionParaPrimary ( ',' functionParaPrimary )*
	;
functionBody
	: state+ /*TODO*/
	;
returnExpr
	:'return' reference
	;
functionDec
	:'function' ID 
	;
functionRunsOn
	:'runs' 'on' ID ( DOT ID )*/*TYPE ID*/
	;
functionDef
	: functionDec ( PARENTHESE_L functionPara PARENTHESE_R ) functionRunsOn? BRACE_L functionBody BRACE_R
	| functionDec ( PARENTHESE_L functionPara PARENTHESE_R ) functionRunsOn? returnExpr BRACE_L functionBody BRACE_R
	| 'external' functionDec /*TODO*/
	;
altStepDef
	: 'altstep' ID PARENTHESE_L PARENTHESE_R 'runs' 'on' ID BRACE_L state+ BRACE_R/*TODO*/
	;
altStatPrimary
	:'[' ']' state BRACE_L state+ BRACE_R
	|state BRACE_L state+ BRACE_R
	;
altStat
	:'alt' BRACE_L altStatPrimary+ BRACE_R /*TODO*/
	;
testCaseDef
	: 'testcase' ID PARENTHESE_L PARENTHESE_R 'runs' 'on' ID 'system' ID BRACE_L state+ BRACE_R/*TODO*/
	;
	
/*******************************************************************
		@START : "import definition"
		@See : John.Wiley's "An introduction to TTCN3" chapter7.3
********************************************************************/
importableType
	:'testcase'
	|'function'
	|'altstep'
	|'template'
	|'type'
	|'signature'
	|'const'
	|'modulepar' 
	;
importByKindDef
	:importableType 'all' ( 'except' ID ( ',' ID)+ )?
	;
importByNameDef
	:importableType ID ( ',' ID )+
	;
importGroupDef
	:'group' ID ( DOT ID)+
	|'group' 'all' ( 'except' ID ( DOT ID)+ ( ',' ID ( DOT ID)+ )+ )?
	;
importRestricting
	:( importByKindDef STATEND )+
	|( importByNameDef STATEND )+
	|( importGroupDef STATEND)+
	;
importNormally
	:importFrom BRACE_L importRestricting BRACE_R
	;
importSuppressionDef
	:importFrom 'all' 'except' BRACE_L importRestricting BRACE_R
	;
importRecusively
	:importFrom 'recusive' BRACE_L importRestricting BRACE_R
	;
importOtherLanguage
	:importFrom 'language' CHARSTRING_LITERAL 'all'
	;
importFrom
	:'import' 'from' ID
	;
importDef
	:importOtherLanguage STATEND
	|importRecusively
	|importNormally
	|importSuppressionDef
	;
/*******************************************************************
				@END : "import definition"
********************************************************************/
attributeDef
	:'with' BRACE_L .*? BRACE_R
	;
groupDef
	:'group' ID BRACE_L .*? BRACE_R
	;
controlDef
	:'control' BRACE_L state+ BRACE_R
	;
typeDef
	:primitiveTypeDef
	|recordTypeDef
	|setTypeDef
	|recordOfTypeDef
	|setOfTypeDef
	|unionTypeDef
	|enumeratedTypeDef
	;
functionActualPara
	:( expr | reference | functionCallStat | OMIT | ANYONE | '*' |propertyExpr | templateBlock | ) ( ',' ( expr | reference | functionCallStat | OMIT | ANYONE | '*' | propertyExpr | templateBlock | ))*
	;
assignment /*for local variable assignment*/
	:reference ASSIGN ( expr | reference | OMIT | ANYONE | '*' | templateBlock) /*reference assignment*/
	|typeDec ASSIGN ( expr | reference | OMIT | ANYONE | '*' | templateBlock )/*directly assignment when declaring*/
	;
reference
	:referencePrimary
	|reference DOT reference 
	;
referencePrimary
	:ID
	|arrayExpr
	;
functionSystemBuildInCall
	:'log' PARENTHESE_L ( CHARSTRING_LITERAL | reference | functionCallStat ) (',' ( CHARSTRING_LITERAL | reference | functionCallStat) )* PARENTHESE_R 
	|'connect' PARENTHESE_L functionActualPara PARENTHESE_R
	;
functionBuildInCall
	:functionSystemBuildInCall
	|ID PARENTHESE_L functionActualPara PARENTHESE_R
	;
functionCallStat
	:reference DOT functionBuildInCall
	|functionBuildInCall
	;
arrayExpr
	:ID '[' INTEGER_LITERAL | ']'
	|ID '[' reference ']'
	|ID '[' arithmeticExpr  ']'
	;
gotoStat
	:'goto' ID
	;
labelStat
	:'label' ID
	;
timerStat
	:'all'? 'timer' DOT 'stop'
	;
state
	:ifStat STATEND?
	|forStat 
	|whileStat
	|doWhileStat
	|altStat
	|varDef STATEND
	|assignment STATEND
	|functionCallStat STATEND
	|returnExpr STATEND
	|gotoStat STATEND
	|labelStat STATEND
	|timerStat
	; 
booleanOprnt
	:relationalExpr
	|logicalExpr
	;
booleanExpr
	:booleanOprnt
	|booleanExpr ( 'and' |'or' | 'xor'  ) booleanExpr
	|'not' booleanExpr
	;
ifPrimaryStat
	:'if' PARENTHESE_L booleanExpr PARENTHESE_R 
		BRACE_L ( state+ | ) BRACE_R
	;
elseStat
	:'else' BRACE_L ( state+ | ) BRACE_R
	;
ifStat
	:ifPrimaryStat
	|ifPrimaryStat elseStat
	|ifPrimaryStat ( 'else' ifPrimaryStat )+
	|ifPrimaryStat ( 'else' ifPrimaryStat )+ elseStat
	;
forStat
	:'for' PARENTHESE_L ( varDef | reference ASSIGN ( expr | reference ) ) 
		STATEND booleanExpr 
			STATEND ID ASSIGN ( expr | ID )  PARENTHESE_R 
		BRACE_L ( state+ | )BRACE_R
	;
whileStat
	:'while' PARENTHESE_L booleanExpr PARENTHESE_R
		BRACE_L ( state+ | ) BRACE_R
	;
doWhileStat
	:'do' BRACE_L ( state+ | ) BRACE_R
		'while' PARENTHESE_L booleanExpr PARENTHESE_R
	;
tld
	:typeDef
	|constDef
	|signatureDef /*TODO*/
	|moduleParDef /*TODO*/
	|controlDef
	|attributeDef /*TODO*/
	|groupDef /*TODO*/
	|importDef
	|templateDef
	|functionDef
	|altStepDef
	|testCaseDef
	;
module
	:'module' ID BRACE_L (tld STATEND?)*  BRACE_R 
	;
compilationUnit
	: module
	;
	
/*****************************************************************************************************************
							Following part is concurrent TTCN related definations.
							@See John Wiley's "An introduction to TTCN3"
@START
*****************************************************************************************************************/

portMessageDec
	: ( 'in' | 'out' | 'inout') ID ( ',' ID)+
	| ( 'in' | 'out' | 'inout') 'all'
	;
portDef
	:'type' 'port' ID 'message' PARENTHESE_R ( portMessageDec STATEND )+ PARENTHESE_L
	;
portDec
	:'port' ID ID
	;
componentDef
	:'type' 'component' ID PARENTHESE_R ( ( portDec | varDef | constDef | timerDef ) STATEND )+ PARENTHESE_L
	;
timerDef
	:'timer' ID ASSIGN FLOAT_LITERAL
	;
/*@END*/
