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

import preprocessing.Preprocessing;
import textrank.Textrank;
import utilities.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*String ArabicText = readfile("ArabicText.txt");
		try {
			Textrank tr = new Textrank(ArabicText);
			System.out.print(tr.getSummarizedText());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
       try {
    	SentenceDetector sd = new SentenceDetector();
    	String filename = "EASCModified.csv";
        InputStream fileInputStream = new FileInputStream(filename);
        Reader reader = new InputStreamReader(fileInputStream, "UTF-8"); // leave charset out for default
        BufferedReader bufferedReader = new BufferedReader(reader);
		ArrayList<String> Text_genrated = new ArrayList<String>();
		ArrayList<String> summary_genrated = new ArrayList<String>();
		ArrayList<String> system_generated = new ArrayList<String>();
		String line = bufferedReader.readLine();
		int i = 0;
        while ((line = bufferedReader.readLine()) != null && i++ < 10) {
        	String [] row = line.split(",");
        	Text_genrated.add(row[1]);
        	summary_genrated.add(row[2]);
        	//System.out.println(row[1]);
        	Textrank tr = new Textrank(row[1], -1);
        	system_generated.add(tr.getSummarizedText());
        	
        }
       System.out.println(system_generated.get(0));
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
	
	public static void test() {
	    try {
	    	LightStemmer1 l1 = new LightStemmer1();
			LightStemmer2 l2 = new LightStemmer2();
			LightStemmer3 l3 = new LightStemmer3();
			LightStemmer8 l8 = new LightStemmer8();
			LightStemmer10 l10 = new LightStemmer10();
			RootStemmer rt = new RootStemmer();
			String word = "Ø¨ÙŠÙ†Ù‡";
			System.out.println(l1.findStem(word));
			System.out.println(l2.findStem(word));
			System.out.println(l3.findStem(word));
			System.out.println(l8.findStem(word));
			System.out.println(l10.findStem(word));
			System.out.println(rt.findRoot(word));
	    		  
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	
	  }

}
