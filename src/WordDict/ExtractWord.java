package WordDict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import util.Toolkit;

/**
 * ���ļ���Ҫ����ǰһ���ֹ����Ļ��ϴ�p1,p2��p3�ļ�����ȡ����Ӧ����ʽ�����ʺ���ʽ�����ʴʱ?
 * ��Ҫ��ȡ����Ϣ������������ƺ͹����ʳ��ֵĴ���
 * 
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Aug 25, 2013
 */
public class ExtractWord
{
	private HashMap<String, Integer> expWords;
	private HashMap<String, Integer> impWords;

	private boolean debug;
	
	public ExtractWord()
	{
		this.expWords = new HashMap<String, Integer>();
		this.impWords = new HashMap<String, Integer>();
		this.debug    = true; 
	}
	
	/**
	 * ���ļ�fName����ȡ��ʽ����ʽ�����ʺͳ��ֵĴ���.
	 * �ú������ڵ���ʽ�ĺ���ÿ�����иú���������µ��ļ����������е���ݼ���
	 * @param fName
	 */
	public void run(String fName)
	{
		String   line    = null;
		String[] lists   = null;
		
		String   wName   = null;	//���������
		
		Integer  expNum  = null;	//��������Ϊ��ʽ�����ʳ��ֵĴ���
		Integer  impNum  = null;
		
		if( this.debug ) System.out.println("Processing: " + fName);
		
		try 
		{
			BufferedReader br = new BufferedReader( new FileReader(fName) );
			
			//�ļ��е�ÿһ�д����һ��������
			while( ( line = br.readLine() ) != null)
			{
				line  = line.trim();
				lists = line.split("\t");
				
				wName = lists[0];
				
				expNum = Integer.valueOf(lists[2]);
				impNum = Integer.valueOf(lists[3]);
				
				
				//�����ж��Ƿ��Ѿ����ڸù����ʵļ�¼
				if( this.expWords.containsKey(wName) )
				{
					expNum += this.expWords.get(wName);
					this.expWords.remove(wName);
				}
				
				if( this.impWords.containsKey(wName) )
				{
					impNum += this.impWords.get(wName);
					this.impWords.remove(wName);
				}
				
				if(expNum > 0 ) this.expWords.put(wName, expNum);
				if(impNum > 0 ) this.impWords.put(wName, impNum);
			}
			
			br.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * ����ʽ�����ʱ�д�뵽�ļ��У�Ϊ����ĳ������ṩ���
	 */
	public void writeExpWordsToFile(String fName)
	{
		String  wName = null;
		Integer wNum  = null;
		
		if( this.debug ) System.out.println("Writing to: " + fName);
		
		try 
		{
			FileWriter fw = new FileWriter( new File(fName) );
			
			for(Map.Entry<String, Integer> item:this.expWords.entrySet())
			{
				wName = item.getKey();
				wNum  = item.getValue();
				
				fw.write(wName + "\t" + wNum.toString() + "\r\n");
			}
			
			fw.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void writeImpWordsToFile(String fName)
	{
		String  wName = null;
		Integer wNum  = null;
		
		if( this.debug ) System.out.println("Writing to: " + fName);
		
		try 
		{
			FileWriter fw = new FileWriter( new File(fName) );
			
			for(Map.Entry<String, Integer> item:this.impWords.entrySet())
			{
				wName = item.getKey();
				wNum  = item.getValue();
				
				fw.write(wName + "\t" + wNum.toString() + "\r\n");
			}
			
			fw.close();
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args)
	{
		String expWordFile = "F:\\Result\\expWord.txt";
		String impWordFile = "F:\\Result\\impWord.txt";
		
		ExtractWord extract = new ExtractWord();
		
		extract.run(Toolkit.p1WordFile);
		extract.run(Toolkit.p2WordFile);
		extract.run(Toolkit.p3WordFile);
		
		extract.writeExpWordsToFile(expWordFile);
		extract.writeImpWordsToFile(impWordFile);
	}
}
