package file;

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

public class AverageFile {
	public XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet;
	Map<Integer, String> CellNames;
	String FilePath = "D:\\Git Projects\\ATS\\Average.xlsx";
	int AVGindex = 0;
	
	public AverageFile() throws IOException {
		this.sheet = workbook.createSheet("Average");		
		XSSFRow xsrow = sheet.createRow(0);
		CellNames = new HashedMap<Integer, String>();
		CellNames.put(0, "Method");
		CellNames.put(1, "Measure");
		CellNames.put(2, "Rouge 1");
		CellNames.put(3, "Rouge 2");
		
		XSSFCell[] Cells = new XSSFCell[4];
		for(Map.Entry<Integer, String> entry : CellNames.entrySet())
			{Cells[entry.getKey()] = xsrow.createCell(entry.getKey());Cells[entry.getKey()].setCellValue(entry.getValue());}
		CreateFile("Average.xlsx",-1,"",this.workbook);
	}
	public void CalculateAverage(Map<String, Float> AVGRNGram,ArrayList<String> system_generated,XSSFRow xsrow,String MethodName) throws IOException {
		int counter = 0;
		for(int i=0;i<3;i++) {
			if(AVGindex == 0){xsrow =  sheet.createRow(AVGindex+1);AVGindex=2;}
			else{xsrow = sheet.createRow(AVGindex);AVGindex++;}
			XSSFCell newcell = xsrow.createCell(0);
		    newcell.setCellValue(MethodName);
		    while(counter < 3) {
		    	newcell = xsrow.createCell(counter+1);
			if(i == 0)
			{if(newcell.getColumnIndex() == 1)newcell.setCellValue("Recall");
		    else if(newcell.getColumnIndex() == 2)newcell.setCellValue(AVGRNGram.get("Recall1")/system_generated.size());
		    else if(newcell.getColumnIndex() == 3)newcell.setCellValue(AVGRNGram.get("Recall2")/system_generated.size());
			}
			else if(i == 1)
			{if(newcell.getColumnIndex() == 1)newcell.setCellValue("Precision");
	        else if(newcell.getColumnIndex() == 2)newcell.setCellValue(AVGRNGram.get("Precision1")/system_generated.size());
	        else if(newcell.getColumnIndex() == 3)newcell.setCellValue(AVGRNGram.get("Precision2")/system_generated.size());
			}
			else if(i == 2) 
			{if(newcell.getColumnIndex() == 1)newcell.setCellValue("FScore");
	        else if(newcell.getColumnIndex() == 2)newcell.setCellValue(AVGRNGram.get("FScore1")/system_generated.size());
	        else if(newcell.getColumnIndex() == 3)newcell.setCellValue(AVGRNGram.get("FScore2")/system_generated.size());
			}counter++;}
		    counter = 0;
		}
		CreateFile(MethodName, 0, this.FilePath, this.workbook);
	}
	public void CreateFile(String MethodName,int update,String Path,XSSFWorkbook workbook) throws IOException {
		 try{FileOutputStream fileOutputStream = (update == -1)? new FileOutputStream(new File(MethodName)):new FileOutputStream(Path);
	       	workbook.write(fileOutputStream);
	       	fileOutputStream.close();
	       	System.out.println(MethodName +" Method is Added Successfully");
	       }catch (FileNotFoundException ex) {}
	}
}
