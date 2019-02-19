import java.io.*;
public class lexicalAnalyzer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		File file = new File("C:\\Users\\David\\source\\Compiliers_Lexical_Analyzer/code.txt");
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		int character;
		while ((character = br.read()) != -1) {
			System.out.println(character);
		}
		
		br.close();
	}
	//Takes input as .csv file
	static int[][] buildTable(String fName) throws IOException{
		
		File file = new File(fName);
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String seperator = ",";
		String[] dim = br.readLine().split(seperator);
		
		int[][] fsm = new int[Integer.parseInt(dim[0])][Integer.parseInt(dim[1])];
		
		br.close();
		}
		
		
	}

}
