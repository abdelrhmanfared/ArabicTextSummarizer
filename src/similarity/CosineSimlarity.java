package similarity;

import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CosineSimlarity {
	private int NO_WORDS;
	private int NO_SENTENCES;

	private List<List<String>> sentencesTokens;
	private Map<String, Integer> tokensIndices;
	private Map<String, Double> ISF;
	private double[][] TF;

	public CosineSimlarity(List<List<String>> sentencesTokens, Set<String> tokens) {
		this.sentencesTokens = sentencesTokens;
		NO_SENTENCES = sentencesTokens.size();
		NO_WORDS = tokens.size();

		// unique token -> 0 based index
		tokensIndices = new HashMap<String, Integer>();

		int ind = 0;
		for (String token : tokens)
			tokensIndices.put(token, ind++);

		// Bag Of Words (2d matrix (rows = sentences, cols = unique terms) each cell =
		// term count)
		int[][] BOW = generateBOW();

		// Term Frequncy (2d matrix (rows = sentences, cols = unique terms) each cell =
		// term count/no_unique_terms)
		TF = generateTF(BOW);

		// Inverse Sentence Frequency
		ISF = generateISF(BOW);
	}

	private int[][] generateBOW() {
		int[][] BOW = new int[NO_SENTENCES][NO_WORDS];

		for (int i = 0; i < NO_SENTENCES; i++) {
			for (String token : sentencesTokens.get(i)) {
				BOW[i][tokensIndices.get(token)]++;
			}
		}

		return BOW;
	}

	private double[][] generateTF(int[][] BOW) {
		double[][] TF = new double[NO_SENTENCES][NO_WORDS];

		for (int i = 0; i < NO_SENTENCES; i++) {
			for (String token : sentencesTokens.get(i)) {
				// if (BOW[i][j] > 0)
				// TF[i][j] = (double) BOW[i][j] / NO_WORDS;
				int ind = tokensIndices.get(token);
				TF[i][ind] = 1 + Math.log10(BOW[i][ind]);
			}
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

		// Creating set of unique tokens in Si and Sj
		List<String> list = new ArrayList<String>(sentencesTokens.get(sentence_i_ind));
		list.addAll(sentencesTokens.get(sentence_j_ind));
		Set<String> tokensInSiSjSet = new HashSet<String>(list);

		for (String token : tokensInSiSjSet) {
			int tokenIndex = tokensIndices.get(token);
			double TFs1 = TF[sentence_i_ind][tokenIndex];
			double TFs2 = TF[sentence_j_ind][tokenIndex];
			double ISFw = ISF.get(token);

			top += TFs1 * TFs2 * (ISFw * ISFw); // TFsi * TFsj * (ISFw)^2
			bottom1 += (TFs1 * ISFw) * (TFs1 * ISFw); // (TFs1 * ISFw)^2
			bottom2 += (TFs2 * ISFw) * (TFs2 * ISFw); // (TFs2 * ISFw)^2
		}

		bottom = Math.sqrt(bottom1) * Math.sqrt(bottom2);

		return (top == 0) ? 0 : top / bottom;
	}

}