package corpus;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class Temp
{
	public String getWord(String file, int beg, int end)
	{
		int count = 0;
		//String coding = "UTF-8";
		String word   = null;
		char[] buf = new char[100];
		try 
		{
			FileInputStream   fr         = new FileInputStream(file);
			InputStreamReader inReader   = new InputStreamReader(fr);
			
			BufferedReader    inFile     = new BufferedReader(inReader);
			
//			String line = inFile.readLine();
//			System.out.print(line);
			
			inFile.skip(beg);
			//count = inFile.read(buf, beg, end);
			
			word = String.copyValueOf(buf, 0, count);
//			word = String.valueOf(buf);

			System.out.println(word);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return word;
	}
	
	public static void main(String[] args)
	{
		//Temp t = new Temp();
		//String file = "F:\\TestData\\part1__msguo__Result\\chtb_1007#.txt";
		String file = "F:\\TestData\\part1__msguo__Result\\test.txt";
		
		//String word = t.getWord(file, 1, 4);
		//System.out.println(word);
		
//		File file = new File("D:\\Music");
//		System.out.println(file.getAbsolutePath());
		
		System.out.println(Math.abs(1-3));
	}
}
