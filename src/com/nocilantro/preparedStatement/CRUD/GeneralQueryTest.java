package com.nocilantro.preparedStatement.CRUD;

import com.nocilantro.bean.Student;
import com.nocilantro.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 针对于不同表的通用的查询操作
 */
public class GeneralQueryTest {

    @Test
    public void testGetInstance() {
        String sql = "select name from student where id = ?";
        Student instance = getInstance(Student.class, sql, 18);
        System.out.println(instance);
    }

    @Test
    public void testGetInstances() {
        String sql = "select id, name from student where id < ?";
        List<Student> instances = getInstances(Student.class, sql, 10);
        instances.forEach(System.out::println);

        System.out.println("--------------------------");

        String sql1 = "select * from student";
        List<Student> instances1 = getInstances(Student.class, sql1);
        instances1.forEach(System.out::println);
    }

    /**
     * 针对于不同表的通用的查询操作，返回一条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return T
     */
    public <T> T getInstance(Class<T> clazz, String sql, Object ... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 执行并返回结果集
            resultSet = ps.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 处理结果集
            if (resultSet.next()) {
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
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps, resultSet);
        }
        return null;
    }

    /**
     * 针对于不同表的通用的查询操作，返回多条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> getInstances(Class<T> clazz, String sql, Object ... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
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
            JDBCUtils.closeResources(connection, ps, resultSet);
        }
        return null;
    }
}
