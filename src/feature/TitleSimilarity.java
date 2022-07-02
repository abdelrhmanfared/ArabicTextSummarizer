package feature;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	public TitleSimilarity(Preprocessing1 pre, String title) throws FileNotFoundException, IOException {
		// TODO Auto-generated constructor stub
		List<List<String>> sentencesTokens = pre.getLight10SentencesTokensList();
		Set<String> tokens = pre.getLight10TokensSet();

		String[] titleTokens = title.split(" ");
		title = "";
		for (int i = 0; i < titleTokens.length; i++) {
			titleTokens[i] = Preprocessing1.arn.normalize(titleTokens[i]);
			titleTokens[i] = Preprocessing1.dr.removeDiacritics(titleTokens[i]);
			titleTokens[i] = Preprocessing1.ls10.findStem(titleTokens[i]);
			title += " " + titleTokens[i];
		}
		title = title.trim();

		int NO_SENTENCES = sentencesTokens.size();
		List<List<String>> Sentences_Title = concat_Sentences_Title(sentencesTokens, titleTokens);
		Set<String> Tokens_Title = concat_Tokens_Title(tokens, titleTokens);
		CosineSimlarity cosineSimlarity = new CosineSimlarity(Sentences_Title, Tokens_Title);
		double[] TitleSimilarityMatrix = getTitleSimilarityMatrix(cosineSimlarity, NO_SENTENCES);

		scoreBasedFeature = new double[NO_SENTENCES];
		svmFetures = new double[NO_SENTENCES][2];

		for (int i = 0; i < NO_SENTENCES; i++) {
			int commonKeyPhrases = getCommonKeyPhrases(pre.getLight10KeyPhrases(), pre.getLight10SentencesList().get(i),
					title);
			svmFetures[i][0] = scoreBasedFeature[i] = TitleSimilarityMatrix[i] * Math.sqrt(1 + commonKeyPhrases);

			if (commonKeyPhrases > 0)
				svmFetures[i][1] = 1;
		}

	}

	private List<List<String>> concat_Sentences_Title(List<List<String>> sentencesTokens, String[] titleTokens) {
		List<List<String>> newArr = new ArrayList<List<String>>(sentencesTokens);
		newArr.add(Arrays.asList(titleTokens));
		return newArr;
	}

	private Set<String> concat_Tokens_Title(Set<String> tokens, String[] titleTokens) {
		Set<String> newSet = new HashSet<String>(tokens);
		for (String token : titleTokens)
			newSet.add(token);

		return newSet;
	}

	private double[] getTitleSimilarityMatrix(CosineSimlarity cosineSimlarity, int NO_SENTENCES) {
		double[] TitleSimilarityMatrix = new double[NO_SENTENCES];

		for (int i = 0; i < NO_SENTENCES; i++) {
			TitleSimilarityMatrix[i] = cosineSimlarity.getCosineSimilarity(i, NO_SENTENCES);
		}

		return TitleSimilarityMatrix;
	}

	private int getCommonKeyPhrases(String[] KeyPhrases, String sentence, String title) {
		int counter = 0;
		for (String keyPhrase : KeyPhrases)
			if (title.contains(keyPhrase) && sentence.contains(keyPhrase))
				counter++;
		return counter;

	}
}
