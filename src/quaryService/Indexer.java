package quaryService;
import java.io.*;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

	private IndexWriter writer;//���������д������
	
	public Indexer(String indexDir) throws IOException {//���췽��
		Directory dir = FSDirectory.open(Paths.get(indexDir));//��һ���ļ��У����ڴ������
		IndexWriterConfig config = new IndexWriterConfig(new SmartChineseAnalyzer());//���ñ�׼�ִ���
		config.setOpenMode(OpenMode.CREATE);//���ô򿪷�ʽ
		writer = new IndexWriter(dir, config);//����IndexWriter����һ������Ϊ���Ŀ¼���ڶ�������Ϊ����
	}
	
	public void close() throws IOException {//�ر�writer�ķ����������ͷ���Դ
		writer.close();//�ر�writer
	}
	
	//��������Ǳ����ļ����������ļ���ѡ����������ļ�д�������ķ���������д����ĵ�����
	public int index(String dataDir, String fieldName) throws IOException{
		File[] files=new File(dataDir).listFiles();
		for(File file:files){
			if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead()){
				indexFile(file, fieldName);
			}
		}
		return writer.numDocs();
	}
	
	//���������д�������ķ����������ɵ�document����д�뵽������
	private void indexFile(File file, String fieldName) throws IOException{
		System.out.println("indexing..."+file.getCanonicalPath());
		Document doc=getDocument(file, fieldName);
		writer.addDocument(doc);
	}
	
	//�������������Document����ķ�����Document������Ƕ��ĵ��������Եķ�װ
	protected Document getDocument(File file, String fieldName) throws IOException{
		Document doc=new Document();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"));
		String line = "";
		StringBuffer content = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			content.append(line);
		}
		doc.add(new Field(fieldName, content+"", TextField.TYPE_NOT_STORED));
		doc.add(new Field("filename",file.getName(),TextField.TYPE_STORED));
		doc.add(new Field("fullpath",file.getCanonicalPath(),TextField.TYPE_STORED));
		return doc;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		String[] fieldNameList = {"description", "production", "edition", "platform"};
		String indexDir="index";
		Indexer indexer=new Indexer(indexDir);
		for (String fieldName:fieldNameList) {
			String dataDir="info\\" + fieldName;
			
			int numberIndexed = indexer.index(dataDir, fieldName);
			System.out.println("number of indexed are " + numberIndexed);
		}
		indexer.close();
	}

}
