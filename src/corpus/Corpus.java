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
 * �����ʷ�������������Ҫ�Ǵӱ�ע��p1,p2,p3�ļ�����ȡ�������ʺͶ�Ӧ�ľ���ϵ��
 * �����Ǵӱ�ע�������ȡ�������ʼ���Ӧ�ľ���ϵ��Ȼ��ͨ����ת�����ʱ������ɾ���ϵ��
 * @author rainbow
 * @time   Apr 21, 2013
 */
public class Corpus
{
	//private Toolkit tool;
	
	private boolean debug;
	
	private int     		 expMoreNum;	    //���2012��python�������ʽ��ϵ�������Ŀ
	private int     		 impMoreNum;	    //���2012��python�������ʽ��ϵ�������Ŀ
	
	private String  		 p1RelFile;		    //p1����ϵ�ļ�·��
	private String  		 p2RelFile;
	private String  		 p3RelFile;
	
	private String  		 p1WordFile;	    //p1�������ļ�·��
	private String  		 p2WordFile;
	private String  		 p3WordFile;
	
	private Vector<Words>     words;			//�����г��ֵ����й����ʵļ���
	private Vector<Relations> relations;		//�����г��ֵ����о���ϵ�ļ���
	
	private Vector<String>   p1Files;		    //������ľ�Ⱥ�ļ���ַ�б�
	private Vector<String>   p2Files;		    //������ĸ����ļ���ַ�б�
	private Vector<String>   p3Files;		    //������ķ־��ļ���ַ�б�
	
	private Vector<Words>     p1Words;		    //p1��ע�ļ��г��ֵĹ�����(��������)
	private Vector<Words>     p2Words;
	private Vector<Words>     p3Words;
	
	private Vector<Relations> p1Relations;	//p1��ע�ļ��г��ֵľ���ϵ(��������)
	private Vector<Relations> p2Relations;
	private Vector<Relations> p3Relations;
	
	private int nearNum;	//λ�����ھ���֮��ľ���ϵ
	private int crosNum;	//λ�ڽ������֮��ľ���ϵ
	
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
	 * ��ĳһָ��·���µı�־�����ӵ���ǰ�ķ�������С�
	 * �����Ҫ�Ƚ�ǰ��ͳ�Ƶ�·�����������ǰ�ڵĽ�����������
	 * @param dir����ע�ļ����ڵ�·�����������ļ���
	 */
	public void run(String dir)
	{
		//���Ȼ�ȡ��ע�ļ�·��
		Toolkit.getFiles(dir, this.p1Files, ".p1");
		Toolkit.getFiles(dir, this.p2Files, ".p2");
		Toolkit.getFiles(dir, this.p3Files, ".p3");
		
		//�Ա�ע���ļ�����ͳ�ƹ������Լ���Ӧ�ľ���ϵ
		countWords(this.p1Files, this.p1Words);
		countWords(this.p2Files, this.p2Words);
		countWords(this.p3Files, this.p3Words);
		
		//���ù����ʱ������ɾ���ϵ��
		this.reverse(this.p1Words, this.p1Relations);
		this.reverse(this.p2Words, this.p2Relations);
		this.reverse(this.p3Words, this.p3Relations);

		//���ֱ�Ϊp1��p2��p3��ͳ�ƽ���ϲ�
		this.mergeAllWords();
		this.mergeAllRels();
		
		
		//������ϵ��д�뵽�ļ���
		this.writeRelationsToFile(this.p1Relations, this.p1RelFile);
		this.writeRelationsToFile(this.p2Relations, this.p2RelFile);
		this.writeRelationsToFile(this.p3Relations, this.p3RelFile);
		
		//�������ʱ�д�뵽�ļ���
		this.writeWordToFile(this.p1Words, this.p1WordFile);
		this.writeWordToFile(this.p2Words, this.p2WordFile);
		this.writeWordToFile(this.p3Words, this.p3WordFile);
		
		//System.out.println("Exp Increase:" + this.expMoreNum);
		//System.out.println("Imp Increase:" + this.impMoreNum);
		
		System.out.println("Near: " + this.nearNum);
		System.out.println("Cros: " + this.crosNum);
	}
	
	//������Ի�õ�ÿһ����ע�ļ����д���,ͳ�Ʊ�ע�����
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
	 * ����ض����ļ����д�����ȡ�����еı�ע�У����δ���
	 * @param fPath
	 * @param words
	 */
	public void parseFile(String fPath, Vector<Words> words)
	{
		String   fName    = null;
		String   text     = null;
		String   line     = null;	//��ǰ��ע��
		
		BufferedReader br = null;
		
		fName = fPath.substring( 0, fPath.lastIndexOf('.') );
		text  = Toolkit.readFileToString( fName + ".txt" );
		text  = text.replace("\r\n", "\n");
		
		try 
		{
			br = new BufferedReader(new FileReader(fPath));
			
			//���ÿһ�еı�ע������д���ͳ�Ʊ�ע�Ĺ����ʺ͹�ϵ
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
	 * ����ض��ı�ע�ļ���ÿһ�б�ע������д���������ȡ�������ʺ;���ϵ
	 * @param line  ��һ�б�ע���
	 * @param text  �����ж�Ӧ��ԭʼ��������
	 * @param fPath �����б�ע�����Ӧ���ļ�·��
	 * @param words �������͵ı�ע���ӵ�е�ͳһ�Ĺ����ʴʱ�
	 */
	private void parseLine(String line, String text, String fPath, 
			               Vector<Words> words)
	{
		int i,j;
		int max  = 0; 					//ʵ�ʵ��������  
		int type = 0;					//��ע�����ͣ�p1,p2
		
		int expNum  = 0, impNum  = 0;	//��ʽ����ʽ��������Ŀ
		int wordBeg = 0, wordEnd = 0;	//�������������е�λ��.
		int lineBeg = 0, lineEnd = 0;	//�þ��������е�ʵ��λ��.
		
		String[] lists = null;			//��ע�е������ʾ
		String   word  = null;			//��ʱ��ʾword
		String   relID = null;			//��ע�ľ���ϵ
		
		Vector<String> expWords  = new Vector<String>();	//��ǰ��ʾ�����ʼ���
		Vector<String> impWords  = new Vector<String>();	//��ǰ��ʽ�����ʼ���
		
		if( fPath.endsWith(".p1") ) type = 1;
		if( fPath.endsWith(".p2") ) type = 2;
		if( fPath.endsWith(".p3") ) type = 3;
		
		lists = line.split(" ");
		
		if( lists.length < 6 ) return;
		
		if(type == 1) 
			max = lists.length - 4;
		else 
			max = lists.length - 2;
		
		//������Ⱥ�е�ǰ���ϵ�����ܻ�������������:
		//1235 1934 1224 1234 0 1 1236 ������� 0 1 1 5-1-1����˵��
		lineBeg = Toolkit.getLineBeg(line);
		lineEnd = Toolkit.getLineEnd(line);
		
		expNum  = Integer.valueOf(lists[4]);
		impNum  = Integer.valueOf(lists[5]);
		
		
		//��ȡ��ϵ���
		relID   = Toolkit.getRelID( lists[lists.length - 1] );
		
		//������0�нӹ�ϵ
		if( relID.equals("0") ) return;
		
		
		//��ȡ�ñ�ע���е���ʽ������
		for(i = 0; i < expNum; i++)
		{
			wordBeg = Integer.valueOf( lists[6 + i*2] );
			wordEnd = Integer.valueOf( lists[7 + i*2] );
			
			//ȥ�������λ�õĹ�����
			if(wordBeg > wordEnd) continue;
			if(wordBeg < lineBeg || wordBeg > lineEnd) continue;
			if(wordEnd < lineBeg || wordEnd > lineEnd) continue;
			
			
			//�������beg��Ҫ��Ϊ�������й�����ʹ��
			word = text.substring(wordBeg, wordEnd);
			word = Toolkit.formatWord(word);
			
			if( word.length() > 0 )
			{
				word = wordBeg + ":" + word;
				this.addTempWords(expWords, word);
			}
		}
		
		//��ȡ�ñ�ע���е���ʽ������
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
		
		//�����г��ֵ����й����ʼ��뵽ϵͳ�Ĺ����ʼ�����
		int exp = 1;
		String finalWord = "";
		
		if(type == 3)
		{
			//����p3�־��г��ֵĹ�����Ҫ���в��й����ʴ���
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
			//����p1,p2�еĹ�����ֱ�Ӽ��뵽�ʱ��о�����
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
			
			//�ô��������python�����б��������й����ʵĸ���
			this.expMoreNum = this.expMoreNum + expWords.size();
			if(expWords.size() > 0) this.expMoreNum = this.expMoreNum - 1;
			
			this.impMoreNum = this.impMoreNum + impWords.size();
			if(impWords.size() > 0 ) this.impMoreNum = this.impMoreNum - 1;
		}
		
		//----------------------------------------
		//��Լ�û����ʽ������Ҳû����ʽ�����ʵľ���ϵ
		if(expNum == 0 && impNum == 0)
		{
			//���ݺ�����Ҫ���ڴ˽����޸�
		}
		
		//----------------------------------------
		//���ڹ�ϵ�ͽ����ϵ����
		
	}
	
	//��һ�������ʼ��뵽ϵͳword����
	private void addWord(String line, Vector<Words> words, String word, String rID, int type)
	{
		//�����ж��Ƿ��Ѿ��и�word
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
		
		//���ڵ�һ�γ��ֵĵ��ʣ��½�һ��word����뵽Words��
		temp = new Words(word);
		temp.addRelations(rID, type, 1);
		
		words.add(temp);
		
		//�޸����ھ���ϵ
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
	 * ����word�ĳ��ִ�����ӵ����б�ע�г��ֵ���ʱ�����ʼ�����
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
		
		//Ѱ�Ҳ���λ��
		for(i=0; i<words.size(); i++)
		{
			temp = words.get(i);
			wordIndex = Integer.valueOf(temp.substring(0, temp.indexOf(':')));
			
			if(beg<wordIndex)
			{
				break;
			}
			
			//ȥ���ظ���ע�Ĺ�����
			if(beg == wordIndex)
			{
				//����ӵ�word���Ѵ��ڵ�word��һ����,ֱ�ӷ���
				if( word.length() <= temp.length() )
				{
					return;
				}
				else
				{
					//����ӵ�word���Ѵ��ڵĸ��ף���ôɾ�����ӣ���Ӹ���
					words.remove(i);
					break;
				}
			}
		}
		
		words.add(i, word);
	}
	
	//-----------------------------------����ϵ����-----------------------------
	
	//���λ�ã���Ҫ��Ϊ��ͳһ��ʽ��ȷ��ÿ����ϵ���ܹ���Vecotor�г���
	public void initRelations(Vector<Relations> relations)
	{
		for(int index = 0; index < Toolkit.relNO.length; index++)
		{
			Relations newRel = new Relations( Toolkit.relNO[index] );
			relations.add(newRel);
		}
	}
	
	
	/**
	 * ��p1,p2,p3��Ӧ�Ĺ����ʱ������ɶ�Ӧ�ľ���ϵ��
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
		
		HashMap<String, Integer> tempRel = null; //������ָʾ�Ĺ�ϵ����
		
		
		//���ȳ�ʼ���ù�ϵ����,ע�����еľ���ϵ���
		this.initRelations(relations);
		
		//����ÿ�������ʵ���ʽ����ʽ����ϵ��������������ľ���ϵ����
		for(int i = 0; i < words.size(); i++)
		{
			curWord = words.get(i);
			tempRel = curWord.getExpRelations();
			
			//��ʽ����ϵ�͹����ʵĶ�Ӧ
			exp = 1;
			for(Map.Entry<String, Integer> item: tempRel.entrySet() )
			{
				relID  = item.getKey();
				relNum = item.getValue();
				
				this.addRel(relations, relID, curWord.getName(), relNum, exp);
			}
			
			//��ʽ�����ʺ;���ϵ�Ķ�Ӧ
			exp = 0;
			tempRel = curWord.getImpRelations();
			for(Map.Entry<String, Integer> item:tempRel.entrySet())
			{
				relID  = item.getKey();
				relNum = item.getValue();
				
				this.addRel(relations, relID, curWord.getName(), relNum, exp);
			}
		}
		
		//�Ծ���ϵ�Ĺ����ʼ��Ͻ�������
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
		
		//�����ж��Ƿ��Ѿ�����
		for(int i = 0; i < relations.size(); i++)
		{
			curRel = relations.get(i);
			
			if( curRel.getRelID().equals(relID) )
			{
				curRel.addWord(word, num, type);
				return;
			}
		}
		
		//��Ϊ��ʼ��ʱ���Ѿ������й�ϵ���룬
		//��ֹ������֣����ڲ����ڵ�Rel
		curRel = new Relations(relID);
		curRel.addWord(word, num, type);
		
		if(this.debug) 
			System.err.println("*****An unRegister relaion: " + relID+"*****");
	}

	/**
	 * ��һ������ϵ����д�뵽�ļ��С�
	 * @param relations
	 * @param fileName����Ҫд����ļ���
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
	 * ��һ�������ʱ���д�뵽�ļ���.
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
	 * ���ֲ���p1,p2��p3�еĹ�������Ϣ�ϲ���һ����Ĵʱ�
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
