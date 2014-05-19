package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * ��Ҫ�ǳ������ļ��������
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Oct 28, 2013
 */
public class FileManager
{
	public FileManager()
	{
		
	}
	
	/**
	 * ��ָ�����ļ�·���б��е��ļ�������Ŀ���ļ�������
	 * @param fListsPath���������Ҫ�������ļ�·��
	 * @param dstDir��Ŀ���ļ���
	 * @throws IOException
	 */
	public static void copyFileTo(String fListsPath, String dstDir) throws IOException
	{
		Vector<String> fLists = new Vector<String>();
		Vector<String> lines  = new Vector<String>();
		
		Toolkit.readFileToLines(fListsPath, lines);
		
		for(String line:lines)
		{
			if( line.endsWith(".p3") )
			{
				fLists.add(line);
				fLists.add(line.replace( ".p3", ".p2") );
				fLists.add(line.replace( ".p3", ".p1") );
				fLists.add(line.replace( ".p3", ".txt") );
			}
		}
		
		for(String fPath:fLists)
		{
			File srcFile = new File(fPath); 
			File dstFile = new File(dstDir + "\\" + srcFile.getName() );
			
			Toolkit.copyFile(srcFile, dstFile);
		}
	}
	
	
	/**
	 * �����ļ�����Ҫ������������ļ�����
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
    public static void copyFile(File sourceFile, File targetFile) throws IOException 
    {
        BufferedInputStream  inBuff  = null;
        BufferedOutputStream outBuff = null;
        
        try 
        {
            // �½��ļ����������������л���
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // �½��ļ���������������л���
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // ��������
            byte[] b = new byte[1024 * 5];
            int len;
            
            while ((len = inBuff.read(b)) != -1) 
            {
                outBuff.write(b, 0, len);
            }
            // ˢ�´˻���������
            outBuff.flush();
        } 
        finally 
        {
            // �ر���
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
	
	public static void main(String[] args) throws IOException
	{
		String dstDir     = "F:\\Distribution Data\\Example";
		String fListsPath =  "F:\\Distribution Data\\ExamplePath.txt";
		
		FileManager.copyFileTo(fListsPath, dstDir);
		
	}
}
