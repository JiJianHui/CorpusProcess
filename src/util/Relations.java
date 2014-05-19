package util;
import java.util.HashMap;
import java.util.Map;

/**
 * ��Ҫ����ͳ�ƹ�ϵ�ķֲ�
 * @author rainbow
 * @time   Apr 21, 2013
 */
public class Relations
{
	private String relID;	//�þ���ϵ�ı��1-1-2
	//private String name;	//�þ���ϵ������(���������)
	//private int allNum;		//�þ���ϵ���ֵ��ܴ���
	
	private int expNum;		//�þ���ϵ��Ϊ��ʽ��ϵ�Ĵ���
	private int impNum;		//�þ���ϵ��Ϊ��ʽ��ϵ���ֵĴ���
	
	//��һ��String:ָ�����ʵ����ƣ��ڶ���Integerָ�����ʳ��ֵĴ���
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
	
	//��ù�ϵ�Ĺ����ʼ��������һ��������
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
		
		//�ù�ϵ�Ѿ������ڹ�ϵ������
		if( words.containsKey(word) )
		{
			temp = words.get(word) + num;
			
			//���ǵ�ԭ�ȵ�ֵ
			words.remove(word);
			words.put(word, temp);
		}
		else
		{
			//���ڲ����ڸù�ϵ�����еĹ�ϵ����ֱ���޸�
			words.put(word, num);
		}
	}
	

	/**
	* ����ǰ��ϵ��ʾΪ�ַ�����ʽ����ʽ���£�
	* [name allNum expNum impNum expKind impKind [X]word n...[Y]word n ....]
	* @return
	*/
	public String convertToString()
	{
		Integer wordNum  = 0;
		String  wordName = null;
		
		String line  = this.relID + '\t';
		
		//���������Ϣ
		int allNum   = this.expNum + this.impNum;
		line = line + String.valueOf(allNum) + '\t';
		
		line = line + String.valueOf(this.expNum) + '\t';
		line = line + String.valueOf(this.impNum) + '\t';
		
		line = line + String.valueOf(this.expWords.size()) + '\t';
		line = line + String.valueOf(this.impWords.size()) + '\t';
		
		//�����ʽ������,ѭ�������һ��\t���������ָ���ʽ������
		for(Map.Entry<String, Integer> item: this.expWords.entrySet() )
		{
			wordName = item.getKey();
			wordNum  = item.getValue();
			
			wordName = "[X]" + wordName + '\t' + wordNum.toString();
			
			line     = line + wordName + '\t';
		}
		
		//�����ʽ������
		for(Map.Entry<String, Integer> item: this.impWords.entrySet())
		{
			wordName = item.getKey();
			wordNum  = item.getValue();
			
			wordName = "[Y]" + wordName + '\t' + wordNum.toString();
			
			line     = line + wordName + '\t';
		}
		
		//��line����\t����
		line = line.trim();
		
		return line;
	}
	
	//��hashmap�����ʼ��Ͻ�������
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
