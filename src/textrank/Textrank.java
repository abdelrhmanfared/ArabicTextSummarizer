package textrank;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import preprocessing.Preprocessing;
import utilities.AraNormalizer;

public class Textrank {
	private Preprocessing pre;
	private String summarizedText;
	AraNormalizer arn = new AraNormalizer();
	
	public String getSummarizedText()
	{
		return summarizedText;
	}
	
	public Textrank(String text) throws ClassNotFoundException, IOException
	{
			pre=new Preprocessing(text);
			
			//Extract features
			
			summarizedText = "";
	}
	
	//A short list of important terms that provide a condensed summary of the main topics of a document
	private int keyPhrases()
	{
		return 0;
	}
	
	//Relating to the position of a sentence to the paragraph and document
	private int sentencelocation()
	{
		return 0;
	}
	
	//Similarity or overlapping between a given sentence and the document title.
	private int similarityWithTitle()
	{
		return 0;
	}
	
	//The similarity or the overlapping between a sentence and other sentences in the document
	private int sentenceCentrality()
	{
		return 0;
	}
	
	//Counting the number of words in the sentence (can be used to classify sentence as too short or too long)
	private int sentenceLength()
	{
		return 0;
	}
	
	//Words in the sentence such as â€œtherefore, finally and thusâ€� can be a good indicators of significant content
	private int cueWords()
	{
		return 0;
	}
	
	//Words that are used to emphasize or focus on special idea such as â€�have outstanding, and support forâ€�
	private double [] positiveKeyWords(String [] sentance)
	{		
		String [] Postive_Words = {
				"ايد","تأييد ","اقر","اقرار","اثبت","اثبات","المثبت من","تحديد","تأكيد","دليل","بينة ","ايجاب","أدلة"
				,"المؤكد من","بالتأكيد","أكد","وثق","توكيد","برهان","شهادة","جزم","تقرير ","تحقيق" 
				,"تقرير", "جزم", "شهادة", "برهان", "توكيد", "من المصدق", "تصديق",		
				"صدق", "دلل", "حدد", "ح قق", "برهن", "شهد", "ذو معنى", "كل المعنى", "داللي"
		};
		for (String str : Postive_Words) {
			str = arn.normalize(str);
		}
		
		int Total = 0 , freq[] = new int [sentance.length];
		for (int i=0;i<sentance.length;i++) {
			for (int j=0;j<Postive_Words.length;j++) {
				Pattern P1 =  Pattern.compile(".*\\b"+Postive_Words[j]+"\\b.*");
				Matcher M = P1.matcher(sentance[i]);
				while(M.find()) {	
					Total++;
					freq[i]++;
				}
			}
		}	
			double Sentance_Score [] = new double[sentance.length];
			if (Total == 0)return Sentance_Score;
			for (int i=0;i<sentance.length;i++) {
				Sentance_Score[i] = ((double)(freq[i])/Total);
			}
			return Sentance_Score;
		}
	
	//Existence of numerical data in the sentence.
	private int sentenceInclusionOfNumericalData()
	{
		
	}
	
	//Words that serve as an explanation words such as â€œfor exampleâ€�
	private int occurrenceOfNonEssentialInformation()
	{
		return 0;
	}
}
