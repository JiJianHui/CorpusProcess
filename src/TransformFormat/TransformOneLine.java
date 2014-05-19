package TransformFormat;

import util.Toolkit;

/**
 * Created with IntelliJ IDEA.
 * User: Ji JianHui
 * Time: 2013-12-16 09:03
 * Email: jhji@ir.hit.edu.cn
 */
public class TransformOneLine
{
    String rawText;
    String fPath;
    String rawAnnoteLine;

    String[] lists;

    int    arg1Beg;    //获取arg1 和 arg2参数信息
    int    arg1End;
    int    arg2Beg;
    int    arg2End;

    int    lineBeg;    //该句子在文中的实际位置.
    int    lineEnd;
    int    expNum;     //获取本行标注中的关联词信息
    int    impNum;

    String source;	//source为完整的Text Unit
    String labeledStr;	//labeledStr，为标注后的Text Unit

    String expWord;
    String expWordSpan;

    String impWord;
    String impWordSpan;

    String arg1Span;
    String arg1Content;

    String arg2Span;
    String arg2Content;

    String relID;
    String relContent;
    String annotation;

    int type;

    private static int EPS = 2; /**控制精度，主要是考虑到手工标注的不准确性**/

    public TransformOneLine(String fPath, String rawText, String annoteLine)
    {
        this.fPath         = fPath;
        this.rawText       = rawText;
        this.rawAnnoteLine = annoteLine.trim();

        if( fPath.endsWith(".p1") ) type = 1;
        if( fPath.endsWith(".p2") ) type = 2;
        if( fPath.endsWith(".p3") ) type = 3;

        lists = this.rawAnnoteLine.split(" ");
    }

    public boolean run()
    {
        boolean result = this.getBasicInformation();

        if( !result ) return false;

        //expNum和impNum同时大于0的情况当做非法处理
        if(expNum > 0 )
        {
            result = this.getExpWord();
            if( !result ) return false;
        }
        else if(impNum > 0)
        {
            result = this.getImpWord();
            if( !result ) return false;
        }
        else
        {
            //expNum和impNum同时为0，当做隐式关系处理
            impNum      = 1;
            impWord     = "NULL";
            impWordSpan = "NULL";
        }
        return true;
    }

    private boolean getBasicInformation()
    {
        if( lists.length < 6 )  return false;

        //获取arg1 和 arg2参数信息
        arg1Beg     = Integer.valueOf(lists[0]);
        arg1End     = Integer.valueOf(lists[1]);

        arg2Beg     = Integer.valueOf(lists[2]);
        arg2End     = Integer.valueOf(lists[3]);

        arg1Span    = lists[0] + "..." + lists[1];
        arg2Span    = lists[2] + "..." + lists[3];

        if( arg1Beg > arg1End || arg2Beg > arg2End ) return false;

        arg1Content = rawText.substring(arg1Beg, arg1End).replace("\n","");
        arg2Content = rawText.substring(arg2Beg, arg2End).replace("\n","");

        if(arg1Content.length() == 0 || arg2Content.length() == 0) return false;

        lineBeg     = (arg1Beg < arg2Beg) ? arg1Beg : arg2Beg;
        lineEnd     = (arg1End > arg2End) ? arg1End : arg2End;

        //判断该行标注是否合法,注意到显式和隐式的关联词个数不同，但是长度的变化却是一样的.
        //662 683 685 703 0 1 686 具体而言 1 4-3-1解释说明
        expNum  = Integer.valueOf(lists[4]);
        impNum  = Integer.valueOf(lists[5]);

        if( expNum != 0  && impNum != 0 ) return false;
        if( lists.length != (6 + 2*(expNum + impNum) + 2) )   return false;


        //获取关关系信息
        String oldID = Toolkit.getRelID( lists[lists.length - 1] );
        relID        = Toolkit.convertOldRelIDToNew(oldID);

        if( relID == null )
        {
            System.err.println("Error:This old sense type cannot recognized!");
            System.err.println(fPath + "\n" + rawAnnoteLine);
            return false;
        }
        relContent   = Toolkit.newRelName[Toolkit.getRelIDIndex(relID)];

        //生成新的标注信息
        annotation = "";
        for(int i = 0; i < lists.length - 1; i++)
        {
            annotation = annotation + lists[i] + " ";
        }
        annotation = annotation + relID + relContent;

        source     = rawText.substring(lineBeg, lineEnd);

        return true;
    }

    /**
     * 获取显式关联词信息，返回值代表了获取的结果
     * @return
     */
    private boolean getExpWord()
    {
        int i = 0, j = 0;
        int wordBeg  = 0, wordEnd = 0;	//关联词在文章中的位置.

        if(expNum == 0) return false;

        expWord = ""; expWordSpan = "";

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
            if( expWord.length() > 1 )
            {
                expWord     = expWord.substring( 0, expWord.length()-1 );
                expWordSpan = expWordSpan.substring(0, expWordSpan.length() - 1);
            }
            else
            {
                return false;
            }
        }
        else
        {
            //在显式关联词个数大于0的情况下出现了wordSpan为空的情况。
            return false;
        }

        return true;
    }

    /**
     * 获取隐式关联词信息
     * @return
     */
    private boolean getImpWord()
    {
        if(impNum == 0 ) return  false;

        impWord = ""; impWordSpan = "";

        for(int j = 0; j < impNum; j++)
        {
            int i = 5 + 2 * expNum + impNum + 1 + j;

            int wordBeg = Integer.valueOf( lists[6 + expNum*2 + j] );

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

            if(impWord.length() == 0 || impWordSpan.length() == 0) return false;
        }
        else
        {
            return false;
        }

        return true;
    }

    public String generateTXT()
    {
        //生成针对该行标注文件的新格式字符串
        String txtResult = "--------------------------------------------------\r\n";

        //生成显式隐式类别标签
        if( expNum > 0 )
            txtResult += "____Explicit____\r\n";
        else
            txtResult += "____Implicit____\r\n";

        txtResult += "Source: " + source + "\r\n";
        txtResult += "Labeled: " + source + "\r\n";

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

        return txtResult;
    }

    public String generateXML()
    {
        String xmlResult = "";
        String temp = "<Sense ";

        if( expNum > 0)
            temp += "type=\"Explicit\"";
        else
            temp += "type=\"Implicit\"";

        temp += " RelNO=\"" + relID + "\" content=\""+ relContent + "\">\r\n";

        xmlResult += temp;

        //生成source标签
        temp = "\t<Source>" + source + "</Source>\r\n";
        temp += "\t<Labeled>" + source + "</Labeled>\r\n";

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

        return xmlResult;
    }
}
