package corpusFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import util.Toolkit;

/**
 * 用来计算每个文件关系数目的分布，比如0个关系的文件有多少，1个关系的文件有多少
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Oct 17, 2013
 */

public class CountRelation
{
	/**用来保存含有指定关系数目的文件路径**/
	private Vector<Vector<String>> relDisPath;
	
	/**保存所有的标注路径**/
	private Vector<String> filePaths;
	
	/**语料库中的基本路径**/
	private String corpus;
	
	/**需要删除的文件列表**/
	private String delPath;
	private String pubDelPath;

	private Vector<String> delFiles;
	
	
	/**需要统计的关系数目的类别0-9 和 >10的共11类**/
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
		
		//为了避免读入NeedDeleted文件夹下的txt文件
		Toolkit.getFiles(corpus, filePaths, ".p1");
		
		//记录每个文件中的关系数目
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
		
		//输出各个类别的关系数目分布
		this.printResult();
		
		//将结果列表抽取出来
		String dir = "F:\\Corpus Data\\Corpus-Filtered";
		this.extractResult(CountRelation.maxRelClass - 1, dir);
	}
	
	/**
	 * 读取已经确定删除的文件
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
	 * 计算一个文件中的关系数目
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
	 * 将满足关系个数的所有指定的文件抽取出来
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
