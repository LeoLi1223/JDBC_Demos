package com.nocilantro.Pool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;

public class C3P0Test {

    // 方式一
    @Test
    public void testConnection1() throws Exception {
        // 获取c3p0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/FirstDatabase" );
        cpds.setUser("root");
        cpds.setPassword("Lzh001223!");

        // 通过设置相关参数，管理数据库连接池
        // 设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(10);

        Connection connection = cpds.getConnection();
        System.out.println(connection);
    }

    // 方式二
    // 使用配置文件，获取c3p0数据库连接池
    public void testConnection2() {
        
    }
}
