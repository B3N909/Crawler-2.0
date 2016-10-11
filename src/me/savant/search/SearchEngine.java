package me.savant.search;

import java.util.HashMap;
import java.util.Map;

import me.savant.data.ResultElement;
import me.savant.userinterface.EngineType;
import me.savant.userinterface.Program;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public abstract class SearchEngine
{
	public String url;
	public EngineType type;
	public WebClient wc;
	public int pageIndex = 1;
	public int position = 1;
	public boolean isActive = true;
	public Map<Integer, ResultElement> data = new HashMap<Integer, ResultElement>();
	
	public SearchEngine(String url, EngineType type)
	{
		this.url = url;
		this.type = type;
		
		reset();
	}
	
	public void reset()
	{
		pageIndex = 1;
		position = 1;
		
		if(wc != null)
			wc.close();
		wc = new WebClient(BrowserVersion.FIREFOX_38);
		
		/** LOGGING **/
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
				
		/** WEB CLIENT SETTINGS **/
		wc.setAjaxController(new NicelyResynchronizingAjaxController());
		wc.waitForBackgroundJavaScript(10000);
		wc.waitForBackgroundJavaScriptStartingBefore(10000);
		wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
		wc.getOptions().setCssEnabled(true);
		wc.getOptions().setRedirectEnabled(true);
		wc.getOptions().setThrowExceptionOnScriptError(false);
		wc.getOptions().setRedirectEnabled(true);
		
		/** COOKIES **/
		CookieManager cookieManager = new CookieManager();
		cookieManager = wc.getCookieManager();
		cookieManager.setCookiesEnabled(true);
	}
	
	public boolean hasNext()
	{
		if(Program.CONTINOUSLY_LOAD)
		{
			if(pageIndex < Program.LOAD_AMOUNT)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	public abstract void query(String query);
	public abstract void next(HtmlPage startingPage);
	public abstract void updateLinks(HtmlPage page);
}
