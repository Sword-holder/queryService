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
		System.out.println("�������Ĵ��У�");
		for (String file:filelist) {
			System.out.println(file);
		}
		return filelist;
	}
	
	public static String noAnswer() {
		String sorry = "sorry";
		return sorry + "disc";
	}
	
	//��ȡ�ı�
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
	
	//�������ϵؽ�����������ڵ�һ��������
	public static void intersection(ArrayList<String> list1, ArrayList<String> list2) {
		for (int i = 0; i < list1.size(); i++) { //�󽻼�
			int index = list2.indexOf(list1.get(i));
			if (index == -1) { //ԭ�ȵĽ���б������µ���Ϣ��������ȥ
				list1.remove(i);
			}
		}
	}
	
	//�ж��û�˵���Ƿ����ټ�
	public static void isGoodBye(String words) {
		if(words.equals("�ټ�")) {
			System.out.println("�ܸ���Ϊ�������ټ���");
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
			return "��������ƽ̨�����⣬Ӧ����" + expected_platform;
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
			
		for (String desp:info) { //desp���û���ÿһ�仰
			String field = desp.split(":")[0];
			String cont = desp.split(":")[1];
			if (field.equals("description")) {
				answerlist = search(indexDir, cont, field);
			}
			else if (field.equals("platform")) { //�Բ���ϵͳ�������
				return diagnosis(answerlist, cont);
			}
			else {
				tempanswer = search(indexDir, cont, field);
				intersection(answerlist, tempanswer);
			}
			
			if (answerlist.size() == 0) { //û����������
				return noAnswer();
			}
			if (overwhelm(answerlist) && !diagnose) { //���ȷ���ǵ�һ���ֱ�Ӹ�����
				return giveAnswer(answerlist);
			}
		}
		return response;
	}

	public static void main(String[] args) throws IOException, ParseException, Exception {
		String description = "description:崻�";
		String production = "production:IBM Notes";
		String edition = "edition:9.0";
		String platform = "platform:MAC OS";
		String query = description + "\n" + production + "\n" + edition + "\n" + platform;
		System.out.println(getAnswer(query));
		
		
		
		/*ArrayList<String> answerlist = null;
		ArrayList<String> tempanswer = null;
		String query;
		Scanner scanner = new Scanner(System.in);
		
		boolean[] has_info = {false, false, false, false};//�տ�ʼ�ĸ����Զ����߱�
		
		
		if (!has_info[0]) {
			//ѯ������������Ϣ
			System.out.println("��������ʲô������Ҫ��æ��");
			query =  scanner.nextLine();
			isGoodBye(query);
			answerlist = search(indexDir, query, "description");
			if (answerlist.size() == 0) {//û���ҵ���
				noAnswer();
				System.exit(0);
			}
			else {//�ҵ���һ�����ϴ�
				for (String answer:answerlist) {
					System.out.println(answerDir + "\\" + answer);
				}
			}	
		}
		
		
		if (!has_info[1]) {
			//ѯ�ʲ�Ʒ
			System.out.println("������ʹ�õ���ʲô��Ʒ�أ�");
			query =  scanner.nextLine();
			isGoodBye(query);
			tempanswer = search(indexDir, query, "production");
			if (tempanswer.size() == 0) {
				noAnswer();
				System.exit(0);
			}
			else {
				intersection(answerlist, tempanswer);
				if (answerlist.size() == 1) { //��׼���������˴�
					give_answer(answerlist.get(0));
				}
				for (String answer:answerlist) {
					System.out.println(answerDir + "\\" + answer);
				}
			}
		}
		
		
		if (!has_info[2]) {
			//ѯ�ʲ�Ʒ�汾��
			System.out.println("�������Ĳ�Ʒ�汾���Ƕ��٣�");
			query =  scanner.nextLine();
			isGoodBye(query);
			tempanswer = search(indexDir, query, "edition");
			if (tempanswer.size() == 0) {
				noAnswer();
				System.exit(0);
			}
			else {
				intersection(answerlist, tempanswer);
				if (answerlist.size() == 1) { //��׼���������˴�
					give_answer(answerlist.get(0));
				}
				for (String answer:answerlist) {
					System.out.println(answerDir + "\\" + answer);
				}
			}
		}
		
		if (!has_info[3]) {
			//ѯ�ʲ���ƽ̨
			System.out.println("�������Ĳ�Ʒʹ�õ�ƽ̨������ϵͳ���ǣ�");
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
		
		//ѯ�ʽ����Ժ�
		if (answerlist.size() == 1) { //��׼���������˴�
			give_answer(answerlist.get(0));
		}
		System.out.println("Ϊ���ҵ��˶������⣬��ѡ���������⣺");
		int no = 1;
		for (String answer:answerlist) {
			System.out.println(String.valueOf(no) + ":" + read_content("info\\description\\" + answer));
		}
		
		scanner.close();*/
	}
	
}
