package com.nocilantro.preparedStatement.CRUD;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class InsertTest {

    @Test
    public void testInsert()  {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();

            // 预编译sql语句，返回PreparedStatement实例
            String sql = "insert into student(id, name) values (?, ?)"; // ?：占位符
            ps = connection.prepareStatement(sql);
            // 填充占位符
            ps.setInt(1, 10);
            ps.setString(2, "Henry");

            // 执行sql
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    private Connection getConnection() throws Exception{
        // 读取配置文件中的四个基本信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(is);
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driver = properties.getProperty("driver");

        // 加载驱动
        Class.forName(driver);

        // 获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}
