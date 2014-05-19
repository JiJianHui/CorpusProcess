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
 * wordDict�����������ļ�����Ҫ������������ʱ�
 * 
 * 1:��ȡ����Щ������(��ʽ������)����һ��ָʾ�˾���ϵ����Щ�����ʳ����˵���û��ָʾ����ϵ��
 *   ������Ҫ��ȡָʾ����ϵ����ʽ�����ʴʱ�Ȼ���������в��ҳ��ֵ�ͬ���ĵ���û�б�עָʾ����ϵ�Ĺ�����
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Aug 25, 2013
 */
public class WordDict
{
	/**�����ַ�**/
	private char fillChar;	
	
	/**ԭʼ����*/
	private Vector<String> rawFiles;
	
	/**ָʾ�˾���ϵ�ĵ�����ʽ�����ʴʱ�*/
	private HashMap<String, Integer> singleExpWords;
	/**ָʾ�˾���ϵ�Ĳ�����ʽ�����ʴʱ�*/
	private HashMap<String, Integer> parallelExpWords;

	
	/**��Щ���ֵĿ���ָʾ����ϵ�ĵ��ǲ�û��ָʾ����ϵ�ĵ���������*/
	private HashMap<String, Integer> unSingleWords;	
	/**��Щ���ֵĿ���ָʾ����ϵ�ĵ��ǲ�û��ָʾ����ϵ�Ĳ��й�����*/
	private HashMap<String, Integer> unParallelWords;
	
	/**��Ҫ��������ʹ��*/
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
			//������ʽ�����ʴʱ�
			this.loadExpWords(Toolkit.expWordFile);
			this.sortSingleExpWords();
			this.saveResult();
			
			//����ԭʼ�����ļ��б�
			Toolkit.getFiles(Toolkit.corpusData, this.rawFiles, ".txt");
			//Toolkit.getFiles(Toolkit.testData, this.rawFiles, ".txt");
			 	
			//���ÿ���ļ����д���,���յĽ��������������ʱ���
			for( String rawFile: this.rawFiles )
			{
				System.out.println(rawFile);
				this.parseRawFile(rawFile);
			}
			
			//�Խ������ƽ���������õ�ƽ�������� (M + Ka) / (N + Ka)
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/** ���س���Ĺ����ʴʱ��ļ�*/
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
	 * ���ԭʼ�����ļ����д���ͳ����Щ�ط������˹����ʣ�
	 * �Լ��ù�����ָʾ����ϵ�������ָʾ����ϵ�Ĺ���������Ѿ�֪����
	 * ֻʣ����Щ��ָʾ����ϵ�Ĺ����ʵ����δ֪��
	 * @param rawFile
	 * @throws IOException 
	 */
	private void parseRawFile(String rawFile) throws IOException
	{
		//ԭʼ�����ı�
		String text   = Toolkit.readFileToString(rawFile).replace("\r\n", "\n");
		char[] chars  = text.toCharArray();
		
		//��Ӧ�ı�ע���
		String p1File = rawFile.replace(".txt", ".p1");
		String p2File = rawFile.replace(".txt", ".p2");
		String p3File = rawFile.replace(".txt", ".p3");
		
		//����ע������漰���Ĺ�����ȫ����������ͳ����Щ��ָʾ����ϵ�ġ������ʡ�
		this.parseAnnotationFile(p1File, chars);
		this.parseAnnotationFile(p2File, chars);
		this.parseAnnotationFile(p3File, chars);
		
		//ͳ��ʣ�µ����������г��ֵġ������ʡ���������Ҫ�����ǲ��й�������ô����
		String remainText = String.valueOf(chars);
		this.parseRemainedText(chars, remainText);
		
	}
	
	/**
	 * �����ע���ļ�,
	 * ����ע������漰���Ĺ�����ȫ��������ͳ����Щ��ָʾ����ϵ�ġ������ʡ�
	 * 
	 * @param fName����ע����ļ���
	 * @param text:ԭʼ���ϵ�����
	 * @throws IOException 
	 */
	private void parseAnnotationFile(String fName, char[] chars) throws IOException
	{
		int expNum  = 0;	            //��ʽ����ʽ��������Ŀ
		int wordBeg = 0, wordEnd = 0;	//�������������е�λ��
		int lineBeg = 0, lineEnd = 0;	//�þ��������е�ʵ��λ��
		
		String   relID = null;
		
		String   line  = null;			//��ע��
		String[] lists = null;			//��ע�е������ʾ
		
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
			
			//��ȡ�ñ�ע���е���ʽ������λ��
			for(int i = 0; i < expNum; i++)
			{
				wordBeg = Integer.valueOf( lists[6 + i*2] );
				wordEnd = Integer.valueOf( lists[7 + i*2] );
				
				//ȥ�������λ�õĹ�����
				if(wordBeg > wordEnd) continue;
				if(wordBeg < lineBeg || wordBeg > lineEnd) continue;
				if(wordEnd < lineBeg || wordEnd > lineEnd) continue;
				
				//��ԭʼ�����н���Ӧλ�õĵ���ת��Ϊ�ո�...Ȼ����ͳһɾ��
				for(int index = wordBeg; index < wordEnd; index++)
				{
					chars[index] = this.fillChar;
				}
			}
		}
		
		bf.close();
	}
	
	/**����ʣ�µ��������ݣ������ҳ�û��ָʾ����ϵ�Ĺ�����*/
	private void parseRemainedText(char[] chars, String text)
	{
		String line = null;
		
		//��һƪ������ʣ�µĲ���ʶ�����������
		for(int index = 0; index < chars.length; )
		{
			boolean end = false;

			//iѭ����ʾ���Ǵ�indexλ�ÿ�ʼȥʶ��һ������
			int i = 0;
			for(i = index; i < chars.length; i++)
			{
				char temp = chars[i];
			
				//�жϸ��ַ��Ƿ��Ǿ��ӱ߽�
				end = Toolkit.isSentenceBoundary(temp);
				
				//������ӱ߽�ʹ�iѭ������
				if(end == true) break;
			}
			
			//�����һ�����ӵ�ʶ�𣬾��ӷָ��λ�þ���i�������λ��
			//��ʱ����indexλ�ã���iλ�þ���һ�������ľ���
			line  = text.substring(index, i);
			index = i + 1; //������һ�����ӿ�ʼ��λ�á�  
			
			this.parseRemainedLine(line);
		}
	}
	
	/**����һƪ������ʣ�������е�һ�о��ӣ��ж��Ƿ����û��ָʾ����ϵ�Ĺ�����*/
	private void parseRemainedLine(String line)
	{
		int index = 0;
		int wNum  = 0;
		
		String   wName = null;
		String[] lists = null;
		
		//�������й����ʴʱ����ж��Ƿ���ڲ��й�����
		for( Map.Entry<String, Integer> item:this.parallelExpWords.entrySet() )
		{
			wName = item.getKey();
			lists = wName.split(Toolkit.parallelWordSplitor);
			
			boolean  match = Toolkit.containsParallelWord(line, wName);
	
			//������ڲ��й����ʣ����ȱ��棬��δӸ�����ɾ����Ӧ�Ĺ�����
			if( match )
			{
				this.addWords(wName, 1, this.unParallelWords);
				
				for( index = 0; index < lists.length; index++ )	
					line.replaceFirst(lists[index], "_");	
			}
		}
		
		//�ж϶�Ӧ��single�������Ƿ�����ڸ�����
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
	 * �����ֵ��ǲ�û��ָʾ����ϵ�Ĺ����ʱ�������
	 * @param fName
	 * @param single: true����ʾ���������ʣ�false����ʾ���й�����
	 */
	private void saveWordsToFile(String fName, boolean single)
	{
		String  wName = null;
		Integer wNum  = null;	//ָʾ����ϵ����Ŀ
		Integer unNum = null;	//��ͨ�ʵ���Ŀ
		
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
