package file;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunPython {
		
	public RunPython() {
		try {
		String path = "C:\\Users\\Abdullah Alaa\\PycharmProjects\\untitled1\\venv\\hi.py";
		ProcessBuilder pb = new ProcessBuilder("C:\\Users\\Abdullah Alaa\\PycharmProjects\\untitled1\\venv\\Scripts\\python.exe", path);
		Process p = pb.start();
		p.waitFor();

		/*		BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = bfr.readLine()) != null) {
				System.out.println(line);
*/
		}
		 catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
		}
	}
}
