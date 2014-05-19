package util;
import java.util.HashMap;
import java.util.Map;


public class Words
{
	private Integer   wID;			//�����ʱ��
	private String    name; 		//���������ݻ���˵����������
	
	private int       expNum;		//�ù�������Ϊ��ʽ�����ʳ��ֵĴ���	
	private int       impNum;		//�ù�������Ϊ��ʽ�����ʳ��ֵĴ���
	
	public static int index = 0; 	//ͳһ�Ĺ����ʱ������
	
	private HashMap<String, Integer> expRelations;	//String ָ��ϵ�ı��
	private HashMap<String, Integer> impRelations;	//Integerָ�ù�����ָʾ�ù�ϵ�Ĵ���
	
	//2013-09-05
	/**�������г��֣����ǲ�û��ָʾ����ϵ�Ĵ���*/
	private Integer    unableNum;
	
	public Words(String name)
	{
		this.name         = name;
		this.wID          = Words.index++;
		
		this.expNum       = 0;
		this.impNum       = 0;
		
		this.impRelations = new HashMap<String, Integer>();
		this.expRelations = new HashMap<String, Integer>();
	
		this.unableNum    = 0;
	}

	/**
	 * ���ݹ�����ָʾ�ľ���ϵ�޸ĸù�����ָʾ�ľ���ϵ����
	 * @param relID������ϵ���
	 * @param type����ʽ����ϵ1��
	 * @param num���ù�ϵ���ֵĸ���
	 */
	public void addRelations(String relID, int type, int num)
	{
		if(type == 0)
		{
			this.impNum  = this.impNum + num;
			this.addRel(this.impRelations, relID, num);
		}
		else
		{
			this.expNum = this.expNum + num;
			this.addRel(this.expRelations, relID, num);
		}
	}
	
	private void addRel(HashMap<String, Integer> rels, String relID, int num)
	{
		int temp = 0;
		
		//�ù�ϵ�Ѿ������ڹ�ϵ������
		if( rels.containsKey(relID) )
		{
			temp = rels.get(relID) + num;
			
			//���ǵ�ԭ�ȵ�ֵ
			rels.remove(relID);
			rels.put(relID, temp);
		}
		else
		{
			//���ڲ����ڸù�ϵ�����еĹ�ϵ����ֱ���޸�
			rels.put(relID, num);
		}
	}

	/**
	 * ���ù�����ת��Ϊһ���ַ���,��ʽ���£�
	 * name num expNum impNum expKind impKind [X]relation n...[Y]relation n ....
	 * @return
	 */
	public String convertToString()
	{
		String  line   = "";
		String  relID  = "";
		Integer relNum = 0 ;
		
		//���������Ϣ
		line = line + this.name + '\t';
		line = line + String.valueOf( this.getAllNum() ) + '\t';
		
		line = line + String.valueOf(this.expNum) + '\t';
		line = line + String.valueOf(this.impNum) + '\t';
		
		line = line + String.valueOf(this.getExpKind()) + '\t';
		line = line + String.valueOf(this.getImpKind()) + '\t';
		
		//�����ʽ����ϵ��ϵ����
		for(Map.Entry<String, Integer> item:this.expRelations.entrySet())
		{
			relID  = item.getKey();
			relNum = item.getValue();
			
			relID  = "[X]" + relID + '\t' + relNum.toString();
			
			line   = line + relID + '\t';
		}
		
		//�����ʽ����������
		for(Map.Entry<String, Integer> item:this.impRelations.entrySet())
		{
			relID  = item.getKey();
			relNum = item.getValue();
			
			relID  = "[Y]" + relID + '\t' + relNum.toString();
			
			line   = line + relID + '\t';
		}
		//������\t����
		line = line.trim();
		
		return line;
	}
	
	public Integer getWordID(){ return this.wID; }
	
	//��ȡ��Ӧ�Ĺ�ϵ�б�
	public HashMap<String, Integer> getExpRelations(){ return expRelations; }
	public HashMap<String, Integer> getImpRelations(){ return impRelations; }

	//��ȡ�ù����ʳ��ֵ��ܴ���
	public int getAllNum(){ return this.expNum + this.impNum; }
	public int getExpNum(){ return this.expNum; }
	public int getImpNum(){ return this.impNum; }
	
	//��ȡ�ù�����ָʾ����ϵ��������
	public int getExpKind(){return this.expRelations.size();}
	public int getImpKind(){return this.impRelations.size();}
	
	public String getName(){ return this.name; }
	
	public void setUnableNum(int unNum){ this.unableNum = unNum; }
	public int getUnableNum(){ return this.unableNum; }
}
