package corpus.word;

/**
 * singleWord和ParallelWord的接口
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Nov 3, 2013
 */
public interface Word
{
    public int getwBeg();
    public int getwEnd();

    public String getWordSpan();
    public String getWordContent();
}
