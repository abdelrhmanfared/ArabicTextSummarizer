package feature;

import java.util.Arrays;
import java.util.List;

import preprocessing.Preprocessing1;

public class SentenceLength {
	private double[] scoreBasedFeature;
	private double[][] svmFetures;

	/**
	 * @return the scoreBasedFeature
	 */
	public double[] getScoreBasedFeature() {
		return scoreBasedFeature;
	}

	/**
	 * @return the svmFetures
	 */
	public double[][] getSvmFetures() {
		return svmFetures;
	}

	public SentenceLength(Preprocessing1 pre) {
		// TODO Auto-generated constructor stub
		List<List<String>> rootSentencesTokens = pre.getRootSentencesTokensList();
		scoreBasedFeature = new double[rootSentencesTokens.size()];
		svmFetures = new double[rootSentencesTokens.size()][2];
		int[] sentences_lengths = new int[rootSentencesTokens.size()];

		for (int i = 0; i < rootSentencesTokens.size(); i++)
			sentences_lengths[i] = rootSentencesTokens.get(i).size();

		int[] help = sentenceLength_helper(sentences_lengths);
		int _q1 = help[0], max = help[1], _q3 = help[2];

		for (int i = 0; i < rootSentencesTokens.size(); i++)
			if (rootSentencesTokens.get(i).size() > _q1 && rootSentencesTokens.get(i).size() < _q3)
				svmFetures[i][0] = scoreBasedFeature[i] = (double) rootSentencesTokens.get(i).size() / max;
			else
				svmFetures[i][1] = 1;

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

		int i = values.length - 1;
		while (values[i] > _q3)
			i--;
		int max = values[i];

		res[0] = _q1;
		res[1] = max;
		res[2] = _q3;
		return res;
	}

	// Function to give
	// index of the median
	static int median(int a[], int l, int r) {
		if(l==r)
			return l;
		
		int n = r - l + 1;
		n = (n + 1) / 2 - 1;
		return n + l;
	}

}
