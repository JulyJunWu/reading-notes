package com.ws.mybatis;

import com.ws.mybatis.cglib.UserServiceCglib;
import com.ws.mybatis.dao.UserMapper;
import com.ws.mybatis.model.SexEnum;
import com.ws.mybatis.model.User;
import com.ws.mybatis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

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
        log.info("{}", map);
    }

    /**
     * 插件 plugins(其实就是Interceptor) 测试
     */
    @Test
    public void testInterceptor() {
        UserMapper userMapper = sqlSessionFactory.openSession().getMapper(UserMapper.class);
        User user = userMapper.selectById("199adfb8118111eab6558c16457fff38");
        List<User> users = userMapper.selectAll(null);
        log.info("{}", user.getName());
    }

    /**
     * 测试一级缓存
     */
    @Test
    public void testCache() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectById("199adfb8118111eab6558c16457fff38");
        User user2 = mapper.selectById("199adfb8118111eab6558c16457fff38");
        // true,说明在同一个SqlSession中,多次获取相同参数 相同SQL,第二次以后都是从缓存中获取;
        log.info("{}", user == user2);
        // 清理缓存
        sqlSession.clearCache();
        User user3 = mapper.selectById("199adfb8118111eab6558c16457fff38");
        log.info("{}", user == user3);

        sqlSession.close();
        sqlSession = sqlSessionFactory.openSession();
        User user4 = sqlSession.getMapper(UserMapper.class).selectById("199adfb8118111eab6558c16457fff38");
        // false,一级缓存失效
        log.info("{}", user == user4);
        sqlSession.close();
        sqlSession.commit();
    }

    @Test
    public void testSecondCache() throws Exception {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = sqlSession.getMapper(UserMapper.class).selectById("199ae424118111eab6558c16457fff38");
        // 只有在关闭或者commit时 才会缓存二级缓存;
        sqlSession.close();

        // 直接取二级缓存
        /**
         * @see CachingExecutor#query 取二级缓存源码
         */
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        user = sqlSession2.getMapper(UserMapper.class).selectById("199ae424118111eab6558c16457fff38");
        sqlSession2.close();
        // 97
        log.info("age -> {}", user.getAge());

        TimeUnit.SECONDS.sleep(10);
        SqlSession session = sqlSessionFactory.openSession();
        User user3 = session.getMapper(UserMapper.class).selectById("199ae424118111eab6558c16457fff38");
        // 1 -> 说明设置缓存过期时间有效果
        log.info("age -> {}", user3.getAge());

        user.setId("199ae424118111eab6558c16457ff1212");
        session.getMapper(UserMapper.class).insert(user);
        session.commit();
    }

    /**
     * 测试cglib代理
     */
    @Test
    public void testCglib() {
        UserService userService = new UserService();
        UserServiceCglib serviceCglib = new UserServiceCglib();
        UserService instance = (UserService) serviceCglib.getInstance(userService);
        instance.getUser();
    }

    /**
     * 测试@Param注解
     */
    @Test
    public void testParamAnnotation() {
        UserMapper mapper = sqlSessionFactory.openSession().getMapper(UserMapper.class);
        User user = mapper.selectByParams(null, "ws", 18);
        log.info("{}", user);
    }

    /**
     * 测试mybatis工具 MetaObject
     */
    @Test
    public void testMetaObject() {
        User user = new User();
        user.setName("ws");
        user.setId("678");
        HashMap<Object, Object> map = new HashMap<>(2);
        map.put("user", user);

        MetaObject metaObject = SystemMetaObject.forObject(map);
        Object value = metaObject.getValue("user.name");

        log.info("user.name -> {}", value);

        metaObject = SystemMetaObject.forObject(user);
        value = metaObject.getValue("name");
        log.info("name -> {}", value);
    }

    /**
     * spring整合mybatis
     */
    @Test
    public void testSpringMybatis() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("mybatis/spring-mybatis.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectById("199adfb8118111eab6558c16457fff38");
        log.info("{}", user);
    }

    /**
     * SqlSessionTemplate
     */
    @Test
    public void testSqlSessionTemplate() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("mybatis/spring-mybatis.xml");
        SqlSessionTemplate sessionTemplate = applicationContext.getBean(SqlSessionTemplate.class);
        Object one = sessionTemplate.selectOne("test.selectById", "199adfb8118111eab6558c16457fff38");
        log.info("{}", one);
    }

    /**
     * 使用mybatis更新多条sql
     */
    @Test
    public void testUpdateList(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<User> allUser = sqlSession.selectList("test.selectAll");
        allUser.forEach(p->p.setAge(88));
        sqlSession.update("test.updateUserList",allUser);
        sqlSession.commit();
    }
}

