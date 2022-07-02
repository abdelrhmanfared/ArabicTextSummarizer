package feature;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		String[] keyPhrases = pre.getLight10KeyPhrases();
		List<String> light10Sentences = pre.getLight10SentencesList();

		// key phrase frequency
		double[] kpF = KeyphraseFrequency(light10Sentences, keyPhrases);
		// key phrase length
		int[] kpL = KeyphraseLength(keyPhrases);
		// proper names
		int[] ProperName = ProperName(pre.getKeyPhrases(), pre);

		scoreBasedFeature = KpScore(kpL, ProperName, kpF, keyPhrases, light10Sentences);
		svmFetures = KpScoreSVM(kpL, ProperName, kpF, keyPhrases, light10Sentences);
	}

	private double[] KeyphraseFrequency(List<String> light10Sentences, String[] keyPhrases) {
		int totalKPF = 0;
		int SKP[] = new int[keyPhrases.length];
		for (int i = 0; i < keyPhrases.length; i++) {
			Pattern pattern = Pattern.compile("\\b" + keyPhrases[i] + "\\b");
			for (int j = 0; j < light10Sentences.size(); j++) {
				Matcher matcher = pattern.matcher(light10Sentences.get(j));
				int count = (int) matcher.results().count();
				if (count > 0) {
					SKP[i]++;
					totalKPF += count;
				}
			}
		}

		double KPF[] = new double[keyPhrases.length];
		if (totalKPF == 0)
			return KPF;
		for (int i = 0; i < KPF.length; i++)
			KPF[i] = (SKP[i] * 1.0 / totalKPF);

		// TODO Auto-generated method stub
		return KPF;
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

	private int[] ProperName(String[] keyPhrases, Preprocessing1 pre) throws IOException, ClassNotFoundException {

		// POS Tagging
// In English data, determiners (DT), nouns (NN, NNS, NNP etc.),
// verbs (VB, VBD, VBP etc.), prepositions (IN) might be more frequent.
//		String txt = pre.getOriginalText();
//		
//		long startTime = System.currentTimeMillis();
//		String pos = pre.stf.tagText(txt);
//		long stopTime = System.currentTimeMillis();
//		System.out.println("Tagging Time : " + (stopTime - startTime) + "ms");
//		
//		String POS_TAGS[] = pos.split("\\s");
//
//		int pN[] = new int[keyPhrases.length];
//		Arrays.fill(pN, 1);
//
//		for (String tag : POS_TAGS) {
//			String[] word_tag = tag.split("/");
//			if (word_tag[1].equals("NNP") || word_tag[1].equals("NNPS")) {
//
//				String word = pre.arn.normalize(word_tag[0]);
//				word = pre.dr.removeDiacritics(word);
////				word = pre.ls10.findStem(word);

//				for (int j = 0; j < keyPhrases.length; j++) {
////					if (keyPhrases[j].matches(".*\\b" + word + "\\b.*")) {
//					if (keyPhrases[j].contains(word)) {
//						pN[j] = 2;
//					}
//				}
//			}
//
//		}
//
//		return pN;
		
//		long startTime = System.currentTimeMillis();
		
		int pN[] = new int[keyPhrases.length];
		Arrays.fill(pN, 1);
		for(int i=0;i<keyPhrases.length;i++)
		{
			String tag = Preprocessing1.stf.tagText(keyPhrases[i]);
			if(tag.contains("NNP"))
				pN[i]=2;
		}
		
//		long stopTime = System.currentTimeMillis();
//		System.out.println("Tagging Time : " + (stopTime - startTime) + "ms");
		
		return pN;
	}

	private double[] KpScore(int[] kpL, int[] ProperName, double[] kpF, String[] keyPhrases,
			List<String> light10Sentences) {
		double KpScore[] = new double[keyPhrases.length];
		for (int i = 0; i < keyPhrases.length; i++) {
			KpScore[i] = ProperName[i] * kpF[i] * 1.0 * Math.sqrt(kpL[i]);
		}

		double SentanceKpScore[] = new double[light10Sentences.size()];

		for (int i = 0; i < keyPhrases.length; i++) {
			Pattern pattern = Pattern.compile(".*\\b" + keyPhrases[i] + "\\b.*");
			for (int j = 0; j < light10Sentences.size(); j++) {
				Matcher matcher = pattern.matcher(light10Sentences.get(j));
				if (matcher.find())
					SentanceKpScore[j] += KpScore[i];
			}
		}
		return SentanceKpScore;
	}

	private double[][] KpScoreSVM(int[] kpL, int[] ProperName, double[] kpF, String[] keyPhrases,
			List<String> light10Sentences) {
		double SentanceKpScore[][] = new double[light10Sentences.size()][3];

		for (int i = 0; i < keyPhrases.length; i++) {
			Pattern pattern = Pattern.compile(".*\\b" + keyPhrases[i] + "\\b.*");
			for (int j = 0; j < light10Sentences.size(); j++) {
				Matcher matcher = pattern.matcher(light10Sentences.get(j));
				if (matcher.find()) {
					SentanceKpScore[j][0] += kpF[i];
					SentanceKpScore[j][1] = (kpL[i] >= 2) ? 1 : 0;
					SentanceKpScore[j][2] = (ProperName[i] == 2) ? 1 : 0;
				}
			}
		}
		return SentanceKpScore;
	}

}
