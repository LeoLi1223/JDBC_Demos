package com.nocilantro.preparedStatement.CRUD;

import com.nocilantro.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateTest {

    @Test
    public void testUpdate() {
        // 获取数据库连接
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();
            // 预编译sql语句，返回PreparedStatement实例
            String sql = "update student set name = ? where id = ?";
            ps = connection.prepareStatement(sql);
            // 填充占位符
            ps.setString(1, "Huhu");
            ps.setInt(2, 10);
            // 执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            JDBCUtils.closeResources(connection, ps);
        }
    }
}
