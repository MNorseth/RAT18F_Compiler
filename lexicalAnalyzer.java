import java.io.*;

public class lexicalAnalyzer {
	static int[][] fsm;
	static int row, col;
	static char[] inputs;
	
	public static void main(String[] args) throws IOException {
		
		File file = new File("C:\\Users\\David\\source\\Compiliers_Lexical_Analyzer/table.csv");
		buildTable(file);
		
		file = new File("C:\\Users\\David\\source\\Compiliers_Lexical_Analyzer/code.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		int character, state = 1, inputVal;
		String lexeme = "";
		
		character = br.read(); //waiting for final table, algo needs tweaking
		while (character != -1) {
			
			lexeme = lexeme + (char)character;
			inputVal = inputTranslation(character);
			state = fsm[state - 1][inputVal];
		
			if (isFinal(state)) {
				System.out.println(printState(state) + "  " + lexeme);
				state = 1;
				lexeme = "";
				//cut off last elem
				character = br.read();
			}
			else {
				character = br.read();
			}
		}
		br.close();
	}
	//Takes input as .csv file
	static void buildTable(File file) throws IOException{ // return dim?
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String seperator = ",";
		String[] line = br.readLine().split(seperator);
		row = Integer.parseInt(line[0]);
		col = Integer.parseInt(line[1]);		
		fsm = new int[row][col];

		//build headers
		//Assuming index 0 is Letters, index 1 is Digits
		line = br.readLine().split(seperator);
		inputs = new char[col];
		for (int i = 2; i < col; i++) {
			inputs[i] = line[i].charAt(0);
		}
		
		//build table
		for(int i = 0; i < row; i++) {
			line = br.readLine().split(seperator);
			for(int j = 0; j < col; j++) {
				fsm [i][j] = Integer.parseInt(line[j]);
			}
		}
		
		//build state list TODO maybe
		//for(int i = 0; i < row; i++) {
			//line = br.readLine().split(seperator);
		
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
	
	//takes ascii value and translates into values to traverse table easier
	static int inputTranslation (int character) {
		//System.out.print((char)character);
		if (character >= 'A' && character <= 'Z' || character >= 'a' && character <= 'z')
			return 0;
		else if (character >= '0' && character <= '9')
			return 1;
		else {
			for (int i = 2; i < col - 1; i++) {
				if (inputs[i] == character) {
					return i;
				}
			}
			return col-1;
		}
	}
	
	static boolean isFinal(int state) { //Edit with final cases, dont forget to change printState
		switch(state) {
		case 3: case 5: case 7: case 11: case 15: case 16:
			return true;
		default: 
			return false;	
		}
	}
	
	static String printState(int state) { // Final cases names
		switch(state) {
		case 3: return "Identifier";
		case 5: return "Number";
		case 7: return "#_Comment";
		case 11: return "**_Comment";
		case 15: return "Punctuation";
		case 16: return "Punctuation";
		default: return "error";
	}
	}
}
