package com.nocilantro.preparedStatement.CRUD;

import com.nocilantro.bean.Student;
import com.nocilantro.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class QueryTest {
    /**
     * 针对Student表的通用的查询方法
     */
    public Student queryStudent(String sql, Object ... args) {
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
                Student student = new Student();
                // 处理一行数据中的每一列
                for (int i = 0; i < columnCount; i++) {
                    // 获取每一列的值：通过ResultSet
                    Object value = resultSet.getObject(i + 1);
                    // 获取每个列的列名：通过ResultSetMetaData
                    String columnName = metaData.getColumnLabel(i + 1);
                    // 给student的某个属性赋值为value => 通过反射
                    Field field = Student.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(student, value);
                }
                return student;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps, resultSet);
        }
        return null;
    }

    @Test
    public void queryStudentTest() {
        String sql = "select name from student where id = ?";
        Student student = queryStudent(sql, 28);
        System.out.println(student);
    }

    @Test
    public void testQuery1(){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select * from student where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, 18);
            // 执行并返回结果集
            resultSet = ps.executeQuery();
            // 处理结果集
            while (resultSet.next()) { // 判断结果集的下一条是否有数据，如果有则返回true指针下移，如果无则返回false
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                Student student = new Student(id, name);
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps, resultSet);
        }
    }
}
