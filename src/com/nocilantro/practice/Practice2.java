package com.nocilantro.practice;

import com.nocilantro.util.JDBCUtils;
import jdk.nashorn.internal.scripts.JD;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Practice2 {
    /**
     * 加入学生
     */
    public static void q1(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("姓名：");
        String name = scanner.next();
        System.out.println("生日：");
        String birth = scanner.next();

        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "insert into student (name, birthday) values (?, ?)";
            int affectRows = JDBCUtils.update(connection, sql, name, birth);
            if (affectRows > 0) {
                System.out.println("Success");
            } else {
                System.out.println("Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
            scanner.close();
        }
    }

    /**
     * 查询指定的学生
     */
    public static void q2() {
        System.out.println("Search by:");
        System.out.println("a. id");
        System.out.println("b. name");
        System.out.println("c. birth");
        Scanner scanner = new Scanner(System.in);
        String selection = scanner.next();
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            if ("a".equalsIgnoreCase(selection)) {
                System.out.println("请输入需要查询的id");
                int id = scanner.nextInt();
                String sql = "select id, name, birthday birth from student where id = ?";
                List<Student> query = JDBCUtils.query(connection, Student.class, sql, id);
                query.forEach(System.out::println);
            } else if ("b".equalsIgnoreCase(selection)) {
                System.out.println("请输入需要查询的姓名：");
                String name = scanner.next();
                String sql = "select id, name, birthday birth from student where name = ?";
                List<Student> query = JDBCUtils.query(connection, Student.class, sql, name);
                query.forEach(System.out::println);
            } else if ("c".equalsIgnoreCase(selection)) {
                System.out.println("请输入需要查询的姓名：");
                String name = scanner.next();
                String sql = "select id, name, birthday birth from student where birthday = ?";
                List<Student> query = JDBCUtils.query(connection, Student.class, sql, name);
                query.forEach(System.out::println);
            } else {
                System.out.println("Illegal searching!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
            scanner.close();
        }
    }

    /**
     * 删除指定学生
     */
    public static void deleteById(){
        System.out.println("请输入需要删除的学生id：");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();

        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "delete from student where id = ?";
            int affectedRows = JDBCUtils.update(connection, sql, id);
            if (affectedRows > 0) {
                System.out.println("Success");
            } else {
                System.out.println("Student Not Found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
            scanner.close();
        }
    }

    public static void main(String[] args) {
//        q1();
        q2();
//        deleteById();
    }
}
