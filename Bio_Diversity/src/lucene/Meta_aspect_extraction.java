package lucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.bind.ParseConversionEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

public class Meta_aspect_extraction {
	public static void main(String[] args) throws Exception{
		
		File fout = new File("E:/python/NMetamap_new_3_9.txt"); //File to store aspects
		
		FileOutputStream fos = new FileOutputStream(fout);
	 
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
		//writer.print("abc");
		HashMap<String, String> topEl = new HashMap<String, String>();
		HashMap<Integer, Set> cstmap = new HashMap<Integer, Set>();
		HashMap<Integer, Set> mstmap = new HashMap<Integer, Set>();
		
		MetaMapApi tapi = new MetaMapApiImpl();//"C:/Users/NiraliPopat/Downloads/public_mm_win32_main_2014/public_mm/bin/metamap14");
		MetaMapApi atapi = new MetaMapApiImpl();
		MetaMapApi api = new MetaMapApiImpl();
		
		//-----Stopwords setup---
		Tokenizer stdToken = new WhitespaceTokenizer();
		final List<String> stopWords = new ArrayList<>(); 
		String f2 = "E:/stopwords_en.txt";
		      
		try (BufferedReader br = new BufferedReader(new FileReader(f2))) {
				    
	    	  String topic;
			  //int qid = 200;//cntr=0;
			  while ((topic = br.readLine()) != null) {
				  stopWords.add(topic.trim());
			  }
	      }
		final CharArraySet stopSet = new CharArraySet(stopWords, false);
		//-----Stopwords setup over ---
		
		//Set<String> aset = new HashSet<>();
		
		
		tapi.setOptions("-yck topp,qlco,qnco,medd,gora");//acty,aggp,bhvr,clas,dora,edac,eehu,evnt,famg,geoa,gora,grpa,grup,hcpp,hcro,humn,orgt,podg,popg,prog,pros,qlco,qnco,shro,tmco");
		
		atapi.setOptions("-yck topp,qlco,qnco,medd,gora,ftcn");
		
		try (BufferedReader br = new BufferedReader(new FileReader("E:/Nirali/Nirali/2007/topicsElements.txt"))) {
		    String line;
		    //String qid="200";
		    while ((line = br.readLine()) != null) {
		    	String[] tEs = line.split(":");
		    	topEl.put(tEs[0], tEs[1]);
		    	int id = Integer.parseInt(tEs[0]);
		    	System.out.println("Key:" + tEs[0]+ " Value:" + topEl.get(tEs[0]));
		    	String tep = tEs[1].toLowerCase();
		    	//-----------Topic semtypes--------
		    	
		    	HashSet<String> matst = new HashSet<String>();
		    	
		    	if(tep.equalsIgnoreCase("proteins")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		//matst.add("aapp");
		    	}
		    	if(tep.equalsIgnoreCase("mutations")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		matst.add("aapp");
		    		matst.add("comd");
		    		matst.add("gngm");
		    	}
		    	if(tep.equalsIgnoreCase("drugs")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		matst.add("opco");
		    		matst.add("orch");
		    	}
		    	if(tep.equalsIgnoreCase("cell or tissue types")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		matst.add("emst");
		    		matst.add("enzy");
		    		matst.add("bodm");
		    		matst.add("bpoc");
		    		matst.add("bdsu");
		    		
		    	}
		    	if(tep.equalsIgnoreCase("signs or symptoms")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		matst.add("sosy");
		    		matst.add("mobd");
		    		matst.add("clna");
		    		matst.add("fndg");
		    		matst.add("clna");
		    		
		    	}
		    	if(tep.equalsIgnoreCase("toxicities")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		matst.add("dsyn");
		    		matst.add("sosy");
		    		matst.add("patf");
		    		matst.add("fndg");
		    		matst.add("comd");
		    		
		    	}
		    	if(tep.equalsIgnoreCase("biological substances")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		matst.add("bpoc");
		    		matst.add("tisu");
		    		matst.add("ortf");
		    		matst.add("enzy");
		    		matst.add("horm");
		    		matst.add("ortf");
		    		matst.add("ortf");
		    		matst.add("ortf");
		    		matst.add("ortf");
		    		matst.add("ortf");
		    		
		     	 }
		    	if(tep.equalsIgnoreCase("molecular functions")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		matst.add("celf");
		    		matst.add("orgf");
		    		matst.add("phsf");
		    		matst.add("biof");
		    		matst.add("genf");
		    		matst.add("moft");
		    		matst.add("ortf");
		    		matst.add("patf");
		    		matst.add("phsf");
		    		
		    	}
		    	if(tep.equalsIgnoreCase("antibodies")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		matst.add("imft");
		    	}
		    	if(tep.equalsIgnoreCase("genes")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		
		    	}
		    	if(tep.equalsIgnoreCase("diseases")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		matst.add("neop");
		    		
		    	}
		    	if(tep.equalsIgnoreCase("pathways")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		
		    	}
		    	if(tep.equalsIgnoreCase("strains")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		matst.add("bact");
		    		matst.add("gngm");
		    		matst.add("virs");
		    		
		    	}
		    	if(tep.equalsIgnoreCase("tumor types")){
		    		System.out.println("Value:" + topEl.get(tEs[0]));
		    		matst.add("aapp");
		    		matst.add("moft");
		    		matst.add("neop");
		    		
		    	}
		    	
				
				
				List<Result> resultList = tapi.processCitationsFromString(tEs[1]);
				
				Result result = resultList.get(0);
		    	
				
				for (Utterance utterance: result.getUtteranceList()) {
					
					for (PCM pcm: utterance.getPCMList()) {
						
				        //for (Ev ev: pcm.getCandidateList()) {
				        	//candst.addAll(ev.getSemanticTypes());
				        //}
				        for (Mapping map: pcm.getMappingList()) {
				            //System.out.println(" Map Score: " + map.getScore());
				            for (Ev mapEv: map.getEvList()) {
				           
				            	matst.addAll(mapEv.getSemanticTypes());
				            }
				          }
					}
				}
				//cstmap.put(id, candst);
				mstmap.put(id, matst);
				//System.out.println("CST---" + candst);*/
		    	System.out.println("MST---" + matst);
				//-------- Topic semtype end----------
		    }
		}
		
		
		///--------Whole topic Metamap start------------------
		try (BufferedReader br = new BufferedReader(new FileReader("E:/Nirali/Nirali/2007/topicsAll.txt"))) {
		    String line;
		    //String qid="200";
		    while ((line = br.readLine()) != null) {
		    	String[] tEs = line.split(":");
		    	topEl.put(tEs[0], tEs[1]);
		    	int id = Integer.parseInt(tEs[0]);
		    	System.out.println("Key:" + tEs[0]+ " Value:" + topEl.get(tEs[0]));
		    	
		    	//-----------Topic semtypes--------
		    	HashSet<String> candst = new HashSet<String>();
		    	//HashSet<String> matst = new HashSet<String>();
				
				
				
				List<Result> resultList = atapi.processCitationsFromString(tEs[1]);
				
				Result result = resultList.get(0);
		    	
				
				for (Utterance utterance: result.getUtteranceList()) {
					
					for (PCM pcm: utterance.getPCMList()) {
						
				        //for (Ev ev: pcm.getCandidateList()) {
				        	//candst.addAll(ev.getSemanticTypes());
				        //}
				        for (Mapping map: pcm.getMappingList()) {
				            //System.out.println(" Map Score: " + map.getScore());
				            for (Ev mapEv: map.getEvList()) {
				            	candst.addAll(mapEv.getSemanticTypes());
				            	//matst.addAll(mapEv.getSemanticTypes());
				            }
				          }
					}
				}
				cstmap.put(id, candst);
				//mstmap.put(id, matst);
				System.out.println("CST---" + candst);
		    	//System.out.println("MST---" + matst);
				//-------- Topic semtype end----------
		    }
		}
	
		///--------Whole topic Metamap over------------------
		
			
		
		//Map<String, Integer> asps = new HashMap<String, Integer>();
		List<String> a = new ArrayList<String>();
		
		//String file = "E:/Nirali/Nirali/2007/LMD_17_t9_2_2.txt";
		//String file = "E:/Nirali/Nirali/2007/submissions/UniNE1.txt";
		String file = "E:/Nirali/Nirali/2007/submissions/NLM3.txt";
		//PrintWriter writer = new PrintWriter("E:/Nirali/Nirali/2007/topics2.txt", "UTF-8");
		
		
		
		try (BufferedReader br = new BufferedReader(new FileReader(file ))) {
			HashSet<String> vst = new HashSet<String>();
			HashSet<String> cst = new HashSet<String>();
			HashSet<String> mst = new HashSet<String>();
			HashSet<String> est = new HashSet<String>();
			vst.add("aapp");
			vst.add("genf");
			vst.add("gngm");
			vst.add("emst");
			vst.add("comd");
			vst.add("bpoc");
			vst.add("bdsy");
			vst.add("bacs");
			vst.add("bdsu");
			vst.add("orch");
			vst.add("dsyn");
			vst.add("patf");
			vst.add("cgab");
			vst.add("mobd");
			vst.add("neop");
			
			
			
			System.out.println("VST---" + vst);
			int i=0;
			String line;
			String field;
			
			String ops="";
			
			if(i%1000==0){
				est = vst;
				int id = 200 + (i/1000);
				cst = (HashSet<String>) cstmap.get(id);
				mst = (HashSet<String>) mstmap.get(id);
				System.out.println("CST---" + cst);
		    	System.out.println("MST---" + mst);
				
				est.addAll(mst);
				est.addAll(cst);
				
				for(String s: est){
					ops = ops + s +",";
				}
				ops = ops.substring(0, ops.length()-1);
				
				api.setOptions("-ycJ "+ ops +" --prune 15");
			}
		    while ((line = br.readLine()) != null) {
		    	Map<String, Integer> asps = new HashMap<String, Integer>();
		    	i++;
		    	//System.out.println(line);
		    	String[] fields = line.split(" ");
		    	if(fields.length>2){
			    	String qid = fields[0];
			    	String docid = fields[1];
			    	String rank = fields[2];
			    	String score = fields[3];
			    	int offset = Integer.parseInt(fields[4]);
			    	int length = Integer.parseInt(fields[5]);
			    	
			    	System.out.println("\nQid:" + qid + " Docid:" + docid + " rank:" + rank + " score:" + score + " length:" + length + " offset:" + offset);
			    	
			    	
			    	
			    	RandomAccessFile f = new RandomAccessFile("E:/documents/text/"+docid+".html", "rw");
			    	
			    	f.seek(offset);
			    	String l = f.readLine();
			    	while ( l.length() < length ) {
			    	  // Process line
			    		//System.out.println(l);
			    		l = l + f.readLine();
			    	}
			    	l = l.substring(0, length);
			    	
			    	Document dochtml = Jsoup.parse(l);
			  	  	String pl = dochtml.body().text();
			  	  	pl.replaceAll("\\<.*?>", " ");//replaceAll("\\<[^>]*>","")
			  	  	//pl.replaceAll("null", "");//replaceAll("\\<[^>]*>","")
			  	  	//l = 
			  	  	//pl = pl.substring(0, pl.indexOf("null"));
			  	  	pl=pl.trim();
			    	System.out.println("oo---"+pl+"...");
			    	
			    	f.close();
			    	
			    	//----------------METAMAP---------------------
			    	String[] t = pl.split(",");
					
					//HashSet  conceptid = new HashSet();
					for(String spl: t){
						if(spl=="")
							System.out.println("\n\nError:");
					
					List<Result> resultList = api.processCitationsFromString(spl.trim()+",");
					
					//if(resultList != null){
					Result result = resultList.get(0);
			    	//inputFile.read(null, offset,offset+length);
					
					String aspects = "";
					//System.out.println("Metamap:");
					
					for (Utterance utterance: result.getUtteranceList()) {
						///System.out.println("Uttr:");
						
						for (PCM pcm: utterance.getPCMList()) {
							//System.out.println("Phrase:");
							System.out.print(": " + pcm.getPhrase().getPhraseText());
							
							//System.out.println("Candidates:");
					        /*for (Ev ev: pcm.getCandidateList()) {
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
					            System.out.println("  Positional Info: " + ev.getPositionalInfo());
					        }*/
					        //String aspects = null;
					        //System.out.println("Mappings:");
							HashSet  conceptid = new HashSet();
					        for (Mapping map: pcm.getMappingList()) {
					            //System.out.println(" Map Score: " + map.getScore());
					            for (Ev mapEv: map.getEvList()) {
					             /* System.out.println("   Score: " + mapEv.getScore());
					              System.out.println("   Concept Id: " + mapEv.getConceptId());
					              System.out.println("   Concept Name: " + mapEv.getConceptName());*/
					              //System.out.println("   Preferred Name: " + mapEv.getPreferredName());
					              //System.out.println("   Matched Words: " + mapEv.getMatchedWords());
					              //System.out.println("   Semantic Types: " + mapEv.getSemanticTypes());
					              //System.out.println("   MatchMap: " + mapEv.getMatchMap());
					              //System.out.println("   MatchMap alt. repr.: " + mapEv.getMatchMapList());
					              //System.out.println("   is Head?: " + mapEv.isHead());
					              //System.out.println("   is Overmatch?: " + mapEv.isOvermatch());
					              //System.out.println("   Sources: " + mapEv.getSources());
					              //System.out.println("   Positional Info: " + mapEv.getPositionalInfo());*/
					              
					              //System.out.println("////////////////");
					              //String cname = mapEv.getConceptName().trim();
					              String[] cnames = mapEv.getPreferredName().split(",");
					              String cname =  cnames[0];
					              cnames = cname.split(":");
					              cname =  cnames[0];
					              cnames = cname.split("\\(");
					              cname =  cnames[0];
					              cname = cname.replaceAll("'", " ");
					             
								   
								  
								  if(!conceptid.contains(mapEv.getConceptId()) && !StringUtils.isNumeric(cname)){  
					            	  //System.out.println(mapEv.getConceptId());
					            	  //System.out.println(conceptid);
					            	  conceptid.add(mapEv.getConceptId());
					            	  //System.out.print(mapEv.getSemanticTypes());
					            	  if(!Collections.disjoint(mapEv.getSemanticTypes(), mst)){
					            		  cname = cname + ".." + 3 + ".." + mapEv.getSemanticTypes();
					            		  //System.out.println(cname);
					            		  
					            	  }
					            	  else if(!Collections.disjoint(mapEv.getSemanticTypes(), cst))
					            		  cname = cname + ".." + 2 + ".." + mapEv.getSemanticTypes();
					            	  else
					            		  cname = cname + ".." + 1 + ".." + mapEv.getSemanticTypes();
					            	  
					            	  cname = cname.toLowerCase();
					            	  a.add(cname);
					            	  //System.out.println(cname);
					            	  Integer count = asps.get(cname);
					          		  asps.put(cname, (count == null) ? 1 : count + 1);
						              aspects = aspects + " (" + mapEv.getPreferredName() +" -- " + mapEv.getSemanticTypes() +")";
					              }
					              //mapEv.
					              //conceptid.add(mapEv.getConceptId());
					            }
					          }
					        //a.addAll(aset);
					        
					    }
					}
					}
					//}
					writer.write("['" + fields[0] + "', '" + fields[1] + "', '" + fields[2] + "', '" + fields[3] + "', '" + fields[4] + "', '" + fields[5] + "', 'Metamap']");
					String s = "{";
					for (Map.Entry<String, Integer> entry : asps.entrySet()) {
						//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
						s= s + "'" + entry.getKey() + "': " + entry.getValue() + ", ";
					}
					//Map<String, Integer> treeMap = sortByValue(asps);;
					if(s.length()>2)
						s = s.substring(0, s.length()-2);
					s = s+ "}";
					writer.newLine();
					writer.write(s);
					writer.newLine();
					//printMap(asps);
					System.out.println("\n@@" + s);
		    	}
		    }
		}
		writer.close();
	}
	
	
}
