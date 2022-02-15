package textrank;

import java.io.IOException;

import preprocessing.Preprocessing;

public class Textrank {
	private Preprocessing pre;
	private String summarizedText;
	
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
	
	//Words in the sentence such as “therefore, finally and thus” can be a good indicators of significant content
	private int cueWords()
	{
		return 0;
	}
	
	//Words that are used to emphasize or focus on special idea such as ”have outstanding, and support for”
	private int positiveKeyWords()
	{
		return 0;
	}
	
	//Existence of numerical data in the sentence.
	private int sentenceInclusionOfNumericalData()
	{
		return 0;
	}
	
	//Words that serve as an explanation words such as “for example”
	private int occurrenceOfNonEssentialInformation()
	{
		return 0;
	}
}
