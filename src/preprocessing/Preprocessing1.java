package preprocessing;

import java.io.IOException;
import java.util.ArrayList;

import KPminer.Extractor;
import utilities.AraNormalizer;
import utilities.DiacriticsRemover;
import utilities.LightStemmer10;
import utilities.LightStemmer2;
import utilities.PunctuationsRemover;
import utilities.RootStemmer;
import utilities.SentenceDetector;
import utilities.TrainedTokenizer;

public class Preprocessing1 {
	private String lightText;
	private String originalText;

	private String[] originalSentences;
	private String[] light10Sentences;
	private String[] rootSentences;

	private String[][] paragraphsSentences;

	private String[][] light10SentencesTokens;
	private String[][] rootSentencesTokens;

	private String[] tokens;
	private String[] rootTokens;

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
		this();

		arabictext = arabictext.trim();
		originalText = arabictext;
		lightText = "";

		// Sentences
		ArrayList<String> originalSentencesList = new ArrayList<String>();
		ArrayList<String> light10SentencesList = new ArrayList<String>();
		ArrayList<String> rootSentencesList = new ArrayList<String>();

		// Paragraphs
		String[] paragraphs = arabictext.split("(?<=\\.| ؟ |!)(\\s*)((\\r?\\n){2,})");
		int NO_PARAGRAPHS = paragraphs.length;
		paragraphsSentences = new String[NO_PARAGRAPHS][];

		// Sentences' Tokens
		ArrayList<String[]> light10SentencesTokensList = new ArrayList<String[]>();
		ArrayList<String[]> rootSentencesTokensList = new ArrayList<String[]>();

		// light10SentencesTokens = new String[NO_SENTENCES][];
		// rootSentencesTokens = new String[NO_SENTENCES][];

		// Tokens
		ArrayList<String> tokensArrayList = new ArrayList<String>();
		ArrayList<String> rootTokensArrayList = new ArrayList<String>();

		for (int i = 0; i < NO_PARAGRAPHS; i++) {
			String[] sentences = paragraphsSentences[i] = sd.detectSentences(paragraphs[i]);
			int NO_SENTENCES = sentences.length;

			for (int j = 0; j < NO_SENTENCES; j++) {
				String normalizedSentence = arn.normalize(sentences[j]);
				normalizedSentence = dr.removeDiacritics(normalizedSentence);
				normalizedSentence = pr.removePunctuations(normalizedSentence);

				String[] sentenceTokens = tok.tokenize(normalizedSentence);
				int NO_TOKENS = sentenceTokens.length;

				String[] light10SentenceTokens = new String[NO_TOKENS];
				String[] rootSentenceTokens = new String[NO_TOKENS];
				// light10SentencesTokens[j] = new String[NO_TOKENS];
				// rootSentencesTokens[j] = new String[NO_TOKENS];

				String light10Sentence = "";
				String rootSentence = "";

				for (int k = 0; k < NO_TOKENS; k++) {
					String light10Token = ls10.findStem(sentenceTokens[k]);
					String rootToken = rs.findRoot(sentenceTokens[k]);

					// Text
					lightText += light10Token;

					// Sentences
					light10Sentence += " " + light10Token;
					rootSentence += " " + rootToken;

					// Tokens
					tokensArrayList.add(sentenceTokens[k]);
					rootTokensArrayList.add(rootToken);

					// Sentences' Tokens
					light10SentenceTokens[k] = light10Token;
					rootSentenceTokens[k] = rootToken;
				}

				originalSentencesList.add(sentences[j]);
				light10SentencesList.add(light10Sentence.trim());
				rootSentencesList.add(rootSentence.trim());
				// light10Sentences[j] = light10Sentence.trim();
				// rootSentences[j] = rootSentence.trim();

				light10SentencesTokensList.add(light10SentenceTokens);
				rootSentencesTokensList.add(rootSentenceTokens);

				// TESTING !!!!!
				// System.out.println(originalSentences[i]);
				// System.out.println(normalizedSentence);
				// System.out.println(light10Sentences[i]);
				// System.out.println(rootSentences[i]);
				// END TESTING !!!!!
			}
		}

		// Text
		lightText = lightText.trim();

		// Sentences
		originalSentences = new String[originalSentencesList.size()];
		originalSentences = originalSentencesList.toArray(originalSentences);
		light10Sentences = new String[light10SentencesList.size()];
		light10Sentences = light10SentencesList.toArray(light10Sentences);
		rootSentences = new String[rootSentencesList.size()];
		rootSentences = rootSentencesList.toArray(rootSentences);

		// Tokens
		tokens = new String[tokensArrayList.size()];
		tokens = tokensArrayList.toArray(tokens);
		rootTokens = new String[rootTokensArrayList.size()];
		rootTokens = rootTokensArrayList.toArray(rootTokens);

		// Sentences' Tokens
		light10SentencesTokens = new String[light10SentencesTokensList.size()][];
		light10SentencesTokens = light10SentencesTokensList.toArray(light10SentencesTokens);
		rootSentencesTokens = new String[rootSentencesTokensList.size()][];
		rootSentencesTokens = rootSentencesTokensList.toArray(rootSentencesTokens);

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
	 * @return the paragraphsSentences
	 */
	public String[][] getParagraphsSentences() {
		return paragraphsSentences;
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
		// TODO Auto-generated method stub
		return originalText;
	}

	public String[] KpMinnerWords(int number) {
		// TODO Auto-generated method stub
		Extractor extractor = new Extractor();
		String lightText = getLightText();
		String[] Words = extractor.getTopN(number, lightText, true);
		return Words;
	}

}
