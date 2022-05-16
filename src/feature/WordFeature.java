package feature;

import java.util.List;

import word.WordsCount;

class WordFeature {
	protected double[] scoreBasedFeature;

	public WordFeature(List<String> sentences, String[] Words) {
		// TODO Auto-generated constructor stub
		WordsCount wc = new WordsCount(sentences, Words);

		scoreBasedFeature = new double[sentences.size()];

		if (wc.total == 0)
			return;

		for (int i = 0; i < sentences.size(); i++)
			scoreBasedFeature[i] = (double) wc.NO_WORDS_IN_SENTENCES[i] / wc.total;
	}

}
