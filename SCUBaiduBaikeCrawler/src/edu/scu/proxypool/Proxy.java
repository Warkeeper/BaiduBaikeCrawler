package edu.scu.proxypool;
/**
 * 
 * @author 杨斌
 *
 */
public class Proxy 
{
	private String ip;//代理ip地址
	private int port;//代理端口
	public Proxy(String m_ip,int m_port)
	{
		this.ip=m_ip;
		this.port=m_port;
	}
	public String getIp()
	{
		return ip;
	}
	public int getPort()
	{
		return port;
	}
	public void setIp(String m_ip)
	{
		this.ip=m_ip;
	}
	public void setPort(int m_port)
	{
		this.port=m_port;
	}
	public String toString()
	{
		return "IP:"+ip+" Port:"+port;
	}
	
}
