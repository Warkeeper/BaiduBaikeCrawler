package edu.scu.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.scu.crawler.Result;
import edu.scu.db.utils.DBUtils;
//TODO����д���ݲ������������Ӹ�������
/**
 * 
 * @author ���
 *
 */
public class ResultDao 
{
	 private static Connection connection = null;                           //���ݿ�����
	 private static ResultSet resultSet = null;                             //�����
	 private static PreparedStatement preparedStatement = null;             //Ԥ����� SQL���
	 
	 public static void insert(Result result){                               //���ӣ����������ݿ�
	        String sql = "insert into crawlerresult(title,url,summary,basicinfo,catalog,context,reference,tags,statics) values(?,?,?,?,?,?,?,?,?);";
	        connection = new DBUtils().openConnection();                                 
	        try {
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1,result.getTitle());                     
	            preparedStatement.setString(2,result.getUrl());
	            preparedStatement.setString(3,result.getSummary());
	            preparedStatement.setString(4,result.getBasicInfo());
	            preparedStatement.setString(5,result.getIndex());
	            preparedStatement.setString(6,result.getContext());
	            preparedStatement.setString(7,result.getReference());
	            preparedStatement.setString(8,result.getTags());
	            preparedStatement.setString(9,result.getStatics());
	            preparedStatement.execute();                                           
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            ReleaseConnection();
	        }
	    }
	 public static ResultSet Query() {//��ȡ��ѯ
	        String sql = " SELECT * FROM text;";
	        connection = new DBUtils().openConnection();
	        try {
	            preparedStatement = connection.prepareStatement(sql);
	            resultSet = preparedStatement.executeQuery();
	            return resultSet;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return resultSet;
	    }

	    public static void ReleaseConnection() {//�ͷ�
	        try {
	            if(connection!=null) {
	                connection.close();
	                connection = null;
	            }
	            if(preparedStatement!=null){
	                preparedStatement.close();
	                preparedStatement = null;
	            }
	            if(resultSet!=null){
	                resultSet.close();
	                resultSet=null;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void persistent(Result result){
	        insert(result);
	    }
	    
	    //����ڣ�������
	    public static void main(String []args){
	    new ResultDao();
	      Result result=new Result();
	      result.setBasicInfo("1");
	      result.setContext("2");
	      result.setIndex("3");
	      result.setReference("4");
	      result.setStatics("5");
	      result.setSummary("6");
	      result.setTags("7");
	      result.setTitle("8");
	      result.setUrl("9");
	      result.setUrlLink(null);
	      
	       ResultDao.insert(result);
//	        try {
//	            while(rs.next()){
//	                String s = rs.getString(1);
//	                String n = rs.getString(2);
//	                System.out.printf("%s %s\n",s,n);
//	            }
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        } finally {
//	            ReleaseConnection();
//	        }
	    }
}
