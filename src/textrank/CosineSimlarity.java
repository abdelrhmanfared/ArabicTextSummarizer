package textrank;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import utilities.TrainedTokenizer;

class CosineSimlarity {
	public double[][] BOW(String[] sentences, String[] tokens) throws FileNotFoundException, IOException {
		HashSet<String> BOW = new HashSet<String>();
		for (String token : tokens)
			BOW.add(token);
		Map<String, ArrayList<Pair>> BowMatrix = new HashMap<String, ArrayList<Pair>>();
		int termCount = 0;
		for (String BOWord : BOW) {
			for (int i = 0; i < sentences.length; i++) {
				termCount = termCount(sentences[i], BOWord);
				if (!BowMatrix.containsKey(sentences[i])) {
					ArrayList<Pair> list = new ArrayList<Pair>();
					list.add(new Pair(BOWord, termCount));
					BowMatrix.put(sentences[i], list);
				} else
					BowMatrix.get(sentences[i]).add(new Pair(BOWord, termCount));
			}
		}

		double[][] cosineBOW = new double[sentences.length][sentences.length];
		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < sentences.length; j++) {
				if (i == j)
					cosineBOW[i][j] = 1.0;
				else if (cosineBOW[j][i] != 0)
					cosineBOW[i][j] = cosineBOW[j][i];
				else {
					double top = 0;
					double bottom = 0;
					double bottom1 = 0;
					double bottom2 = 0;

					for (int k = 0; k < BowMatrix.get(sentences[i]).size(); k++) {
						top += (BowMatrix.get(sentences[i]).get(k).getCount()
								* BowMatrix.get(sentences[j]).get(k).getCount());
						bottom1 += Math.pow(BowMatrix.get(sentences[i]).get(k).getCount(), 2.0);
						bottom2 += Math.pow(BowMatrix.get(sentences[j]).get(k).getCount(), 2.0);
					}
					bottom = Math.sqrt(bottom1) * Math.sqrt(bottom2);
					cosineBOW[i][j] = top / bottom;
				}
			}
		}
		return cosineBOW;
	}
	
	/*public  double[][] TF_IDF(String[] sentences,String[] tokens) {
		
	}*/

	public int termCount(String sentence, String term) throws FileNotFoundException, IOException {
		TrainedTokenizer tokenizer = new TrainedTokenizer();
		String[] tokens = tokenizer.tokenize(sentence);
		int counter = 0;

		for (String token : tokens)
			if (term.equals(token.toString()))
				counter++;

		return counter;
	}
}