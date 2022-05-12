package feature;

import preprocessing.Preprocessing1;
import similarity.CosineSimlarity;

public class TitleSimilarity {
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

	public TitleSimilarity(Preprocessing1 pre, String title) {
		// TODO Auto-generated constructor stub
		String[][] sentencesTokens = pre.getLight10SentencesTokens();
		String[] tokens = pre.getLight10Tokens();
		String[] titleTokens = title.split(" ");
		for (int i = 0; i < titleTokens.length; i++) {
			titleTokens[i] = pre.arn.normalize(titleTokens[i]);
			titleTokens[i] = pre.dr.removeDiacritics(titleTokens[i]);
			titleTokens[i] = pre.ls10.findStem(titleTokens[i]);
		}

		int NO_SENTENCES = pre.getOriginalSentences().length;
		String[][] Sentences_Title = concat_Sentences_Title(sentencesTokens, titleTokens);
		String[] Tokens_Title = concat_Tokens_Title(tokens, titleTokens);
		CosineSimlarity cosineSimlarity = new CosineSimlarity(Sentences_Title, Tokens_Title);

		double[] TitleSimilarityMatrix = getTitleSimilarityMatrix(cosineSimlarity, NO_SENTENCES);

		scoreBasedFeature = new double[NO_SENTENCES];
		svmFetures = new double[NO_SENTENCES][2];

		for (int i = 0; i < NO_SENTENCES; i++) {
			int commonKeyPhrases = getCommonKeyPhrases(pre.getKeyPhrase(), sentencesTokens[i], titleTokens);
			svmFetures[i][0] = scoreBasedFeature[i] = TitleSimilarityMatrix[i] * Math.sqrt(1 + commonKeyPhrases);

			if (commonKeyPhrases > 0)
				svmFetures[i][1] = 1;
		}

	}

	private String[][] concat_Sentences_Title(String[][] sentencesTokens, String[] titleTokens) {
		String[][] newArr = new String[sentencesTokens.length + 1][];

		for (int i = 0; i < sentencesTokens.length; i++)
			newArr[i] = sentencesTokens[i];
		newArr[sentencesTokens.length] = titleTokens;

		return newArr;
	}
	
	private String[] concat_Tokens_Title(String[] tokens, String[] titleTokens) {
		String[] newArr = new String[tokens.length + titleTokens.length];

		for (int i = 0; i < tokens.length; i++)
			newArr[i] = tokens[i];
		for (int i = 0; i < titleTokens.length; i++)
			newArr[i] = titleTokens[tokens.length + i];

		return newArr;
	}

	private double[] getTitleSimilarityMatrix(CosineSimlarity cosineSimlarity, int NO_SENTENCES) {
		double[] TitleSimilarityMatrix = new double[NO_SENTENCES];

		for (int i = 0; i < NO_SENTENCES; i++) {
			TitleSimilarityMatrix[i] = cosineSimlarity.getCosineSimilarity(i, NO_SENTENCES);
		}

		return TitleSimilarityMatrix;
	}

	private int getCommonKeyPhrases(String[] KeyPhrses, String[] sentence, String[] title) {
		int counter = 0;
		for (String titleToken : title)
			if (contains(KeyPhrses, titleToken) && contains(sentence, titleToken))
				counter++;
		return counter;
	}

	private boolean contains(String[] arr, String val) {
		for (String el : arr)
			if (el.equals(val))
				return true;
		return false;
	}
}
