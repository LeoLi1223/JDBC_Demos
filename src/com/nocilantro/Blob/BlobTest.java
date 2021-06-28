package com.nocilantro.Blob;

import com.nocilantro.util.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 使用PreparedStatement操作Blob类型的数据
 */
public class BlobTest {

    // 向Celebrity表中插入Blob类型字段
    @Test
    public void testInsert() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into celebrity (name, photo) value (?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setObject(1, "Huhu");
        FileInputStream fis = new FileInputStream(new File("Huhu.jpeg"));
        ps.setBlob(2, fis);

        ps.executeUpdate();

        JDBCUtils.closeResources(connection, ps);
    }

    // 查询Celebrity表中的Blob类型字段
    @Test
    public void testQuery() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select name, photo from celebrity where name = ?";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, "Huhu");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString(1);
                Blob photo = resultSet.getBlob(2);
                // 保存到对象
                Celebrity celebrity = new Celebrity(name);
                System.out.println(celebrity);
                // 保存Blob数据到本地
                is = photo.getBinaryStream();
                fos = new FileOutputStream(new File("new_Huhu.jpg"));
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResources(connection, ps, resultSet);
        }


    }
}
