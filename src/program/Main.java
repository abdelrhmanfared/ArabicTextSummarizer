package program;

import textrank.Textrank;
import utilities.SentenceDetector;
import java.util.ArrayList;

import Files.AccuracyFile;
import Files.DatasetFile;

//import javafx.scene.control.Cell;
public class Main {

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		try {
			// PREPROCESSING TESTING !!!
			// String ArabicText = ArabicTextFile.getArabicText();
			// Preprocessing1 pre1 = new Preprocessing1(ArabicText);
			// Preprocessing pre = new Preprocessing(ArabicText);

			DatasetFile datasetFile = new DatasetFile();
			AccuracyFile acFile = new AccuracyFile();

			ArrayList<String> TextRankSummary = new ArrayList<String>();
			SentenceDetector sd = new SentenceDetector();

			for (int i = 0; i < datasetFile.size(); i++) {
				String Article = datasetFile.getArticles().get(i);
				String Title = datasetFile.getTitles().get(i);
				String Summary = datasetFile.getSummaries().get(i);
				
				System.out.println("Summarizing Article " + Title + " #" + i);


				int SUMMARY_NO_SENTENCES = sd.detectSentences(Summary).length;
				Textrank textrank = new Textrank(Article, Title, SUMMARY_NO_SENTENCES);
				String GeneratedSummary = textrank.getSummarizedText();
				
				TextRankSummary.add(GeneratedSummary);
			}

			acFile.CreateSheet1(TextRankSummary, datasetFile);

			// accuracy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
