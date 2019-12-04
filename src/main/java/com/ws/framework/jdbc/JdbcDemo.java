package com.ws.framework.jdbc;

import com.ws.framework.model.Person;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Test;

import java.lang.reflect.Method;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @Description:
 * @Date: 2019/8/15 0015 11:19
 * <p>
 * <p>
 * 对数据库mysql int 类型字段进行模糊查询
 * select id , age , name from `user` where  cast(age as char) like '%7%' ;
 */
public class JdbcDemo {


    private static final String PREFIX = "set";

    private static final Map<Class<?>, Class<?>> ORIGINAL = new HashMap<>(16);

    static {
        ORIGINAL.put(Integer.class, int.class);
        ORIGINAL.put(Boolean.class, boolean.class);
        ORIGINAL.put(Byte.class, byte.class);
        ORIGINAL.put(Short.class, short.class);
        ORIGINAL.put(Character.class, char.class);
        ORIGINAL.put(Double.class, double.class);
        ORIGINAL.put(Float.class, float.class);
        ORIGINAL.put(Long.class, long.class);
    }


    @Test
    public void query() {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ws", "root", "admin");
            //使用PreparedStatement预编译
            PreparedStatement prepareStatement = connection.prepareStatement("UPDATE USER SET NAME = '大宝'  WHERE age = 1  AND @now := NOW() LIMIT 1 ");
            prepareStatement.executeUpdate();
            ResultSet resultSet = connection.prepareStatement("SELECT @now").executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getObject(1));
            }

            connection.close();

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ws", "root", "admin");
            resultSet = connection.prepareStatement("SELECT @now").executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getObject(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void batchInsert(){

        try (
                //SPI ServiceLoader自动加载驱动包 了解下线程上下文类加载器
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
                //使用PreparedStatement预编译
                CallableStatement statement = connection.prepareCall("insert into test values (?,?,?,?)")
        ) {

            IntStream.range(100000,200000).forEach(p->{

                try{
                    statement.setString(1, UUID.randomUUID().toString().replace("-",""));
                    statement.setInt(2, RandomUtils.nextInt(10000000));
                    statement.setInt(3,RandomUtils.nextInt(10000000));
                    statement.setString(4,"ws" + p);
                    statement.executeUpdate();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void test(){
        String str = "/api/sys/sxzl/admYhtjFwsxfgdDf/addAdmYhtjFwsxfgdDf";
        String regex = "/.*/rest/sys/.*";
        System.out.println(str.matches(regex));

    }

}
