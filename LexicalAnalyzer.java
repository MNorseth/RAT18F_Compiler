import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class LexicalAnalyzer {
	static BufferedReader br;
	static int character; // current char being read
	static int[][] fsm; //States are represented by their numbers, only has a -1 offset when traversing table
	static int row, col;
	static char[] inputs;
	static boolean lastCharFlag;
	static HashMap<Integer, String> finalStates;
	static Vector<String> keywords;
	static Vector<Integer> backup;
	static Boolean commentOutput; // outputs comments
	
	public LexicalAnalyzer() throws IOException {
		buildTable("lex_table.csv");
		specialCases(); //EDIT with characters like , \n \r etc
		br = new BufferedReader(new FileReader("code.txt"));
		character = br.read();
		//character = br.read();
		lastCharFlag = false;
		commentOutput = false;
	}
	
	public void run() throws IOException {
		String[] tokens;
		
		while (!lastCharFlag) {
			tokens = lexer();
			System.out.println(tokens[0] + ": " + tokens[1]);
		}
	}
	
	public void runOutputFile() throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter("output.txt"));
		String[] tokens = {"",""};
		
		while (tokens[0] != "-1") {
			tokens = lexer();
			out.println(tokens[0] + ": " + tokens[1]);
		}
		out.close();
	}
	
	//Returns {"Type", "Lexeme"}, or outputs {"-1" , "-1"} at end of file
	public String[] lexer() throws IOException{

		int state = 1, inputVal;
		String lexeme = "";
		String[] tokens = {"-1", "-1"};
		
		//waiting for final table, algo needs tweaking
		while(character != -1) {
			inputVal = inputTranslation(character);
			state = fsm[state - 1][inputVal];
		
			if (isFinal(state) || lastCharFlag) {
				
				if (!backup.contains(state)) {  
					lexeme = lexeme + (char)character;
					character = br.read();
					
				}
				tokens[1] = lexeme;
				
				if (state == 3 && isKeyword(lexeme))
					tokens[0] = "Keyword";
				else 
					tokens[0] = finalStates.get(state);
				if (lastCharFlag) {
					character = -1;
				}
				if (!commentOutput && (tokens[0].equals("Comment") 
						|| tokens[0].equals("Punctuation")))
						return lexer();
			
				return tokens;
			}
			else {
				if (state != 1) //Spaces, new line, etc
					lexeme = lexeme + (char)character;
				
				//check for end of file and add space to put last lexeme to final state
				if ((character = br.read()) == -1 && state != 1) {
					lastCharFlag = true;
					character = 32;
			} 
		}
		} 
		br.close();
		lastCharFlag = true;
		return tokens; //means error
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
		backup = new Vector<Integer>();
		for(int i = 0; i < row; i++) {
			line = br.readLine().split(seperator);
			if (line.length > 2 ) { // Check Final State Flag
				finalStates.put(i + 1, line[1]);	
			}
			if (line.length == 4) // Check Backup flag
				backup.addElement(i+1);
		}
		
		//Build Keyword list
		line = br.readLine().split(seperator); // Blank line
		line = br.readLine().split(seperator); //Eliminate header
		line = br.readLine().split(seperator); //Read keywords
		keywords = new Vector<String>(Arrays.asList(line));
		
		br.close();
	}
	
	static void specialCases() {
		inputs[14] = ',';
		inputs[15] = '\r';
		inputs[16] = '\n';
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
