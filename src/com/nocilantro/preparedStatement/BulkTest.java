package com.nocilantro.preparedStatement;

import com.nocilantro.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 使用PreparedStatement实现批量数据的操作
 *
 * update, delete本身就有批量操作的效果
 * 这里的批量操作，主要指批量插入。使用PreparedStatement如何实现更高效的批量插入？
 *
 * 任务：向Student表中插入20000条数据
 */
public class BulkTest {

    @Test
    public void testBulkAdd1() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {

            long start = System.currentTimeMillis();

            connection = JDBCUtils.getConnection();
            String sql = "insert into student(name, birthday) values (?, ?)";
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < 20000; i++) {
                ps.setObject(1, "name_" + i);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(i + 4000000);
                String birth = sdf.format(date);
                ps.setObject(2, birth);

                ps.execute();
            }

            long end = System.currentTimeMillis();

            System.out.println("Spent: " + (end - start) + "ms"); // 20000 => Spent: 10000ms

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps);
        }
    }

    /**
     * 优化批量插入
     * 使用addBatch(), executeBatch(), clearBatch()
     */
    @Test
    public void testBulkAdd2() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {

            long start = System.currentTimeMillis();

            connection = JDBCUtils.getConnection();
            String sql = "insert into student(name, birthday) values (?, ?)";
            ps = connection.prepareStatement(sql);

            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1, "name_" + i);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(i + 4000000);
                String birth = sdf.format(date);
                ps.setObject(2, birth);

                ps.addBatch();

                if (i % 500 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }

            long end = System.currentTimeMillis();

            System.out.println("Spent: " + (end - start) + "ms"); // 1000000 => Spent: 9829ms

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps);
        }
    }

    /**
     * 再优化批量插入
     * 关闭自动提交
     */
    @Test
    public void testBulkAdd3() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {

            long start = System.currentTimeMillis();

            connection = JDBCUtils.getConnection();
            // 关闭自动提交
            connection.setAutoCommit(false);

            String sql = "insert into student(name, birthday) values (?, ?)";
            ps = connection.prepareStatement(sql);

            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1, "name_" + i);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(i + 4000000);
                String birth = sdf.format(date);
                ps.setObject(2, birth);

                ps.addBatch();

                if (i % 500 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            // 提交数据
            connection.commit();

            long end = System.currentTimeMillis();

            System.out.println("Spent: " + (end - start) + "ms"); // 1000000 => Spent: 9308ms

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps);
        }
    }
}
