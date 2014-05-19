package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * 主要是常见的文件处理操作
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
	 * 将指定的文件路径列表中的文件拷贝到目标文件夹下面
	 * @param fListsPath：存放了需要拷贝的文件路径
	 * @param dstDir：目标文件夹
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
	 * 复制文件，需要传入的是两个文件对象
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
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            
            while ((len = inBuff.read(b)) != -1) 
            {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } 
        finally 
        {
            // 关闭流
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
