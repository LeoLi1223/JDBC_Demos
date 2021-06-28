package com.nocilantro.transaction;

import com.nocilantro.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 1.什么叫数据库事物？
 * 一组逻辑操作单元，使数据从一种状态变换到另一种状态
 *
 * 2.事务处理（事务操作）：保证所有事务都作为一个工作单元来执行，即使出现了故障，
 * 都不能改变这种执行方式。当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，
 * 那么这些修改就永久地保存下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态。
 *
 * 3.数据一旦提交，就不可回滚
 *
 * 4.哪些操作会导致数据自动提交？
 *      DDL操作一旦执行，都会自动提交
 *      DML默认情况下，一旦执行，会自动提交 （可以通过 set autocommit = false 的方式取消DML自动提交）
 *      默认在关闭连接时，会自动提交数据
 */
public class TransactionTest {

    @Test
    public void testQueryTx() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        // 设置隔离级别
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.setAutoCommit(false);

        String sql = "select Accound_id id, name, balance from account where name = ?";
        List<Account> result = JDBCUtils.query(connection, Account.class, sql, "AA");
        result.forEach(System.out::println);
    }

    @Test
    public void testUpdateTx(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            // 取消DML自动提交
            connection.setAutoCommit(false);

            String sql1 = "update account set balance = balance - 100 where name = ?";
            JDBCUtils.update(connection, sql1, "AA");

            String sql2 = "update account set balance = balance + 100 where name = ?";
            JDBCUtils.update(connection, sql2, "BB");

            System.out.println("Success");
            // 提交数据
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // 回滚数据
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtils.closeResources(connection, null);
        }
    }
}
