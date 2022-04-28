package textrank;

public class Pair {
public String term;
public Integer Count;
public double Wtf;
public double Widf;

public Pair(String term,Integer Count) {
	this.term = term;
	this.Count = Count;
}
public Pair(String term,double Wtf,double Widf) {
	this.term = term;
	this.Wtf = Wtf;
	this.Widf = Widf;
}

public String getTerm() {
	return term;
}
public Integer getCount() {
	return Count;
}
public  double getWtf() {
	return Wtf;
}
public  double getWidf() {
	return Widf;
}
public void setTerm(String term) {
	this.term = term;
}
public void setCount(Integer Count) {
	this.Count = Count;
}
public  void setWtf(double Wtf) {
	this.Wtf = Wtf;
}
public void setWidf(double Widf) {
	this.Widf = Widf;
}
}
