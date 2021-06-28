package com.nocilantro.DBUtilsTest;

import com.nocilantro.practice.Student;
import com.nocilantro.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * commons-dbutils 是Apache组织提供的一个开源JDBC工具类库，封装了针对数据库的增删改查操作
 */
public class TestQueryRunner {

    @Test
    public void testInsert() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection1();
            String sql = "insert into student(name, birthday) values (?, ?)";
            int insertCount = runner.update(connection, sql, "huhu", new Date(841395719438L));

            System.out.println("Added " + insertCount + " tuple");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }

    /*
    BeanHandler：是ResultSetHandler的一个实现类，用于封装表中的一条记录
     */
    @Test
    public void testQuery1() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection1();
            String sql = "select id, name, birthday birth from student where id = ?";
            BeanHandler<Student> handler = new BeanHandler<Student>(Student.class);
            Student student = runner.query(connection, sql, handler, 4);
            System.out.println(student);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }

    }

    /*
    BeanListHandler：是ResultSetHandler的一个实现类，用于封装表中的多条记录
     */
    @Test
    public void testQuery2() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection1();
            String sql = "select id, name, birthday birth from student where id < ?";
            BeanListHandler<Student> handler = new BeanListHandler<Student>(Student.class);
            List<Student> student = runner.query(connection, sql, handler, 4);
            student.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }

    /*
    MapHandler：是ResultSetHandler的一个实现类，用于对应表中的一条记录
    将字段和字段对应的值作为Map中的key和value
     */
    @Test
    public void testQuery3() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection1();
            String sql = "select id, name, birthday birth from student where id < ?";
            MapHandler handler = new MapHandler();
            Map<String, Object> student = runner.query(connection, sql, handler, 4);
            System.out.println(student);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }

    /*
    MapListHandler：是ResultSetHandler的一个实现类，用于对应表中的对条记录
    将字段和字段对应的值作为Map中的key和value
     */
    @Test
    public void testQuery4() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection1();
            String sql = "select id, name, birthday birth from student where id < ?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> student = runner.query(connection, sql, handler, 4);
            System.out.println(student);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }

    /*
    ScalarHandler：是ResultSetHandler的一个实现类，用于查询特殊值
     */
    @Test
    public void testQuery5() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection1();
            String sql = "select Max(birthday) from student";

            ScalarHandler<Date> handler = new ScalarHandler<>();

            Date date = runner.query(connection, sql, handler);

            System.out.println(date.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }


    /*
    自定义ResultSetHandler的实现类
     */
    @Test
    public void testQuery6() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection1();
            String sql = "select id, name, birthday birth from student where id = ?";

            ResultSetHandler<Student> handler = new ResultSetHandler<Student>() {
                @Override
                public Student handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        Date date = rs.getDate("birth");
                        return new Student(id, name, date);
                    }
                    return null;
                }
            };
            Student student = runner.query(connection, sql, handler, 3);
            System.out.println(student);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }
 }
