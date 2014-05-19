package corpusFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import util.Toolkit;

/**
 * ���ڹ������ϣ����˵ı�׼�ǣ�����������������е�.
 * ��Ҫʹ�õ���һ�������ļ�������������Ҫ���˵����ļ�·��
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Oct 16, 2013
 */
public class Filter
{
	private String corpus;
	
	private String worseFile;		//��Ҫɾ�����ļ�·������
	private String worseFileDir;	//ɾ�����ļ��ı���λ��
	
	private String thinkFile;		//��Ҫ���ǵ��ļ�
	private String thinkFileDir;	//��Ҫ���ǵ��ļ���λ��
	
	private Vector<String> wfList;	//��Ҫɾ�����ļ�·��
	private Vector<String> tfList;	//��Ҫ���ǵ��ļ�·��
	
	
	private String pworseFile;		//����������ļ�
	private String pworseFileDir;
	
	private String pthinkFile;
	private String pthinkFileDir;
	
	private Vector<String> pwfList;
	private Vector<String> ptfList;
	
	/**���ڴ����ظ����ļ�*/
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
	 * �����Ͽ����ҳ���Ҫɾ������Ҫ�������ļ�
	 * @return
	 * @throws IOException 
	 */
	public void run() throws IOException
	{
		//��ȡ��Ҫ��ȡ���ļ�·��
		this.getFileList(this.pworseFile, this.wfList);
		this.getFileList(this.pthinkFile, this.tfList);

		this.getFileList(this.worseFile, this.wfList);
		this.getFileList(this.thinkFile, this.tfList);
		
		//����Ӧ���ļ���ȡ����
		this.filterCorpus(this.wfList, this.worseFileDir);
		this.filterCorpus(this.tfList, this.thinkFileDir);
	}
	
	/**
	 * ����Ҫ���˵��ļ��б���ȡ����
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
	 * �������еĲ����ʵ�������ȡ����
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
