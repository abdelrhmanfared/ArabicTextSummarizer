package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import accuracy.AccuracyMeasures;

public class AccuracyFile {
	public XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet;
	Map<Integer, String> CellNames;
	Map<String,Float> AVGRougeNGrams;
	static AverageFile averageFile;
	String MethodName;

	
	public AccuracyFile(String MethodName,AverageFile averageFile) throws IOException {
	  AVGRougeNGrams = new HashMap<String, Float>();
	  AVGRougeNGrams.put("Recall1",(float)0);
	  AVGRougeNGrams.put("Recall2",(float)0);
	  AVGRougeNGrams.put("Precision1",(float)0);
	  AVGRougeNGrams.put("Precision2",(float)0);
	  AVGRougeNGrams.put("FScore1",(float)0);
	  AVGRougeNGrams.put("FScore2",(float)0);

	  this.averageFile = averageFile;
	  this.MethodName = MethodName;
	  
	  //Create First Row
	  this.sheet = workbook.createSheet(MethodName);
	  XSSFRow xsrow = sheet.createRow(0);	  
	  CellNames = new HashedMap<Integer, String>();
	  CellNames.put(0, "Original");
	  CellNames.put(1, "Referenced");
	  CellNames.put(2, "System Generaed");
	  CellNames.put(3, "Recall 1");
	  CellNames.put(4, "Precision 1");
	  CellNames.put(5, "FScore 1");
	  CellNames.put(6, "Recall 2");
	  CellNames.put(7, "Precision 2");
	  CellNames.put(8, "FScore 2");
		
		XSSFCell[] Cells = new XSSFCell[9];
		for(Map.Entry<Integer, String> entry : CellNames.entrySet())
			{Cells[entry.getKey()] = xsrow.createCell(entry.getKey());Cells[entry.getKey()].setCellValue(entry.getValue());}
		
	}
	public void CreateAccuracySheet(List<List<String>> system_generated, List<List<String>> referenced) throws IOException {
		try {
			XSSFRow xsrow = null;
			for(int i=0;i<system_generated.size();i++)
			{
	 		AccuracyMeasures acMeasures = new AccuracyMeasures();
			xsrow = sheet.createRow(i+1);
		         for(int j=0;j<9;j++)
		         {
		         	XSSFCell newcell = xsrow.createCell(j);
		         	if(newcell.getColumnIndex()==0)
		         		newcell.setCellValue("");
		         	else if(newcell.getColumnIndex() == 1)
		         		newcell.setCellValue("");
		         	else if(newcell.getColumnIndex() == 2)
		         		newcell.setCellValue("");
		         	else if(newcell.getColumnIndex() == 3)
		         		{acMeasures.Rouge1(system_generated.get(i), referenced.get(i));
		         		newcell.setCellValue(acMeasures.getRecall());
		         		AVGRougeNGrams.computeIfPresent("Recall1", (k, v) -> v + acMeasures.getRecall());}
		         	else if(newcell.getColumnIndex() == 4)
		         		{newcell.setCellValue(acMeasures.getPrecision());AVGRougeNGrams.computeIfPresent("Precision1", (k, v) -> v + acMeasures.getPrecision());}
		         	else if(newcell.getColumnIndex() == 5)
		         		{newcell.setCellValue(acMeasures.getFScore());AVGRougeNGrams.computeIfPresent("FScore1", (k, v) -> v + acMeasures.getFScore());}
		         	else if(newcell.getColumnIndex() == 6)
		         		{acMeasures.Rouge2(system_generated.get(i), referenced.get(i));newcell.setCellValue(acMeasures.getRecall());
		         		AVGRougeNGrams.computeIfPresent("Recall2", (k, v) -> v + acMeasures.getRecall());}
		         	else if(newcell.getColumnIndex() == 7)
		         		{newcell.setCellValue(acMeasures.getPrecision());AVGRougeNGrams.computeIfPresent("Precision2", (k, v) -> v + acMeasures.getPrecision());}
		         	else if(newcell.getColumnIndex() == 8)
		         		{newcell.setCellValue(acMeasures.getFScore());AVGRougeNGrams.computeIfPresent("FScore2", (k, v) -> v + acMeasures.getFScore());}
		         }
	         }
			this.averageFile.CalculateAverage(AVGRougeNGrams, system_generated, xsrow, this.MethodName);
		}catch (Exception e) {System.err.println(e.getMessage());}
		
		CreateFile(MethodName+".xlsx");
	}
	public void CreateFile(String MethodName) throws IOException {
		 try{FileOutputStream fileOutputStream = new FileOutputStream(new File(MethodName));
	       	workbook.write(fileOutputStream);
	       	fileOutputStream.close();
	       	System.out.println(MethodName +" file is Created");
	       }catch (FileNotFoundException ex) {}
	}
}