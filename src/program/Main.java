package program;

import utilities.SentenceDetector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;

import featuresextraction.FeaturesExtraction;
import file.AccuracyFile;
import file.AverageFile;
import file.DatasetFile;
import file.ReadWriteToCSV;
import file.RunPython;
import preprocessing.Preprocessing1;

//import javafx.scene.control.Cell;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		try {
//			DatasetFile datasetFile = new DatasetFile();
//			AccuracyFile acFile;
//			AverageFile averageFile = new AverageFile();
//
//			ArrayList<String> TextRankSummary = new ArrayList<String>();
//			SentenceDetector sd = new SentenceDetector();
//
//			/*ReadWriteToCSV CSV_Writer = new ReadWriteToCSV("ML_Data.csv","ML_Data.csv");
//			CSV_Writer.Write(new String[][] { { "KPF", "KPL", "PNV", "First Sentence in First Paragraph",
//					"First sentence in last Paragraph", "First sentence in any of other paragraphs",
//					"Sentence location in other paragraphs", "Sentence location in first and last paragraph",
//					"cosine Similarity with title", "common keyphrases with title", "sentence centrality",
//					"sentence length is short/long", "sentence length equation", "cue phrases", "strong words",
//					"number scores", "sentence begins with weak word", "weak word score in other location in sentence",
//					"Label" } });
//*/
			RunPython py = new RunPython();
//			for (int i = 0; i < datasetFile.size(); i++) {
//				String Article = datasetFile.getArticles().get(i);
//				String Title = datasetFile.getTitles().get(i);
//				String Summary = datasetFile.getSummaries().get(i);
//
//				System.out.println("Summarizing Article " + Title + " #" + i);
//
//				// Testing Features !
//
//				testDataset(Article, Summary);
//				int SUMMARY_NO_SENTENCES = sd.detectSentences(Summary).length;
//				FeaturesExtraction fe = new FeaturesExtraction(Article, Title, Summary);
//				CSV_Writer.Write(fe.get_svm_vectors());
//
//				// End Testing
//				int SUMMARY_NO_SENTENCES = sd.detectSentences(Summary).length;
//				Textrank textrank = new Textrank(Article, Title, SUMMARY_NO_SENTENCES);
//				String GeneratedSummary = textrank.getSummarizedText();
//
//				TextRankSummary.add(GeneratedSummary);
//			}
//			acFile = new AccuracyFile("TextRank", averageFile);
//			acFile.CreateAccuracySheet(TextRankSummary, datasetFile);
//			acFile = new AccuracyFile("SVM", averageFile);
//			acFile.CreateAccuracySheet(TextRankSummary, datasetFile);
//			CSV_Writer.finalize();
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("Program Finished Successfully !");

	}

	public static void testDataset(String article, String summary) throws ClassNotFoundException, IOException {
//		SentenceDetector sd = new SentenceDetector();
//		String[] article_sentences = sd.detectSentences(article);
//		String[] summary_sentences = sd.detectSentences(summary);
		Preprocessing1 article_sentences = new Preprocessing1(article);
		Preprocessing1 summary_sentences = new Preprocessing1(summary);

		if (article_sentences.getNormalizedSentencesList().get(2)
				.equals(summary_sentences.getNormalizedSentencesList().get(0)))
			;
		System.out.println("correct");

		Set<String> map = new HashSet<String>();
		for (String sen : article_sentences.getNormalizedSentencesList())
			map.add(sen.trim());

//		String correctSummary = "";
		for (String sen : summary_sentences.getNormalizedSentencesList())
			if (!map.contains(sen.trim())) {
				System.out.println("احا");
				System.out.println(sen);
			}

	}

}
