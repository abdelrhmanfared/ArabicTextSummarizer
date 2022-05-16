package feature;

import preprocessing.Preprocessing1;

public class NumberExistence extends WordFeature {
	/**
	 * @return both scoreBased & svm Features
	 */
	public double[] getSVM_ScoreBasedFeature() {
		return scoreBasedFeature;
	}

	public NumberExistence(Preprocessing1 pre) {
		// TODO Auto-generated constructor stub
		super(pre.getRootSentencesList(), new String[] { "\\d" });
	}

}
