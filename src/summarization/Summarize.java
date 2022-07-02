package summarization;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import feature.*;
import file.ReadWriteToCSV;
import file.RunPython;
import preprocessing.Preprocessing1;

public class Summarize {
	private Preprocessing1 articlePre;

	private KeyPhrases f1;
	private SentenceLocation f2;
	private TitleSimilarity f3;
	private SentenceCenterality f4;
	private SentenceLength f5;
	private CueWords f6;
	private StrongWords f7;
	private NumberExistence f8;
	private WeakWords f9;

	public Summarize(String text, String title) throws ClassNotFoundException, IOException {
		// TODO Auto-generated constructor stub
		articlePre = new Preprocessing1(text);

		f1 = new KeyPhrases(articlePre);
		f2 = new SentenceLocation(articlePre);
		f3 = new TitleSimilarity(articlePre, title);
		f4 = new SentenceCenterality(articlePre);
		f5 = new SentenceLength(articlePre);
		f6 = new CueWords(articlePre);
		f7 = new StrongWords(articlePre);
		f8 = new NumberExistence(articlePre);
		f9 = new WeakWords(articlePre);
	}

	public String getScoreBasedSummary(int no_sentences) throws UnknownHostException, IOException {
		ArrayList<Score> sentences_scores = new ArrayList<Score>();

		for (int i = 0; i < articlePre.getOriginalSentencesList().size(); i++) {
			sentences_scores.add(new Score(i,
					f1.getScoreBasedFeature()[i] + f2.getScoreBasedFeature()[i] + f3.getScoreBasedFeature()[i]
							+ f4.getSVM_ScoreBasedFeature()[i] + f5.getScoreBasedFeature()[i]
							+ f6.getSVM_ScoreBasedFeature()[i] + f7.getScoreBasedFeature()[i]
							+ f8.getSVM_ScoreBasedFeature()[i] + f9.getScoreBasedFeature()[i]));
		}

		Collections.sort(sentences_scores, new Sortbyscore());
		
		int Summarylength = no_sentences;
		if(no_sentences == -1)
			Summarylength = (int) ((double)1/3 * articlePre.getOriginalSentencesList().size());

		Set<Integer> summaryIndices = new HashSet<Integer>();
		for (int i = 0; i < Summarylength; i++)
			summaryIndices.add(sentences_scores.get(i).index);

		String[] labels = new String[articlePre.getOriginalSentencesList().size()];
		Arrays.fill(labels, "0");
		for (int i : summaryIndices)
				labels[i] = "1";
		
		return getSummaryHelper(labels);
	}

	public String getSVMSummary() throws UnknownHostException, IOException {

		String[][] featureVectors = getFeatureVectors();
		writeVectorToCsv(featureVectors);

		String[] labels = RunPython.getLabels(true);

		return getSummaryHelper(labels);
	}

	public String getNNSummary() throws UnknownHostException, IOException {

		String[][] featureVectors = getFeatureVectors();
		writeVectorToCsv(featureVectors);

		String[] labels = RunPython.getLabels(false);

		return getSummaryHelper(labels);
	}

	private String getSummaryHelper(String[] labels) {
		String Summary = "";
		int Cnt = 0;

		for (int i = 0; i < articlePre.getParagraphsSentences().length; i++) {
			boolean check = false;
			for (int j = 0; j < articlePre.getParagraphsSentences()[i].length; j++) {
				Summary += (labels[Cnt].equals("1")) ? articlePre.getOriginalSentencesList().get(Cnt) + " " : "";
				check |= labels[Cnt++].equals("1");
			}
			if (check)
				Summary += "\n";
		}
		return Summary;
	}

	public String[][] getFeatureVectors() {
		// Machine learning feature Vector
		String[][] featureVectors = new String[articlePre.getOriginalSentencesList().size()][19]; // 0->18

		/*
		 * , "KPL", "PNV", "First Sentence in First Paragraph",
		 * "First sentence in last Paragraph",
		 * "First sentence in any of other paragraphs",
		 * "Sentence location in other paragraphs",
		 * "Sentence location in first and last paragraph",
		 * "cosine Similarity with title", "common keyphrases with title",
		 * "sentence centrality", "sentence length is short/long",
		 * "sentence length equation", "cue phrases", "strong words", "number scores",
		 * "sentence begins with weak word",
		 * "weak word score in other location in sentence", "Label"
		 */
		for (int i = 0; i < featureVectors.length; i++) {

			featureVectors[i][0] = String.valueOf(f1.getSvmFetures()[i][0]); // KPF
			featureVectors[i][1] = String.valueOf(f1.getSvmFetures()[i][1]); // KPL
			featureVectors[i][2] = String.valueOf(f1.getSvmFetures()[i][2]); // PNV
			featureVectors[i][3] = String.valueOf(f2.getSvmFetures()[i][0]); // First Sentence in First Paragraph
			featureVectors[i][4] = String.valueOf(f2.getSvmFetures()[i][1]); // First sentence in last Paragraph
			featureVectors[i][5] = String.valueOf(f2.getSvmFetures()[i][2]); // First sentence in any of other
																				// paragraphs
			featureVectors[i][6] = String.valueOf(f2.getSvmFetures()[i][3]); // Sentence location in other paragraphs
			featureVectors[i][7] = String.valueOf(f2.getSvmFetures()[i][4]); // Sentence location in first and last
																				// paragraph
			featureVectors[i][8] = String.valueOf(f3.getSvmFetures()[i][0]); // cosine Similarity with title
			featureVectors[i][9] = String.valueOf(f3.getSvmFetures()[i][1]); // common keyphrases with title
			featureVectors[i][10] = String.valueOf(f4.getSVM_ScoreBasedFeature()[i]); // sentence centrality
			featureVectors[i][11] = String.valueOf(f5.getSvmFetures()[i][1]); // sentence length is short/long
			featureVectors[i][12] = String.valueOf(f5.getSvmFetures()[i][0]); // sentence length equation
			featureVectors[i][13] = String.valueOf(f6.getSVM_ScoreBasedFeature()[i]); // cue phrases
			featureVectors[i][14] = String.valueOf(f7.getSvmFetures()[i]); // strong words
			featureVectors[i][15] = String.valueOf(f8.getSVM_ScoreBasedFeature()[i]); // number scores
			featureVectors[i][16] = String.valueOf(f9.getSvmFetures()[i][0]); // sentence begins with weak word
			featureVectors[i][17] = String.valueOf(f9.getSvmFetures()[i][1]); // weak word score in other location in
																				// sentence
		}

		return featureVectors;
	}

	public String[][] getFeatureVectors(List<String> refSummary) throws ClassNotFoundException, IOException {
		
		// Optimal Solution that requires without bad format
		/*
		 *
		 * Set<String> summary = new HashSet<String>(); for(String sen :
		 * pre.getNormalizedSentencesList()) summary.add(sen);
		 */

		String[][] featureVectors = getFeatureVectors();
		for (int i = 0; i < featureVectors.length; i++) {
			int found = 0;

			for (String summarySentence : refSummary)
				if (articlePre.getNormalizedSentencesList().get(i).contains(summarySentence)) {
					found = 1;
					break;
				}

			featureVectors[i][18] = String.valueOf(found); // Label (Sentence Exists in reference summary)
		}

		return featureVectors;
	}

	private void writeVectorToCsv(String[][] featureVectors) throws IOException {
		ReadWriteToCSV csv = new ReadWriteToCSV("TextVectors.csv");
		csv.Write(new String[][] {
				{ "KPF", "KPL", "PNV", "First Sentence in First Paragraph", "First sentence in last Paragraph",
						"First sentence in any of other paragraphs", "Sentence location in other paragraphs",
						"Sentence location in first and last paragraph", "cosine Similarity with title",
						"common keyphrases with title", "sentence centrality", "sentence length is short/long",
						"sentence length equation", "cue phrases", "strong words", "number scores",
						"sentence begins with weak word", "weak word score in other location in sentence", "Label" } });
		csv.Write(featureVectors);
		csv.finalize();
	}
}
