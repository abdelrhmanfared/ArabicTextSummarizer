package file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class ReadWriteToCSV {
	private FileWriter outputfile;
	private CSVWriter writer;
	private File file;

	public ReadWriteToCSV(String filePath) throws IOException {
		// create FileWriter object with file as parameter
		outputfile = new FileWriter(filePath);

		// create CSVWriter object filewriter object as parameter
		writer = new CSVWriter(outputfile);

		// first create file object for file placed at location
		// specified by filepath
		file = new File(filePath);
	}

	public void Write(String[][] lines) {

		for (String[] line : lines)
			writer.writeNext(line);
	}

	public void Read() {

	}

	public void finalize() throws IOException {
		// resources to be close
		// closing writer connection
		writer.close();
	}
}
