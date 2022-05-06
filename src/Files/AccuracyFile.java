package Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Accuracy.AccuracyMeasures;
import textrank.Textrank;

public class AccuracyFile {
	public XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet1,sheet2;
	Map<Integer, String> CellNames1,CellNames2;

	
	public AccuracyFile() {
		
	  //Sheet 1 Create First Row
	  this.sheet1 = workbook.createSheet("Accuracy1");
	  XSSFRow xsrow1 = sheet1.createRow(0);	  
	  CellNames1 = new HashedMap<Integer, String>();
	  CellNames1.put(0, "Original");
	  CellNames1.put(1, "Referenced");
	  CellNames1.put(2, "System Generaed");
	  CellNames1.put(3, "Recall 1");
	  CellNames1.put(4, "Precision 1");
	  CellNames1.put(5, "FScore 1");
	  CellNames1.put(6, "Recall 2");
	  CellNames1.put(7, "Precision 2");
	  CellNames1.put(8, "FScore 2");
		
		XSSFCell[] Cells1 = new XSSFCell[9];
		for(Map.Entry<Integer, String> entry : CellNames1.entrySet())
			{Cells1[entry.getKey()] = xsrow1.createCell(entry.getKey());Cells1[entry.getKey()].setCellValue(entry.getValue());}
		
		
		//Sheet 2 Create First Row
		this.sheet2 = workbook.createSheet("Accuracy2");		
		XSSFRow xsrow2 = sheet2.createRow(0);
		CellNames2 = new HashedMap<Integer, String>();
		CellNames2.put(0, "Method");
		CellNames2.put(1, "Measure");
		CellNames2.put(2, "Rouge 1");
		CellNames2.put(3, "Rouge 2");
		
		XSSFCell[] Cells2 = new XSSFCell[4];
		for(Map.Entry<Integer, String> entry : CellNames2.entrySet())
			{Cells2[entry.getKey()] = xsrow2.createCell(entry.getKey());Cells2[entry.getKey()].setCellValue(entry.getValue());}
		
	}
	public void CreateSheet1(ArrayList<String> system_generated,DatasetFile datasetFile) throws IOException {
		try {
			for(int i=0;i<system_generated.size();i++)
			{
	 		AccuracyMeasures acMeasures = new AccuracyMeasures();
			XSSFRow xsrow = sheet1.createRow(i+1);
		         for(int j=0;j<9;j++)
		         {
		         	XSSFCell newcell = xsrow.createCell(j);
		         	if(newcell.getColumnIndex()==0)
		         		newcell.setCellValue(datasetFile.getArticles().get(i));
		         	else if(newcell.getColumnIndex() == 1)
		         		newcell.setCellValue(datasetFile.getSummaries().get(i));
		         	else if(newcell.getColumnIndex() == 2)
		         		newcell.setCellValue(system_generated.get(i));
		         	else if(newcell.getColumnIndex() == 3)
		         		{acMeasures.Rouge1(system_generated.get(i), datasetFile.getSummaries().get(i));newcell.setCellValue(acMeasures.getRecall());}
		         	else if(newcell.getColumnIndex() == 4)
		         		newcell.setCellValue(acMeasures.getPrecision());
		         	else if(newcell.getColumnIndex() == 5)
		         		newcell.setCellValue(acMeasures.getFScore());
		         	else if(newcell.getColumnIndex() == 6)
		         		{acMeasures.Rouge2(system_generated.get(i), datasetFile.getSummaries().get(i));newcell.setCellValue(acMeasures.getRecall());}
		         	else if(newcell.getColumnIndex() == 7)
		         		newcell.setCellValue(acMeasures.getPrecision());
		         	else if(newcell.getColumnIndex() == 8)
		         		newcell.setCellValue(acMeasures.getFScore());
		         }
	         }
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		CreateFile("Accuracy.xlsx");
	       
	}
	public void CreateFile(String SheetName) throws IOException {
		 try 
	       {
	       	FileOutputStream fileOutputStream = new FileOutputStream(new File(SheetName));
	       	this.workbook.write(fileOutputStream);
	       	/*fileOutputStream = new FileOutputStream(new File("Accuracy2.xlsx"));
	       	workbook.write(fileOutputStream);*/
	       	fileOutputStream.close();
	       	System.out.println("Excel file is Created");
	       }catch (FileNotFoundException ex) {}
	}
    /*for(int k=0;k<3;k++) {
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

}catch (Exception e) {System.err.println(e.getMessage());}*/
}