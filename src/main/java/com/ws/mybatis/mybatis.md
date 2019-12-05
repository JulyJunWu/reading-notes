mappers配置: 允许两种方式
    1.mapper标签  :  单一配置(一个Mapper接口对应一个xml) , 三种配置方式
        1.1.resource : 通过*Mapper.xml路径配置
        1.2.url      : 通过url配置
        1.3.class    : 通过类的全限定类名配置
    2.package标签 :  配置扫描包路径(批量扫描)
        使用方式  :  name属性配置为Mapper接口的路径,Mapper配置也和Mapper接口在同一路径下
        
Configuration:
    Properties variables; 存放定义的Properties属性
SqlSessionFactory -> DefaultSqlSessionFactory
SqlSession -> DefaultSqlSession
TransactionIsolationLevel ; 事物的级别枚举
ExecutorType: Executor类型,枚举有三个值:SIMPLE,REUSE,BATCH; 默认是SIMPLE, 如果是批量的sql语句的话BATCH类型更实用;
Executor ->  BatchExecutor  -->  ExecutorType.SIMPLE
             ReuseExecutor  -->  ExecutorType.REUSE
             SimpleExecutor --> ExecutorType.BATCH
MapperProxyFactory : 生成Mapper接口代理类
MapperProxy : 实现InvocationHandler接口,很明显JDK的动态代理
MapperMethod
MappedStatement
ResultHandler

理论上mybatis是可以不需要Mapper接口的,因为可以通过命名空间+id进行访问;
    
解析配置文件中${},如${driver}:
    XPathParser : 解析器
        Properties variables; 存放自定义的Properties属性以及读取外部的Properties配置
    真正解析是在XNode构造函数中调用私有函数parseAttributes解析${},最终调用静态方法PropertyParser.parse完成解析

Properties属性添加的三种方式(优先级顺序导读):
    1.在构建SqlSessionFactoryBuilder时build Properties属性,如下:
        new SqlSessionFactoryBuilder().build(reader, properties);
    2.通过配置读取外部Properties文件,如下:
        <properties resource="mybatis/jdbc.properties"/>
    3.通过配置文件配置properties属性,如下:
            <properties >
                <property name="custom" value="1"/>
            </properties>
    上述三种方式优先级是 1 > 2 > 3 , 也就是说相同的属性,那么最终的值是以1为准;
    源码地址 -> XMLConfigBuilder.propertiesElement