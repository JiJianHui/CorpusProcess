package util;
import java.util.Vector;

/**
 * 标注的每一行作为一个记录
 * @author rainbow
 * @time   May 21, 2013
 */
public class Record
{	 
	private int            arg1Beg;	  //第一个元素的起始位置
	private int            arg1End;
	private String         arg1Content;
	
	private int            arg2Beg;	  //第二个元素的起始位置
	private int            arg2End;
	private String 		   arg2Content;
	
	private int            expNum;	  //该记录中的显式关联词个数
	private int            impNum;	  //该记录中的隐式关联词个数
	
	private String         senseID;	  //句间关系编号
	private String         senseContent; 
	
	private String         line;      //该记录的原始文本标注行
	private String         file;      //该记录对应的文档名称
	private int 		   type;	  //标注行所属的标注类型p1:1 .p2:2 p3:3
	
	private Vector<String> expWords;  //显式关联词集合	
	private Vector<String> impWords;  //隐式关联词集合
	
	public Record(String line, String file)
	{
		this.line     = line;
		this.file     = file;
		this.type     = Integer.valueOf( file.substring(file.length()-1) );
		
		this.expWords = new Vector<String>();
		this.impWords = new Vector<String>();
		
		//this.init();
	}
	
	
	
	/**
	private void init()
	{
		String lists[] = this.line.split(" ");
		
		this.arg1Beg   = Integer.valueOf(lists[0]).intValue();
		this.arg1End   = Integer.valueOf(lists[1]).intValue();
		
		this.arg2Beg   = Integer.valueOf(lists[2]).intValue();
		this.arg2End   = Integer.valueOf(lists[3]).intValue();
		
		this.expNum = Integer.valueOf(lists[4]).intValue();
		this.impNum = Integer.valueOf(lists[5]).intValue();
		
		//调整两个元素的开始和结束
		if(this.arg1Beg > this.arg2Beg)
		{
			int temp  = this.arg1Beg;
			this.arg1Beg = this.arg2Beg;
			this.arg2Beg = temp;
			
			temp = this.arg1End;
			this.arg1End = this.arg2End;
			this.arg2End = temp;
		}
		
		//获取关联词
		//this.getExpWords(this.line);
		//this.getImpWords(this.line);
	}
	**/
	/**
	public void getExpWords( String line )
	{
		Integer wordBeg = 0;
		Integer wordEnd = 0;
		
		String  expWord = null;
		String  lists[] = line.split(" ");
		
		//将多个关联词的起始位置放置在一个表中，供排序
		ArrayList<Integer> begs = new ArrayList<Integer>();
		ArrayList<Integer> ends = new ArrayList<Integer>();
		
		
		//获取该标注行中的显式关联词
		for(int i = 0; i < expNum; i++)
		{
			wordBeg = Integer.valueOf( lists[6 + i*2] );
			wordEnd = Integer.valueOf( lists[7 + i*2] );
			
			//去除错误的位置的关联词
			if(wordBeg > wordEnd) continue;
			if(wordBeg < this.arg1Beg || wordBeg > this.arg2End) continue;
			if(wordEnd < this.arg1Beg || wordEnd > this.arg2End) continue;
			
			begs.add(wordBeg);
			ends.add(wordEnd);
		}
		
		//从中找出beg最小的位置，获取关联词，并删除
		while(begs.size() > 0)
		{
			int min = 0;
			
			for(int index = 0; index < begs.size(); index++)
			{
				if( begs.get(index) < begs.get(min) ) 
					min = index;
			}
			
			//获取关联词,该方法太慢....
			wordBeg = begs.get(min);
			wordEnd = ends.get(min);
			
			expWord = Toolkit.getWordFromFile(wordBeg, wordEnd, this.file);
			this.expWords.add(expWord);
			
			
			//删除对应的位置
			begs.remove(min);
			ends.remove(min);
		}
	}
	
	public void getImpWords(String line)
	{
		Integer wordBeg = 0;
		Integer wordEnd = 0;
		
		String  expWord = null;
		String  lists[] = line.split(" ");
		
		//将多个关联词的起始位置放置在一个表中，供排序
		ArrayList<Integer> begs = new ArrayList<Integer>();
		ArrayList<Integer> ends = new ArrayList<Integer>();
	}
	**/
}
