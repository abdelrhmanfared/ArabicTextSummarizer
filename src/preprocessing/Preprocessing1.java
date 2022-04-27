package preprocessing;

import java.io.IOException;
import java.util.ArrayList;

import utilities.AraNormalizer;
import utilities.DiacriticsRemover;
import utilities.LightStemmer10;
import utilities.LightStemmer2;
import utilities.PunctuationsRemover;
import utilities.RootStemmer;
import utilities.SentenceDetector;
import utilities.TrainedTokenizer;

public class Preprocessing1 {
	private String[] originalSentences;
	private String[] light10Sentences;
	private String[] rootSentences;

	private String[][] light10SentencesTokens;
	private String[][] rootSentencesTokens;

	private String[] tokens;
	private String[] rootTokens;
	
	private String lightText;
	private String Orginal;
	public TrainedTokenizer tok;
	public RootStemmer rs;
	public AraNormalizer arn;
	public DiacriticsRemover dr;
	public PunctuationsRemover pr;
	public LightStemmer10 ls10;
	public LightStemmer2 ls2;
	public SentenceDetector sd;

	public Preprocessing1() {
		tok = new TrainedTokenizer();
		rs = new RootStemmer();
		arn = new AraNormalizer();
		dr = new DiacriticsRemover();
		pr = new PunctuationsRemover();
		ls10 = new LightStemmer10();
		ls2 = new LightStemmer2();
		sd = new SentenceDetector();
	}

	public Preprocessing1(String arabictext) throws IOException, ClassNotFoundException {
		tok = new TrainedTokenizer();
		rs = new RootStemmer();
		arn = new AraNormalizer();
		dr = new DiacriticsRemover();
		pr = new PunctuationsRemover();
		ls10 = new LightStemmer10();
		ls2 = new LightStemmer2();
		sd = new SentenceDetector();

		// Sentences
		arabictext = arabictext.trim();
		Orginal = arabictext;
		originalSentences = sd.detectSentences(arabictext);
		int NO_SENTENCES = originalSentences.length;

		light10Sentences = new String[NO_SENTENCES];
		rootSentences = new String[NO_SENTENCES];

		// Tokens
		ArrayList<String> tokensArrayList = new ArrayList<String>();
		ArrayList<String> rootTokensArrayList = new ArrayList<String>();

		// Sentences' Tokens
		light10SentencesTokens = new String[NO_SENTENCES][];
		rootSentencesTokens = new String[NO_SENTENCES][];
		
		lightText = "";
		
		for (int i = 0; i < NO_SENTENCES; i++) {
			String normalizedSentence = arn.normalize(originalSentences[i]);
			normalizedSentence = dr.removeDiacritics(normalizedSentence);
			normalizedSentence = pr.removePunctuations(normalizedSentence);

			String[] sentenceTokens = tok.tokenize(normalizedSentence);
			int NO_TOKENS = sentenceTokens.length;

			light10SentencesTokens[i] = new String[NO_TOKENS];
			rootSentencesTokens[i] = new String[NO_TOKENS];

			String light10Sentence = "";
			String rootSentence = "";

			for (int j = 0; j < NO_TOKENS; j++) {

				String light10Token = ls10.findStem(sentenceTokens[j]);
				String rootToken = rs.findRoot(sentenceTokens[j]);
				
				// Text
				lightText += light10Token;
				
				// Sentences
				light10Sentence += " " + light10Token;
				rootSentence += " " + rootToken;

				// Tokens
				tokensArrayList.add(sentenceTokens[j]);
				rootTokensArrayList.add(rootToken);

				// Sentences' Tokens
				light10SentencesTokens[i][j] = light10Token;
				rootSentencesTokens[i][j] = rootToken;

			}

			light10Sentences[i] = light10Sentence.trim();
			rootSentences[i] = rootSentence.trim();

			// TESTING !!!!!
			//System.out.println(originalSentences[i]);
			//System.out.println(normalizedSentence);
			//System.out.println(light10Sentences[i]);
			//System.out.println(rootSentences[i]);
			// END TESTING !!!!!
		}
		
		lightText = lightText.trim();
		
		tokens = new String[tokensArrayList.size()];
		tokens = tokensArrayList.toArray(tokens);
		rootTokens = new String[rootTokensArrayList.size()];
		rootTokens = rootTokensArrayList.toArray(rootTokens);

	}

	/**
	 * @return the originalSentences
	 */
	public String[] getOriginalSentences() {
		return originalSentences;
	}

	/**
	 * @return the light10Sentences
	 */
	public String[] getLight10Sentences() {
		return light10Sentences;
	}

	/**
	 * @return the rootSentences
	 */
	public String[] getRootSentences() {
		return rootSentences;
	}

	/**
	 * @return the lightSentencesTokens
	 */
	public String[][] getLight10SentencesTokens() {
		return light10SentencesTokens;
	}

	/**
	 * @return the rootSentencesTokens
	 */
	public String[][] getRootSentencesTokens() {
		return rootSentencesTokens;
	}

	/**
	 * @return the tokens
	 */
	public String[] getTokens() {
		return tokens;
	}

	/**
	 * @return the rootTokens
	 */
	public String[] getRootTokens() {
		return rootTokens;
	}

	/**
	 * @return the lightText
	 */
	public String getLightText() {
		return lightText;
	}

	public String getOrginal() {
		return Orginal;
	}

	public String getOrginal() {
		// TODO Auto-generated method stub
		return Orginal;
	}

}
