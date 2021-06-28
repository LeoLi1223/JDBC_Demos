package com.nocilantro.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 操作数据库的工具类
 */
public class JDBCUtils {
    /**
     * 获取数据库的连接
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception{
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

    /**
     * 关闭连接和statement的操作
     * @param connection
     * @param statement
     */
    public static void closeResources(Connection connection, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
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

    /**
     * 关闭连接,statement和resultSet的操作
     * @param connection
     * @param statement
     */
    public static void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (statement != null) {
                statement.close();
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

    /**
     * 使用dbutils.jar中提供的DBUtils工具类，实现资源的关闭
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        DbUtils.closeQuietly(connection);
        DbUtils.closeQuietly(statement);
        DbUtils.closeQuietly(resultSet);
    }

    /**
     * 通用的增删改操作
     * @param sql
     * @param args
     * @return
     */
    public static int update(Connection connection, String sql, Object ...args) { // sql中占位符的个数与可变形参的长度相同
        PreparedStatement ps = null;
        try {
            // 预编译sql语句，返回PreparedStatement实例
            ps = connection.prepareStatement(sql);
            // 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 执行
            /*
            ps.execute();
            如果执行的是查询操作，有返回结果，则此方法返回true
            如果执行的是增删改操作，没有返回结果，则此方法返回false
             */
//            ps.execute();
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            JDBCUtils.closeResources(null, ps);
        }
        return 0;
    }

    /**
     * 针对于不同表的通用的查询操作，返回多条记录的集合
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static <T> List<T> query(Connection connection, Class<T> clazz, String sql, Object ... args) {
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 执行并返回结果集
            resultSet = ps.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 创建集合对象
            List<T> output = new ArrayList<T>();
            // 处理结果集
            while (resultSet.next()) {
                T t = clazz.newInstance();
                // 处理一行数据中的每一列
                for (int i = 0; i < columnCount; i++) {
                    // 获取每一列的值：通过ResultSet
                    Object value = resultSet.getObject(i + 1);
                    // 获取每个列的列名：通过ResultSetMetaData
                    String columnName = metaData.getColumnLabel(i + 1);
                    // 给student的某个属性赋值为value => 通过反射
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, value);
                }
                output.add(t);
            }
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(null, ps, resultSet);
        }
        return null;
    }

    /**
     * 使用Druid数据库连接池技术，创建连接
     */
    private static DataSource source;
    static {
        try {
            Properties pro = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pro.load(is);
            source = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection1() throws Exception {
        Connection connection = source.getConnection();
        return connection;
    }
}
