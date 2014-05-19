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
 * ��Ҫ�Ĺ�����
 * @author rainbow
 * @time   Apr 21, 2013
 */
public class Toolkit
{
	/*��ע���ϵ����ݼ���*/
	public static String corpusData = "F:\\Corpus Data\\check_pub_guo_only";
	public static String testData   = "F:\\Corpus Data\\Test Data";

	public static String p1WordFile = "F:\\Result\\p1Word.txt";
	public static String p2WordFile = "F:\\Result\\p2Word.txt";
	public static String p3WordFile = "F:\\Result\\p3Word.txt";
	
	//��ʽ�����ʺ���ʽ�����ʴ��λ��
	public static String expWordFile = "F:\\Result\\expWord.txt";
	public static String impWordFile = "F:\\Result\\impWord.txt";
	
	/**���й����ʵķָ���*/
	public static String parallelWordSplitor     = "...";
	
	/**�з־��ӵķָ���*/
	public static char[] sentenceSeparator = {'��', '��', '��', '?', '!', '\n'};
	
	public static int EPS = 2;
	
	public static String relSeperator = "--------------------------------------------------";
	
	
	/**�ɰ汾�Ĺ�ϵ���**/
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
	
	/**�ɰ汾�Ĺ�ϵ����**/
	public static String[] relName = 
	{
		"�н�",
		"ʱ��","ͬ��","�첽","����","����",
		
		"���","ֱ�������˵�������","ԭ������","�������","�����������������",
		"֤������","��������","Ŀ��","Ŀ������","Ŀ���ں�",
		
		"����","ֱ������","��Ҫ����","��Ҫ����","��Ҫ�ں�","�������","�������","����ں�",
		"��������","��������","�����ں�","��ʽ���������裩","��������","�����ں�",
	         
	    "�Ƚ�","ֱ�ӶԱ�","ͬ��Ա�","����Ա�","��ӶԱȣ�ת�ۣ�","�ò�","�ò�����","�ò��ں�",
	         
	    "��չ","ϸ��","����˵��","ʵ��","ʵ������","ʵ���ں�","����","��������",
	    "�����ں�","����","�ݽ�",
	    
	    "����","����","ѡ��","����ѡ��","����ѡ��"
	};
	
	/**�ɰ汾��ϵ��Ŷ�Ӧ���°汾��ϵ��ţ���relNOһһ��Ӧ�޸�**/
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
	
	/**��ȡ�µĹ�ϵ����£��ù�ϵ������λ��**/
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
		"ʱ��", 
		"ͬ��",
		"�첽", "����", "����",
		
		"���", 
		"ֱ�����", "ԭ������", "�������", 
		"������", "֤������", "��������", 
		"Ŀ��", "Ŀ������", "Ŀ���ں�",
		"ֱ������", "��Ҫ����", "��Ҫ��������", "��Ҫ�����ں�", "�������", "�����������", "��������ں�", 
		"��ʽ����", "�������", "�����������", "��������ں�", "�����ƶ�", "�����ƶ�����", "�����ƶ��ں�", 
		"��������", "������������", "���������ں�",
		
		"�Ƚ�", 
		"ֱ�ӶԱ�", "����Ա�", "����Ա�", 
		"��ӶԱ�", 
		"�ò�", "�ò�����", "�ò��ں�", 
		"��ʽ�ò�", 
		
		"��չ", 
		"�н�", 
		"�ݽ�", 
		"ϸ��", "����˵��", "ʵ��", "ʵ������", "ʵ���ں�", "����", "��������", "�����ں�", 
		"����", 
		"ƽ��", 
		"ѡ��", "����ѡ��", "����ѡ��", "ȷ��ѡ��", 
		"�б�"
	};
	
	//�������ļ���ȡΪ�ַ���
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
	 * ��ȡָ���ļ����µ������ļ������������ӵ�files�б��С�
	 * @param dir����ע�ļ�·��
	 * @param files: ��Ҫ���µ��ļ��б�����ԭ�еĻ������޸ġ������ݹ����������.
	 * @param ending:ָ�����ļ���׺������ָ����������Ϊnull,��������ȡ���е��ļ���
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
	
	/**��ȡ��ϵ�ı�ţ��������1-1ʱ�����ȹ�ϵ**/
	public static String getRelID(String relName)
	{
		int    index = 0;
		String relID = null;
		
		relName = relName.trim();
		index   = relName.lastIndexOf('-') + 1; //��Ϊ0�н�ʱ,����Ϊ0
		
		relID   = relName.substring(0, index+1);
		
		return relID;
	}
	/**��һ����ע�������ȡ����ϵ���**/
	public static String getRelIDInLine(String line)
	{
		String relID = null;
		String[] lists = line.split(" ");
		
		if(lists.length < 6) return null;
		
		relID   = Toolkit.getRelID( lists[lists.length - 1] );
		
		return relID;
	}
	/**
	 * ��ȡһ�б�ע�ı��Ŀ�ʼλ��
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
	 * ��ȡһ�б�ע�ı��Ľ���λ��
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
	 * ��һ����ע�����ȡ��ʽ������
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
//		//��ȡ�ñ�ע���е���ʽ������
//		for(int i = 0; i < expNum; i++)
//		{
//			wordBeg = Integer.valueOf( lists[6 + i*2] );
//			wordEnd = Integer.valueOf( lists[7 + i*2] );
//			
//			//ȥ�������λ�õĹ�����
//			if(wordBeg > wordEnd) continue;
//			if(wordBeg < beg1 || wordBeg > end2) continue;
//			if(wordEnd < beg1 || wordEnd > end2) continue;
//		}
//	}
//	
	
	
	public static String formatWord(String word)
	{
		word = word.trim();
		
	    String[] errChar = {",", ":", ";", "��", "��", "��","��", "-","��",
	               "��", "��", "��", "��", "��","��", "��", "��", "\t", "\n", "����","��"};
	    
	    for(int index = 0; index < errChar.length; index++)
	    {
	    	word = word.replace( errChar[index], "");
	    }
	    
		if( word.equals("��ϸ��˵f") ) 
			word = "��ϸ��˵";
		if( word.equals("�������v") )
			word = "�������";
		
		word = word.trim();
		
		if(word.length() > 10) word = "";
		
		return word;
	}
	
	/**
	 * ��һ���ַ��������÷ָ����ϲ�Ϊһ���ַ�����
	 * @param lists����Ҫ�ϲ����ַ�������
	 * @param seperator���ָ���
	 * @return:�ϲ��õ��ַ���
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
	
	
	/**ͳ��һ�������г����˶��ٴε�single����*/
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
	
	/**�ж�һ���ַ��Ƿ���һ�����ӱ߽�ָ���*/
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
	
	/**�ж�һ���������Ƿ���ڲ��й�����**/
	public static boolean containsParallelWord(String line, String wName)
	{
		int      beg    = 0;
		int      end    = 0;
		int      index  = 0;
		
		boolean  match  = false;
		String[] lists  = line.split(Toolkit.parallelWordSplitor);
		
		//�жϸþ����Ƿ���ڸò��й�����
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
	 * �����ļ�����Ҫ������������ļ�����
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
            // �½��ļ����������������л���
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // �½��ļ���������������л���
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // ��������
            byte[] b = new byte[1024 * 5];
            int len;
            
            while ((len = inBuff.read(b)) != -1) 
            {
                outBuff.write(b, 0, len);
            }
            // ˢ�´˻���������
            outBuff.flush();
        } 
        finally 
        {
            // �ر���
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
    
    /**
     * ��һ���ļ����ɰ��д洢��vector
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
     * ���ɹ�ϵ���ת��Ϊ�¹�ϵ���
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
     * ����implicit word�ĸ�ʽ˵��
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
    	
    	//�ϲ���ͬ��
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
     * �����ж��word��span����������Ĺ��˺�����
     * spanBeg...spanEnd; spanBeg...spanEnd...
     * @param spans
     */
    public static String formatExpWordSpan(String spans)
    {
    	//��Ҫɾ���ĸ�������Ҫ��Ϊ������expNum;
    	int error = 0;
    	
    	String[] lists = spans.split(";");
    	
    	String correctSpan = "";
    	
    	if( lists.length == 1 ) return spans; 
    	
    	int i = 0, j = 0;
    	
    	//ɾ��Ƕ�׵�span, ѡ����������span��Ϊ��ȷ��span����ȵ�spanҲ��Ƕ��һ��
    	for(i = 0; i < lists.length; i++)
    	{
    		boolean find   = false;
    		String  result = null;
    		
    		for(j = i + 1; j < lists.length; j++)
    		{
    			result = Toolkit.emergeSpan( lists[i], lists[j] );
    			
    			//�����Ƕ��
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
    	
    	//������ȷ��˳������span
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
     * �ϲ�����span���ж�spanA��spanB�Ƿ�Ƕ�ף����Ƕ�ף����غϲ������ȷ��span
     * ����span֮����������ʽ�Ĺ�ϵ������ȫ�����ʱ����null�����෵����ȷ�ϲ�span
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
    	
    	//No1: ��ȫ��Ƕ��
    	if(aBeg > bEnd + EPS || aEnd < bBeg - EPS) return result;
    	
    	//No2: Ƕ��������޸ı߽磬��ȡ��С�߽�����߽�,��������ȵ����
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
	 * ����ɰ汾��ע��ʽ�����һ���ļ��еĹ�ϵ��Ŀ,�������нӹ�ϵ
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
        // ���ָ���
        int chCnt = 0;  
        
        String regEx = "[\\u4e00-\\u9fa5]"; // ������Ƿ����֣�u9fa5-->u9fff
        
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
