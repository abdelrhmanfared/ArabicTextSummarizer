package file;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ArabicTextFile {
	public static String Title = "اللغة العربية كأداة علمية";
	public static String getArabicText() throws IOException {
		String ArabicText = readfile("ArabicText.txt");
		return ArabicText;
	}

	public static String readfile(String filename) throws IOException {
		File myObj = new File(filename);
		Scanner myReader = new Scanner(myObj);
		myReader.useDelimiter("\\Z");
		String data = myReader.next();
		myReader.close();
		return data;
	}
}
