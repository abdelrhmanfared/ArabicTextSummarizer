package Files;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import Accuracy.AccuracyMeasures;
import textrank.Textrank;
import utilities.SentenceDetector;

public class DatasetFile {
	static ArrayList<String> Text_generated;
	static ArrayList<String> summary_generated;
	
public void ReadDataset() {
	
	 try {
	    	SentenceDetector sd = new SentenceDetector();
	    	String filename = "EASCModified.csv";
	        InputStream fileInputStream = new FileInputStream(filename);
	        Reader reader = new InputStreamReader(fileInputStream, "UTF-8"); // leave charset out for default
	        BufferedReader bufferedReader = new BufferedReader(reader);
			this.Text_generated = new ArrayList<String>();
			this.summary_generated = new ArrayList<String>();
			String line = bufferedReader.readLine();
			int i = -1;
			
	        while ((line = bufferedReader.readLine()) != null) {
	        	System.out.println("Summarizing article #" + ++i + "...");
	        	String [] row = line.split(",");
	        	if(row.length > 3)
	            	System.out.println("Dataset reading error !");
	        	
	        	this.Text_generated.add(row[1]);
	        	this.summary_generated.add(row[2]);       
	        }
	        }catch(Exception e) {System.err.println(e.getMessage());}
	
}
public ArrayList<String> getOriginal() {
	return Text_generated;
}
public ArrayList<String> getReferenced() {
	return summary_generated;
}

}
