package TransformFormat;

import java.io.IOException;
import java.util.Vector;

import util.Toolkit;

public class Counter
{
	private String corpusRoot;
	
	private Vector<String> txtFileLists;
	
	private Vector<String> p1FileLists;
	private Vector<String> p2FileLists;
	private Vector<String> p3FileLists;
	
	public Counter(String disRoot)
	{
		this.corpusRoot = disRoot;
		
		this.txtFileLists = new Vector<String>();
		
		this.p1FileLists  = new Vector<String>();
		this.p2FileLists  = new Vector<String>();
		this.p3FileLists  = new Vector<String>();
		
		Toolkit.getFiles(this.corpusRoot, this.txtFileLists, ".txt");
		Toolkit.getFiles(this.corpusRoot, this.p1FileLists, ".p1");
		Toolkit.getFiles(this.corpusRoot, this.p2FileLists, ".p2");
		Toolkit.getFiles(this.corpusRoot, this.p3FileLists, ".p3");
	}
	
	/**
	 * 计算所有的关系数目
	 * @return
	 * @throws IOException
	 */
	public int countRelationNum() throws IOException
	{
		int relNum = 0;
		
		for(String fPath:this.p1FileLists)
		{
			relNum = relNum + this.countRelNumInFile(fPath);
		}
		for(String fPath:this.p2FileLists)
		{
			relNum = relNum + this.countRelNumInFile(fPath);
		}
		for(String fPath:this.p3FileLists)
		{
			relNum = relNum + this.countRelNumInFile(fPath);
		}
		
		return relNum;
	}
	
	/***
	 * 计算一个文件中的关系数目
	 * @param fPath
	 * @return
	 * @throws IOException
	 */
	private int countRelNumInFile(String fPath) throws IOException
	{
		int relNum = 0;
		
		Vector<String> lines = new Vector<String>();
		Toolkit.readFileToLines(fPath, lines);
		
		for(String line:lines)
		{
			if( line.length() > 0 )
			{
				if( line.equalsIgnoreCase(Toolkit.relSeperator) )
					relNum++;
			}
		}
		return relNum;
	}
	
	/**
	 * 计算所有的词数
	 * @return
	 */
	public long countWordNum()
	{
		long wordNum = 0;
		
		for(String fPath:this.txtFileLists)
		{
			String rawText = Toolkit.readFileToString(fPath);
			
			wordNum += Toolkit.countChineseWordNumInText(rawText);
		}
		
		return wordNum;
	}
	
	public static void main(String[] args) throws IOException
	{
		String corRoot = "F:\\Distribution Data\\Distribution Data HIT\\Corpus Data\\TXT";
		
		//String corRoot = "F:\\Test\\Distribution Data\\Corpus_pubGuoOnly\\TXT";
		
		Counter counter = new Counter(corRoot);
		
		int  relNum  = counter.countRelationNum();
		long wordNum = counter.countWordNum();
		
		System.out.println(relNum);
		System.out.println(wordNum);
	}
}
