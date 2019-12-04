mappers配置: 允许两种方式
    1.mapper标签  :  单一配置(一个Mapper接口对应一个xml) , 三种配置方式
        1.1.resource : 通过*Mapper.xml路径配置
        1.2.url      : 通过url配置
        1.3.class    : 通过类的全限定类名配置
    2.package标签 :  配置扫描包路径(批量扫描)
        使用方式  :  name属性配置为Mapper接口的路径,Mapper配置也和Mapper接口在同一路径下
        
Configuration
SqlSessionFactory -> DefaultSqlSessionFactory