package summarization;

import java.util.Comparator;

public class Score {
	public int index;
	public double score;

	public Score(int index, double score) {
		this.index = index;
		this.score = score;
	}
}

class Sortbyscore implements Comparator<Score> {

	// Method
	// Sorting in descending order of score number
	public int compare(Score a, Score b) {

		return Double.compare(b.score, a.score);
	}
}

//class Sortbyindex implements Comparator<Score> {
//
//	// Method
//	// Sorting in ascending order of index number
//	public int compare(Score a, Score b) {
//
//		return a.index - b.index;
//	}
//}

