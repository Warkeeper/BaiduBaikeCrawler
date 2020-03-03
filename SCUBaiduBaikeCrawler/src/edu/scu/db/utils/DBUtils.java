package edu.scu.db.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 
 * @author 王港庆
 *
 */
public class DBUtils {
    private static String driver;                       //驱动路径
    private static String url;                          //数据库连接地址与端口
    private static String dbName;                       //数据库名称
    private static String dbPassWd;                     //数据库密码

    static {                                            //静态代码块，初始化数据变量
        Properties properties = new Properties();       //配置文件
        Reader in =null;                                
        try {
        	in =new FileReader("config//db-config//config.properties");
            properties.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        driver = properties.getProperty("driver");
        url=properties.getProperty("url");
        dbName=properties.getProperty("dbName");
        dbPassWd=properties.getProperty("dbPassWd");
    }

    public Connection openConnection(){
        Connection connection = null;                       
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,dbName,dbPassWd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
