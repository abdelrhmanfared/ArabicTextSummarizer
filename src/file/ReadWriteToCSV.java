package file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class ReadWriteToCSV {
	private FileWriter outputfile;
//	private FileReader filereader;
	private CSVWriter writer;
//	private CSVReader csvReader;
	private File file;

	public ReadWriteToCSV(String filePath_Write)  throws IOException {
		// create FileWriter object with file as parameter
		outputfile = new FileWriter(filePath_Write);
//		filereader = new FileReader(filePath_Read);
        // create csvReader object passing
        // file reader as a parameter
//		csvReader = new CSVReader(filereader);
 
		// create CSVWriter object filewriter object as parameter
		writer = new CSVWriter(outputfile);

		// first create file object for file placed at location
		// specified by filepath
		file = new File(filePath_Write);
	}

	public void Write(String[][] lines) {

		for (String[] line : lines)
			writer.writeNext(line);
	}

//	public String[][] Read() throws IOException, CsvException {
//        List<String[]> allData = csvReader.readAll();
//        return allData.toArray(new String[allData.size()][]);
//	}
	public void finalize() throws IOException {
		// resources to be close
		// closing writer connection
		writer.close();
	}
}
