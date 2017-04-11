package lucene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.EnglishMinimalStemmer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.AfterEffectL;
import org.apache.lucene.search.similarities.BasicModelIn;
import org.apache.lucene.search.similarities.DFRSimilarity;
import org.apache.lucene.search.similarities.DistributionLL;
import org.apache.lucene.search.similarities.IBSimilarity;
import org.apache.lucene.search.similarities.LambdaDF;
import org.apache.lucene.search.similarities.NormalizationH1;
import org.apache.lucene.search.similarities.NormalizationH2;
import org.apache.lucene.search.similarities.NormalizationH3;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

/*
002 * Licensed to the Apache Software Foundation (ASF) under one or more
003 * contributor license agreements.  See the NOTICE file distributed with
004 * this work for additional information regarding copyright ownership.
005 * The ASF licenses this file to You under the Apache License, Version 2.0
006 * (the "License"); you may not use this file except in compliance with
007 * the License.  You may obtain a copy of the License at
008 *
009 *     http://www.apache.org/licenses/LICENSE-2.0
010 *
011 * Unless required by applicable law or agreed to in writing, software
012 * distributed under the License is distributed on an "AS IS" BASIS,
013 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
014 * See the License for the specific language governing permissions and
015 * limitations under the License.
016 */


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class IndexFiles3 {
  
  private IndexFiles3() {}
  int counter = 0;
  /** Index all text files under a directory. */
  public static void main(String[] args) {
    String usage = "java org.apache.lucene.demo.IndexFiles"
                 + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
                 + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                 + "in INDEX_PATH that can be searched with SearchFiles";
    String indexPath = "E:/index12"; 
    // is DFR
    //2 is normal
    //3 is ib with h3
    //4 is ib with h2 with porter stemmer
    //5 is ib with h2 with s stemmer
    //6 is ib with h2 without stemmer
  //7 is  without stemmer without <p
    //8 is basic with all tags
    //9 is ib with h2 and stopwords without stemmer
    //10 like  without ib, lower tags
    String docsPath = "E:/new";
    boolean create = true;
    for(int i=0;i<args.length;i++) {
      if ("-index".equals(args[i])) {
        indexPath = args[i+1];
        i++;
      } else if ("-docs".equals(args[i])) {
        docsPath = args[i+1];
        i++;
      } else if ("-update".equals(args[i])) {
        create = false;
      }
    }

    if (docsPath == null) {
      System.err.println("Usage: " + usage);
      System.exit(1);
    }

    final Path docDir = Paths.get(docsPath);
    if (!Files.isReadable(docDir)) {
      System.out.println("Document directory dhfndk '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
      System.exit(1);
    }
    
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
        new AfterEffectL(),
        new NormalizationH1());*/
      //iwc.setSimilarity(similarity);
      IndexWriter writer = new IndexWriter(dir, iwc);

      if (create) {
        // Create a new index in the directory, removing any
        // previously indexed documents:
        iwc.setOpenMode(OpenMode.CREATE);
      } else {
        // Add new documents to an existing index:
        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
      }
      System.out.println("Test 1");

      // Optional: for better indexing performance, if you
      // are indexing many documents, increase the RAM
      // buffer.  But if you do this, increase the max heap
      // size to the JVM (eg add -Xmx512m or -Xmx1g):
      //
      // iwc.setRAMBufferSizeMB(256.0);

      //IndexWriter writer = new IndexWriter(dir, iwc);
      System.out.println("Test 2");
      indexDocs(writer, docDir);
      System.out.println("Test 3");

      // NOTE: if you want to maximize search performance,
      // you can optionally call forceMerge here.  This can be
      // a terribly costly operation, so generally it's only
      // worth it when your index is relatively static (ie
      // you're done adding documents to it):
      //
      // writer.forceMerge(1);

      writer.close();

      Date end = new Date();
      System.out.println(end.getTime() - start.getTime() + " total milliseconds");

    } catch (IOException e) {
      System.out.println(" caught a " + e.getClass() +
       "\n with message: " + e.getMessage());
    }
  }

  /**
135   * Indexes the given file using the given writer, or if a directory is given,
136   * recurses over files and directories found under the given directory.
137   * 
138   * NOTE: This method indexes one document per input file.  This is slow.  For good
139   * throughput, put multiple documents into your input file(s).  An example of this is
140   * in the benchmark module, which can create "line doc" files, one document per line,
141   * using the
142   * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
143   * >WriteLineDocTask</a>.
144   *  
145   * @param writer Writer to the index where the given file/dir info will be stored
146   * @param path The file to index, or the directory to recurse into to find files to index
147   * @throws IOException If there is a low-level I/O error
148   */
  static void indexDocs(final IndexWriter writer, Path path) throws IOException {
	  System.out.println("Test 2.1");
	  
    if (Files.isDirectory(path)) {
    	System.out.println("Test 2.2");
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          try {
        	  System.out.println("Test 2.3");
        	  System.out.println(file.toString());
            indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
            System.out.println("Test 2.4");
          } catch (IOException ignore) {
            // don't index files that can't be read.
          }
          return FileVisitResult.CONTINUE;
        }
      });
    } else {
      indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
    }
  }

  /** Indexes a single document */
  static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
	  
    try (InputStream stream = Files.newInputStream(file)) {
    	
      // make a new, empty document
    	System.out.println("Test 3.1");
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
      
      
      // Add the last modified date of the file a field named "modified".
      // Use a LongPoint that is indexed (i.e. efficiently filterable with
      // PointRangeQuery).  This indexes to milli-second resolution, which
      // is often too fine.  You could instead create a number based on
      // year/month/day/hour/minutes/seconds, down the resolution you require.
      // For example the long value 2011021714 would mean
      // February 17, 2011, 2-3 PM.
      
    
      // Add the contents of the file to a field named "contents".  Specify a Reader,
      // so that the text of the file is tokenized and indexed, but not stored.
      // Note that FileReader expects the file to be in UTF-8 encoding.
      // If that's not the case searching for special characters will fail.
      
      
      String file_content = stringBuilder.toString();
      //System.out.println(file_content);
      //String[] passages = file_content.split("<P|<p");
      //String[] passages = file_content.split("<P");
      int j=0;
      String[] passages = file_content.split("<P|<H1|<H2|<H3|<H4|<H5|<H6|<BR|<HR|<TABLE|<TD|<TH|<TR|<OL|<UL");//|<p|<h1|<h2|<h3|<h4|<h5|<h6|<br|<hr|<table|<td|<th|<tr|<ol|<ul");
     
      //String[] passages = StringUtils.substringsBetween(file_content, "<P", "<P");
      //String[] title = StringUtils.substringsBetween(file_content, "<body>", "</");
      //System.out.println("path");
      //String title = passages[0];
      String title;
      Document dochtml;// = Jsoup.parse(title);
	  String ptitle=""; //= dochtml.body().text();
	  //System.out.println("Title is" + ptitle);
      //Field titleField = new StringField("title", ptitle, Field.Store.YES);
	  
	  
	  ///////------FORMATING TEXT---------
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
	     
	        
	  //////------FORMATING TEXT---------
      if(passages!= null){
    	  if(passages.length>1){
        	  title = passages[1].split("</P|</H1|</H2|</H3|</H4|</H5|</H6|</p")[0];
        	  dochtml = Jsoup.parse(title);
        	  ptitle = dochtml.body().text();
        	  System.out.println("Title is" + ptitle);
    	  }
		  for(int i=1; i<passages.length; i++){
			  
			  //System.out.println(i);
			  //cnames = cname.split(":");
              //cname =  cnames[0];
			  //String[] passage_contents = passages[i].split("</P|</p");
			  String[] passage_contents = passages[i].split("</P");
			  String passage_content = passage_contents[0];
			  //if(passage_content.trim().isEmpty()){
				//  System.out.println("abc");
				  //continue;
			  //}
			  dochtml = Jsoup.parse(passage_content);
			  String plainStr = dochtml.body().text();
			  String[] validpas = plainStr.split(" ");
			  
			  if(validpas.length>10){
				  j++;
				  Field passageId = new StringField("id",
			    	      file.getFileName().toString()+"."+j,
			    	      Field.Store.YES);
				 
				  org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
				  doc.add(fileNameField);
				  doc.add(pathField);
				  doc.add(passageId);
				  //doc.add(titleField);
				  doc.add(new StringField("offset", file_content.indexOf(passage_content)+"", Field.Store.YES));
				  doc.add(new StringField("length", passage_content.length()+"", Field.Store.YES));
				  doc.add(new LongPoint("modified", lastModified));
				  ((org.apache.lucene.document.Document) doc).add(new TextField("title", ptitle, Store.YES));
				  //System.out.println(passage_content);
				  //InputStream is = new ByteArrayInputStream(passage_content.getBytes());
				  
				  //String strippedText = passage_content.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
				  
				  //--------TEXT PROCESSING------------
				  TokenStream tokenStream;
				  stdToken.setReader(new StringReader(plainStr));
				  
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
				  
				  ///----------END OF TExt processin----------
				   
				   
				   
				  ((org.apache.lucene.document.Document) doc).add(new TextField("contents", sb.toString(), Store.YES));//new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))));
			      //doc.add(new StringField("contents", passage_content, Field.Store.YES));
				  System.out.println(sb.toString());
				  //writer.addDocument(doc);
				  
				  if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				        // New index, so we just add the document (no old document can be there):
					  //j++;
				        System.out.println(".......adding " + file.getFileName().toString() + " passage "+ j + " " + passageId);
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
      
      
    }
    
  }
}
