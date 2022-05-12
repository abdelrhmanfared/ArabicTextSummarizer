package feature;

import java.util.HashMap;
import java.util.Map;

import preprocessing.Preprocessing1;
import similarity.*;

public class SentenceCenterality {
	private double[] svm_scoreBasedFeature;

	/**
	 * @return the scoreBasedFeature
	 */
	public double[] getScoreBasedFeature() {
		return svm_scoreBasedFeature;
	}

	public SentenceCenterality(Preprocessing1 pre) {
		// TODO Auto-generated constructor stub
		int NO_SENTENCES = pre.getOriginalSentences().length;
		String[] sentences = pre.getRootSentences();
		String[] tokens=pre.getRootTokens();
		CosineSimlarity cosineSimilarity = new CosineSimlarity(pre.getRootSentencesTokens(), tokens);

		double Threshold = 0.1;
		double[][] CosineSimMatrix;
		double[] CentralityMatrix = new double[NO_SENTENCES];

		CosineSimMatrix = centeralityMatrix(cosineSimilarity, NO_SENTENCES);
		/*
		 * for(int i=0;i<CosineSimMatrix.length;i++) { for(int
		 * j=0;j<CosineSimMatrix[i].length;j++)
		 * System.out.print(Math.round(CosineSimMatrix[i][j]*100.0)/100.0+" ");
		 * System.out.println(); }
		 */

		Map<String, Integer> SimlarityDegree = new HashMap<String, Integer>();
		double MaxDegree = 0;
		int SentenceDegree = 0;
		for (int i = 0; i < CosineSimMatrix.length; i++) {
			for (int j = 0; j < CosineSimMatrix[i].length; j++)
				if (CosineSimMatrix[i][j] >= Threshold)
					SentenceDegree++;

			SimlarityDegree.put(sentences[i], SentenceDegree);
			if (SentenceDegree > MaxDegree)
				MaxDegree = SentenceDegree;
		}
		int k = 0;
		double CentralityScore = 0;
		for (Map.Entry<String, Integer> entry : SimlarityDegree.entrySet()) {
			CentralityMatrix[k] = entry.getValue();
			k++;
			CentralityScore = entry.getValue() / MaxDegree;
			/*
			 * System.out.println(entry.getKey()+" "+Math.round(CentralityScore*100.0)/100.0
			 * );
			 */}

		svm_scoreBasedFeature = CentralityMatrix;
	}

	private double[][] centeralityMatrix(CosineSimlarity cosineSimilarity, int NO_SENTENCES) {
		double[][] cosineBOW = new double[NO_SENTENCES][NO_SENTENCES];

		for (int i = 0; i < NO_SENTENCES; i++)
			cosineBOW[i][i] = 1.0;

		for (int i = 0; i < NO_SENTENCES; i++) {
			for (int j = 0; j < i; j++) {
				cosineBOW[i][j] = cosineSimilarity.getCosineSimilarity(i, j);
			}
		}

		for (int i = 0; i < NO_SENTENCES; i++)
			for (int j = i + 1; j < NO_SENTENCES; j++)
				cosineBOW[i][j] = cosineBOW[j][i];

		return cosineBOW;
	}
}
