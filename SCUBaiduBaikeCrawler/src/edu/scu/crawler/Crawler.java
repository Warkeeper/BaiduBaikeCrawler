package edu.scu.crawler;

import java.util.concurrent.Callable;
/**
 * 
 * @author ���
 * ��װ���������������ύ�̳߳�
 */

public class Crawler implements Callable<Result>{
	
	public static int CrawlerID = 0;//����ID���ɹ���һ��������1������Ҳ������Ϊ����������
    private String url = null;//������Ҫ����Url

    public Crawler(String url) {
        this.url = url;
    }

    @Override
    public Result call() throws Exception {
        HtmlParserTool parserTool = new HtmlParserTool();
        Result result=parserTool.getResult(url,CrawlerStart.ifUseProxy);//ͨ��HtmlParserTool��ȡ���
        CrawlerID++;
        return result;
    }

    public String getLinks() {
        return url;
    }

    public void setLinks(String links) {
        this.url = links;
    }
}