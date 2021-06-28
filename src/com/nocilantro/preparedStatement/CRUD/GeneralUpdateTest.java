package com.nocilantro.preparedStatement.CRUD;

import com.nocilantro.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 通用的增删改操作
 */
public class GeneralUpdateTest {
    public int update(String sql, Object ...args) { // sql中占位符的个数与可变形参的长度相同
        // 获取数据库连接
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();
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
            JDBCUtils.closeResources(connection, ps);
        }
        return 0;
    }

    @Test
    public void testUpdate() {
        String sql = "update student set name = ? where id = ?";
        update(sql, "Huhu", 5);
    }
}
