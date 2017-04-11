package lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

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



public class IndexFiles2 {
  
  private IndexFiles2() {}
  int counter = 0;
  /** Index all text files under a directory. */
  public static void main(String[] args) {
    String usage = "java org.apache.lucene.demo.IndexFiles"
                 + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
                 + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                 + "in INDEX_PATH that can be searched with SearchFiles";
    String indexPath = "E:/index8";
    String docsPath = "E:/documents/text";
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
      Analyzer analyzer = new StandardAnalyzer();
      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

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

      IndexWriter writer = new IndexWriter(dir, iwc);
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
      String[] passages = file_content.split("<P|<H1|<H2|<H3|<H4|<H5|<H6|<BR|<HR|<TABLE|<TD|<TH|<TR|<OL|<UL|<p|<h1|<h2|<h3|<h4|<h5|<h6|<br|<hr|<table|<td|<th|<tr|<ol|<ul");
      //passages = StringUtils.substringsBetween(file_content, "<P", "<P");
      //String[] title = StringUtils.substringsBetween(file_content, "<body>", "</");
      //System.out.println("path");
      Document dochtml;
      String ptitle = "";
      /*if(passages.length>1){
    	  String title = passages[1];
    	  dochtml = Jsoup.parse(title);
    	  ptitle = dochtml.body().text();
    	  System.out.println("Title is" + ptitle);
	  }*/
	  
      //Field titleField = new StringField("title", ptitle, Field.Store.YES);
      if(passages!= null){
    	  if(passages.length>1){
        	  String title = passages[1];
        	  dochtml = Jsoup.parse(title);
        	  ptitle = dochtml.body().text();
        	  System.out.println("Title is" + ptitle);
    	  }
		  for(int i=1; i<passages.length; i++){
			  
			  //System.out.println(i);
			  
			  String passage_content = passages[i];
			  
			  dochtml = Jsoup.parse(passage_content);
			  String plainStr = dochtml.body().text();
			  String[] validpas = plainStr.split(" ");
			  
			  if(validpas.length>10){
				  //if(passage_content.trim().isEmpty()){
					//  System.out.println("abc");
					  //continue;
				  //}
					 
				  org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
				  doc.add(fileNameField);
				  doc.add(pathField);
				  //doc.add(titleField);
				  doc.add(new StringField("offset", file_content.indexOf(passage_content)+"", Field.Store.YES));
				  doc.add(new StringField("length", passage_content.length()+"", Field.Store.YES));
				  doc.add(new LongPoint("modified", lastModified));
				  ((org.apache.lucene.document.Document) doc).add(new TextField("title", ptitle, Store.YES));
				  //System.out.println(passage_content);
				  //InputStream is = new ByteArrayInputStream(passage_content.getBytes());
				  
				  //String strippedText = passage_content.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
				  //dochtml = Jsoup.parse(passage_content);
				  //String plainStr = dochtml.body().text();
				  
				  ((org.apache.lucene.document.Document) doc).add(new TextField("contents", plainStr, Store.YES));//new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))));
			      //doc.add(new StringField("contents", passage_content, Field.Store.YES));
				  //System.out.println(plainStr);
				  //writer.addDocument(doc);
				  
				  if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				        // New index, so we just add the document (no old document can be there):
				        System.out.println("adding " + file.getFileName().toString() + " passage "+ i);
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
