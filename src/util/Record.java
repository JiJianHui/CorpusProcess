package util;
import java.util.Vector;

/**
 * ��ע��ÿһ����Ϊһ����¼
 * @author rainbow
 * @time   May 21, 2013
 */
public class Record
{	 
	private int            arg1Beg;	  //��һ��Ԫ�ص���ʼλ��
	private int            arg1End;
	private String         arg1Content;
	
	private int            arg2Beg;	  //�ڶ���Ԫ�ص���ʼλ��
	private int            arg2End;
	private String 		   arg2Content;
	
	private int            expNum;	  //�ü�¼�е���ʽ�����ʸ���
	private int            impNum;	  //�ü�¼�е���ʽ�����ʸ���
	
	private String         senseID;	  //����ϵ���
	private String         senseContent; 
	
	private String         line;      //�ü�¼��ԭʼ�ı���ע��
	private String         file;      //�ü�¼��Ӧ���ĵ�����
	private int 		   type;	  //��ע�������ı�ע����p1:1 .p2:2 p3:3
	
	private Vector<String> expWords;  //��ʽ�����ʼ���	
	private Vector<String> impWords;  //��ʽ�����ʼ���
	
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
		
		//��������Ԫ�صĿ�ʼ�ͽ���
		if(this.arg1Beg > this.arg2Beg)
		{
			int temp  = this.arg1Beg;
			this.arg1Beg = this.arg2Beg;
			this.arg2Beg = temp;
			
			temp = this.arg1End;
			this.arg1End = this.arg2End;
			this.arg2End = temp;
		}
		
		//��ȡ������
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
		
		//����������ʵ���ʼλ�÷�����һ�����У�������
		ArrayList<Integer> begs = new ArrayList<Integer>();
		ArrayList<Integer> ends = new ArrayList<Integer>();
		
		
		//��ȡ�ñ�ע���е���ʽ������
		for(int i = 0; i < expNum; i++)
		{
			wordBeg = Integer.valueOf( lists[6 + i*2] );
			wordEnd = Integer.valueOf( lists[7 + i*2] );
			
			//ȥ�������λ�õĹ�����
			if(wordBeg > wordEnd) continue;
			if(wordBeg < this.arg1Beg || wordBeg > this.arg2End) continue;
			if(wordEnd < this.arg1Beg || wordEnd > this.arg2End) continue;
			
			begs.add(wordBeg);
			ends.add(wordEnd);
		}
		
		//�����ҳ�beg��С��λ�ã���ȡ�����ʣ���ɾ��
		while(begs.size() > 0)
		{
			int min = 0;
			
			for(int index = 0; index < begs.size(); index++)
			{
				if( begs.get(index) < begs.get(min) ) 
					min = index;
			}
			
			//��ȡ������,�÷���̫��....
			wordBeg = begs.get(min);
			wordEnd = ends.get(min);
			
			expWord = Toolkit.getWordFromFile(wordBeg, wordEnd, this.file);
			this.expWords.add(expWord);
			
			
			//ɾ����Ӧ��λ��
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
		
		//����������ʵ���ʼλ�÷�����һ�����У�������
		ArrayList<Integer> begs = new ArrayList<Integer>();
		ArrayList<Integer> ends = new ArrayList<Integer>();
	}
	**/
}
