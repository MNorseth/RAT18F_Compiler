import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Parser {
	static Stack<String> stack;
	static LexicalAnalyzer lex;
	static String[] token;
	static PrintWriter out;
	
	public Parser() throws IOException {
		lex = new LexicalAnalyzer();
		stack = new Stack<String>();
		stack.push("$");
		stack.push("S");
		token = new String[2];
		token = lex.lexer();
		
	}
	
	public void runOutput() throws IOException {
		out = new PrintWriter(new FileWriter("output.txt"));
		out.println("Token: " + token[0] +  "     Lexeme: " + token[1]);
		
		while (!stack.isEmpty()) {
			if (!Character.isUpperCase(stack.peek().charAt(0))) { //is terminal
				
				if (stack.peek().equals(token[1]) || stack.peek().equals("i")) { 
					
					stack.pop();
					token = lex.lexer();
					if (token[0].equals("-1")) {
						token[0] = "$";
						token[1] = "$";
					}
					out.println("Token: " + token[0] +  "     Lexeme: " + token[1]);
				}
				else {
				out.println("Error"); //
				}
			}		
			else {// if nonterm
				Boolean valid = false;
				char term = stack.peek().charAt(0);
				switch(term) {
					case 'S':
						valid = s();
						break;
					case 'E':
						valid = e();
						break;
					case 'Q':
						valid = q();
						break;
					case 'T':
						valid = t();
						break;
					case 'R':
						valid = r();
						break;
					case 'F':
						valid = f();
						break;
				}
				if (!valid) {
					out.println("Error"); //
				}
			}
		}
		out.close();
	}
	
	static Boolean s() {
		if (token[0].equals("Identifier")) {
			out.println("S -> i = E");
			stack.pop();
			stack.push("E");
			stack.push("=");
			stack.push("i");
			return true;
		}
		return false;
	}
	
	static Boolean e() {
		if (token[0].equals("Identifier") || token[1].equals("(")) {
			out.println("E -> TQ");
			stack.pop();
			stack.push("Q");
			stack.push("T");
			return true;		
		}
		return false;
	}
	
	static Boolean q() {
		if (token[1].equals("+")) {
			out.println("Q -> +TQ");
			stack.pop();
			stack.push("Q");
			stack.push("T");
			stack.push("+");
			return true;		
		}
		else if (token[1].equals("-")) {
			out.println("Q -> -TQ");
			stack.pop();
			stack.push("Q");
			stack.push("T");
			stack.push("-");
			return true;		
		}
		else if(token[1].equals(")") || token[1].equals("$")) { //eps condition
			out.println("Q -> EPSILION");	
			stack.pop();
			return true;
		}

		return false;
	}
	
	static Boolean t() {
		if (token[0].equals("Identifier") || token[1].equals("(")) {
			out.println("T -> FR");
			stack.pop();
			stack.push("R");
			stack.push("F");
			return true;		
		}
		return false;
	}

	static Boolean r() {
		if (token[1].equals("*")) {
			out.println("R -> *FR");
			stack.pop();
			stack.push("R");
			stack.push("F");
			stack.push("*");
			return true;
		}
		else if (token[1].equals("/")) {
			out.println("R -> /FR");
			stack.pop();
			stack.push("R");
			stack.push("F");	
			stack.push("/");
			return true;
		}
		else if (token[0].equals("Identifier") || token[1].equals("(") 
				|| token[1].equals("+") || token[1].equals("-") || token[1].equals("$")) {//epsilon
			out.println("R -> EPSILON");
			stack.pop();
			return true;
		}
		return false;
	}
	
	static Boolean f() {
		if (token[1].equals("(")) {
			out.println("F -> (E)");
			stack.pop();
			stack.push(")");
			stack.push("E");
			return true;
		}
		else if (token[0].equals("Identifier")) {
			out.println("F -> i");
			stack.pop();
			stack.push("i"); 
			return true;
	}
		return false;
		
	}
	
}
