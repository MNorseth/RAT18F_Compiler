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
		stack.push("$");
		token = new String[2];
		token =lex.lexer();
	}
	
	public void runOutput() throws IOException {
		out = new PrintWriter(new FileWriter("output.txt"));
		
		out.close();
	}
	
	static Boolean s() {
		if (token[0] == "Identifier") {
			
			return true;
		}
		return false;
	}
	
	static Boolean e() {
		out.println("E -> TQ");
		stack.pop();
		stack.push("R");
		stack.push("F");
		return true;		
		
	}
	
	static Boolean q() {
		if (token[1] == "+") {
			out.println("Q -> +TQ");
			stack.pop();
			stack.push("Q");
			stack.push("T");
			return true;		
		}
		else if (token[1] == "-") {
			out.println("Q -> -TQ");
			stack.pop();
			stack.push("Q");
			stack.push("T");
			return true;		
		}
		else { // epsilon
			out.println("Q -> EPSILON");
			stack.pop();
		}
		return false;
	}
	
	static Boolean t() { //check
		out.println("T -> FR");
		stack.pop();
		stack.push("R");
		stack.push("F");
		return true;		
		
		//return false;
	}

	static Boolean r() {
		if (token[1] == "*") {
			out.println("R -> *FR");
			stack.pop();
			stack.push("R");
			stack.push("F");
			return true;
		}
		else if (token[1] == "/") {
			out.println("R -> /FR");
			stack.pop();
			stack.push("R");
			stack.push("F");			
			return true;
		}
		else {//epsilon
			out.println("R -> EPSILON");
			stack.pop();
			return true;
			}
	}
	
	static Boolean f() {
		if (token[1] == "(") {
			out.println("F -> (E)");
			stack.pop();
			stack.push(")");
			stack.push("E");
			return true;
		}
		else if (token[0] == "Identifier") {
			out.println("F -> i");
			stack.pop();
			stack.push("i"); 
			return true;
	}
		return false;
	}
	
}
