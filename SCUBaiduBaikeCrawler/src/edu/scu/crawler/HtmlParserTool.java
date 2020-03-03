package edu.scu.crawler;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import edu.scu.proxypool.ProxyPool;
/**
 * HtmlParserTool HTML��������
 * @author ��Ƽ
 * ������Ҫ����HTML����������html�еı�ǩ���ݷֽ������Ҫ�ı��⡢��顢Ŀ¼��
 */
public class HtmlParserTool {

    public  ArrayList<String> parseLink(String html){
        Document dom =  Jsoup.parse(html);            
        
        Elements paras = dom.getElementsByClass("para");        //��ȡclassΪpara������

        StringBuffer sb = new StringBuffer();                    

        for(Element link : paras){
            sb.append(link);
        }
        

        dom = Jsoup.parse(sb.toString());                           
        paras = dom.select("a[href]");                              //��ȡ����Ϊhref��Ԫ��

        ArrayList<String> links = new ArrayList<>();

        for(Element link:paras){
            links.add("http://baike.baidu.com"+link.attr("href"));  //����url
        }
        return links;
    }

    

    public  String parseTitle(String html){//��ȡ���������

        StringBuffer title = new StringBuffer();
        Document dom = Jsoup.parse(html);
        Elements elements = dom.select("h1");
        for(Element element:elements){
            title.append(element.text());                              
        }
        return title.toString();
    }
    
    public String parseSummary(String html)
    {
    	StringBuffer  summary=new StringBuffer();
    	Document dom = Jsoup.parse(html);
    	Elements elements = dom.getElementsByClass("lemma-summary");//���
    	//Elements elements = dom.getElementsByClass("lemma-summary").get(0).getElementsByClass("para");
    	for(Element element:elements){
    		summary.append(element.text()+"\n");
        }
        return summary.toString();
    }
    public String parseBasicInfo(String html)
    {
    	StringBuffer  basicInfo=new StringBuffer();
    	Document dom = Jsoup.parse(html);
    	Elements elements = dom.getElementsByClass("basicInfo-item");//������Ϣ
    	boolean isName=true;
    	for(Element element:elements){
    		if(isName)
    			basicInfo.append(element.text()+"  ");
    		else
    			basicInfo.append(element.text()+"\n");
    		
    		isName=!isName;
        }
        return basicInfo.toString();
    }
    public String parseIndex(String html)
    {
    	StringBuffer  index=new StringBuffer();
    	Document dom = Jsoup.parse(html);
    	Elements elements = dom.getElementsByClass("lemma-catalog");//Ŀ¼
    	for(Element element:elements){
    		index.append(element.text()+"\n");
        }
        return index.toString();
    }
    public  String parseContext(String html){
        StringBuffer context = new StringBuffer();
        Document dom = Jsoup.parse(html);
        Elements elements = dom.getElementsByClass("para");//��������
        for(Element element:elements){
            context.append(element.text()+"\n");
        }

        return context.toString();
    }
    public  String parseStatistics(String html){// ����ͳ��
        StringBuffer Statistics = new StringBuffer();
        Document dom = Jsoup.parse(html);
        Elements elements = dom.getElementsByClass("lemma-statistics");
        for(Element element:elements){
        	Statistics.append(element.text()+"\n");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
        df.format(new Date());
        return df.format(new Date())+"\n"+Statistics.toString();
    }
    //reference-list
    public String parseRef(String html)
    {
    	StringBuffer  Ref=new StringBuffer();
    	Document dom = Jsoup.parse(html);
    	Elements elements = dom.getElementsByClass("reference-item");//������Ϣ
    	for(Element element:elements){
    		Ref.append(element.text()+"  "+"http://baike.baidu.com"+element.getElementsByClass("text").attr("href")+"\n");
        }
        return Ref.toString();
    }
    //open-tag-item
    public String parseTag(String html)
    {
    	StringBuffer  tag=new StringBuffer();
    	Document dom = Jsoup.parse(html);
    	Elements elements = dom.getElementsByClass("taglist");//������Ϣ
    	for(Element element:elements){
    		tag.append(element.text()+"\n");
        }
        return tag.toString();
    }

    public  Result getResult(String url,boolean ifUseProxy){
        Result result = new Result();
        //TODO���������޸Ĺ��캯�����ò����Խ��������趨,������������
        /**
    	 * @author ��Ƽ
    	 * @param m_timeOut ��ʱʱ��ms Ĭ��5000
    	 * @param m_referer httpͷ����Ϣ Ĭ�������ַ���
    	 * @param m_isUseHttps �Ƿ���httpsЭ�� Ĭ�Ͽ���
    	 * @param m_isUseJs �Ƿ���JS Ĭ�Ϲر�
    	 * @param m_outputHeaderInfo ��Client�Ƿ�Ҫ�����Ӧͷ����Ϣ
    	 * @param m_proxy ������Ϣ
    	 */
        DownLoadHTML downLoadHTML;
        if(ifUseProxy)
        	 downLoadHTML = new DownLoadHTML(5000,"",true,false,false,ProxyPool.getProxy());
        else
        	 downLoadHTML = new DownLoadHTML(5000,"",true,false,false,null);
        downLoadHTML.initDownloadClient();
        String html = downLoadHTML.downloadHtml(url);
        
        result.setTitle(parseTitle(html));
        result.setUrl(url);
        result.setSummary(parseSummary(html));
        result.setBasicInfo(parseBasicInfo(html));
        result.setIndex(parseIndex(html));
        result.setContext(parseContext(html));
        result.setReference(parseRef(html));
        result.setTags(parseTag(html));
        result.setStatics(parseStatistics(html));
        
        
        result.setUrlLink(parseLink(html));
        
        /*System.out.println("Title:\n"+
        parseTitle(html)+"\nSummary:\n"
        +parseSummary(html) +"\nBasicInfo:\n"
        +parseBasicInfo(html)+"\nIndex:\n"
        +parseIndex(html)+"\nStatistics:\n"
        +parseStatistics(html)+"\nRef:\n"+
        parseRef(html) +"\nTag:\n"+
        parseTag(html));*/
        
        return result;
    }
    //����ڣ�������
    public static void main(String []args){
    	//Thread proxyPoolThread=new Thread(new ProxyPool());
    	//proxyPoolThread.start();
    	//System.out.println("ProxyPoolThread InitComplete.");
        long st = System.currentTimeMillis();
        
    	//HtmlParserTool test=new HtmlParserTool();
    	//Result result=test.getResult("http://baike.baidu.com/item/�����ѧ��ʷ��ò��",false);

        //System.out.println(" Result:\n"+result+"Time Last:"+(System.currentTimeMillis()-st));
    	System.out.println("Time Last:"+(System.currentTimeMillis()-st));
    } 
}
