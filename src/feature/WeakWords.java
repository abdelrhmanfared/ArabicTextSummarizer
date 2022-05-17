package feature;

import java.util.List;

import preprocessing.Preprocessing1;
import word.Words;
import word.WordsCount;

public class WeakWords {
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

	public WeakWords(Preprocessing1 pre) {
		// TODO Auto-generated constructor stub
		List<String> sentences = pre.getNormalizedSentencesList();

		WordsCount wc = new WordsCount(sentences, Words.WEAK_WORDS);

		scoreBasedFeature = new double[sentences.size()];
		svmFetures = new double[sentences.size()][2];

		for (int i = 0; i < sentences.size(); i++) {
			if (wc.NO_WORDS_IN_SENTENCES[i] > 0) {
				if (startWithWeakWord(sentences.get(i))) {
					scoreBasedFeature[i] = -2.0;
					svmFetures[i][0] = 1.0;
				} else {
					svmFetures[i][1] = scoreBasedFeature[i] = (-1)
							* ((double) wc.NO_WORDS_IN_SENTENCES[i] / pre.getNormalizedSentencesTokensList().get(i).size());
				}
			}
		}

	}

	private boolean startWithWeakWord(String sen) {
		for (String weakWord : Words.WEAK_WORDS)
			if (sen.startsWith(weakWord))
				return true;
		return false;
	}
}
