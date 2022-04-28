package textrank;

public class Pair {
public String term;
public Integer Count;

public Pair(String term,Integer Count) {
	this.term = term;
	this.Count = Count;
}

public String getTerm() {
	return term;
}
public Integer getCount() {
	return Count;
}

public void setTerm(String term) {
	this.term = term;
}
public void setCount(Integer Count) {
	this.Count = Count;
}
}
