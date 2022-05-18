package file;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunPython {
	public String[] labels;
	
	public RunPython() {
		try {
			String path = "C:\\Users\\Abdullah Alaa\\ArabicTextSummarizer\\pythonProject\\predict.py";
			ProcessBuilder pb = new ProcessBuilder("C:\\Users\\Abdullah Alaa\\ArabicTextSummarizer\\pythonProject\\venv\\Scripts\\python.exe", path).inheritIO();
			Process p = pb.start();
			p.waitFor();

			BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = bfr.readLine()) != null) 
				labels=line.split(",");

//			System.out.println(labels);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
		}
	}
}
