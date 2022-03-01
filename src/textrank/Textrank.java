package textrank;

import java.io.IOException;
import java.util.ArrayList;


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
	
//	//Relating to the position of a sentence to the paragraph and document
//	public  double[] sentencelocation(String text)
//	{
//		
//		int numberOfParagraphs = pre.getOriginal_paragraphs().length;
//		String[] paragraphs = pre.getOriginal_paragraphs();
//		String[][] sentences = new String[numberOfParagraphs][];
//		ArrayList<Double> SentenceLocationScores = new ArrayList<Double>();
//		int lastParagraph = numberOfParagraphs-1;
//		
//		//Detect the boundaries of each sentence for each paragraph.
//		for(int eachParagraph=0; eachParagraph<numberOfParagraphs; eachParagraph++)
//		{	try {
//				sentences[eachParagraph] = pre.SentencesOfParagraph(paragraphs[eachParagraph]);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				System.out.println("An error in sentence detection has occurred.");
//				e.printStackTrace();
//			}
//		}
//		
//		//Sentence locations cases.
//		for(int eachParagraph=0; eachParagraph<numberOfParagraphs; eachParagraph++)
//		{
//			for(int eachSentence=0; eachSentence<sentences[eachParagraph].length; eachSentence++)
//			{
//				if(eachParagraph == 0)
//				{
//					if(eachSentence == 0)
//						SentenceLocationScores.add(Double.valueOf(3));
//					else
//						SentenceLocationScores.add(Double.valueOf(1/Math.sqrt(Double.valueOf(eachSentence+1))));
//				}
//				
//				else if(eachParagraph == lastParagraph)
//				{
//					if(eachSentence==0)
//						SentenceLocationScores.add(Double.valueOf(2));
//					else
//						SentenceLocationScores.add(Double.valueOf(1/Math.sqrt(Double.valueOf(eachSentence+1))));
//				}
//				
//				else
//				{
//					if(eachSentence == 0)
//						SentenceLocationScores.add(Double.valueOf(1));
//					else
//						SentenceLocationScores.add(Double.valueOf(1/Math.sqrt(Double.valueOf( (eachSentence+1) + ((eachParagraph+1)*(eachParagraph+1)) ))));
//				}
//			}
//		}
//		
//		//Convert Double ArrayList --> double[] 
//		double[] scores = new double[SentenceLocationScores.size()];
//		for(int i=0; i<SentenceLocationScores.size(); i++)
//			scores[i]=SentenceLocationScores.get(i).doubleValue();
//		
//		return scores;
//	}
	
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
