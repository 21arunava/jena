/* Generated By:JJTree&JavaCC: Do not edit this line. RDQLParserConstants.java */
/*
 * (c) Copyright 2001, 2002, 2003, 2004 Hewlett-Packard Development Company, LP
 */

package com.hp.hpl.jena.sparql.lang.rdql ;

public interface RDQLParserConstants {

  int EOF = 0;
  int SINGLE_LINE_COMMENT = 14;
  int MULTI_LINE_COMMENT = 15;
  int INTEGER_LITERAL = 17;
  int DECIMAL_LITERAL = 18;
  int HEX_LITERAL = 19;
  int FLOATING_POINT_LITERAL = 20;
  int EXPONENT = 21;
  int STRING_LITERAL1 = 22;
  int STRING_LITERAL2 = 23;
  int URI = 24;
  int QNAME = 25;
  int QNAME_PREFIX = 26;
  int QNAME_LNAME = 27;
  int SELECT = 28;
  int SOURCE = 29;
  int FROM = 30;
  int WHERE = 31;
  int SUCHTHAT = 32;
  int PREFIXES = 33;
  int FOR = 34;
  int STR_EQ = 35;
  int STR_NE = 36;
  int STR_LANGEQ = 37;
  int BOOLEAN_LITERAL = 38;
  int NULL_LITERAL = 39;
  int DIGITS = 40;
  int NCName = 41;
  int VAR = 42;
  int LPAREN = 43;
  int RPAREN = 44;
  int LBRACE = 45;
  int RBRACE = 46;
  int LBRACKET = 47;
  int RBRACKET = 48;
  int SEMICOLON = 49;
  int COMMA = 50;
  int DOT = 51;
  int ASSIGN = 52;
  int GT = 53;
  int LT = 54;
  int BANG = 55;
  int TILDE = 56;
  int HOOK = 57;
  int COLON = 58;
  int EQ = 59;
  int NEQ = 60;
  int LE = 61;
  int GE = 62;
  int SC_OR = 63;
  int SC_AND = 64;
  int INCR = 65;
  int DECR = 66;
  int PLUS = 67;
  int MINUS = 68;
  int STAR = 69;
  int SLASH = 70;
  int BIT_AND = 71;
  int BIT_OR = 72;
  int BIT_XOR = 73;
  int REM = 74;
  int LSHIFT = 75;
  int RSIGNEDSHIFT = 76;
  int RUNSIGNEDSHIFT = 77;
  int STR_MATCH = 78;
  int STR_NMATCH = 79;
  int DATATYPE = 80;
  int AT = 81;
  int PATTERN = 82;

  int DEFAULT = 0;
  int READ_IDENTIFIER = 1;
  int IN_SINGLE_LINE_COMMENT = 2;
  int IN_MULTI_LINE_COMMENT = 3;
  int READ_URI = 4;
  int READ_QNAME = 5;
  int READ_REGEX = 6;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "\"//\"",
    "\"#\"",
    "\"/*\"",
    "<SINGLE_LINE_COMMENT>",
    "\"*/\"",
    "<token of kind 16>",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEX_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<STRING_LITERAL1>",
    "<STRING_LITERAL2>",
    "<URI>",
    "<QNAME>",
    "<QNAME_PREFIX>",
    "<QNAME_LNAME>",
    "\"select\"",
    "\"source\"",
    "\"from\"",
    "\"where\"",
    "\"and\"",
    "\"using\"",
    "\"for\"",
    "\"eq\"",
    "\"ne\"",
    "\"langeq\"",
    "<BOOLEAN_LITERAL>",
    "\"null\"",
    "<DIGITS>",
    "<NCName>",
    "<VAR>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"=\"",
    "\">\"",
    "\"<\"",
    "\"!\"",
    "\"~\"",
    "\"?\"",
    "\":\"",
    "\"==\"",
    "\"!=\"",
    "\"<=\"",
    "\">=\"",
    "\"||\"",
    "\"&&\"",
    "\"++\"",
    "\"--\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"&\"",
    "\"|\"",
    "\"^\"",
    "\"%\"",
    "\"<<\"",
    "\">>\"",
    "\">>>\"",
    "<STR_MATCH>",
    "\"!~\"",
    "\"^^\"",
    "\"@\"",
    "<PATTERN>",
  };

}
