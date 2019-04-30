# Compiler
Written by David Luong, Mitchell Norseth, and Mauricio Macias.

It is written on Java and compiled on eclipse.

To run program place Parser.jar, code.txt, lex_table.csv, syntax_table.csv into one directory and run .jar

Parser Class.

This program uses LexicalAnalyzer to receive tokens and lexemes, and outputs process used and whether or not it is valid.

Inputs: syntax_table.csv

Outputs: output.txt

Constructor: Parser()

Public Functions: runOutput()

Format for .csv file

row, col   //excludes nonterminal column and inputs row

Inputs list //extends horizontally, first cell is filled with random character to align table

Table (Non-terminals are the first element in each row)


Lexical Analyzer Class.

This class outputs tokens. 

Inputs: lex_table.csv, code.txt

Constructor: LexicalAnalyzer()

Public Functions: lexer(), run(), runOutputFile()

Format for .csv file

row, col   //excludes state column and inputs row

Inputs list //extends horizontally

Table (No state numbers on the side)

//Blank line

States // Extends downward

//Syntax: StatesNum FinalState? BackupCharacter?

//Blank Line

Keywords //Label only

//Keywords List, extends Horizontally
