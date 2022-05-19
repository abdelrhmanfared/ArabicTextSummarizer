package com;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import KPminer.FeatureExtractor;
import featuresextraction.FeaturesExtraction;
import file.ReadWriteToCSV;
import file.RunPython;
import textrank.Textrank;

/**
 * Servlet implementation class Working
 */
@WebServlet("/Working")
public class Working extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Working() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String text = request.getParameter("originalText");
		try {
			FeaturesExtraction Feature = new FeaturesExtraction(text, "اللغة العربية كاداة علمية");
			ReadWriteToCSV csv = new ReadWriteToCSV("pythonProject\\TextVectors.csv");
			csv.Write(new String[][] { { "KPF", "KPL", "PNV", "First Sentence in First Paragraph",
					"First sentence in last Paragraph", "First sentence in any of other paragraphs",
					"Sentence location in other paragraphs", "Sentence location in first and last paragraph",
					"cosine Similarity with title", "common keyphrases with title", "sentence centrality",
					"sentence length is short/long", "sentence length equation", "cue phrases", "strong words",
					"number scores", "sentence begins with weak word", "weak word score in other location in sentence",
					"Label" } });
			csv.Write(Feature.get_svm_vectors());
			csv.finalize();
			// Run python
			RunPython py = new RunPython();
			// genrate Summary from object Feature
			String summary = Feature.GenrateSVMSummary(py.labels);
			ProcessBuilder pb = new ProcessBuilder();
			request.setAttribute("original", text);
			request.setAttribute("result", summary);
			request.getRequestDispatcher("HomeJSP.jsp").forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
