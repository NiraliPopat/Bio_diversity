package lucene;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

/** Simple command-line based search demo. */
public class SearchFiles2 {

  private SearchFiles2() {}
  
  

  /** Simple command-line based search demo. */
  public static void main(String[] args) throws Exception {
    String usage =
      "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
    if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
      System.out.println(usage);
      System.exit(0);
    }
    	
    String[] nq = null;
    String index = "E:/index2";
    String field = "contents";
    //String queries = "health" + "food";
    String queries = "E:/Nirali/Nirali/2007/2007topics (another copy).txt";
    int repeat = 0;
    boolean raw = false;
    String queryString ="(Serum Proteins OR Serum Proteins) (Change OR Changing) (Expression OR Expression procedure) (Association OR Chemical Association) (With OR In addition to) (High OR Abnormally high) (disease activity OR Condition activity) (Lupus OR Lupus Erythematosus)"; //"(Serum Proteins OR Serum Proteins) (Expression OR Expression procedure) (Association OR Chemical Association) (Lupus OR Lupus Erythematosus)";//"serum PROTEINS lupus";
    int hitsPerPage = 1000;
    
    for(int i = 0;i < args.length;i++) {
      if ("-index".equals(args[i])) {
        index = args[i+1];
        i++;
      } else if ("-field".equals(args[i])) {
        field = args[i+1];
        i++;
      } else if ("-queries".equals(args[i])) {
        queries = args[i+1];
        i++;
      } else if ("-query".equals(args[i])) {
        queryString = args[i+1];
        i++;
      } else if ("-repeat".equals(args[i])) {
        repeat = Integer.parseInt(args[i+1]);
        i++;
      } else if ("-raw".equals(args[i])) {
        raw = true;
      } else if ("-paging".equals(args[i])) {
        hitsPerPage = Integer.parseInt(args[i+1]);
        if (hitsPerPage <= 0) {
          System.err.println("There must be at least 1 hit per page.");
          System.exit(1);
        }
        i++;
      }
    }
    
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
    IndexSearcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer();

    BufferedReader in = null;
    if (queries != null) {
      in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
    } else {
      in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    }
    QueryParser parser = new QueryParser(field, analyzer);
    while (true) {
      if (queries == null && queryString == null) {                        // prompt the user
        System.out.println("Enter query: ");
      }

      String line = queryString != null ? queryString : in.readLine();

      if (line == null || line.length() == -1) {
        break;
      }

      line = line.trim();
      if (line.length() == 0) {
        break;
      }
      
      Query query = parser.parse(line);
      System.out.println("Searching for: " + query.toString(field));
            
      if (repeat > 0) {                           // repeat & time as benchmark
        Date start = new Date();
        for (int i = 0; i < repeat; i++) {
          searcher.search(query, 100);
        }
        Date end = new Date();
        System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
      }

      /*int qid=200;
      doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null, qid);
      query = parser.parse("SLE");
      //qid++;
      doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null, qid );
      query = parser.parse("health");
      //qid++;
      doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null, qid);*/

      //String file = "E:/Nirali/Nirali/2007/topics.txt";
      String file = "E:/Nirali/Nirali/2007/2007topics (copy).txt";
      try (BufferedReader br = new BufferedReader(new FileReader(file ))) {
		    
    	  //String l;
    	  String topic;
		  int qid = 200;//cntr=0;
		  while ((topic = br.readLine()) != null) {
		    	
			  	//topic = topic.substring(4);
			  	topic = topic.substring(5);
		    	System.out.println("Topic------"+qid+"...." + topic);
		    	topic = topic.replace("[", "");
			    topic = topic.replace("]", "");
		    	
		    	query = parser.parse(topic);
		    	System.out.println("Q: "+query);
			    doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null, qid );
			    qid++;

		  }
      }catch (Exception e) {
		// TODO: handle exception
	}
	 
      if (queryString != null) {
        break;
      }
    }
      
    reader.close();
  }

  /**
139   * This demonstrates a typical paging search scenario, where the search engine presents 
140   * pages of size n to the user. The user can then go to the next page if interested in
141   * the next hits.
142   * 
143   * When the query is executed for the first time, then only enough results are collected
144   * to fill 5 result pages. If the user wants to page beyond this limit, then the query
145   * is executed another time and all hits are collected.
146   * 
147   */
  //static PrintWriter writer = new PrintWriter("E:/Nirali/Nirali/2007/Lucene1.txt", "UTF-8");
  public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, 
                                     int hitsPerPage, boolean raw, boolean interactive, int qid) throws IOException {
 
	
    // Collect enough docs to show 5 pages
	  
	//FileWriter fw = new FileWriter("E:/Nirali/Nirali/2007/Lucene1.txt", true);
	//BufferedWriter bw = new BufferedWriter(fw);
	//PrintWriter out = new PrintWriter(bw);
	
	//PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("E:/Nirali/Nirali/2007/Lucene1.txt", true)));
	//FileWriter fw = new FileWriter("E:/Nirali/Nirali/2007/Lucene1.txt",true); //the true will append the new data
	BufferedWriter bw = null;

    TopDocs results = searcher.search(query, 5 * hitsPerPage);
    ScoreDoc[] hits = results.scoreDocs;
    int counter;
    int numTotalHits = results.totalHits;
    //System.out.println(numTotalHits + " total matching documents");

    int start = 0;
    int end = Math.min(numTotalHits, hitsPerPage);
        
    while (true) {
      if (end > hits.length) {
        System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
        System.out.println("Collect more (y/n) ?");
        String line = in.readLine();
        if (line.length() == 0 || line.charAt(0) == 'n') {
          break;
        }

        hits = searcher.search(query, numTotalHits).scoreDocs;
      }
      
      end = Math.min(hits.length, start + hitsPerPage);
      
      for (int i = start; i < end; i++) {
        if (raw) {                              // output raw format
          System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
          continue;
        }
        
        org.apache.lucene.document.Document doc = searcher.doc(hits[i].doc);
        String path = doc.get("path");
        if (path != null) {
          String nm = doc.get("name").substring(0, doc.get("name").length() - 5);
          System.out.println(qid + " " + nm + " " + (i+1) + " " + hits[i].score + " " + doc.get("offset") + " " + doc.get("length") + " Lucene" );
          //fw.write("\n" + qid + " " + nm + " " + (i+1) + " " + hits[i].score + " " + doc.get("offset") + " " + doc.get("length") + " Lucene1");
          try {
              // APPEND MODE SET HERE
              bw = new BufferedWriter(new FileWriter("E:/Nirali/Nirali/2007/Lucene_withoutQE.txt", true));
              bw.write(qid + " " + nm + " " + (i+1) + " " + hits[i].score + " " + doc.get("offset") + " " + doc.get("length") + " Lucene" );
              bw.newLine();
              bw.flush();
           } catch (IOException ioe) {
        	   ioe.printStackTrace();
           } finally {                       // always close the file
     	 if (bw != null) try {
     	    bw.close();
     	 } catch (IOException ioe2) {
     	    // just ignore it
     	 }
           } // end try/
          
          //("add a line\n");//appends the string to the file
          //fw.close();
          //System.out.println(doc.get("title"));
          //System.out.println(doc.get("contents"));
          //String title = doc.get("name");
          //if (title != null) {
            //System.out.println("   Title: " + doc.get("name"));
          //}
          //System.out.println("   Score: " + hits[i].score);
        } else {
          System.out.println((i+1) + ". " + "No path for this document");
        }
                  
      }

      if (!interactive || end == 0) {
        break;
      }

      if (numTotalHits >= end) {
        boolean quit = false;
        while (true) {
          System.out.print("Press ");
          if (start - hitsPerPage >= 0) {
            System.out.print("(p)revious page, ");  
          }
          if (start + hitsPerPage < numTotalHits) {
            System.out.print("(n)ext page, ");
          }
          System.out.println("(q)uit or enter number to jump to a page.");
          
          String line = in.readLine();
          if (line.length() == 0 || line.charAt(0)=='q') {
            quit = true;
            break;
          }
          if (line.charAt(0) == 'p') {
            start = Math.max(0, start - hitsPerPage);
            break;
          } else if (line.charAt(0) == 'n') {
            if (start + hitsPerPage < numTotalHits) {
              start+=hitsPerPage;
            }
            break;
          } else {
            int page = Integer.parseInt(line);
            if ((page - 1) * hitsPerPage < numTotalHits) {
              start = (page - 1) * hitsPerPage;
              break;
            } else {
              System.out.println("No such page");
            }
          }
        }
        if (quit) break;
        end = Math.min(numTotalHits, start + hitsPerPage);
      }
    }
    
  }
  //writer.;
}