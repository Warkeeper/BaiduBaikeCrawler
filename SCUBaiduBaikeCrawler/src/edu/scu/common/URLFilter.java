package edu.scu.common;

import java.util.BitSet;

/**
 * @author ��Դ����
 *  ��¡�㷨
 *  ����ȡ��urlȥ��
 *  �㷨�ܽ�
 *  1.����һ����¡������������һ���㹻��λ�ռ䣨�������������� 
 *	2.���һЩ����������������һЩ�в�ͬ��ӳ�亯������ϣ�������� 
 *	3.���ÿ��urlռ����λ����ÿ��url��������ֽڵĴ洢�ռ䣻 
 *	4.ʹ��һϵ�еĹ�ϣ�����Դ�url�е�ÿһ��Ԫ�ؽ��м��㣬����һϵ�е��������Ҳ������Ϣָ�ƣ� 
 *	5.����Ϣָ�Ʒ��䵽�ղŵ�url��ռ���ֽڿռ䣻 
 *	6.����Ϣָ������Ӧ��һϵ��ʮ�������ֶ�Ӧ����¡����������Ӧλ���ϣ��ж��Ƿ��Ѿ������ڲ�¡������֮�С�
 */
public class URLFilter 
{
	 private static final  int  DEFAULT_SIZE  =2 << 24 ;
	  //��������������ӣ��ɲ���6����ͬ�������������
	    private static final  int [] seeds =new  int []{5,7, 11 , 13 , 31 , 37 , 61};
	  //Java�еİ�λ�洢��˼�룬���㷨�ľ���ʵ�֣���¡��������
	    private BitSet bits= new  BitSet(DEFAULT_SIZE);
	  //��������������ӣ�����6����ϣ����
	    private  SimpleHash[]  func=new  SimpleHash[seeds.length];

	    public  URLFilter() 
	    {
	        for( int  i= 0 ; i< seeds.length; i ++ ) {
	            func[i]=new  SimpleHash(DEFAULT_SIZE, seeds[i]);
	        }
	    }

	    public void  add(String value) {
	        for(SimpleHash f : func) {
	            bits.set(f.hash(value),  true );
	        }
	    }

	  //���ݴ�URL�õ��ڲ�¡�������еĶ�Ӧλ�����ж����־λ��6����ͬ�Ĺ�ϣ��������6�ֲ�ͬ��ӳ�䣩
	    public boolean  contains(String value) {
	        if(value ==null ) {
	            return false ;
	        }
	        boolean  ret  = true ;
	        for(SimpleHash f : func) {
	            ret=ret&& bits.get(f.hash(value));
	        }
	        return  ret;
	    }
	  //Ĭ�Ϲ���������ϣ��Ĭ��ΪDEFAULT_SIZE��С���˹�ϣ����������Ϊseed
	    public static class SimpleHash {
	        private int cap;
	        private int seed;

	        public SimpleHash(int cap, int seed) {
	            this.cap = cap;
	            this.seed = seed;
	        }

	        public int hash(String value) {
	            int result = 0;
	            int len = value.length();
	            for (int i = 0; i < len; i++) {
	            	//����URL�ù�ϣ��������һ��ֵ��ʹ�õ��˼����е�ÿһ��Ԫ�أ�
	                result = seed * result + value.charAt(i);
	            }
	          //����������Ϣָ��
	            return (cap - 1) & result;
	        }
	    }

	    public static void  main(String[] args) {
	        String value  = "cc.scu.edu.cn" ;
	        URLFilter filter=new URLFilter();
	        System.out.println(filter.contains(value));
	        filter.add(value);
	        System.out.println(filter.contains(value));
	    }
}
