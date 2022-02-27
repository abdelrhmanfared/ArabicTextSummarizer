package textrank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import preprocessing.Preprocessing;
import utilities.AraNormalizer;
import utilities.TrainedTokenizer;

class Score
{
	public int index;
	public double score;
	
	public Score(int index, double score)
	{
		this.index = index;
		this.score = score;
	}
}

class Sortbyscore implements Comparator<Score> {
	 
    // Method
    // Sorting in descending order of score number
    public int compare(Score a, Score b)
    {
 
        return Double.compare(b.score, a.score);
    }
}

class Sortbyindex implements Comparator<Score> {
	 
    // Method
    // Sorting in ascending order of index number
    public int compare(Score a, Score b)
    {
 
        return Double.compare(a.index, b.index);
    }
}

public class Textrank {
	private Preprocessing pre;
	private String summarizedText;
	public AraNormalizer arn = new AraNormalizer();
	public static TrainedTokenizer tok = new TrainedTokenizer();

	public String getSummarizedText() {
		return summarizedText;
	}

	public Textrank(String text) throws ClassNotFoundException, IOException {
		pre = new Preprocessing(text);
		double ratio = 0.3;
		summarizedText = "";
		
		// Extract features
		//double[] keyPhrases = keyPhrases(lightText_sentences, topKeys, post);
		//double[] sentenceLocation = sentencelocation(original_paragraphs, originalText_sentences);
		//double[] titleSimilarity = similarityWithTitle(lightText_sentences, tokens, lightSentencesTokens, title, topKeys);
		//double[] senCentrality = sentenceCentrality(rootText_sentences, rootTextTokens, rootSentencesTokens);
		double[] senLength = sentenceLength(pre.getrootSentencesTokens());
		double[] cuePhrases = cueWords(pre.getNormalized_sentences());
		double[] strongWords = positiveKeyWords(pre.getNormalized_sentences());
		double[] numberScores = numberScore(pre.getRootText_sentences(), pre.getrootSentencesTokens());
		//double[] weakWords = occurrenceOfNonEssentialInformation(normalized_sentences);
		
		//Ranking
		ArrayList<Score> sentences_scores = new ArrayList<Score>();
		String[] originalsentences = pre.getOriginalText_sentences();
		
		for (int i = 0; i < originalsentences.length; i++) {
			sentences_scores.add(new Score(i, //keyPhrases[i] + sentenceLocation[i] + titleSimilarity[i] + senCentrality[i] + 
					senLength[i] + cuePhrases[i] + strongWords[i] + numberScores[i])); // + weakWords[i];))
		}
		
		Collections.sort(sentences_scores, new Sortbyscore());
		
		int Summarylength = (int) (ratio * originalsentences.length);
		
		ArrayList<Score> summary = new ArrayList<Score>();
		for(int i=0; i<Summarylength; i++)
			summary.add(sentences_scores.get(i));
		Collections.sort(summary, new Sortbyindex());
		
		for(Score score : summary)
			summarizedText += originalsentences[score.index];

	}

	// A short list of important terms that provide a condensed summary of the main
	// topics of a document
	@SuppressWarnings("unused")
	private int keyPhrases() {
		return 0;
	}

	// Relating to the position of a sentence to the paragraph and document
	@SuppressWarnings("unused")
	private int sentencelocation() {
		return 0;
	}

	// Similarity or overlapping between a given sentence and the document title.
	@SuppressWarnings("unused")
	private int similarityWithTitle() {
		return 0;
	}

	// The similarity or the overlapping between a sentence and other sentences in
	// the document
	@SuppressWarnings("unused")
	private int sentenceCentrality() {
		return 0;
	}

	// Counting the number of words in the sentence (can be used to classify
	// sentence as too short or too long)
	// Counting the number of words in the sentence (can be used to classify
	// sentence as too short or too long)
	// root stemmed
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
		int q3 = values[mid_index + median(values, mid_index + 1, values.length)];

		int iqr = q3 - q1;
		int _q1 = q1 - (int) (1.5f * iqr);
		int _q3 = q3 + (int) (1.5f * iqr);

		int max = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i] > _q1 && values[i] < _q3)
				if (values[i] > max)
					max = values[i];
		}
//			int end_q1, start_q2;
//			if(values.length % 2 == 0)
//			{
//				start_q2 = values.length / 2;
//				end_q1 = start_q2 - 1;
//			}
//			else
//			{
//				start_q2 = (values.length / 2) + 1;
//				end_q1 = start_q2 - 2;
//			}
//			
//			res[0]= (end_q1 % 2 == 0)?  	

		res[0] = _q1;
		res[1] = max;
		res[2] = _q3;
		return res;
	}

	private int median(int a[], int l, int r) {
		int n = r - l + 1;
		n = (n + 1) / 2 - 1;
		return n + l;
	}

	// Words in the sentence such as â€œtherefore, finally and thusâ€� can be a good
	// indicators of significant content
	// normalized
	private double[] cueWords(String[] sentences) {
		double[] sentences_score = new double[sentences.length];
		int[] cue_counts = new int[sentences.length];
		int total = 0;

		String[] cue_words = { "الافضل", "كنتيجة", "بالتحديد", "كملخص", "الاهم", "نتيجة لهذا", "نتيجة لذلك", "اخيرا",
				"خاتمة", "في النهاية", "استنتاج", "المؤكد", "كمنطق", "بناء على", "بالتالي", "هكذا", "بالكاد",
				"لهذا السبب", "لذلك", "بذلك", "ولهذا", "بشكل كبير", "بشكل ملحوظ", "على هذا الشرط", "على ناحية اخرى",
				"على صعيد اخر", "تلخيصا", "منطقيا" };

		for (String cue : cue_words)
			cue = arn.normalize(cue);

		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < cue_words.length; j++) {
				if (sentences[i].contains(cue_words[j])) {
					cue_counts[i]++;
					total++;
				}
			}
		}

		if (total != 0) {
			for (int i = 0; i < sentences.length; i++)
				sentences_score[i] = (double) cue_counts[i] / total;
		}
		return sentences_score;
	}

	// Words that are used to emphasize or focus on special idea such as â€�have
	// outstanding, and support forâ€�
	public double[] positiveKeyWords(String[] sentance) {
		String[] Postive_Words = { "ايد", "تأييد", "اقر", "اقرار", "اثبت", "اثبات", "المثبت من", "تحديد", "تأكيد",
				"دليل", "بينة", "ايجاب", "أدلة", "المؤكد من", "بالتأكيد", "أكد", "وثق", "توكيد", "برهان", "شهادة",
				"جزم", "تقرير", "تحقيق", "تقرير", "جزم", "شهادة", "برهان", "توكيد", "من المصدق", "تصديق", "صدق", "دلل",
				"حدد", "حقق", "برهن", "شهد", "ذو معنى", "كل المعنى", "داللي" };
		for (String str : Postive_Words) {
			str = arn.normalize(str);
		}

		int Total = 0, freq[] = new int[sentance.length];
		for (int i = 0; i < sentance.length; i++) {
			for (int j = 0; j < Postive_Words.length; j++) {
				Pattern P1 = Pattern.compile(".*\\b" + Postive_Words[j] + "\\b.*");
				Matcher M = P1.matcher(sentance[i]);
				while (M.find()) {
					Total++;
					freq[i]++;
				}
			}
		}
		double Sentance_Score[] = new double[sentance.length];
		if (Total == 0)
			return Sentance_Score;
		for (int i = 0; i < sentance.length; i++) {
			Sentance_Score[i] = ((double) (freq[i] * 1.0) / Total);
		}
		return Sentance_Score;
	}

	// Existence of numerical data in the sentence.
	public double[] numberScore(String[] sentences, String[][] Word) throws IOException {
		int sen[] = new int[sentences.length], Num = 0;
		for (int i = 0; i < sentences.length; i++) {
			Word[i] = tok.tokenize(sentences[i]);
		}
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
		double Sentance_Score[] = new double[sentences.length];
		if (Num == 0)
			return Sentance_Score;
		for (int i = 0; i < sentences.length; i++) {
			Sentance_Score[i] = (double) (sen[i] * 1.0 / Num);
		}
		return Sentance_Score;
	}

	// Words that serve as an explanation words such as â€œfor exampleâ€�
	private int occurrenceOfNonEssentialInformation() {
		return 0;
	}
}
