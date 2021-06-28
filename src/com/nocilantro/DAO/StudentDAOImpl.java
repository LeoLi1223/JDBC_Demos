package com.nocilantro.DAO;

import com.nocilantro.practice.Student;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class StudentDAOImpl extends BaseDAO<Student> implements StudentDAO{
    @Override
    public void insert(Connection connection, Student student) {
        String sql = "insert into student(name, birthday) values (?, ?)";
        update(connection, sql, student.getName(), student.getBirth());
    }

    @Override
    public void deleteById(Connection connection, int id) {
        String sql = "delete from student where id = ?";
        update(connection, sql, id);
    }

    @Override
    public void updateById(Connection connection, Student student) {
        String sql = "update student set name = ?, birthday = ? where id = ?";
        update(connection, sql, student.getName(), student.getBirth(), student.getId());
    }

    @Override
    public Student getStudentById(Connection connection, int id) {
        String sql = "select id, name, birthday birth from student where id = ?";
        List<Student> query = query(connection, sql, id);
        return query.get(0);
    }

    @Override
    public List<Student> getAll(Connection connection) {
        String sql = "select id, name, birthday birth from student";
        List<Student> query = query(connection, sql);
        return query;
    }

    @Override
    public Long count(Connection connection) {
        String sql = "select count(*) from student";
        return applyFunc(connection, sql);
    }

    @Override
    public Date getMaxBirth(Connection connection) {
        String sql = "select MAX(birthday) from student";
        return applyFunc(connection, sql);
    }
}
