package TransformFormat;

import org.dom4j.DocumentException;
import util.Toolkit;

import java.io.*;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Ji JianHui
 * Time: 2013-12-16 11:15
 * Email: jhji@ir.hit.edu.cn
 */
public class TransFormat
{

    private String corpusRoot;      /**原始标注语料的根地址**/

    private String disTXTRoot;      /**Distribution的txt格式数据存放目录**/
    private String disXMLRoot;      /**Distribution的xml格式数据存放目录**/

    private Vector<String> corFiles;    /**存放标注语料的所有路径**/
    private Vector<String> delFiles;    /**所有需要筛选掉的文件列表**/

    private String delPath;
    private String pubDelPath;

    private int allFileNum;         /**所有处理的文件总数**/

    public TransFormat(String corRoot, String disRoot,String delPath, String pDelPath)
    {
        this.corpusRoot     = corRoot;

        this.disTXTRoot     = disRoot + "\\TXT";
        this.disXMLRoot     = disRoot + "\\XML";

        this.delPath        = delPath;
        this.pubDelPath     = pDelPath;

        this.corFiles       = new Vector<String>();
        this.delFiles       = new Vector<String>();

        this.allFileNum     = 0;
    }

    /**
     * 首先根据corpus的路径构建对应的文件夹路径
     * @throws java.io.IOException
     * @throws org.dom4j.DocumentException
     */
    public void run() throws IOException, DocumentException
    {
        Toolkit.getFiles(this.corpusRoot, this.corFiles, ".p1");

        //this.readDeletedFiles(this.delPath);
        //this.readDeletedFiles(this.pubDelPath);

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

    /** 读取已经确定删除的文件*/
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
        File corpFile      = new File(filePath);

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
            parsedLines.add(line);

            TransformOneLine temp = new TransformOneLine(filePath,rawText, line);
            boolean result = temp.run();

            if(!result) continue;

            txtResult = temp.generateTXT();
            xmlResult = temp.generateXML();

            if( txtResult.length() > 1 ){
                txtWriter.write(txtResult+"\r\n\r\n");
            }

            if( xmlResult.length() > 1){
                xmlWriter.write(xmlResult + "\r\n\r\n");
            }
        }

        xmlWriter.write("</doc>");
        txtWriter.close();
        xmlWriter.close();

        //将txt raw语料原样拷贝过去
        File srcTxtFile = new File(rawPath);

        File dstTxtFile = new File( disTxtDir + "\\" + srcTxtFile.getName() );
        File dstXmlFile = new File( disXmlDir + "\\" + srcTxtFile.getName() );

        if( !dstTxtFile.exists() )
        {
            dstTxtFile.createNewFile();
            Toolkit.copyFile(srcTxtFile, dstTxtFile);
        }
        if( !dstXmlFile.exists() )
        {
            dstXmlFile.createNewFile();
            Toolkit.copyFile(srcTxtFile, dstXmlFile);
        }
    }

    public static void main(String[] args) throws IOException, DocumentException
    {
        //String corpusRoot        = "F:\\Corpus Data\\Corpus_pubGuoOnly";
        //String distributeRoot    = "F:\\Distribution Data\\Corpus_pubGuoOnly";

        String corpusRoot     = "F:\\Test\\Corpus Data\\Corpus_pubGuoOnly";
        String distributeRoot = "F:\\Test\\Distribution Data\\Corpus_pubGuoOnly";

        String delPath  = "F:\\Corpus Data\\Corpus_pubGuoOnly\\Need Deleted.txt";
        String pDelPath = "F:\\Corpus Data\\Corpus_pubGuoOnly\\pub Deleted.txt";

        TransFormat trans = new TransFormat(corpusRoot, distributeRoot, delPath, pDelPath);

        trans.run();
    }
}
