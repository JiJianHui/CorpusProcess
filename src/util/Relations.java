package util;
import java.util.HashMap;
import java.util.Map;

/**
 * 主要用于统计关系的分布
 * @author rainbow
 * @time   Apr 21, 2013
 */
public class Relations
{
	private String relID;	//该句间关系的编号1-1-2
	//private String name;	//该句间关系的名称(不包括编号)
	//private int allNum;		//该句间关系出现的总次数
	
	private int expNum;		//该句间关系作为显式关系的次数
	private int impNum;		//该句间关系作为隐式关系出现的次数
	
	//第一个String:指关联词的名称，第二个Integer指关联词出现的次数
	private HashMap<String, Integer> expWords;	
	private HashMap<String, Integer> impWords;
	
	public Relations(String rID)
	{
		this.relID  = rID;
		//this.name   = name;
		//this.allNum = 0;
		
		this.expNum = 0;
		this.impNum = 0;
		
		this.expWords = new HashMap<String, Integer>();
		this.impWords = new HashMap<String, Integer>();
	}
	
	//向该关系的关联词集合中添加一个关联词
	public void addWord(String word, int num, int type)
	{
		if( type == 1)
		{
			this.expNum = this.expNum + num;
			this.addWord(this.expWords, word, num);
		}
		else
		{
			this.impNum = this.impNum + num;
			this.addWord(this.impWords, word, num);
		}
	}
	
	private void addWord(HashMap<String, Integer> words, String word, int num)
	{
		int temp = 0;
		
		//该关系已经存在于关系集合中
		if( words.containsKey(word) )
		{
			temp = words.get(word) + num;
			
			//覆盖掉原先的值
			words.remove(word);
			words.put(word, temp);
		}
		else
		{
			//对于不存在该关系集合中的关系进行直接修改
			words.put(word, num);
		}
	}
	

	/**
	* 将当前关系表示为字符串形式，格式如下：
	* [name allNum expNum impNum expKind impKind [X]word n...[Y]word n ....]
	* @return
	*/
	public String convertToString()
	{
		Integer wordNum  = 0;
		String  wordName = null;
		
		String line  = this.relID + '\t';
		
		//添加数量信息
		int allNum   = this.expNum + this.impNum;
		line = line + String.valueOf(allNum) + '\t';
		
		line = line + String.valueOf(this.expNum) + '\t';
		line = line + String.valueOf(this.impNum) + '\t';
		
		line = line + String.valueOf(this.expWords.size()) + '\t';
		line = line + String.valueOf(this.impWords.size()) + '\t';
		
		//添加显式关联词,循环的最后一个\t正好用来分割隐式关联词
		for(Map.Entry<String, Integer> item: this.expWords.entrySet() )
		{
			wordName = item.getKey();
			wordNum  = item.getValue();
			
			wordName = "[X]" + wordName + '\t' + wordNum.toString();
			
			line     = line + wordName + '\t';
		}
		
		//添加隐式关联词
		for(Map.Entry<String, Integer> item: this.impWords.entrySet())
		{
			wordName = item.getKey();
			wordNum  = item.getValue();
			
			wordName = "[Y]" + wordName + '\t' + wordNum.toString();
			
			line     = line + wordName + '\t';
		}
		
		//将line最后的\t消除
		line = line.trim();
		
		return line;
	}
	
	//对hashmap关联词集合进行排序
	public void sortWords()
	{
		
	}
	
	public String getRelID(){ return this.relID; }
	
	public int getExpNum(){ return this.expNum; }
	public int getImpNum(){ return this.impNum; }
	public int getAllNum(){ return this.expNum+this.impNum; }

	public HashMap<String, Integer> getExpWords(){ return this.expWords; }
	public HashMap<String, Integer> getImpWords(){ return this.impWords; }
}
