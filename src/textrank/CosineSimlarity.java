package textrank;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.VarHandle;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.poi.hpsf.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import utilities.TrainedTokenizer;

class CosineSimlarity {
	HashSet<String> BOW;
	double[][] cosineBOW;
	/**
	 * @return the cosineBOW2
	 */
	public double[][] getCosineBOW() {
		return cosineBOW;
	}

	Map<String, ArrayList<Pair>> BowMatrix;

	// set of (uniqueToken(T), |T in Si|) for n sentences
	ArrayList<Map<String, Integer>> BowMatrix2;
	
	public CosineSimlarity(String[][] sentencesTokens) {
		int NO_SENTENCES = sentencesTokens.length;
		BowMatrix2 = new ArrayList<Map<String, Integer>>();

		for (int i = 0; i < NO_SENTENCES; i++) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			for (String token : sentencesTokens[i])
				map.put(token, map.getOrDefault(token, 0) + 1);

			BowMatrix2.add(map);
		}
		
		centeralityMatrix2(sentencesTokens);
	}

	private void centeralityMatrix2(String[][] sentencesTokens) {
		int NO_SENTENCES = sentencesTokens.length;
		double[][] cosineBOW = new double[NO_SENTENCES][NO_SENTENCES];

		for (int i = 0; i < NO_SENTENCES; i++)
			cosineBOW[i][i] = 1.0;

		for (int i = 0; i < NO_SENTENCES; i++) {
			for (int j = 0; j < i; j++) {
				double top = 0;
				double bottom = 0;
				double bottom1 = 0;
				double bottom2 = 0;

				Map<String, Integer> tfS1 = BowMatrix2.get(i);
				Map<String, Integer> tfS2 = BowMatrix2.get(j);

				for (String token : tfS1.keySet()) {
					top += tfS1.get(token) * tfS2.getOrDefault(token, 0) /* *isf(token)^2 */;
					bottom1 += Math.pow(tfS1.get(token) /* *isf(token)^2 */, 2.0);
				}

				for (String token : tfS2.keySet()) {
					bottom2 += Math.pow(tfS2.get(token) /* *isf(token)^2 */, 2.0);
				}

				bottom = Math.sqrt(bottom1) * Math.sqrt(bottom2);
				cosineBOW[i][j] = top / bottom;

			}
		}

		for (int i = 0; i < NO_SENTENCES; i++)
			for (int j = i + 1; j < NO_SENTENCES; j++)
				cosineBOW[i][j] = cosineBOW[j][i];

	}

	private double[][] centeralityMatrix(String[] sentences, String[] tokens) throws FileNotFoundException, IOException {
		BOW = new HashSet<String>();
		for (String token : tokens)
			BOW.add(token);
		BowMatrix = new HashMap<String, ArrayList<Pair>>();
		int termCount = 0;
		for (String BOWord : BOW) {
			for (int i = 0; i < sentences.length; i++) {
				termCount = termCount(sentences[i], BOWord);
				if (!BowMatrix.containsKey(sentences[i])) {
					ArrayList<Pair> list = new ArrayList<Pair>();
					list.add(new Pair(BOWord, termCount));
					BowMatrix.put(sentences[i], list);
				} else
					BowMatrix.get(sentences[i]).add(new Pair(BOWord, termCount));
			}
		}

		cosineBOW = new double[sentences.length][sentences.length];
		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < sentences.length; j++) {
				if (i == j)
					cosineBOW[i][j] = 1.0;
				else if (cosineBOW[j][i] != 0)
					cosineBOW[i][j] = cosineBOW[j][i];
				else {
					double top = 0;
					double bottom = 0;
					double bottom1 = 0;
					double bottom2 = 0;

					for (int k = 0; k < BowMatrix.get(sentences[i]).size(); k++) {
						top += (BowMatrix.get(sentences[i]).get(k).getCount()
								* BowMatrix.get(sentences[j]).get(k).getCount());
						bottom1 += Math.pow(BowMatrix.get(sentences[i]).get(k).getCount(), 2.0);
						bottom2 += Math.pow(BowMatrix.get(sentences[j]).get(k).getCount(), 2.0);
					}
					bottom = Math.sqrt(bottom1) * Math.sqrt(bottom2);
					cosineBOW[i][j] = top / bottom;
				}
			}
		}
		return cosineBOW;
	}

	public double[][] TF_IDF(String[] sentences, String[] tokens) throws FileNotFoundException, IOException {
		Map<String, ArrayList<Pair>> TF_IDFMatrix = new HashMap<String, ArrayList<Pair>>();
		BOW = new HashSet<String>();
		for (String token : tokens)
			BOW.add(token);
		int termCount = 0;
		double TF = 0;
		double IDF = 0;
		for (String BOWord : BOW) {
			for (int i = 0; i < sentences.length; i++) {
				TF = CalculateTF(sentences[i], BOWord);
				IDF = CalculateIDF(Arrays.asList(sentences), BOWord);
				if (!TF_IDFMatrix.containsKey(sentences[i])) {
					ArrayList<Pair> list = new ArrayList<Pair>();
					list.add(new Pair(BOWord, TF, IDF));
					TF_IDFMatrix.put(sentences[i], list);
				} else
					TF_IDFMatrix.get(sentences[i]).add(new Pair(BOWord, TF, IDF));
			}
		}
		cosineBOW = new double[sentences.length][sentences.length];
		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < sentences.length; j++) {
				if (i == j)
					cosineBOW[i][j] = 1.0;
				else if (cosineBOW[j][i] != 0)
					cosineBOW[i][j] = cosineBOW[j][i];
				else {
					double top = 0;
					double bottom = 0;
					double bottom1 = 0;
					double bottom2 = 0;

					for (int k = 0; k < TF_IDFMatrix.get(sentences[i]).size(); k++) {
						top += (TF_IDFMatrix.get(sentences[i]).get(k).getWtf()
								* TF_IDFMatrix.get(sentences[j]).get(k).getWtf()
								* Math.pow(TF_IDFMatrix.get(sentences[i]).get(k).getWidf(), 2.0));
						bottom1 += Math.pow(TF_IDFMatrix.get(sentences[i]).get(k).getWtf()
								* TF_IDFMatrix.get(sentences[i]).get(k).getWidf(), 2.0);
						bottom2 += Math.pow(TF_IDFMatrix.get(sentences[j]).get(k).getWtf()
								* TF_IDFMatrix.get(sentences[j]).get(k).getWidf(), 2.0);
					}
					bottom = Math.sqrt(bottom1) * Math.sqrt(bottom2);
					cosineBOW[i][j] = top / bottom;
				}
			}
		}

		return cosineBOW;
	}

	public double CalculateTF(String sentence, String Word) throws FileNotFoundException, IOException {
		int termCount = termCount(sentence, Word);
		TrainedTokenizer tokenizer = new TrainedTokenizer();
		String[] tokens = tokenizer.tokenize(sentence);
		double sentenceLenth = tokens.length;
		return Math.round(termCount / sentenceLenth * 100.0) / 100.0;
	}

	public double CalculateIDF(List<String> sentences, String Word) {
		double termSentenceCount = 0;
		for (String sentence : sentences)
			if (sentence.contains(Word))
				termSentenceCount++;

		double IDFscore = Math.round(termSentenceCount / sentences.size() * 100.0) / 100.0;
		return Math.log10(IDFscore);
	}

	public int termCount(String sentence, String term) throws FileNotFoundException, IOException {
		TrainedTokenizer tokenizer = new TrainedTokenizer();
		String[] tokens = tokenizer.tokenize(sentence);
		int counter = 0;

		for (String token : tokens)
			if (term.equals(token.toString()))
				counter++;

		return counter;
	}
}