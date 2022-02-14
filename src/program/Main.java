package program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import preprocessing.Preprocessing;
import test.Test;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String ArabicText = readfile("ArabicText.txt");
		try {
			Preprocessing pre = new Preprocessing(ArabicText);
			System.out.print(pre.getRootText());
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
