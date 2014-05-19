package WordDict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import util.Toolkit;

/**
 * wordDict包的主程序文件，主要用来整理关联词表。
 * 
 * 1:提取出哪些关联词(显式关联词)出现一定指示了句间关系，哪些关联词出现了但是没有指示句间关系。
 *   首先需要获取指示句间关系的显式关联词词表，然后在语料中查找出现的同名的但是没有标注指示句间关系的关联词
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Aug 25, 2013
 */
public class WordDict
{
	/**填充的字符**/
	private char fillChar;	
	
	/**原始语料*/
	private Vector<String> rawFiles;
	
	/**指示了句间关系的单个显式关联词词表*/
	private HashMap<String, Integer> singleExpWords;
	/**指示了句间关系的并列显式关联词词表*/
	private HashMap<String, Integer> parallelExpWords;

	
	/**那些出现的可以指示句间关系的但是并没有指示句间关系的单独关联词*/
	private HashMap<String, Integer> unSingleWords;	
	/**那些出现的可以指示句间关系的但是并没有指示句间关系的并列关联词*/
	private HashMap<String, Integer> unParallelWords;
	
	/**主要用来排序使用*/
	private List<Map.Entry<String, Integer>> sortSingleWords;
	
	public WordDict()
	{
		this.fillChar         = '_';
		this.rawFiles         = new Vector<String>();
		
		this.singleExpWords   = new HashMap<String, Integer>();
		this.parallelExpWords = new HashMap<String, Integer>();
		
		this.unSingleWords    = new HashMap<String, Integer>();
		this.unParallelWords  = new HashMap<String, Integer>();
	}
	
	public void run()
	{
		try 
		{
			//加载显式关联词词表
			this.loadExpWords(Toolkit.expWordFile);
			this.sortSingleExpWords();
			this.saveResult();
			
			//加载原始语料文件列表
			Toolkit.getFiles(Toolkit.corpusData, this.rawFiles, ".txt");
			//Toolkit.getFiles(Toolkit.testData, this.rawFiles, ".txt");
			 	
			//针对每个文件进行处理,最终的结果存放在了两个词表中
			for( String rawFile: this.rawFiles )
			{
				System.out.println(rawFile);
				this.parseRawFile(rawFile);
			}
			
			//对结果进行平滑处理，采用的平滑方法是 (M + Ka) / (N + Ka)
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/** 加载程序的关联词词表文件*/
	public void loadExpWords(String fName) throws IOException
	{
		int      wNum  = 0;
		String   wName = null;
		
		String   line  = null;
		String[] lists = null;
		
		BufferedReader br = new BufferedReader(new FileReader(fName));
		
		while( (line = br.readLine())!=null )
		{
			lists = line.split("\t");
			
			if(lists.length > 0 )
			{
				wName = lists[0];
				wNum  = Integer.valueOf(lists[1]).intValue();
				
				if( wName.indexOf(Toolkit.parallelWordSplitor) == -1 )
				{
					this.singleExpWords.put( wName, wNum );
				}
				else
				{
					this.parallelExpWords.put( wName, wNum );
				}
			}
		}
		
		br.close();
	}

	public void sortSingleExpWords()
	{

		this.sortSingleWords = new ArrayList<Map.Entry<String, Integer>>
		                      (this.singleExpWords.entrySet());
		
		Collections.sort(this.sortSingleWords, 
				new Comparator<Map.Entry<String,Integer>>(){
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2){
				return o1.getKey().length() - o2.getKey().length();
			}
		}
		);
	}
	
	/**
	 * 针对原始语料文件进行处理，统计哪些地方出现了关联词，
	 * 以及该关联词指示句间关系的情况。指示句间关系的关联词情况已经知道，
	 * 只剩下那些不指示句间关系的关联词的情况未知。
	 * @param rawFile
	 * @throws IOException 
	 */
	private void parseRawFile(String rawFile) throws IOException
	{
		//原始语料文本
		String text   = Toolkit.readFileToString(rawFile).replace("\r\n", "\n");
		char[] chars  = text.toCharArray();
		
		//对应的标注结果
		String p1File = rawFile.replace(".txt", ".p1");
		String p2File = rawFile.replace(".txt", ".p2");
		String p3File = rawFile.replace(".txt", ".p3");
		
		//将标注结果里涉及到的关联词全部擦除，来统计那些不指示句间关系的“关联词”
		this.parseAnnotationFile(p1File, chars);
		this.parseAnnotationFile(p2File, chars);
		this.parseAnnotationFile(p3File, chars);
		
		//统计剩下的文章语料中出现的“关联词”个数，主要问题是并列关联词怎么处理
		String remainText = String.valueOf(chars);
		this.parseRemainedText(chars, remainText);
		
	}
	
	/**
	 * 处理标注的文件,
	 * 将标注结果里涉及到的关联词全部擦除来统计那些不指示句间关系的“关联词”
	 * 
	 * @param fName：标注结果文件名
	 * @param text:原始语料的内容
	 * @throws IOException 
	 */
	private void parseAnnotationFile(String fName, char[] chars) throws IOException
	{
		int expNum  = 0;	            //显式和隐式的种类数目
		int wordBeg = 0, wordEnd = 0;	//关联词在文章中的位置
		int lineBeg = 0, lineEnd = 0;	//该句子在文中的实际位置
		
		String   relID = null;
		
		String   line  = null;			//标注行
		String[] lists = null;			//标注行的数组表示
		
		BufferedReader bf = new BufferedReader( new FileReader(fName) );
		
		while( (line = bf.readLine()) != null )
		{
			lists = line.split(" ");
			if( lists.length < 6 )  continue;

			relID = Toolkit.getRelID( lists[lists.length - 1] );
			
			if( relID.equals("0") ) continue;
			
			lineBeg = Toolkit.getLineBeg(line);
			lineEnd = Toolkit.getLineEnd(line);
			
			expNum  = Integer.valueOf(lists[4]);
			
			//获取该标注行中的显式关联词位置
			for(int i = 0; i < expNum; i++)
			{
				wordBeg = Integer.valueOf( lists[6 + i*2] );
				wordEnd = Integer.valueOf( lists[7 + i*2] );
				
				//去除错误的位置的关联词
				if(wordBeg > wordEnd) continue;
				if(wordBeg < lineBeg || wordBeg > lineEnd) continue;
				if(wordEnd < lineBeg || wordEnd > lineEnd) continue;
				
				//从原始语料中将对应位置的单词转化为空格...然后再统一删除
				for(int index = wordBeg; index < wordEnd; index++)
				{
					chars[index] = this.fillChar;
				}
			}
		}
		
		bf.close();
	}
	
	/**处理剩下的语料内容，从中找出没有指示句间关系的关联词*/
	private void parseRemainedText(char[] chars, String text)
	{
		String line = null;
		
		//从一篇文章中剩下的部分识别出句子序列
		for(int index = 0; index < chars.length; )
		{
			boolean end = false;

			//i循环表示的是从index位置开始去识别一个句子
			int i = 0;
			for(i = index; i < chars.length; i++)
			{
				char temp = chars[i];
			
				//判断该字符是否是句子边界
				end = Toolkit.isSentenceBoundary(temp);
				
				//到达句子边界就从i循环跳出
				if(end == true) break;
			}
			
			//完成了一个句子的识别，句子分割的位置就是i所代表的位置
			//此时，从index位置，到i位置就是一个完整的句子
			line  = text.substring(index, i);
			index = i + 1; //修正下一个句子开始的位置。  
			
			this.parseRemainedLine(line);
		}
	}
	
	/**处理一篇文章中剩余语料中的一行句子，判断是否存在没有指示句间关系的关联词*/
	private void parseRemainedLine(String line)
	{
		int index = 0;
		int wNum  = 0;
		
		String   wName = null;
		String[] lists = null;
		
		//遍历并列关联词词表来判断是否存在并列关联词
		for( Map.Entry<String, Integer> item:this.parallelExpWords.entrySet() )
		{
			wName = item.getKey();
			lists = wName.split(Toolkit.parallelWordSplitor);
			
			boolean  match = Toolkit.containsParallelWord(line, wName);
	
			//如果存在并列关联词，首先保存，其次从该行中删除对应的关联词
			if( match )
			{
				this.addWords(wName, 1, this.unParallelWords);
				
				for( index = 0; index < lists.length; index++ )	
					line.replaceFirst(lists[index], "_");	
			}
		}
		
		//判断对应的single关联词是否出现在该行中
//		for( Map.Entry<String, Integer> item:this.singleExpWords.entrySet() )
//		{
//			wName = item.getKey();
//			wNum  = Toolkit.countWordsInLine(line, wName);
//			
//			if(wNum > 0 )
//			{
//				this.addWords(wName, wNum, this.unSingleWords);
//				line.replaceAll(wName, "_");
//			}
//		}
		
		for(index = this.sortSingleWords.size() - 1; index >= 0; index--)
		{
			wName = this.sortSingleWords.get(index).getKey();
			wNum  = Toolkit.countWordsInLine(line, wName);
			
			if(wNum > 0 )
			{
				this.addWords(wName, wNum, this.unSingleWords);
				line.replaceAll(wName, "_");
			}
		}
	}
	
	/***/
	private void addWords(String wName, Integer wNum, HashMap<String, Integer> dicts)
	{
		if(dicts.containsKey(wName))
		{
			wNum = wNum + dicts.get(wName);
			dicts.remove(wName);
		}
		
		dicts.put(wName, wNum);
	}
	
	/**
	 * 将出现但是并没有指示句间关系的关联词保存起来
	 * @param fName
	 * @param single: true：表示单个关联词，false：表示并列关联词
	 */
	private void saveWordsToFile(String fName, boolean single)
	{
		String  wName = null;
		Integer wNum  = null;	//指示句间关系的数目
		Integer unNum = null;	//普通词的数目
		
		try
		{
			FileWriter fw = new FileWriter( new File(fName) );
			
			if(single == true)
			{
				for( Map.Entry<String, Integer> item: this.singleExpWords.entrySet() )
				{
					wName = item.getKey();
					wNum  = item.getValue();
					unNum = this.unSingleWords.get(wName);

					if(unNum == null) unNum = 0;
					
					fw.write(wName + "\t" + String.valueOf(wNum) + "\t" + String.valueOf(unNum) + "\r\n");
				}
			}
			else
			{
				for( Map.Entry<String, Integer> item: this.parallelExpWords.entrySet())
				{
					wName = item.getKey();
					wNum  = item.getValue();
					unNum = this.unParallelWords.get(wName);

					if(unNum == null) unNum = 0;
					
					fw.write(wName + "\t" + String.valueOf(wNum) + "\t" + String.valueOf(unNum) + "\r\n");
				}
			}
		
			fw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveUnParallelWordsToFile(String fName)
	{
		this.saveWordsToFile(fName, false);
	}
	
	public void saveUnSingleWordsToFile(String fName)
	{
		this.saveWordsToFile(fName, true);
	}
	
	private void saveResult()
	{
		try
		{
			String fName  = "test.txt"; 
			FileWriter fw = new FileWriter( new File(fName) );
			for(int index = 0; index < this.sortSingleWords.size(); index++)
			{
				fw.write(this.sortSingleWords.get(index).getKey() + "\r\n");
			}
			fw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		String unSingleWordFile   = "F:\\Result\\unSingleWord.txt";
		String unParallelWordFile = "F:\\Result\\unParallelWord.txt";
		
		WordDict wDict = new WordDict();
		
		wDict.run();
		wDict.saveUnSingleWordsToFile(unSingleWordFile);
		wDict.saveUnParallelWordsToFile(unParallelWordFile);
	}
	
}
