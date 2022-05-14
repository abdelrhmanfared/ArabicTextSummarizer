package feature;

import preprocessing.Preprocessing1;

// Relating to the position of a sentence to the paragraph and document
public class SentenceLocation {
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

	public SentenceLocation(Preprocessing1 pre) {
		// TODO Auto-generated constructor stub

		String[][] paragraphsSentences = pre.getParagraphsSentences();
		int NO_SENTENCES = pre.getOriginalSentencesList().size();
		int lastParagraphStart = NO_SENTENCES - paragraphsSentences[paragraphsSentences.length - 1].length;

		scoreBasedFeature = new double[NO_SENTENCES];
		svmFetures = new double[NO_SENTENCES][5];

		// FIRST SENTENCES
		// last parag
		scoreBasedFeature[lastParagraphStart] = 2.0;
		svmFetures[lastParagraphStart][1] = 1.0;

		// first parag
		scoreBasedFeature[0] = 3.0;
		svmFetures[0][0] = 1.0;

		// rem parags
		for (int i = 0, ind = 0; i < paragraphsSentences.length - 2; i++) {
			ind += paragraphsSentences[i].length;
			svmFetures[ind][2] = scoreBasedFeature[ind] = 1.0;
		}

		// PARAGRAPHS
		// first
		for (int i = 1; i < paragraphsSentences[0].length; i++) {
			svmFetures[i][4] = scoreBasedFeature[i] = 1.0 / Math.sqrt((double) i + 1.0);
		}

		// last
		for (int i = 1; i < paragraphsSentences[paragraphsSentences.length - 1].length; i++) {
			svmFetures[lastParagraphStart + i][4] = scoreBasedFeature[lastParagraphStart + i] = 1.0
					/ Math.sqrt((double) i + 1.0);
		}

		// rem
		int ind = paragraphsSentences[0].length;
		for (int i = 1; i < paragraphsSentences.length - 1; i++) {
			ind++;
			for (int j = 1; j < paragraphsSentences[i].length; j++) {
				svmFetures[ind][3] = scoreBasedFeature[ind] = 1.0
						/ Math.sqrt(((double) j + 1.0) + (double) ((i + 1) * (i + 1)));
				ind++;
			}
		}

	}
}
