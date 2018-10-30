package quaryService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
	static String[] fieldNameList = {"description"};
	static String indexDir="index";
	static String answerDir = "info\\answer";
	static String query = "";
	
	public static ArrayList<String> search(String indexDir,String query, String fieldName) throws IOException, ParseException{
		Directory dir=FSDirectory.open(Paths.get(indexDir));
		IndexSearcher searcher=new IndexSearcher(DirectoryReader.open(dir));
		QueryParser parser=new QueryParser(fieldName,new StandardAnalyzer());
		Query q=parser.parse(query);
		TopDocs hits=searcher.search(q, 10);
		ArrayList<String> filelist = new ArrayList<String>();
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=searcher.doc(scoreDoc.doc);
			String filename = doc.get("filename");
			filelist.add(filename);
		}
		return filelist;
	}
	

	public static void main(String[] args) throws IOException, ParseException {
		ArrayList<String> answerlist = null;
		boolean found = false;
		
		for (String fieldName:fieldNameList) {
			System.out.println("请问您的" + fieldName + "是？");
			Scanner scanner = new Scanner(System.in);
			query =  scanner.nextLine();
			if(query.equals("再见")) {
				System.out.println("再见");
				break;
			}
			answerlist = search(indexDir, query, fieldName);
			if (answerlist.size() == 0) {
				System.out.println("找不到答案！");
			}
			else {
				for (String answer:answerlist) {
					System.out.println(answerDir + "\\" + answer);
				}
			}
		}	
	}
	
}
