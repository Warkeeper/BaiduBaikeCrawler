package edu.scu.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.scu.crawler.Result;
import edu.scu.db.utils.DBUtils;
//TODO：重写数据插入结果类以增加更多内容
/**
 * 
 * @author 杨斌
 *
 */
public class ResultDao 
{
	 private static Connection connection = null;                           //数据库连接
	 private static ResultSet resultSet = null;                             //结果集
	 private static PreparedStatement preparedStatement = null;             //预编译的 SQL语句
	 
	 public static void insert(Result result){                               //增加，即插入数据库
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
	 public static ResultSet Query() {//读取查询
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

	    public static void ReleaseConnection() {//释放
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
	    
	    //非入口，测试用
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
