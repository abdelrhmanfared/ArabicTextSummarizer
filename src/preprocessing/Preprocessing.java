package preprocessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import utilities.AraNormalizer;
import utilities.DiacriticsRemover;
import utilities.PunctuationsRemover;
import utilities.RootStemmer;
import utilities.SentenceDetector;
import utilities.TrainedTokenizer;
import utilities.LightStemmer10;

public class Preprocessing {
	private String original_text;
	private String[] original_paragraphs;
	private String normalizedText;
	private String[] tokens;
	private String rootText;
	private String lightText;

	
	public Preprocessing(String arabictext) throws IOException, ClassNotFoundException {
		TrainedTokenizer tok = new TrainedTokenizer();
		RootStemmer rs = new RootStemmer();
		AraNormalizer arn = new AraNormalizer();
		DiacriticsRemover dr = new DiacriticsRemover();
		PunctuationsRemover pr = new PunctuationsRemover();
		LightStemmer10 ls10 = new LightStemmer10();

		original_text = arabictext;
		original_text = original_text.trim();

		original_paragraphs = original_text.split("(?<=\\.| ؟ |!)(\\s*)((\\r?\\n){2,})");
		original_text = addSpacesAroundPeriods(original_text);

		normalizedText = arn.normalize(original_text);
		normalizedText = dr.removeDiacritics(normalizedText);
		normalizedText = pr.removePunctuations(normalizedText);
		tokens = tok.tokenize(normalizedText);

		rootText = "";
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equals(".")) {
				rootText = rootText + tokens[i];
			} else {
				String stem = rs.findRoot(tokens[i]);
				rootText = rootText + " " + stem;
			}
		}
		rootText = rootText.trim().replaceAll(" +", " ");
		//rootText = addSpacesAroundPeriods(rootText);

		lightText = "";
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equals(".")) {
				lightText = lightText + tokens[i];
			} else {
				String stem = ls10.findStem(tokens[i]);
				lightText = lightText + " " + stem;
			}
		}
		lightText = lightText.trim().replaceAll(" +", " ");
		//lightText = addSpacesAroundPeriods(lightText);

	}
	
	
	
	public String getOriginal_text() {
		return original_text;
	}
	public String[] getOriginal_paragraphs() {
		return original_paragraphs;
	}
	public String getNormalizedText() {
		return normalizedText;
	}
	public String[] getTokens() {
		return tokens;
	}
	public String getRootText() {
		return rootText;
	}
	public String getLightText() {
		return lightText;
	}

	public String[] getOriginalText_sentences() throws IOException {
		SentenceDetector sd = new SentenceDetector();
		String[] originalText_sentences;
		originalText_sentences = sd.detectSentences(original_text);
		//originalText_sentences = splitSentences(originalText_sentences);
		return originalText_sentences;
	}
	
	//SALMA
	//Sentence Detector for paragraphs
	public String[] SentencesOfParagraph(String paragraph) throws IOException {
		SentenceDetector sd = new SentenceDetector();
		String[] sentencesOfParagraph;
		sentencesOfParagraph = sd.detectSentences(paragraph);
		sentencesOfParagraph = splitSentences(sentencesOfParagraph);
		return sentencesOfParagraph;
	}
	
	public String[] getLightText_sentences() throws IOException {
		SentenceDetector sd = new SentenceDetector();
		String[] lightText_sentences;
		lightText_sentences = sd.detectSentences(lightText);
		//lightText_sentences = splitSentences(lightText_sentences);
		return lightText_sentences;
	}
	public String[] getRootText_sentences() throws IOException {
		SentenceDetector sd = new SentenceDetector();
		String[] rootText_sentences;
		rootText_sentences = sd.detectSentences(rootText);
		//rootText_sentences = splitSentences(rootText_sentences);
		return rootText_sentences;
	}
	public String[] getNormalized_sentences() throws IOException {
		SentenceDetector sd = new SentenceDetector();
		String[] normalized_sentences;
		normalized_sentences = sd.detectSentences(normalizedText);
		//normalized_sentences = splitSentences(normalized_sentences);
		return normalized_sentences;
	}
	public String[][] getrootSentencesTokens() throws IOException {
		TrainedTokenizer tok = new TrainedTokenizer();
		String[] rootText_sentences=getRootText_sentences();
		
		String[][] rootSentencesTokens;
		rootSentencesTokens = new String[rootText_sentences.length][];
		for (int i = 0; i < rootText_sentences.length; i++) {
			rootSentencesTokens[i] = tok.tokenize(rootText_sentences[i]);
		}
		return rootSentencesTokens;
	}
	public String[][] getlightSentencesTokens() throws IOException {
		TrainedTokenizer tok = new TrainedTokenizer();
		String[] lightText_sentences = getLightText_sentences();
		
		String[][] lightSentencesTokens;
		lightSentencesTokens = new String[lightText_sentences.length][];
		for (int i = 0; i < lightText_sentences.length; i++) {
			lightSentencesTokens[i] = tok.tokenize(lightText_sentences[i]);
		}
		return lightSentencesTokens;
	}

	public String[] getRootTextTokens() throws IOException {
		TrainedTokenizer tok = new TrainedTokenizer();
		String[] rootTextTokens;
		rootTextTokens = tok.tokenize(rootText);
		return rootTextTokens;
	}

	public String[] getNormalized_paragraphs() {
		AraNormalizer arn = new AraNormalizer();
		DiacriticsRemover dr = new DiacriticsRemover();
		PunctuationsRemover pr = new PunctuationsRemover();

		String[] normalized_paragraphs;
		normalized_paragraphs = new String[original_paragraphs.length];
		for (int i = 0; i < normalized_paragraphs.length; i++) {
			normalized_paragraphs[i] = arn.normalize(original_paragraphs[i]);
			normalized_paragraphs[i] = dr.removeDiacritics(normalized_paragraphs[i]);
			normalized_paragraphs[i] = pr.removePunctuations(normalized_paragraphs[i]);
		}
		return normalized_paragraphs;
	}



	private String addSpacesAroundPeriods(String text) {
		String Temp = new String();
		for (int i = 0; i < text.length() - 1; i++) {
			Temp += text.charAt(i);
			if (text.charAt(i) == '.' && text.charAt(i + 1) != ' ' && text.charAt(i + 1) != '.') {
				Temp += ' ';
			}
			/*if (text.charAt(i) != '.' && text.charAt(i + 1) == '.') {
				Temp += ' ';
			}*/
		}
		Temp += text.charAt(text.length() - 1);
		return Temp;
	}

	/*private String[] splitSentences(String[] sentences) {
		ArrayList<String> tempOriginal = new ArrayList<String>();
		for (int i = 0; i < sentences.length; i++) {
			String[] tempSentences = sentences[i].split("(?<=(؟|!))");
			tempOriginal.addAll(Arrays.asList(tempSentences));
		}
		return tempOriginal.toArray(sentences);
	}*/
}
