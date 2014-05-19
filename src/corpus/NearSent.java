package corpus;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

import util.Toolkit;

/**
 * ��Ҫ������ͳ�ƾ���ϵ���ж�����λ�����ڵľ���֮�䣬�ж�����λ�ڽ���ľ���֮��
 * @author rainbow
 * @time   May 9, 2013
 */
public class NearSent
{
	private String  dir;
	
	private Vector<String> files;
	
	private int nearNum;	//λ�����ھ���֮��ľ���ϵ
	private int crosNum;	//λ�ڽ������֮��ľ���ϵ
	
	private int nullNum;	//û�������ԵĹ�ϵ
	
	public static int EPS = 4; //��Ϊ�ո�ͱ��,��EXP��Χ�ڵ�����������Ϊ����
	
	private boolean debug;
	
	public NearSent()
	{
		this.dir     = null;
		
		this.nearNum = 0;
		this.crosNum = 0;
		
		this.files   = new Vector<String>();
		
		this.debug   = false;
	}
	
	public void init()
	{
		this.nearNum = 0;
		this.crosNum = 0;
		this.files.clear();
	}
	
	public void run(String dir, String ending)
	{
		this.init();
		
		try
		{
			this.dir = dir;
		
			//�޸��������p2��p3��Ӧ��
			Toolkit.getFiles(this.dir, this.files, ending);
		
			for(int index = 0; index < this.files.size(); index++)
			{
				String fPath = this.files.get(index);
				this.parseFile(fPath);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * ���ļ����д���
	 * @param fPath
	 * @throws Exception
	 */
	public void parseFile(String fPath) throws Exception
	{
		int beg1 = 0, end1 = 0;	//ÿһ�б�ע����漰�����������ӵĿ�ʼ�ͽ���
		int beg2 = 0, end2 = 0;
		
		int expNum = 0, impNum = 0;
		
		String   line  = null;
		String   relID = null;
		String[] lists = null;
		
		BufferedReader br = new BufferedReader(new FileReader(fPath));
		
		while( (line = br.readLine()) != null )
		{
			lists = line.split(" ");

			//ȥ�����кͳнӹ�ϵ
			if( lists.length < 4 ) continue;
			
			relID = Toolkit.getRelID( lists[lists.length-1] ); 
			
			if( relID.equals("0") ) continue;
			
			beg1  = Integer.valueOf(lists[0]);
			end1  = Integer.valueOf(lists[1]);
			
			beg2  = Integer.valueOf(lists[2]);
			end2  = Integer.valueOf(lists[3]);
			
			expNum = Integer.valueOf(lists[4]);
			impNum = Integer.valueOf(lists[5]);
			
			//�ж���ʽ��ϵ�Ķ���
			if(expNum == 0 && impNum == 0) 
				this.nullNum = this.nullNum + 1;
			
			//if(impNum == 0) continue;
			if(expNum == 0) continue;
			
			//�ж���������֮��Ĺ�ϵ,�жϵ������ǵ�һ�����ӵĽ����͵ڶ������ӵĿ�ʼ�Ĳ��
			if( Math.abs(end1- beg2) < NearSent.EPS )
			{
				this.nearNum = this.nearNum + 1;
			}
			else
			{
				this.crosNum = this.crosNum + 1;
				
				if(this.debug)
				{
					System.out.println(fPath);
					System.out.println(line);
				}
			}
		}
		
		br.close();
		
		return;
	}
	
	public int getNearNum(){ return this.nearNum; }
	public int getCrosNum(){ return this.crosNum; }
	public int getNullNum(){ return this.nullNum; }
	
	public static void main(String[] args)
	{
		NearSent test = new NearSent();
		
		String ending = ".p2";
		//String dir = "F:\\TestData";
		String dir = "F:\\check_pub_guo_only";
		
		test.run(dir, ending);
		System.out.println(ending);
		System.out.println("Near: " + test.getNearNum());
		System.out.println("Cros: " + test.getCrosNum());
		System.out.println("Null: " + test.getNullNum());
		
		ending = ".p3";
		test.run(dir, ending);
		System.out.println("\n"+ending);
		System.out.println("Near: " + test.getNearNum());
		System.out.println("Cros: " + test.getCrosNum());
		System.out.println("Null: " + test.getNullNum());
	}
}
