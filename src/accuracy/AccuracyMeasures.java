package accuracy;

import java.util.ArrayList;
import java.util.List;

import utilities.AraNormalizer;

public class AccuracyMeasures {

	public float Recall; 
	public float Precision;	
	public float FScore;
	public List<String> Generated;
	public List<String> Referenced;
	
	public void Rouge1(List<String> Generated,List<String> Referenced) {
		this.Generated = Generated;
		this.Referenced = Referenced;
		
		float refGen = CalculateSimlarity(Generated, Referenced);

		this.Recall = (Referenced.size()==0)? 0 : refGen/Referenced.size();
		this.Precision = (Generated.size()==0)? 0 : refGen/Generated.size();
		this.FScore = (Float.isNaN((2*Recall*Precision)/(Recall+Precision))? 0 : (2*Recall*Precision)/(Recall+Precision));
	}
	public void Rouge2(List<String> Generated,List<String> Referenced) {
		this.Generated = Generated;
		this.Referenced = Referenced;
		
		ArrayList<String> Gen = BiGram(Generated);
		ArrayList<String> Ref = BiGram(Referenced);
		float refGen = CalculateSimlarity(Gen, Ref);
	
		this.Recall = (Referenced.size()==0)? 0 : refGen/Referenced.size();
		this.Precision = (Generated.size()==0)? 0 : refGen/Generated.size();
		this.FScore = (Float.isNaN((2*Recall*Precision)/(Recall+Precision))? 0 : (2*Recall*Precision)/(Recall+Precision));
		
	}
//	public String[] UniGram(String Article) {
//		AraNormalizer arn = new AraNormalizer();
//		Article = arn.normalize(Article);
//		String sentences[] = Article.trim().replace("\"", "").replaceAll(" ","").split("(?<=\\.| ؟ |!)");
//		for(String s : sentences)
//			s.replace(".","");
//		return sentences;
//	}
	public ArrayList<String> BiGram(List<String> ArticleSentences){
		ArrayList<String> BiGramSentences = new ArrayList<String>();
		for(int i=0;i<ArticleSentences.size();i++)
		{
			if(ArticleSentences.size() == 1)
				BiGramSentences.add(ArticleSentences.get(i));
			if(i == ArticleSentences.size()-1)
				break;
		  BiGramSentences.add(ArticleSentences.get(i)+ArticleSentences.get(i));
			
		}
		return BiGramSentences;
	}
	public float CalculateSimlarity(List<String> Gen, List<String> Ref) {
		float refGen = 0;
		for(int i=0;i<Gen.size();i++)
			for(int j=0;j<Ref.size();j++) 
				if(Gen.get(i).contains(Ref.get(j)))
					refGen++;
		return refGen;
	}
	public float getRecall() {
		return Recall;
	}
	public float getPrecision() {
		return Precision;
	}
	public float getFScore() {
		return FScore;
	}
}
