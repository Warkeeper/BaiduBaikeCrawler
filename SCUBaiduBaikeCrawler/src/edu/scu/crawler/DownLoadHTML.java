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
 * @author �����졢���
 * ����HTML��
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
	 * @param m_timeOut ��ʱʱ��ms Ĭ��5000
	 * @param m_referer httpͷ����Ϣ Ĭ�������ַ���
	 * @param m_isUseHttps �Ƿ���httpsЭ�� Ĭ�Ͽ���
	 * @param m_isUseJs �Ƿ���JS Ĭ�Ϲر�
	 * @param m_outputHeaderInfo ��Client�Ƿ�Ҫ�����Ӧͷ����Ϣ
	 * @param m_proxy ������Ϣ
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
		//��Client��������
		this.client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		this.client.getOptions().setJavaScriptEnabled(isUseJs);
		this.client.getOptions().setCssEnabled(false);
		this.client.getOptions().setThrowExceptionOnScriptError(false);
		this.client.getOptions().setTimeout(timeOut);
		this.client.getOptions().setAppletEnabled(true);
		this.client.getOptions().setGeolocationEnabled(true);
		this.client.getOptions().setRedirectEnabled(true);
		
		// ���д����������HTTPS��վ
		this.client.getOptions().setUseInsecureSSL(isUseHttps);
		
		if (referer != null && !"".equals(referer)) {
			this.client.addRequestHeader("Referer", referer);
		}
		//���ô���
		if (proxy != null) {
			ProxyConfig proxyConfig = new ProxyConfig(proxy.getIp(),proxy.getPort());
			this.client.getOptions().setProxyConfig(proxyConfig);
		}
		else {
			//System.out.println("Proxy null.");
		}
	}
	//����html
	public String downloadHtml(String url)
	{
		try 
		{
			String html="";
			
			Page page = client.getPage(url);
			WebResponse response = page.getWebResponse();
			if (outputHeaderInfo) 
			{
				// ���httpͷ����Ϣ
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
					ProxyPool.addInvalidProxy(proxy);//�����쳣�������Ǵ��������⣬�ʼ���ʧЧ������У��ȴ�����ؽ�ʧЧ����Ӵ������ȥ��
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
		
		
	


