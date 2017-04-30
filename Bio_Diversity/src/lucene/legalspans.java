package lucene;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishMinimalStemmer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.AfterEffectB;
import org.apache.lucene.search.similarities.AfterEffectL;
import org.apache.lucene.search.similarities.BasicModelIn;
import org.apache.lucene.search.similarities.DFRSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.NormalizationH1;
import org.apache.lucene.search.similarities.NormalizationH2;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.tartarus.snowball.ext.PorterStemmer;

public class legalspans {
	static int j=0;
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String file = "F:/Pendrive/2006 data zip/2006_docs_zip/legalspans.txt";
		String usage = "java org.apache.lucene.demo.IndexFiles"
                + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
                + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                + "in INDEX_PATH that can be searched with SearchFiles";
		
		String indexPath = "E:/index30"; 
		
	      //String file = "E:/Nirali/Nirali/2007/2007topics (copy).txt";
		//String docsPath = "E:/documents/text";
	    boolean create = true;
	    for(int i=0;i<args.length;i++) {
	      if ("-index".equals(args[i])) {
	        indexPath = args[i+1];
	        i++;
	      } else if ("-docs".equals(args[i])) {
	        //docsPath = args[i+1];
	        i++;
	      } else if ("-update".equals(args[i])) {
	        create = false;
	      }
	    }

	    //if (docsPath == null) {
	     // System.err.println("Usage: " + usage);
	      //System.exit(1);
	    //}

	    /*final Path docDir = Paths.get(docsPath);
	    if (!Files.isReadable(docDir)) {
	      System.out.println("Document directory dhfndk '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
	      System.exit(1);
	    }*/
	    
		Date start = new Date();
	    try {
	      System.out.println("Indexing to directory '" + indexPath + "'...");

	      Directory dir = FSDirectory.open(Paths.get(indexPath));
	      //Analyzer analyzer = new StandardAnalyzer();
	      //IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
	      
	      StandardAnalyzer analyzer = new StandardAnalyzer();
	      //Directory dir = new RAMDirectory();
	      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
	      /*IBSimilarity similarity = new IBSimilarity(
	      		new DistributionLL(),//1 
	      		//new DistributionSPL(),//2
	      		new LambdaDF(),//1 
	      		//new LambdaTTF(), //2
	      		new NormalizationH2());*/
	      /*DFRSimilarity similarity = new DFRSimilarity(
	        new BasicModelIn(),
	        new AfterEffectB(),
	        new NormalizationH2());*/
	      LMDirichletSimilarity similarity = new LMDirichletSimilarity();
	      iwc.setSimilarity(similarity);
	      IndexWriter writer = new IndexWriter(dir, iwc);
	   

	      if (create) {
	        // Create a new index in the directory, removing any
	        // previously indexed documents:
	        iwc.setOpenMode(OpenMode.CREATE);
	      } else {
	        // Add new documents to an existing index:
	        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
	      }
	      
	    try (BufferedReader br = new BufferedReader(new FileReader(file ))) {
			    
		  //String l;
		  String topic2;
		  String pdoc = "";
		  int i = 0;//cntr=0;
		  RandomAccessFile f1 = new RandomAccessFile(file, "rw");
		  while((topic2 = br.readLine()) != null){
			 //topic2 = br.readLine();
			  //System.out.println(topic2);
			  i++;
			  String[] t = topic2.split("\t");
			  String docid = t[0];
			  int si = Integer.parseInt(t[1]);
			  int ei = Integer.parseInt(t[2]);
			  //System.out.println("Doc:" + docid + " si:" + si + " ei:" + ei);
			  //indexDoc(writer, docid, si, ei);
			  Path f2 = Paths.get("E:/documents/text/"+docid+".html");
			  
			  if (!docid.equals(pdoc)){
				  pdoc = docid;
				  //System.out.println(docid + " " + pdoc);
				  System.out.println("Doc:" + docid + " si:" + si + " ei:" + ei);
				  try{
					  f1.close();
					  f1 = new RandomAccessFile("E:/documents/text/"+docid+".html", "rw");
				  }
				  catch (Exception e) {
					    System.out.println("Opening file caught a " + e.getClass() +
					     "\n with message: " + e.getMessage());
				  }
			    
			  }    
			    //index file name
			      Field fileNameField = new StringField("name",
			    	      docid,
			    	      Field.Store.YES);
			      
			   // Add the path of the file as a field named "path".  Use a
			      // field that is indexed (i.e. searchable), but don't tokenize 
			      // the field into separate words and don't index term frequency
			      // or positional information:
			      Field pathField = new StringField("path", file.toString(), Field.Store.YES);
			
			      Document dochtml;// = Jsoup.parse(title);
				  
				  //RandomAccessFile f1 = new RandomAccessFile("E:/documents/text/"+docid+".html", "rw");
			    	
			    	f1.seek(si);
			    	String l = f1.readLine();
			    	int length = ei;
			    	while ( l.length() < length ) {
			    	  // Process line
			    		//System.out.println(l);
			    		l = l + f1.readLine();
			    	}
			    	l = l.substring(0, length);
				  
				  
				  ///////------FORMATING TEXT---------
				  StandardTokenizer stdToken = new StandardTokenizer();
				  //Tokenizer stdToken = new WhitespaceTokenizer();
				  //EnglishMinimalStemmer stemmer = new EnglishMinimalStemmer();
				  PorterStemmer stemmer = new PorterStemmer();
				     //stdToken.setReader(new StringReader("Some stuff that is in need of analysis. stuff patients PATIENT d > 0.5 Dnn>Bnn D.N.A diseases heart attacks at cl-fo"));
				   
				     

				     //You're code starts here
				     final List<String> stopWords = new ArrayList<>(); 
				     String f = "E:/stopwords_en.txt";
				      
				      try (BufferedReader br2 = new BufferedReader(new FileReader(f))) {
						    
				    	  String topic;
						  //int qid = 200;//cntr=0;
						  while ((topic = br2.readLine()) != null) {
							  stopWords.add(topic.trim());
						  }
				      }
				    final CharArraySet stopSet = new CharArraySet(stopWords, false);
				     
				        
				  
						  dochtml = Jsoup.parse(l);
						  String plainStr = dochtml.body().text();
						  //String[] validpas = plainStr.split(" ");
						  
						  
							  j++;
							  Field passageId = new StringField("id",
						    	      docid+"."+j,
						    	      Field.Store.YES);
							 
							  org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
							  doc.add(fileNameField);
							  doc.add(pathField);
							  doc.add(passageId);
							  //doc.add(titleField);
							  doc.add(new StringField("offset", si+"", Field.Store.YES));
							  doc.add(new StringField("length", ei+"", Field.Store.YES));
							  //doc.add(new LongPoint("modified", lastModified));
							  //((org.apache.lucene.document.Document) doc).add(new TextField("title", ptitle, Store.YES));
							  //System.out.println(passage_content);
							  //InputStream is = new ByteArrayInputStream(passage_content.getBytes());
							  
							  //String strippedText = passage_content.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
							  
							  //--------TEXT PROCESSING------------
							  TokenStream tokenStream;
							  //String nplainstr = plainStr.replaceAll("-", ".zz");
							  //stdToken.setReader(new StringReader(nplainstr));
							  stdToken.setReader(new StringReader(plainStr));
							  
							  tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(stdToken))), stopSet);
							  			    
						      //tokenStream = new PorterStemFilter(tokenStream);
						      tokenStream.reset();
						     //int l=0;
						     String term="";
						     StringBuilder sb = new StringBuilder();
						     //OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
						     CharTermAttribute charTermAttr = tokenStream.getAttribute(CharTermAttribute.class);
						        try{
						        	//int l;
						            while (tokenStream.incrementToken()) {
						                if (sb.length() > 0) {
						                    sb.append(" ");
						                }
						                term = charTermAttr.toString();
						                /*if(term.contains(".zz")){
						                	term = term.replaceAll(".zz", "-");
						                	String[] terms=term.split("-");
						                	String at="";
						                	for(String t : terms){
						                		//l = stemmer.stem(t.toCharArray(), t.length());
						                		//t = t.substring(0, l); 
								                //sb.append(t.toString(),0,l);
						              			sb.append(t + " ");
						              			at = at+t;
						                	}
						                	
						                	sb.append(at + " ");
						                }*/
						                
						                if(term.contains(".") && !term.matches(".*\\d+.*")){//&& StringUtils.isAlpha(term)){
						                	term=term.replaceAll("\\.", "");
						                	//sb.append(term);
						                }
						                //int l = stemmer.stem(charTermAttr.toString().toCharArray(), charTermAttr.toString().length());
						                //int l2 = stemmer.stem(term.toCharArray(), term.length());
						                //sb.append(charTermAttr.toString(),0,l);
						                //sb.append(term,0,l2);
						                //sb.append(term);
						                stemmer.setCurrent(term);
						                stemmer.stem();
						                sb.append(stemmer.getCurrent());
						                
						                /*if(term.contains("-")){
						                	String[] terms=term.split("-");
						                	String at="";
						                	for(String t : terms){
						              			sb.append(" " + t);
						              			at = at+t;
						                	}
						                	
						                	sb.append(" " + at);
						                }*/
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
							  
							  ///----------END OF TExt processin----------
							   
							   
							   
							  ((org.apache.lucene.document.Document) doc).add(new TextField("contents", sb.toString(), Store.YES));//new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))));
						      //doc.add(new StringField("contents", passage_content, Field.Store.YES));
							  //System.out.println(plainStr);
							  //System.out.println(sb.toString());
							  //writer.addDocument(doc);
							  
							  if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
								 
							        // New index, so we just add the document (no old document can be there):
							        System.out.println(".......adding " + docid + " passage "+ j + "--" + si + " " + ei);
							        writer.addDocument(doc);
							      } else {
							        // Existing index (an old copy of this document may have been indexed) so 
							        // we use updateDocument instead to replace the old one matching the exact 
							        // path, if present:
							        System.out.println("updating " + file);
							        writer.updateDocument(new Term("path", file.toString()), doc);
							      }
						      
						  }
		  
			  
		  }
	    
	    finally {
	        //reader.close();
	        writer.close();

	  	    Date end = new Date();
	  	    System.out.println(end.getTime() - start.getTime() + " total milliseconds");
	      }
	    
	    
	}
	catch (IOException e) {
    System.out.println(" caught a " + e.getClass() +
     "\n with message: " + e.getMessage());
	}
	}
	/** Indexes a single document */
	  static void indexDoc(IndexWriter writer, String docid, int si, int ei)throws IOException {//, long lastModified) throws IOException {
		  Path file = Paths.get("E:/documents/text/"+docid+".html");
	    try (InputStream stream = Files.newInputStream(file)) {
	    	
	      // make a new, empty document
	    	//System.out.println("Test 3.1");
	      BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
	      String         line = null;
	      StringBuilder  stringBuilder = new StringBuilder();
	      String         ls = System.getProperty("line.separator");

	      try {
	          while((line = reader.readLine()) != null) {
	              stringBuilder.append(line);
	              stringBuilder.append(ls);
	          }

	      
	      } finally {
	          reader.close();
	      }
	      
	    //index file name
	      Field fileNameField = new StringField("name",
	    	      file.getFileName().toString(),
	    	      Field.Store.YES);
	      
	   // Add the path of the file as a field named "path".  Use a
	      // field that is indexed (i.e. searchable), but don't tokenize 
	      // the field into separate words and don't index term frequency
	      // or positional information:
	      Field pathField = new StringField("path", file.toString(), Field.Store.YES);
	   
	      
	      
	      //String file_content = stringBuilder.toString();
	      //System.out.println(file_content);
	      //String[] passages = file_content.split("<P|<p");
	      //String[] passages = file_content.split("<P");
	      //String[] passages = file_content.split("<P>|<H1>|<H2>|<H3>|<H4>|<H5>|<H6>|<BR>|<HR>|<TABLE>|<TD>|<TH>|<TR>|<OL>|<UL>|<p>|<br>|<hr>");//|<p|<h1|<h2|<h3|<h4|<h5|<h6|<br|<hr|<table|<td|<th|<tr|<ol|<ul");
	      //String[] passages = file_content.split("(?i)<P|(?i)<H1|(?i)<H2|(?i)<H3|(?i)<H4|(?i)<H5|(?i)<H6|(?i)<BR|(?i)<HR|(?i)<TABLE|(?i)<TD|(?i)<TH|(?i)<TR|(?i)<OL|(?i)<UL");//|<p|<h1|<h2|<h3|<h4|<h5|<h6|<br|<hr|<table|<td|<th|<tr|<ol|<ul");
	      
	      //String[] passages = StringUtils.substringsBetween(file_content, "<P", "<P");
	      //String[] title = StringUtils.substringsBetween(file_content, "<body>", "</");
	      //System.out.println("path");
	      //String title = passages[0];
	      //String title;
	      Document dochtml;// = Jsoup.parse(title);
		  //String ptitle=""; //= dochtml.body().text();
		  //System.out.println("Title is" + ptitle);
	      //Field titleField = new StringField("title", ptitle, Field.Store.YES);
		  
		  RandomAccessFile f1 = new RandomAccessFile("E:/documents/text/"+docid+".html", "rw");
	    	
	    	f1.seek(si);
	    	String l = f1.readLine();
	    	int length = ei;
	    	while ( l.length() < length ) {
	    	  // Process line
	    		//System.out.println(l);
	    		l = l + f1.readLine();
	    	}
	    	l = l.substring(0, length);
		  
		  
		  ///////------FORMATING TEXT---------
		  StandardTokenizer stdToken = new StandardTokenizer();
		  //Tokenizer stdToken = new WhitespaceTokenizer();
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
		     
		        
		  
				  dochtml = Jsoup.parse(l);
				  String plainStr = dochtml.body().text();
				  //String[] validpas = plainStr.split(" ");
				  
				  
					  j++;
					  Field passageId = new StringField("id",
				    	      file.getFileName().toString()+"."+j,
				    	      Field.Store.YES);
					 
					  org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
					  doc.add(fileNameField);
					  doc.add(pathField);
					  doc.add(passageId);
					  //doc.add(titleField);
					  doc.add(new StringField("offset", si+"", Field.Store.YES));
					  doc.add(new StringField("length", ei+"", Field.Store.YES));
					  //doc.add(new LongPoint("modified", lastModified));
					  //((org.apache.lucene.document.Document) doc).add(new TextField("title", ptitle, Store.YES));
					  //System.out.println(passage_content);
					  //InputStream is = new ByteArrayInputStream(passage_content.getBytes());
					  
					  //String strippedText = passage_content.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
					  
					  //--------TEXT PROCESSING------------
					  TokenStream tokenStream;
					  //String nplainstr = plainStr.replaceAll("-", ".zz");
					  //stdToken.setReader(new StringReader(nplainstr));
					  stdToken.setReader(new StringReader(plainStr));
					  
					  tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(stdToken))), stopSet);
					  			    
				      //tokenStream = new PorterStemFilter(tokenStream);
				      tokenStream.reset();
				     //int l=0;
				     String term="";
				     StringBuilder sb = new StringBuilder();
				     //OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
				     CharTermAttribute charTermAttr = tokenStream.getAttribute(CharTermAttribute.class);
				        try{
				        	//int l;
				            while (tokenStream.incrementToken()) {
				                if (sb.length() > 0) {
				                    sb.append(" ");
				                }
				                term = charTermAttr.toString();
				                /*if(term.contains(".zz")){
				                	term = term.replaceAll(".zz", "-");
				                	String[] terms=term.split("-");
				                	String at="";
				                	for(String t : terms){
				                		//l = stemmer.stem(t.toCharArray(), t.length());
				                		//t = t.substring(0, l); 
						                //sb.append(t.toString(),0,l);
				              			sb.append(t + " ");
				              			at = at+t;
				                	}
				                	
				                	sb.append(at + " ");
				                }*/
				                
				                if(term.contains(".") && !term.matches(".*\\d+.*")){//&& StringUtils.isAlpha(term)){
				                	term=term.replaceAll("\\.", "");
				                	//sb.append(term);
				                }
				                //int l = stemmer.stem(charTermAttr.toString().toCharArray(), charTermAttr.toString().length());
				                //l = stemmer.stem(term.toCharArray(), term.length());
				                //sb.append(charTermAttr.toString(),0,l);
				                //sb.append(term,0,l);
				                sb.append(term);
				                
				                /*if(term.contains("-")){
				                	String[] terms=term.split("-");
				                	String at="";
				                	for(String t : terms){
				              			sb.append(" " + t);
				              			at = at+t;
				                	}
				                	
				                	sb.append(" " + at);
				                }*/
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
					  
					  ///----------END OF TExt processin----------
					   
					   
					   
					  ((org.apache.lucene.document.Document) doc).add(new TextField("contents", sb.toString(), Store.YES));//new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))));
				      //doc.add(new StringField("contents", passage_content, Field.Store.YES));
					  //System.out.println(plainStr);
					  //writer.addDocument(doc);
					  
					  if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
						 
					        // New index, so we just add the document (no old document can be there):
					        System.out.println(".......adding " + file.getFileName().toString() + " passage "+ j + "--");
					        writer.addDocument(doc);
					      } else {
					        // Existing index (an old copy of this document may have been indexed) so 
					        // we use updateDocument instead to replace the old one matching the exact 
					        // path, if present:
					        System.out.println("updating " + file);
					        writer.updateDocument(new Term("path", file.toString()), doc);
					      }
				      
				  }
			  }
	      
	      
	      
	    
	    
	  
}
