package com.nocilantro.DAO;

import com.nocilantro.practice.Student;
import com.nocilantro.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

public class StudentDAOImplTest {

    StudentDAOImpl dao = new StudentDAOImpl();

    @Test
    public void testInsert() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Student student = new Student(30, "Mian", new Date(4363562634656L));
            dao.insert(connection, student);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }

    @Test
    public void testDeleteById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            dao.deleteById(connection, 1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }

    @Test
    public void testUpdateById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Student student = new Student(1, "Huhu", new Date(4363251324656L));
            dao.updateById(connection, student);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }

    @Test
    public void testGetStudentById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Student student = dao.getStudentById(connection, 2);
            System.out.println(student);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }

    @Test
    public void testGetAll() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection1();
            List<Student> all = dao.getAll(connection);
            all.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }

    @Test
    public void testCount() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Long count = dao.count(connection);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }

    @Test
    public void testGetMaxBirth() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Date birth = dao.getMaxBirth(connection);
            System.out.println(birth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }
}