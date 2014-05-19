package util;
import java.util.HashMap;
import java.util.Map;


public class Words
{
	private Integer   wID;			//关联词编号
	private String    name; 		//关联词内容或者说关联词名称
	
	private int       expNum;		//该关联词作为显式关联词出现的次数	
	private int       impNum;		//该关联词作为隐式关联词出现的次数
	
	public static int index = 0; 	//统一的关联词编号索引
	
	private HashMap<String, Integer> expRelations;	//String 指关系的编号
	private HashMap<String, Integer> impRelations;	//Integer指该关联词指示该关系的次数
	
	//2013-09-05
	/**在语料中出现，但是并没有指示句间关系的次数*/
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
	 * 根据关联词指示的句间关系修改该关联词指示的句间关系集合
	 * @param relID：句间关系编号
	 * @param type：显式句间关系1，
	 * @param num：该关系出现的个数
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
		
		//该关系已经存在于关系集合中
		if( rels.containsKey(relID) )
		{
			temp = rels.get(relID) + num;
			
			//覆盖掉原先的值
			rels.remove(relID);
			rels.put(relID, temp);
		}
		else
		{
			//对于不存在该关系集合中的关系进行直接修改
			rels.put(relID, num);
		}
	}

	/**
	 * 将该关联词转换为一个字符串,格式如下：
	 * name num expNum impNum expKind impKind [X]relation n...[Y]relation n ....
	 * @return
	 */
	public String convertToString()
	{
		String  line   = "";
		String  relID  = "";
		Integer relNum = 0 ;
		
		//添加数量信息
		line = line + this.name + '\t';
		line = line + String.valueOf( this.getAllNum() ) + '\t';
		
		line = line + String.valueOf(this.expNum) + '\t';
		line = line + String.valueOf(this.impNum) + '\t';
		
		line = line + String.valueOf(this.getExpKind()) + '\t';
		line = line + String.valueOf(this.getImpKind()) + '\t';
		
		//添加显式句间关系关系数据
		for(Map.Entry<String, Integer> item:this.expRelations.entrySet())
		{
			relID  = item.getKey();
			relNum = item.getValue();
			
			relID  = "[X]" + relID + '\t' + relNum.toString();
			
			line   = line + relID + '\t';
		}
		
		//添加隐式关联词数据
		for(Map.Entry<String, Integer> item:this.impRelations.entrySet())
		{
			relID  = item.getKey();
			relNum = item.getValue();
			
			relID  = "[Y]" + relID + '\t' + relNum.toString();
			
			line   = line + relID + '\t';
		}
		//将最后的\t消除
		line = line.trim();
		
		return line;
	}
	
	public Integer getWordID(){ return this.wID; }
	
	//获取对应的关系列表
	public HashMap<String, Integer> getExpRelations(){ return expRelations; }
	public HashMap<String, Integer> getImpRelations(){ return impRelations; }

	//获取该关联词出现的总次数
	public int getAllNum(){ return this.expNum + this.impNum; }
	public int getExpNum(){ return this.expNum; }
	public int getImpNum(){ return this.impNum; }
	
	//获取该关联词指示句间关系的种类数
	public int getExpKind(){return this.expRelations.size();}
	public int getImpKind(){return this.impRelations.size();}
	
	public String getName(){ return this.name; }
	
	public void setUnableNum(int unNum){ this.unableNum = unNum; }
	public int getUnableNum(){ return this.unableNum; }
}
