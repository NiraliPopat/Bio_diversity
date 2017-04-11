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

public class Query2 {
	
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
		
		
		
		String file = "E:/Nirali/Nirali/2007/2007topics (copy).txt";
		PrintWriter writer = new PrintWriter("E:/Nirali/Nirali/2007/topics10.txt", "UTF-8");
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
					  
					  ///----------END OF Topic processin----------
			    	
					 //--------For Element-------------------
					   String qes = "";//("+qe;
				    	List<Result> resultList = api.processCitationsFromString(qe);
						
						Result result = resultList.get(0);
						/*List<AcronymsAbbrevs> aaList = result.getAcronymsAbbrevs();
						if (aaList.size() > 0) {
						  System.out.println("Acronyms and Abbreviations:");
						  for (AcronymsAbbrevs e: aaList) {
						    System.out.println("Acronym: " + e.getAcronym());
						    System.out.println("Expansion: " + e.getExpansion());
						    System.out.println("Count list: " + e.getCountList());
						    System.out.println("CUI list: " + e.getCUIList());
						  }
						} else {
						  System.out.println(" None.");
						}*/
						
						for (Utterance utterance: result.getUtteranceList()) {
							/*System.out.println("Utterance:");
							System.out.println(" Id: " + utterance.getId());
							System.out.println(" Utterance text: " + utterance.getString());
							System.out.println(" Position: " + utterance.getPosition());*/

							for (PCM pcm: utterance.getPCMList()) {
								//System.out.println("Phrase:");
								//System.out.println(" text: " + pcm.getPhrase().getPhraseText());
								
								//System.out.println("Candidates:");
								//writer.println("Candidates:");
						        for (Ev ev: pcm.getCandidateList()) {
						            /*System.out.println(" Candidate:");
						            System.out.println("  Score: " + ev.getScore());
						            System.out.println("  Concept Id: " + ev.getConceptId());
						            System.out.println("  Concept Name: " + ev.getConceptName());
						            System.out.println("  Preferred Name: " + ev.getPreferredName());
						            //System.out.println("  Matched Words: " + ev.getMatchedWords());
						            System.out.println("  Semantic Types: " + ev.getSemanticTypes());*/
						            /*System.out.println("  MatchMap: " + ev.getMatchMap());
						            System.out.println("  MatchMap alt. repr.: " + ev.getMatchMapList());
						            System.out.println("  is Head?: " + ev.isHead());
						            System.out.println("  is Overmatch?: " + ev.isOvermatch());
						            System.out.println("  Sources: " + ev.getSources());
						            System.out.println("  Positional Info: " + ev.getPositionalInfo());*/
						            //writer.println(ev.getConceptName() + "..OR.." + ev.getPreferredName() + "--- " + ev.getSemanticTypes());
						            //if(!conceptid.contains(mapEv.getConceptId()) && !mapEv.getSemanticTypes().contains("ftcn") && !mapEv.getSemanticTypes().contains("qlco") && !mapEv.getSemanticTypes().contains("qnco") && !mapEv.getSemanticTypes().contains("fndg")){ //!= "[ftcn]" && mapEv.getSemanticTypes() != "[qlco]" && mapEv.getSemanticTypes() != "[qnco]" && mapEv.getSemanticTypes() != "[fndg]"){
						            	  //System.out.println(mapEv.getConceptId());
						            	  //System.out.println(conceptid);
						            	  //conceptid.add(mapEv.getConceptId());
						            //char s='*';
						        	/*if(!ev.getPreferredName().contains("*") && !ev.getPreferredName().contains("%")){
						        		qes = qes + " OR " + ev.getPreferredName().split("\\(|:|,")[0];
						        	}
						            qes = qes + " OR " + ev.getPreferredName();*/
						            
						            /*if(!ev.getConceptName().contains("*") && !ev.getConceptName().contains("%"))
							        cnewQuery = cnewQuery + " (" + ev.getConceptName() +" OR " + ev.getPreferredName().split("\\(|:")[0] +")";
						             }*/
						        }
						        
						        //System.out.println("Mappings:");
						        //writer.println("Mappings:");
						        HashSet  Econceptid = new HashSet();
						        for (Mapping map: pcm.getMappingList()) {
						        	int j=0;
						        	HashMap qelms = new HashMap<String, Float>();
						        	//String[] qelms = null;
						            //System.out.println(" Map Score: " + map.getScore());
						            for (Ev mapEv: map.getEvList()) {
						            	//qelms[j] =  mapEv.getPreferredName().split("\\(|:|,")[0];
						            	String qelm = mapEv.getPreferredName().split("\\(|:|,")[0];
						            	qelms.put(qelm, mapEv.getScore());
						            	j++;
						              /*System.out.println("   Score: " + mapEv.getScore());
						              System.out.println("   Concept Id: " + mapEv.getConceptId());
						              System.out.println("   Concept Name: " + mapEv.getConceptName());
						              System.out.println("   Preferred Name: " + mapEv.getPreferredName());
						              //System.out.println("   Matched Words: " + mapEv.getMatchedWords());
						              System.out.println("   Semantic Types: " + mapEv.getSemanticTypes());*/
						              //System.out.println("   MatchMap: " + mapEv.getMatchMap());
						              //System.out.println("   MatchMap alt. repr.: " + mapEv.getMatchMapList());
						              //System.out.println("   is Head?: " + mapEv.isHead());
						              //System.out.println("   is Overmatch?: " + mapEv.isOvermatch());
						              //System.out.println("   Sources: " + mapEv.getSources());
						              //System.out.println("   Positional Info: " + mapEv.getPositionalInfo());
						              
						              //System.out.println("////////////////");
						              //writer.println(mapEv.getConceptName() + "..OR.." + mapEv.getPreferredName() + "--- " + mapEv.getSemanticTypes());
						            	//if(!mapEv.getSemanticTypes().contains("ftcn") && !mapEv.getSemanticTypes().contains("qlco") && !mapEv.getSemanticTypes().contains("qnco") && !mapEv.getSemanticTypes().contains("fndg")){ //!= "[ftcn]" && mapEv.getSemanticTypes() != "[qlco]" && mapEv.getSemanticTypes() != "[qnco]" && mapEv.getSemanticTypes() != "[fndg]"){
							            	 qes = qes +  mapEv.getPreferredName().split("\\(|:|,")[0] +" ";
						            	//}
						              //System.out.println(conceptid);
						              /*if(!Econceptid.contains(mapEv.getConceptId()) && !mapEv.getSemanticTypes().contains("ftcn") && !mapEv.getSemanticTypes().contains("qlco") && !mapEv.getSemanticTypes().contains("qnco") && !mapEv.getSemanticTypes().contains("fndg")){ //!= "[ftcn]" && mapEv.getSemanticTypes() != "[qlco]" && mapEv.getSemanticTypes() != "[qnco]" && mapEv.getSemanticTypes() != "[fndg]"){
						            	  //System.out.println(mapEv.getConceptId());
						            	  //System.out.println(conceptid);
						            	  Econceptid.add(mapEv.getConceptId());
							              qes = qes + " (" + mapEv.getConceptName() +" OR " + mapEv.getPreferredName().split("\\(|:|,")[0] +")";
						              }*/
						              //conceptid.add(mapEv.getConceptId());
						            }
						          }
						    }
						}
						//qes = qes + ") ";
				    	//-----------End of Element -------------
						
						//--------For Topic-------------------
						String qt = sb.toString();
						
				    	List<Result> resultList2 = api.processCitationsFromString(qt);
						
						Result result2 = resultList2.get(0);
						/*List<AcronymsAbbrevs> aaList = result.getAcronymsAbbrevs();
						if (aaList.size() > 0) {
						  System.out.println("Acronyms and Abbreviations:");
						  for (AcronymsAbbrevs e: aaList) {
						    System.out.println("Acronym: " + e.getAcronym());
						    System.out.println("Expansion: " + e.getExpansion());
						    System.out.println("Count list: " + e.getCountList());
						    System.out.println("CUI list: " + e.getCUIList());
						  }
						} else {
						  System.out.println(" None.");
						}*/
						
						for (Utterance utterance: result2.getUtteranceList()) {
							/*System.out.println("Utterance:");
							System.out.println(" Id: " + utterance.getId());
							System.out.println(" Utterance text: " + utterance.getString());
							System.out.println(" Position: " + utterance.getPosition());*/

							for (PCM pcm: utterance.getPCMList()) {
								//System.out.println("Phrase:");
								//System.out.println(" text: " + pcm.getPhrase().getPhraseText());
								
								//System.out.println("Candidates:");
								//writer.println("Candidates:");
						        for (Ev ev: pcm.getCandidateList()) {
						            /*System.out.println(" Candidate:");
						            System.out.println("  Score: " + ev.getScore());
						            System.out.println("  Concept Id: " + ev.getConceptId());
						            System.out.println("  Concept Name: " + ev.getConceptName());
						            System.out.println("  Preferred Name: " + ev.getPreferredName());
						            //System.out.println("  Matched Words: " + ev.getMatchedWords());
						            System.out.println("  Semantic Types: " + ev.getSemanticTypes());*/
						            /*System.out.println("  MatchMap: " + ev.getMatchMap());
						            System.out.println("  MatchMap alt. repr.: " + ev.getMatchMapList());
						            System.out.println("  is Head?: " + ev.isHead());
						            System.out.println("  is Overmatch?: " + ev.isOvermatch());
						            System.out.println("  Sources: " + ev.getSources());
						            System.out.println("  Positional Info: " + ev.getPositionalInfo());*/
						            //writer.println(ev.getConceptName() + "..OR.." + ev.getPreferredName() + "--- " + ev.getSemanticTypes());
						            //if(!conceptid.contains(mapEv.getConceptId()) && !mapEv.getSemanticTypes().contains("ftcn") && !mapEv.getSemanticTypes().contains("qlco") && !mapEv.getSemanticTypes().contains("qnco") && !mapEv.getSemanticTypes().contains("fndg")){ //!= "[ftcn]" && mapEv.getSemanticTypes() != "[qlco]" && mapEv.getSemanticTypes() != "[qnco]" && mapEv.getSemanticTypes() != "[fndg]"){
						            	  //System.out.println(mapEv.getConceptId());
						            	  //System.out.println(conceptid);
						            	  //conceptid.add(mapEv.getConceptId());
						            //char s='*';
						            
						            /*if(!ev.getConceptName().contains("*") && !ev.getConceptName().contains("%"))
							        cnewQuery = cnewQuery + " (" + ev.getConceptName() +" OR " + ev.getPreferredName().split("\\(|:")[0] +")";
						             }*/
						        }
						        
						        //System.out.println("Mappings:");
						        //writer.println("Mappings:");
						        for (Mapping map: pcm.getMappingList()) {
						            //System.out.println(" Map Score: " + map.getScore());
						            for (Ev mapEv: map.getEvList()) {
						              /*System.out.println("   Score: " + mapEv.getScore());
						              System.out.println("   Concept Id: " + mapEv.getConceptId());
						              System.out.println("   Concept Name: " + mapEv.getConceptName());
						              System.out.println("   Preferred Name: " + mapEv.getPreferredName());
						              //System.out.println("   Matched Words: " + mapEv.getMatchedWords());
						              System.out.println("   Semantic Types: " + mapEv.getSemanticTypes());*/
						              //System.out.println("   MatchMap: " + mapEv.getMatchMap());
						              //System.out.println("   MatchMap alt. repr.: " + mapEv.getMatchMapList());
						              //System.out.println("   is Head?: " + mapEv.isHead());
						              //System.out.println("   is Overmatch?: " + mapEv.isOvermatch());
						              //System.out.println("   Sources: " + mapEv.getSources());
						              //System.out.println("   Positional Info: " + mapEv.getPositionalInfo());
						              
						              //System.out.println("////////////////");
						              //writer.println(mapEv.getConceptName() + "..OR.." + mapEv.getPreferredName() + "--- " + mapEv.getSemanticTypes());
						              
						              //System.out.println(conceptid);
						              if(!conceptid.contains(mapEv.getConceptId()) && !mapEv.getSemanticTypes().contains("ftcn") && !mapEv.getSemanticTypes().contains("qlco") && !mapEv.getSemanticTypes().contains("qnco") && !mapEv.getSemanticTypes().contains("fndg")){ //!= "[ftcn]" && mapEv.getSemanticTypes() != "[qlco]" && mapEv.getSemanticTypes() != "[qnco]" && mapEv.getSemanticTypes() != "[fndg]"){
						            	  //System.out.println(mapEv.getConceptId());
						            	  //System.out.println(conceptid);
						            	  conceptid.add(mapEv.getConceptId());
							              newQuery = newQuery +  mapEv.getPreferredName().split("\\(|:|,")[0] +" ";
						              }
						              //conceptid.add(mapEv.getConceptId());
						            }
						          }
						    }
						}
				    	//-----------End of Element -------------
					   
					int l = newQuery.length();
			    	//newQuery =  newQuery.substring(0, l-4); 
			    	//writer.println(qid+" " + sb.toString() + newQuery.toLowerCase()+ cnewQuery.toLowerCase());
					//writer.println(qid+" (" + sb.toString()+")^2 OR ("+ qes.toLowerCase() + newQuery.toLowerCase() +")");// + newQuery.toLowerCase());
					writer.println(qid+" (" + sb.toString()+")^2 ("+ qes.toLowerCase() +")");// + newQuery.toLowerCase());
			    	    
			    	
			    	//System.out.println(qid+newQuery+" " + sb.toString());
			    	//System.out.println(qid +" "+ sb.toString());//+newQuery.toLowerCase()+ cnewQuery.toLowerCase());
					//System.out.println(qid+" (" + sb.toString()+")^2 OR ("+ qes.toLowerCase() + newQuery.toLowerCase() +")");
					System.out.println(qid+" (" + sb.toString()+")^2 ("+ qes.toLowerCase() +")");

		    	}
		    	
		    	
		       
		    }
		}
		writer.close();
	}
	
}
