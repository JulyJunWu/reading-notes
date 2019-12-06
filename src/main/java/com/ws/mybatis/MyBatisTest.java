package com.ws.mybatis;

import com.ws.mybatis.dao.UserMapper;
import com.ws.mybatis.model.SexEnum;
import com.ws.mybatis.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

/**
 * @author JunWu
 * mybatis测试
 */
@Slf4j
public class MyBatisTest {

    public static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            Reader reader = Resources.getResourceAsReader("mybatis/mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * 配置创建
     */
    @Test
    public void select() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = mapper.selectById("199ae857118111eab6558c16457fff38");
        log.info("{}", user);
        session.close();
    }

    /**
     * 代码创建Configuration,生成SqlSessionFactory
     * <p>
     * show status like '%thread%' 查看mysql连接线程
     */
    @Test
    public void create() {
        // 设置数据源属性
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/ws?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("admin");
        dataSource.setDriver("com.mysql.jdbc.Driver");
        // 创建环境
        Environment environment = new Environment("manual", new JdbcTransactionFactory(), dataSource);
        // 创建配置中心
        Configuration configuration = new Configuration(environment);
        // 注册实体别名
        configuration.getTypeAliasRegistry().registerAlias("user", User.class);
        /**
         * 添加映射器 这种方式等同配置里配置如下
         * <mappers>
         *     <package name="com.ws.mybatis.dao"/>
         * </mappers>
         */
        configuration.addMappers("com.ws.mybatis.dao");

        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        UserMapper mapper = sessionFactory.openSession().getMapper(UserMapper.class);
        User user = mapper.selectById("199ae857118111eab6558c16457fff38");
        log.info("{}", user);
    }

    /**
     * 不用Mapper接口
     * 直接通过命名空间 + id 查询
     */
    @Test
    public void createWithoutMapper() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Object selectOne = sqlSession.selectOne("test.selectById", "199ae857118111eab6558c16457fff38");
        log.info("{}", selectOne);
    }

    /**
     * 测试 Properties配置读取的优先级问题
     * 优先级顺序 代码构造函数的Properties > 读取外部Properties配置 > 配置properties属性
     *
     * @see XMLConfigBuilder#propertiesElement(XNode)
     */
    @Test
    public void testProperties() throws Exception {
        Reader reader = Resources.getResourceAsReader("mybatis/mybatis-config.xml");
        Properties properties = new Properties();
        properties.setProperty("custom", "3");

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, properties);

        String custom = sqlSessionFactory.getConfiguration().getVariables().getProperty("custom");
        // 3
        log.info("{}", custom);

    }

    /**
     * 测试TypeHandler属性转换器
     */
    @Test
    public void testTypeHandler() {
        SqlSessionFactory sessionFactory = sqlSessionFactory;
        SqlSession sqlSession = sessionFactory.openSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectById("199ae857118111eab6558c16457fff38");
        log.info("{}", user);

        user.setId("199ae857118111eab6558c164578ff98");
        user.setAge(88);
        user.setName("念念不忘");
        user.setSex(SexEnum.MALE);

        mapper.insert(user);
        sqlSession.commit();
        sqlSession.close();
    }

    /**
     * jdbc测试
     *
     * @throws Exception
     */
    @Test
    public void jdbc() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ws?useSSL=false", "root", "admin");
        PreparedStatement statement = connection.prepareStatement("select * from user where id = ?");
        statement.setString(1, "199ae857118111eab6558c16457fff98");
        statement.execute();

        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String sex = resultSet.getString("sex");
            log.info("id -> {} , name -> {} , age -> {} , sex -> {}", new Object[]{id, name, age, sex});
        }
    }

    /**
     * 测试ObjectFactory
     */
    @Test
    public void testObjectFactory() {
        UserMapper userMapper = sqlSessionFactory.openSession().getMapper(UserMapper.class);
        User user = userMapper.selectById("199ae857118111eab6558c16457fff98");
        log.info("{}", user);

        Map map = userMapper.selectMapById("199ae857118111eab6558c16457fff98");
        log.info("{}",map);

    }
}
