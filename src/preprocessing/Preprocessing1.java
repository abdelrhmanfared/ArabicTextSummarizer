package preprocessing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import KPminer.Extractor;
import utilities.AraNormalizer;
import utilities.DiacriticsRemover;
import utilities.LightStemmer10;
import utilities.PunctuationsRemover;
import utilities.RootStemmer;
import utilities.SentenceDetector;
import utilities.StanfordPOSTagger;
import utilities.TrainedTokenizer;
import word.Words;

public class Preprocessing1 {
//	private String lightText;
	private String originalText;
//	private String normalizedText;
	private String[][] paragraphsSentences;

	private List<String> originalSentencesList;
	private List<String> normalizedSentencesList;
	private List<String> light10SentencesList;
	private List<String> rootSentencesList;

	private List<List<String>> normalizedSentencesTokensList;
	private List<List<String>> light10SentencesTokensList;
	private List<List<String>> rootSentencesTokensList;

//	private Set<String> tokensSet;
	private Set<String> light10TokensSet;
	private Set<String> rootTokensSet;

	private String[] keyPhrases;
	private String[] light10KeyPhrases;

	public static TrainedTokenizer tok = new TrainedTokenizer();
	public static RootStemmer rs = new RootStemmer();
	public static AraNormalizer arn = new AraNormalizer();
	public static DiacriticsRemover dr = new DiacriticsRemover();
	public static PunctuationsRemover pr = new PunctuationsRemover();
	public static LightStemmer10 ls10 = new LightStemmer10();
	public static SentenceDetector sd = new SentenceDetector();
	public static StanfordPOSTagger stf = new StanfordPOSTagger();
	public static Extractor extractor = new Extractor();

	public Preprocessing1() {
	}

	public Preprocessing1(String arabictext) throws IOException, ClassNotFoundException {
		init();

//		long startTime = System.currentTimeMillis();
		arabictext = arabictext.trim();
		originalText = arabictext;
//		lightText = "";
//		normalizedText = "";

		// Detect Paragraphs
		String[] paragraphs = arabictext.split("(?<=\\.| ؟ |!)(\\s*)((\\r?\\n)+)");
//		String[] paragraphs = arabictext.split("(\\r?\\n)+");
		int NO_PARAGRAPHS = paragraphs.length;
		paragraphsSentences = new String[NO_PARAGRAPHS][];

		// Iterate over paragraphs
		for (int i = 0; i < NO_PARAGRAPHS; i++) {
			// Detect sentences in the paragraph
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

				String light10Sentence = "";
				String rootSentence = "";

				for (int k = 0; k < NO_TOKENS - 1; k++) {
					if (Words.STOP_WORDS.contains(sentenceTokens[k]))
						continue;

					String light10Token = ls10.findStem(sentenceTokens[k]);
					String rootToken = rs.findRoot(sentenceTokens[k]);

					// Text
//					lightText += " " + light10Token;
//					normalizedText += " " + sentenceTokens[k];

					// Sentences
					light10Sentence += " " + light10Token;
					rootSentence += " " + rootToken;

					// Tokens
//					tokensSet.add(sentenceTokens[k]);
					light10TokensSet.add(light10Token);
					rootTokensSet.add(rootToken);

					// Sentences' Tokens
					light10SentenceTokens.add(light10Token);
					rootSentenceTokens.add(rootToken);
				}
				// Text
//				lightText += ".";
//				normalizedText += ".";

				// Sentences
				light10Sentence += ".";
				rootSentence += ".";

				originalSentencesList.add(sentences[j]);
				normalizedSentencesList.add(normalizedSentence);
				light10SentencesList.add(light10Sentence.trim());
				rootSentencesList.add(rootSentence.trim());

				// Sentences' Tokens
				normalizedSentencesTokensList
						.add(Arrays.asList(Arrays.copyOf(sentenceTokens, sentenceTokens.length - 1)));
				light10SentencesTokensList.add(light10SentenceTokens);
				rootSentencesTokensList.add(rootSentenceTokens);
			}
		}

		// Text
//		lightText = lightText.trim();
//		normalizedText = normalizedText.trim();

//		long stopTime = System.currentTimeMillis();
//		System.out.println("Preprocessing Time : " + (stopTime - startTime) + "ms");

//		startTime = System.currentTimeMillis();

		keyPhrases = extractor.getTopN(7, originalText, true);// new String[]{"لغه عرب

		light10KeyPhrases = new String[keyPhrases.length];

		for (int i = 0; i < keyPhrases.length; i++) {
			String[] tokens = tok.tokenize(keyPhrases[i]);
			String stem = "";
			for (int j = 0; j < tokens.length; j++)
				stem += " " + ls10.findStem(tokens[j]);
			stem = stem.trim();
			light10KeyPhrases[i] = stem;
		}
//		stopTime = System.currentTimeMillis();
//		System.out.println("keyphrases extraction Time : " + (stopTime - startTime) + "ms");
	}

	private void init() throws ClassNotFoundException, IOException {
		// Sentences
		originalSentencesList = new ArrayList<String>();
		normalizedSentencesList = new ArrayList<String>();
		light10SentencesList = new ArrayList<String>();
		rootSentencesList = new ArrayList<String>();

		// Sentences' Tokens
		normalizedSentencesTokensList = new ArrayList<List<String>>();
		light10SentencesTokensList = new ArrayList<List<String>>();
		rootSentencesTokensList = new ArrayList<List<String>>();

		// Tokens
//		tokensSet = new HashSet<String>();
		light10TokensSet = new HashSet<String>();
		rootTokensSet = new HashSet<String>();
	}

	/**
	 * @return the lightText
	 */
//	public String getLightText() {
//		return lightText;
//	}

	/**
	 * @return the originalText
	 */
	public String getOriginalText() {
		return originalText;
	}

	/**
	 * @return the paragraphsSentences
	 */
	public String[][] getParagraphsSentences() {
		return paragraphsSentences;
	}

	/**
	 * @return the originalSentencesList
	 */
	public List<String> getOriginalSentencesList() {
		return originalSentencesList;
	}

	/**
	 * @return the normalizedSentencesList
	 */
	public List<String> getNormalizedSentencesList() {
		return normalizedSentencesList;
	}

	/**
	 * @return the light10SentencesList
	 */
	public List<String> getLight10SentencesList() {
		return light10SentencesList;
	}

	/**
	 * @return the rootSentencesList
	 */
	public List<String> getRootSentencesList() {
		return rootSentencesList;
	}

	/**
	 * @return the normalizedSentencesTokensList
	 */
	public List<List<String>> getNormalizedSentencesTokensList() {
		return normalizedSentencesTokensList;
	}

	/**
	 * @return the light10SentencesTokensList
	 */
	public List<List<String>> getLight10SentencesTokensList() {
		return light10SentencesTokensList;
	}

	/**
	 * @return the rootSentencesTokensList
	 */
	public List<List<String>> getRootSentencesTokensList() {
		return rootSentencesTokensList;
	}

	/**
	 * @return the tokensSet
	 */
//	public Set<String> getTokensSet() {
//		return tokensSet;
//	}

	/**
	 * @return the light10TokensSet
	 */
	public Set<String> getLight10TokensSet() {
		return light10TokensSet;
	}

	/**
	 * @return the rootTokensSet
	 */
	public Set<String> getRootTokensSet() {
		return rootTokensSet;
	}

	/**
	 * @return the keyPhrses
	 */
	public String[] getKeyPhrases() {
		return keyPhrases;
	}

	/**
	 * @return the keyPhrses
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public String[] getLight10KeyPhrases() throws FileNotFoundException, IOException {
		// عصرنا","مدن","حديث","علم","اهل","لغه
		// عرب","لغه"};
		
		return light10KeyPhrases;
	}
}