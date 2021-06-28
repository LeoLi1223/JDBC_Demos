package com.nocilantro.connection;

import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {

    // 方式一
    @Test
    public void testConnection1() throws SQLException {
        // 获取driver的实现类对象
        Driver driver = new com.mysql.cj.jdbc.Driver();
        /*
        url: jdbc:mysql://localhost:3306/FirstDatabase
        jdbc:mysql: 协议
        localhost: ip地址
        3306: 默认mysql的端口号
        FirstDatabase: 数据库
         */
        String url = "jdbc:mysql://localhost:3306/FirstDatabase";
        Properties info = new Properties();
        // 将用户名和密码封装在Properties中
        info.setProperty("user", "root");
        info.setProperty("password", "Lzh001223!");

        Connection connect = driver.connect(url, info);

        System.out.println(connect);
    }

    // 方式二：对方法一的迭代
    // 在如下的程序中不出现第三方api，使得程序具有更好的可移植性
    @Test
    public void testConnection2() throws Exception {
        // 获取driver的实现类对象
        Class aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        // 提供需要连接的数据库
        String url = "jdbc:mysql://localhost:3306/FirstDatabase";

        // 提供连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "Lzh001223!");

        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    // 方式三：使用DriverManager替换Driver
    @Test
    public void testConnection3() throws Exception{
        // 获取driver的实现类对象
        Class aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        // 提供三个基本信息
        String url = "jdbc:mysql://localhost:3306/FirstDatabase";
        String user = "root";
        String password = "Lzh001223!";

        // 注册驱动
        DriverManager.registerDriver(driver);

        // 获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    // 方式四：对方式三的优化，可以只是加载驱动，不用显式的注册驱动了
    @Test
    public void testConnection4() throws Exception{
        // 提供三个基本信息
        String url = "jdbc:mysql://localhost:3306/FirstDatabase";
        String user = "root";
        String password = "Lzh001223!";

        // 加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        /*
        相较于方式三，可以省略如下操作：
        Driver driver = (Driver) aClass.newInstance();
        DriverManager.registerDriver(driver);

        为什么可以省略呢？
        在mysql的Driver实现类中，声明了如下操作：
        static {
            try {
                java.sql.DriverManager.registerDriver(new Driver());
            } catch (SQLException E) {
                throw new RuntimeException("Can't register driver!");
            }
        }
         */


        // 获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    // 方式五：最终版
    // 将数据库连接需要的基本信息声明在配置文件中，通过读取配置文件的方式
    // 好处：实现了数据和代码分离，实现了解耦。
    //      如果需要修改配置文件信息，可以避免重新打包。
    @Test
    public void testConnection5() throws Exception{
        // 读取配置文件中的四个基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
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
        System.out.println(connection);
    }
}
