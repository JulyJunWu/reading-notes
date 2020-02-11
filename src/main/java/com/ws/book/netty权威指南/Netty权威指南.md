Linux 网络1/O模型简介:
    Linux的内核将所有外部设备都看做一个文件来操作，对一个文件的读写操作会调用内核提供的系统命令，返回一个file descriptor 
(fd,文件描述符)。而对一个socket 的读写也会有相应的描述符，称为socketfd ( socket描述符)，描述符就是一个数字，它指向内核中
的一个结构体(文件路径，数据区等一些属性)。
    根据UNIX网络编程对I/O 模型的分类，UNIX提供了5种1/0模型，分别如下。
    (1)阻塞I/O模型:最常用的I/O模型就是阻塞I/O模型，缺省情形下，所有文件操作都是阻塞的。我们以套接字接口为例来讲解此模型:
在进程空间中调用recvfrom, 其系 统调用直到数据包到达且被复制到应用进程的缓冲区中或者发生错误时才返回，在此期间 一直会等待，
进程在从调用recvfrom开始到它返回的整段时间内都是被阻塞的，因此被称为阻塞I/O模型，如图1-1所示。
    (2)非阻塞I/O模型: recvfrom从应用层到内核的时候，如果该缓冲区没有数据的话，就直接返回-个EWOULDBLOCK错误，一般都对非阻
塞I/O模型进行轮询检查这个状态，看内核是不是有数据到来，如图1-2所示。
    (3)I/O复用模型:Linux提供select/poll,进程通过将-~个或多个fd传递给select或poll系统调用，阻塞在selct操作上，这样select/poll
可以帮我们侦测多个fd是否处于就绪状态。select/poll 是顺序扫描fd是否就绪，而且支持的fd数量有限，因此它的使用受到了一些制约。
Linux 还提供了一个epoll系统调用，epoll 使用基于事件驱动方式代替顺序扫描，因此性能更高。当有fd就绪时，立即回调函数rollback,
如图1-3所示。
    (4)信号驱动I/O模型:首先开启套接口信号驱动I/O功能，并通过系统调用sigaction执行一个信号处理函数(此系统调用立即返回，进
继续工作，它是非阻塞的)。当数据准备就绪时，就为该进程生成一个SIGIO信号，通过信号回调通知应用程序调用recvfrom来读取数据，
通知主循环函数处理数据，如图1-4所示。    
    (5)异步I/O: 告知内核启动某个操作，并让内核在整个操作完成后(包括将数据从内核复制到用户自己的缓冲区)通知我们。这种模型
信号驱动模型的主要区别是:信号驱动I/O由内核通知我们何时可以开始一个I/O操作;异步I/O 模型由内核通知我们I/O操作何时已经完成，
图1-5所示。
    Java NIO的核心类库多路复用器Selector是基于epoll的多路复用技术实现。

I/O多路复用:
    在I/O编程过程中，当需要同时处理多个客户端接入请求时，可以利用多线程或者I/O多路复用技术进行处理。I/O多路复用技术通过
把多个I/O的阻塞复用到同一个select的阻塞上，从而使得系统在单线程的情况下可以同时处理多个客户端请求。与传统的多线程/多进程
模型比，I/O 多路复用的最大优势是系统开销小，系统不需要创建新的额外进程或者线程，也不需要维护这些进程和线程的运行，降低了
系统的维护工作量，节省了系统资源，I/O多路复用的主要应用场景如下。
         1.服务器需要同时处理多个处于监听状态或者多个连接状态的套接字;
         2.服务器需要同时处理多种网络协议的套接字。
    epoll对select的改进:
    1.支持一个进程打开的socket描述符( FD )不受限制(仅受限于操作系统的最大文件句柄数)。
    select最大的缺陷就是单个进程所打开的FD是有一定限制的，它由FD_ SETSIZE设置，默认值是1024。 epoll并没有这个限制，它所
支持的FD上限是操作系统的最大文件句柄数，这个数字远远大于1024。例如，在1GB内存的机器上大约是10万个句柄左右，具体的值可以
通过cat /proc/sys/fs/file-max察看，通常情况下这个值跟系统的内存关系比较大。
    2. I/O 效率不会随着FD数目的增加而线性下降。
        传统select/poll的另一个致命弱点，就是当你拥有一个很大的socket集合时，由于网络延时或者链路空闲，任一时刻只有少部
分的socket是“活跃”的，但是select/poll 每次调用都会线性扫描全部的集合，导致效率呈现线性下降。epoll不存在这个问题，它只
会对“活跃”的socket进行操作---这是因为在内核实现中,epoll是根据每个fd上面的callback函数实现的。那么，只有“活跃”的socket
才会去主动调用callback函数，其他idle状态的socket则不会。
    3.使用mmap加速内核与用户空间的消息传递。
        无论是select、poll还是epoll都需要内核把FD消息通知给用户空间，如何避免不必要的内存复制就显得非常重要;
        epoll是通过内核和用户空间mmap同一块内存来实现的。
    4.epoll 的API更加简单。
    
使用NIO编程的优点总结如下。
    (1)客户端发起的连接操作是异步的，可以通过在多路复用器注册OP CONNECT等待后续结果，不需要像之前的客户端那样被同步阻塞。
    (2)SocketChannel的读写操作都是异步的，如果没有可读写的数据它不会同步等待，直接返回，这样I/O通信线程就可以处理其他的
链路，不需要同步等待这个链路可用。
    (3)线程模型的优化:由于JDK的Selector在Linux等主流操作系统上通过epoll实现，它没有连接句柄数的限制(只受限于操作系统的最
大句柄数或者对单个进程的句柄限 制)，这意味着一个Selector 线程可以同时处理成千上万个客户端连接，而且性能不会随着客户端的
增加而线性下降。因此，它非常适合做高性能、高负载的网络服务器。

粘包和拆包:
    假设客户端分别发送了两个数据包D1和D2给服务端，由于服务端一次读取到的字节数是不确定的，故可能存在以下4种情况。
    (1)服务端分两次读取到了两个独立的数据包，分别是D1和D2，没有粘包和拆包:
    (2)服务端一次接收到了两个数据包，D1和D2粘合在一起，被称为TCP粘包;
    (3)服务端分两次读取到了两个数据包，第一次读取到了完整的D1包和D2包的部分内容，第二次读取到了D2包的剩余内容，这被称为TCP拆包;
    (4)服务端分两次读取到了两个数据包，第1次读取到了D1包的部分内容D1_1,第2次读取到了D1包的剩余内容D1_2和D2包的整包。


解决粘包和拆包问题:
  TCP以流的方式进行数据传输，上层的应用协议为了对消息进行区分，往往采用如下4种方式。
    (1)消息长度固定，累计读取到长度总和为定长LEN的报文后，就认为读取到了一个完整的消息:将计数器置位，重新开始读取下一个
数据报;如FixedLengthFrameDecoder;
    (2)将回车换行符作为消息结束符，例如FTP协议，这种方式在文本协议中应用比较广泛;如:DelimiterBasedFrameDecoder
    (3)将特殊的分隔符作为消息的结束标志，回车换行符就是一种特殊的结束分隔符;
    (4)通过在消息头中定义长度字段来标识消息的总长度。LengthFieldBasedFrameDecoder解码,LengthFieldPrepender编码

    
JAVA序列化的缺陷:
    1.无法跨语言;
      由于Java序列化技术是Java语言内部的私有协议，其他语言并不支持;
    2.序列化后的码流太大

    
HTTP协议介绍:
    HTTP是一个属于应用层的面向对象的协议，由于其简捷、快速的方式，适用于分布式超媒体信息系统。
    HTTP协议的主要特点如下。
        1.支持Client/Server模式;
        2.简单一客户向服务器请求服务时，只需指定服务URL,携带必要的请求参数或者消息体;
        3.灵活一-HTTP允许传输任意类型的数据对象，传输的内容类型由HTTP消息头中的Content-Type加以标记;
        4.无状态一-HTTP协议是无状态协议，无状态是指协议对于事务处理没有记忆能力。缺少状态意味着如果后续处理需要之前的信息，
则它必须重传，这样可能导致每次连接传送的数据量增大。另一方面，在服务器不需要先前信息时它的应答就较快，负载较轻。
    HTTP协议的URL:
        HTTP URL(URL是一种特殊类型的URI,包含了用于查找某个资源的足够的信息)的格式如下。
        http://host[":"port] [abs_ path]
    
   HTTP请求消息(HttpRequest):
        HTTP请求由三部分组成，具体如下。
        1.HTTP 请求行;
            请求行以一个方法符开头，以空格分开，后面跟着请求的URI和协议的版本，格式为:
            Method Request-URI HTTP-Version CRLF。
        2.HTTP 消息头;
        3.HTTP请求正文。
    GET和POST的主要区别如下:
        (1)根据HTTP规范，GET用于信息获取，而且应该是安全的和幂等的; POST则表示可能改变服务器上的资源的请求。
        (2)GET提交，请求的数据会附在URL之后，就是把数据放置在请求行(request line)中，以“?”分隔URL和传输数据，多个参数
用“&”连接;而POST提交会把提交的数据放置在HTTP消息的包体中，数据不会在地址栏中显示出来。
        (3)传输数据的大小不同。特定浏览器和服务器对URL长度有限制，例如IE对URL长度的限制是2083字节(2KB+35B),因此GET携带的
参数的长度会受到浏览器的限制:而POST由于不是通过URL传值，理论上数据长度不会受限。
        (4)安全性。POST的安全性要比GET的安全性高。比如通过GET提交数据，用户名和密码将明文出现在URL上。
           因为1)登录页面有可能被浏览器缓存;2)其他人查看浏览器的历史记录，那么别人就可以拿到你的账号和密码了。除此之外，
使用GET提交数据还可能会造成Cross-site request forgery攻击。POST提交的内容由于在消息体中传输，因此不存在上述安全问题。

   HTTP响应消息( HttpResponse ):
      HTTP响应也是由三个部分组成，分别是:状态行、消息报头、响应正文。
      状态行: 格式为 HTTP-Version Status-Code Reason-Phrase CRLF,
              其中HTTP-Version表示服务器HTTP协议的版本，
              Status-Code表示服务器返回的响应状态代码。
              状态代码由三位数字组成，第一个数字定义了响应的类别，它有5种可能的取值。
              (1) 1xx: 指示信息。表示请求已接收，继续处理;
              (2) 2xx: 成功。表示请求已被成功接收、理解、接受;
              (3) 3xx: 重定向。要完成请求必须进行更进一步的操作;
              (4) 4xx: 客户端错误。请求有语法错误或请求无法实现;
              (5) 5xx: 服务器端错误。服务器未能处理请求。


私有协议栈开发:
  1.基本功能:
    (1)基于Netty的NIO通信框架，提供高性能的异步通信能力;
    (2)提供消息的编解码框架，可以实现POJO的序列化和反序列化;
    (3)提供基于IP地址的白名单接入认证机制;
    (4)链路的有效性校验机制;
    (5)链路的断连重连机制。
  2.通信模型:
     图:12-2Netty协议栈通讯交互图.png
     具体步骤如下。
        (1) Netty协议栈客户端发送握手请求消息，携带节点ID等有效身份认证信息;
        (2) Netty 协议栈服务端对握手请求消息进行合法性校验，包括节点ID有效性校验、节点重复登录校验和IP地址合法性校验，
校验通过后，返回登录成功的握手应答消息:
        (3)链路建立成功之后，客户端发送业务消息;
        (4)链路成功之后，客户端发送心跳消息;
        (5)链路建立成功之后，服务端发送心跳响应消息;
        (6)链路建立成功之后，服务端发送业务消息;
        (7)服务端退出时，服务端关闭连接，客户端感知对方关闭连接后，被动关闭客户端连接。
   3.消息定义,对消息进行定制结构化的属性 + 扩展属性(如map,保存将来扩展需求)
       消息头
       消息体
   4.对消息进行编解码,对编解码进行规范;
   5.链路建立
     1.tcp三次握手成功后,客户端发送身份验证;
     2.服务端对消息进行验证,返回是否验证成功通知,将成功后客户端数据缓存与服务端;
     3.客户端接收服务端验证成功信息,链路建立成功,客户端与服务端可互发业务信息;
     4.客户端与服务端保持心跳;
   6.链路关闭
      客户端与服务端通过心跳保持链接,在以下情况下双方需要关闭链接:
        (1)当对方宕机或者重启时，会主动关闭链路，另一方读取到操作系统的通知信号，得知对方REST链路，需要关闭连接，释放自
身的句柄等资源。由于采用TCP全双工通信，通信双方都需要关闭连接，释放资源;
        (2)消息读写过程中，发生了1/O异常，需要主动关闭连接;
        (3)心跳消息读写过程中发生了I/O 异常，需要主动关闭连接;
        (4)心跳超时，需要主动关闭连接;
        (5)发生编码异常等不可恢复错误时，需要主动关闭连接。
    7.可靠性设计
      Netty协议栈可能会运行在非常恶劣的网络环境中，网络超时、闪断、对方进程僵死或者处理缓慢等情况,保证在极端异常场景下
Netty协议栈仍能够正常工作或者自动恢复，需要对它的可靠性进行统一规划和设计。
      1.心跳机制
      2.重连机制
      3.重复登录保护
      4.消息缓存重发
    8.安全性设计
      1.服务器对客户端请求进行认证,如IP白名单认证;
      2.如果将Netty协议栈放到公网中使用，需要采用更加严格的安全认证机制;
        例如基于密钥和AES加密的用户名+密码认证机制，也可以采用SSL/TSL安全传输。
    9.可扩展设计
      在消息定义中添加额外的字段进行可扩展,如map,后续新增需求可以直接添加入map中;
      
netty自带常用Handler:
    (1)系统编解码框架一ByteToMessageCodec; 
    (2)通用基于长度的半包解码器一LengthFieldBasedFrameDecoder;
    (3)码流日志打印Handler一LoggingHandler:
    (4)SSL安全认证Handler--SslHandler;
    (5)链路空闲检测Handler一IdleStateHandler;
    (6)流量整形Handler一ChannelTrafficShapingHandler;
    (7)Base64编解码一Base64Decoder和Base64Encoder.
  
netty读取数据代码:
    io.netty.channel.nio.AbstractNioByteChannel.NioByteUnsafe.read


##Boss线程与work线程切换 , boss线程监听accept事件后到此处将连接交给Work线程处理,该Channel注册到work上
io.netty.bootstrap.ServerBootstrap.ServerBootstrapAcceptor.channelRead
##订阅事件(通过该函数改变监听的事件) 
io.netty.channel.nio.AbstractNioChannel.doBeginRead
##BOSS接收的请求事件是ACCEPT操作,读取数据是通过UnSafe接口的实现类io.netty.channel.nio.AbstractNioMessageChannel.NioMessageUnsafe
##通过该实现类的read操作读取数据,最终是通过NIO的ServerSocketChannel.accept得到SocketChannel对象,最终创建NioSocketChannel对象;
##WORK线程接收BOSS线程所创建的NioSocketChannel对象,将该对象注册到到selector中,事件是READ操作,后续对该NioSocketChannel对象的读操作与BOSS
##线程是不一样的;

### NioServerSocketChannel和NioSocketChannel对象都拥有一个unSafe对象,但是实现类不一样,此对象是真正对数据进行读取和写入;
### NioServerSocketChannel创建的时候也会同时在内部创建原生ServerSocketChannel,并设置监听的事件是OP_ACCEPT(16)
### NioSocketChannel创建的时候同时在内部创建原生SocketChannel对象,并设置监听的事件为OP_READ(1);
###NioServerSocketChannel内部的unSafe接口使用的实现类是NioMessageUnsafe,读写,注册等操作由该实现类负责;
###NioSocketChannel内部的unSafe接口使用的实现类是NioSocketChannelUnsafe,读写,注册等操作由该实现类负责;

##NioSocketChannel添加自定义的handler的时机: 
    ChannelPipeline.fireChannelRead ##通过NioServerSocketChannel的pipeline的fireChannelRead进行触发所有的入栈Hanlder
    ServerBootstrapAcceptor.channelRead ##该对象是属于pipeline中第二个handler(如果没有指定非一添加其他handler的话) 
        1.为NioSocketChannel添加用户自定义的childHandler;
        2.将该NioSocketChannle注册到work线程中(childGroup)
##修改监听事件的时机:
  ##首次连接成功后,触发pipeline.channelActive(),最后在HeadContext中调整
  
##ByteBuffer它的主要缺点如下:
   (1) ByteBuffer长度固定，一且分配完成，它的容量不能动态扩展和收缩，当需要编 码的POJO对象大于ByteBuffer的容量时，会发生
索引越界异常;
   (2) ByteBuffer 只有一个标识位置的指针position,读写的时候需要手工调用flip(和 rewind()等，使用者必须小心谨慎地处理这些
API,否则很容易导致程序处理失败:
   (3) ByteBuffer的API功能有限，一些高级和实用的特性它不支持，需要使用者自己编程实现。

##从内存分配的角度看，ByteBuf可以 分为两类。
   (1)堆内存( HeapByteBuf) 字节缓冲区:特点是内存的分配和回收速度快，可以被JVM自动回收;缺点就是如果进行Socket 的I/O读写，
需要额外做一次内存复制，将堆内存对应的缓冲区复制到内核Channel中，性能会有一定程度的下降。
   (2)直接内存(DirectByteBuf) 字节缓冲区:非堆内存，它在堆外进行内存分配，相比于堆内存，它的分配和回收速度会慢一些，但是
将它写入或者从Socket Channel中读取时，由于少了一次内存复制，速度比堆内存快。
    正是因为各有利弊，所以Netty提供了多种ByteBuf供开发者使用，经验表明，ByteBuf的最佳实践是在I/O通信线程的读写缓冲区使用
DirectByteBuf,后端业务消息的编解码模块使用HeapByteBuf,这样组合可以达到性能最优。

##netty的引用计数 io.netty.buffer.AbstractReferenceCountedByteBuf
   通过java的AtomicIntegerFieldUpdater对变量refCnt进行原子性操作;
   refCnt: 初始值为1

io.netty.buffer.PoolArena : netty对象池

##ChannelInitializer
 该接口的作用是将多个handler注册到ChannelPipeline中,当NioSocketChannel第一次注册时,会调用handlerAdded执行initChannel函数,当添加handler完成后,
 将该ChannelHandlerContext从ChannelPipeline中移除;

###SelectedSelectionKeySet 优化类(虽然实现了Set,但是重写成使用数组了)
在NioEventLoop中使用该类替换Selector实现类(SelectorImpl)中的selectedKeys,publicSelectedKeys两个属性
源码位置:io.netty.channel.nio.NioEventLoop.openSelector()

###NioEventLoop中task类型 : 1.普通taskQueue队列  2.定时task队列scheduledTaskQueue
   定时任务中即将执行的任务会从scheduledTaskQueue队列弹出并加入到普通task队列中taskQueue串行执行;
   具体源码: SingleThreadEventExecutor.runAllTasks(long)
                fetchFromScheduledTaskQueue

##是否使用优化后的Selector的keySet,默认是优化,使用SelectedSelectionKeySet
io.netty.noKeySetOptimization: DISABLE_KEYSET_OPTIMIZATION
源码位置:io.netty.channel.nio.NioEventLoop.openSelector()

##解决netty空轮训的BUG的阀值,默认是512,当轮训次数超过此值,就认为出现轮训BUG,则建立新的一个Selector替换旧的;
io.netty.selectorAutoRebuildThreshold: SELECTOR_AUTO_REBUILD_THRESHOLD
源码位置: io.netty.channel.nio.NioEventLoop.select(boolean oldWakenUp)

###netty使用Thread实现类FastThreadLocalThread代替原生Thread,最重要的其实是InternalThreadLocalMap

###GlobalEventExecutor
###FastThreadLocal
##PoolThreadCache
##PoolArena
##Chunk

                                         +---------------------------+
                                         | Completed successfully    |
                                         +---------------------------+
                                    +---->      isDone() = true      |
    +--------------------------+    |    |   isSuccess() = true      |
    |        Uncompleted       |    |    +===========================+
    +--------------------------+    |    | Completed with failure    |
    |      isDone() = false    |    |    +---------------------------+
    |   isSuccess() = false    |----+---->      isDone() = true      |
    | isCancelled() = false    |    |    |       cause() = non-null  |
    |       cause() = null     |    |    +===========================+
    +--------------------------+    |    | Completed by cancellation |
                                    |    +---------------------------+
                                    +---->      isDone() = true      |
                                         | isCancelled() = true      |
                                         +---------------------------+
                                         
###netty使用的设计模式:
1.单例模式
     DefaultEventExecutorChooserFactory(也是个单例工厂)
     DefaultSelectStrategyFactory
     ReadTimeoutException
     MqttEncoder
2.策略模式:
    线程选择器: io.netty.util.concurrent.DefaultEventExecutorChooserFactory.newChooser
                impl : PowerOfTwoEventExecutorChooser/GenericEventExecutorChooser 根据参数的不同选择对应选择器
3.责任链模式
   ChannelPipeline : 作用在每一个读取数据上
4.迭代器模式
   NioEventLoopGroup  : 线程组,同时也实现Iterable接口
5.观察者模式
   典型的就是ChannelFuture(ChannelFutureListener),也可以说是一种回调
6.工厂模式
  DefaultEventExecutorChooserFactory/ReflectiveChannelFactory
  DefaultThreadFactory
7.模板模式
  抽象父类定义流程,具体实现由子类实现;
    ByteToMessageDecoder(如抽象方法decode)
    ChannelInitializer(如抽象方法initChannel)
    AbstractBootstrap抽象类的init,具体实现由ServerBootstrap/Bootstrap实现;
    SimpleChannelInboundHandler抽象类中的channelRead0抽象方法
8.装饰模式
  装饰类与对装饰类实现同一个类/接口,对装饰类进行扩展,如 
    WrappedByteBuf
    DefaultRunnableDecorator
9.适配器模式
  ChannelInboundHandlerAdapter/ChannelOutboundHandlerAdapter
10.构造器模式/建造者模式
  ServerBootstrap/Bootstrap
11.Reactor模式
   netty的线程模型就是典型的Reactor模式,主从Reactor多线程模型
   Reactor单线程模型:
        new NioEventLoopGroup(1);
   Reactor多线程模型:
        NioEventLoopGroup boss =  new NioEventLoopGroup(1);// Acceptor仅负责接受连接事件
        NioEventLoopGroup work =  new NioEventLoopGroup();// 负责I/O的读写事件
   主从Reactor多线程模型:
        NioEventLoopGroup boss =  new NioEventLoopGroup();// Acceptor仅负责接受连接事件,bind多个端口进行监听连接事件
        NioEventLoopGroup work =  new NioEventLoopGroup();// 负责I/O的读写事件
12.facade模式:
   jdk中的SocketChannel和ServerSocketChannel没有统一的堆外提供的接口,开发难度大
   netty提供统一的对外提供的Channel供开发者使用,将NioServerSocketChannel和NioSocketChannel统一起来;
   如: io.netty.channel.Channel
 
TCP参数: ChannelOption类参数列表
    SO_RCVBUF和SO_SNDBUF:通常建议值为128KB或者256KB;
    SO_TCPNODELAY: NAGLE算法通过将缓冲区内的小封包自动相连，组成较大的封包，阻止大量小封包的发送阻塞网络，从而提高网络应
用效率。但是对于时延敏感的应用场景需要关闭该优化算法;
