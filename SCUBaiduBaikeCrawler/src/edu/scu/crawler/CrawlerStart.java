package edu.scu.crawler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.scu.common.URLFilter;
import edu.scu.db.dao.ResultDao;
import edu.scu.proxypool.ProxyPool;

/**
 * 
 * @author ���������
 *
 */
public class CrawlerStart {
	
	public static URLFilter urlFilter = new URLFilter();//ʹ���˲�¡�����㷨��URLȥ��
	public static ExecutorService crawlerThreadPool = Executors.newFixedThreadPool(20);//�����̳߳�
	public static Queue<Result> resultsQueue= new LinkedList<>();//������У�������HTML�������Ľ����������
	
	public static int taskCounter = 0;//���ã����������
    public static final int MAX_TASK_NUM = 1000;//���ã����������
	
    public static boolean ifUseProxy=false;//�Ƿ�ʹ�ô���ؽ�������
	//public static Result queueTop = new Result();
	
	
	public CrawlerStart initial(String url){ //��һ��url��ʼ������
        HtmlParserTool parserTool = new HtmlParserTool();
        resultsQueue.add(parserTool.getResult(url,ifUseProxy));//�������������������
        urlFilter.add(url);//������url����ȥ��filter
        return this;
    }
	public void launch()
	{
		ResultDao.persistent(resultsQueue.peek());//��������ȡ����������ݳ־û�
		while(resultsQueue.size()>0)
		{
			
			System.out.println("*********Already Crawled: "+Crawler.CrawlerID+" lemmas*********");
			Result resultInQueueTop = resultsQueue.poll();//���������ȡ��һ�����
			ArrayList< FutureTask<Result> > tasks = new ArrayList<>();//���������б�
			
			//����ȡ����е�����ȡ������ѯ�Ƿ�������û�����url����ȥ��filter�����������б�
			for(String link:resultInQueueTop.getUrlLink()) 
            {                                   
                if(!urlFilter.contains(link)) //��û����ȡ�����ҳ��
                {                                            
                	urlFilter.add(link);
                    tasks.add(new FutureTask<>(new Crawler(link)));
                }
            }
			//�������б��е������ύ���̳߳ؽ��д���
			for(FutureTask<Result> task:tasks)
            {                                         //�̳߳�ִ��������߳�
				//System.out.println(">>>>>>>>>>Submit");
				crawlerThreadPool.submit(task);
            }
			//TODO: Not Tested,Maybe Dangerous
        	//taskCounter += tasks.size();
        	//�ȴ������б��е�����ȫ����ɣ���ɺ󽫽������������׼���´�ȡ�������ѽ�����ݳ־û�
			for(FutureTask<Result> task:tasks) 
            {
                try {
                    Result taskResult = task.get(5, TimeUnit.SECONDS);      //��ȡ����Ľ��,����ÿ������ĳ�ʱʱ��Ϊ1s
                    taskCounter--;
                    if(!taskResult.getTitle().equals("")) //���˵�����������
                    {                           
                    	System.out.println(taskResult.getTitle());
                        resultsQueue.add(taskResult);
                        ResultDao.persistent(taskResult);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
			
		}
	}
	
	//�������
	public static void main(String[] args) 
	{
		if(args.length!=0)
		{
			if(args[0]=="EnableProxy")
				ifUseProxy=true;
			else if(args[0]=="DisableProxy")
				ifUseProxy=false;
		}
		if(ifUseProxy)//���ʹ�ô�������������߳�
		{
			ProxyPool proxyPool=new ProxyPool();
			proxyPool.initProxyPool();
			Thread proxyPoolThread=new Thread(proxyPool);
	    	proxyPoolThread.start();
		}
		long st = System.currentTimeMillis();
        new CrawlerStart().initial("http://baike.baidu.com/item/�Ĵ���ѧ").launch();
		//new CrawlerStart().initial("https://baike.baidu.com/view/21087.html").launch();
        System.out.println("\n�����ܹ�����ʱ�䣺"+ (System.currentTimeMillis()-st));
	}

}
