package com;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import file.ArabicTextFile;
import summarization.Summarize;

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
		long startTime = System.currentTimeMillis();

		request.setCharacterEncoding("UTF-8");
		String text = request.getParameter("originalText");
		try {
			long startTime2 = System.currentTimeMillis();

			// Feature Extraction
			String title = ArabicTextFile.Title;
			Summarize summarize = new Summarize(text, title);

			long stopTime2 = System.currentTimeMillis();
			System.out.println("Feature Extraction Time : " + (stopTime2 - startTime2) + "ms");

			// Sumarization
			String teq = request.getParameter("TechniquesBox");
			String summary = "";

			if (teq.equals("Score Based"))
				summary = summarize.getScoreBasedSummary(-1);// score
			else if (teq.equals("SVM"))
				summary = summarize.getSVMSummary();
			else
				summary = summarize.getNNSummary();// nn

			request.setAttribute("original", text);
			request.setAttribute("result", summary);

			long stopTime = System.currentTimeMillis();
			System.out.println("Run Time : " + (stopTime - startTime) + "ms");

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
