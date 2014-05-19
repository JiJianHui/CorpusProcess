package corpusFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import util.Toolkit;

/**
 * 用于过滤语料，过滤的标准是：行文质量差，政治敏感的.
 * 主要使用的是一个配置文件，里面存放了需要过滤掉的文件路径
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Oct 16, 2013
 */
public class Filter
{
	private String corpus;
	
	private String worseFile;		//需要删除的文件路径保存
	private String worseFileDir;	//删除的文件的保存位置
	
	private String thinkFile;		//需要考虑的文件
	private String thinkFileDir;	//需要考虑的文件的位置
	
	private Vector<String> wfList;	//需要删除的文件路径
	private Vector<String> tfList;	//需要考虑的文件路径
	
	
	private String pworseFile;		//公共错误的文件
	private String pworseFileDir;
	
	private String pthinkFile;
	private String pthinkFileDir;
	
	private Vector<String> pwfList;
	private Vector<String> ptfList;
	
	/**用于处理重复的文件*/
	private Vector<String> delFile;
	
	
	public Filter(String cp, String wf, String wfDir, String pf, String pfDir
			               , String pubWF, String pubTF)
	{
		this.corpus        = cp;
		
		this.worseFile     = wf;
		this.worseFileDir  = wfDir;
		
		this.thinkFile     = pf;
		this.thinkFileDir  = pfDir;
		
		this.wfList        = new Vector<String>();
		this.tfList        = new Vector<String>();
		
		this.pworseFile    = pubWF;
		this.pthinkFile    = pubTF;
		
		this.delFile       = new Vector<String>();
	}
	
	/**
	 * 从语料库中找出需要删除的需要保留的文件
	 * @return
	 * @throws IOException 
	 */
	public void run() throws IOException
	{
		//获取需要提取的文件路径
		this.getFileList(this.pworseFile, this.wfList);
		this.getFileList(this.pthinkFile, this.tfList);

		this.getFileList(this.worseFile, this.wfList);
		this.getFileList(this.thinkFile, this.tfList);
		
		//将对应的文件提取出来
		this.filterCorpus(this.wfList, this.worseFileDir);
		this.filterCorpus(this.tfList, this.thinkFileDir);
	}
	
	/**
	 * 将需要过滤的文件列表提取出来
	 * @param file
	 * @param fList
	 * @throws IOException 
	 */
	private void getFileList(String file, Vector<String> fList) throws IOException
	{
		String line = null;
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		while( (line = br.readLine()) != null )
		{
			if(line.indexOf('\\') == -1)
			{
				continue;
			}
			
			line = line.trim();
			
			String fName = line.replaceFirst("public__result", "");
			fName = fName.replaceFirst("part1__msguo__Result", "");
			fName = fName.replaceFirst("part2__jwang__Result", "");
			fName = fName.replaceFirst("part3__liu__Result", "");
			
			if(this.delFile.contains(fName)) continue;
			
			this.delFile.add(fName);
			fList.add(line);
		}
		
		br.close();
	}
	
	/**
	 * 将语料中的不合适的语料提取出来
	 * @param filterList
	 * @param destDir
	 * @throws IOException
	 */
	public void filterCorpus(Vector<String> filterList, String destDir) throws IOException
	{
		String fName = null;
		String temp  = "F:\\Corpus Data\\result2-Filtered";
		
		for(String fPath:filterList)
		{
			File srcFile = new File(fPath);
			
			fName = srcFile.getAbsolutePath();
			fName = fName.replace(temp,"");
			//fName = fName.replaceAll("\\", " ");
			fName = fName.replace('\\', ' ');
			
			File dstFile = new File(destDir+"\\" + fName);
			
			if(srcFile.exists())
			{
				if( !dstFile.exists() )
				{
					dstFile.createNewFile();
					Toolkit.copyFile(srcFile, dstFile);
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		String corpus = "F:\\Corpus Data\\result2-Filtered";
		
		String wf     = "F:\\Corpus Data\\result2-Filtered\\Need Deleted.txt";
		String wfDir  = "F:\\Corpus Data\\result2-Filtered\\NeedDeleted";
		
		String tf     = "F:\\Corpus Data\\result2-Filtered\\Need Think.txt";
		String tfDir  = "F:\\Corpus Data\\result2-Filtered\\NeedThinked";
		
		
		String pubWF = "F:\\Corpus Data\\result2-Filtered\\pub Deleted.txt";
		String pubTF = "F:\\Corpus Data\\result2-Filtered\\pub Think.txt";
		
		Filter filter = new Filter(corpus, wf, wfDir, tf, tfDir, pubWF, pubTF);
		
		filter.run();
	}
}
