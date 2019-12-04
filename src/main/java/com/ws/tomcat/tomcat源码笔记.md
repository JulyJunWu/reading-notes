不规范笔记:
    -verbose:class 设置jvm参数 -> 控制台打印出哪个类是由哪个jar加载的
    默认8005端口监听SHUTDOWN命令关闭程序
    默认address为localhost监听地址
    Poller thread count: 2个
    容器 : 自上而下容器顺序,顶层容器不能拥有父容器,底层容器不能拥有子容器
           Engine, Host , Context , Wrapper;
           Wrapper的父容器只能是Context;
           Wrapper添加子容器直接报错;
    容器与Pipeline和Valve关系: 相当于Netty中Channel,ChannelPipeLine,ChannelContextHandler
        每个容器持有一个Pipeline
        每个Pipeline持有基础的Valve和Valve顶层引用;
        Valve是一种链表结构,next获取下一个Valve;
    Valve链表最后一个一定是basic,具体的basics实现看各自的容器是什么,比如Host , 那么basic的实现就是StandardHostValve,以此类推;
    Server:
        List<LifecycleListener> lifecycleListeners;
        GlobalNamingResources globalNamingResources;
        Service [] service;
    Service:
        Connector [] connector;        
        Engine engine;
    Engine:
        Realm realm;   
        Host [] host;
        Pipeline Pipeline;
    Host host;
        Context [] Context;
        Pipeline Pipeline;
    Wrapper wrapper;
        Pipeline Pipeline;
    Pipeline :
        Valve basic;
        Valve first;
    启动类 : Bootstrap.main
    tomcat启动所需要的配置文件路径: 具体源码请看org.apache.catalina.startup.Bootstrap P66
        1.如果有 System.getProperty("catalina.home")有值则使用该目录作为配置文件路径;
        2.不满足1,则使用System.getProperty("user.dir")的路径作为配置文件路径;
        3.设置系统属性catalina.home
        4.设置系统属性catalina.base
    Bootstrap持有3个类加载器引用,分别为:   
        1.ClassLoader commonLoader
        2.ClassLoader catalinaLoader
        3.ClassLoader sharedLoader
    Bootstrap构造器没有做P事
    init():
        initClassLoaders(): 
            1.创建commonLoader类加载器(createClassLoader)
                1.1从catalina.properties配置文件寻找common.loader属性作为类加载器的范围(加载路径)
                1.构建类加载器,如果有父类加载new URLClassLoader(array,parent),否则new URLClassLoader(array) , array就是上述加载路径经过转换得到的
            2.创建catalinaLoader类加载器(createClassLoader)
                1.将commonLoader类加载器作为参数传入;
                2.从catalina.properties配置文件寻找server.loader属性作为类加载器的范围(加载路径)
                3.若2值为空,则将commonLoader类加载器作为catalinaLoader类加载器;
                4.若2不空,则将commonLoader作为父加载器;
            3.创建sharedLoader类加载器
                步骤同2
            默认情况下commonLoader,catalinaLoader,sharedLoader都是同一个实例;
        将catalinaLoader类加载器作为线程下上下文类加载器;
        使用catalinaLoader类加载器加载类Catalina,而后反射创建Catalina实例(Catalina构造就是设置了下需要安全认证的包路径);
        将sharedLoader设置将Catalina实例的parentClassLoader属性设为sharedLoader类加载器;
    设置Bootstrap实例为true阻塞 : false的话直接程序执行完毕就结束了,只有阻塞程序才会不停的运转,直接接受SHUTDOWN关闭命令
    load: 反射执行catalina.load()
        Catalina.load()流程: 主要是启动Server实例
            1.initDirs() 临时目录验证 (System.getProperty("java.io.tmpdir"))
            2.initNaming()  主要是设置一些环境变量,是否使用命名
            3.创建解析器,用来解析server.xml变成对象的过程; 解析器包含了容器的默认实现类, org.apache.catalina.startup.Catalina.createStartDigester 
    Server init() : 执行各种生命周期, 然后各种触发观察者事件
            
    