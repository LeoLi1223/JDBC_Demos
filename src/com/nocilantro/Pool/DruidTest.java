package com.nocilantro.Pool;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DruidTest {

    @Test
    public void getConnection1() throws Exception {
        Properties pro = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        pro.load(is);
        DataSource source = DruidDataSourceFactory.createDataSource(pro);

        Connection connection = source.getConnection();
        System.out.println(connection);
    }
}
