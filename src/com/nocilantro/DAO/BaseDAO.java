package com.nocilantro.DAO;

import com.nocilantro.util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装了针对于数据表的通用的操作
 */
public abstract class BaseDAO<T> {

    private Class<T> clazz = null;

    {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType type = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = type.getActualTypeArguments(); // 获取了父类的泛型
        clazz = (Class<T>) actualTypeArguments[0]; // 泛型的第一个参数
    }

    /**
     * 通用的增删改操作
     * @param sql
     * @param args
     * @return
     */
    public int update(Connection connection, String sql, Object ...args) { // sql中占位符的个数与可变形参的长度相同
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
     * @param connection
     * @param sql
     * @param args
     * @return
     */
    public List<T> query(Connection connection, String sql, Object ... args) {
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

    public <T> T applyFunc(Connection connection, String sql, Object ...args){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return (T) resultSet.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(null, ps, resultSet);
        }
        return null;
    }
}
