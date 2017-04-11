package lucene;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

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
		
		
		
		String file = "E:/Nirali/Nirali/2007/2007topics (copy).txt";
		PrintWriter writer = new PrintWriter("E:/Nirali/Nirali/2007/topicsCandidate.txt", "UTF-8");
		PrintWriter writer2 = new PrintWriter("E:/Nirali/Nirali/2007/topicsElements.txt", "UTF-8");
		
		String qe = "";
		HashSet<String>  qeSet = new HashSet<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file ))) {
		    String line;
		    String qid="200";
		    
		    
		    
		    while ((line = br.readLine()) != null) {
		    	String newQuery = "";

		    	HashSet  conceptid = new HashSet();
				//HashSet<String> = null;
		   
		    	String[] topic = line.split(">");
		    	if(topic.length == 2){
		    		qid = topic[0].substring(1);		    		
				    String qtopic = topic[1];
				    String[] qElement = StringUtils.substringsBetween(qtopic, "[", "]");
				    qtopic = qtopic.replace("[", "");
				    qtopic = qtopic.replace("]", "");
				    
				    writer.println(qid + ":" + qElement[0]);
				    writer2.println(qid + ":" + qElement[0]);
				    
				    qeSet.add(qElement[0]);
				    //qe = qe + "and " + qElement[0];
				    
			    	System.out.println(qid +"........ " + qElement[0]+"........ " + qtopic);			    

		    	}
		    	
		    	//int l = newQuery.length();
		    	 //newQuery =  newQuery.substring(0, l-4); 
		    	 //writer.println(qid+newQuery);
		    	    //writer.println("The second line");	    	    
		    	
		    	//System.out.println(newQuery);
		       
		    }
		    for(String s : qeSet)
		    	qe = qe + " and " + s;
		}
		List<Result> resultList = api.processCitationsFromString(qe);
		writer.println(qeSet);
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
				System.out.println("Phrase:");
				writer.println();
				writer.println("\n\n//////-----" + pcm.getPhrase().getPhraseText());
				System.out.println(" text: " + pcm.getPhrase().getPhraseText());
				
				System.out.println("Candidates:");
				writer.println("Candidates:");
		        for (Ev ev: pcm.getCandidateList()) {
		            System.out.println(" Candidate:");
		            System.out.println("  Score: " + ev.getScore());
		            System.out.println("  Concept Id: " + ev.getConceptId());
		            System.out.println("  Concept Name: " + ev.getConceptName());
		            System.out.println("  Preferred Name: " + ev.getPreferredName());
		            //System.out.println("  Matched Words: " + ev.getMatchedWords());
		            System.out.println("  Semantic Types: " + ev.getSemanticTypes());
		            /*System.out.println("  MatchMap: " + ev.getMatchMap());
		            System.out.println("  MatchMap alt. repr.: " + ev.getMatchMapList());
		            System.out.println("  is Head?: " + ev.isHead());
		            System.out.println("  is Overmatch?: " + ev.isOvermatch());
		            System.out.println("  Sources: " + ev.getSources());
		            System.out.println("  Positional Info: " + ev.getPositionalInfo());*/
		            writer.println(ev.getConceptName() + "..OR.." + ev.getPreferredName() + "--- " + ev.getSemanticTypes());
		        }
		        
		        System.out.println("Mappings:");
		        writer.println("Mappings:");
		        for (Mapping map: pcm.getMappingList()) {
		            System.out.println(" Map Score: " + map.getScore());
		            for (Ev mapEv: map.getEvList()) {
		              System.out.println("   Score: " + mapEv.getScore());
		              System.out.println("   Concept Id: " + mapEv.getConceptId());
		              System.out.println("   Concept Name: " + mapEv.getConceptName());
		              System.out.println("   Preferred Name: " + mapEv.getPreferredName());
		              //System.out.println("   Matched Words: " + mapEv.getMatchedWords());
		              System.out.println("   Semantic Types: " + mapEv.getSemanticTypes());
		              //System.out.println("   MatchMap: " + mapEv.getMatchMap());
		              //System.out.println("   MatchMap alt. repr.: " + mapEv.getMatchMapList());
		              //System.out.println("   is Head?: " + mapEv.isHead());
		              //System.out.println("   is Overmatch?: " + mapEv.isOvermatch());
		              //System.out.println("   Sources: " + mapEv.getSources());
		              //System.out.println("   Positional Info: " + mapEv.getPositionalInfo());
		              
		              //System.out.println("////////////////");
		              writer.println(mapEv.getConceptName() + "..OR.." + mapEv.getPreferredName() + "--- " + mapEv.getSemanticTypes());
		              
		              //System.out.println(conceptid);
		              /*if(!conceptid.contains(mapEv.getConceptId()) && !mapEv.getSemanticTypes().contains("ftcn") && !mapEv.getSemanticTypes().contains("qlco") && !mapEv.getSemanticTypes().contains("qnco") && !mapEv.getSemanticTypes().contains("fndg")){ //!= "[ftcn]" && mapEv.getSemanticTypes() != "[qlco]" && mapEv.getSemanticTypes() != "[qnco]" && mapEv.getSemanticTypes() != "[fndg]"){
		            	  //System.out.println(mapEv.getConceptId());
		            	  //System.out.println(conceptid);
		            	  conceptid.add(mapEv.getConceptId());
			              newQuery = newQuery + " (" + mapEv.getConceptName() +" OR " + mapEv.getPreferredName() +")";
		              }*/
		              //conceptid.add(mapEv.getConceptId());
		            }
		          }
		    }
		}
    	
		writer.close();
		writer2.close();
	}
	
}
