package textrank;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import preprocessing.Preprocessing1;
import utilities.StanfordPOSTagger;

public class Textrank {
	private String summarizedText;
	private Preprocessing1 pre;

	public String getSummarizedText() {
		return summarizedText;
	}

	public Textrank(String text, String title, int no_sentences) throws Exception {
		pre = new Preprocessing1(text);
		summarizedText = "";

		// Parameters
		if (pre.getOriginalSentences().length == 1) {
			summarizedText = pre.getOriginalSentences()[0];
			return;
		}

		if (no_sentences > pre.getOriginalSentences().length)
			throw new Exception("LENGTHS NOT THE SAME!");

		// Extract features
		//double[] keyPhrases = keyPhrases(pre.getLight10Sentences());
		double[] sentenceLocation = sentencelocation(pre.getParagraphsSentences(), pre.getOriginalSentences().length);
		//double[] titleSimilarity = similarityWithTitle(pre.getLight10Sentences(), pre.getTokens(),
				//pre.getLight10SentencesTokens(), title, pre.KpMinnerWords(7));
		double[] senCentrality = sentenceCentrality(pre.getRootSentences(),pre.getRootTokens(), pre.getRootSentencesTokens());
		double[] senLength = sentenceLength(pre.getRootSentencesTokens());
		double[] cuePhrases = cueWords(pre.getLight10Sentences());
		double[] strongWords = positiveKeyWords(pre.getLight10Sentences());
		double[] numberScores = numberScore(pre.getRootSentences(), pre.getRootSentencesTokens());
		double[] weakWords = WeakWords_Scoring(pre.getLight10Sentences());

		// Ranking
		ArrayList<Score> sentences_scores = new ArrayList<Score>();

		for (int i = 0; i < pre.getOriginalSentences().length; i++) {
			sentences_scores.add(new Score(i, /*keyPhrases[i] +*/ sentenceLocation[i] + //titleSimilarity[i] +
			 senCentrality[i] +  senLength[i] + cuePhrases[i] + strongWords[i] + numberScores[i] + weakWords[i]));
		}

		Collections.sort(sentences_scores, new Sortbyscore());

		int Summarylength = no_sentences;
		if (no_sentences == -1)
			Summarylength = (int) ((double) 1 / 3 * pre.getOriginalSentences().length);

		ArrayList<Score> summary = new ArrayList<Score>();
		for (int i = 0; i < Summarylength; i++)
			summary.add(sentences_scores.get(i));
		Collections.sort(summary, new Sortbyindex());

		for (Score score : summary)
			summarizedText += pre.getOriginalSentences()[score.index];

	}

	public double[][] Key_phreases_SVM(String[] Sentences) {
		String Words[] = pre.KpMinnerWords(7);
		double sentanceScore[][] = new double[Sentences.length][3];
		// function to get key phrase frequency
		double kpF[] = KeyphraseFrequency(Sentences, Words);
		int kpL[] = KeyphraseLength(Words);
		int ProperName[] = ProperName(Words);
		return KpScoreSVM(kpL, ProperName, kpF, Words, Sentences);
	}

// A short list of important terms that provide a condensed summary of the main
	// topics of a document
	private double[] keyPhrases(String[] Sentences) {
		String Words[] = pre.KpMinnerWords(7);
		// function to get key phrase frequency
		// key phrase frequency
		double kpF[] = KeyphraseFrequency(Sentences, Words);
		// key phrase length
		int kpL[] = KeyphraseLength(Words);
		// proper names
		int ProperName[] = ProperName(Words);
		return KpScore(kpL, ProperName, kpF, Words, Sentences);
	}

	private double[] KeyphraseFrequency(String[] sentences, String[] words) {
		int totalKPF = 0;
		int SentanceScore[] = new int[words.length];
		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < words.length; j++) {
				if (sentences[i].contains(" " + words[j] + " ")) {
					totalKPF++;
					SentanceScore[j]++;
				}
			}
		}
		double Score[] = new double[words.length];
		if (totalKPF == 0)
			return Score;
		for (int i = 0; i < Score.length; i++)
			Score[i] = (SentanceScore[i] * 1.0 / totalKPF);

		// TODO Auto-generated method stub
		return Score;
	}

	private int[] KeyphraseLength(String[] words) {
		int kpL[] = new int[words.length];
//		return length of number of words consist the key phrase
		for (int i = 0; i < words.length; i++) {
			String s[] = words[i].split(" ");
			kpL[i] = s.length;
		}
		return kpL;
	}

	private int[] ProperName(String[] words) {

		// POS Tagging
// In English data, determiners (DT), nouns (NN, NNS, NNP etc.),
// verbs (VB, VBD, VBP etc.), prepositions (IN) might be more frequent.

		int pN[] = new int[words.length];
		try {
			StanfordPOSTagger stf = new StanfordPOSTagger();
			String txt = pre.getOrginal();
			String pos = stf.tagText(txt);
			pos = pre.arn.normalize(pos);
			pos = pre.dr.removeDiacritics(pos);
			String Proper_Names[] = pos.split("/|\\s");
			Arrays.fill(pN, 1);
			for (int i = 0; i < Proper_Names.length; i++) {
				Proper_Names[i] = pre.ls10.findStem(Proper_Names[i]);
				for (int j = 0; j < words.length; j++) {
					if (Proper_Names[i].contains(" " + words[j] + " ")) {
						if (Proper_Names[i].equals("NNP") || Proper_Names[i].equals("NNPS"))
							pN[j] = 2;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return pN;
	}

	private double[] KpScore(int[] kpL, int[] ProperName, double[] kpF, String[] words, String[] sentences) {
		double KpScore[] = new double[words.length];
		for (int i = 0; i < words.length; i++) {
			KpScore[i] = ProperName[i] * kpF[i] * 1.0 * Math.sqrt(kpL[i]);
		}
		double SentanceKpScore[] = new double[words.length];

		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < words.length; j++) {
				if (sentences[i].contains(" " + words[j] + " ")) {
					SentanceKpScore[i] += KpScore[j];
				}
			}
		}
		return SentanceKpScore;
	}

	private double[][] KpScoreSVM(int[] kpL, int[] ProperName, double[] kpF, String[] words, String[] sentences) {
		double SentanceKpScore[][] = new double[words.length][3];

		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < words.length; j++) {
				if (sentences[i].contains(" " + words[j] + " ")) {
					SentanceKpScore[i][0] += kpF[j];
					SentanceKpScore[i][1] = (kpL[j] >= 2) ? 1 : 0;
					SentanceKpScore[i][2] = --ProperName[j];
				}
			}
		}
		return SentanceKpScore;
	}

	// Relating to the position of a sentence to the paragraph and document
	public double[] sentencelocation(String[][] paragraphsSentences, int NO_SENTENCES) {
		double[] SentenceLocationScores = new double[NO_SENTENCES];
		int lastParagraphStart = NO_SENTENCES - paragraphsSentences[paragraphsSentences.length - 1].length;

		// FIRST SENTENCES
		// last parag
		SentenceLocationScores[lastParagraphStart] = 2.0;

		// first parag
		SentenceLocationScores[0] = 3.0;

		// rem parags
		for (int i = 0, ind = 0; i < paragraphsSentences.length - 2; i++) {
			ind += paragraphsSentences[i].length;
			SentenceLocationScores[ind] = 1;
		}

		// PARAGRAPHS
		// first
		for (int i = 1; i < paragraphsSentences[0].length; i++)
			SentenceLocationScores[i] = 1.0 / Math.sqrt((double) i + 1.0);

		// last
		for (int i = 1; i < paragraphsSentences[paragraphsSentences.length - 1].length; i++)
			SentenceLocationScores[lastParagraphStart + i] = 1.0 / Math.sqrt((double) i + 1.0);

		// rem
		int ind = paragraphsSentences[0].length;
		for (int i = 1; i < paragraphsSentences.length - 1; i++) {
			ind++;
			for (int j = 1; j < paragraphsSentences[i].length; j++) {
				SentenceLocationScores[ind++] = 1.0 / Math.sqrt(((double) j + 1.0) + (double) ((i + 1) * (i + 1)));
			}
		}

		return SentenceLocationScores;
	}
	
	public double[][] sentencelocationSVM(String[][] paragraphsSentences, int NO_SENTENCES)
	{
		double[][] SentenceLocationScores = new double[NO_SENTENCES][5];
		int lastParagraphStart = NO_SENTENCES - paragraphsSentences[paragraphsSentences.length - 1].length;
		
		// FIRST SENTENCES
				// first parag
				SentenceLocationScores[0][0] = 1.0;
				
				// last parag
				SentenceLocationScores[lastParagraphStart][1] = 1.0;


				// rem parags
				for (int i = 0, ind = 0; i < paragraphsSentences.length - 2; i++) {
					ind += paragraphsSentences[i].length;
					SentenceLocationScores[ind][2] = 1.0;
				}

				// PARAGRAPHS
				// first
				for (int i = 1; i < paragraphsSentences[0].length; i++)
					SentenceLocationScores[i][4] = 1.0 / Math.sqrt((double) i + 1.0);

				// last
				for (int i = 1; i < paragraphsSentences[paragraphsSentences.length - 1].length; i++)
					SentenceLocationScores[lastParagraphStart + i][4] = 1.0 / Math.sqrt((double) i + 1.0);

				// rem
				int ind = paragraphsSentences[0].length;
				for (int i = 1; i < paragraphsSentences.length - 1; i++) {
					ind++;
					for (int j = 1; j < paragraphsSentences[i].length; j++) {
						SentenceLocationScores[ind++][3] = 1.0 / Math.sqrt(((double) j + 1.0) + (double) ((i + 1) * (i + 1)));
					}
				}

				return SentenceLocationScores;
	}

	public double[] cosineTitle(String[] titleTokens, String[] token, String[] sentences, String[][] sentenceTokens) {
		int new_len = token.length + titleTokens.length;
		String[] tokens = new String[new_len];
		for (int i = 0; i < new_len; i++) {
			if (i < token.length) {
				tokens[i] = token[i];
			} else {
				for (int ii = 0; ii < titleTokens.length; ii++) {
					tokens[i] = titleTokens[ii];
				}
			}
		}

		Set<String> words = new HashSet<String>();
		for (String string : tokens) {
			words.add(string);
		}
		String[] WORDS = words.toArray(new String[words.size()]);

		double[][] tf = new double[sentences.length + 1][WORDS.length];
		double[] isf = new double[WORDS.length];
		double[] cosineScore = new double[sentences.length];
		int countTF = 0;
		int countISF = 0;

		for (int i = 0; i < WORDS.length; i++) {
			countISF = 0;
			for (int j = 0; j < sentences.length + 1; j++) {
				boolean found = false;
				countTF = 0;
				if (j < sentences.length) {
					for (int k = 0; k < sentenceTokens[j].length; k++) {
						if (sentenceTokens[j][k].matches(WORDS[i])) {
							countTF = countTF + 1;
							found = true;
						}
					}
				} else if (j == sentences.length) {
					for (int k = 0; k < titleTokens.length; k++) {
						if (titleTokens[k].matches(WORDS[i])) {
							countTF = countTF + 1;
							found = true;
						}
					}
				}
				if (found == true) {
					countISF = countISF + 1;
				}
				if (countTF != 0) {
					tf[j][i] = 1 + Math.log10(Double.valueOf(countTF));
				} else if (countTF == 0) {
					tf[j][i] = 0;
				}
			}
			if (countISF != 0) {
				isf[i] = Math.log10(sentences.length + 1 / countISF);
			} else if (countISF == 0) {
				isf[i] = 0;
			}
		}

		for (int i = 0; i < sentences.length; i++) {
			double sum = 0, sqrt1 = 0, sqrt2 = 0;
			for (int j = 0; j < WORDS.length; j++) {
				sum += tf[i][j] * tf[sentences.length][j] * isf[j] * isf[j];
				sqrt1 += (tf[i][j] * isf[j]) * (tf[i][j] * isf[j]);
				sqrt2 += (tf[sentences.length][j] * isf[j]) * (tf[sentences.length][j] * isf[j]);
			}

			cosineScore[i] = sum / (Math.sqrt(sqrt1) * Math.sqrt(sqrt2));
		}
		return cosineScore;
	}

	public double[] similarityWithTitle(String[] sentences_light, String[] token, String[][] sentenceTokens,
			String fileName, String[] KeyPhrases) throws IOException, ClassNotFoundException {
		String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
		String[] title = tokens[0].split(" ");

		for (int i = 0; i < title.length; i++) {
			title[i] = pre.arn.normalize(title[i]);
			title[i] = pre.dr.removeDiacritics(title[i]);
			title[i] = pre.ls10.findStem(title[i]);
		}

		double[] sentence_TitleCosineScores = cosineTitle(title, token, sentences_light, sentenceTokens);
		int[] CommonKeyPhrasewithTitle = new int[sentences_light.length];

		for (int i = 0; i < sentences_light.length; i++) {
			for (int j = 0; j < title.length; j++) {
				boolean exist = false;
				if (!title[j].equals(" ")) {
					if (sentences_light[i].matches(".*\\b" + title[j] + "\\b.*")) {
						for (int k = 0; k < KeyPhrases.length; k++) {
							if (KeyPhrases[k].matches(".*\\b" + title[j] + "\\b.*")) {
								exist = true;
								break;
							}
						}
						if (exist == true) {
							CommonKeyPhrasewithTitle[i]++;
						}
					}
				}
			}
		}
		double[] sentence_scores = new double[sentences_light.length];
		for (int i = 0; i < sentence_scores.length; i++) {
			sentence_scores[i] = Math.sqrt(1 + CommonKeyPhrasewithTitle[i]);
		}

		double[] TitleSimilarityScores = new double[sentences_light.length];
		for (int i = 0; i < sentences_light.length;) {
			TitleSimilarityScores[i] = sentence_TitleCosineScores[i] * sentence_scores[i];
		}

		return TitleSimilarityScores;
	}

	// The similarity or the overlapping between a sentence and other sentences in
	// the document
private double[] sentenceCentrality(String[] sentences, String[] tokens, String[][] senTokens) throws FileNotFoundException, IOException {
		
		double Threshold = 0.2;
		double [][] CosineSimMatrix;
		double []CentralityMatrix = new double[sentences.length];
		CosineSimlarity cosSim = new CosineSimlarity(pre.getRootSentencesTokens());
		CosineSimMatrix = cosSim.getCosineBOW();
		/*for(int i=0;i<CosineSimMatrix.length;i++)
		{
			for(int j=0;j<CosineSimMatrix[i].length;j++)
				System.out.print(Math.round(CosineSimMatrix[i][j]*100.0)/100.0+" ");
			System.out.println();
		}*/
				
		Map<String, Integer> SimlarityDegree = new HashMap<String, Integer>();
		double MaxDegree = 0;
		int SentenceDegree = 0;
		for(int i=0;i<CosineSimMatrix.length;i++)
		{
			for(int j=0;j<CosineSimMatrix[i].length;j++)
				if(CosineSimMatrix[i][j] >= Threshold)
					SentenceDegree++;
		
			SimlarityDegree.put(sentences[i], SentenceDegree);
			if(SentenceDegree > MaxDegree)
				MaxDegree = SentenceDegree;
		}
		int k=0;
		double CentralityScore = 0;
		for(Map.Entry<String, Integer> entry : SimlarityDegree.entrySet())
			{CentralityMatrix[k] = entry.getValue();k++;
			CentralityScore = entry.getValue()/MaxDegree;
			/*System.out.println(entry.getKey()+" "+Math.round(CentralityScore*100.0)/100.0);*/}
		
		return CentralityMatrix;
		}

	private double[] sentenceLength(String[][] sentences_words) {
		double[] sentences_score = new double[sentences_words.length];
		int[] sentences_lengths = new int[sentences_words.length];

		for (int i = 0; i < sentences_words.length; i++)
			sentences_lengths[i] = sentences_words[i].length;

		int[] help = sentenceLength_helper(sentences_lengths);
		int _q1 = help[0], max = help[1], _q3 = help[2];

		for (int i = 0; i < sentences_words.length; i++)
			if (sentences_words[i].length > _q1 && sentences_words[i].length < _q3)
				sentences_score[i] = (double) sentences_words[i].length / max;

		return sentences_score;
	}

	// [0] = q1, [1] = max between q1 and q3, [2] = q3_index
	private int[] sentenceLength_helper(int[] values) {
		Arrays.sort(values);
		int[] res = new int[3];

		int mid_index = median(values, 0, values.length);

		// Median of first half
		int q1 = values[median(values, 0, mid_index)];

		// Median of second half
		int q3 = values[median(values, mid_index + 1, values.length)];

		int iqr = q3 - q1;
		int _q1 = q1 - (int) (1.5f * iqr);
		int _q3 = q3 + (int) (1.5f * iqr);

		int max = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i] > _q1 && values[i] < _q3)
				if (values[i] > max)
					max = values[i];
		}

		res[0] = _q1;
		res[1] = max;
		res[2] = _q3;
		return res;
	}

	// Function to give
	// index of the median
	static int median(int a[], int l, int r) {
		int n = r - l + 1;
		n = (n + 1) / 2 - 1;
		return n + l;
	}

	private double[] cueWords(String[] sentences) {
		double[] sentences_score = new double[sentences.length];
		int[] cue_counts = new int[sentences.length];
		int total = 0;

		String[] cue_words = { "الافضل", "كنتيجة", "بالتحديد", "كملخص", "الاهم", "نتيجة لهذا", "نتيجة لذلك", "اخيرا",
				"خاتمة", "في النهاية", "استنتاج", "المؤكد", "كمنطق", "بناء على", "بالتالي", "هكذا", "بالكاد",
				"لهذا السبب", "لذلك", "بذلك", "ولهذا", "بشكل كبير", "بشكل ملحوظ", "على هذا الشرط", "على ناحية اخرى",
				"على صعيد اخر", "تلخيصا", "منطقيا" };

		try {
			findlight(cue_words);

			for (int i = 0; i < sentences.length; i++) {
				for (int j = 0; j < cue_words.length; j++) {
					if (sentences[i].contains(cue_words[j])) {
						cue_counts[i]++;
						total++;
					}
				}
			}

			for (int i = 0; i < sentences.length; i++)
				sentences_score[i] = (double) cue_counts[i] / total;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sentences_score;
	}

	private double[] positiveKeyWords(String[] sentance) {
		double Sentance_Score[] = new double[sentance.length];
		String[] Postive_Words = { "وثق", "أكد", "بالتأكيد", "من المؤكد", "من المثبت", "اثبات", "اثبت ", "اقرار", "اقر",
				"تأييد", "ايد", "أدلة", "ايجاب", "بينة", "دليل", "تأكيد", "تحديد", "تحقيق", "تقرير", "جزم", "شهادة",
				"برهان", "توكيد", "من المصدق", "تصديق", "صدق", "دلل", "حدد", "حقق", "برهن", "شهد", "ذو معنى",
				"كل المعنى", "دلالي" };

		try {
			findlight(Postive_Words);

			int Total = 0, freq[] = new int[sentance.length];
			for (int i = 0; i < sentance.length; i++) {
				for (int j = 0; j < Postive_Words.length; j++) {
					if (sentance[i].contains(Postive_Words[j])) {
						Total++;
						freq[i]++;
					}
				}
			}

			if (Total == 0)
				return Sentance_Score;
			for (int i = 0; i < sentance.length; i++) {
				Sentance_Score[i] = (double) freq[i] / Total;

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Sentance_Score;
	}
	private int[] positiveWordsSVM(String[] sentance) {
		
		String[] Postive_Words = { "وثق", "أكد", "بالتأكيد", "من المؤكد", "من المثبت", "اثبات", "اثبت ", "اقرار", "اقر",
				"تأييد", "ايد", "أدلة", "ايجاب", "بينة", "دليل", "تأكيد", "تحديد", "تحقيق", "تقرير", "جزم", "شهادة",
				"برهان", "توكيد", "من المصدق", "تصديق", "صدق", "دلل", "حدد", "حقق", "برهن", "شهد", "ذو معنى",
				"كل المعنى", "دلالي" };
		
		for(int i = 0 ; i < Postive_Words.length ; i++)
		{
			Postive_Words[i] = pre.arn.normalize(Postive_Words[i]);			
		}
		
		int[]sentSW_scores = new int[sentance.length];
		for(int i = 0 ; i < sentance.length ; i++)
		{
		
		  for(int j = 0 ; j < Postive_Words.length ; j++)
		   {
			  if(sentance[i].matches(".*\\b" + Postive_Words[j] + "\\b.*"))
			  {
				  sentSW_scores[i] = 1;
				  break;
			  }
		
		   }
		}
		return sentSW_scores;
	}

	public double[] numberScore(String[] sentences, String[][] Word) throws IOException {
		int sen[] = new int[sentences.length], Num = 0;

		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < Word[i].length; j++) {
				// when the word contain data ascii code between 0 to 7F then this sentance
				// contain numberical data
				if (Word[i][j].matches("^\\d+[^\\x00-\\x7F]*$")) {
					Num++;
					sen[i]++;
				}
			}
		}
		double[] Sentance_Score = new double[sentences.length];
		if (Num == 0)
			return Sentance_Score;
		for (int i = 0; i < sentences.length; i++) {
			Sentance_Score[i] = (double) sen[i] / Num;
		}
		return Sentance_Score;
	}

	// Occurence of non-essential information.
	public double[] WeakWords_Scoring(String[] Sentences) {
		double[] sentenceScoreWW = new double[Sentences.length];
		String[] WeakWords = { "بالاضافة", "علي سبيل المثال", "مثل", "كمثال", "علي اي حال", "علاوة علي ذلك", "أولا",
				"ثانيا", "ثم", "زيادة علي ذلك", "بصيغة أخري" };
		try {
			findlight(WeakWords);

			int[] sentenceCount = new int[Sentences.length];
			int[] sentenceWW = new int[Sentences.length];

			for (int i = 0; i < Sentences.length; i++) {

				String[] SentenceWords = Sentences[i].split(" ");
				sentenceCount[i] = SentenceWords.length;

				for (int j = 0; j < WeakWords.length; j++) {
					if (Sentences[i].contains(WeakWords[j])) {
						sentenceWW[i]++;
					}
				}
			}

			int i = 0;
			for (String Sentence : Sentences) {
				for (String WW : WeakWords) {
					if (Sentence.startsWith(WW)) {
						sentenceScoreWW[i] = -2;
						break;
					} else {
						sentenceScoreWW[i] = -1 * ((double) sentenceWW[i]) / ((double) sentenceCount[i]);
					}
				}
				i++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sentenceScoreWW;
	}
	
	public double[][] WeakWords_Scoring_SVM(String[] Sentences)
	{
		String[] WeakWords = { "بالاضافة", "علي سبيل المثال", "مثل", "كمثال", "علي اي حال", "علاوة علي ذلك", "أولا",
				"ثانيا", "ثم", "زيادة علي ذلك", "بصيغة أخري" };
		
		for(int i = 0 ; i < WeakWords.length ; i++)
		{
			WeakWords[i] = pre.arn.normalize(WeakWords[i]);			
		}
		
		int [] sentWW = new int[Sentences.length];
		int [] sentWordCount = new int[Sentences.length];
		for(int i = 0 ; i < Sentences.length ; i++ )
		{
			for(int j = 0 ; j < WeakWords.length ; j++)
			{	
				if (Sentences[i].contains(WeakWords[j]))
				{
					sentWW[i]++;
				}
			}
				String[] sentWords = Sentences[i].split(" ");
				sentWordCount[i] = sentWords.length;	
		}
		double[] sentWW_scores0 = new double[Sentences.length];
		for(int i = 0 ; i< Sentences.length ; i++)
		{
			for(int j = 0 ; j < WeakWords.length ; j++)
			{
				if(Sentences[i].startsWith(WeakWords[j]))
				{
					sentWW_scores0[i] = 1;
					break;
					
				}
			}
		}
		
		double[] sentWW_scores1 = new double[Sentences.length];
		for(int i = 0 ; i < Sentences.length ; i++)
		{
			if(sentWW_scores0[i] == 0)
			{
				sentWW_scores1[i] = ((double)sentWW[i])/ ((double)sentWordCount[i]);
			}
			
		}
		
		double[][] sentWW_scores = new double[Sentences.length][2];
		for(int i = 0 ; i < sentWW_scores.length ; i++)
		{
			sentWW_scores[i][0] = sentWW_scores0[i];
			sentWW_scores[i][1] = sentWW_scores1[i];
			
		}
		return sentWW_scores;
	}
	

	private void findlight(String[] words) throws Exception {
		for (int i = 0; i < words.length; i++)
			words[i] = pre.arn.normalize(words[i]);

		for (int i = 0; i < words.length; i++) {
			String[] tokens = pre.tok.tokenize(words[i]);
			String lightText = " ";
			for (int j = 0; j < tokens.length; j++) {
				String stem = pre.ls2.findStem(tokens[j]);
				lightText = lightText + stem + " ";
			}
			words[i] = lightText;
		}
	}
}
