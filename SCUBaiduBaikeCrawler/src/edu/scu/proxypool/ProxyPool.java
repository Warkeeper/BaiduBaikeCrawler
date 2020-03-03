package edu.scu.proxypool;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.Random;
/**
 * 
 * @author 杨斌
 * 代理池类
 * 每sleepMs从代理供应商获取n个ip加入代理池
 * 调用getProxy则随机从代理池中取出一个代理返回
 * 调用addInvalidProxy则在失效代理队列中添加一个失效代理
 * 程序将会定时处理失效代理
 * 
 * 
 * ！！！由于代理比较贵，买了最便宜的每5秒调用1次返回1个IP的代理供应，且每个ip在一分钟后就失效
 * 所以当代理池容量设为10，每5秒获取一个，满了后，就每五秒去掉一个，保持代理池可用性
 * 
 */
public class ProxyPool implements Runnable
{
	private static long sleepMs = 5000;//获取ip的时间间隔
	private final static int MAX_SIZE=10;//代理池最大容量
	private final static int INIT_SIZE=1;//代理池初始容量，不到这个数字代理池将阻塞调用方直到代理池初始容量达到
	private static String order = "398f73ca16adf5cd29d0933162b99c56";//data5u代理供应商提供的订单号
	
	private static LinkedList<Proxy> proxyPool = new LinkedList<Proxy>();//代理池，是个队列
	public static LinkedList<Proxy> invalidProxyQueue = new LinkedList<Proxy>();//无效代理池，代理池中浑水摸鱼没用的代理
	public void initProxyPool()//获取一部分代理以初始化代理池
	{
		System.out.println("爬虫程序正在获取代理中...");
		for(int i=0;i<INIT_SIZE;i++)
		{
			proxyPool.offer(requestProxy());
			try {
				Thread.sleep(sleepMs);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("爬虫程序获取代理完成！");
	}
	public static Proxy getProxy()//从代理池中随机返回一个代理
	{
		Random random=new Random(System.currentTimeMillis());
		return proxyPool.get(random.nextInt(proxyPool.size()));
	}
	public static void addInvalidProxy(Proxy proxy)//在无效代理池中添加一个无效代理
	{
		invalidProxyQueue.offer(proxy);
	}
	
	
	@Override
	public void run() 
	{
		while(true)
		{
			//将代理池和无效代理池进行匹配，匹配到的就从代理池中删除
			while(invalidProxyQueue!=null&&!invalidProxyQueue.isEmpty())
			{
				Proxy invalidProxy=invalidProxyQueue.poll();
				proxyPool.remove(invalidProxy);
				//System.out.println("Deleting Invalid Proxy:"+invalidProxy);
			}
			//如果超过代理池容量则删掉一个
			if(proxyPool.size()>MAX_SIZE)
			{
				//Debug
				//System.out.println(">>>>>>>>>>>>>>Too Much Proxy");
				proxyPool.poll();
				continue;
			}
			//添加代理到代理池
			proxyPool.offer(requestProxy());
			
			//线程睡眠，等待代理供应商能给个新代理
			try {
				Thread.sleep(sleepMs);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	//请求代理函数：发送http请求到data5u代理供应商那，获取代理
	private Proxy requestProxy()
	{
		try {
			java.net.URL url = new java.net.URL("http://api.ip.data5u.com/dynamic/get.html?order=" + order + "&ttl&random=true");
			
	    	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	    	connection.setConnectTimeout(3000);
	    	connection = (HttpURLConnection)url.openConnection();
	    	
	        InputStream raw = connection.getInputStream();  
	        InputStream in = new BufferedInputStream(raw);  
	        byte[] data = new byte[in.available()];
	        int bytesRead = 0;  
	        int offset = 0;  
	        while(offset < data.length) {  
	            bytesRead = in.read(data, offset, data.length - offset);  
	            if(bytesRead == -1) {  
	                break;  
	            }  
	            offset += bytesRead;  
	        }  
	        in.close();  
	        raw.close();
			String[] res = new String(data, "UTF-8").split("\n");
			//System.out.println(">>>>>>>>>>>>>>当前返回IP量 " + res.length);
			for (String ip : res) 
			{
				Proxy pb=new Proxy((ip.split(",")[0]).split(":")[0], Integer.parseInt((ip.split(",")[0]).split(":")[1]));
				//proxyPool.offer(pb);
				//System.out.println(">>>>>>>>>>>>>>当前返回IP有 " + pb.getIp()+" port:" + pb.getPort());
				//new Crawler(100, targetUrl, useJs, timeOut, ip, referer, https, outputHeaderInfo).start();
				return pb;
			}
		} catch (Exception e) {
			System.err.println(">>>>>>>>>>>>>>获取IP出错, " + e.getMessage());
		}
		return null;
	}
	
}
