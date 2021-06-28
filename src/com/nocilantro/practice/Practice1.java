package com.nocilantro.practice;

import com.nocilantro.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.util.Scanner;

public class Practice1 {

    public static void test1(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入id：");
        int id = scanner.nextInt();
        System.out.println("请输入姓名：");
        String name = scanner.next();

        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "insert into student values (?, ?)";
            int affectRows = JDBCUtils.update(connection, sql, id, name);
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

    public static void main(String[] args) {
        test1();
    }
}
