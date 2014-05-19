package util;

public class Sentence
{
	private long    docID;
	private Integer sentID;
	
	private int     end;	//句子的开始
	private int     beg;
	
	private String  content;
	
	public Sentence(long docID, int sentID, int beg, int end)
	{
		this.docID   = docID;
		this.sentID  = sentID;
		
		this.beg     = beg;
		this.end     = end;
		
		this.content = null;
	}
	
	public long getDocID(){ return this.docID; }
	public Integer getSentID(){ return this.sentID; }
	
	public int getBeg(){ return this.beg; }
	public int getEnd(){ return this.end; }
	
	public String getContent(){ return this.content; }
}
