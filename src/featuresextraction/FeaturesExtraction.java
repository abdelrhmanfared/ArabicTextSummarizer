package featuresextraction;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import feature.*;
import preprocessing.Preprocessing1;

public class FeaturesExtraction {
	private String[][] vector;
	private Preprocessing1 articlePre;

	public FeaturesExtraction(String text, String title) throws ClassNotFoundException, IOException {
		// TODO Auto-generated constructor stub
		articlePre = new Preprocessing1(text);

		KeyPhrases f1 = new KeyPhrases(articlePre);
		SentenceLocation f2 = new SentenceLocation(articlePre);
		TitleSimilarity f3 = new TitleSimilarity(articlePre, title);
		SentenceCenterality f4 = new SentenceCenterality(articlePre);
		SentenceLength f5 = new SentenceLength(articlePre);
		CueWords f6 = new CueWords(articlePre);
		StrongWords f7 = new StrongWords(articlePre);
		NumberExistence f8 = new NumberExistence(articlePre);
		WeakWords f9 = new WeakWords(articlePre);

		// SVM Vector
		vector = new String[articlePre.getOriginalSentencesList().size()][19]; // 0->18

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
		for (int i = 0; i < vector.length; i++) {

			vector[i][0] = String.valueOf(f1.getSvmFetures()[i][0]); // KPF
			vector[i][1] = String.valueOf(f1.getSvmFetures()[i][1]); // KPL
			vector[i][2] = String.valueOf(f1.getSvmFetures()[i][2]); // PNV
			vector[i][3] = String.valueOf(f2.getSvmFetures()[i][0]); // First Sentence in First Paragraph
			vector[i][4] = String.valueOf(f2.getSvmFetures()[i][1]); // First sentence in last Paragraph
			vector[i][5] = String.valueOf(f2.getSvmFetures()[i][2]); // First sentence in any of other paragraphs
			vector[i][6] = String.valueOf(f2.getSvmFetures()[i][3]); // Sentence location in other paragraphs
			vector[i][7] = String.valueOf(f2.getSvmFetures()[i][4]); // Sentence location in first and last paragraph
			vector[i][8] = String.valueOf(f3.getSvmFetures()[i][0]); // cosine Similarity with title
			vector[i][9] = String.valueOf(f3.getSvmFetures()[i][1]); // common keyphrases with title
			vector[i][10] = String.valueOf(f4.getSVM_ScoreBasedFeature()[i]); // sentence centrality
			vector[i][11] = String.valueOf(f5.getSvmFetures()[i][1]); // sentence length is short/long
			vector[i][12] = String.valueOf(f5.getSvmFetures()[i][0]); // sentence length equation
			vector[i][13] = String.valueOf(f6.getSVM_ScoreBasedFeature()[i]); // cue phrases
			vector[i][14] = String.valueOf(f7.getSvmFetures()[i]); // strong words
			vector[i][15] = String.valueOf(f8.getSVM_ScoreBasedFeature()[i]); // number scores
			vector[i][16] = String.valueOf(f9.getSvmFetures()[i][0]); // sentence begins with weak word
			vector[i][17] = String.valueOf(f9.getSvmFetures()[i][1]); // weak word score in other location in sentence
		}
	}

	public FeaturesExtraction(String text, String title, String refSummary) throws ClassNotFoundException, IOException {
		this(text, title);

		Preprocessing1 summaryPre = new Preprocessing1(refSummary);
		// Optimal Solution that requires without bad format
		/*
		 *
		 * Set<String> summary = new HashSet<String>(); for(String sen :
		 * pre.getNormalizedSentencesList()) summary.add(sen);
		 */

		for (int i = 0; i < vector.length; i++) {
			int found = 0;

			for (String summarySentence : summaryPre.getNormalizedSentencesList())
				if (articlePre.getNormalizedSentencesList().get(i).contains(summarySentence)) {
					found = 1;
					break;
				}

			vector[i][18] = String.valueOf(found); // Label (Sentence Exists in reference summary)
		}
	}

	public String GenrateSVMSummary(String [][] vector) {
		
		String Summary = ""; 
		int Cnt = 0;
		for (int i=0;i<articlePre.getParagraphsSentences().length;i++) {
			for (int j=0;j<articlePre.getParagraphsSentences()[i].length;j++) {
				Summary +=(vector[Cnt++][18].equals("1"))?articlePre.getOriginalSentencesList().get(Cnt):"";				
			}
			Summary+="\n";
		}
		return Summary;
	}

	public String[][] get_svm_vectors() {
		return vector;
	}

}
