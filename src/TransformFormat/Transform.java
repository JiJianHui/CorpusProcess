package TransformFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.dom4j.DocumentException;

import util.Toolkit;

/**
 * 为了分发语料，需要将语料的格式进行转换，总体思想是按照PDTB语料的格式进行分发
 * 需要将标注语料格式进行二次转换，共需要提供两种格式的语料：.txt .xml
 * 最终转换的格式为：在新的文件件下面创建对应文件夹路径。
 * 
 * 此文件并做了简单的处理，后续更为复杂的处理交给上层处理
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Oct 24, 2013
 */
public class Transform
{
	/**原始标注语料的根地址**/
	private String corpusRoot;
	
	/**Distribution Corpus 的根目录**/
	private String distributeRoot;
	
	/**Distribution的txt格式数据存放目录**/
	private String disTXTRoot;
	
	/**Distribution的xml格式数据存放目录**/
	private String disXMLRoot;
	
	/**存放标注语料的所有路径**/
	private Vector<String> corFiles;
	
	/**所有需要筛选掉的文件列表**/
	private Vector<String> delFiles;
	
	private String delPath;
	private String pubDelPath;
	
	/**所有处理的文件总数**/
	private int allFileNum;  
	
	private int impRelNum;
	private int expRelNum;
	private int relNumWithout;
	
	/**控制精度，主要是考虑到手工标注的不准确性**/
	private static int EPS = 2;	
	
	public Transform(String corRoot, String disRoot,String delPath, String pDelPath)
	{
		this.corpusRoot     = corRoot;
		this.distributeRoot = disRoot;
		
		this.disTXTRoot     = disRoot + "\\TXT";
		this.disXMLRoot     = disRoot + "\\XML";
		
		this.delPath        = delPath;
		this.pubDelPath     = pDelPath;
		
		this.corFiles       = new Vector<String>();
		this.delFiles       = new Vector<String>();
		
		this.allFileNum     = 0;
		
		this.impRelNum      = 0;
		this.expRelNum      = 0;
		this.relNumWithout  = 0;
	}
	
	/**
	 * 首先根据corpus的路径构建对应的文件夹路径
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public void run() throws IOException, DocumentException
	{
		Toolkit.getFiles(this.corpusRoot, this.corFiles, ".p1");
		
		this.readDeletedFiles(this.delPath);
		this.readDeletedFiles(this.pubDelPath);
		
		int relNum = 0;
		
		for(String p1Path:this.corFiles)
		{
			//消除根目录的影响
			p1Path = p1Path.substring( this.corpusRoot.length() ); 
			
			String txtPath = p1Path.replaceAll(".p1", ".txt");
			
			if( this.delFiles.contains(txtPath) ) continue;
			
			String p2Path  = p1Path.replaceAll(".p1", ".p2");
			String p3Path  = p1Path.replaceAll(".p1", ".p3");
			
			relNum  = Toolkit.countRelNum(this.corpusRoot + p1Path);
			relNum += Toolkit.countRelNum(this.corpusRoot + p2Path);
			relNum += Toolkit.countRelNum(this.corpusRoot + p3Path);
			
			if( relNum < 10 ) continue;
			
			this.transformFile(p1Path);
			this.transformFile(p2Path);
			this.transformFile(p3Path);
			
		}
		System.out.println("ALL File: " + String.valueOf(this.allFileNum) );
	}
	
	/**
	 * 读取已经确定删除的文件
	 * @throws IOException
	 */
	public void readDeletedFiles(String fPath) throws IOException
	{
		String line = null;
		
		//fPath = this.corpusRoot + "\\" + fPath;
		
		BufferedReader br = new BufferedReader(new FileReader(fPath));
		
		while( (line = br.readLine()) != null )
		{
			if(line.indexOf('\\') == -1){
				continue;
			}
			
			line = line.trim();
			
			this.delFiles.add(line);
		}
		
		br.close();
	}
	
	/***
	 * 将指定的文件转换格式成发布的语料格式，并进行保存
	 * 首先获取文件夹路径，然后构建对应的txt,xml文件夹和对应的文件
	 * @param filePath
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public void transformFile(String filePath) throws IOException, DocumentException
	{
		filePath = this.corpusRoot + filePath;
		//System.out.println(filePath);
		
		this.allFileNum++;
		
		//原始语料位置和内容以及标注语料的原始存放路径名
		File   corpFile    = new File(filePath);
		
		String rawPath     = filePath.substring(0, filePath.lastIndexOf('.')) + ".txt";
		String rawText     = Toolkit.readFileToString(rawPath).replace("\r\n", "\n");
		
		String fName       = corpFile.getName();
		String relativeDir = corpFile.getParentFile().getAbsolutePath();
		
		relativeDir        = relativeDir.substring( this.corpusRoot.length() );
		
		
		//分发txt格式语料存放的文件夹和文件名
		String disTxtDir  = this.disTXTRoot + "\\" + relativeDir;
		String disTxtName = disTxtDir + "\\" + fName;
		
		String disXmlDir  = this.disXMLRoot + "\\" + relativeDir;
		String disXmlName = disXmlDir + "\\" + fName;
		
		File   txtDirFile = new File(disTxtDir);
		File   txtFile    = new File(disTxtName);
		
		File   xmlDirFile = new File(disXmlDir);
		File   xmlFile    = new File(disXmlName);
		
		if( !txtDirFile.exists() )   txtDirFile.mkdirs();
		if( !txtFile.exists() )      txtFile.createNewFile();
		
		if( !xmlDirFile.exists() )  xmlDirFile.mkdirs();
		if( !xmlFile.exists() )     xmlFile.createNewFile();
		
		
		//构建完成文件夹路径之后，将文件进行转换,获得转换后的结果
		Vector<String> annotaLines = new Vector<String>();
		Toolkit.readFileToLines(filePath, annotaLines);
		
		FileWriter txtWriter = new FileWriter(disTxtName);
		FileWriter xmlWriter = new FileWriter(disXmlName);
		
		String[]   result    = new String[2];
		String     txtResult = null;
		String     xmlResult = null;
		
		xmlWriter.write("<?xml version=\"1.0\" encoding=\"gb2312\" ?>\r\n");
		xmlWriter.write("<doc>\r\n");
		
		/**已经处理过的标注行，主要用于在同一个标注文件中删除重复标注行**/
		Vector<String> parsedLines = new Vector<String>();
		
		//将每一个标注行都进行修改
		for( String line:annotaLines )
		{
			//判断是否是重复行
			if( parsedLines.contains(line) ) continue;
			
			result = this.parseLineToDisFormat(filePath, rawText, line);
			
			if(result == null ) continue;
			
			txtResult = result[0];
			xmlResult = result[1];
			
			if( txtResult.length() > 1 ){
				txtWriter.write(txtResult+"\r\n\r\n");
			}
			
			if( xmlResult.length() > 1){
				xmlWriter.write(xmlResult + "\r\n\r\n");
			}
			
			parsedLines.add(line);
		}
		
		xmlWriter.write("</doc>");
		
		txtWriter.close();
		xmlWriter.close();
		
		//将txt raw语料原样拷贝过去
		File srcTxtFile = new File(rawPath);
		
		File dstTxtFile = new File( disTxtDir + "\\" + srcTxtFile.getName() );
		File dstXmlFile = new File( disXmlDir + "\\" + srcTxtFile.getName() );
		
		if( !dstTxtFile.exists() ){
			dstTxtFile.createNewFile();
			Toolkit.copyFile(srcTxtFile, dstTxtFile);
		}
		if( !dstXmlFile.exists() ){
			dstXmlFile.createNewFile();
			Toolkit.copyFile(srcTxtFile, dstXmlFile);
		}
		
	}
	
	/**
	 * 针对标注的每一行进行处理，每一行代表了一个关系，因此可以独立处理
	 * 返回该标注行转换格式后的结果。fPath为p1,p2,p3
	 * @param rawText
	 * @param line
	 */
	public String[] parseLineToDisFormat(String fPath, String rawText, String line)
	{
        String xmlResult   = "";	//输出的xml格式
        String txtResult   = "";	//输出的txt格式
        String[] result    = new String[2];	//0保存txt格式，1保存xml格式

		//-----------------------获取基本的数据信息-------------------------
		
		String source      = "";	//source为完整的Text Unit
		String labeledStr  = "";	//labeledStr，为标注后的Text Unit
		
		String expWord     = "", impWord     = "";
		String expWordSpan = "", impWordSpan = "";
		
		String arg1Span    = "", arg2Span    = "";
		String arg1Content = "", arg2Content = "";
		
		int type = 0, max = 0;
		
		if( fPath.endsWith(".p1") ) type = 1;
		if( fPath.endsWith(".p2") ) type = 2;
		if( fPath.endsWith(".p3") ) type = 3;
		
		line = line.trim();
		String[] lists = line.split(" ");
		
		if( lists.length < 6 ) return null;
		
		if(type == 1) 	max = lists.length - 4;
		else 			max = lists.length - 2;

		//获取arg1 和 arg2参数信息
		int arg1Beg  = Integer.valueOf(lists[0]);
		int arg1End  = Integer.valueOf(lists[1]);
        int arg2Beg  = Integer.valueOf(lists[2]);
        int arg2End  = Integer.valueOf(lists[3]);

        if( arg1Beg  >  arg1End ) return null;
        if( arg2Beg  >  arg2End ) return null;

		arg1Span     = lists[0] + "..." + lists[1];
        arg2Span     = lists[2] + "..." + lists[3];
        arg1Content  = rawText.substring(arg1Beg, arg1End).replace("\n","");
		arg2Content  = rawText.substring(arg2Beg, arg2End).replace("\n","");

        if(arg1Content.length() == 0 || arg2Content.length() == 0) return null;

        //该句子在文中的实际位置.
        int lineBeg  = (arg1Beg < arg2Beg) ? arg1Beg : arg2Beg;
        int lineEnd  = (arg1End > arg2End) ? arg1End : arg2End;


		//获取本行标注中的关联词信息
		int expNum   = Integer.valueOf(lists[4]);
		int impNum   = Integer.valueOf(lists[5]);

        //判断该行标注是否合法,注意到显式和隐式的关联词个数不同，但是长度的变化却是一样的.
        //662 683 685 703 0 1 686 具体而言 1 4-3-1解释说明
        int optLength = 6 + 2*(expNum + impNum) + 2;

        if( expNum != 0  && impNum != 0 ) return null;
        if( lists.length != optLength )   return null;

		int i = 0, j = 0;
        //-----------------------------------------------------------------
        //获取显式关联词信息
        int wordBeg  = 0, wordEnd = 0;	//关联词在文章中的位置.
		if( expNum > 0 )
		{
			//获取显式关联词的边界范围
    		for(i = 0; i < expNum; i++)
    		{
    			wordBeg = Integer.valueOf( lists[6 + i*2] );
    			wordEnd = Integer.valueOf( lists[7 + i*2] );
    			
    			//去除错误的位置的关联词
    			if(wordBeg > wordEnd)	continue;
    			if(wordBeg < lineBeg - 2*EPS || wordBeg > lineEnd + 2*EPS)  continue;
    			if(wordEnd < lineBeg - 2*EPS || wordEnd > lineEnd + 2*EPS)  continue;
    			
    			//expWord     += Toolkit.formatWord(rawText.substring(wordBeg, wordEnd)) + ";";
    			expWordSpan += String.valueOf(wordBeg) + "..." + String.valueOf(wordEnd) + ";";
    		}
    		
    		//在这个地方对span和word进行修改标注错误的结果，并完成排序
    		if( expWordSpan.length() > 1 )
    		{
    			//修正边界错误的span
    			expWordSpan = expWordSpan.substring(0, expWordSpan.length() - 1);
    			expWordSpan = Toolkit.formatExpWordSpan(expWordSpan);
    			
    			//获取显式显式关联词
    			String[] temp = expWordSpan.split(";");
    			
    			for(int m = 0; m < temp.length; m++)
    			{
    				String[] pos = temp[m].replace("...", "#").split("#");
    				
    				wordBeg  = Integer.valueOf(pos[0]);
    				wordEnd  = Integer.valueOf(pos[1]);
    				String w = Toolkit.formatWord( rawText.substring(wordBeg, wordEnd) );
    				
    				if(w.length() == 0)	temp[m] = "";	//修正指示word错误的span
    				else expWord += w + ";";
    			}
    			
    			expWordSpan = "";
    			
    			for(i = 0; i < temp.length; i++)
                {
    				if(temp[i].length() > 1) expWordSpan += temp[i] + ";" ;
    			}

                //最终获取的到的关联词的结果
    			if( expWord.length() > 0 )
    			{
    				expWord     = expWord.substring( 0, expWord.length()-1 );
    				expWordSpan = expWordSpan.substring(0, expWordSpan.length() - 1);
    			}
                else
                {
                    return null;
                }
    		}
            else
            {
                //在显式关联词个数大于0的情况下出现了wordSpan为空的情况。
                return null;
            }
		}	

        //---------------------------------------------------------------------
		//获取隐式关联词
		if( impNum > 0 )
		{
			for(j = 0; j < impNum; j++)
			{
				i = 5 + 2 * expNum + impNum + 1 + j;
				
				if(i >= max)	break;
				
				wordBeg = Integer.valueOf( lists[6 + expNum*2 + j] );
				
				if(wordBeg < lineBeg - 2*EPS || wordBeg > lineEnd + 2*EPS ) continue;
				
				impWord     += Toolkit.formatWord( lists[i] ) + ";";
				impWordSpan += String.valueOf( lists[6 + expNum*2 + j] ) + ";";
			}
			
			if(impWord.length() > 1)
			{
				//修正隐式关联词
				impWord     = impWord.substring( 0, impWord.length()-1 );
				impWordSpan = impWordSpan.substring(0, impWordSpan.length() - 1);
				
				String[] re = Toolkit.formatImpWordSpan(impWordSpan, impWord);
				
				impWord     = re[1];
				impWordSpan = re[0];

                if(impWord.length() == 0) return null;
                if(impWordSpan.length() == 0 ) return null;
			}
            else
            {
                return null;
            }
		}
		
//		//修正expNum和impNum的值
//		if(expWord.length() >= 1){
//			expNum = Toolkit.countWordsInLine(expWord, ";") + 1;
//		}
//		else{
//			expNum  = 0;
//			expWord = null;
//			expWordSpan = null;
//		}
//
//		if(impWord.length() >= 1){
//			impNum = Toolkit.countWordsInLine(impWord, ";") + 1;
//		}
//		else{
//			impNum = 0;
//			impWord = "";
//			impWordSpan = "";
//		}
		
		//判断该行标注是否合法并处理非法情况
		if(expNum == 0 && impNum == 0)
		{
			//当做隐式关系处理
			impNum      = 1;
			impWord     = "NULL";
			impWordSpan = "NULL";
		}
		
		//获取关系信息, 将原始标注行进行修改，主要是将关系进行映射
		String oldID      = Toolkit.getRelID( lists[lists.length - 1] );
		String relID      = Toolkit.convertOldRelIDToNew(oldID);
		String relContent = Toolkit.newRelName[Toolkit.getRelIDIndex(relID)];

        //4-1为新版本的承接关系
		if( !relID.equalsIgnoreCase("4-1") )
		{
			if(expNum > 0) this.expRelNum++;
			else this.impRelNum++;
			
			this.relNumWithout++;
		}
		
		String annotation = "";
		
		for(i = 0; i < lists.length - 1; i++)
        {
			annotation = annotation + lists[i] + " ";
        }
		annotation = annotation + relID + relContent;

		//获取rawText信息
		/**
		if( expNum > 0 ){
			source = arg1Content + " " + arg2Content; //还没考虑是否已经包含关联词？？？
		}
		else{
			//source = arg1Content + " {implicit = " + impWord + "} " + arg2Content;

			//impWord的插入位置还有待细化
			if(impWordSpan.length() > 0 && !impWordSpan.equals("NULL") )
			{
				String[] posLists = impWordSpan.split(";");
				i = Integer.valueOf(posLists[0]);

				if( Math.abs(i - arg1Beg) < Math.abs(i - arg2Beg) ) {
					source = "{implicit = " + impWord + "} " + arg1Content +  arg2Content;
				}
				else{
					source = arg1Content + " {implicit = " + impWord + "} " + arg2Content;
				}
			}
			else{
				source = arg1Content + " {implicit = " + impWord + "} " + arg2Content;
			}
		}
		**/
		
		source = rawText.substring(lineBeg, lineEnd);
		
		if( impWord.split(";").length > 2 )
		{
            System.out.println("This file's Line contains more than 3 implicit wordds");
			System.out.println(fPath);
			return null;	//当做非法处理
		}
		
		//获取标注后的文本内容，需要标示出来关联词和参数，重点是选择implicit word的插入位置
		//if(impWord.indexOf(";") != -1) System.out.println(fPath);
		
		//标注关联词
		String   arg1Labeled  = arg1Content;
		String   arg2Labeled  = arg2Content;
		
		if( impWord.length() > 0 && !impWord.equalsIgnoreCase("NULL") )
		{
			String[] impWordLists = impWord.split(";");
			String[] impSpanLists = impWordSpan.split(";");
			
			//记载因为插入元素而导致的
			for( i = 0; i < impSpanLists.length; i++)
			{
				//判断该关联词位于arg1 or arg2
				int wordpos = Integer.valueOf(impSpanLists[i]);
				
				//修改arg1来产生labeled 文本,大部分的word都位于句首，因此首先判断是否位于句首
				if(wordpos > arg1Beg - EPS && wordpos < arg1End){
					if( Math.abs(wordpos - arg1Beg) <= Toolkit.EPS ){
						arg1Labeled = "[implicit = " + impWordLists[i] + "]" + arg1Content;
					}
					else{
						arg1Labeled  = arg1Content.substring(0, wordpos - arg1Beg);
						arg1Labeled += "[implicit = " + impWordLists[i] + "]";
						arg1Labeled += arg1Content.substring(wordpos - arg1Beg);
					}
				}
				if(wordpos > arg2Beg - EPS && wordpos < arg2End){
					if( Math.abs(wordpos - arg2Beg) <= Toolkit.EPS ){
						arg2Labeled = "[implicit = " + impWordLists[i] + "]" + arg2Content;
					}
					else{
						arg2Labeled  = arg2Content.substring(0, wordpos - arg2Beg);
						arg2Labeled += "[implicit = " + impWordLists[i] + "]";
						arg2Labeled += arg2Content.substring(wordpos - arg2Beg);
					}
				}
			}
			
			labeledStr = arg1Labeled;
			if( arg1End < arg2Beg - EPS )
			{
				labeledStr += source.substring(arg1End-arg1Beg, arg2Beg - arg1Beg);
			}
			labeledStr += arg2Labeled;
		}
		else if( impWord.equalsIgnoreCase("NULL") ){
			
			labeledStr = arg1Labeled + "[implicit = NULL]" + arg2Labeled;
		}
		else
		{
			labeledStr = "";
			
			String[] expLists     = expWordSpan.split(";");
			String[] expWordLists = expWord.split(";");
			
			int tempBeg = arg1Beg;
			int tempEnd = arg1End;
			
			for( i = 0; i < expLists.length; i++ )
			{
				String span = expLists[i];
				span = span.replace("...", "#");
				
				String[] tempLists = span.split("#");
				
				wordBeg = Integer.valueOf(tempLists[0]);
				wordEnd = Integer.valueOf(tempLists[1]);
				
				if(tempBeg < wordBeg)
				{
					labeledStr += rawText.substring(tempBeg, wordBeg);
					labeledStr += "[" + expWordLists[i] + "]"; 
					tempBeg = wordEnd + 1;
				}
				else
				{
					labeledStr += "[" + expWordLists[i] + "]";
					labeledStr += rawText.substring(wordBeg,tempBeg);
					tempBeg = tempBeg + 1;
				}
				
				//System.out.println(fPath);
				//System.out.println(String.valueOf(tempBeg) + " " + String.valueOf(wordBeg) );
				//System.out.println(line);
				//System.out.println(arg1Content);
				//System.out.println(arg2Content);
			}
			
			if(tempBeg < arg2End)
			{
				labeledStr += rawText.substring(tempBeg, arg2End);
			}
		}
		
		//标注arg1和arg2
		int index = labeledStr.lastIndexOf("[");
		if(index == -1) return null;
		//System.out.println("\n###"+labeledStr);
		String tempLabeled = "{" + labeledStr.substring(0, index) + "}ARG1 ";
		tempLabeled += "{" + labeledStr.substring(index) + "}ARG2";
		
		//-------------------------生成Txt格式---------------------------------
		
		//生成针对该行标注文件的新格式字符串
		txtResult += "--------------------------------------------------\r\n";
		
		//生成显式隐式类别标签
		if( expNum > 0 )
			txtResult += "____Explicit____\r\n";
		else
			txtResult += "____Implicit____\r\n";
		
		txtResult += "Source: " + source + "\r\n";
		txtResult += "Labeled: " + tempLabeled + "\r\n";
		
		//生成关联词块信息
		txtResult += "____Connective____\r\n";
		
		if(expNum > 0 )  
		{
			txtResult += "Span:" + expWordSpan + "\r\n";
			txtResult += "Content:" + expWord + "\r\n";
		}
		else
		{
			txtResult += "Span:" + impWordSpan + "\r\n";
			txtResult += "Content:" + impWord + "\r\n";
		}
		
		//生成Arg信息
		txtResult += "____Arg1____\r\n";
		txtResult += "Span:" + arg1Span + "\r\n";
		txtResult += "Content:" + arg1Content + "\r\n";
		
		txtResult += "____Arg2____\r\n";
		txtResult += "Span:" + arg2Span + "\r\n";
		txtResult += "Content:" + arg2Content + "\r\n";
		
		//生成关系信息
		txtResult += "____Sense____\r\n";
		txtResult += "RelNO:" + relID + "\r\n";
		txtResult += "Content:" + relContent + "\r\n";
		
		//生成标注信息
		txtResult += "____Annotation____\r\n";
		txtResult += "All:" + annotation;
		
		
		//------------------------生成X M L格式数据------------------
		//生成XML文档格式

		//生成第一行标签
		xmlResult   = "";
		String temp = "<Sense ";

		if( expNum > 0) temp += "type=\"Explicit\"";
		else            temp += "type=\"Implicit\"";

		temp += " RelNO=\"" + relID + "\" content=\""+ relContent + "\">\r\n";

		xmlResult += temp;

		//生成source标签
		temp = "\t<Source>" + source + "</Source>\r\n";
		temp += "\t<Labeled>" + tempLabeled + "</Labeled>\r\n";

		xmlResult += temp;

		//生成关联词dom节点
		temp = "\t<Connectives>\r\n";

		if( expNum > 0 )
		{
			temp += "\t\t<Span>" + expWordSpan + "</Span>\r\n";
			temp += "\t\t<Content>" + expWord + "</Content>\r\n";
		}
		else
		{
			temp += "\t\t<Span>" + impWordSpan + "</Span>\r\n";
			temp += "\t\t<Content>" + impWord + "</Content>\r\n";
		}
		temp += "\t</Connectives>\r\n";

		xmlResult += temp;

		//生成arg1 dom节点
		temp  = "\t<Arg1>\r\n";
		temp += "\t\t<Span>" + arg1Span + "</Span>\r\n";
		temp += "\t\t<Content>" + arg1Content + "</Content>\r\n";
		temp += "\t</Arg1>\r\n";

		xmlResult += temp;

		//生成arg2 dom 节点
		temp  = "\t<Arg2>\r\n";
		temp += "\t\t<Span>" + arg2Span + "</Span>\r\n";
		temp += "\t\t<Content>" + arg2Content + "</Content>\r\n";
		temp += "\t</Arg2>\r\n";

		xmlResult += temp;

		//生成Annation dom节点
		temp = "\t<Annotation>" + annotation + "</Annotation>\r\n";

		xmlResult += temp;

		//生成最终sense节点
		temp = "</Sense>";

		xmlResult += temp;
		
		//最后的赋值
		result[0] = txtResult;
		result[1] = xmlResult;
		
		return result;
	}
	
	public int getRelNum(){ return this.relNumWithout; }
	public int getExpRelNum(){return this.expRelNum;}
	public int getImpRelNum(){return this.impRelNum;}
	
	public static void main(String[] args) throws IOException, DocumentException
	{
		String corpusRoot        = "F:\\Corpus Data\\Corpus_pubGuoOnly";
		String distributeRoot    = "F:\\Distribution Data\\Corpus_pubGuoOnly";
		
		//String corpusRoot     = "F:\\Test\\Corpus Data\\Corpus_pubGuoOnly";
		//String distributeRoot = "F:\\Test\\Distribution Data\\Corpus_pubGuoOnly";
		
		String delPath  = "F:\\Corpus Data\\Corpus_pubGuoOnly\\Need Deleted.txt";
		String pDelPath = "F:\\Corpus Data\\Corpus_pubGuoOnly\\pub Deleted.txt"; 
		
		Transform trans = new Transform(corpusRoot, distributeRoot, delPath, pDelPath);
		
		trans.run();
		
		System.out.println("Exp Rel：" + String.valueOf(trans.getExpRelNum()) );
		System.out.println("Imp Rel: " + String.valueOf(trans.getImpRelNum()) );
		System.out.println("All rel without: " + String.valueOf( trans.getRelNum() ));
	}
}
