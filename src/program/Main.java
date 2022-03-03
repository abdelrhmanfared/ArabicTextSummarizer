package program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import preprocessing.Preprocessing;
import textrank.Textrank;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*Preprocessing test;
		try {
			test = new Preprocessing(
					 "لو أتيح لنا أن ننظر إلى مستقبل اللغة العربية فتُرى ماذا نجد؟، هل نجد لغة واحدة يكتبها ويتكلمها المتعلمون من أهل مصر وأهل العراق وأهل الشام وغيرهم من الأمم العربية بفروق ضئيلة لا تزيد على الفروق بين لغة أهل استراليا ولغة أهل إنجلترا، وهل تكون هذه اللغة قريبة من اللغة العربية التي أكتبها الآن قرب لغة الإنجليزي المتعلم الآن من لغة شكسبير؟، أم هل نجد لغات مختلفة؛ لغة في مصر وأخرى في العراق وأخرى في لبنان؟، مثلها كمثل اللغة الألمانية واللغة السويدية واللغة الهولندية في تقاربها وتباعدها، كل لغة متأقلمة بلهجة أهلها، ولا صلة بين أيها وبين لغة هذا المقال إلا كالصلة بين اللغة الألمانية واللغة اللاتينية. وبعبارة أخرى: هل ستحيا اللغة العربية وتنتشر أو ستموت وتندثر وتحل محلها لغات أخرى؟!."
					);
		String[] org = test.getOriginalText_sentences();
		String[] norm = test.getNormalized_sentences();
		String[] light = test.getLightText_sentences();
		String[] root = test.getRootText_sentences();
	      System.out.println("An error occurred.");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		String ArabicText = readfile("ArabicText.txt");
		try {
			Textrank tr = new Textrank(ArabicText);
			
			//SALMA 
			//*****************
//			double[] scores = tr.sentencelocation(ArabicText);
//			for(int i=0; i<scores.length; i++)
//				System.out.print("score: "+ scores[i]);
			//******************
			System.out.print(tr.getSummarizedText());
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String readfile(String filename) {
	    try {
	      File myObj = new File(filename);
	      Scanner myReader = new Scanner(myObj);
	      myReader.useDelimiter("\\Z");
	      String data = myReader.next();
	      myReader.close();
	      return data;
	    		  
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	      return null;
	    }
	    
	  }

}
