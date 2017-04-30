package lucene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

public class MetamapEx {
public static <E> void main(String[] args) throws Exception {
		
		MetaMapApi api = new MetaMapApiImpl();//"C:/Users/NiraliPopat/Downloads/public_mm_win32_main_2014/public_mm/bin/metamap14");
		
		//String terms= "GENE and PATHWAY ICAM-1|TNF-ALPHA|CD14|E-SELECTIN";
		
		api.setOptions("-yc");
		String t ="TESTIS TUMOR,	LEUKEMIA,	NERVE SHEATH TUMORS, ";//LUNG CANCER|HEAD AND NECK CANCER , STEM CELL FACTOR/KIT RECEPTOR PATHWAY, THE TNF-{ALPHA} SIGNALING PATHWAY";// "BRAF MUTATIONS|B-RAFV599E (ALSO NAMED AS BRAF T1796A MUTATION, LATER DESIGNATED  AS B-RAFV600E)";
		List<Result> resultList2 = api.processCitationsFromString(t);
		
		Result result2 = resultList2.get(0);
		
		
		for (Utterance utterance: result2.getUtteranceList()) {
			/*System.out.println("Utterance:");
			System.out.println(" Id: " + utterance.getId());
			System.out.println(" Utterance text: " + utterance.getString());
			System.out.println(" Position: " + utterance.getPosition());*/

			for (PCM pcm: utterance.getPCMList()) {
				System.out.println("Phrase:");
				System.out.println(" text: " + pcm.getPhrase().getPhraseText());
				
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
		        	}
		          }

			}
		}
}
		
}
