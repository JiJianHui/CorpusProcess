package corpus.relation;

import corpus.argument.Argument;
import corpus.word.Word;

/**
 * 每一个句间关系的实例，每一个标注行转换为一个实例
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Nov 3, 2013
 */
public class Relation
{
    private int type;

	private String relID;
	private String relContent;

	private Word word;
    //private String wordSpan;
    //private String wordContent;

	private Argument arg1;
	private Argument arg2;


    private String fPath;
    private String annotation;

    private String rawText;   /**句间关系的实际语料**/
    private String labeledText; /**句间关系的标注后的语料**/


	public Relation(int type, String relID, String relContent)
	{
        this.type       = type;
        this.relID      = relID;
        this.relContent = relContent;
	}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRelID() {
        return relID;
    }

    public void setRelID(String relID) {
        this.relID = relID;
    }

    public String getRelContent() {
        return relContent;
    }

    public void setRelContent(String relContent) {
        this.relContent = relContent;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public Argument getArg1() {
        return arg1;
    }

    public void setArg1(Argument arg1) {
        this.arg1 = arg1;
    }

    public Argument getArg2() {
        return arg2;
    }

    public void setArg2(Argument arg2) {
        this.arg2 = arg2;
    }

    public String getfPath() {
        return fPath;
    }

    public void setfPath(String fPath) {
        this.fPath = fPath;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getLabeledText() {
        return labeledText;
    }

    public void setLabeledText(String labeledText) {
        this.labeledText = labeledText;
    }
}
