package feature;

import java.io.IOException;
import java.util.Arrays;

import preprocessing.Preprocessing1;

public class KeyPhrases {
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

	public KeyPhrases(Preprocessing1 pre) throws ClassNotFoundException, IOException {
		// TODO Auto-generated constructor stub
		String[] keyPhrases = pre.getKeyPhrase();
		String[] light10Sentences = pre.getLight10Sentences();

		// key phrase frequency
		double[] kpF = KeyphraseFrequency(light10Sentences, keyPhrases);
		// key phrase length
		int[] kpL = KeyphraseLength(keyPhrases);
		// proper names
		int[] ProperName = ProperName(keyPhrases, pre);

		scoreBasedFeature = KpScore(kpL, ProperName, kpF, keyPhrases, light10Sentences);
		svmFetures = KpScoreSVM(kpL, ProperName, kpF, keyPhrases, light10Sentences);
	}

	private double[] KeyphraseFrequency(String[] sentences, String[] words) {
		int totalKPF = 0;
		int SentanceScore[] = new int[words.length];
		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < words.length; j++) {
				if (sentences[i].contains(" " + words[j] + " ")) {
					totalKPF++;
					SentanceScore[j]++;
				}
			}
		}
		double Score[] = new double[words.length];
		if (totalKPF == 0)
			return Score;
		for (int i = 0; i < Score.length; i++)
			Score[i] = (SentanceScore[i] * 1.0 / totalKPF);

		// TODO Auto-generated method stub
		return Score;
	}

	private int[] KeyphraseLength(String[] words) {
		int kpL[] = new int[words.length];
//		return length of number of words consist the key phrase
		for (int i = 0; i < words.length; i++) {
			String s[] = words[i].split(" ");
			kpL[i] = s.length;
		}
		return kpL;
	}

	private int[] ProperName(String[] words, Preprocessing1 pre) throws IOException, ClassNotFoundException {

		// POS Tagging
// In English data, determiners (DT), nouns (NN, NNS, NNP etc.),
// verbs (VB, VBD, VBP etc.), prepositions (IN) might be more frequent.

		int pN[] = new int[words.length];
		String txt = pre.getOrginal();
		String pos = pre.stf.tagText(txt);
		pos = pre.arn.normalize(pos);
		pos = pre.dr.removeDiacritics(pos);
		String Proper_Names[] = pos.split("/|\\s");
		Arrays.fill(pN, 1);
		for (int i = 0; i < Proper_Names.length; i++) {
			Proper_Names[i] = pre.ls10.findStem(Proper_Names[i]);
			for (int j = 0; j < words.length; j++) {
				if (Proper_Names[i].contains(" " + words[j] + " ")) {
					if (Proper_Names[i].equals("NNP") || Proper_Names[i].equals("NNPS"))
						pN[j] = 2;
				}
			}
		}

		return pN;
	}

	private double[] KpScore(int[] kpL, int[] ProperName, double[] kpF, String[] words, String[] sentences) {
		double KpScore[] = new double[words.length];
		for (int i = 0; i < words.length; i++) {
			KpScore[i] = ProperName[i] * kpF[i] * 1.0 * Math.sqrt(kpL[i]);
		}
		double SentanceKpScore[] = new double[words.length];

		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < words.length; j++) {
				if (sentences[i].contains(" " + words[j] + " ")) {
					SentanceKpScore[i] += KpScore[j];
				}
			}
		}
		return SentanceKpScore;
	}

	private double[][] KpScoreSVM(int[] kpL, int[] ProperName, double[] kpF, String[] words, String[] sentences) {
		double SentanceKpScore[][] = new double[words.length][3];

		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < words.length; j++) {
				if (sentences[i].contains(" " + words[j] + " ")) {
					SentanceKpScore[i][0] += kpF[j];
					SentanceKpScore[i][1] = (kpL[j] >= 2) ? 1 : 0;
					SentanceKpScore[i][2] = --ProperName[j];
				}
			}
		}
		return SentanceKpScore;
	}

}
