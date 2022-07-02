package program;

import utilities.SentenceDetector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;

import file.AccuracyFile;
import file.ArabicTextFile;
import file.AverageFile;
import file.DatasetFile;
import file.ReadWriteToCSV;
import file.RunPython;
import preprocessing.Preprocessing1;
import summarization.Summarize;

//import javafx.scene.control.Cell;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Preprocessing1 pre = new Preprocessing1();
			
			DatasetFile datasetFile = new DatasetFile();
			AccuracyFile acFile;
			AverageFile averageFile = new AverageFile();

			ReadWriteToCSV CSV_Writer = new ReadWriteToCSV("ML_Data.csv");
			CSV_Writer.Write(new String[][] { { "KPF", "KPL", "PNV", "First Sentence in First Paragraph",
					"First sentence in last Paragraph", "First sentence in any of other paragraphs",
					"Sentence location in other paragraphs", "Sentence location in first and last paragraph",
					"cosine Similarity with title", "common keyphrases with title", "sentence centrality",
					"sentence length is short/long", "sentence length equation", "cue phrases", "strong words",
					"number scores", "sentence begins with weak word", "weak word score in other location in sentence",
					"Label" } });

			
			List<List<String>> ScoreBasedSummaries = new ArrayList<List<String>>();
			List<List<String>> svmSummaries = new ArrayList<List<String>>();
			List<List<String>> nnSummaries = new ArrayList<List<String>>();
			List<List<String>> refSummaries = new ArrayList<List<String>>();

			for (int i = 0; i < datasetFile.size(); i++) {
				String Article = datasetFile.getArticles().get(i);
				String Title = datasetFile.getTitles().get(i);
				String Summary = datasetFile.getSummaries().get(i);
//
				System.out.println("Summarizing Article " + Title + " #" + i);
//
//				// Testing Features !
//
//				testDataset(Article, Summary);
				int SUMMARY_NO_SENTENCES = Preprocessing1.sd.detectSentences(Summary).length;
				Summarize summarize = new Summarize(Article, Title);		
				
				List<String> norm_summary = norm(Summary);
				norm_summary=fixSummary(norm_summary, norm(Article));
				
				CSV_Writer.Write(summarize.getFeatureVectors(norm_summary));

//				// End Testing
//				int SUMMARY_NO_SENTENCES = sd.detectSentences(Summary).length;
				ScoreBasedSummaries.add(norm(summarize.getScoreBasedSummary(SUMMARY_NO_SENTENCES)));
				svmSummaries.add(norm(summarize.getSVMSummary()));
				nnSummaries.add(norm(summarize.getNNSummary()));
				refSummaries.add(norm_summary);
				
			}
			acFile = new AccuracyFile("TextRank", averageFile);
			acFile.CreateAccuracySheet(ScoreBasedSummaries, refSummaries);
			acFile = new AccuracyFile("SVM", averageFile);
			acFile.CreateAccuracySheet(svmSummaries, refSummaries);
			acFile = new AccuracyFile("NN", averageFile);
			acFile.CreateAccuracySheet(nnSummaries, refSummaries);
			CSV_Writer.finalize();
//
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Program Finished Successfully !");

	}

	public static List<String> norm(String text) throws FileNotFoundException{
		List<String> normalizedSentences = new ArrayList<String>();
//		String[] sentences = Preprocessing1.sd.detectSentences(addSpacesAroundPeriods(text));
		String[] sentences = Preprocessing1.sd.detectSentences(text);
		int NO_SENTENCES = sentences.length;

		for (int j = 0; j < NO_SENTENCES; j++) {
			String normalizedSentence = Preprocessing1.arn.normalize(sentences[j]);
			normalizedSentence = Preprocessing1.dr.removeDiacritics(normalizedSentence);
			normalizedSentence = Preprocessing1.pr.removePunctuations(normalizedSentence);
			
			normalizedSentences.add(normalizedSentence.trim());
		}
		
		return normalizedSentences;
	}
	
	public static List<String> fixSummary(List<String> refSummary, List<String> article){
		List<String> fixedSummary = new ArrayList<String>();
		
		for(String summSen : refSummary)
			for(String artSen : article)
				if(artSen.contains(summSen))
					fixedSummary.add(artSen);
		
		return fixedSummary;
	}
	
//	public static String addSpacesAroundPeriods(String arabicText) {
//		if(arabicText.equals(""))
//			return arabicText;
//		String Temp = "";
//		Temp += arabicText.charAt(0);
//		for (int i = 1; i < arabicText.length() - 1; i++) {
//			if (arabicText.charAt(i) == '.') {
//				char charBefore = arabicText.charAt(i - 1);
//				char charAfter = arabicText.charAt(i + 1);
//
//				if (charBefore != '.' && charBefore != ' ')
//					Temp += ' ';
//
//				Temp += '.';
//
//				if (charAfter != '.' && charAfter != ' ')
//					Temp += ' ';
//			} else {
//				Temp += arabicText.charAt(i);
//			}
//		}
//		if (arabicText.charAt(arabicText.length() - 2) != ' ')
//			Temp += ' ';
//		Temp += arabicText.charAt(arabicText.length() - 1);
//
//		return Temp;
//	}
//	public static void testDataset(String article, String summary) throws ClassNotFoundException, IOException {
////		SentenceDetector sd = new SentenceDetector();
////		String[] article_sentences = sd.detectSentences(article);
////		String[] summary_sentences = sd.detectSentences(summary);
//		List<String> article_sentences = new Preprocessing1(article).getNormalizedSentencesList();
//		List<String> summary_sentences = new Preprocessing1(summary).getNormalizedSentencesList();
//
////		if (article_sentences.getNormalizedSentencesList().get(2)
////				.equals(summary_sentences.getNormalizedSentencesList().get(0)))
////			;
////		System.out.println("correct");
//
//		Set<String> map = new HashSet<String>();
//		for (String sen : article_sentences)
//			map.add(sen.trim());
//
////		String correctSummary = "";
//		for (String sen : summary_sentences)
//			if (!map.contains(sen.trim())) {
//				System.out.println("Error");
//				System.out.println(sen);
//			}
//
//	}

}
