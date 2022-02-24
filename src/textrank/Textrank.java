package textrank;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import preprocessing.Preprocessing;
import utilities.AraNormalizer;
import utilities.TrainedTokenizer;

public class Textrank {
	private Preprocessing pre;
	private String summarizedText;
	public AraNormalizer arn = new AraNormalizer();
	public static TrainedTokenizer tok = new TrainedTokenizer();

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
	@SuppressWarnings("unused")
	private int keyPhrases()
	{
		return 0;
	}
	
	//Relating to the position of a sentence to the paragraph and document
	@SuppressWarnings("unused")
	private int sentencelocation()
	{
		return 0;
	}
	
	//Similarity or overlapping between a given sentence and the document title.
	@SuppressWarnings("unused")
	private int similarityWithTitle()
	{
		return 0;
	}
	
	//The similarity or the overlapping between a sentence and other sentences in the document
	@SuppressWarnings("unused")
	private int sentenceCentrality()
	{
		return 0;
	}
	
	//Counting the number of words in the sentence (can be used to classify sentence as too short or too long)
	@SuppressWarnings("unused")
	private int sentenceLength()
	{
		return 0;
	}
	
	//Words in the sentence such as â€œtherefore, finally and thusâ€� can be a good indicators of significant content
	@SuppressWarnings("unused")
	private int cueWords()
	{
		return 0;
	}
	
	//Words that are used to emphasize or focus on special idea such as â€�have outstanding, and support forâ€�
	public double [] positiveKeyWords(String [] sentance)
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
				Sentance_Score[i] = ((double)(freq[i]*1.0)/Total);
			}
			return Sentance_Score;
		}
	
	//Existence of numerical data in the sentence.
	public  double[] numberScore (String [] sentences, String[][] Word) throws  IOException	{
		int sen[] = new int [sentences.length] , Num = 0;
		for(int i=0;i<sentences.length;i++) {
			Word[i] = tok.tokenize(sentences[i]);
		}
		for (int i=0;i<sentences.length;i++) {
			for (int j=0;j<Word[i].length;j++) {
				// when the word contain data ascii code between 0 to 7F  then this sentance contain numberical data 
				if (Word[i][j].matches("^\\d+[^\\x00-\\x7F]*$")) {
					Num++;
					sen[i]++;
				}
			}
		}
		double Sentance_Score [] = new double[sentences.length];
		if (Num == 0)return Sentance_Score;
		for (int i=0;i<sentences.length;i++) {
			Sentance_Score[i] = (double)(sen[i]*1.0/Num);
		}
		return Sentance_Score;
	}
	
	//Words that serve as an explanation words such as â€œfor exampleâ€�
	private int occurrenceOfNonEssentialInformation()
	{
		return 0;
	}
}
