package DiscourseRelation;

import java.util.HashMap;
import java.util.Vector;
import util.Words;

/**
 * 中文句间关系处理的主程序，负责调度和统一句间关系处理的整个流程
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Sep 5, 2013
 */
public class DiscourseRelation
{
	/**关联词词表，代表了关联词能够指示句间关系的概率*/
	public static HashMap<String, Float> wordProbablity;
	
	/**关联词词表，里面包含了显隐式关系以及对应的数目*/
	public static Vector<Words> words;
	
	
	
}
