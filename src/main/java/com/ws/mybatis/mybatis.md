mappers配置: 允许两种方式
    1.mapper标签  :  单一配置(一个Mapper接口对应一个xml) , 三种配置方式
        1.1.resource : 通过*Mapper.xml路径配置
        1.2.url      : 通过url配置
        1.3.class    : 通过类的全限定类名配置
    2.package标签 :  配置扫描包路径(批量扫描)
        使用方式  :  name属性配置为Mapper接口的路径,Mapper配置也和Mapper接口在同一路径下
        
Configuration:
    Properties variables; 存放定义的Properties属性
    TypeHandlerRegistry typeHandlerRegistry; 存放已注册的TypeHandler
    TypeAliasRegistry typeAliasRegistry; 存放已注册的别名
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
ObjectFactory

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
  
settings标签解析:
    1.将setting标签属性解析转换为Properties
    2.设置configuration属性,从properties获取,无则设置默认变量(Configuration设置属性源码 -> XMLConfigBuilder.settingsElement )   
    
typeAliases标签解析(注册别名服务):
    二种配置方式:
        方式一: 使用package标签,将此路径下的所有的类注册别名;将类名变成小写作为别名;
                如果该类拥有@Alias标签,则以此值作为别名;
        方式二: 使用typeAlias标签注册单个类别名;
   TypeAliasRegistry: 别名注册服务类,默认已经注册大量常用类别名
        Map<String, Class<?>> TYPE_ALIASE;  存放别名缓存; 
        注册参考:
            this.registerAlias("object", Object.class);
            this.registerAlias("date[]", Date[].class);
    解析源码: XMLConfigBuilder.typeAliasesElement         

TypeHandlerRegistry : 存放注册的typeHandler
typeHandlers: 对数据库返回数据进行类型转换为所需类型,如 String -> Enum;
    使用方式一: 如User类中包含一个枚举属性,那么注册一个该枚举属性转换器即可(TypeHandler),然后将转换器注册到mybatis中,这样就可以了;注册示例如下:
        <typeHandlers>
            <typeHandler handler="com.ws.mybatis.typehandler.StringToSexEnum" />
        </typeHandlers>
    使用方式二:
        无需注册转换器,直接在xxxMapper.xml中 为字段指定typeHandler="com.xxx.xxxTypeHandler"即可
        
ObjectFactory -> DefaultObjectFactory
    作用: 当MyBatis在构建一个结果返回的时候，都会使用ObjectFactory (对象工厂)去构建POJO接受对应的返回数据;

plugins(其实就是Interceptor): 拦截器
    
resultMap标签:
    一对一标签 : <association></association>
    一对多标签 : <collection></collection>
    鉴别器标签