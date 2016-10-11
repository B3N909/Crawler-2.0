package me.savant.userinterface;

import java.io.IOException;
import java.util.List;

import me.savant.data.ResultElement;
import me.savant.search.SearchEngine;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlCitation;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Google extends SearchEngine
{
	public Google()
	{
		super("http://www.google.com", EngineType.GOOGLE);
	}
	
	@Override
	public void query(String query)
	{
		System.out.println("Quering Google (" + url + ")");
		try
		{
			query.replace(" ", "+");
			HtmlPage page = wc.getPage(url + "/search?q=" + query + "&num=40");
			
			updateLinks(page);
			
			if(isActive && hasNext())
			{
				next(page);
			}
		}
		catch (FailingHttpStatusCodeException | IOException e)
		{
			System.out.println("Failed to fetch Google: " + query);
		}
	}

	@Override
	public void next(HtmlPage startingPage)
	{
		pageIndex++;
		DomElement link = startingPage.getElementById("pnnext");
		HtmlPage page = null;
		try
		{
			page = wc.getPage(url + link.getAttribute("href"));
			updateLinks(page);
		}
		catch (FailingHttpStatusCodeException | IOException e)
		{
			e.printStackTrace();
		}
		if(isActive)
		{
			if(page != null)
			{
				if(hasNext())
				{
					next(page);
				}
				else
				{
					isActive = false;
				}
			}
			else
			{
				System.out.println("Stopped: Error fetching the next page");
			}
		}
	}

	@Override
	public void updateLinks(HtmlPage page)
	{
		if(page != null)
		{
			List<?> links = page.getByXPath("//cite");
			
			for(Object o : links)
			{
				HtmlCitation e = (HtmlCitation) o;
				String link = e.getTextContent();
				
				DomNode header = e.getParentNode().getParentNode().getParentNode().getParentNode();
				
				if(!link.contains("google") && !link.contains("javascript") && !link.contains("#") && !link.contains("blogger"))
				{
					data.put(position, new ResultElement(header.asText(), link));
				}
				position++;
			}
			System.out.println("Found Links: " + links.size());
		}
		else
		{
			System.out.println("This page has not yet loaded! (" + url + ") possibly a hierarchy issue.");
		}
	}

}
