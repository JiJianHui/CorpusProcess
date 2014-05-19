package util;

import java.util.HashMap;
import java.util.HashSet;

public class Test
{
	public int age;
	public String name;
	
	public Test(int age, String name)
	{
		this.age  = age;
		this.name = name;
	}
	public Test(String name)
	{
		this.name = name;
	}
	public Test()
	{
		
	}
	
	public void format(String line)
	{
		line = line.substring(2);
	}
	
    public static int getLenOfString(String nickname) {
        // 汉字个数
        int chCnt = 0;  
        
        String regEx = "[\\u4e00-\\u9fa5]"; // 如果考虑繁体字，u9fa5-->u9fff
        
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regEx);  
        java.util.regex.Matcher m = p.matcher(nickname);  
        
        while (m.find()) { 
                chCnt++; 
        }  
        return chCnt;
    }

    public static void testsplit()
    {
        String   test  = "160...132";
        String[] lists = test.split("\\.\\.\\.");
        System.out.println("The Length is: " + lists.length);
        for(int index = 0; index < lists.length; index++)
        {
            System.out.println(index + ":" + lists[index]);
        }
    }
	
	public static void main(String args[])
	{

		//Test.testsplit();
        HashMap[] temp = new HashMap[100];

        temp[0] = new HashMap<String, Integer>();

        temp[0].put("Name", 100);

        System.out.println(temp[0].get("Name"));




	}
}
