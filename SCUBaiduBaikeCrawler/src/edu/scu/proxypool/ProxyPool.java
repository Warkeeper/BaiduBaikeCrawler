package edu.scu.proxypool;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.Random;
/**
 * 
 * @author ���
 * �������
 * ÿsleepMs�Ӵ���Ӧ�̻�ȡn��ip��������
 * ����getProxy������Ӵ������ȡ��һ��������
 * ����addInvalidProxy����ʧЧ������������һ��ʧЧ����
 * ���򽫻ᶨʱ����ʧЧ����
 * 
 * 
 * ���������ڴ���ȽϹ���������˵�ÿ5�����1�η���1��IP�Ĵ���Ӧ����ÿ��ip��һ���Ӻ��ʧЧ
 * ���Ե������������Ϊ10��ÿ5���ȡһ�������˺󣬾�ÿ����ȥ��һ�������ִ���ؿ�����
 * 
 */
public class ProxyPool implements Runnable
{
	private static long sleepMs = 5000;//��ȡip��ʱ����
	private final static int MAX_SIZE=10;//������������
	private final static int INIT_SIZE=1;//����س�ʼ����������������ִ���ؽ��������÷�ֱ������س�ʼ�����ﵽ
	private static String order = "398f73ca16adf5cd29d0933162b99c56";//data5u����Ӧ���ṩ�Ķ�����
	
	private static LinkedList<Proxy> proxyPool = new LinkedList<Proxy>();//����أ��Ǹ�����
	public static LinkedList<Proxy> invalidProxyQueue = new LinkedList<Proxy>();//��Ч����أ�������л�ˮ����û�õĴ���
	public void initProxyPool()//��ȡһ���ִ����Գ�ʼ�������
	{
		System.out.println("����������ڻ�ȡ������...");
		for(int i=0;i<INIT_SIZE;i++)
		{
			proxyPool.offer(requestProxy());
			try {
				Thread.sleep(sleepMs);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("��������ȡ������ɣ�");
	}
	public static Proxy getProxy()//�Ӵ�������������һ������
	{
		Random random=new Random(System.currentTimeMillis());
		return proxyPool.get(random.nextInt(proxyPool.size()));
	}
	public static void addInvalidProxy(Proxy proxy)//����Ч����������һ����Ч����
	{
		invalidProxyQueue.offer(proxy);
	}
	
	
	@Override
	public void run() 
	{
		while(true)
		{
			//������غ���Ч����ؽ���ƥ�䣬ƥ�䵽�ľʹӴ������ɾ��
			while(invalidProxyQueue!=null&&!invalidProxyQueue.isEmpty())
			{
				Proxy invalidProxy=invalidProxyQueue.poll();
				proxyPool.remove(invalidProxy);
				//System.out.println("Deleting Invalid Proxy:"+invalidProxy);
			}
			//������������������ɾ��һ��
			if(proxyPool.size()>MAX_SIZE)
			{
				//Debug
				//System.out.println(">>>>>>>>>>>>>>Too Much Proxy");
				proxyPool.poll();
				continue;
			}
			//��Ӵ��������
			proxyPool.offer(requestProxy());
			
			//�߳�˯�ߣ��ȴ�����Ӧ���ܸ����´���
			try {
				Thread.sleep(sleepMs);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	//���������������http����data5u����Ӧ���ǣ���ȡ����
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
			//System.out.println(">>>>>>>>>>>>>>��ǰ����IP�� " + res.length);
			for (String ip : res) 
			{
				Proxy pb=new Proxy((ip.split(",")[0]).split(":")[0], Integer.parseInt((ip.split(",")[0]).split(":")[1]));
				//proxyPool.offer(pb);
				//System.out.println(">>>>>>>>>>>>>>��ǰ����IP�� " + pb.getIp()+" port:" + pb.getPort());
				//new Crawler(100, targetUrl, useJs, timeOut, ip, referer, https, outputHeaderInfo).start();
				return pb;
			}
		} catch (Exception e) {
			System.err.println(">>>>>>>>>>>>>>��ȡIP����, " + e.getMessage());
		}
		return null;
	}
	
}
