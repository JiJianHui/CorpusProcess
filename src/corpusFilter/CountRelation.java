package corpusFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import util.Toolkit;

/**
 * ��������ÿ���ļ���ϵ��Ŀ�ķֲ�������0����ϵ���ļ��ж��٣�1����ϵ���ļ��ж���
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Oct 17, 2013
 */

public class CountRelation
{
	/**�������溬��ָ����ϵ��Ŀ���ļ�·��**/
	private Vector<Vector<String>> relDisPath;
	
	/**�������еı�ע·��**/
	private Vector<String> filePaths;
	
	/**���Ͽ��еĻ���·��**/
	private String corpus;
	
	/**��Ҫɾ�����ļ��б�**/
	private String delPath;
	private String pubDelPath;

	private Vector<String> delFiles;
	
	
	/**��Ҫͳ�ƵĹ�ϵ��Ŀ�����0-9 �� >10�Ĺ�11��**/
	public static int maxRelClass = 11;
	
	public CountRelation(String corpus, String delFile, String pubDelFile)
	{
		this.corpus     = corpus;
		
		this.delPath    = delFile;
		this.pubDelPath = pubDelFile;
		
		this.relDisPath = new Vector<Vector<String>>();
		this.relDisPath.clear();
		this.relDisPath.setSize(CountRelation.maxRelClass);
		
		for(int index = 0; index < CountRelation.maxRelClass; index++)
		{
			this.relDisPath.set(index, new Vector<String>() );
		}
		
		this.filePaths = new Vector<String>();
		this.delFiles  = new Vector<String>();
	}
	
	public void run() throws IOException
	{
		this.readDeletedFiles(this.delPath);
		this.readDeletedFiles(this.pubDelPath);
		
		//Ϊ�˱������NeedDeleted�ļ����µ�txt�ļ�
		Toolkit.getFiles(corpus, filePaths, ".p1");
		
		//��¼ÿ���ļ��еĹ�ϵ��Ŀ
		int relNum = 0;
		
		String fPath  = null;
		String p2Path = null;
		String p3Path = null;
		
		for(String p1Path:filePaths)
		{
			relNum = 0;
		
			fPath  = p1Path.replaceAll(".p1", ".txt");
			p2Path = p1Path.replaceAll(".p1", ".p2");
			p3Path = p1Path.replaceAll(".p1", ".p3");
			
			if( this.delFiles.contains(fPath) )	
			{
				continue;
			}

			relNum += this.countNum(p1Path);
			relNum += this.countNum(p2Path);
			relNum += this.countNum(p3Path);
			
			int index = relNum;
			
			if(relNum > 9)
			{
				index = CountRelation.maxRelClass - 1;
			}
			
			this.relDisPath.get(index).add(fPath);
		}
		
		//����������Ĺ�ϵ��Ŀ�ֲ�
		this.printResult();
		
		//������б��ȡ����
		String dir = "F:\\Corpus Data\\Corpus-Filtered";
		this.extractResult(CountRelation.maxRelClass - 1, dir);
	}
	
	/**
	 * ��ȡ�Ѿ�ȷ��ɾ�����ļ�
	 * @throws IOException
	 */
	public void readDeletedFiles(String fPath) throws IOException
	{
		String line = null;
		
		BufferedReader br = new BufferedReader(new FileReader(fPath));
		
		while( (line = br.readLine()) != null )
		{
			if(line.indexOf('\\') == -1)
			{
				continue;
			}
			
			line = line.trim();
			
			this.delFiles.add(line);
		}
		
		br.close();
	}
	
	/**
	 * ����һ���ļ��еĹ�ϵ��Ŀ
	 * @param p1Path
	 * @return
	 * @throws IOException
	 */
	public int countNum(String p1Path) throws IOException
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
	
	public void printResult()
	{
		for(int index = 0; index < CountRelation.maxRelClass; index++)
		{
			String num = String.valueOf( this.relDisPath.get(index).size() );
			
			if(index < CountRelation.maxRelClass - 1)
			{
				System.out.println("relNum =  " + String.valueOf(index) + ": " + num);
			}
			else
			{
				System.out.println("relNum > " + String.valueOf(index) + ": " + num);
			}
			if(Integer.valueOf(num) > 0)
			{
				//System.out.println(this.relDisPath.get(index).get(Integer.valueOf(num)/2) + "\n");
			}
		}
	}
	
	/**
	 * �������ϵ����������ָ�����ļ���ȡ����
	 * @param index
	 * @param dir
	 * @throws IOException
	 */
	public void extractResult(int index, String dir) throws IOException
	{
		Vector<String> fList = this.relDisPath.get(index);
		
		for(String fPath: fList)
		{
			File srcFile = new File(fPath);
			
			String temp  = "F:\\Corpus Data\\result2-Filtered";
			String fName = fPath.replace(temp,"");
			
			File dstFile = new File( dir + "\\" + fName.replace('\\', ' ') );
			
			if( !dstFile.exists() )
			{
				dstFile.createNewFile();
				Toolkit.copyFile(srcFile, dstFile);
			}
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		String corpus     = "F:\\Corpus Data\\result2-Filtered";
		String delFPath   = "F:\\Corpus Data\\result2-Filtered\\Need Deleted.txt";
		String pubDelPath = "F:\\Corpus Data\\result2-Filtered\\pub Deleted.txt";
		
		CountRelation count = new CountRelation(corpus, delFPath, pubDelPath);
		
		count.run();
	}
}
