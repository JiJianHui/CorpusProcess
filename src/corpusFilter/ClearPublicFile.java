package corpusFilter;

import java.util.Vector;

/**
 * ������������ע���ļ���ɾ����Ӧ�Ĺ����ļ�
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Oct 17, 2013
 */
public class ClearPublicFile
{
	private String pubDir;
	
	private String guoDir;
	private String liuDir;
	private String wangDir;
	
	private Vector<String> pubFiles;
	
	public ClearPublicFile(String pDir, String gDir, String lDir, String wDir)
	{
		this.pubDir  = pDir;
		
		this.guoDir  = gDir;
		this.liuDir  = lDir;
		this.wangDir = wDir;
		
		this.pubFiles = new Vector<String>();
	}
	
	public void run()
	{
		
	}
	
}
