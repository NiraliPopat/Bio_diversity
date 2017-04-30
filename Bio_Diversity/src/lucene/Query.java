package lucene;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

public class Query {
	
	//@SuppressWarnings("null")
	public static <E> void main(String[] args) throws Exception {
		
		MetaMapApi api = new MetaMapApiImpl();//"C:/Users/NiraliPopat/Downloads/public_mm_win32_main_2014/public_mm/bin/metamap14");
		
		//String terms= "GENE and PATHWAY ICAM-1|TNF-ALPHA|CD14|E-SELECTIN";
		
		api.setOptions("-yc");
		
		//System.out.println(terms);
		
		StandardTokenizer stdToken = new StandardTokenizer();
		  //EnglishMinimalStemmer stemmer = new EnglishMinimalStemmer();
		     //stdToken.setReader(new StringReader("Some stuff that is in need of analysis. stuff patients PATIENT d > 0.5 Dnn>Bnn D.N.A diseases heart attacks at cl-fo"));
		   
		     

		     //You're code starts here
		final List<String> stopWords = new ArrayList<>(); 
		String f = "E:/stopwords_en.txt";
		      
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				    
	    	  String topic;
			  //int qid = 200;//cntr=0;
			  while ((topic = br.readLine()) != null) {
				  stopWords.add(topic.trim());
			  }
	      }
		final CharArraySet stopSet = new CharArraySet(stopWords, false);
		
		
		
		String file = "E:/Nirali/Nirali/2007/2007topics (copy).txt"; //Original Query File
		PrintWriter writer = new PrintWriter("E:/Nirali/Nirali/2007/topics_nq.txt", "UTF-8");
		try (BufferedReader br = new BufferedReader(new FileReader(file ))) {
		    String line;
		    String qid="200";
		    String qe = "";
		    
		    while ((line = br.readLine()) != null) {
		    	String newQuery = "";
		    	String cnewQuery = "";

		    	HashSet  conceptid = new HashSet();
		    	HashSet  ccid = new HashSet();
				//HashSet<String> = null;
		   
		    	String[] topic = line.split(">");
		    	if(topic.length == 2){
		    		qid = topic[0].substring(1);		    		
				    String qtopic = topic[1];
				    String[] qElement = StringUtils.substringsBetween(qtopic, "[", "]");
				    qtopic = qtopic.replace("[", "");
				    qtopic = qtopic.replace("]", "");
				    
				    //writer.println(qid + ":" + qElement[0]);
				    qe = qElement[0].toLowerCase();
				    
			    	System.out.println(qid +"........ " + qElement[0]+"........ " + qtopic);
			    	

					 //--------TEXT PROCESSING------------
					  TokenStream tokenStream;
					  stdToken.setReader(new StringReader(qtopic));
					  
					  tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(stdToken))), stopSet);
					  			    
				      //tokenStream = new PorterStemFilter(tokenStream);
				      tokenStream.reset();
				     //int l=0;
				     String term="";
				     StringBuilder sb = new StringBuilder();
				     OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
				     CharTermAttribute charTermAttr = tokenStream.getAttribute(CharTermAttribute.class);
				        try{
				            while (tokenStream.incrementToken()) {
				                if (sb.length() > 0) {
				                    sb.append(" ");
				                }
				                term = charTermAttr.toString();
				                if(term.contains(".") && !StringUtils.isNumeric(term)){
				                	term=term.replaceAll("\\.", "");
				                	//sb.append(term);
				                }
				                //else{
				                	//term=term.replaceAll("\\.", "");
				                	//int l = stemmer.stem(charTermAttr.toString().toCharArray(), charTermAttr.toString().length());
					                //sb.append(charTermAttr.toString(), 0, l);
				                //}
				                
				                //l = stemmer.stem(term.toCharArray(), term.length());
				                sb.append(term);
				                /*sb.append(charTermAttr.toString());
				                String[] hl = charTermAttr.toString().split("-");
				                if (hl.length > 1){
				                	for(int j=0; j<hl.length; j++){
				                		sb.append(" " + hl[j]);
				                	}
				                	
				                	//sb.append(" " + charTermAttr.toString().split("-")[1]);
				                	//sb.append(charTermAttr.toString());
				                }*/
				            
				                
				            }
				        }
				        catch (IOException e){
				            System.out.println(e.getMessage());
				        }
				       //System.out.println(sb.toString());
					   tokenStream.close();
					  
		    	
				    	//-----------End of Element -------------
				
			    	//newQuery =  newQuery.substring(0, l-4); 
			    	//writer.println(qid+" " + sb.toString() + newQuery.toLowerCase()+ cnewQuery.toLowerCase());
					//writer.println(qid+" (" + sb.toString()+")^2 OR ("+ qes.toLowerCase() + newQuery.toLowerCase() +")");// + newQuery.toLowerCase());
					//writer.println(qid+" (" + sb.toString()+")^4 ("+ qes.toLowerCase() +")" + nq);// + newQuery.toLowerCase());
					writer.println(qid+" (" + sb.toString()+")");
					//writer2.println(qid+" (" + sb.toString()+") ("+ newq2.toLowerCase() +")");
					//writer2.println(qid+":" + sb.toString());
			    	    
			    	
			    	//System.out.println(qid+newQuery+" " + sb.toString());
			    	//System.out.println(qid +" "+ sb.toString());//+newQuery.toLowerCase()+ cnewQuery.toLowerCase());
					//System.out.println(qid+" (" + sb.toString()+")^2 OR ("+ qes.toLowerCase() + newQuery.toLowerCase() +")");
					System.out.println(qid+" (" + sb.toString()+")");
					//System.out.println(qid+" (" + sb.toString()+") ("+ newq.toLowerCase() +")");

		    	}
		    	
		    	
		       
		    }
		}
		writer.close();
		
	}
	
}
