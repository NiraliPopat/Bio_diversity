package lucene;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.synonym.SynonymFilter;
//import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.util.Version;
import org.apache.lucene.wordnet.*;

public class WordnetDemo {
	Analyzer analyzer;
	public static void main(String[] args) throws Exception {
	

		String[] words = new String[] { "hard", "cell", "tissue", "mutation", "genes", "SLE", "GENES"};
		SynonymMap map = new SynonymMap(new FileInputStream("C:/Users/NiraliPopat/Downloads/prolog/wn_s.pl"));
		 for (int i = 0; i < words.length; i++) {
		     String[] synonyms = map.getSynonyms(words[i]);
		     System.out.println(words[i] + ":" + java.util.Arrays.asList(synonyms).toString());
		 }
	}  
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
	    Tokenizer source = new ClassicTokenizer();
	    TokenStream filter = new StandardFilter(source);
	    filter = new LowerCaseFilter(filter);
	    org.apache.lucene.analysis.synonym.SynonymMap mySynonymMap = null;
		filter = new SynonymFilter(filter, mySynonymMap, false);
	    //Whatever other filter you want to add to the chain, being mindful of order.
	    return new TokenStreamComponents(source, filter);
	} 
}

