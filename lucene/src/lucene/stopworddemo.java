package lucene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.TokenStreamToAutomaton;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.EnglishMinimalStemmer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
//import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;

public class stopworddemo {
	/*public String removeStopWords(String string) throws IOException 
    {
		//String string = "this is a str which has to remove words";
        StandardAnalyzer ana = new StandardAnalyzer();
        TokenStream tokenStream = new StandardTokenizer();
        StringBuilder sb = new StringBuilder();
        tokenStream = new StopFilter(tokenStream, ana.STOP_WORDS_SET);
        CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) 
        {
            if (sb.length() > 0) 
            {
                sb.append(" ");
            }
            sb.append(token.toString());
        }
        return sb.toString();
    }

    public static void main(String args[]) throws IOException
    {
          String text = "this is a java project written by james.";
          stopworddemo stopwords = new stopworddemo();
          stopwords.removeStopWords(text);

    }
*/
	public static void main(String[] args) throws IOException, ParseException { 
		Tokenizer stdToken = new StandardTokenizer();
		//Tokenizer stdToken = new WhitespaceTokenizer();
		String input = "Some[abc] stuff(def) abc/sf Ab|ce ab.ce.ef AB.EF.CEDWA. hopes that is in need of analysis. stuff hopes analysis axis oxen genesis bus buses mg3 patients PATIENT d > 0.5 Dnn>Bnn D.N.A diseases heart attacks at cl-fo D.N.A., kjihj,hjkk.";
	    System.out.println(input); 
	    //String input2 = input.replaceAll("[\\p{P}&&[^-.]]", " ");//replaceAll("[^a-zA-Z-. 0-9]", " ");
	    String input2 = input.replaceAll("-", ".zz");
	    System.out.println(input2); 
		//stdToken.setReader(new StringReader(input2));
	    stdToken.setReader(new StringReader(input2));
	     
		//StringBuilder output = new StringBuilder(input);
		//int delta=0;
	     TokenStream tokenStream;

	     //You're code starts here
	     final List<String> stopWords = new ArrayList<>(); /*Arrays.asList(
	    		   "a", "an", "and", "are", "as", "at", "be", "but", "by",
	    		   "for", "if", "in", "into", "is", "it",
	    		   "no", "not", "of", "on", "or", "such",
	    		   "that", "the", "their", "then", "there", "these",
	    		   "they", "this", "to", "was", "will", "with", "some"
	    		 );*/
	     String file = "E:/stopwords_en.txt";
	      
	      try (BufferedReader br = new BufferedReader(new FileReader(file ))) {
			    
	    	  String topic;
			  //int qid = 200;//cntr=0;
			  while ((topic = br.readLine()) != null) {
				  stopWords.add(topic.trim());
			  }
	      }
	    final CharArraySet stopSet = new CharArraySet(stopWords, false);
	     tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(stdToken))), stopSet);
	     
	    
	     tokenStream.reset();
	     EnglishMinimalStemmer stemmer = new EnglishMinimalStemmer();
	     //tokenStream = new PorterStemFilter(tokenStream);
	     
	     StringBuilder sb = new StringBuilder();
	     OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
	     CharTermAttribute charTermAttr = tokenStream.getAttribute(CharTermAttribute.class);
	     
	     /*while (tokenStream.incrementToken()) {
			    String term = charTermAttr.toString();
			    int start = offsetAttribute.startOffset();
			    int end = offsetAttribute.endOffset();
			    term=term.replaceAll("\\.", "");
			    output.replace(delta + start, delta + end, term);
			    delta += (term.length() - (end - start));
				  System.out.println(term +" " + start + " " + end);
			  }
			  tokenStream.close();

			System.out.println(output.toString());*/
	        try{
	            while (tokenStream.incrementToken()) {
	            	String term = charTermAttr.toString();
	                if (sb.length() > 0) {
	                    sb.append(" ");
	                }
	                if(term.contains(".zz")){
	                	term = term.replaceAll(".zz", "-");
	                	String[] terms=term.split("-");
	                	String at="";
	                	for(String t : terms){
	              			sb.append(t + " ");
	              			at = at+t;
	                	}
	                	
	                	sb.append(at + " ");
	                }
	                if(term.contains(".") && !term.matches(".*\\d+.*")){//&& StringUtils.isAlpha(term)){
	                	term=term.replaceAll("\\.", "");
	                	//sb.append(term);
	                }
	                //int l = stemmer.stem(charTermAttr.toString().toCharArray(), charTermAttr.toString().length());
	                int l = stemmer.stem(term.toCharArray(), term.length());
	                //sb.append(charTermAttr.toString(),0,l);
	                sb.append(term,0,l);
	                
	                
	                /*if (charTermAttr.toString().split("-").length > 1){
	                	sb.append(" " + charTermAttr.toString().split("-")[0],0,l-1);
	                	sb.append(" " + charTermAttr.toString().split("-")[1],0,l-1);
	                	//sb.append(charTermAttr.toString());
	                }*/
	            
	                
	            }
	        }
	        catch (IOException e){
	            System.out.println(e.getMessage());
	        }
	        System.out.println(sb.toString());
	     //And ends here

	     /*CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
	     while (tokenStream.incrementToken()) {
	         System.out.println(token.toString());
	     }*/
	     //tokenStream.close();
	     /*String term = "stuff patients diseases heart attacks";
	     EnglishMinimalStemmer stemmer = new EnglishMinimalStemmer();
	     char[] s = "attacks"
		System.out.println(stemmer.stem(s, 0));*/
	     //System.out.println(stemmer);
	     /*EnglishAnalyzer en_an = new EnglishAnalyzer();
	     QueryParser parser = new QueryParser("your_field", en_an);
	     String str = "stuff patients diseases heart attacks";
	     System.out.println("result: " + parser.parse(str));*/
		/*TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_5_0_0, new StringReader(string));
		CharArraySet stopSet = CharArraySet.copy(Version.LUCENE_5_0_0, StandardAnalyzer.STOP_WORD_SET);
		stopSet.add("add");
		stopSet.add("your");
		stopSet.add("stop");
		stopSet.add("words");
		tokenStream = new StopFilter(Version.LUCENE_5_0_0, tokenStream, stopSet);*/
		
		/*String input = "Some stuff that is in need of analysis. stuff patients PATIENT d>n Dnn>Bnn D.N.A diseases heart attacks at cl-fo";
		  ASCIIFoldingFilter analyzer = new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter());
		  StringBuilder output = new StringBuilder(input);
		  // in some cases, the analyzer will make terms longer or shorter.
		  // because of this we must track how much we have adjusted the text so far
		  // so that the offsets returned will still work for us via replace()
		  int delta = 0;

		  /*TokenStream ts = analyzer.tokenStream("bogus", new StringReader(input));
		  CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
		  OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);
		  ts.reset();*/
		  /*while (ts.incrementToken()) {
		    String term = termAtt.toString();
		    int start = offsetAtt.startOffset();
		    int end = offsetAtt.endOffset();
		    output.replace(delta + start, delta + end, term);
		    delta += (term.length() - (end - start));
			  System.out.println(termAtt.toString());
		  }
		  ts.close();

		System.out.println(output.toString());*/
	}

}

	       
