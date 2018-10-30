package quaryService;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
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

	private IndexWriter writer;//这个类用来写入索引
	
	public Indexer(String indexDir) throws IOException {//构造方法
		Directory dir = FSDirectory.open(Paths.get(indexDir));//打开一个文件夹，用于存放索引
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());//采用标准分词器
		config.setOpenMode(OpenMode.CREATE);//配置打开方式
		writer = new IndexWriter(dir, config);//创建IndexWriter，第一个参数为存放目录，第二个参数为配置
	}
	
	public void close() throws IOException {//关闭writer的方法，用于释放资源
		writer.close();//关闭writer
	}
	
	//这个方法是遍历文件夹下所有文件并选择符合条件文件写入索引的方法，返回写入的文档总数
	public int index(String dataDir, String fieldName) throws IOException{
		File[] files=new File(dataDir).listFiles();
		for(File file:files){
			if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead()){
				indexFile(file, fieldName);
			}
		}
		return writer.numDocs();
	}
	
	//这个方法是写入索引的方法，将生成的document对象写入到索引中
	private void indexFile(File file, String fieldName) throws IOException{
		System.out.println("indexing..."+file.getCanonicalPath());
		Document doc=getDocument(file, fieldName);
		writer.addDocument(doc);
	}
	
	//这个方法是生成Document对象的方法，Document对象就是对文档各个属性的封装
	protected Document getDocument(File file, String fieldName) throws IOException{
		Document doc=new Document();
		doc.add(new Field(fieldName, new FileReader(file),TextField.TYPE_NOT_STORED));
		doc.add(new Field("filename",file.getName(),TextField.TYPE_STORED));
		doc.add(new Field("fullpath",file.getCanonicalPath(),TextField.TYPE_STORED));
		return doc;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		String[] fieldNameList = {"description"};
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
