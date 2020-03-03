package edu.scu.crawler;

import java.util.concurrent.Callable;
/**
 * 
 * @author 杨斌
 * 包装了爬虫任务，用以提交线程池
 */

public class Crawler implements Callable<Result>{
	
	public static int CrawlerID = 0;//爬虫ID，成功爬一个就自增1，所以也可以认为是已爬个数
    private String url = null;//该爬虫要爬的Url

    public Crawler(String url) {
        this.url = url;
    }

    @Override
    public Result call() throws Exception {
        HtmlParserTool parserTool = new HtmlParserTool();
        Result result=parserTool.getResult(url,CrawlerStart.ifUseProxy);//通过HtmlParserTool获取结果
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