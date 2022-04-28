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

//import javafx.scene.control.Cell;
public class Main {

	public static void main(String[] args)  {
		
		// TODO Auto-generated method stub
		String ArabicText = readfile("ArabicText.txt");
		try {
			Preprocessing1 pre1 = new Preprocessing1(ArabicText);
			Preprocessing pre = new Preprocessing(ArabicText);
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
			accuracy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static String 	readall(String fullpath) throws IOException {
		InputStream fs = new FileInputStream(fullpath);
		Reader rd = new InputStreamReader(fs, "UTF-8");
        BufferedReader bf = new BufferedReader(rd);
        String tmp = "" , s = "";
        while((s = bf.readLine()) != null)tmp+=s;
        return tmp;		
	}

	public static void accuracy() throws IOException
	{
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Accuracy1");
		XSSFSheet sheet2 = workbook.createSheet("Accuracy2");
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
		int i = -1;
		XSSFRow xsrow = sheet.createRow(0);
		XSSFCell cell1 = xsrow.createCell(0);
		XSSFCell cell2 = xsrow.createCell(1);
		XSSFCell cell3 = xsrow.createCell(2);
		XSSFCell cell4 = xsrow.createCell(3);
		XSSFCell cell5 = xsrow.createCell(4);
		XSSFCell cell6 = xsrow.createCell(5);
		XSSFCell cell7 = xsrow.createCell(6);
		XSSFCell cell8 = xsrow.createCell(7);
		XSSFCell cell9 = xsrow.createCell(8);
		cell1.setCellValue("Original");
		cell2.setCellValue("Referenced");
		cell3.setCellValue("System Generated");
		cell4.setCellValue("Recall1");
		cell5.setCellValue("Precision1");
		cell6.setCellValue("FScore1");
		cell7.setCellValue("Recall2");
		cell8.setCellValue("Precision2");
		cell9.setCellValue("FScore2");
		
		XSSFRow xsrow2 = sheet2.createRow(0);
		XSSFCell cell12 = xsrow2.createCell(0);
		XSSFCell cell22 = xsrow2.createCell(1);
		XSSFCell cell32 = xsrow2.createCell(2);
		XSSFCell cell42 = xsrow2.createCell(3);
		cell12.setCellValue("Method");
		cell22.setCellValue("Measure");
		cell32.setCellValue("Rouge1");
		cell42.setCellValue("Rouge2");
		

		
		float avgRecall1 = 0,avgRecall2=0;
		float avgPrecision1 = 0,avgPrecision2=0;
		float avgFScore1 = 0,avgFScore2=0;
		float Recall1,Recall2;
		float Precision1,Precision2;
		float FScore1,FScore2;
		
		
        while ((line = bufferedReader.readLine()) != null) {
        	System.out.println("Summarizing article #" + ++i + "...");
        	String [] row = line.split(",");
        	if(row.length > 3)
            	System.out.println("Dataset reading error !");
        	
        	Text_genrated.add(row[1]);
        	summary_genrated.add(row[2]);
        	//System.out.println(row[1]);
        	Textrank tr = new Textrank(row[1], -1);
        	system_generated.add(tr.getSummarizedText());
            //System.out.println(system_generated.get(i)+"\n\n");
            /*System.out.printf("%f,%f,%f\n",
            Rouge_2(summary_genrated.get(i), system_generated.get(i),"Recall"),
            Rouge_2(summary_genrated.get(i), system_generated.get(i),"Precision"),
            Rouge_2(summary_genrated.get(i), system_generated.get(i),"FScore")); */
            
            //System.out.println();
        	
        	Recall1 = Rouge_1(summary_genrated.get(i), system_generated.get(i),"Recall");
        	Precision1 = Rouge_1(summary_genrated.get(i), system_generated.get(i),"Precision");
        	FScore1 = Rouge_1(summary_genrated.get(i), system_generated.get(i),"FScore");
        	avgRecall1+=Recall1;
        	avgPrecision1+=Precision1;
        	avgFScore1+=FScore1;
        	
        	Recall2 = Rouge_2(summary_genrated.get(i), system_generated.get(i),"Recall");
        	Precision2 = Rouge_2(summary_genrated.get(i), system_generated.get(i),"Precision");
        	FScore2 = Rouge_2(summary_genrated.get(i), system_generated.get(i),"FScore");
        	avgRecall2+=Recall2;
        	avgPrecision2+=Precision2;
        	avgFScore2+=FScore2;
        	
            xsrow = sheet.createRow(i+1);
            for(int j=0;j<9;j++)
            {
            	XSSFCell newcell = xsrow.createCell(j);
            	if(newcell.getColumnIndex()==0)
            		newcell.setCellValue(row[1]);
            	else if(newcell.getColumnIndex() == 1)
            		newcell.setCellValue(row[2]);
            	else if(newcell.getColumnIndex() == 2)
            		newcell.setCellValue(system_generated.get(i));
            	else if(newcell.getColumnIndex() == 3)
            		newcell.setCellValue(Recall1);
            	else if(newcell.getColumnIndex() == 4)
            		newcell.setCellValue(Precision1);
            	else if(newcell.getColumnIndex() == 5)
            		newcell.setCellValue(FScore1);
            	else if(newcell.getColumnIndex() == 6)
            		newcell.setCellValue(Recall2);
            	else if(newcell.getColumnIndex() == 7)
            		newcell.setCellValue(Precision2);
            	else if(newcell.getColumnIndex() == 8)
            		newcell.setCellValue(FScore2);
            }
        }
        for(int k=0;k<3;k++) {
            xsrow2 = sheet2.createRow(k+1);
	        for(int j=0;j<4;j++)
	        {
	            XSSFCell newcell = xsrow2.createCell(j);
	            if(newcell.getColumnIndex()==0)
	        		newcell.setCellValue("TextRank");
	            if(k == 0) {
	        	if(newcell.getColumnIndex() == 1)
	        		newcell.setCellValue("Recall");
	        	else if(newcell.getColumnIndex() == 2)
	        		newcell.setCellValue(avgRecall1/11.0);
	        	else if(newcell.getColumnIndex() == 3)
	        		newcell.setCellValue(avgRecall2/11.0);
	        	}
	            else if(k == 1)
	            {
		        	if(newcell.getColumnIndex() == 1)
		        		newcell.setCellValue("Precision");
		        	else if(newcell.getColumnIndex() == 2)
		        		newcell.setCellValue(avgPrecision1/11.0);
		        	else if(newcell.getColumnIndex() == 3)
		        		newcell.setCellValue(avgPrecision2/11.0);
	            }
	            else if(k == 2)
	            {
	            	if(newcell.getColumnIndex() == 1)
		        		newcell.setCellValue("FScore");
		        	else if(newcell.getColumnIndex() == 2)
		        		newcell.setCellValue(avgFScore1/11.0);
		        	else if(newcell.getColumnIndex() == 3)
		        		newcell.setCellValue(avgFScore2/11.0);
	            }
	        }
        }
        System.out.println("Average Recall 1:"+avgRecall1/10.0);
        System.out.println("Average Precision 1:"+avgPrecision1/10.0);
        System.out.println("Average FScore 1:"+avgFScore1/10.0);
    }catch (Exception e) {System.err.println(e.getMessage());}
       try {
       	FileOutputStream fileOutputStream = new FileOutputStream(new File("Accuracy.xlsx"));
       	workbook.write(fileOutputStream);
       	fileOutputStream = new FileOutputStream(new File("Accuracy2.xlsx"));
       	workbook.write(fileOutputStream);
       	fileOutputStream.close();
       	System.out.println("Excel file is Created");
       }catch (FileNotFoundException ex) {}
       
}
	
	public static float Rouge_1(String reference,String generated,String Method) {
		float refGen=0;
		AraNormalizer arn = new AraNormalizer();
		generated = arn.normalize(generated);
		reference = arn.normalize(reference);
		String generated1[] = generated.trim().replace("\"", "").replaceAll(" ","").split("(?<=\\.| ؟ |!)");
		String reference1[] = reference.trim().replace("\"", "").replaceAll(" ","").split("(?<=\\.| ؟ |!)");
		for(String r : reference1)
			r.replace(".","");
		for(String g : generated1)
			g.replace(".","");
		for(int i=0;i<generated1.length;i++)
		{
			for(int j=0;j<reference1.length;j++) {
			
				if(generated1[i].equals(reference1[j]))
					refGen++;
			}
		}
		float Recall = refGen/reference1.length;
		float Precision = refGen/generated1.length;
		float FScore = (2*Recall*Precision)/(Recall+Precision);
		
		if(Method.equals("Recall"))
			return Recall;
		else if(Method.equals("Precision"))
			return Precision;
		else if(Method.equals("FScore"))
		{ if(Float.isNaN(FScore))
			return 0;
		  else 
			return FScore;
		}
		else 
			return -1;
	}
	public static float Rouge_2(String reference,String generated,String Method) {
		float refGen=0;
		AraNormalizer arn = new AraNormalizer();
		generated = arn.normalize(generated);
		reference = arn.normalize(reference);
		String generated1[] = generated.trim().replace("\"", "").replaceAll(" ","").split("(?<=\\.| ؟ |!)");
		String reference1[] = reference.trim().replace("\"", "").replaceAll(" ","").split("(?<=\\.| ؟ |!)");
		for(String r : reference1)
			r.replace(".","");
		for(String g : generated1)
			g.replace(".","");
		
		ArrayList<String> generated2 = new ArrayList<String>();
		ArrayList<String> reference2= new ArrayList<String>();
	
		
		for(int i=0;i<generated1.length;i++)
		{
			if(generated1.length == 1)
				generated2.add(generated1[i]);
			if(i == generated1.length-1)
				break;
		  generated2.add(generated1[i]+generated1[i+1]);
			
		}
		for(int i=0;i<reference1.length;i++)
		{
			if(reference1.length == 1)
				reference2.add(reference1[i]);
			if(i== reference1.length-1)
				break;
			reference2.add(reference1[i]+reference1[i+1]);
		}
		
		for(int i=0;i<generated2.size();i++)
		{
			for(int j=0;j<reference2.size();j++) {
			
				if(generated2.get(i).equals(reference2.get(j)))
					refGen++;
			}
		}
		float Recall = refGen/reference2.size();
		float Precision = refGen/generated2.size();
		float FScore = (2*Recall*Precision)/(Recall+Precision);
				
		if(Method.equals("Recall"))
			return Recall;
		else if(Method.equals("Precision"))
			return Precision;
		else if(Method.equals("FScore"))
			{ if(Float.isNaN(FScore))
				return 0;
			  else 
				return FScore;
			}
		else 
			return -1;
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
