# Compiler
Written by David Luong, Mitchell Norseth, and Mauricio Macias.

Lexical Analyzer.

This Program outputs tokens. It is written on Java and compiled on eclipse.

To run program place Lex_analzer.jar, code.txt, and table.csv into one directory and run .jar

Inputs: table.csv, code.txt

Output: output.txt

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
