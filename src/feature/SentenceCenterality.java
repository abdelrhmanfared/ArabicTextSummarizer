package feature;

import preprocessing.Preprocessing1;

public class SentenceCenterality {
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

	public SentenceCenterality(Preprocessing1 pre) {
		// TODO Auto-generated constructor stub
	}

}
