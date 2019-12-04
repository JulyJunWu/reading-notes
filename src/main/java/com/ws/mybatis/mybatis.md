mappers配置: 允许两种方式
    1.mapper标签  :  单一配置(一个Mapper接口对应一个xml) , 三种配置方式
        1.1.resource : 通过*Mapper.xml路径配置
        1.2.url      : 通过url配置
        1.3.class    : 通过类的全限定类名配置
    2.package标签 :  配置扫描包路径(批量扫描)
        使用方式  :  name属性配置为Mapper接口的路径,Mapper配置也和Mapper接口在同一路径下
        
Configuration
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
    
    
