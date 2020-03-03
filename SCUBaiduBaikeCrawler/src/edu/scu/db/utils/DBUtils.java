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
 * @author ������
 *
 */
public class DBUtils {
    private static String driver;                       //����·��
    private static String url;                          //���ݿ����ӵ�ַ��˿�
    private static String dbName;                       //���ݿ�����
    private static String dbPassWd;                     //���ݿ�����

    static {                                            //��̬����飬��ʼ�����ݱ���
        Properties properties = new Properties();       //�����ļ�
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
