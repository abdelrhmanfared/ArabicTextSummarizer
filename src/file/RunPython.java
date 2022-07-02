package file;

import java.io.*;  
import java.net.*;

import java.io.IOException;

public class RunPython {
//	public String[] labels;
	
	public static String[] getLabels(boolean isSVM) throws UnknownHostException, IOException
	{
		Socket socket=new Socket("localhost",1000);
		PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);	
//		PrintWriter writer= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8)));;
		
		//csv path
		String csv_path = System.getProperty("user.dir") + "\\TextVectors.csv";
		writer.println(csv_path);
		writer.flush();
		
		//teq
		writer.println(isSVM);
		writer.flush();
		
		DataInputStream inputStream = new DataInputStream(socket.getInputStream()); 
		String labels = (String)inputStream.readUTF();
		
	    socket.close();
	    
	    return labels.split(",");
	}
//	public RunPython() throws IOException, InterruptedException {
		
//		String programPath = "C:\\pythonProject\\predict.py";
//		String pythonExePath = "C:\\pythonProject\\venv\\Scripts\\python.exe";
//		ProcessBuilder pb = new ProcessBuilder(pythonExePath, programPath);
//		
//		Process p = pb.start();
//		/* int code = */p.waitFor();

//			System.out.println("Python Exit Code : " + code);

//		BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
////			BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//
//		String line = bfr.readLine();
//		 System.out.println(line);
//
//		if (labels != null)
//			this.labels = line.split(",");

//			while ((line = error.readLine()) != null) {
//				System.out.println(line);
//			}
//			System.out.println(labels);
//	}
}
