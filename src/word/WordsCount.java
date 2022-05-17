package word;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordsCount {
	public int[] NO_WORDS_IN_SENTENCES;
	public int total;

	public WordsCount(List<String> Sentences, String[] Words) {
		// TODO Auto-generated constructor stub
		NO_WORDS_IN_SENTENCES = new int[Sentences.size()];
		total = 0;

		for (String word : Words) {
			Pattern pattern = Pattern.compile("\\b" + word + "\\b");
			for (int i = 0; i < Sentences.size(); i++) {
				Matcher matcher = pattern.matcher(Sentences.get(i));

				int count = (int) matcher.results().count();
				NO_WORDS_IN_SENTENCES[i] += count;
				total += count;

			}
		}

	}

}
