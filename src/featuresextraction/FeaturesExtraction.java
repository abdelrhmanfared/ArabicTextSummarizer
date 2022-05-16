package featuresextraction;

import java.io.IOException;

import feature.*;
import preprocessing.Preprocessing1;

public class FeaturesExtraction {
	
	
	public FeaturesExtraction(String text, String title) throws ClassNotFoundException, IOException {
		// TODO Auto-generated constructor stub
		Preprocessing1 pre = new Preprocessing1(text);
		
		KeyPhrases f1 = new KeyPhrases(pre);
		SentenceLocation f2 = new SentenceLocation(pre);
		TitleSimilarity f3 = new TitleSimilarity(pre, title);
		SentenceCenterality f4 = new SentenceCenterality(pre);
		SentenceLength f5 = new SentenceLength(pre);
		CueWords f6 = new CueWords(pre);
		StrongWords f7 = new StrongWords(pre);
		NumberExistence f8 = new NumberExistence(pre);
		WeakWords f9 = new WeakWords(pre);
		
		
	}

}
