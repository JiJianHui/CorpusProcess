package corpus.word;

/**
 * Created with IntelliJ IDEA.
 * User: Ji JianHui
 * Time: 2013-12-18 21:35
 * Email: jhji@ir.hit.edu.cn
 */
public class SingleWord implements Word
{
    private int wBeg;
    private int wEnd;
    private String wSpan;
    private String wContent;

    public SingleWord(String span, String content)
    {
        this.wSpan    = span;
        this.wContent = content;

        String[] lists = this.wSpan.split("\\.\\.\\.");

        if( wContent.equalsIgnoreCase("NULL") ) return;

        if(lists.length > 1)
        {
            wBeg = Integer.valueOf(lists[0]);
            wEnd = Integer.valueOf(lists[1]);
        }
        else
        {
            wBeg = Integer.valueOf(lists[0]);
            wEnd = Integer.valueOf(lists[0]);
        }
    }

    public int getwBeg() {
        return wBeg;
    }
    public void setwBeg(int wBeg) {
        this.wBeg = wBeg;
    }

    public int getwEnd() {
        return wEnd;
    }

    public void setwEnd(int wEnd) {
        this.wEnd = wEnd;
    }

    @Override
    public String getWordSpan() {
        return this.wSpan;
    }
    @Override
    public String getWordContent() {
        return this.wContent;
    }
}
