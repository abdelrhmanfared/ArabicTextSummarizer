package accuracy;

import java.util.ArrayList;

import utilities.AraNormalizer;

public class AccuracyMeasures {

	public float Recall; 
	public float Precision;	
	public float FScore;
	public String Generated;
	public String Referenced;
	
	public void Rouge1(String Generated,String Referenced) {
		this.Generated = Generated;
		this.Referenced = Referenced;
		
		String[] Gen = UniGram(this.Generated);
		String[] Ref = UniGram(this.Referenced);
		float refGen = CalculateSimlarity(Gen, Ref);

		this.Recall = refGen/Ref.length;
		this.Precision = refGen/Gen.length;
		this.FScore = (Float.isNaN((2*Recall*Precision)/(Recall+Precision))? 0 : (2*Recall*Precision)/(Recall+Precision));
	}
	public void Rouge2(String Generated,String Referenced) {
		this.Generated = Generated;
		this.Referenced = Referenced;
		
		String[] Gen = BiGram(Generated).toArray(new String[0]);
		String[] Ref = BiGram(Referenced).toArray(new String[0]);
		float refGen = CalculateSimlarity(Gen, Ref);
	
		this.Recall = refGen/Ref.length;
		this.Precision = refGen/Gen.length;
		this.FScore = (Float.isNaN((2*Recall*Precision)/(Recall+Precision))? 0 : (2*Recall*Precision)/(Recall+Precision));
		
	}
	public String[] UniGram(String Article) {
		AraNormalizer arn = new AraNormalizer();
		Article = arn.normalize(Article);
		String sentences[] = Article.trim().replace("\"", "").replaceAll(" ","").split("(?<=\\.| ؟ |!)");
		for(String s : sentences)
			s.replace(".","");
		return sentences;
	}
	public ArrayList<String> BiGram(String Article){
		String[] ArticleSentences = UniGram(Article);
		ArrayList<String> BiGramSentences = new ArrayList<String>();
		for(int i=0;i<ArticleSentences.length;i++)
		{
			if(ArticleSentences.length == 1)
				BiGramSentences.add(ArticleSentences[i]);
			if(i == ArticleSentences.length-1)
				break;
		  BiGramSentences.add(ArticleSentences[i]+ArticleSentences[i+1]);
			
		}
		return BiGramSentences;
	}
	public float CalculateSimlarity(String[] Gen,String[] Ref) {
		float refGen = 0;
		for(int i=0;i<Gen.length;i++)
			for(int j=0;j<Ref.length;j++) 
				if(Gen[i].equals(Ref[j]))
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
