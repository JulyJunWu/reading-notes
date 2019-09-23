package com.ws.framework.jdbc;

import com.ws.framework.model.Person;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import java.lang.reflect.Method;
import java.sql.*;
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

        try (
                //SPI ServiceLoader自动加载驱动包 了解下线程上下文类加载器
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
                //使用PreparedStatement预编译
                CallableStatement statement = connection.prepareCall("select age , name from user where id = ? ")
        ) {

            //处理字符串参数, mysql会将"1" -> "'1'"
            statement.setString(1, "1");

            ResultSet resultSet = statement.executeQuery();
            Method[] methods = Person.class.getMethods();

            //把指针移到最后一行
            resultSet.last();
            //获取数量
            int row = resultSet.getRow();

            //大于0代表 有返回数据
            if (row > 0) {
                List<Person> list = new ArrayList<>(row);
                //指针重新移到最初始位置,以便于后续的读取数据
                resultSet.beforeFirst();

                while (resultSet.next()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    Person person = new Person();

                    IntStream.range(1, columnCount + 1).forEach(p -> {

                        try {
                            String columnName = metaData.getColumnName(p);
                            String className = metaData.getColumnClassName(p);

                            Class<?> aClass = Class.forName(className);

                            Object object = resultSet.getObject(p);

                            char[] chars = columnName.toCharArray();
                            chars[0] = Character.toUpperCase(chars[0]);

                            String newColumnName = PREFIX + new String(chars);

                            IntStream.range(0, methods.length).forEach(p2 -> {

                                Method method = methods[p2];
                                if (method.getName().equals(newColumnName)) {
                                    Class<?> parameterType = method.getParameterTypes()[0];
                                    method.setAccessible(true);

                                    try {
                                        Class<?> aClass1 = aClass;
                                        if (parameterType.isPrimitive()) {
                                            aClass1 = ORIGINAL.get(aClass);
                                        }

                                        if (aClass1 == parameterType) {
                                            method.invoke(person, object);
                                        }

                                    } catch (Exception e) {
                                        throw new RuntimeException("注入属性报错!");
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    list.add(person);
                }

                list.stream().forEach(System.out::println);
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

}
