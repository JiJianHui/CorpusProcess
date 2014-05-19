package corpus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;

import util.Toolkit;

/**
 * ����ϵ������ͳ�ƣ�ͳ�ƹ�ϵ�ڸ��������ϵ������ֲ�.
 * �ó�����Ҫ����Corpus�������ɵ�relations����ļ�,Ŀǰֻͳ�Ƶ���������ϵ�ϡ�
 * @author rainbow
 * @time   May 3, 2013
 */
public class RelTree
{
	private String p1RelPath;	//Corpus����ϵ����ļ�λ��
	private String p2RelPath;
	private String p3RelPath;
	
	private String expRelTreePath;	//�����ַ
	private String impRelTreePath;	//

	private HashMap<String, Integer> expRels;
	private HashMap<String, Integer> impRels;
	
	//String����relID��Integer������Ŀ
	//private HashMap<String, Integer> p1ExpRelations;	
	//private HashMap<String, Integer> p1ImpRelations;
	
	//private HashMap<String, Integer> p2ExpRelations;
	//private HashMap<String, Integer> p2ImpRelaitons;
	//private HashMap<String, Integer> p3ExpRelations;
	//private HashMap<String, Integer> p3ImpRelations;
	
	public RelTree()
	{
		this.p1RelPath = "F:\\Result\\p1Rel.txt";
		this.p2RelPath = "F:\\Result\\p2Rel.txt";
		this.p3RelPath = "F:\\Result\\p3Rel.txt";
		
		this.expRelTreePath = "F:\\Result\\expRelTree.txt";
		this.impRelTreePath = "F:\\Result\\impRelTree.txt";
		
		this.expRels   = new HashMap<String, Integer>();
		this.impRels   = new HashMap<String, Integer>();
		
		this.init();
	}
	
	public void run()
	{
		this.countRelTree(this.p1RelPath, this.expRels, this.impRels);
		this.countRelTree(this.p2RelPath, this.expRels, this.impRels);
		this.countRelTree(this.p3RelPath, this.expRels, this.impRels);
		
		this.saveResult(this.expRelTreePath, this.expRels);
		this.saveResult(this.impRelTreePath, this.impRels);
	}
	
	
	private void init()
	{
		int index = 0;
		
		//String relID   = null;
		String[] lists = null;
		
		for(index = 0; index < Toolkit.relNO.length; index++)
		{
			lists = Toolkit.relNO[index].split("-");
			
			if(lists.length > 3) continue;
			
			//relID = new String( this.tool.relNO[index] );
			
			this.expRels.put(Toolkit.relNO[index], 0);
			this.impRels.put(Toolkit.relNO[index], 0);
		}
	}
	
	/**
	 * ͳ���ļ��µľ���ϵ�����α�ʾ��
	 * @param path
	 */
	public void countRelTree(String path, HashMap<String, Integer> expRels, 
			                 HashMap<String, Integer> impRels)
	{
		try
		{
			String line   = null;
			File   file   = new File(path); 
			
			FileReader     fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			while( ( line = br.readLine() ) != null)
			{
				this.parseLine(line, expRels, impRels);
			}
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	//���һ�о���ϵ������д���
	// [name allNum expNum impNum expKind impKind [X]word n...[Y]word n ....]
	public void parseLine(String line, HashMap<String, Integer> expRels, 
			                           HashMap<String, Integer> impRels)
	{
		line = line.trim();
		
		String[] lists = line.split("\t");
		String   relID = lists[0];
		
		int temp   = 0;
		
		int expNum = Integer.valueOf(lists[2]);
		int impNum = Integer.valueOf(lists[3]);
		
		if( expNum == 0 && impNum == 0 ) return;
		
		String[] nodes = relID.split("-");
		
		//����ָ���ĸ�������3���������1-2-1-*�����ļ���ֻȡǰ����������
		if(nodes.length > 3)
		{
			relID = nodes[0] + "-" + nodes[1] + "-" + nodes[2];
		}
		
		//ͨ���������ù�ϵ��ӵ�������
		while(true)
		{
			temp = expNum;
			
			if( expRels.containsKey(relID) )
			{
				temp = temp + expRels.get(relID).intValue();
			}
			expRels.put(relID, temp);
			
			temp = impNum;
			if( impRels.containsKey(relID) )
			{
				temp = temp + impRels.get(relID).intValue();
			}
			impRels.put(relID, temp);
			
			if(relID.lastIndexOf('-') != -1)
				relID = relID.substring(0, relID.lastIndexOf('-') );
			else
				break;
		}
	}
	
	//������������ļ���
	public void saveResult(String filePath, HashMap<String, Integer> rels)
	{
		int relIndex  = -1;
		
		Integer num   = null;
		String  relID = null;
		
		String  relName = null;
		
		try
		{
			File outFile  = new File(filePath);
			FileWriter fw = new FileWriter(outFile);
			
			Object[] keys = rels.keySet().toArray(); 
			Arrays.sort(keys);
			
			for(int index = 0; index < keys.length; index++)
			{
				relID = (String) keys[index];
				num   = rels.get(relID);
				
				relIndex = this.getIndex(relID);
				relName  = Toolkit.relName[relIndex];
				
				relID = relID + relName +"��ϵ" + '\t' + num.toString() + "\r\n";
				
				fw.write(relID);
			}
//
//			for(Map.Entry<String, Integer> item:rels.entrySet())
//			{
//				relID = item.getKey();
//				num   = item.getValue();
//				relID = relID + '\t' + num.toString() + "\r\n";
//				
//				fw.write(relID);
//			}
			
			fw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private int getIndex(String relID)
	{
		String curRel = null;
		
		for(int index = 0; index < Toolkit.relNO.length; index++)
		{
			curRel = Toolkit.relNO[index];
			
			if(curRel.equals(relID))
				return index;
		}
		
		return -1;
	}
	
	public static void main(String args[])
	{
		RelTree relTree = new RelTree();
		relTree.run();
	}
}
