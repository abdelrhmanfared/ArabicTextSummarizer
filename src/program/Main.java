package program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import textrank.Textrank;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String ArabicText = readfile("ArabicText.txt");
		try {
			Textrank tr = new Textrank(ArabicText);
			
			//SALMA 
			//*****************
//			double[] scores = tr.sentencelocation(ArabicText);
//			for(int i=0; i<scores.length; i++)
//				System.out.print("score: "+ scores[i]);
			//******************
			System.out.print(tr.getSummarizedText());
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String readfile(String filename) {
	    try {
	      File myObj = new File(filename);
	      Scanner myReader = new Scanner(myObj);
	      myReader.useDelimiter("\\Z");
	      String data = myReader.next();
	      myReader.close();
	      return data;
	    		  
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	      return null;
	    }
	    
	  }

}
