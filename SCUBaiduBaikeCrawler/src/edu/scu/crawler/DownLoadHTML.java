package edu.scu.crawler;

import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import edu.scu.proxypool.Proxy;
import edu.scu.proxypool.ProxyPool;


/**
 * @author 王港庆、杨斌
 * 下载HTML类
 */

public class DownLoadHTML {
	private WebClient client;
	private boolean isUseJs=false;
	private int timeOut=5000;//ms
	private boolean isUseHttps=false;
	
	private String referer = "";
	private Proxy proxy;
	
	
	private boolean outputHeaderInfo=false;
	
	public DownLoadHTML()
	{
		
	}
	/**
	 * @author Aaron
	 * @param m_timeOut 超时时间ms 默认5000
	 * @param m_referer http头部信息 默认留空字符串
	 * @param m_isUseHttps 是否开启https协议 默认开启
	 * @param m_isUseJs 是否开启JS 默认关闭
	 * @param m_outputHeaderInfo 该Client是否要输出响应头部信息
	 * @param m_proxy 代理信息
	 */
	public DownLoadHTML(int m_timeOut,String m_referer,boolean m_isUseHttps,boolean m_isUseJs,boolean m_outputHeaderInfo,Proxy m_proxy)
	{
		timeOut=m_timeOut;
		referer=m_referer;
		isUseHttps=m_isUseHttps;
		isUseJs=m_isUseJs;
		outputHeaderInfo=m_outputHeaderInfo;
		proxy=m_proxy;
	}
	public void initDownloadClient()
	{
		BrowserVersion[] versions = { BrowserVersion.CHROME, BrowserVersion.FIREFOX_38, BrowserVersion.INTERNET_EXPLORER_11, BrowserVersion.INTERNET_EXPLORER_8};
		this.client= new WebClient(versions[(int)(versions.length * Math.random())]);
		//对Client进行设置
		this.client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		this.client.getOptions().setJavaScriptEnabled(isUseJs);
		this.client.getOptions().setCssEnabled(false);
		this.client.getOptions().setThrowExceptionOnScriptError(false);
		this.client.getOptions().setTimeout(timeOut);
		this.client.getOptions().setAppletEnabled(true);
		this.client.getOptions().setGeolocationEnabled(true);
		this.client.getOptions().setRedirectEnabled(true);
		
		// 这行代码允许访问HTTPS网站
		this.client.getOptions().setUseInsecureSSL(isUseHttps);
		
		if (referer != null && !"".equals(referer)) {
			this.client.addRequestHeader("Referer", referer);
		}
		//设置代理
		if (proxy != null) {
			ProxyConfig proxyConfig = new ProxyConfig(proxy.getIp(),proxy.getPort());
			this.client.getOptions().setProxyConfig(proxyConfig);
		}
		else {
			//System.out.println("Proxy null.");
		}
	}
	//下载html
	public String downloadHtml(String url)
	{
		try 
		{
			String html="";
			
			Page page = client.getPage(url);
			WebResponse response = page.getWebResponse();
			if (outputHeaderInfo) 
			{
				// 输出http头部信息
				List<NameValuePair> headers = response.getResponseHeaders();
				for (NameValuePair nameValuePair : headers) 
				{
					System.out.println(nameValuePair.getName() + "-->" + nameValuePair.getValue());
				}
			}
			
			if (response.getContentType().equals("application/json")) {
				html = response.getContentAsString();
				//System.out.println("Return Content is Json");
			}else if(page.isHtmlPage()){
				html = ((HtmlPage)page).asXml();
			}
			return html;
		}catch (Exception e) {
			if(CrawlerStart.ifUseProxy)
				{
					ProxyPool.addInvalidProxy(proxy);//出现异常基本都是代理有问题，故加入失效代理队列，等待代理池将失效代理从代理池中去除
					System.err.println("Proxy:"+proxy + " " + e.getMessage());
				}
			else
				System.err.println("Download HTML failed:"+e.getMessage());
		} finally {
			client.close();
		}
		return null;
	}
	
	
}
		
		
	


