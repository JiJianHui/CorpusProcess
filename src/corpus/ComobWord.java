package corpus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计并列关联词出现的规律
 * @author rainbow
 * @time   May 6, 2013
 */
public class ComobWord
{
	private String p1WordFile;
	private String p2WordFile;
	private String p3WordFile;
	
	private HashMap<String, Integer> combWord;	//并列关联词
	private HashMap<String, Integer> oneWord;	//只含有一个词的关联词
	
	private HashMap<String, Integer> singWord;	//单独关联词(可以作为并列关联词一部分)
	private HashMap<String, Integer> soloWord;	//独自关联词，不作为并列关联词的一种
	
	public ComobWord()
	{
		this.p1WordFile = "F:\\Result\\p1Word.txt";
		this.p2WordFile = "F:\\Result\\p2Word.txt";
		this.p3WordFile = "F:\\Result\\p3Word.txt";
		
		this.combWord = new HashMap<String, Integer>();
		this.oneWord  = new HashMap<String, Integer>();
		
		this.singWord = new HashMap<String, Integer>();
		this.soloWord = new HashMap<String, Integer>();
	}
	
	public void run()
	{
		int number   = 0;
		int combNum  = 0, singNum = 0, soloNum  = 0;
		int combKind = 0, singKind = 0, soloKind = 0;
		
		try
		{
			this.countWord(this.p1WordFile);
			this.countWord(this.p2WordFile);
			this.countWord(this.p3WordFile);
			
			this.classWord();
			
			this.saveResult(this.combWord, "F:\\Result\\combWord.txt");
			this.saveResult(this.singWord, "F:\\Result\\singWord.txt");
			this.saveResult(this.soloWord, "F:\\Result\\soloWord.txt");
			
			//输出信息
			for( Map.Entry<String, Integer> item: this.combWord.entrySet() )
			{
				number = item.getValue().intValue();
				
				if(number > 0)
					combKind = combKind + 1;
				
				combNum  = combNum  + number; 
			}
			for( Map.Entry<String, Integer> item: this.singWord.entrySet() )
			{
				number = item.getValue().intValue();
				
				if(number > 0)
					singKind  = singKind + 1;
				
				singNum   = singNum  + number;
			}
			for( Map.Entry<String, Integer> item: this.soloWord.entrySet() )
			{
				number = item.getValue().intValue();
				
				if(number > 0)
					soloKind = soloKind + 1;
				
				soloNum  = soloNum  + number;
			}
			
			System.out.println("ComobWord:");
			System.out.println("\tKind: " + combKind + " Num: " + combNum);
			
			System.out.println("SingWord:");
			System.out.println("\tKind: " + singKind + " Num: " + singNum);
			
			System.out.println("SoloWord");
			System.out.println("\tKind: " + soloKind + " Num: " + soloNum);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 对Corpus产生的中间文件进行处理，将对应的关联词分开到两个HashMap中。
	 * @param fPath
	 * @throws Exception
	 */
	public void countWord(String fPath) throws Exception
	{
		
		String line = null;
		String[] lists = null;

		int num = 0;
		String wName = null;
		
		BufferedReader br = new BufferedReader(new FileReader(fPath));
		
		while( (line = br.readLine()) != null )
		{
			line  = line.trim();
			lists = line.split("\t");
			
			wName = lists[0];
			
			//修改这个地方产生显式关联词和隐式关联词的区别
			//num   = Integer.valueOf(lists[1]).intValue();	//总数
			//num   = Integer.valueOf(lists[2]).intValue();	//显式
			num   = Integer.valueOf(lists[3]).intValue();	//隐式
			
			if(num == 0) continue;
			
			if( wName.indexOf("...")!= -1)
			{
				if(this.combWord.containsKey(wName))
				{
					num = num + this.combWord.get(wName).intValue();
				}
				this.combWord.put(wName, num);
			}
			else
			{
				if(this.oneWord.containsKey(wName))
				{
					num = num + this.oneWord.get(wName).intValue();
				}
				this.oneWord.put(wName, num);
			}
		}
		
		br.close();
	}
	/**
	 * 对oneWord中的关联词进行分类，将它们分割为：soloWord和singleWord
	 */
	public void classWord()
	{
		String  wName = null;
		String  cName = null;
		Integer wNum  = null;
		
		boolean  flag  = false;
		String[] lists = null;
		
		for(Map.Entry<String, Integer> item:this.oneWord.entrySet())
		{
			flag  = false;
			wName = item.getKey();
			wNum  = item.getValue();
			
			for(Map.Entry<String, Integer> cWord:this.combWord.entrySet())
			{
				cName = cWord.getKey();
				lists = cName.split("...");
				
				for(int index = 0; index < lists.length; index++)
				{
					if( lists[index].equals(wName) ) flag = true;
				}
				if(flag) break;
			}
			
			if(flag) 
				this.singWord.put(wName, wNum);
			else    
				this.soloWord.put(wName, wNum);
		}
	}
	
	/**
	 * 保存结果
	 * @param word
	 * @param fName
	 * @throws IOException
	 */
	public void saveResult(HashMap<String, Integer> word, String fName)
				throws IOException
	{
		String  wName = null;
		Integer wNum  = null;
		
		File outFile  = new File(fName);
		FileWriter fw = new FileWriter(outFile);
		
		for(Map.Entry<String, Integer> item: word.entrySet())
		{
			wName = item.getKey();
			wNum  = item.getValue();
			wName = wName + "\t" + wNum.toString() + "\r\n";
			
			fw.write(wName);
		}
		
		fw.close();
	}
	
	public static void main(String[] args)
	{
		ComobWord coWord = new ComobWord();
		
		coWord.run();
	}
}
