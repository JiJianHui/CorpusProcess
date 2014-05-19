package util;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


/**
 * 主要的工具类
 * @author rainbow
 * @time   Apr 21, 2013
 */
public class Toolkit
{
	/*标注语料的数据集合*/
	public static String corpusData = "F:\\Corpus Data\\check_pub_guo_only";
	public static String testData   = "F:\\Corpus Data\\Test Data";

	public static String p1WordFile = "F:\\Result\\p1Word.txt";
	public static String p2WordFile = "F:\\Result\\p2Word.txt";
	public static String p3WordFile = "F:\\Result\\p3Word.txt";
	
	//显式关联词和隐式关联词存放位置
	public static String expWordFile = "F:\\Result\\expWord.txt";
	public static String impWordFile = "F:\\Result\\impWord.txt";
	
	/**并列关联词的分隔符*/
	public static String parallelWordSplitor     = "...";
	
	/**切分句子的分隔符*/
	public static char[] sentenceSeparator = {'。', '？', '！', '?', '!', '\n'};
	
	public static int EPS = 2;
	
	public static String relSeperator = "--------------------------------------------------";
	
	
	/**旧版本的关系编号**/
	public static String[] relNO = 
	{
		"0",
		"1","1-1","1-2","1-2-1","1-2-2",
		
		"2","2-1","2-1-1","2-1-2","2-2","2-2-1","2-2-2","2-3","2-3-1","2-3-2",
		
		"3","3-1","3-1-1","3-1-1-1","3-1-1-2","3-1-2","3-1-2-1","3-1-2-2",
		"3-1-3","3-1-3-1","3-1-3-2","3-2","3-2-1","3-2-2",
	         
	    "4","4-1","4-1-1","4-1-2","4-2","4-3","4-3-1","4-3-2",
	         
	    "5","5-1","5-1-1","5-1-2","5-1-2-1","5-1-2-2","5-1-3","5-1-3-1",
	    "5-1-3-2","5-2","5-3",
	    
	    "6","6-1","6-2","6-2-1","6-2-2"
	  };
	
	/**旧版本的关系名称**/
	public static String[] relName = 
	{
		"承接",
		"时序","同步","异步","先序","后序",
		
		"因果","直接因果（说明因果）","原因在先","结果在先","间接因果（推论因果）",
		"证据在先","推论在先","目的","目的在先","目的在后",
		
		"条件","直接条件","必要条件","必要在先","必要在后","充分条件","充分在先","充分在后",
		"任意条件","任意在先","任意在后","形式条件（假设）","假设在先","假设在后",
	         
	    "比较","直接对比","同向对比","反向对比","间接对比（转折）","让步","让步在先","让步在后",
	         
	    "扩展","细化","解释说明","实例","实例在先","实例在后","例外","例外在先",
	    "例外在后","泛化","递进",
	    
	    "并列","并行","选择","相容选择","互斥选择"
	};
	
	/**旧版本关系编号对应的新版本关系编号，与relNO一一对应修改**/
	public static String[] relNO_oldTonew = 
	{
		"4-1", 
		
		"1", "1-1", "1-2", "1-2-1", "1-2-2", 
		
		"2", "2-1", "2-1-1", "2-1-2", "2-2", "2-2-1", "2-2-2", "2-3", "2-3-1", "2-3-2", 
		
		"2-4", "2-4", "2-4-1", "2-4-1-1", "2-4-1-2", "2-4-2", "2-4-2-1", "2-4-2-2",
		"2-6", "2-6-1", "2-6-2", "2-5-1", "2-5-1-1", "2-5-1-2", 
		
		"3", "3-1", "3-1-1", "3-1-2", "3-2", "3-3", "3-3-1", "3-3-2", 
		
		"4", "4-3", "4-3-1", "4-3-2", "4-3-2-1", "4-3-2-2", "4-3-3", "4-3-3-1", 
		"4-3-3-2", "4-4", "4-2",
		
		"4-5", "4-5", "4-6", "4-6-1", "4-6-2"
	};
	
	public static String[] newRelNO = 
	{
		"1",
		"1-1",
		"1-2", "1-2-1", "1-2-2", 

		"2", 
		"2-1", "2-1-1", "2-1-2", 
		"2-2", "2-2-1", "2-2-2", 
		"2-3", "2-3-1", "2-3-2", 
		"2-4", "2-4-1", "2-4-1-1", "2-4-1-2", "2-4-2", "2-4-2-1", "2-4-2-2", 
		"2-5", "2-5-1", "2-5-1-1", "2-5-1-2", "2-5-2", "2-5-2-1", "2-5-2-2", 
		"2-6", "2-6-1", "2-6-2", 

		"3", 
		"3-1", "3-1-1", "3-1-2", 
		"3-2", 
		"3-3", "3-3-1", "3-3-2", 
		"3-4", 

		"4", 
		"4-1", 
		"4-2", 
		"4-3", "4-3-1", "4-3-2", "4-3-2-1", "4-3-2-2", "4-3-3", "4-3-3-1", "4-3-3-2", 
		"4-4",
		"4-5", 
		"4-6", "4-6-1", "4-6-2", "4-6-3", 
		"4-7"
	};
	
	/**获取新的关系编号下，该关系的索引位置**/
	public static int getRelIDIndex(String relID)
	{
		int     index = -1;
		boolean find  = false;
		
		for(index = 0; index < Toolkit.newRelNO.length; index++)
		{
			if(Toolkit.newRelNO[index].equalsIgnoreCase(relID))
			{
				find = true;
				break;
			}
		}
		
		if( find )
		{
			return index;
		}
		
		return -1;
	}
	
	public static String[] newRelName = 
	{
		"时序", 
		"同步",
		"异步", "先序", "后序",
		
		"因果", 
		"直接因果", "原因在先", "结果在先", 
		"间接因果", "证据在先", "推论在先", 
		"目的", "目的在先", "目的在后",
		"直接条件", "必要条件", "必要条件在先", "必要条件在后", "充分条件", "充分条件在先", "充分条件在后", 
		"形式条件", "相关条件", "相关条件在先", "相关条件在后", "隐含推断", "隐含推断在先", "隐含推断在后", 
		"任意条件", "任意条件在先", "任意条件在后",
		
		"比较", 
		"直接对比", "正向对比", "反向对比", 
		"间接对比", 
		"让步", "让步在先", "让步在后", 
		"形式让步", 
		
		"扩展", 
		"承接", 
		"递进", 
		"细化", "解释说明", "实例", "实例在先", "实例在后", "例外", "例外在先", "例外在后", 
		"泛化", 
		"平行", 
		"选择", "相容选择", "互斥选择", "确定选择", 
		"列表"
	};
	
	//将整个文件读取为字符串
    public static String readFileToString(String fileName) 
    {  
        File file       = new File(fileName);  
        Long filelength = file.length();  
        
        byte[] filecontent = new byte[filelength.intValue()];  
        
        try 
        {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } 
        catch (FileNotFoundException e)
        {  
            e.printStackTrace();  
        } 
        catch (IOException e) 
        {  
            e.printStackTrace();  
        }  
        return new String(filecontent);  
    }  
    
	/**
	 * 获取指定文件夹下的所有文件，并将结果添加到files列表中。
	 * @param dir：标注文件路径
	 * @param files: 需要更新的文件列表，是在原有的基础上修改。这样递归就起作用啦.
	 * @param ending:指定的文件后缀。必须指定，若设置为null,则用来获取所有的文件。
	 * @return
	 */
	public static void getFiles(String dir, Vector<String> files, String ending)
	{
		File   temp   = null;
		File   parent = new File(dir);
		File[] lists  = parent.listFiles();
		
		for(int i = 0; i < lists.length; i++)
		{
			temp = lists[i];
			
			if( temp.isFile() )
			{
				if( temp.getName().endsWith(ending) )
				{
					files.add( temp.getAbsolutePath() );
				}
			}
			else if( temp.isDirectory() )
			{
				Toolkit.getFiles( temp.getAbsolutePath(), files, ending );
			}
		}
	}
	
	/**获取关系的编号，传入的是1-1时序在先关系**/
	public static String getRelID(String relName)
	{
		int    index = 0;
		String relID = null;
		
		relName = relName.trim();
		index   = relName.lastIndexOf('-') + 1; //当为0承接时,正好为0
		
		relID   = relName.substring(0, index+1);
		
		return relID;
	}
	/**从一个标注行里面获取到关系编号**/
	public static String getRelIDInLine(String line)
	{
		String relID = null;
		String[] lists = line.split(" ");
		
		if(lists.length < 6) return null;
		
		relID   = Toolkit.getRelID( lists[lists.length - 1] );
		
		return relID;
	}
	/**
	 * 获取一行标注文本的开始位置
	 * @param line
	 * @return
	 */
	public static int getLineBeg(String line)
	{
		String[] lists = line.split(" ");
		
		int beg1 = Integer.valueOf(lists[0]);
		int beg2 = Integer.valueOf(lists[2]);
		
		if(beg1 < beg2) 
			return beg1;
		else 
			return beg2;
	}
	/**
	 * 获取一行标注文本的结束位置
	 * @param line
	 * @return
	 */
	public static int getLineEnd(String line)
	{
		String[] lists = line.split(" ");
		
		int end1 = Integer.valueOf(lists[1]);
		int end2 = Integer.valueOf(lists[3]);
		
		if(end1 < end2) 
			return end2;
		else 
			return end1;		
	}
	
	/**
	 * 从一个标注行里获取显式关联词
	 */
	public static String getWordFromFile(int beg, int end, String fileName)
	{
		String word = null;
		String text = null; 
		
		text = Toolkit.readFileToString(fileName + ".txt" );
		text = text.replace("\r\n", "\n");	
		
		word = text.substring(beg, end);
		word = Toolkit.formatWord(word);
		
		return word;
	}
	
	public static String getWordInLine(String rawText, String line)
	{
		String word = null;
		
		
		
		return word;
	}
	
//	public static void getExpWords(String line, String file)
//	{
//		String text = null; 
//		text = this.readFileToString(file + ".txt" );
//		text = text.replace("\r\n", "\n");
//		
//		int expNum  = 0;
//		int wordBeg = 0;
//		int wordEnd = 0;
//		
//		String[] lists = line.split(" ");
//		
//		int beg1 = Integer.valueOf(lists[0]).intValue();
//		int end1 = Integer.valueOf(lists[1]).intValue();
//		
//		int beg2 = Integer.valueOf(lists[2]).intValue();
//		int end2 = Integer.valueOf(lists[3]).intValue();
//		
//		if(beg1 > beg2)
//		{
//			int temp  = beg1;
//			beg1 = beg2;
//			beg2 = temp;
//			
//			temp = end1;
//			end1 = end2;
//			end2 = temp;
//		}
//		
//		//获取该标注行中的显式关联词
//		for(int i = 0; i < expNum; i++)
//		{
//			wordBeg = Integer.valueOf( lists[6 + i*2] );
//			wordEnd = Integer.valueOf( lists[7 + i*2] );
//			
//			//去除错误的位置的关联词
//			if(wordBeg > wordEnd) continue;
//			if(wordBeg < beg1 || wordBeg > end2) continue;
//			if(wordEnd < beg1 || wordEnd > end2) continue;
//		}
//	}
//	
	
	
	public static String formatWord(String word)
	{
		word = word.trim();
		
	    String[] errChar = {",", ":", ";", "（", "）", "（","＋", "-","？",
	               "，", "；", "：", "“", "、","。", "。", "’", "\t", "\n", "——","－"};
	    
	    for(int index = 0; index < errChar.length; index++)
	    {
	    	word = word.replace( errChar[index], "");
	    }
	    
		if( word.equals("详细来说f") ) 
			word = "详细来说";
		if( word.equals("具体而言v") )
			word = "具体而言";
		
		word = word.trim();
		
		if(word.length() > 10) word = "";
		
		return word;
	}
	
	/**
	 * 将一个字符数组利用分隔符合并为一个字符串。
	 * @param lists：需要合并的字符串数组
	 * @param seperator：分隔符
	 * @return:合并好的字符串
	 */
	public static String joinString(String[] lists, char seperator)
	{
		String result = "";
		
		for(int index = 0; index < lists.length; index++)
		{
			if(index != 0)
				result = result + seperator + lists[index];
			else
				result = lists[index];
		}
		
		return result;
	}
	
	
	/**统计一行语料中出现了多少次的single词语*/
	public static int countWordsInLine(String line, String wName)
	{
		int num = 0;
		int beg = 0;
		
		for( ; ; )
		{
			beg = line.indexOf(wName);
			
			if(beg == -1 ) break;
			
			num  = num + 1;
			line = line.substring( beg + wName.length() );
		}
		
		return num;
	}
	
	/**判断一个字符是否是一个句子边界分隔符*/
	public static boolean isSentenceBoundary(char ch)
	{
		boolean temp = false;
		
		for(int j = 0; j < Toolkit.sentenceSeparator.length; j++)
		{
			if( ch == Toolkit.sentenceSeparator[j] )
			{
				temp = true;
				break;
			}
		}
		
		return temp;
	}
	
	/**判断一个句子中是否存在并列关联词**/
	public static boolean containsParallelWord(String line, String wName)
	{
		int      beg    = 0;
		int      end    = 0;
		int      index  = 0;
		
		boolean  match  = false;
		String[] lists  = line.split(Toolkit.parallelWordSplitor);
		
		//判断该句子是否存在该并列关联词
		for(index = 0; index < lists.length; index++)
		{
			if( ( end=line.lastIndexOf(lists[index]) ) != -1 )
			{
				if(beg > end)
				{
					match = false;
					break;
				}
				beg = end;
			}
			else
			{
				match = false;
				break;
			}
		}
		
		return match;
	}
	
	/**
	 * 复制文件，需要传入的是两个文件对象
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
    public static void copyFile(File sourceFile, File targetFile) throws IOException 
    {
        BufferedInputStream  inBuff  = null;
        BufferedOutputStream outBuff = null;
        
        try 
        {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            
            while ((len = inBuff.read(b)) != -1) 
            {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } 
        finally 
        {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
    
    /**
     * 将一个文件读成按行存储的vector
     * @param lines
     * @throws IOException 
     */
    public static void readFileToLines(String fPath, Vector<String> lines) throws IOException
    {
    	BufferedReader br = new BufferedReader( new FileReader(fPath) );
    	
    	String line = null;
    	
    	while( ( line = br.readLine() ) != null )
    	{
    		line = line.trim();
    		lines.add(line);
    	}
    	
    	br.close();
    }
    
    /***
     * 将旧关系编号转换为新关系编号
     * @param oldRelID
     * @return
     */
    public static String convertOldRelIDToNew(String oldRelID)
    {
    	String newRelID = null;
    	
    	int index = 0;
    	boolean find    = false;
    	
    	for(index = 0; index < Toolkit.relNO.length; index++)
    	{
    		if(Toolkit.relNO[index].equalsIgnoreCase(oldRelID))
    		{
    			find = true;
    			break;
    		}
    	}
    	
    	if( find )
    	{
    		newRelID = Toolkit.relNO_oldTonew[index];
    	}
    	
    	return newRelID;
    }
    
    /**
     * 修正implicit word的格式说明
     * @param spans
     * @param impWords
     * @return
     */
    public static String[] formatImpWordSpan(String spans, String impWords)
    {
    	String[] result    = new String[2];
    	
    	String correctSpan = "";
    	String correctWord = "";
    	
    	String[] lists = spans.split(";");
    	String[] words = impWords.split(";");
    	
    	//合并相同的
    	int i = 0, j = 0;
    	
    	for(i = 0; i < lists.length; i++)
    	{
    		int tempA = Integer.valueOf(lists[i]);
    		
    		for(j = i + 1; j < lists.length; j++)
    		{
    			//if( lists[i].equalsIgnoreCase(lists[j]) )
    				//lists[i] = "";
    			int tempB = Integer.valueOf(lists[j]);
    			
    			if( Math.abs(tempA - tempB) < Toolkit.EPS )
    				lists[i] = "";
    		}
    	}
    	
    	for(i = 0; i < lists.length; i++)
    	{
    		if( lists[i].length() > 0 )
    		{
    			correctSpan += lists[i] + ";";
    			correctWord += words[i] + ";";
    		}
    	}
    	correctSpan = correctSpan.substring(0, correctSpan.length() - 1);
    	correctWord = correctWord.substring(0, correctWord.length() - 1);
    	
    	result[0] = correctSpan;
    	result[1] = correctWord;
    	
    	return result;
    }
    
    /**
     * 将含有多个word的span进行最基本的过滤和排序
     * spanBeg...spanEnd; spanBeg...spanEnd...
     * @param spans
     */
    public static String formatExpWordSpan(String spans)
    {
    	//需要删除的个数，主要是为了修正expNum;
    	int error = 0;
    	
    	String[] lists = spans.split(";");
    	
    	String correctSpan = "";
    	
    	if( lists.length == 1 ) return spans; 
    	
    	int i = 0, j = 0;
    	
    	//删除嵌套的span, 选择其中最大的span作为正确的span，相等的span也是嵌套一种
    	for(i = 0; i < lists.length; i++)
    	{
    		boolean find   = false;
    		String  result = null;
    		
    		for(j = i + 1; j < lists.length; j++)
    		{
    			result = Toolkit.emergeSpan( lists[i], lists[j] );
    			
    			//如果是嵌套
    			if( result != null ){
    				find     = true;
    				lists[j] = result;
    			}
    		}
    		
    		if( find )	lists[i] = "";
    	}
    	
    	
    	for(i = 0; i < lists.length; i++){
    		if(lists[i].length() > 0){
    			correctSpan += lists[i] + ";";
    		}
    	}
    	
    	//按照正确的顺序排列span
    	correctSpan = correctSpan.substring(0, correctSpan.length() - 1);
    	lists = correctSpan.split(";");

    	for(i = 0; i < lists.length; i++)
    	{
    		int min = i;
    		for(int n = i; n < lists.length; n++)
    		{
    			int big = Toolkit.compareSpan(lists[min], lists[n]);
    			if(big > 0 ) min = n;
    		}
    		String temp = lists[i];
    		lists[i]    = lists[min];
    		lists[min]  = temp;
    	}
    	
    	correctSpan = "";
    	
    	for(i = 0; i < lists.length; i++)
    	{
    		correctSpan += lists[i] + ";";
    	}
    	correctSpan = correctSpan.substring(0, correctSpan.length() - 1);
    	
    	return correctSpan;
    }
    
    /**
     * 合并两个span。判断spanA和spanB是否嵌套，如果嵌套，返回合并后的正确的span
     * 两个span之间有五中形式的关系，当完全不相关时返回null，其余返回正确合并span
     * 
     * @param spanA
     * @param spanB
     * @return
     */
    public static String emergeSpan(String spanA, String spanB)
    {
    	spanA = spanA.replace("...", "#");
    	spanB = spanB.replace("...", "#");
    	
    	String result = null;
    	
    	int beg = 0, end = 0;
    	
    	String[] al = spanA.split("#");
    	String[] bl = spanB.split("#");
    	
    	int aBeg = Integer.valueOf(al[0]);
    	int aEnd = Integer.valueOf(al[1]);
    	
    	int bBeg = Integer.valueOf(bl[0]);
    	int bEnd = Integer.valueOf(bl[1]);
    	
    	//No1: 完全不嵌套
    	if(aBeg > bEnd + EPS || aEnd < bBeg - EPS) return result;
    	
    	//No2: 嵌套情况，修改边界，获取最小边界和最大边界,包括了相等的情况
    	if( aBeg > bBeg ) beg = bBeg;
    	else beg = aBeg;
    	
    	if(aEnd > bEnd) end = aEnd;
    	else end = bEnd;
    	
    	result = String.valueOf(beg) + "..." + String.valueOf(end);
    	
    	return result;
    }
    
    public static int compareSpan(String spanA, String spanB)
    {
    	spanA = spanA.replace("...", "#");
    	spanB = spanB.replace("...", "#");
    	
    	String[] al = spanA.split("#");
    	String[] bl = spanB.split("#");
    	
    	int aBeg = Integer.valueOf(al[0]);
    	int bBeg = Integer.valueOf(bl[0]);
    	
    	return aBeg - bBeg;
    }
    
    
	/**
	 * 计算旧版本标注格式里面的一个文件中的关系数目,不包含承接关系
	 * @param p1Path
	 * @return
	 * @throws IOException
	 */
	public static int countRelNum(String p1Path) throws IOException
	{
		int num = 0;
	
		String line = null;
		BufferedReader br = new BufferedReader(new FileReader(p1Path));
		
		while( (line = br.readLine()) != null )
		{
			String relID = Toolkit.getRelIDInLine(line);
			
			if(relID != null && !relID.equalsIgnoreCase("0") )
			{
				num = num + 1;
			}
		}
		
		return num;
	}
	
    public static int countChineseWordNumInText(String text) {
        // 汉字个数
        int chCnt = 0;  
        
        String regEx = "[\\u4e00-\\u9fa5]"; // 如果考虑繁体字，u9fa5-->u9fff
        
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regEx);  
        java.util.regex.Matcher m = p.matcher(text);  
        
        while (m.find()) { 
                chCnt++; 
        }  
        return chCnt;
    }

    public static Document parseXMLToDOM(String fileName)
    {
        Document domobj = null;
        SAXReader reader = new SAXReader();
        File file = new File(fileName);
        try
        {
            domobj = reader.read(file);
        }
        catch (DocumentException e)
        {
            e.printStackTrace();
        }

        return domobj;
    }
	
}
