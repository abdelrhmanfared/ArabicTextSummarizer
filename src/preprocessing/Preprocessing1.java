package preprocessing;

import java.io.IOException;

import utilities.AraNormalizer;
import utilities.DiacriticsRemover;
import utilities.LightStemmer10;
import utilities.PunctuationsRemover;
import utilities.RootStemmer;
import utilities.TrainedTokenizer;

public class Preprocessing1 {
	public Preprocessing1(String arabictext) throws IOException, ClassNotFoundException {
		TrainedTokenizer tok = new TrainedTokenizer();
		RootStemmer rs = new RootStemmer();
		AraNormalizer arn = new AraNormalizer();
		DiacriticsRemover dr = new DiacriticsRemover();
		PunctuationsRemover pr = new PunctuationsRemover();
		LightStemmer10 ls10 = new LightStemmer10();
	}
}
