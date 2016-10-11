package me.savant.data;

public class ResultElement
{
	String header;
	String link;
	
	public ResultElement(String header, String link)
	{
		this.header = header;
		this.link = link;
	}
	
	public String getHeader()
	{
		return header;
	}
	
	public String getLink()
	{
		return link;
	}
}
