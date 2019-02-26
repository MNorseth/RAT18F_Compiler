import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
 
public class lexicalAnalyzer {
	static BufferedReader br;
	static int character; // current char being read
	static int[][] fsm; //States are represented by their numbers, only has a -1 offset when traversing table
	static int row, col;
	static char[] inputs;
	static HashMap<Integer, String> finalStates;
	static Vector<String> keywords;
	
	public static void main(String[] args) throws IOException {
		
		buildTable("table.csv");
		
		br = new BufferedReader(new FileReader("code.txt"));
		PrintWriter out = new PrintWriter(new FileWriter("output.txt"));
		character = br.read();
		String[] tokens;

		while (br.ready()) {
			tokens = lexer();
			System.out.println(tokens[0] + ": " + tokens[1]);
			//out.println(tokens[0] + ": " + tokens[1]);

		}
		br.close();
		out.close();
	}
	
	static String[] lexer() throws IOException{
		
		int state = 1, inputVal;
		String lexeme = "";
		String[] tokens = {"Not Found", "Not Found"};//new String[2];
		
		//waiting for final table, algo needs tweaking
		while (br.ready()) {
			
			inputVal = inputTranslation(character);
			state = fsm[state - 1][inputVal];
		
			if (isFinal(state)) {
				
				if (state != 3) {  // special cases, do not use last character
					lexeme = lexeme + (char)character;
					character = br.read();
				}
				tokens[1] = lexeme;
				
				if (state == 3 && isKeyword(lexeme))
					tokens[0] = "Keyword";
				else 
					tokens[0] = finalStates.get(state);
				
				return tokens;
			}
			else {
				if (character != 32) //Spaces
					lexeme = lexeme + (char)character;
				character = br.read();
			}
		}
		return tokens;
	}
	
	//Takes input as .csv file
	static void buildTable(String file) throws IOException{ 
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String seperator = ",";
		String[] line = br.readLine().split(seperator);
		row = Integer.parseInt(line[0]);
		col = Integer.parseInt(line[1]);		
		fsm = new int[row][col];

		//Build headers
		//Assuming index 0 is Letters, index 1 is Digits
		line = br.readLine().split(seperator);
		inputs = new char[col];
		for (int i = 2; i < col; i++) {
				inputs[i] = line[i].charAt(0);
		}
		
		//Build table
		for(int i = 0; i < row; i++) {
			line = br.readLine().split(seperator);
			for(int j = 0; j < col; j++) {
				fsm [i][j] = Integer.parseInt(line[j]);
			}
		}
		
		//Build state list, Only records final states
		line = br.readLine().split(seperator); //blank space
		finalStates = new HashMap<Integer, String>();
		for(int i = 0; i < row; i++) {
			line = br.readLine().split(seperator);
			if (line.length == 3) { // Check Flag
				finalStates.put(i + 1, line[1]);	
			}
		}
		
		//Build Keyword list
		line = br.readLine().split(seperator); // Blank line
		line = br.readLine().split(seperator); //Eliminate header
		line = br.readLine().split(seperator); //Read keywords
		keywords = new Vector<String>(Arrays.asList(line));
		
		br.close();
	}
	
	static void printTable() {
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				System.out.print(fsm[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	//Takes ascii value and translates into values to traverse table easier
	static int inputTranslation (int character) {
		//System.out.print((char)character);
		if (character >= 'A' && character <= 'Z' || character >= 'a' && character <= 'z')
			return 0;
		else if (character >= '0' && character <= '9')
			return 1;
		else if (character == ',') // special case, CSV file limitation
			return col - 2;
		else {
			for (int i = 2; i < col - 2; i++) { //Change to -1 offset ^
				if (inputs[i] == character) {
					return i; 
				}
			}
			return col-1;
		}
	}
	
	static boolean isKeyword(String lexeme) {
		return keywords.contains(lexeme);
	}
	
	static boolean isFinal (int state) {
		return finalStates.containsKey(state);
	}
}
