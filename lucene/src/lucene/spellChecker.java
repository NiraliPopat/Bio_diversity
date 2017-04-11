package lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Dictionary;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.spell.SuggestMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class spellChecker {
	public static void main(String[] args) throws IOException{
		
		//Object indexPath;
		String index = "E:/index6";
		Directory directory = FSDirectory.open(Paths.get(index));
		IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		SpellChecker spellChecker=new SpellChecker(directory);
		Object field;
		//IndexReader indexReader;
		LuceneDictionary dictionary = new LuceneDictionary(indexReader, "contents");
		IndexWriterConfig iwc = new IndexWriterConfig();
        //SpellChecker spellChecker = new SpellChecker(spellIndexDirectory);
        spellChecker.indexDictionary(dictionary, iwc, false);
		String[] answer=spellChecker.suggestSimilar("protein", 5);//("proteins", 100,indexReader,"contents",SuggestMode.SUGGEST_MORE_POPULAR);
		System.out.println(answer.length);
		for (String ans : answer)
		System.out.println("..." + ans);
	}

}
