package file;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class RunPython {
	public String[] labels;

	public RunPython() {
		try {
			String programPath = System.getProperty("user.dir") + "\\pythonProject\\predict.py";
			String pythonExePath = System.getProperty("user.dir") + "\\pythonProject\\venv\\Scripts\\python.exe";

			ProcessBuilder pb = new ProcessBuilder(pythonExePath, programPath);
			Process p = pb.start();

			int code = p.waitFor();
			System.out.println("Python Exit Code : " + code);

			BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			String line = bfr.readLine();
			System.out.println(line);

			if (line != null)
				labels = line.split(",");

			while ((line = error.readLine()) != null) {
				System.out.println(line);
			}
//			System.out.println(labels);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
		}
	}
}
