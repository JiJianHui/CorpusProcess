package corpus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import util.Relations;
import util.Toolkit;
import util.Words;

/**
 * 关联词分析的主程序。主要是从标注的p1,p2,p3文件中提取出关联词和对应的句间关系。
 * 首先是从标注结果中提取出关联词及对应的句间关系；然后通过反转关联词表来生成句间关系表。
 * @author rainbow
 * @time   Apr 21, 2013
 */
public class Corpus
{
	//private Toolkit tool;
	
	private boolean debug;
	
	private int     		 expMoreNum;	    //相比2012年python程序的显式关系多出的数目
	private int     		 impMoreNum;	    //相比2012年python程序的隐式关系多出的数目
	
	private String  		 p1RelFile;		    //p1句间关系文件路径
	private String  		 p2RelFile;
	private String  		 p3RelFile;
	
	private String  		 p1WordFile;	    //p1关联词文件路径
	private String  		 p2WordFile;
	private String  		 p3WordFile;
	
	private Vector<Words>     words;			//语料中出现的所有关联词的集合
	private Vector<Relations> relations;		//语料中出现的所有句间关系的集合
	
	private Vector<String>   p1Files;		    //待处理的句群文件地址列表
	private Vector<String>   p2Files;		    //待处理的复句文件地址列表
	private Vector<String>   p3Files;		    //待处理的分句文件地址列表
	
	private Vector<Words>     p1Words;		    //p1标注文件中出现的关联词(包括显隐)
	private Vector<Words>     p2Words;
	private Vector<Words>     p3Words;
	
	private Vector<Relations> p1Relations;	//p1标注文件中出现的句间关系(包括显隐)
	private Vector<Relations> p2Relations;
	private Vector<Relations> p3Relations;
	
	private int nearNum;	//位于相邻句子之间的句间关系
	private int crosNum;	//位于交叉句子之间的句间关系
	
	public Corpus()
	{
		this.debug       = true;
		
		this.expMoreNum  = 0;
		this.impMoreNum  = 0;
		
		this.words       = new Vector<Words>();
		this.relations   = new Vector<Relations>();
		
		this.p1Files     = new Vector<String>();
		this.p2Files     = new Vector<String>();
		this.p3Files     = new Vector<String>();
		
		this.p1Words     = new Vector<Words>();
		this.p2Words     = new Vector<Words>();
		this.p3Words     = new Vector<Words>();
		
		this.p1Relations = new Vector<Relations>();
		this.p2Relations = new Vector<Relations>();
		this.p3Relations = new Vector<Relations>();
		
		this.p1RelFile   = "F:\\Result\\p1Rel.txt";
		this.p2RelFile   = "F:\\Result\\p2Rel.txt";
		this.p3RelFile   = "F:\\Result\\p3Rel.txt";
		
		this.p1WordFile  = "F:\\Result\\p1Word.txt";
		this.p2WordFile  = "F:\\Result\\p2Word.txt";
		this.p3WordFile  = "F:\\Result\\p3Word.txt";
		
		this.nearNum     = 0;
		this.crosNum     = 0;
	}
	
	/**
	 * 将某一指定路径下的标志结果添加到当前的分析结果中。
	 * 因此需要先将前期统计的路径清除，但是前期的结果不能清除。
	 * @param dir：标注文件所在的路径，包括子文件夹
	 */
	public void run(String dir)
	{
		//首先获取标注文件路径
		Toolkit.getFiles(dir, this.p1Files, ".p1");
		Toolkit.getFiles(dir, this.p2Files, ".p2");
		Toolkit.getFiles(dir, this.p3Files, ".p3");
		
		//对标注的文件进行统计关联词以及对应的句间关系
		countWords(this.p1Files, this.p1Words);
		countWords(this.p2Files, this.p2Words);
		countWords(this.p3Files, this.p3Words);
		
		//利用关联词表来生成句间关系表
		this.reverse(this.p1Words, this.p1Relations);
		this.reverse(this.p2Words, this.p2Relations);
		this.reverse(this.p3Words, this.p3Relations);

		//将分别为p1、p2、p3的统计结果合并
		this.mergeAllWords();
		this.mergeAllRels();
		
		
		//将句间关系表写入到文件中
		this.writeRelationsToFile(this.p1Relations, this.p1RelFile);
		this.writeRelationsToFile(this.p2Relations, this.p2RelFile);
		this.writeRelationsToFile(this.p3Relations, this.p3RelFile);
		
		//将关联词表写入到文件中
		this.writeWordToFile(this.p1Words, this.p1WordFile);
		this.writeWordToFile(this.p2Words, this.p2WordFile);
		this.writeWordToFile(this.p3Words, this.p3WordFile);
		
		//System.out.println("Exp Increase:" + this.expMoreNum);
		//System.out.println("Imp Increase:" + this.impMoreNum);
		
		System.out.println("Near: " + this.nearNum);
		System.out.println("Cros: " + this.crosNum);
	}
	
	//依次针对获得的每一个标注文件进行处理,统计标注结果中
	public void countWords(Vector<String> files, Vector<Words> words)
	{
		String fPath = null;
	
    	for(int i = 0; i < files.size(); i++)
    	{
    		fPath = files.elementAt(i);
    		
    		if(this.debug) System.out.println(fPath);
    			
    		this.parseFile(fPath, words);
    	}
	}
	
	/**
	 * 针对特定的文件进行处理，提取出所有的标注行，依次处理
	 * @param fPath
	 * @param words
	 */
	public void parseFile(String fPath, Vector<Words> words)
	{
		String   fName    = null;
		String   text     = null;
		String   line     = null;	//当前标注行
		
		BufferedReader br = null;
		
		fName = fPath.substring( 0, fPath.lastIndexOf('.') );
		text  = Toolkit.readFileToString( fName + ".txt" );
		text  = text.replace("\r\n", "\n");
		
		try 
		{
			br = new BufferedReader(new FileReader(fPath));
			
			//针对每一行的标注结果进行处理，统计标注的关联词和关系
			while( (line=br.readLine()) != null )
			{
				this.parseLine(line, text, fPath, words);
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	/**
	 * 针对特定的标注文件中每一行标注结果进行处理，从中提取出关联词和句间关系
	 * @param line  ：一行标注结果
	 * @param text  ：该行对应的原始语料内容
	 * @param fPath ：该行标注结果对应的文件路径
	 * @param words ：该类型的标注结果拥有的统一的关联词词表
	 */
	private void parseLine(String line, String text, String fPath, 
			               Vector<Words> words)
	{
		int i,j;
		int max  = 0; 					//实际的最大索引  
		int type = 0;					//标注的类型：p1,p2
		
		int expNum  = 0, impNum  = 0;	//显式和隐式的种类数目
		int wordBeg = 0, wordEnd = 0;	//关联词在文章中的位置.
		int lineBeg = 0, lineEnd = 0;	//该句子在文中的实际位置.
		
		String[] lists = null;			//标注行的数组表示
		String   word  = null;			//临时表示word
		String   relID = null;			//标注的句间关系
		
		Vector<String> expWords  = new Vector<String>();	//当前显示关联词集合
		Vector<String> impWords  = new Vector<String>();	//当前隐式关联词集合
		
		if( fPath.endsWith(".p1") ) type = 1;
		if( fPath.endsWith(".p2") ) type = 2;
		if( fPath.endsWith(".p3") ) type = 3;
		
		lists = line.split(" ");
		
		if( lists.length < 6 ) return;
		
		if(type == 1) 
			max = lists.length - 4;
		else 
			max = lists.length - 2;
		
		//修正句群中的前后关系，可能会出现下面的例子:
		//1235 1934 1224 1234 0 1 1236 具体而言 0 1 1 5-1-1解释说明
		lineBeg = Toolkit.getLineBeg(line);
		lineEnd = Toolkit.getLineEnd(line);
		
		expNum  = Integer.valueOf(lists[4]);
		impNum  = Integer.valueOf(lists[5]);
		
		
		//获取关系编号
		relID   = Toolkit.getRelID( lists[lists.length - 1] );
		
		//不考虑0承接关系
		if( relID.equals("0") ) return;
		
		
		//获取该标注行中的显式关联词
		for(i = 0; i < expNum; i++)
		{
			wordBeg = Integer.valueOf( lists[6 + i*2] );
			wordEnd = Integer.valueOf( lists[7 + i*2] );
			
			//去除错误的位置的关联词
			if(wordBeg > wordEnd) continue;
			if(wordBeg < lineBeg || wordBeg > lineEnd) continue;
			if(wordEnd < lineBeg || wordEnd > lineEnd) continue;
			
			
			//这里添加beg主要是为了排序并列关联词使用
			word = text.substring(wordBeg, wordEnd);
			word = Toolkit.formatWord(word);
			
			if( word.length() > 0 )
			{
				word = wordBeg + ":" + word;
				this.addTempWords(expWords, word);
			}
		}
		
		//获取该标注行中的隐式关联词
		for(j = 0; j < impNum; j++)
		{
			i = 5 + 2*expNum + impNum + 1 + j;
			
			if(i >= max) break;
			
			word    = Toolkit.formatWord( lists[i] );
			wordBeg = Integer.valueOf( lists[6 + expNum*2 + j] );
			
			if(word.length() > 0)
			{
				word = wordBeg + ":" + word;
				this.addTempWords(impWords, word);
			}
		}
		
		//将行中出现的所有关联词加入到系统的关联词集合中
		int exp = 1;
		String finalWord = "";
		
		if(type == 3)
		{
			//对于p3分句中出现的关联词要进行并列关联词处理
			exp = 1;
			for(i = 0; i < expWords.size(); i++)
			{
				word = expWords.get(i); 
				word = word.substring( word.indexOf(':')+1 );
				
				if(i == 0)
					finalWord = finalWord + word;
				else
					finalWord = finalWord + "..."+word;
			}
			
			if(i > 0)
				this.addWord(line,words, finalWord, relID, exp);
			
			exp = 0;
			for(finalWord = "", i = 0; i < impWords.size(); i++)
			{
				word = impWords.get(i);
				word = word.substring( word.indexOf(':')+1 );
				
				if(i == 0)
					finalWord = finalWord + word;
				else
					finalWord = finalWord + "..."+word;
			}
			
			if(i > 0)
				this.addWord(line, words,  finalWord, relID, exp);
		}
		else
		{
			//对于p1,p2中的关联词直接加入到词表中就是啦
			exp = 1;
			for(i = 0; i < expWords.size(); i++)
			{
				word = expWords.get(i);
				word = word.substring( word.indexOf(':')+1 );
				this.addWord(line, words, word, relID, exp);
			}
			
			exp = 0;
			for(i = 0; i < impWords.size(); i++)
			{
				word = impWords.get(i);
				word = word.substring( word.indexOf(':') + 1);
				this.addWord(line, words, word, relID, exp);
			}
			
			//该错误个数是python程序中被误当做并列关联词的个数
			this.expMoreNum = this.expMoreNum + expWords.size();
			if(expWords.size() > 0) this.expMoreNum = this.expMoreNum - 1;
			
			this.impMoreNum = this.impMoreNum + impWords.size();
			if(impWords.size() > 0 ) this.impMoreNum = this.impMoreNum - 1;
		}
		
		//----------------------------------------
		//针对既没有显式关联词也没有隐式关联词的句间关系
		if(expNum == 0 && impNum == 0)
		{
			//根据后续的要求在此进行修改
		}
		
		//----------------------------------------
		//相邻关系和交叉关系计算
		
	}
	
	//将一个关联词加入到系统word表中
	private void addWord(String line, Vector<Words> words, String word, String rID, int type)
	{
		//首先判断是否已经有该word
		word = word.trim();
		Words temp = null;
		
		for(int i = 0; i < words.size(); i++)
		{
			temp = words.get(i);
			if( temp.getName().equalsIgnoreCase(word) )
			{
				temp.addRelations(rID, type, 1);
				return;
			}
		}
		
		//对于第一次出现的单词，新建一个word类加入到Words中
		temp = new Words(word);
		temp.addRelations(rID, type, 1);
		
		words.add(temp);
		
		//修改相邻句间关系
		String[] lists = line.split(" ");
		//int beg1 = Integer.valueOf(lists[0]).intValue();
		int end1 = Integer.valueOf(lists[1]).intValue();
		
		int beg2 = Integer.valueOf(lists[2]).intValue();
		//int end2 = Integer.valueOf(lists[3]).intValue();
		
		if( Math.abs(end1 - beg2) < 4 ) 
			this.nearNum = this.nearNum + 1;
		else 
			this.crosNum = this.crosNum + 1;
		
	}

	/**
	 * 按照word的出现次序添加到该行标注中出现的临时关联词集合中
	 * @param words
	 * @param word
	 */
	private void addTempWords(Vector<String> words, String word)
	{
		int i = 0;
		int wordIndex = 0;
		String temp   = null;
		
		if( words.size() == 0) 
		{
			words.add(word);
			return;
		}
		
		int beg  = Integer.valueOf( word.substring(0, word.indexOf(':')) );
		
		//寻找插入位置
		for(i=0; i<words.size(); i++)
		{
			temp = words.get(i);
			wordIndex = Integer.valueOf(temp.substring(0, temp.indexOf(':')));
			
			if(beg<wordIndex)
			{
				break;
			}
			
			//去除重复标注的关联词
			if(beg == wordIndex)
			{
				//新添加的word是已存在的word的一部分,直接返回
				if( word.length() <= temp.length() )
				{
					return;
				}
				else
				{
					//新添加的word是已存在的父亲，那么删除孩子，添加父亲
					words.remove(i);
					break;
				}
			}
		}
		
		words.add(i, word);
	}
	
	//-----------------------------------句间关系处理-----------------------------
	
	//填充位置，主要是为了统一格式，确保每个关系都能够在Vecotor中出现
	public void initRelations(Vector<Relations> relations)
	{
		for(int index = 0; index < Toolkit.relNO.length; index++)
		{
			Relations newRel = new Relations( Toolkit.relNO[index] );
			relations.add(newRel);
		}
	}
	
	
	/**
	 * 由p1,p2,p3对应的关联词表来生成对应的句间关系表。
	 * @param words
	 * @param relations
	 */
	public void reverse(Vector<Words> words, Vector<Relations> relations)
	{
		int      exp     = 0;
		String   relID   = "";
		Integer  relNum  = 0;
		
		Words     curWord = null;
		Relations curRel  = null;
		
		HashMap<String, Integer> tempRel = null; //关联词指示的关系集合
		
		
		//首先初始化该关系集合,注册所有的句间关系编号
		this.initRelations(relations);
		
		//利用每个关联词的显式和隐式句间关系集合来更新总体的句间关系集合
		for(int i = 0; i < words.size(); i++)
		{
			curWord = words.get(i);
			tempRel = curWord.getExpRelations();
			
			//显式句间关系和关联词的对应
			exp = 1;
			for(Map.Entry<String, Integer> item: tempRel.entrySet() )
			{
				relID  = item.getKey();
				relNum = item.getValue();
				
				this.addRel(relations, relID, curWord.getName(), relNum, exp);
			}
			
			//隐式关联词和句间关系的对应
			exp = 0;
			tempRel = curWord.getImpRelations();
			for(Map.Entry<String, Integer> item:tempRel.entrySet())
			{
				relID  = item.getKey();
				relNum = item.getValue();
				
				this.addRel(relations, relID, curWord.getName(), relNum, exp);
			}
		}
		
		//对句间关系的关联词集合进行排序
		for(int index = 0; index < relations.size(); index++)
		{
			curRel = relations.get(index);
			curRel.sortWords();
		}
	}
	
	private void addRel(Vector<Relations> relations, String relID, String word,
			            int num, int type)
	{
		Relations curRel = null;
		
		//首先判断是否已经存在
		for(int i = 0; i < relations.size(); i++)
		{
			curRel = relations.get(i);
			
			if( curRel.getRelID().equals(relID) )
			{
				curRel.addWord(word, num, type);
				return;
			}
		}
		
		//因为初始化时，已经将所有关系加入，
		//防止意外出现，对于不存在的Rel
		curRel = new Relations(relID);
		curRel.addWord(word, num, type);
		
		if(this.debug) 
			System.err.println("*****An unRegister relaion: " + relID+"*****");
	}

	/**
	 * 将一个句间关系集合写入到文件中。
	 * @param relations
	 * @param fileName：需要写入的文件名
	 */
	public void writeRelationsToFile(Vector<Relations> relations, String fileName)
	{
		String   line   = null;
		Relations curRel = null;
		
		try
		{
			File outFile  = new File(fileName);
			FileWriter fw = new FileWriter(outFile);
		
			for(int i = 0; i < relations.size(); i++)
			{
				curRel = relations.get(i);
				line   = curRel.convertToString();
				
				fw.write(line + "\r\n");
			}
			
			fw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 将一个关联词表集合写入到文件中.
	 * @param words
	 * @param fileName
	 */
	public void writeWordToFile(Vector<Words> words, String fileName)
	{
		String line    = null;
		Words   curWord = null;
		
		try
		{
			File outFile  = new File(fileName);
			FileWriter fw = new FileWriter(outFile);
		
			for(int i = 0; i < words.size(); i++)
			{
				curWord = words.get(i);
				line    = curWord.convertToString();
				
				fw.write(line + "\r\n");
			}
			
			fw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void test(Vector<Relations> relations)
	{
		int i = 0;
		Relations curRel = null;
		
		System.out.println("************************");
		for(i = 0; i < relations.size(); i++)
		{
			curRel = relations.get(i);
			System.out.print( "\n" + curRel.getRelID() );
			
			for(Map.Entry<String, Integer> item: curRel.getExpWords().entrySet())
			{
				System.out.print("\t1 " + item.getKey() + " " + item.getValue());
			}
			for(Map.Entry<String, Integer> item: curRel.getImpWords().entrySet())
			{
				System.out.print("\t0 " + item.getKey() + " " + item.getValue());
			}
		}
		System.out.println("\n************************");
	}
	
	/**
	 * 将分布在p1,p2和p3中的关联词信息合并成一个大的词表
	 */
	public void mergeAllWords()
	{
		for(Words item:this.p1Words)
		{
			this.words.add(item);
		}
		for(Words item:this.p2Words)
		{
			
		}
	}
	public void mergeAllRels()
	{
		
	}
	
	public static void main(String[] args)
	{
//		String dir = "F:\\Corpus Data\\check_pub_guo_only";
		String dir = "F:\\Corpus Data\\check_pub_guo_only";
		
		Corpus cor = new Corpus();
		
		cor.run(dir);
	}
	
}
