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
    boolean lazyLoadingEnabled; 延迟加载开关,默认为false;
    Map<String, MappedStatement> mappedStatements; 
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
SqlCommandType ：sql的类型,如select|update|insert|delete等

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

XMLMapperBuilder : 解析Mapper.xml文件,构造属性;    

缓存:
    默认开启一级缓存(一级缓存只是相对于同一个SqlSession而言);
    二级缓存; 默认全局开关是开启,但是还需要在mapper.xml文件中开启; MappedStatement级别缓存;
    当开启二级缓存时,优先从二级缓存中获取Cache,如果获取为null,则通过委托查找,查找显示从一级缓存LocalCache中获取,
如果获取不到,最后才是从数据库获取,然后放入一级缓存;二级缓存暂时放入到CachingExecutor中的tcm中,只有最终SqlSession执行了commit或者close,才会放入
       注意: 需要将SqlSession close或者commit才会缓存到MS中
       开启方式,直接在需要缓存的配置中添加如下
            <cache/>
       注意:
          1.映射语句文件中的所有select语句将会被缓存。
          2.映射语句文件中的所有insert、update和delete 语句会刷新缓存。
          3.缓存会使用默认的Least Recently Used (LRU,最近最少使用的)算法来收回。
          4.根据时间表，比如No Flush Interval,(CNFI, 没有刷新间隔)，缓存不会以任何时间顺序来刷新。
          5.缓存会存储列表集合或对象(无论查询方法返回什么)的1024个引用。
          6.缓存会被视为是read/write (可读/可写)的缓存，意味着对象检索不是共享的，而且可以安全地被调用者修改，不干扰其他调用者或线程所做的潜在修改。
    查询缓存的源码:  
        CachingExecutor.query:
            1.从MappedStatement中获取缓存Cache
            2.当开启了二级缓存:
                2.1是否配置了flushCache=true,如果是则刷新缓存;
                2.2从CachingExecutor中的tcm变量获取结果,其实就是一层层包装,最终还是从MS中的Cache中获取结果;
                2.3结果不为null,直接返回结果;
                2.4结果为null,执行第三步;
                2.5将数据库查询结果存入tcm中暂缓(此时还未加入到缓存中)
                2.6结果返回;
            3.当未开启二级缓存,则直接委托查询;
    放入缓存的源码:
        DefaultSqlSession.close/commit : 执行sqlSession.close释放资源
            TransactionalCacheManager.commit: 对map的values进行迭代,得到TransactionalCache
                TransactionalCache.commit : 调用自身的flushPendingEntries
                    TransactionalCache.flushPendingEntries: 最终使用持有MS的Cache引用进行放入缓存,这个时候才是正真的放入缓存;
                    TransactionalCache.reset : 重置将加入缓存的数据,置空;
CachingExecutor: 装饰模式

@Param 注解解析:
    ParamNameResolver: 解析注解并存储相关值
        SortedMap<Integer, String> names; 存储解析后的值,一个有序的map
        初次解析
            ParamNameResolver.ParamNameResolver : 在构造函数中解析
            1.如果参数含有@Param注解,那么获取该注解的value值,以参数索引位置为key,value值为value存入map中(这个map是TreeMap);
            2.如果未找到@Param注解,那么获取参数的名称作为值,同样以参数索引位置为key,参数的名称为value存入map中;
                注意:: 在mybatis的解析得出的参数名称最终是以 arg +　参数索引下标　作为参数名称的，例如：　
                get(String id , int age) , 则解析得出 id -> arg0, age -> arg1 ;
        最终解析:初次解析只是为最终解析作铺垫工作,最终的参数是以此处为准;
            MapperMethod.execute
                MethodSignature.convertArgsToSqlCommandParam
                    ParamNameResolver.getNamedParams
            1.创建一个新的map,取名为param
            2.迭代初次解析中的map(就是变了为names的SortedMap)
                2.1.以初步解析的map的value为key,以参数的值为value(真正的传入的参数实际值),存入param中,
                2.2.在以param + (参数索引下标 + 1) 作为 key,然后将参数的值作为value,存入param中;
        例子: 以getUser(@Param("id") String id , int age): 假设id -> "888" , age -> 18;
              那么最终解析得出的map如下:
                map.put("id","888")            
                map.put("param1","888")            
                map.put("agr1",18)            
                map.put("param2",18)            