package Files;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class DatasetFile {
	private ArrayList<String> Articles;
	private ArrayList<String> Summaries;
	private ArrayList<String> Titles;

	public DatasetFile() throws IOException {
		String path = System.getProperty("user.dir") + "\\EASC\\";
		InputStream fileInputStream = new FileInputStream(path + "Titles.txt");
		Reader reader = new InputStreamReader(fileInputStream, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);

		ArrayList<String> FileName = new ArrayList<String>();
		Titles = new ArrayList<String>();
		Articles = new ArrayList<String>();
		Summaries = new ArrayList<String>();

		String T = "";
		while ((T = bufferedReader.readLine()) != null) {
			String[] v = T.split("\\*");
			FileName.add(v[0]);
			if (v.length > 1) {
				Titles.add(v[1]);
			}
		}
		bufferedReader.close();
		reader.close();
		fileInputStream.close();

		for (int i = 0; i < FileName.size(); i++) {
			Articles.add(readall(path + "\\Articles\\" + FileName.get(i) + ".txt"));
			Summaries.add(readall(path + "\\Summaries\\" + FileName.get(i) + "_A.txt"));
		}

	}

	private String readall(String fullpath) throws IOException {
		InputStream fs = new FileInputStream(fullpath);
		Reader rd = new InputStreamReader(fs, "UTF-8");
		BufferedReader bf = new BufferedReader(rd);

		String tmp = "", s = "";
		while ((s = bf.readLine()) != null)
			tmp += s;

		bf.close();
		rd.close();
		fs.close();

		return tmp;
	}

	public ArrayList<String> getArticles() {
		return Articles;
	}

	public ArrayList<String> getSummaries() {
		return Summaries;
	}

	public ArrayList<String> getTitles() {
		return Titles;
	}

	public int size()
	{
		return Articles.size();
	}
}
