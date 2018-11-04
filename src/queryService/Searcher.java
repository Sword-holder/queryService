package queryService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
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
	static String indexDir="index";
	static String answerDir = "info\\answer";
	static String query = "";
	
	public static ArrayList<String> search(String indexDir,String query, String fieldName) throws IOException, ParseException{
		Directory dir=FSDirectory.open(Paths.get(indexDir));
		IndexSearcher searcher=new IndexSearcher(DirectoryReader.open(dir));
		QueryParser parser=new QueryParser(fieldName,new SmartChineseAnalyzer());
		Query q=parser.parse(query);
		TopDocs hits=searcher.search(q, 10);
		ArrayList<String> filelist = new ArrayList<String>();
		float maxscore = hits.getMaxScore();
		for(ScoreDoc scoreDoc:hits.scoreDocs) {
			Document doc=searcher.doc(scoreDoc.doc);
			if (scoreDoc.score / maxscore > 0.5) {
				String filename = doc.get("filename");
				filelist.add(filename);
			}
		}
		System.out.println("搜索到的答案有：");
		for (String file:filelist) {
			System.out.println(file);
		}
		return filelist;
	}
	
	public static String noAnswer() {
		String sorry = "sorry";
		return sorry + "disc";
	}
	
	//读取文本
	public static String read_content(String filename) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename), "UTF-8"));
		String line = "";
		StringBuffer content = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			content.append(line);
		}
		return content + "";
	}
	
	//两个集合地交集，结果放在第一个集合里
	public static void intersection(ArrayList<String> list1, ArrayList<String> list2) {
		for (int i = 0; i < list1.size(); i++) { //求交集
			int index = list2.indexOf(list1.get(i));
			if (index == -1) { //原先的结果列表不符合新的信息，将其舍去
				list1.remove(i);
			}
		}
	}
	
	//判断用户说的是否是再见
	public static void isGoodBye(String words) {
		if(words.equals("再见")) {
			System.out.println("很高兴为您服务，再见！");
			System.exit(0);
		}
	}
	
	public static String diagnosis(ArrayList<String> answerlist, String cont) throws Exception {
		String ans = answerlist.get(0);
		String expected_platform = read_content("info\\platform\\" + ans);
		if (expected_platform.equals(cont)) {
			return giveAnswer(answerlist);
		}
		else {
			return "platformerror";
		}
	}
	
	public static boolean overwhelm(ArrayList<String> answerlist) {
		if (answerlist.size() == 1) {
			return true;
		}
		return false;
	}
	
	public static String giveAnswer(ArrayList<String> answerlist) {
		return answerlist.get(0) + "disc";
	}
	
	public static String getAnswer(String query) throws IOException, ParseException, Exception {
		String response = "next";
		ArrayList<String> answerlist = null;
		ArrayList<String> tempanswer = null;
		boolean diagnose = false;
		String[] info = query.split("\n");
		if (info.length > 0 && info[info.length - 1].equals("diagnose"))
			diagnose = true;
			
		for (String desp:info) { //desp是用户的每一句话
			String field = desp.split(":")[0];
			String cont = desp.split(":")[1];
			if (field.equals("description")) {
				answerlist = search(indexDir, cont, field);
			}
			else if (field.equals("platform")) { //对操作系统进行诊断
				return diagnosis(answerlist, cont);
			}
			else {
				tempanswer = search(indexDir, cont, field);
				intersection(answerlist, tempanswer);
			}
			
			if (answerlist.size() == 0) { //没有搜索到答案
				return noAnswer();
			}
			if (overwhelm(answerlist) && !diagnose) { //如果确定是第一项，就直接给出答案
				return giveAnswer(answerlist);
			}
		}
		return response;
	}

	public static void main(String[] args) throws IOException, ParseException, Exception {
		String description = "description:宕机";
		String production = "production:IBM Notes";
		String edition = "edition:9.0";
		String platform = "platform:MAC OS";
		String query = description + "\n" + production + "\n" + edition + "\n" + platform;
		System.out.println(getAnswer(query));
		
		
		
		/*ArrayList<String> answerlist = null;
		ArrayList<String> tempanswer = null;
		String query;
		Scanner scanner = new Scanner(System.in);
		
		boolean[] has_info = {false, false, false, false};//刚开始四个属性都不具备
		
		
		if (!has_info[0]) {
			//询问问题描述信息
			System.out.println("请问您有什么问题需要帮忙吗？");
			query =  scanner.nextLine();
			isGoodBye(query);
			answerlist = search(indexDir, query, "description");
			if (answerlist.size() == 0) {//没有找到答案
				noAnswer();
				System.exit(0);
			}
			else {//找到了一个以上答案
				for (String answer:answerlist) {
					System.out.println(answerDir + "\\" + answer);
				}
			}	
		}
		
		
		if (!has_info[1]) {
			//询问产品
			System.out.println("请问您使用的是什么产品呢？");
			query =  scanner.nextLine();
			isGoodBye(query);
			tempanswer = search(indexDir, query, "production");
			if (tempanswer.size() == 0) {
				noAnswer();
				System.exit(0);
			}
			else {
				intersection(answerlist, tempanswer);
				if (answerlist.size() == 1) { //精准地搜索到了答案
					give_answer(answerlist.get(0));
				}
				for (String answer:answerlist) {
					System.out.println(answerDir + "\\" + answer);
				}
			}
		}
		
		
		if (!has_info[2]) {
			//询问产品版本号
			System.out.println("请问您的产品版本号是多少？");
			query =  scanner.nextLine();
			isGoodBye(query);
			tempanswer = search(indexDir, query, "edition");
			if (tempanswer.size() == 0) {
				noAnswer();
				System.exit(0);
			}
			else {
				intersection(answerlist, tempanswer);
				if (answerlist.size() == 1) { //精准地搜索到了答案
					give_answer(answerlist.get(0));
				}
				for (String answer:answerlist) {
					System.out.println(answerDir + "\\" + answer);
				}
			}
		}
		
		if (!has_info[3]) {
			//询问操作平台
			System.out.println("请问您的产品使用的平台（操作系统）是？");
			query =  scanner.nextLine();
			isGoodBye(query);
			tempanswer = search(indexDir, query, "platform");
			if (tempanswer.size() == 0) {
				noAnswer();
				System.exit(0);
			}
			else {
				intersection(answerlist, tempanswer);
			}
		}
		
		//询问结束以后
		if (answerlist.size() == 1) { //精准地搜索到了答案
			give_answer(answerlist.get(0));
		}
		System.out.println("为您找到了多种问题，请选择您的问题：");
		int no = 1;
		for (String answer:answerlist) {
			System.out.println(String.valueOf(no) + ":" + read_content("info\\description\\" + answer));
		}
		
		scanner.close();*/
	}
	
}
