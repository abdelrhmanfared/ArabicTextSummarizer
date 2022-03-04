package program;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;
import textrank.Textrank;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		String ArabicText = readfile("ArabicText.txt");
		try {
			Textrank tr = new Textrank(ArabicText);
			
			//SALMA 
//			double[] scores = tr.sentencelocation(ArabicText);
//			for(int i=0; i<scores.length; i++)
//				System.out.print("score: "+ scores[i]);
			//******************
			System.out.print(tr.getSummarizedText());
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       try {
    	String filename = "EASCModified.csv";
        InputStream fileInputStream = new FileInputStream(filename);
        Reader reader = new InputStreamReader(fileInputStream, "UTF-8"); // leave charset out for default
        BufferedReader bufferedReader = new BufferedReader(reader);
		ArrayList<String> Text_genrated = new ArrayList<String>();
		ArrayList<String> summary_genrated = new ArrayList<String>();
		String line = bufferedReader.readLine();
        while ((line = bufferedReader.readLine()) != null) {        	
        	String [] row = line.split(",");
        	Text_genrated.add(row[1]);
        	summary_genrated.add(row[2]);
        }
        
    } catch (Exception e) {
        System.err.println(e.getMessage()); // handle all exceptions

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
