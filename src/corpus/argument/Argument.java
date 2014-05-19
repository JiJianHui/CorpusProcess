package corpus.argument;

/**
 * ������Ϣ��װ�࣬��Ҫ��װ��ÿһ������ϵ��Argument
 * @author: rainbow
 * @email : jhji@ir.hit.edu.cn
 * @time  : Nov 3, 2013
 */
public class Argument
{

	private int argBeg;/**Argument�Ŀ�ʼλ��**/
	private int argEnd;/**Argument�Ľ���λ��**/

    private String argSpan;
	private String argContent; /**Argument����������**/

	public Argument(String span, String argContent)
	{
        this.argSpan    = span;
		this.argContent = argContent;

        String[] lists = this.argSpan.split("\\.\\.\\.");

        this.argBeg = Integer.valueOf(lists[0]);
        this.argEnd = Integer.valueOf(lists[1]);
	}

    public int getArgBeg(){
		return argBeg;
	}
    public void setArgBeg(int argBeg) {
        this.argBeg = argBeg;
    }


    public int getArgEnd(){
        return argEnd;
    }
    public void setArgEnd(int argEnd){
        this.argEnd = argEnd;
    }


    public String getArgSpan() {
        return argSpan;
    }
    public void setArgSpan(String argSpan) {
        this.argSpan = argSpan;
    }


    public String getArgContent(){
		return argContent;
	}
    public void setArgContent(String argContent) {
        this.argContent = argContent;
    }
	
	
}
