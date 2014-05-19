package corpus;
import java.util.Vector;

import util.Relations;
import util.Words;

/**
 * 关联词分析的核心处理程序
 * @author rainbow
 * @time   May 21, 2013
 */
public class CoreParse
{
	private Vector<String>   files;			//所有待处理的句群文件地址列表
	
	private Vector<Words>     words;   		//标注文件中出现的关联词(包括显隐)
	private Vector<Relations> relations;	//标注文件中出现的句间关系(包括显隐)
	
	public CoreParse()
	{
	}
	
	/**
	 * 对ending标注文件进行处理
	 * @param filePath：文件夹路径
	 * @param ending: 需要处理的文件后缀名
	 */
	public void run(String filePath, String ending)
	{
		
	}
}
