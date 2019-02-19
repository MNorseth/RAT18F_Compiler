import java.io.*;
public class lexicalAnalyzer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		File table = new File("C:\\Users\\David\\source\\Compiliers_Lexical_Analyzer/table.csv");
		int[][] fsm = buildTable(table);
		
		File file = new File("C:\\Users\\David\\source\\Compiliers_Lexical_Analyzer/code.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		int character;
		while ((character = br.read()) != -1) {
			System.out.print(character + " ");
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

		//build headers
		fsm [0][0] = -1;
		line = br.readLine().split(seperator);
		for(int i = 0; i < col - 1; i++) {
			fsm [0][i+1] = line[i].charAt(0);
		}
		
		//build table
		for(int i = 1; i < row; i++) {
			line = br.readLine().split(seperator);
			for(int j = 0; j < col; j++) {
				fsm [i][j] = Integer.parseInt(line[j]);
			}
		}
		//print table		
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				System.out.print(fsm[i][j] + " ");
			}
			System.out.println();
		}
		
		br.close();
		return fsm;	
		
	}
}
