package com.nocilantro.DAO;

import com.nocilantro.practice.Student;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * 此接口用于规范针对于Student表的常用操作
 */
public interface StudentDAO {

    /**
     * 将student添加到表中
     * @param connection
     * @param student
     */
    void insert(Connection connection, Student student);

    /**
     * 针对指定id，删除记录
     * @param connection
     * @param id
     */
    void deleteById(Connection connection, int id);

    /**
     * 针对指定id，更改记录
     * @param connection
     * @param student
     */
    void updateById(Connection connection, Student student);

    /**
     * 针对指定id，返回记录
     * @param connection
     * @param id
     * @return
     */
    Student getStudentById(Connection connection, int id);

    /**
     * 返回表中所有记录构成的集合
     * @param connection
     * @return
     */
    List<Student> getAll(Connection connection);

    /**
     * 返回数据表的数据的条目数
     * @param connection
     * @return
     */
    Long count(Connection connection);

    /**
     * 返回生日的最大值
     * @param connection
     * @return
     */
    Date getMaxBirth(Connection connection);
}
