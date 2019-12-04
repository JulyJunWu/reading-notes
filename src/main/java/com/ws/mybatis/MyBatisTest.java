package com.ws.mybatis;

import com.ws.mybatis.dao.UserMapper;
import com.ws.mybatis.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author JunWu
 * mybatis测试
 */
@Slf4j
public class MyBatisTest {

    /**
     * 配置创建
     */
    @Test
    public void select() {
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("mybatis/mybatis-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sessionFactory.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);

        User user = mapper.selectById("199ae857118111eab6558c16457fff38");
        System.out.println(user);
    }

    /**
     * 代码创建Configuration,生成SqlSessionFactory
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
        Environment environment = new Environment("manual", new JdbcTransactionFactory(),dataSource );
        // 创建配置中心
        Configuration configuration = new Configuration(environment);
        // 注册实体别名
        configuration.getTypeAliasRegistry().registerAlias("user",User.class);
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
        System.out.println(user);
    }
}
