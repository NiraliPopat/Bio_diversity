package lucene;

import java.io.IOException;
import java.util.logging.LogManager;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Constants;

public class QueryIndex {
	public static LogManager logger = new LogManager(QueryIndex.class);

	 private FSDirectory  directory;

	 private IndexSearcher  searcher;

	 public QueryIndex(){
	  init();
	 }

	 public void close() {
	  try {

	   if (null != searcher) searcher.close();
	   if (null != directory) directory.close();

	   this.searcher = null;
	   this.directory = null;

	  } catch (IOException e) {
	   //logger.error(e);
	   //throw new BusinessException("Unable to shutdown Lucene Index");
	  }
	 }

	 private void init() {
	  try {

	   directory = FSDirectory.open(Constants.WORDNET_SYNONYMS_DIR);
	   searcher = new IndexSearcher(directory);

	  } catch (Exception e) {
	   logger.error(e);
	   throw new BusinessException("Unable to open Lucene Directory (path = %s)", Constants.WORDNET_SYNONYMS_DIR.getAbsolutePath());
	  }
	 }

	 public Collection<String> process(String term) throws BusinessException {
	  try {

	   if (null == directory || null == searcher) init();
	   Query query = new TermQuery(new Term(Syns2Index.F_WORD, term));

	   TotalHitCountCollector thcc = new TotalHitCountCollector();
	   searcher.search(query, thcc);

	   Set<String> results = new TreeSet<String>();
	   ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;

	   for (ScoreDoc hit : hits) {
	    Document doc = searcher.doc(hit.doc);

	    String[] values = doc.getValues(Syns2Index.F_SYN);
	    results.addAll(SetUtils.toSet(values));
	   }

	   if (0 == thcc.getTotalHits()) logger.debug("No Results Found (term = %s)", term);
	   else logger.info("Synonyms Found (term = %s, total = %s, list = %s)", term, results.size(), SetUtils.toString(results, ", "));

	   return results;

	  } catch (IOException e) {
	   logger.error(e);
	   throw new BusinessException("Unable to Execute Query (term = %s)", term);
	  }
	 }

}
