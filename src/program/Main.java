package program;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import preprocessing.Preprocessing;
import preprocessing.Preprocessing1;
import textrank.Textrank;
import utilities.*;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Accuracy.AccuracyMeasures;
import Files.AccuracyFile;
import Files.DatasetFile;

//import javafx.scene.control.Cell;
public class Main {

	public static void main(String[] args) throws IOException  {
		
		// TODO Auto-generated method stub
		/*String ArabicText = readfile("ArabicText.txt");
		try {
			//Preprocessing1 pre1 = new Preprocessing1(ArabicText);
			//Preprocessing pre = new Preprocessing(ArabicText);
			Textrank tr = new Textrank(ArabicText, null, -1);
			String path = "‪C:\\Users\\body fared\\Documents\\GitHub\\ArabicTextSummarizer\\EASC\\";
			InputStream fileInputStream = new FileInputStream(path+"Titles.txt");
	        Reader reader = new InputStreamReader(fileInputStream, "UTF-8");
	        BufferedReader bufferedReader = new BufferedReader(reader);
	        String T = "";
	        ArrayList<String> FileName  = new ArrayList<String>();
	        ArrayList<String> title  = new ArrayList<String>();
	        ArrayList<String> Articles  = new ArrayList<String>();
	        ArrayList<String> Summaries  = new ArrayList<String>();	        
	        while( (T = bufferedReader.readLine()) != null) {	       
	        	String[] v = T.split("\\*");
	    		FileName.add(v[0]);
	    		if (v.length > 1) {title.add(v[1]);}
	        }
	        for (int i=0;i<FileName.size();i++) {
	        	Articles.add(readall(path+"\\Articles\\"+FileName.get(i)+".txt")); 
	        	Summaries.add(readall(path+"\\Summaries\\"+FileName.get(i)+"_A.txt")); 
	      }
			//accuracy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();
		}		*/
		 DatasetFile datasetFile =  new DatasetFile();
		 datasetFile.ReadDataset();
		 Accuracy(datasetFile);


	}
	public static String readall(String fullpath) throws IOException {
		InputStream fs = new FileInputStream(fullpath);
		Reader rd = new InputStreamReader(fs, "UTF-8");
        BufferedReader bf = new BufferedReader(rd);
        String tmp = "" , s = "";
        while((s = bf.readLine()) != null)tmp+=s;
        return tmp;		
	}

	public static void Accuracy(DatasetFile datasetFile) throws IOException
	{
		 AccuracyFile acFile = new AccuracyFile();

		try 
		{
			Textrank tr;
			ArrayList<String> TextRankSummary = new ArrayList<String>(); 
			for(int i=0;i<16;i++)
				TextRankSummary.add((new Textrank(datasetFile.getOriginal().get(i), null, -1)).getSummarizedText());
			
		 acFile.CreateSheet1(TextRankSummary,datasetFile);
		}catch (Exception e) {System.out.println(e.getMessage());}
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
