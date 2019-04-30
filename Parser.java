import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Parser {
	static Stack<String> stack;
	static LexicalAnalyzer lex;
	static String[] token;
	static PrintWriter out;
	static int row, col;
	static String[][] table;
	static String[] symbols;
	static String[] nonTerminals;
	
	public Parser() throws IOException {
		lex = new LexicalAnalyzer();
		buildTable("syntax_table.csv");
		//printTable();
		
		stack = new Stack<String>();
		stack.push("$");
		stack.push("S");
		token = new String[2];
		token = lex.lexer();
		
		
	}
	
	public void runOutput() throws IOException {
		out = new PrintWriter(new FileWriter("output.txt"));
		out.println("Token: " + token[0] +  "     Lexeme: " + token[1]);
		Boolean valid = false;
		String str = "";
		
		while (!stack.isEmpty()) {
			if (!Character.isUpperCase(stack.peek().charAt(0))) { //is terminal
				
				if (stack.peek().equals(token[1]) || stack.peek().equals("i")) { 
					
					stack.pop();
					token = lex.lexer();
					if (token[0].equals("-1") || token[1].equals(";")) {
						token[0] = "$";
						token[1] = "$";
					}
					out.println("Token: " + token[0] +  "     Lexeme: " + token[1]);
				}
				else {
					valid = false;
					break;	
				}
			}		
			else {// if nonterminal
				valid = false;
				str = tableTranslation(stack.peek(), token);
				String term;
				if (!str.equals("~")) {
					valid = true;
					out.println(stack.peek() + "  -> " + str);
					stack.pop();
					if(!str.equals("EPSILON")) {
						for (int i = str.length()-1; i >= 0; i--) {
							term = "" + str.charAt(i);
							stack.push(term);
						}
					}
				
				}
				else 
					break;
			}
		}
		if (!valid) {
			out.println("Error Lexeme: " + token[0] + "  " + token[1]); 
			out.println("Symbol table data: " + str + "  Top of stack: " + stack.peek());
		}
		else
			out.println("Syntax is valid.");
		out.close();
	}
	
	static String tableTranslation(String term, String[] token) {
		String lex = token[1];
		if (token[0].equals("Identifier"))
			lex = "i";
		
		int _row = -1, _col = -1;
		for(int i = 0; i < row; i++) {
			if(nonTerminals[i].equals(term)) {
				_row = i;
				continue;
			}
		}
		for(int i = 0; i < col; i++) {
			if(symbols[i].equals(lex)) {
				_col = i;
				continue;
			}
		}
		if (_col == -1 || _row == -1) {
			out.println("Error reading token: Token: " + token[0] + "  " + token[1] + "  Nonterminal: " + term);
			return "-1";
		}
		else
			if (table[_row][_col].equals("~"))
				return "-1";
		
		return table[_row][_col];
	}
	
	//Takes input as .csv file
	static void buildTable(String file) throws IOException{ 
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String seperator = ",";
		String[] line = br.readLine().split(seperator);
		row = Integer.parseInt(line[0]);
		col = Integer.parseInt(line[1]);		
		table = new String[row][col];

		//Build headers
		line = br.readLine().split(seperator);
		symbols = new String[col];
		for (int i = 1; i < col + 1; i++) {
				symbols[i - 1] = line[i];
		}
		
		//Build table
		nonTerminals = new String[row];
		for(int i = 0; i < row; i++) {
			line = br.readLine().split(seperator);
			nonTerminals[i] = line[0]; 
			for(int j = 1; j < col+1; j++) {
				table [i][j - 1] = line[j];
			}
		}
		br.close();
	}
	
	static void printTable() {
		for(int i = 0; i < col; i++)
			System.out.print(symbols[i] + " ");
		System.out.println();
	
		for(int i = 0; i < row; i++) {
			System.out.print(nonTerminals[i] + " ");
			
			for(int j = 0; j < col; j++) {
				System.out.print(table[i][j] + " ");
			}
			System.out.println();
		}	
	}
		
}
