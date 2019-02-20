import java.io.*;
public class lexicalAnalyzer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		File table = new File("C:\\Users\\David\\source\\Compiliers_Lexical_Analyzer/table.csv");
		int[][] fsm = buildTable(table);
		
		File file = new File("C:\\Users\\David\\source\\Compiliers_Lexical_Analyzer/code.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		//Translates input into table values
		int character, state = 1;
		String lexeme = "";
		
		while ((character = br.read()) != -1) {
			lexeme = lexeme + (char)character;
			
			character = inputTranslation(character);
			state = fsm[state - 1][character];
		
			if (isFinal(state)) {
				System.out.println(printState(state) + "  " + lexeme);
				state = 1;
				lexeme = "";
			}
		}
		br.close();
	}
	//Takes input as .csv file
	static int[][] buildTable(File file) throws IOException{ // return dim?
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String seperator = ",";
		String[] line = br.readLine().split(seperator);
		int row = Integer.parseInt(line[0]);
		int col = Integer.parseInt(line[1]);
		
		int[][] fsm = new int[row][col];

		/*
		//build headers
		line = br.readLine().split(seperator);
		for(int i = 0; i < col - 1; i++) {
			fsm [0][i+1] = line[i].charAt(0);
		}
		*/
		//build table
		for(int i = 0; i < row; i++) {
			line = br.readLine().split(seperator);
			for(int j = 0; j < col; j++) {
				fsm [i][j] = Integer.parseInt(line[j]);
			}
		}
/*
		//print table		
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				System.out.print(fsm[i][j] + " ");
			}
			System.out.println();
		}
*/	
		br.close();
		return fsm;		
	}
	
	//takes ascii value and translates into values to traverse table easier
	static int inputTranslation (int character) {
		//System.out.print((char)character);
		if (character >= 'A' && character <= 'Z' || character >= 'a' && character <= 'z')
			return 0;
		else if (character >= '0' && character <= '9')
			return 1;
		else {
			switch(character) {
			case '.': return 2; 
			case '#': return 3;
			case '\n': return 4; 
			case '(': return 5; 
			case '*': return 6; 
			case ')': return 7; 
			case '=': return 8; 
			case '<': return 9; 
			case '>': return 10; 
			case ' ': return 11; 
			default: return 12; 
			}
		}
	}
	
	static boolean isFinal(int state) {
		switch(state) {
		case 3: case 5: case 7: case 11: case 15: case 16:
			return true;
		default: 
			return false;	
		}
	}
	
	static String printState(int state) {
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
