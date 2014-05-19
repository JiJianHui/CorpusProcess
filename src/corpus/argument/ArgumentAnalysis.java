package corpus.argument;

import corpus.relation.Relation;
import corpus.word.SingleWord;
import corpus.word.Word;
import org.dom4j.*;
import util.Toolkit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Ji JianHui
 * Time: 2013-12-18 19:37
 * Email: jhji@ir.hit.edu.cn
 */
public class ArgumentAnalysis
{
    private String xmlCorpusDir;
    private Vector<String> p1Files;
    private Vector<String> p2Files;
    private Vector<String> p3Files;

    private ArrayList<Relation> p1Relations;
    private ArrayList<Relation> p2Relations;
    private ArrayList<Relation> p3Relations;

    public static int EPS = 4;

    private int arg_word_arg;
    private int word_arg_arg;
    private int arg_arg_word;

    public ArgumentAnalysis(String xmlCorpusDir)
    {
        this.xmlCorpusDir = xmlCorpusDir;

        this.p1Files = new Vector<String>();
        this.p2Files = new Vector<String>();
        this.p3Files = new Vector<String>();

        this.p1Relations = new ArrayList<Relation>();
        this.p2Relations = new ArrayList<Relation>();
        this.p3Relations = new ArrayList<Relation>();
    }

    public void run()
    {
        Toolkit.getFiles(xmlCorpusDir, this.p1Files, ".p1");
        Toolkit.getFiles(xmlCorpusDir, this.p2Files, ".p2");
        Toolkit.getFiles(xmlCorpusDir, this.p3Files, ".p3");

        for(String fPath:this.p3Files)
        {
            System.out.println(fPath);
            this.extractInfo(fPath, this.p3Relations);
        }
        for(String fPath:this.p2Files) this.extractInfo(fPath, this.p2Relations);
        for(String fPath:this.p1Files) this.extractInfo(fPath, this.p1Relations);

        this.analysisArgument(this.p3Relations);

    }

    public void extractInfo(String fPath, ArrayList<Relation> relations)
    {
        Document domObj   = Toolkit.parseXMLToDOM(fPath);
        Element  rootNode = domObj.getRootElement();

        //doc节点下面是一些列的sense节点
        for(Iterator ite = rootNode.elementIterator(); ite.hasNext(); )
        {
            Element curSenseNode = (Element) ite.next();

            //获取sense信息
            String type       = curSenseNode.attribute("type").getText();
            String relID      = curSenseNode.attribute("RelNO").getText();
            String relContent = curSenseNode.attribute("content").getText();

            int relType = 0;
            if( type.equalsIgnoreCase("Explicit") ) relType = 1;

            //获取source信息
            String source = curSenseNode.element("Source").getText();

            //获取word信息
            Element wordElement = curSenseNode.element("Connectives");
            String  wordSpan    = wordElement.element("Span").getText();
            String  wordContent = wordElement.element("Content").getText();

            if( wordSpan.indexOf(";") != -1 ) continue;
            if(wordContent.equalsIgnoreCase("Null")) continue;

            Word word = new SingleWord(wordSpan, wordContent);

            //获取Argument信息
            Element arg1Element = curSenseNode.element("Arg1");
            Element arg2Element = curSenseNode.element("Arg2");

            String arg1Span    = arg1Element.element("Span").getText();
            String arg1Content = arg1Element.element("Content").getText();

            String arg2Span    = arg2Element.element("Span").getText();
            String arg2Content = arg2Element.element("Content").getText();

            Argument arg1 = new Argument(arg1Span, arg1Content);
            Argument arg2 = new Argument(arg2Span, arg2Content);

            String annotation = curSenseNode.element("Annotation").getText();

            Relation relation = new Relation(relType, relID, relContent);

            relation.setWord(word);
            relation.setRawText(source);
            relation.setArg1(arg1);
            relation.setArg2(arg2);
            relation.setAnnotation(annotation);

            relations.add(relation);
        }
    }

    public void analysisArgument(ArrayList<Relation> relations)
    {
        int total, word_arg_arg = 0, arg_word_arg = 0, arg_arg_word = 0;

        for(Relation curRel:relations)
        {
            if( curRel.getType() == 1 ) continue;

            int wBeg    = curRel.getWord().getwBeg();
            int arg1Beg = curRel.getArg1().getArgBeg();
            int arg1End = curRel.getArg1().getArgEnd();
            int arg2Beg = curRel.getArg2().getArgBeg();
            int arg2End = curRel.getArg2().getArgEnd();

            if(wBeg >= (arg2End + arg2Beg)/2)
            {
                arg_arg_word++;
            }
            else if( wBeg > (arg1Beg+arg1End)/2 )
            {
                arg_word_arg++;
            }else
            {
                word_arg_arg++;
            }
        }
        total = word_arg_arg + arg_word_arg + arg_arg_word;

        System.out.println( "Total: " + total );
        System.out.println( "word_arg_arg: " + word_arg_arg + " " + word_arg_arg*1.0/total + "%" );
        System.out.println( "arg_word_arg: " + arg_word_arg + " " + arg_word_arg*1.0/total + "%" );
        System.out.println( "arg_arg_word: " + arg_arg_word + " " + arg_arg_word*1.0/total + "%" );

    }

    public static void main(String[] args)
    {
        String xmlCorpusDir  = "F:\\Distribution Data\\Distribution Data HIT\\Corpus Data\\XML";
        //String xmlCorpusDir  =  "F:\\Distribution Data\\Distribution Data HIT\\Test";
        ArgumentAnalysis ana = new ArgumentAnalysis(xmlCorpusDir);

        ana.run();
    }
}
