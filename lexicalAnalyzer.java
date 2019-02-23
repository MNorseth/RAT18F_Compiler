import java.io.*;
import java.util.HashMap;
//import java.util.Map;

public class lexicalAnalyzer {
	static int[][] fsm;
	static int row, col;
	static char[] inputs;
	static HashMap<Integer, String> finalStates;
	
	public static void main(String[] args) throws IOException {
		
		buildTable("table.csv");
		
		BufferedReader br = new BufferedReader(new FileReader("code.txt"));
		
		int character, state = 1, inputVal;
		String lexeme = "";
		
		character = br.read(); //waiting for final table, algo needs tweaking
		while (character != -1) {
			
			lexeme = lexeme + (char)character;
			inputVal = inputTranslation(character);
			state = fsm[state - 1][inputVal];
		
			if (finalStates.containsKey(state)) {
				System.out.println(finalStates.get(state) + "  " + lexeme);
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
	static void buildTable(String file) throws IOException{ 
		
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
		
		//build state list, Only records final states
		finalStates = new HashMap<Integer, String>();
		for(int i = 0; i < row; i++) {
			line = br.readLine().split(seperator);
			if (line.length == 3) {
				finalStates.put(i, line[1]);	
			}
		}
		
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

}
