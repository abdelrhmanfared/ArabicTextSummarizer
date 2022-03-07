package textrank;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import preprocessing.Preprocessing;
import utilities.AraNormalizer;
import utilities.DiacriticsRemover;
import utilities.LightStemmer10;
import utilities.PunctuationsRemover;

class Score {
	public int index;
	public double score;

	public Score(int index, double score) {
		this.index = index;
		this.score = score;
	}
}

class Sortbyscore implements Comparator<Score> {

	// Method
	// Sorting in descending order of score number
	public int compare(Score a, Score b) {

		return Double.compare(b.score, a.score);
	}
}

class Sortbyindex implements Comparator<Score> {

	// Method
	// Sorting in ascending order of index number
	public int compare(Score a, Score b) {

		return a.index - b.index;
	}
}

public class Textrank {
	private String summarizedText;
	private Preprocessing pre;
	AraNormalizer arn = new AraNormalizer();
	DiacriticsRemover dr = new DiacriticsRemover();
	LightStemmer10 ls10 = new LightStemmer10();

	public String getSummarizedText() {
		return summarizedText;
	}

	public Textrank(String text) throws Exception {
		pre = new Preprocessing(text);
		double ratio = (double) 1 / 3;
		summarizedText = "";
		
		//Parameters
		String[] RootText_sentences = pre.getRootText_sentences();
		String[][] rootSentencesTokens = pre.getrootSentencesTokens();
		String[] Normalized_sentences = pre.getNormalized_sentences();
		String[] originalsentences = pre.getOriginalText_sentences();
		
		// Extract features
		// double[] keyPhrases = keyPhrases(lightText_sentences, topKeys, post);
		double[] sentenceLocation = sentencelocation(pre.getOriginal_paragraphs());
		// double[] titleSimilarity = similarityWithTitle(lightText_sentences, tokens, lightSentencesTokens, title, topKeys);
		double[] senCentrality = sentenceCentrality(RootText_sentences, pre.getRootTextTokens(), rootSentencesTokens);
		double[] senLength = sentenceLength(rootSentencesTokens);
		double[] cuePhrases = cueWords(Normalized_sentences);
		double[] strongWords = positiveKeyWords(Normalized_sentences);
		double[] numberScores = numberScore(RootText_sentences, rootSentencesTokens);
		double[] weakWords = WeakWords_Scoring(Normalized_sentences );
		
		if(RootText_sentences.length != originalsentences.length || rootSentencesTokens.length != originalsentences.length
				|| Normalized_sentences.length != originalsentences.length)
			throw new Exception("LENGTHS NOT THE SAME!");
		
		// Ranking
		ArrayList<Score> sentences_scores = new ArrayList<Score>();
		
		for (int i = 0; i < originalsentences.length; i++) {
			sentences_scores.add(new Score(i, /*keyPhrases[i] +*/ sentenceLocation[i] + /*titleSimilarity[i] +*/
				senCentrality[i] + senLength[i] + cuePhrases[i] + strongWords[i] + numberScores[i] + weakWords[i]));
		}

		Collections.sort(sentences_scores, new Sortbyscore());

		int Summarylength = (int) (ratio * originalsentences.length);

		ArrayList<Score> summary = new ArrayList<Score>();
		for (int i = 0; i < Summarylength; i++)
			summary.add(sentences_scores.get(i));
		Collections.sort(summary, new Sortbyindex());

		for (Score score : summary)
			summarizedText += originalsentences[score.index];

	}

	// A short list of important terms that provide a condensed summary of the main
	// topics of a document
	@SuppressWarnings("unused")
	private int keyPhrases() {
		return 0;
	}

	// Relating to the position of a sentence to the paragraph and document
	public double[] sentencelocation(String[] paragraphs)
	{
		
		int numberOfParagraphs = paragraphs.length;
		String[][] sentences = new String[numberOfParagraphs][];
		ArrayList<Double> SentenceLocationScores = new ArrayList<Double>();
		int lastParagraph = numberOfParagraphs-1;
		
		//Detect the boundaries of each sentence for each paragraph.
		for(int eachParagraph=0; eachParagraph<numberOfParagraphs; eachParagraph++)
		{	try {
				sentences[eachParagraph] = pre.SentencesOfParagraph(paragraphs[eachParagraph]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("An error in sentence detection has occurred.");
				e.printStackTrace();
			}
		}
		
		//Sentence locations cases.
		for(int eachParagraph=0; eachParagraph<numberOfParagraphs; eachParagraph++)
		{
			for(int eachSentence=0; eachSentence<sentences[eachParagraph].length; eachSentence++)
			{
				if(eachParagraph == 0)
				{
					if(eachSentence == 0)
						SentenceLocationScores.add(Double.valueOf(3));
					else
						SentenceLocationScores.add(Double.valueOf(1/Math.sqrt(Double.valueOf(eachSentence+1))));
				}
				
				else if(eachParagraph == lastParagraph)
				{
					if(eachSentence==0)
						SentenceLocationScores.add(Double.valueOf(2));
					else
						SentenceLocationScores.add(Double.valueOf(1/Math.sqrt(Double.valueOf(eachSentence+1))));
				}
				
				else
				{
					if(eachSentence == 0)
						SentenceLocationScores.add(Double.valueOf(1));
					else
						SentenceLocationScores.add(Double.valueOf(1/Math.sqrt(Double.valueOf( (eachSentence+1) + ((eachParagraph+1)*(eachParagraph+1)) ))));
				}
			}
		}
		
		//Convert Double ArrayList --> double[] 
		double[] scores = new double[SentenceLocationScores.size()];
		for(int i=0; i<SentenceLocationScores.size(); i++)
			scores[i]=SentenceLocationScores.get(i).doubleValue();
		
		return scores;
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
			title[i] = arn.normalize(title[i]);
			title[i] = dr.removeDiacritics(title[i]);
			title[i] = ls10.findStem(title[i]);
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
	private double[] sentenceCentrality(String[] sentences, String[] tokens, String[][] senTokens) {
		Set<String> words = new HashSet<String>();
		for (String str : tokens) {
			words.add(str);
		}

		String[] WORDS = words.toArray(new String[words.size()]);
		double[][] tf = new double[sentences.length][WORDS.length];
		double[] isf = new double[WORDS.length];
		double[][] cosineScore = new double[sentences.length][sentences.length];
		int countTF = 0;
		int countISF = 0;

		for (int i = 0; i < WORDS.length; i++) {
			countISF = 0;
			for (int j = 0; j < sentences.length; j++) {
				boolean find = false;
				countTF = 0;
				for (int k = 0; k < senTokens[j].length; k++) {
					if (senTokens[j][k].matches(WORDS[i])) {
						countTF = countTF + 1;
						find = true;
					}

				}
				if (find == true) {
					countISF = countISF + 1;
				}
				if (countTF != 0) {
					tf[j][i] = 1 + Math.log10(Double.valueOf(countTF));
				} else if (countTF == 0) {
					tf[j][i] = 0;
				}
			}
			if (countISF != 0) {
				isf[i] = Math.log10(sentences.length / countISF);
			} else if (countISF == 0) {
				isf[i] = 0;
			}
		}
		for (int k = 0; k < sentences.length; k++) {
			for (int i = 0; i < sentences.length; i++) {
				double sum = 0, sqrt1 = 0, sqrt2 = 0;
				if (i != k) {
					for (int j = 0; j < WORDS.length; j++) {
						sum += tf[k][j] * tf[i][j] * isf[j] * isf[j];
						sqrt1 += (tf[k][j] * isf[j]) * (tf[k][j] * isf[j]);
						sqrt2 += (tf[i][j] * isf[j]) * (tf[i][j] * isf[j]);
					}

					cosineScore[k][i] = sum / (Math.sqrt(sqrt1) * Math.sqrt(sqrt2));
				} else if (i == k) {
					cosineScore[k][i] = 0;
				}
			}
		}

		double[] senCentrality = new double[sentences.length];
		int[] count = new int[sentences.length];
		double max = 0;

		for (int k = 0; k < sentences.length; k++) {
			count[k] = 0;

			for (int i = 0; i < sentences.length; i++) {
				if (cosineScore[k][i] > 0.1) {
					count[k]++;
					if (count[k] > max) {
						max = count[k];
					}
				}
			}
		}
		for (int i = 0; i < sentences.length; i++) {
			if (max == 0) {
				senCentrality[i] = 0;
			} else {
				senCentrality[i] = count[i] / max;
			}
		}

		return senCentrality;
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

		for (int i = 0; i < cue_words.length; i++)
			cue_words[i] = arn.normalize(cue_words[i]);

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
		return sentences_score;
	}

	private double[] positiveKeyWords(String[] sentance) {
		String[] Postive_Words = { "وثق", "أكد", "بالتأكيد", "من المؤكد", "من المثبت", "اثبات", "اثبت ", "اقرار", "اقر",
				"تأييد", "ايد", "أدلة", "ايجاب", "بينة", "دليل", "تأكيد", "تحديد", "تحقيق", "تقرير", "جزم", "شهادة",
				"برهان", "توكيد", "من المصدق", "تصديق", "صدق", "دلل", "حدد", "حقق", "برهن", "شهد", "ذو معنى",
				"كل المعنى", "دلالي" };

		for (int i = 0; i < Postive_Words.length; i++)
			Postive_Words[i] = arn.normalize(Postive_Words[i]);

		int Total = 0, freq[] = new int[sentance.length];
		for (int i = 0; i < sentance.length; i++) {
			for (int j = 0; j < Postive_Words.length; j++) {
				if (sentance[i].contains(Postive_Words[j])) {
					Total++;
					freq[i]++;
				}
			}
		}
		double Sentance_Score[] = new double[sentance.length];
		if (Total == 0)
			return Sentance_Score;
		for (int i = 0; i < sentance.length; i++) {
			Sentance_Score[i] = (double) freq[i] / Total;
		}
		return Sentance_Score;
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
		if (Num == 0)return Sentance_Score;
		for (int i = 0; i < sentences.length; i++) {
			Sentance_Score[i] = (double) sen[i] / Num;
		}
		return Sentance_Score;
	}

	// Occurence of non-essential information.
	public double[] WeakWords_Scoring(String[] Sentences) {
		String[] WeakWords = { "بالاضافة", "علي سبيل المثال", "مثل", "كمثال", "علي اي حال", "علاوة علي ذلك", "أولا",
				"ثانيا", "ثم", "زيادة علي ذلك", "بصيغة أخري" };
		for (int i = 0; i<WeakWords.length; i++) {
			WeakWords[i] = arn.normalize(WeakWords[i]);
		}
		int[] sentenceCount = new int[Sentences.length];
		int[] sentenceWW = new int[Sentences.length];

		for (int i = 0; i < Sentences.length; i++) {

			String[] SentenceWords = Sentences[i].split(" ");
			sentenceCount[i] = SentenceWords.length;

			for (int j = 0; j < WeakWords.length; j++) {
				Pattern pattern = Pattern.compile(".*\\b" + WeakWords[j] + "\\b.*");
				Matcher matcher = pattern.matcher(Sentences[i]);
				while (matcher.find()) {
					sentenceWW[i]++;
				}
			}
		}

		double[] sentenceScoreWW = new double[Sentences.length];
		int i = 0;
		for (String Sentence : Sentences) {
			for (String WW : WeakWords) {
				if (Sentence.startsWith(WW)) {
					sentenceScoreWW[i] = -2;
					break;
				} else {
					sentenceScoreWW[i] = ((double) sentenceWW[i]) / ((double) sentenceCount[i]);
				}
			}
			i++;
		}
		return sentenceScoreWW;
	}
}
