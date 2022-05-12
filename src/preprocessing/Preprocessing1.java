package preprocessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import KPminer.Extractor;
import utilities.AraNormalizer;
import utilities.DiacriticsRemover;
import utilities.LightStemmer10;
import utilities.LightStemmer2;
import utilities.PunctuationsRemover;
import utilities.RootStemmer;
import utilities.SentenceDetector;
import utilities.StanfordPOSTagger;
import utilities.TrainedTokenizer;
import word.Words;

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
	private String[] light10Tokens;
	private String[] keyPhrses;

	public TrainedTokenizer tok;
	public RootStemmer rs;
	public AraNormalizer arn;
	public DiacriticsRemover dr;
	public PunctuationsRemover pr;
	public LightStemmer10 ls10;
	public LightStemmer2 ls2;
	public SentenceDetector sd;
	public StanfordPOSTagger stf;

	public Preprocessing1() {
		tok = new TrainedTokenizer();
		rs = new RootStemmer();
		arn = new AraNormalizer();
		dr = new DiacriticsRemover();
		pr = new PunctuationsRemover();
		ls10 = new LightStemmer10();
		ls2 = new LightStemmer2();
		sd = new SentenceDetector();
		stf = new StanfordPOSTagger();
	}

	public Preprocessing1(String arabictext) throws IOException, ClassNotFoundException {
		this();

		arabictext = arabictext.trim();
		originalText = arabictext;
		lightText = "";

		// Sentences
		List<String> originalSentencesList = new ArrayList<String>();
		List<String> light10SentencesList = new ArrayList<String>();
		List<String> rootSentencesList = new ArrayList<String>();

		// Paragraphs
		String[] paragraphs = arabictext.split("(?<=\\.| ؟ |!)(\\s*)((\\r?\\n){2,})");
		int NO_PARAGRAPHS = paragraphs.length;
		paragraphsSentences = new String[NO_PARAGRAPHS][];

		// Sentences' Tokens
		List<String[]> light10SentencesTokensList = new ArrayList<String[]>();
		List<String[]> rootSentencesTokensList = new ArrayList<String[]>();

		// light10SentencesTokens = new String[NO_SENTENCES][];
		// rootSentencesTokens = new String[NO_SENTENCES][];

		// Tokens
		Set<String> tokensSet = new HashSet<String>();
		Set<String> light10TokensSet = new HashSet<String>();
		Set<String> rootTokensSet = new HashSet<String>();

		for (int i = 0; i < NO_PARAGRAPHS; i++) {
			String[] sentences = paragraphsSentences[i] = sd.detectSentences(paragraphs[i]);
			int NO_SENTENCES = sentences.length;

			for (int j = 0; j < NO_SENTENCES; j++) {
				String normalizedSentence = arn.normalize(sentences[j]);
				normalizedSentence = dr.removeDiacritics(normalizedSentence);
				normalizedSentence = pr.removePunctuations(normalizedSentence);

				String[] sentenceTokens = tok.tokenize(normalizedSentence);
				int NO_TOKENS = sentenceTokens.length;

				List<String> light10SentenceTokens = new ArrayList<String>();
				List<String> rootSentenceTokens = new ArrayList<String>();
				// String[] light10SentenceTokens = new String[NO_TOKENS];
				// String[] rootSentenceTokens = new String[NO_TOKENS];

				// light10SentencesTokens[j] = new String[NO_TOKENS];
				// rootSentencesTokens[j] = new String[NO_TOKENS];

				String light10Sentence = "";
				String rootSentence = "";

				for (int k = 0; k < NO_TOKENS - 1; k++) {
					if (Words.STOP_WORDS.contains(sentenceTokens[k]))
						continue;

					String light10Token = ls10.findStem(sentenceTokens[k]);
					String rootToken = rs.findRoot(sentenceTokens[k]);

					// Text
					lightText += " " + light10Token;

					// Sentences
					light10Sentence += " " + light10Token;
					rootSentence += " " + rootToken;

					// Tokens
					tokensSet.add(sentenceTokens[k]);
					light10TokensSet.add(light10Token);
					rootTokensSet.add(rootToken);

					// Sentences' Tokens
					light10SentenceTokens.add(light10Token);
					rootSentenceTokens.add(rootToken);
				}
				// Text
				lightText += ".";

				// Sentences
				light10Sentence += ".";
				rootSentence += ".";
				
				originalSentencesList.add(sentences[j]);
				light10SentencesList.add(light10Sentence.trim());
				rootSentencesList.add(rootSentence.trim());
				// light10Sentences[j] = light10Sentence.trim();
				// rootSentences[j] = rootSentence.trim();

				light10SentencesTokensList.add(light10SentenceTokens.toArray(new String[light10SentenceTokens.size()]));
				rootSentencesTokensList.add(rootSentenceTokens.toArray(new String[rootSentenceTokens.size()]));

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
		originalSentences = originalSentencesList.toArray(new String[originalSentencesList.size()]);
		light10Sentences = light10SentencesList.toArray(new String[light10SentencesList.size()]);
		rootSentences = rootSentencesList.toArray(new String[rootSentencesList.size()]);

		// Tokens
		tokens = tokensSet.toArray(new String[tokensSet.size()]);
		light10Tokens = light10TokensSet.toArray(new String[light10TokensSet.size()]);
		rootTokens = rootTokensSet.toArray(new String[rootTokensSet.size()]);

		// Sentences' Tokens
		light10SentencesTokens = light10SentencesTokensList.toArray(new String[light10SentencesTokensList.size()][]);
		rootSentencesTokens = rootSentencesTokensList.toArray(new String[rootSentencesTokensList.size()][]);

		Extractor extractor = new Extractor();
		keyPhrses = extractor.getTopN(7, lightText, true);
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
	 * @return the light10Tokens
	 */
	public String[] getLight10Tokens() {
		return light10Tokens;
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

	public String[] getKeyPhrase() {
		// TODO Auto-generated method stub
		return keyPhrses;
	}

}
