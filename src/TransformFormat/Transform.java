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
 * Ϊ�˷ַ����ϣ���Ҫ�����ϵĸ�ʽ����ת��������˼���ǰ���PDTB���ϵĸ�ʽ���зַ�
 * ��Ҫ����ע���ϸ�ʽ���ж���ת��������Ҫ�ṩ���ָ�ʽ�����ϣ�.txt .xml
 * ����ת���ĸ�ʽΪ�����µ��ļ������洴����Ӧ�ļ���·����
 * 
 * ���ļ������˼򵥵Ĵ���������Ϊ���ӵĴ������ϲ㴦��
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Oct 24, 2013
 */
public class Transform
{
	/**ԭʼ��ע���ϵĸ���ַ**/
	private String corpusRoot;
	
	/**Distribution Corpus �ĸ�Ŀ¼**/
	private String distributeRoot;
	
	/**Distribution��txt��ʽ���ݴ��Ŀ¼**/
	private String disTXTRoot;
	
	/**Distribution��xml��ʽ���ݴ��Ŀ¼**/
	private String disXMLRoot;
	
	/**��ű�ע���ϵ�����·��**/
	private Vector<String> corFiles;
	
	/**������Ҫɸѡ�����ļ��б�**/
	private Vector<String> delFiles;
	
	private String delPath;
	private String pubDelPath;
	
	/**���д�����ļ�����**/
	private int allFileNum;  
	
	private int impRelNum;
	private int expRelNum;
	private int relNumWithout;
	
	/**���ƾ��ȣ���Ҫ�ǿ��ǵ��ֹ���ע�Ĳ�׼ȷ��**/
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
	 * ���ȸ���corpus��·��������Ӧ���ļ���·��
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
			//������Ŀ¼��Ӱ��
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
	 * ��ȡ�Ѿ�ȷ��ɾ�����ļ�
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
	 * ��ָ�����ļ�ת����ʽ�ɷ��������ϸ�ʽ�������б���
	 * ���Ȼ�ȡ�ļ���·����Ȼ�󹹽���Ӧ��txt,xml�ļ��кͶ�Ӧ���ļ�
	 * @param filePath
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public void transformFile(String filePath) throws IOException, DocumentException
	{
		filePath = this.corpusRoot + filePath;
		//System.out.println(filePath);
		
		this.allFileNum++;
		
		//ԭʼ����λ�ú������Լ���ע���ϵ�ԭʼ���·����
		File   corpFile    = new File(filePath);
		
		String rawPath     = filePath.substring(0, filePath.lastIndexOf('.')) + ".txt";
		String rawText     = Toolkit.readFileToString(rawPath).replace("\r\n", "\n");
		
		String fName       = corpFile.getName();
		String relativeDir = corpFile.getParentFile().getAbsolutePath();
		
		relativeDir        = relativeDir.substring( this.corpusRoot.length() );
		
		
		//�ַ�txt��ʽ���ϴ�ŵ��ļ��к��ļ���
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
		
		
		//��������ļ���·��֮�󣬽��ļ�����ת��,���ת����Ľ��
		Vector<String> annotaLines = new Vector<String>();
		Toolkit.readFileToLines(filePath, annotaLines);
		
		FileWriter txtWriter = new FileWriter(disTxtName);
		FileWriter xmlWriter = new FileWriter(disXmlName);
		
		String[]   result    = new String[2];
		String     txtResult = null;
		String     xmlResult = null;
		
		xmlWriter.write("<?xml version=\"1.0\" encoding=\"gb2312\" ?>\r\n");
		xmlWriter.write("<doc>\r\n");
		
		/**�Ѿ�������ı�ע�У���Ҫ������ͬһ����ע�ļ���ɾ���ظ���ע��**/
		Vector<String> parsedLines = new Vector<String>();
		
		//��ÿһ����ע�ж������޸�
		for( String line:annotaLines )
		{
			//�ж��Ƿ����ظ���
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
		
		//��txt raw����ԭ��������ȥ
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
	 * ��Ա�ע��ÿһ�н��д���ÿһ�д�����һ����ϵ����˿��Զ�������
	 * ���ظñ�ע��ת����ʽ��Ľ����fPathΪp1,p2,p3
	 * @param rawText
	 * @param line
	 */
	public String[] parseLineToDisFormat(String fPath, String rawText, String line)
	{
        String xmlResult   = "";	//�����xml��ʽ
        String txtResult   = "";	//�����txt��ʽ
        String[] result    = new String[2];	//0����txt��ʽ��1����xml��ʽ

		//-----------------------��ȡ������������Ϣ-------------------------
		
		String source      = "";	//sourceΪ������Text Unit
		String labeledStr  = "";	//labeledStr��Ϊ��ע���Text Unit
		
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

		//��ȡarg1 �� arg2������Ϣ
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

        //�þ��������е�ʵ��λ��.
        int lineBeg  = (arg1Beg < arg2Beg) ? arg1Beg : arg2Beg;
        int lineEnd  = (arg1End > arg2End) ? arg1End : arg2End;


		//��ȡ���б�ע�еĹ�������Ϣ
		int expNum   = Integer.valueOf(lists[4]);
		int impNum   = Integer.valueOf(lists[5]);

        //�жϸ��б�ע�Ƿ�Ϸ�,ע�⵽��ʽ����ʽ�Ĺ����ʸ�����ͬ�����ǳ��ȵı仯ȴ��һ����.
        //662 683 685 703 0 1 686 ������� 1 4-3-1����˵��
        int optLength = 6 + 2*(expNum + impNum) + 2;

        if( expNum != 0  && impNum != 0 ) return null;
        if( lists.length != optLength )   return null;

		int i = 0, j = 0;
        //-----------------------------------------------------------------
        //��ȡ��ʽ��������Ϣ
        int wordBeg  = 0, wordEnd = 0;	//�������������е�λ��.
		if( expNum > 0 )
		{
			//��ȡ��ʽ�����ʵı߽緶Χ
    		for(i = 0; i < expNum; i++)
    		{
    			wordBeg = Integer.valueOf( lists[6 + i*2] );
    			wordEnd = Integer.valueOf( lists[7 + i*2] );
    			
    			//ȥ�������λ�õĹ�����
    			if(wordBeg > wordEnd)	continue;
    			if(wordBeg < lineBeg - 2*EPS || wordBeg > lineEnd + 2*EPS)  continue;
    			if(wordEnd < lineBeg - 2*EPS || wordEnd > lineEnd + 2*EPS)  continue;
    			
    			//expWord     += Toolkit.formatWord(rawText.substring(wordBeg, wordEnd)) + ";";
    			expWordSpan += String.valueOf(wordBeg) + "..." + String.valueOf(wordEnd) + ";";
    		}
    		
    		//������ط���span��word�����޸ı�ע����Ľ�������������
    		if( expWordSpan.length() > 1 )
    		{
    			//�����߽�����span
    			expWordSpan = expWordSpan.substring(0, expWordSpan.length() - 1);
    			expWordSpan = Toolkit.formatExpWordSpan(expWordSpan);
    			
    			//��ȡ��ʽ��ʽ������
    			String[] temp = expWordSpan.split(";");
    			
    			for(int m = 0; m < temp.length; m++)
    			{
    				String[] pos = temp[m].replace("...", "#").split("#");
    				
    				wordBeg  = Integer.valueOf(pos[0]);
    				wordEnd  = Integer.valueOf(pos[1]);
    				String w = Toolkit.formatWord( rawText.substring(wordBeg, wordEnd) );
    				
    				if(w.length() == 0)	temp[m] = "";	//����ָʾword�����span
    				else expWord += w + ";";
    			}
    			
    			expWordSpan = "";
    			
    			for(i = 0; i < temp.length; i++)
                {
    				if(temp[i].length() > 1) expWordSpan += temp[i] + ";" ;
    			}

                //���ջ�ȡ�ĵ��Ĺ����ʵĽ��
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
                //����ʽ�����ʸ�������0������³�����wordSpanΪ�յ������
                return null;
            }
		}	

        //---------------------------------------------------------------------
		//��ȡ��ʽ������
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
				//������ʽ������
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
		
//		//����expNum��impNum��ֵ
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
		
		//�жϸ��б�ע�Ƿ�Ϸ�������Ƿ����
		if(expNum == 0 && impNum == 0)
		{
			//������ʽ��ϵ����
			impNum      = 1;
			impWord     = "NULL";
			impWordSpan = "NULL";
		}
		
		//��ȡ��ϵ��Ϣ, ��ԭʼ��ע�н����޸ģ���Ҫ�ǽ���ϵ����ӳ��
		String oldID      = Toolkit.getRelID( lists[lists.length - 1] );
		String relID      = Toolkit.convertOldRelIDToNew(oldID);
		String relContent = Toolkit.newRelName[Toolkit.getRelIDIndex(relID)];

        //4-1Ϊ�°汾�ĳнӹ�ϵ
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

		//��ȡrawText��Ϣ
		/**
		if( expNum > 0 ){
			source = arg1Content + " " + arg2Content; //��û�����Ƿ��Ѿ����������ʣ�����
		}
		else{
			//source = arg1Content + " {implicit = " + impWord + "} " + arg2Content;

			//impWord�Ĳ���λ�û��д�ϸ��
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
			return null;	//�����Ƿ�����
		}
		
		//��ȡ��ע����ı����ݣ���Ҫ��ʾ���������ʺͲ������ص���ѡ��implicit word�Ĳ���λ��
		//if(impWord.indexOf(";") != -1) System.out.println(fPath);
		
		//��ע������
		String   arg1Labeled  = arg1Content;
		String   arg2Labeled  = arg2Content;
		
		if( impWord.length() > 0 && !impWord.equalsIgnoreCase("NULL") )
		{
			String[] impWordLists = impWord.split(";");
			String[] impSpanLists = impWordSpan.split(";");
			
			//������Ϊ����Ԫ�ض����µ�
			for( i = 0; i < impSpanLists.length; i++)
			{
				//�жϸù�����λ��arg1 or arg2
				int wordpos = Integer.valueOf(impSpanLists[i]);
				
				//�޸�arg1������labeled �ı�,�󲿷ֵ�word��λ�ھ��ף���������ж��Ƿ�λ�ھ���
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
		
		//��עarg1��arg2
		int index = labeledStr.lastIndexOf("[");
		if(index == -1) return null;
		//System.out.println("\n###"+labeledStr);
		String tempLabeled = "{" + labeledStr.substring(0, index) + "}ARG1 ";
		tempLabeled += "{" + labeledStr.substring(index) + "}ARG2";
		
		//-------------------------����Txt��ʽ---------------------------------
		
		//������Ը��б�ע�ļ����¸�ʽ�ַ���
		txtResult += "--------------------------------------------------\r\n";
		
		//������ʽ��ʽ����ǩ
		if( expNum > 0 )
			txtResult += "____Explicit____\r\n";
		else
			txtResult += "____Implicit____\r\n";
		
		txtResult += "Source: " + source + "\r\n";
		txtResult += "Labeled: " + tempLabeled + "\r\n";
		
		//���ɹ����ʿ���Ϣ
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
		
		//����Arg��Ϣ
		txtResult += "____Arg1____\r\n";
		txtResult += "Span:" + arg1Span + "\r\n";
		txtResult += "Content:" + arg1Content + "\r\n";
		
		txtResult += "____Arg2____\r\n";
		txtResult += "Span:" + arg2Span + "\r\n";
		txtResult += "Content:" + arg2Content + "\r\n";
		
		//���ɹ�ϵ��Ϣ
		txtResult += "____Sense____\r\n";
		txtResult += "RelNO:" + relID + "\r\n";
		txtResult += "Content:" + relContent + "\r\n";
		
		//���ɱ�ע��Ϣ
		txtResult += "____Annotation____\r\n";
		txtResult += "All:" + annotation;
		
		
		//------------------------����X M L��ʽ����------------------
		//����XML�ĵ���ʽ

		//���ɵ�һ�б�ǩ
		xmlResult   = "";
		String temp = "<Sense ";

		if( expNum > 0) temp += "type=\"Explicit\"";
		else            temp += "type=\"Implicit\"";

		temp += " RelNO=\"" + relID + "\" content=\""+ relContent + "\">\r\n";

		xmlResult += temp;

		//����source��ǩ
		temp = "\t<Source>" + source + "</Source>\r\n";
		temp += "\t<Labeled>" + tempLabeled + "</Labeled>\r\n";

		xmlResult += temp;

		//���ɹ�����dom�ڵ�
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

		//����arg1 dom�ڵ�
		temp  = "\t<Arg1>\r\n";
		temp += "\t\t<Span>" + arg1Span + "</Span>\r\n";
		temp += "\t\t<Content>" + arg1Content + "</Content>\r\n";
		temp += "\t</Arg1>\r\n";

		xmlResult += temp;

		//����arg2 dom �ڵ�
		temp  = "\t<Arg2>\r\n";
		temp += "\t\t<Span>" + arg2Span + "</Span>\r\n";
		temp += "\t\t<Content>" + arg2Content + "</Content>\r\n";
		temp += "\t</Arg2>\r\n";

		xmlResult += temp;

		//����Annation dom�ڵ�
		temp = "\t<Annotation>" + annotation + "</Annotation>\r\n";

		xmlResult += temp;

		//��������sense�ڵ�
		temp = "</Sense>";

		xmlResult += temp;
		
		//���ĸ�ֵ
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
		
		System.out.println("Exp Rel��" + String.valueOf(trans.getExpRelNum()) );
		System.out.println("Imp Rel: " + String.valueOf(trans.getImpRelNum()) );
		System.out.println("All rel without: " + String.valueOf( trans.getRelNum() ));
	}
}
