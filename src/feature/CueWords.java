package feature;

import preprocessing.Preprocessing1;
import word.Words;

public class CueWords extends WordFeature {

	/**
	 * @return both scoreBased & svm Features
	 */
	public double[] getSVM_ScoreBasedFeature() {
		return scoreBasedFeature;
	}

	public CueWords(Preprocessing1 pre) {
		// TODO Auto-generated constructor stub
		super(pre.getNormalizedSentencesList(), Words.CUE_WORDS);
	}

}
