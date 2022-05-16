package feature;

import java.util.List;

import preprocessing.Preprocessing1;
import word.Words;
import word.WordsCount;

public class StrongWords extends WordFeature{
	private int[] svmFetures;

	/**
	 * @return the scoreBasedFeature
	 */
	public double[] getScoreBasedFeature() {
		return scoreBasedFeature;
	}

	/**
	 * @return the svmFetures
	 */
	public int[] getSvmFetures() {
		return svmFetures;
	}

	public StrongWords(Preprocessing1 pre) {
		// TODO Auto-generated constructor stub
		super(pre.getNormalizedSentencesList(), Words.STRONG_WORDS);
		
		List<String> sentences = pre.getNormalizedSentencesList();
		svmFetures = new int[sentences.size()];

		for(int i=0;i<sentences.size();i++)
			if(scoreBasedFeature[i]>0)
				svmFetures[i]=1;
	}

}
