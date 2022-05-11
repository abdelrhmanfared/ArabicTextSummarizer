package similarity;

import java.util.Map;
import java.util.HashMap;

public class CosineSimlarity {
	private int NO_WORDS;
	private int NO_SENTENCES;

	private Map<String, Integer> tokensIndices;
	private Map<String, Double> ISF;
	private double[][] TF;

	public CosineSimlarity(String[][] sentencesTokens) {
		NO_SENTENCES = sentencesTokens.length;
		// unique token -> 0 based index
		tokensIndices = new HashMap<String, Integer>();

		for (int i = 0, ind = 0; i < NO_SENTENCES; i++) {
			for (String token : sentencesTokens[i]) {
				if (!tokensIndices.containsKey(token)) {
					tokensIndices.put(token, ind++);
				}
			}
		}

		NO_WORDS = tokensIndices.size();

		// Bag Of Words (2d matrix (rows = sentences, cols = unique terms) each cell =
		// term count)
		int[][] BOW = generateBOW(sentencesTokens);

		// Term Frequncy (2d matrix (rows = sentences, cols = unique terms) each cell =
		// term count/no_unique_terms)
		TF = generateTF(BOW);

		// Inverse Sentence Frequency
		ISF = generateISF(BOW);
	}

	private int[][] generateBOW(String[][] sentencesTokens) {
		int[][] BOW = new int[NO_SENTENCES][NO_WORDS];

		for (int i = 0; i < NO_SENTENCES; i++) {
			for (String token : sentencesTokens[i]) {
				BOW[i][tokensIndices.get(token)]++;
			}
		}

		return BOW;
	}

	private double[][] generateTF(int[][] BOW) {
		double[][] TF = new double[NO_SENTENCES][NO_WORDS];

		for (int i = 0; i < NO_SENTENCES; i++) {
			for (int j = 0; j < NO_WORDS; j++)
				if (BOW[i][j] > 0)
					// TF[i][j] = (double) BOW[i][j] / NO_WORDS;
					TF[i][j] = 1 + Math.log10(BOW[i][j]);
		}

		return TF;
	}

	private Map<String, Double> generateISF(int[][] BOW) {
		Map<String, Integer> documentsCount = new HashMap<String, Integer>();
		Map<String, Double> ISF = new HashMap<String, Double>();

		for (int i = 0; i < NO_SENTENCES; i++) {
			for (Map.Entry<String, Integer> tokenIndex : tokensIndices.entrySet()) {
				if (BOW[i][tokenIndex.getValue()] > 0)
					documentsCount.put(tokenIndex.getKey(), documentsCount.getOrDefault(tokenIndex.getKey(), 0) + 1);
			}
		}

		for (Map.Entry<String, Integer> entry : documentsCount.entrySet())
			ISF.put(entry.getKey(), Math.log10(NO_SENTENCES / (1 + entry.getValue())));

		return ISF;
	}

	@SuppressWarnings("unused")
	public double getCosineSimilarity(int sentence_i_ind, int sentence_j_ind) {
		double top = 0;
		double bottom = 0;
		double bottom1 = 0;
		double bottom2 = 0;

		for (Map.Entry<String, Integer> tokenIndex : tokensIndices.entrySet()) {
			double TFs1 = TF[sentence_i_ind][tokenIndex.getValue()];
			double TFs2 = TF[sentence_j_ind][tokenIndex.getValue()];
			double ISFw = ISF.get(tokenIndex.getKey());

			if (TFs1 == 0 || TFs2 == 0)
				continue;

			top += TFs1 * TFs2 * (ISFw * ISFw);
			bottom1 += (TFs1 * ISFw) * (TFs1 * ISFw);
			bottom2 += (TFs2 * ISFw) * (TFs2 * ISFw);
		}

		bottom = Math.sqrt(bottom1) * Math.sqrt(bottom2);

		return (top == 0) ? 0 : top / bottom;
	}

}